package com.remoteadds.mohammedabdelsattar.movieapplication;

import android.app.Application;

import com.activeandroid.ActiveAndroid;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        try {

            ActiveAndroid.initialize(this);
        }catch (Exception ex)
        {


        }

    }
}
