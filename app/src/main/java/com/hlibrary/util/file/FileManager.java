package com.hlibrary.util.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static com.hlibrary.util.constants.Constants.PICTURE_CACHE;
import static com.hlibrary.util.constants.Constants.PICTURE_MIN_CACHE;

/**
 * @author linwh
 * @date 2015/01/23
 */
public class FileManager {

    private static final String TAG = "FileManager";


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


    public static void writeFile(File file, String content) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            byte[] data = content.getBytes();
            long position = randomAccessFile.length();
            randomAccessFile.seek(position);
            randomAccessFile.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
