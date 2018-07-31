package com.hdlang.util.test;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.hlibrary.util.DensityUtil;
import com.hlibrary.util.Logger;
import com.hlibrary.util.PermissionGrant;
import com.hlibrary.util.PermissionManager;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private PermissionManager permissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DensityUtil.setCustomDensity(this, getApplication(), 360);
        setContentView(R.layout.activity_main);
        Logger.getInstance().setPackageName(this);
        permissionManager = new PermissionManager(this, new PermissionGrant() {
            @Override
            public void onPermissionGranted(@NotNull String permission) {
                Logger.getInstance().defaultTagD("onPermissionGranted = ", permission);
            }

            @Override
            public void onPermissionError(@NotNull Exception e) {
                Logger.getInstance().defaultTagD("onPermissionError = ", e.toString());
            }
        });
        permissionManager.requestPermission(10, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permissionManager.requestMultiPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
