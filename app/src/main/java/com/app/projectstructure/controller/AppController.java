package com.app.projectstructure.controller;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class AppController extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LPBrmY8uV3mXhVIQXBYOR11Jo";
    private static final String TWITTER_SECRET = "vHX5Hojdvx1nWQl8s7y8OuwaP9r1WNabMGBeW49PztBGDwKpBv";


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
