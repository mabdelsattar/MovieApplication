package com.remoteadds.mohammedabdelsattar.movieapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TrailerCustom_Adapter extends BaseAdapter{

        Context context;
ArrayList<TrailerItemData> trailersData;
        private static LayoutInflater inflater=null;


        public TrailerCustom_Adapter(Context context, ArrayList<TrailerItemData> trailersData) {

            this.trailersData=trailersData;
            this.context=context;

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return trailersData.size();
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
            TextView tvName;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ReviewHolder holder=new ReviewHolder();

            if(convertView == null)
                convertView = inflater.inflate(R.layout.trailer_list_item, null);
            holder.tvName=(TextView) convertView.findViewById(R.id.tvTrailerName);

            holder.tvName.setText(trailersData.get(position).Name);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailersData.get(position).URL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });


            return convertView;

        }

}
