package com.example.back4app.twitterlogin;

import com.parse.Parse;
import com.parse.twitter.ParseTwitterUtils;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if desired
                .clientKey(getString(R.string.back4app_client_key))
                .server("https://parseapi.back4app.com/")
                .build()
        );

        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
    }
}
