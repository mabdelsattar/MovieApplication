package com.remoteadds.mohammedabdelsattar.movieapplication;

import com.activeandroid.annotation.Column;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SharedPreferenceManager {
    public String ArrayToJson(ArrayList<MovieContract> movieContracts) {
        return new JSONArray(movieContracts).toString();

    }

    public ArrayList<MovieContract> JsonToArray(String jsonStr) {
        JSONArray arr = null;

        ArrayList<MovieContract> movies = new ArrayList<>();
        try {
            arr = new JSONArray(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (arr != null) {
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    int movieID = jsonObject.getInt("movieID");
                    String title = jsonObject.getString("title");
                    String path = jsonObject.getString("path");
                    String overview = jsonObject.getString("overview");
                    String date = jsonObject.getString("date");
                    int rate = jsonObject.getInt("rate");
                    movies.add(new MovieContract(movieID, title, path, overview, date, rate));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
        return  movies;
    }
}






