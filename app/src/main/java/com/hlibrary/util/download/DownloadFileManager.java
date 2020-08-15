package com.hlibrary.util.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.hlibrary.util.Utils;
import com.hlibrary.util.encrypt.Md5Util;
import com.hlibrary.util.file.SdUtil;

import java.io.File;

/**
 * 下载更新APP的工具类
 *
 * @author shz
 * @date 2019/8/28
 */
public class DownloadFileManager {
    private static final String CONFIG = "APK_UPDATE";
    private static final String DOWNLOADED = "DOWNLOADED";
    private final SharedPreferences mPreferences;
    // 下载器
    private android.app.DownloadManager downloadManager;
    // 下载的ID
    private long downloadId = -1;
    private Context context;
    private OnUpdateCallback mOnUpdateListener;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public DownloadFileManager(Context context) {
        this.context = context;
        mPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    }

    /**
     * 注意：要自己添加内存卡读取权限
     * 下载apk主要方法
     */
    public void downloadAPK(String url, String title) {
        // 2. URL校验
        if (!URLUtil.isNetworkUrl(url)) {
            if (mOnUpdateListener != null) {
                mOnUpdateListener.onFailed("APK下载地址不正确");
            }
            return;
        }
        // 1. 在这里要做一下校验
        File apkFile = getApkFile(url);
//        // 判断有没有下载成功
        if (apkFile.exists()) {
            if (downloadManager == null) {
                downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            }
            DownloadManager.Query query = new DownloadManager.Query();
            // 通过下载的id查找
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = downloadManager.query(query);
            boolean downloadStatus = false;
            while (cursor.moveToNext()) {
                String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                if (url.equals(uri)) {
                    downloadStatus = true;
                    break;
                }
            }
            if (downloadStatus) {
                if (mOnUpdateListener != null) {
                    mOnUpdateListener.onSucceed(getApkFile(url));
                }
                return;
            }
        }
        // 2. 删除APK
        deleteApkFile(apkFile);
        // 3. 创建下载任务
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));
        // 移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        request.allowScanningByMediaScanner();
        // 在通知栏中显示，默认就是显示的,优化成下载完成还有提示，并且可以点击
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(title);
        request.setDescription("版本更新");
        request.setVisibleInDownloadsUi(true);
        //4. 设置下载的路径
        request.setDestinationUri(Uri.fromFile(getApkFile(url)));
        // 设置该属性能解决打开下载的APK就报“无法打开文件”错误的问题
        request.setMimeType("application/vnd.android.package-archive");
        // 获取DownloadManager
        if (downloadManager == null) {
            downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        // 将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);
        }
        // 注册广播接收者，监听下载完成状态
        context.registerReceiver(receiver, new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if (mOnUpdateListener != null) {
            // 开启个任务去每秒查询下载进度
            mHandler.postDelayed(mTask, 1000);
        }
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (mOnUpdateListener != null) {
                checkStatus();
                mHandler.postDelayed(mTask, 1000);
            }
        }
    };

    /**
     * 手动删除原来的APK，下载器不会覆盖。
     */
    private void deleteApkFile(File apkFile) {
        try {
            putDownload(false);
            if (apkFile.exists()) {
                apkFile.delete();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    /**
     * 广播监听下载完成
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 下载完成会调一次这里
            checkStatus();
        }
    };

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        if (downloadId == -1) {
            return;
        }
        DownloadManager.Query query = new DownloadManager.Query();
        // 通过下载的id查找
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
            try {
                // 如果有下载监听就去查询文件下载进度
                if (mOnUpdateListener != null) {
                    //已经下载文件大小
                    int current = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    //下载文件的总大小
                    int total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (total >= 0 && current >= 0 && mOnUpdateListener != null) {
                        // 更新进度
                        float progress = current * 100f / total;
                        mOnUpdateListener.onProgress(total, current, progress);
                    }
                }
            } catch (Exception e) {
            }
            switch (status) {
                // 下载暂停
                case android.app.DownloadManager.STATUS_PAUSED:
                    break;
                // 下载延迟
                case android.app.DownloadManager.STATUS_PENDING:
                    break;
                // 正在下载
                case android.app.DownloadManager.STATUS_RUNNING:
                    break;
                // 下载完成
                case android.app.DownloadManager.STATUS_SUCCESSFUL:
                    mHandler.removeCallbacks(mTask);
                    // 下载完成安装APK
                    putDownload(true);
                    // 有监听让用户去做
                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onSucceed(getApkFile(uri));
                    }
                    cursor.close();
                    try {
                        context.unregisterReceiver(receiver);
                    } catch (Exception e) {
                    }

                    break;
                // 下载失败
                case android.app.DownloadManager.STATUS_FAILED:
                    mHandler.removeCallbacks(mTask);
                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onFailed("下载失败");
                    }
                    cursor.close();
                    try {
                        context.unregisterReceiver(receiver);
                    } catch (Exception e) {
                    }
                    break;
            }
        }
    }


    /**
     * 文件下载的路径
     */
    private File getApkFile(String url) {
        File directory = null;
        if (SdUtil.INSTANCE.existSDCard()) {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        if (directory == null) {
            directory = context.getCacheDir();
        }
        String fileName = Md5Util.INSTANCE.digest(url);
        if (TextUtils.isEmpty(fileName)) {
            fileName = "update";
        }
        return new File(directory, String.format("%s.apk", fileName));
    }


    /**
     * 缓存下载信息
     */
    private void putDownload(boolean isDownload) {
        if (mPreferences == null) {
            return;
        }
        mPreferences.edit().putBoolean(DOWNLOADED, isDownload).apply();
    }

    /**
     * 有没有下载完成
     */
    private boolean isDownload() {
        if (mPreferences == null) {
            return false;
        }
        return mPreferences.getBoolean(DOWNLOADED, false);
    }

    public void setOnUpdateListener(OnUpdateCallback onUpdateListener) {
        mOnUpdateListener = onUpdateListener;
    }

    /**
     * 如果设置了下载进度回调，在Activity 的OnDestroy方法调用 一下。
     */
    public void stop() {
        try {
            mOnUpdateListener = null;
            mHandler.removeCallbacks(mTask);
            context.unregisterReceiver(receiver);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
