package com.hlibrary.util.download;

import java.io.File;

/**
 * 更新apk的监听
 *
 * @author shz
 * @date 2019/8/28
 */
public interface OnUpdateCallback {

    /**
     * 下载失败
     *
     * @param msg 失败信息
     */
    void onFailed(String msg);

    /**
     * 下载成功
     *
     * @param apkFile 成功信息
     */
    void onSucceed(File apkFile);

    /**
     * 下载进度
     *
     * @param total    总的apk大小
     * @param current  当前进度
     * @param progress 进度百分比
     */
    void onProgress(int total, int current, float progress);

}
