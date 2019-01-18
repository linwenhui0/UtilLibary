package com.hdlang.util.test;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hlibrary.util.FaceConversion;
import com.hlibrary.util.Logger;
import com.hlibrary.util.PermissionGrant;
import com.hlibrary.util.PermissionManager;
import com.hlibrary.util.command.CommandResult;
import com.hlibrary.util.command.CommandTool;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private PermissionManager permissionManager;
    private TextView txtvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtvw = findViewById(R.id.txtvw);
        Logger.getInstance().defaultTagD("MainActivity");
    }

    public void onRequestClick(View v) {
        if (permissionManager == null) {
            permissionManager = new PermissionManager(this, new PermissionGrant() {
                @Override
                public void onPermissionDenied(@NotNull ArrayList<String> permissions) {
                    txtvw.setText("onPermissionDenied " + JSON.toJSONString(permissions));
                }

                @Override
                public void onPermissionGranted(@NotNull String permission) {
                    Logger.getInstance().defaultTagD("onPermissionGranted = ", permission);
                    txtvw.setText("onPermissionGranted " + permission);
                }

                @Override
                public void onPermissionError(@NotNull Exception e) {
                    Logger.getInstance().defaultTagD("onPermissionError = ", e.toString());
                    txtvw.setText("onPermissionError " + e.toString());
                }
            });
        }
        permissionManager.requestPermission(10, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permissionManager.requestMultiPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA});
    }

    public void onFaceClick(View v) {
        FaceConversion faceConversion = FaceConversion.getInstance("\\[[^\\]]+\\]");
        boolean result = faceConversion.addFace(R.mipmap.ic_launcher, "[群主]");
        String text = "我是[群主]群主";
        SpannableStringBuilder textString = faceConversion.getExpressionString(this, text);
        txtvw.setText(textString);
    }

    public void onCommandClick(View v) {
        try {
            CommandResult result = new CommandTool().execCommand(new String[]{"ls"}, false);
            Logger.getInstance().defaultTagD(result);
        } catch (Exception e) {
        }
    }

    public void onLogClick(View v) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Logger.getInstance().defaultTagD("日志测试 " + format.format(System.currentTimeMillis()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
