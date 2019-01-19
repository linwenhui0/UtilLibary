package com.hdlang.util.test;

import android.app.Application;

import com.hlibrary.util.Logger;
import com.hlibrary.util.crash.AppCrashHandler;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCrashHandler.getInstance();
        Logger.getInstance().setPackageName(this);
    }

}
