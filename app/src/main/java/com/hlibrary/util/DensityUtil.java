package com.hlibrary.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;

public class DensityUtil {

    private DensityUtil() {
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    /**
     * @param application
     */
    public static void setApplicationDensity(@NonNull final Application application) {
        Class cls = application.getClass();
        int staticDPI = 360;
        try {
            Method method = cls.getDeclaredMethod("getStaticDPI");
            staticDPI = (int) method.invoke(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getInstance().defaultTagD(" == setCustomDensity == ", staticDPI);
        setApplicationDensity(application, staticDPI);
    }

    /**
     * @param application
     * @param staticPixels 默认为360(1280x720)
     */
    public static void setApplicationDensity(@NonNull final Application application, int staticPixels) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / staticPixels;
        final float targetScaleDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);
        Logger.getInstance().defaultTagD(" == density == ", appDisplayMetrics.density, " : ", targetDensity,
                " == scaledDensity == ", appDisplayMetrics.scaledDensity, " : ", targetScaleDensity,
                " == densityDpi == ", appDisplayMetrics.densityDpi, " : ", targetDensityDpi);
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

    }

    /**
     * @param application
     */
    public static void setActivityDensity(@NonNull final Application application, Activity activity) {
        Class cls = application.getClass();
        int staticDPI = 360;
        try {
            Method method = cls.getDeclaredMethod("getStaticDPI");
            staticDPI = (int) method.invoke(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getInstance().defaultTagD(" == setActivityDensity == ", staticDPI);
        setActivityDensity(application, activity, staticDPI);
    }

    /**
     * @param application
     * @param staticPixels 默认为360(1280x720)
     */
    public static void setActivityDensity(@NonNull final Application application, Activity activity, int staticPixels) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / staticPixels;
        final float targetScaleDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);
        Logger.getInstance().defaultTagD(" appDisplayMetrics == density == ", appDisplayMetrics.density, " : ", targetDensity,
                " == scaledDensity == ", appDisplayMetrics.scaledDensity, " : ", targetScaleDensity,
                " == densityDpi == ", appDisplayMetrics.densityDpi, " : ", targetDensityDpi);
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        Logger.getInstance().defaultTagD(" activityDisplayMetrics == density == ", activityDisplayMetrics.density, " : ", targetDensity,
                " == scaledDensity == ", activityDisplayMetrics.scaledDensity, " : ", targetScaleDensity,
                " == densityDpi == ", activityDisplayMetrics.densityDpi, " : ", targetDensityDpi);
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;

    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
