package com.remoteadds.mohammedabdelsattar.movieapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public static  ProgressDialog pDialog;
   static String  path;
    static String   title;
    static String date;
    static ImageView imView;
    static TextView tvTitle ;
    static TextView tvReleaseDate;

    public  static  boolean isUpdated=false;
    static ArrayList<ReviewItemData> reviewsData;
   static ArrayList<TrailerItemData> trailersData;

    public DetailsActivityFragment() {
        reviewsData=new ArrayList<ReviewItemData>();
        trailersData=new ArrayList<TrailerItemData>();

    }
    static  int movieID;
  static   ListView listViewReviews;
  static   ListView listViewTrailers;
  static    Button btnFavorite;

public  static Activity myActivity;

    public static  void updateContent(Movie movie){
       isUpdated=true;
        path = movie.poster_path;
        title = movie.original_title;
        date = movie.releaseDate;
        movieID = movie.ID;

        boolean isFavorite= new DataBaseManager().isFavorite(movieID);
        if(isFavorite)
            btnFavorite.setEnabled(false);
        tvTitle.setText(title);
        tvReleaseDate.setText(date);

        Picasso.with(myActivity.getApplicationContext())
                .load("http://image.tmdb.org/t/p/w342"+path)
                .into(imView);


        new ReviewsManager().execute();







    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_details, container, false);


        myActivity=getActivity();
        listViewReviews = (ListView) V.findViewById(R.id.lstReview);
        listViewTrailers = (ListView) V.findViewById(R.id.lstTrailer);

         imView=(ImageView)V.findViewById(R.id.dImg);
         tvTitle =(TextView)V.findViewById(R.id.dOriginalTitle);
         tvReleaseDate =(TextView)V.findViewById(R.id.dReleaseDate);
        tvTitle.setTextAppearance(getActivity().getApplicationContext(), android.R.style.TextAppearance_Large);
btnFavorite=(Button)V.findViewById(R.id.btnAddFavorite);



try {
    path = getActivity().getIntent().getExtras().getString("path");
    title = getActivity().getIntent().getExtras().getString("title");
    date = getActivity().getIntent().getExtras().getString("date");
    movieID = getActivity().getIntent().getExtras().getInt("movieID");
}catch (Exception ex)
{
    path = "http://markmeets.com/wp-content/uploads/2013/10/Movie-Releases.jpg";
    title = "";
    date = "";
    movieID = 1;

}


        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavorite.setEnabled(false);

                ArrayList<MovieContract> movieList = new ArrayList<MovieContract>();
                movieList.add(new MovieContract(movieID, title, path, "", date, 0));
                new DataBaseManager(movieList).Save();



            }
        });

        boolean isFavorite= new DataBaseManager().isFavorite(movieID);
        if(isFavorite)
            btnFavorite.setEnabled(false);

        if(!isUpdated) {
            tvTitle.setText(title);
            tvReleaseDate.setText(date);

            Picasso.with(getActivity().getApplicationContext())
                    .load("http://image.tmdb.org/t/p/w342" + path)
                    .into(imView);

            new ReviewsManager().execute();


        }


        return V;
    }





    private static class TrailersManager extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(myActivity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String data = "";
            try {


                URL url = new URL( "http://api.themoviedb.org/3/movie/"+movieID+"/videos?api_key="+BuildConfig.MOVIEW_API);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }


                data = buffer.toString();
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            parseTrailersData(result);

            listViewTrailers.setAdapter(new TrailerCustom_Adapter(myActivity.getApplicationContext(), trailersData));


            setListViewHeightBasedOnChildren(listViewTrailers);
        }

        public void parseReviewsData(String data) {


            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject obj;
                String author;
                String content;


                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = jsonArray.getJSONObject(i);
                    author = obj.getString("author");
                    content = obj.getString("content");
                    reviewsData.add(new ReviewItemData(author,content));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        public void parseTrailersData(String data) {


            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject obj;
                String Name;
                String URL;


                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = jsonArray.getJSONObject(i);
                    Name = obj.getString("name");
                    URL =  "https://youtu.be/"+obj.getString("key");
                    trailersData.add(new TrailerItemData(Name,URL));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }




    private static class ReviewsManager extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(myActivity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String data = "";
            try {


                URL url = new URL( "http://api.themoviedb.org/3/movie/"+movieID+"/reviews?api_key="+BuildConfig.MOVIEW_API);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }


                data = buffer.toString();
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            parseReviewsData(result);

            listViewReviews.setAdapter(new RevCustom_Adapter(myActivity.getApplicationContext(), reviewsData));

            setListViewHeightBasedOnChildren(listViewReviews);

            new TrailersManager().execute();
        }

        public void parseReviewsData(String data) {


            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject obj;
                String author;
                String content;


                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = jsonArray.getJSONObject(i);
                    author = obj.getString("author");
                    content = obj.getString("content");
                    reviewsData.add(new ReviewItemData(author,content));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


}
