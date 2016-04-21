package com.remoteadds.mohammedabdelsattar.movieapplication;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Abdelsattar on 4/14/2016.
 */
public class DataBaseManager  {

    public DataBaseManager() {

    }
    public DataBaseManager(ArrayList<MovieContract> movieList) {
        this.movieList = movieList;
    }

    public ArrayList<MovieContract> getMovieList() {
        return movieList;
    }


    public  boolean isFavorite(int ID){
        List<MovieContract> tempList=  new Select()
                .from(MovieContract.class)
                        .where("movieID = ?", ID)
                .execute();

        int size = tempList.size();
       return !tempList.isEmpty();

    }
    public void setMovieList(ArrayList<MovieContract> movieList) {
        this.movieList = movieList;
    }

    public void Save(){
        for(MovieContract mov: movieList)
            mov.save();

    }


    public  void clearmovieTable(){
        new Delete().from(MovieContract.class).execute();



    }
    public ArrayList<MovieContract> getAllFavoriteMovies()
    {


        ArrayList<MovieContract> movieContracts=new ArrayList<>();
        List<MovieContract> tempList=  new Select()
                .from(MovieContract.class)
                .execute();

        for(int i=0 ; i<tempList.size() ; i++)
        {

            movieContracts.add(tempList.get(i));
        }
        return  movieContracts;




    }

    ArrayList<MovieContract> movieList=new ArrayList<MovieContract>();


}
