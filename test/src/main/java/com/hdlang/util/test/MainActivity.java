package com.hdlang.util.test;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.EnvironmentCompat;

import com.alibaba.fastjson.JSON;
import com.hlibrary.util.AbstractFaceConversion;
import com.hlibrary.util.ApkInfoUtil;
import com.hlibrary.util.HexUtil;
import com.hlibrary.util.Logger;
import com.hlibrary.util.PermissionGrant;
import com.hlibrary.util.PermissionManager;
import com.hlibrary.util.SIMCardInfo;
import com.hlibrary.util.Utils;
import com.hlibrary.util.command.CommandTool;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private PermissionManager permissionManager;
    private TextView txtvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtvw = findViewById(R.id.txtvw);
        Logger.Companion.getInstance().defaultTagD("MainActivity");
        NutDB nutDB = NutDB.getInstance(this);
        SdcardUserEntity sdcardUserEntity = new SdcardUserEntity();
        sdcardUserEntity.setUser("18606064557");
        sdcardUserEntity.setUserId("1");
        sdcardUserEntity.setAutoLogin(false);
        sdcardUserEntity.setCompanyId("2");
        sdcardUserEntity.setPasswd("111111");
        nutDB.addUser(sdcardUserEntity);
//        nutDB.updateUserWithNextAccountId("1","");
        sdcardUserEntity = nutDB.getUserById("1");
        Logger.Companion.getInstance().defaultTagI(sdcardUserEntity);

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
                    Logger.Companion.getInstance().defaultTagD("onPermissionGranted = ", permission);
                    txtvw.setText("onPermissionGranted " + permission);
                }

                @Override
                public void onPermissionError(@NotNull Exception e) {
                    Logger.Companion.getInstance().defaultTagD("onPermissionError = ", e.toString());
                    txtvw.setText("onPermissionError " + e.toString());
                }
            });
        }
//        permissionManager.requestPermission(10, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionManager.requestMultiPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.BLUETOOTH, Manifest.permission.READ_PHONE_STATE});
    }

    public void onFaceClick(View v) {
        AbstractFaceConversion faceConversion = AbstractFaceConversion.getInstance("\\[[^\\]]+\\]");
        boolean result = faceConversion.addFace(R.mipmap.ic_launcher, "[群主]");
        String text = "我是[群主]群主";
        SpannableStringBuilder textString = faceConversion.getExpressionString(this, text);
        txtvw.setText(textString);
    }

    public void onCommandClick(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new CommandTool().execCommand(new String[]{"monkey -p com.tencent.mm -v  -f /sdcard/monkey.script 1"}, false);
                } catch (Exception e) {
                    Logger.Companion.getInstance().defaultTagE(e.toString());
                }

            }
        }).start();

    }

    public void onLogClick(View v) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Logger.Companion.getInstance().d("dd", "日志");
        Logger.Companion.getInstance().defaultTagD("日志测试 " + format.format(System.currentTimeMillis()));
    }

    public void onClearLogClick(View v) {
        Logger.Companion.getInstance().clearLog();
    }

    public void onByteToString(View v) {
        Logger.Companion.getInstance().d(TAG, HexUtil.INSTANCE.hexStringToByte("1234"), Logger.TYPE.CODE16);
        Logger.Companion.getInstance().d(TAG, HexUtil.INSTANCE.int2bytes(120), Logger.TYPE.CODE16);
        Logger.Companion.getInstance().defaultTagD(HexUtil.INSTANCE.bytesToHexString(new byte[]{1, 2, 3, 14}));
        Logger.Companion.getInstance().defaultTagD(HexUtil.INSTANCE.formatLeftAlign("1234", 6, "9"));
    }

    public void onDevideId(View v) {
        String id = new SIMCardInfo(this).getImsi();
        txtvw.setText("生成id: " + id);
        Logger.Companion.getInstance().defaultTagD("生成id: ", id);

    }

    public void onProxyIP(View v) {
        String id = Utils.INSTANCE.getProxyHost(this);
        txtvw.setText("代理ip : " + id);
        Logger.Companion.getInstance().defaultTagD("代理ip : ", id);

    }

    public void onProxyPort(View v) {
        String id = Utils.INSTANCE.getProxyPort(this);
        txtvw.setText("代理端口 : " + id);
        Logger.Companion.getInstance().defaultTagD("代理端口 : ", id);

    }

    public void onCalSign(View v) {
        String k = ApkInfoUtil.INSTANCE.getAppSignature(this,"MD5");
        Logger.Companion.getInstance().defaultTagD("cal ",k);
    }

    private String deviceId() {
        String str = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.HOST.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
        String obj;
        try {
            obj = Build.class.getField("SERIAL").get(null).toString();
            if (EnvironmentCompat.MEDIA_UNKNOWN.equalsIgnoreCase(obj) || "null".equalsIgnoreCase(obj)) {
                obj = UUID.randomUUID().toString();
            }
            return new UUID((long) str.hashCode(), (long) obj.hashCode()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            obj = UUID.randomUUID().toString();
            return new UUID((long) str.hashCode(), (long) obj.hashCode()).toString();
        }
    }

    public static String getAndroidId(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        return ANDROID_ID;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
