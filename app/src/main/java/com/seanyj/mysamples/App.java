package com.seanyj.mysamples;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class App extends Application {

    private static App sInstance;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
