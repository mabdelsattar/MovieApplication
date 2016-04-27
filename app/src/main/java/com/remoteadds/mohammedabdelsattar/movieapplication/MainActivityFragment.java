package com.remoteadds.mohammedabdelsattar.movieapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivityFragment extends Fragment implements  OnClickMovie{

    public ProgressDialog pDialog;
    public ArrayList<Movie> movieList;
    GridView gv;

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String sortingWay = settings.getString("SortingWay", "");

        if(sortingWay.equals("toprated"))
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Movies(Top Rated)");
        else if(sortingWay.equals("favorite"))
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Movies(Favorite)");

        else
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Movies(Most Populars)");


        new MovieManager().execute();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(getActivity(), SettingsActivity.class), 1);
        }

        if(id == R.id.action_Refersh)
        {
            new MovieManager().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public   boolean isNetworkConnected(){

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MainView = inflater.inflate(R.layout.fragment_main, container, false);
        gv = (GridView) MainView.findViewById(R.id.gridView1);

        if(!isNetworkConnected()) {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();

                    }
                });

                alertDialog.show();
            } catch (Exception e) {
            }
        }
else {

            new MovieManager().execute();

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onMovieClick(position);


                }
            });
        }
        return MainView;
    }



    @Override
    public void onMovieClick(int position) {
        DetailsActivityFragment detailsFrag = (DetailsActivityFragment) getFragmentManager()
                .findFragmentById(R.id.fragmentdetials);

        if (detailsFrag == null) {

            Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
            intent.putExtra("title", movieList.get(position).title);
            intent.putExtra("date", movieList.get(position).releaseDate);
            intent.putExtra("path", movieList.get(position).poster_path);
            intent.putExtra("movieID", movieList.get(position).ID);
            intent.putExtra("overview", movieList.get(position).overview);
            intent.putExtra("rate", movieList.get(position).vote_average);



            getActivity().startActivity(intent);
        } else {

            detailsFrag.updateContent(movieList.get(position));
        }
    }


    private class MovieManager extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... arg0) {


            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String sortingWay = settings.getString("SortingWay", "");


           if(sortingWay.equals("favorite"))
           {

               ArrayList<MovieContract> favoriteMoviesList=new DataBaseManager().getAllFavoriteMovies();
               movieList=new ArrayList<>();
               for(int i=0 ;i<favoriteMoviesList.size() ; i++){

                   movieList.add(
                           new Movie(favoriteMoviesList.get(i).getmovieID(),
                                   favoriteMoviesList.get(i).getTitle(),
                                   favoriteMoviesList.get(i).getTitle(),
                                   false,
                                   favoriteMoviesList.get(i).getPath(),
                                   favoriteMoviesList.get(i).getPath(),
                                   0.0,
                                   1,
                                   favoriteMoviesList.get(i).getRate(),

                                   favoriteMoviesList.get(i).getOverview(),
                                   false,
                                   favoriteMoviesList.get(i).getDate())
                   );
               }



           }else {
               if (sortingWay.equals("toprated"))
                   sortingWay = "top_rated";
               else
                   sortingWay = "popular";

               HttpURLConnection urlConnection = null;
               BufferedReader reader = null;
               String data = "";
               try {


                   URL url = new URL("http://api.themoviedb.org/3/movie/" + sortingWay + "?api_key=" + BuildConfig.MOVIEW_API);

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
                   parseGrideData(data);
               } catch (Exception e) {
                   Log.d("Background Task", e.toString());
               }
           }
            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            gv.setAdapter(new MovieGrideAdapter(getActivity().getApplicationContext(), movieList));


            DetailsActivityFragment detailsFrag = (DetailsActivityFragment) getFragmentManager()
                    .findFragmentById(R.id.fragmentdetials);


                if(detailsFrag != null)
                detailsFrag.updateContent(movieList.get(0));

        }

        public void parseGrideData(String data) {
            movieList = new ArrayList<>();


            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                JSONObject obj;
                int ID;
                String original_title;
                String title;
                boolean adult;
                String poster_path;
                String backdrop_path;
                double popularity;
                int vote_count;
                double vote_average;
                String overview;
                boolean video;
                String releaseDate;

                for (int i = 0; i < jsonArray.length(); i++) {
                    obj = jsonArray.getJSONObject(i);
                    ID = obj.getInt("id");
                    original_title = obj.getString("original_title");
                    title = obj.getString("title");
                    adult = obj.getBoolean("adult");
                    poster_path = obj.getString("poster_path");

                    backdrop_path = obj.getString("backdrop_path");
                    popularity = obj.getDouble("popularity");
                    vote_count = obj.getInt("vote_count");
                    vote_average = obj.getDouble("vote_average");
                    overview = obj.getString("overview");
                    video = obj.getBoolean("video");
releaseDate=obj.getString("release_date");
                    movieList.add(new Movie(ID, original_title, title, adult, poster_path, backdrop_path, popularity, vote_count, vote_average, overview, video,releaseDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


}
