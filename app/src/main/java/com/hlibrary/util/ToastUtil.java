package com.hlibrary.util;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastUtil {


    private static ToastUtil instance;
    private static WeakReference<Toast> mWeakToast;

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        if (instance == null)
            synchronized (ToastUtil.class) {
                if (instance == null)
                    instance = new ToastUtil();
            }
        return instance;
    }

    public synchronized void showToast(@NonNull Context context, @NonNull String text, int duration) {
        Toast mToast = null;
        if (mWeakToast != null)
            mToast = mWeakToast.get();
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
            mWeakToast = null;
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        if (mWeakToast == null)
            mWeakToast = new WeakReference<>(mToast);
        mToast.show();
    }

    public synchronized void showToast(@NonNull Context context, @StringRes int resId, int duration) {
        Toast mToast = null;
        if (mWeakToast != null)
            mToast = mWeakToast.get();
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, duration);
            mWeakToast = null;
        } else {
            mToast.setText(resId);
            mToast.setDuration(duration);
        }
        if (mWeakToast == null)
            mWeakToast = new WeakReference<>(mToast);
        mToast.show();
    }

    public static void showLongTime(@NonNull Context context, @NonNull String text) {
        ToastUtil.getInstance().showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showShortTime(@NonNull Context context, @NonNull String text) {
        ToastUtil.getInstance().showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showCustomTime(@NonNull Context context, @NonNull String text, @IntRange(from = 1) int duration) {
        if (duration > 0)
            ToastUtil.getInstance().showToast(context, text, duration);
    }

    public static void showLongTime(@NonNull Context context, @StringRes int resId) {
        ToastUtil.getInstance().showToast(context, resId, Toast.LENGTH_LONG);
    }

    public static void showShortTime(@NonNull Context context, @StringRes int resId) {
        ToastUtil.getInstance().showToast(context, resId, Toast.LENGTH_SHORT);
    }

    public static void showCustomTime(@NonNull Context context, @StringRes int resId, @IntRange(from = 1) int duration) {
        ToastUtil.getInstance().showToast(context, resId, duration);
    }

}
