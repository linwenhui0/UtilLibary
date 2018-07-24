package com.hlibrary.util.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.hlibrary.util.BitmapDecoder;
import com.hlibrary.util.Logger;
import com.hlibrary.util.SDUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    private static final String TAG = "FileManager";
    private static final String PICTURE_CACHE = "picture_cache";
    private static final String PICTURE_MIN_CACHE = "picture_min_cache";
    private static final String LOG = "log";
    /**
     * install weiciyuan, open app and login in, Android system will create
     * cache dir. then open cache dir (/sdcard
     * dir/Android/data/org.qii.weiciyuan) with Root Explorer, uninstall
     * weiciyuan and reinstall it, the new weiciyuan app will have the bug it
     * can't read cache dir again, so I have to tell user to delete that cache
     * dir
     */
    private static volatile boolean cantReadBecauseOfAndroidBugPermissionProblem = false;

    /**
     * @param context
     * @return 如果存在SD卡返回SD路径，不存在SD卡返回内部访问空间路径
     */
    public static String getSdCardPath(Context context) {
        if (SDUtil.ExistSDCard()) {
            File path = Environment.getExternalStorageDirectory();
            if (path != null) {
                return path.getAbsolutePath();
            }
        }
        return context.getCacheDir().getAbsolutePath();
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName);
        if (!file.mkdirs()) {
            Logger.getInstance().e(TAG, "Directory not created");
        }
        return file;
    }


    public static String getUploadPicTempFile(Context context) {
        String path;
        if (!SDUtil.ExistSDCard()) {
            path = context.getCacheDir().getAbsolutePath() + File.separator + "upload" + File.separator + "upload.jpg";
        } else {
            path = getSdCardPath(context) + File.separator + "Android" + File.separator + "data"
                    + File.separator + context.getPackageName() + File.separator + "upload.jpg";
        }
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        } else {
            File parentFile = file.getParentFile();
            if (parentFile.exists() && parentFile.isFile()) {
                parentFile.delete();
                parentFile.mkdirs();
            } else if (!parentFile.exists())
                parentFile.mkdirs();
        }
        return path;
    }

    public static String generateDownloadFileName(Context context, String url) {
        if (!SDUtil.ExistSDCard()) {
            return "";
        }
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String path = String.valueOf(url.hashCode());
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append(getSdCardPath(context));
        resultBuffer.append(File.separator);
        resultBuffer.append(PICTURE_CACHE);
        resultBuffer.append(File.separator);
        resultBuffer.append(path);
        if (url.endsWith(".jpg")) {
            resultBuffer.append(".jpg");
        } else if (url.endsWith(".gif")) {
            resultBuffer.append(".gif");
        }
        String result = resultBuffer.toString();
        if (!result.endsWith(".jpg") && !result.endsWith(".gif")
                && !result.endsWith(".png")) {
            resultBuffer.append(".jpg");
        }

        return resultBuffer.toString();

    }

    public static File createNewFileInSDCard(String absolutePath) {
        if (!SDUtil.ExistSDCard()) {
            Logger.getInstance().e(TAG, "sdcard unavailiable");
            return null;
        }

        if (TextUtils.isEmpty(absolutePath)) {
            return null;
        }

        File file = new File(absolutePath);
        if (file.exists()) {
            return file;
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                Logger.getInstance().d(TAG, e.getMessage());
                return null;

            }

        }
        return null;

    }

    public static String getPictureMinCachePath(Context mCtx) {
        String path = getSdCardPath(mCtx) + File.separator + "Android"
                + File.separator + "data" + File.separator + mCtx.getPackageName()
                + File.separator + PICTURE_MIN_CACHE + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getPictureCachePath(Context mCtx) {
        String path = getSdCardPath(mCtx) + File.separator + "Android"
                + File.separator + "data" + File.separator + mCtx.getPackageName()
                + File.separator + PICTURE_CACHE + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getCacheSize(Context mCtx) {
        if (SDUtil.ExistSDCard()) {
            String path = getSdCardPath(mCtx) + File.separator + "Android"
                    + File.separator + "data" + File.separator + mCtx.getPackageName();
            FileSize size = new FileSize(new File(path));
            return size.toString();
        }
        return "0MB";
    }

    public static String getPictureCacheSize(Context mCtx) {
        long size = 0L;
        if (SDUtil.ExistSDCard()) {
            String thumbnailPath = getSdCardPath(mCtx) + File.separator + "Android"
                    + File.separator + "data" + File.separator + mCtx.getPackageName()
                    + File.separator + PICTURE_CACHE;

            size += new FileSize(new File(thumbnailPath)).getLongSize();

        }
        return FileSize.convertSizeToString(size);
    }

    public static boolean deleteCache(Context mCtx) {
        String path = getSdCardPath(mCtx) + File.separator + "Android"
                + File.separator + "data" + File.separator + mCtx.getPackageName();
        return deleteDirectory(new File(path));
    }

    public static boolean deletePictureCache(Context mCtx) {
        String thumbnailPath = getSdCardPath(mCtx) + File.separator + "Android"
                + File.separator + "data" + File.separator + mCtx.getPackageName()
                + File.separator + PICTURE_CACHE;

        deleteDirectory(new File(thumbnailPath));

        return true;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static boolean saveToPicDir(Context context, String path) {
        if (!SDUtil.ExistSDCard()) {
            return false;
        }

        File file = new File(path);
        String name = file.getName();
        String newPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + File.separator + "Android" + File.separator + "data"
                + File.separator + name;
        try {
            FileManager.createNewFileInSDCard(newPath);
            copyFile(file, new File(newPath));
            BitmapDecoder.forceRefreshSystemAlbum(context, newPath);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    private static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }
}
