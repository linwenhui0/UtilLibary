package com.hdlang.util.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hlibrary.util.Logger;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            writeLogTest();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int len = grantResults.length;
            for (int i = 0; i < len; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        writeLogTest();
                        break;
                    }
                }
            }
        }
    }

    private void writeLogTest() {
        Log.i(TAG, "start write log test");
        Logger.getInstance().setDEBUG(false, true);
        Logger.getInstance().setPackageName(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < 100; j++)
                        Logger.getInstance().defaultTagD("qazwsxedcrfvtgbyhnujmik,ol.p;/[']1234567890-=",true);
                }
                Log.i(TAG, "write finish");
            }
        }).start();

    }
}
