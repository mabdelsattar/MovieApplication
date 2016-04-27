package com.remoteadds.mohammedabdelsattar.movieapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class MovieGrideAdapter extends BaseAdapter {

ArrayList<Movie> moviesData;
        Context context;
        private static LayoutInflater inflater=null;

        public MovieGrideAdapter(Context mainActivity, ArrayList<Movie> moviesData) {
            // TODO Auto-generated constructor stub
            this.moviesData=moviesData;
            context=mainActivity;
            inflater = (LayoutInflater)context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return moviesData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

       public class MYtag
       {
        ImageView img;
       }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.grid_item, null);
                MYtag mYtag=new MYtag();

                mYtag.img=(ImageView) convertView.findViewById(R.id.imageView1);



                Picasso.with(context)
                        .load("http://image.tmdb.org/t/p/w342"+moviesData.get(position).poster_path)
                        .into(mYtag.img);
                convertView.setTag(mYtag);
            }

else {

                MYtag mymy= (MYtag) convertView.getTag();

 Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185"+moviesData.get(position).poster_path)
                .into(mymy.img);


            }
            return convertView;
        }


}
