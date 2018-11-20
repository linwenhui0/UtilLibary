package com.hdlang.util.test;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hlibrary.util.DensityUtil;
import com.hlibrary.util.FaceConversion;
import com.hlibrary.util.Logger;
import com.hlibrary.util.PermissionGrant;
import com.hlibrary.util.PermissionManager;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private PermissionManager permissionManager;
    private TextView txtvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtvw = findViewById(R.id.txtvw);

    }

    public void onRequestClick(View v) {
        if (permissionManager == null)
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
    }

    public void onFaceClick(View v) {
        FaceConversion faceConversion = FaceConversion.getInstance("\\[[^\\]]+\\]");
        boolean result = faceConversion.addFace(R.mipmap.ic_launcher, "[群主]");
        String text = "我是[群主]群主";
        SpannableStringBuilder textString = faceConversion.getExpressionString(this,text);
        txtvw.setText(textString);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
