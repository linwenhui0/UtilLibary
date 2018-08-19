package com.hdlang.util.test;

import android.app.Application;

import com.hlibrary.util.DensityUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DensityUtil.setApplicationDensity(this);
    }

    public int getStaticDPI() {
        return 240;
    }
}
