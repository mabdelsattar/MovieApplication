package com.remoteadds.mohammedabdelsattar.movieapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RevCustom_Adapter extends BaseAdapter{

        Context context;
        ArrayList<ReviewItemData> reviews;
        private static LayoutInflater inflater=null;


        public RevCustom_Adapter(Context context,ArrayList<ReviewItemData> reviews) {

            this.reviews=reviews;
            this.context=context;

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return reviews.size();
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

        public class ReviewHolder
        {
            TextView tvContent;
            TextView tvAuthor;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ReviewHolder holder=new ReviewHolder();

            if(convertView == null)
                convertView = inflater.inflate(R.layout.review_list_item, null);
            holder.tvAuthor=(TextView) convertView.findViewById(R.id.tvAuthor);
            holder.tvContent=(TextView) convertView.findViewById(R.id.tvContent);

            holder.tvAuthor.setText(reviews.get(position).author);
            holder.tvContent.setText(reviews.get(position).content);


            return convertView;

        }

}
