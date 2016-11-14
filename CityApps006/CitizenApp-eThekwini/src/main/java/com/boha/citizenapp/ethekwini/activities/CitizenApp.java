package com.boha.citizenapp.ethekwini.activities;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.boha.library.dto.MunicipalityDTO;
import com.boha.library.util.SharedUtil;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.fabric.sdk.android.Fabric;

/**
 * Created by aubreymalabie on 9/29/16.
 */

public class CitizenApp extends Application {
    public static final String TAG = CitizenApp.class.getSimpleName();
    public static Picasso picasso;
    static final long MAX_CACHE_SIZE = 1024 * 1024 * 1024; // 1 GB cache on device

    @Override
    public void onCreate() {
        super.onCreate();

        StringBuilder sb = new StringBuilder();
        sb.append("####################################################\n");
        sb.append("########## SMARTCITY CitizenApp Starting ... ##########\n");
        sb.append("####################################################\n");
        Log.d(TAG, "onCreate: \n" + sb.toString());

        Firebase.setAndroidContext(getApplicationContext());
        Log.e(TAG, "onCreate: ####### Firebase Android context set" );
        Fabric.with(this, new Crashlytics());
        //
        MunicipalityDTO m = SharedUtil.getMunicipality(getApplicationContext());
        boolean isDebuggable = 0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
        // create Picasso.Builder object
        File picassoCacheDir = getCacheDir();
        Log.w(TAG, "####### images in picasso cache: " + picassoCacheDir.listFiles().length);
        Picasso.Builder picassoBuilder = new Picasso.Builder(getApplicationContext());
        picassoBuilder.downloader(new OkHttpDownloader(picassoCacheDir, MAX_CACHE_SIZE));
        picasso = picassoBuilder.build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }
        if (isDebuggable) {
            Picasso.with(getApplicationContext())
                    .setIndicatorsEnabled(false);
            Picasso.with(getApplicationContext())
                    .setLoggingEnabled(true);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
