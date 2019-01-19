package com.hlibrary.util.file;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @version v1.0.0
 * @date 2015-01-27
 */
public class SDUtil {

    private SDUtil() {
    }

    /**
     * 获得手机内存
     */
    public static String getRom(Context mCtx) {
        return Formatter.formatFileSize(mCtx.getApplicationContext(), getOldRom());
    }

    /**
     * 获得手机内存，单位：字节
     */
    public static long getOldRom() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        final long blockSize;
        final long totalBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }
        return totalBlocks * blockSize;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return true:存在<br/> false:不存在
     */
    public static boolean ExistSDCard() {
        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);
        return !(!canRead || onlyRead || unMounted);
    }

    /**
     * @return 查看SD卡的剩余空间，单位：字节
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        final long blockSize;
        // 空闲的数据块的数量
        final long freeBlocks;
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = sf.getBlockSizeLong();
            freeBlocks = sf.getAvailableBlocksLong();
        } else {
            blockSize = sf.getBlockSize();
            freeBlocks = sf.getAvailableBlocks();
        }

        return freeBlocks * blockSize;
    }

    /**
     * @return 查看SD卡总容量，单位：字节
     */
    public static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        final long blockSize;
        // 获取所有数据块数
        final long allBlocks;
        // 返回SD卡大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
            allBlocks = sf.getBlockCountLong();
        } else {
            blockSize = sf.getBlockSize();
            allBlocks = sf.getBlockCount();
        }
        return allBlocks * blockSize;
    }

    public static String getTotalMemory(Context mCtx) {
        // 系统内存信息文件
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            // 读取meminfo第一行，系统总内存大小
            str2 = localBufferedReader.readLine();

            arrayOfString = str2.split("\\s+");

            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            try {
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            localBufferedReader.close();

        } catch (IOException e) {
        }
        // Byte转换为KB或者MB，内存大小规格化
        return Formatter.formatFileSize(mCtx.getApplicationContext(), initial_memory);
    }

    public static long getTotal() {
        File path = Environment.getRootDirectory();
        StatFs sf = new StatFs(path.getPath());
        final long blockSize;
        final long allBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
            allBlocks = sf.getBlockCountLong();
        } else {
            blockSize = sf.getBlockSize();
            allBlocks = sf.getBlockCount();
        }
        return blockSize * allBlocks;
    }

    public static long getTotalRoot() {
        File path = Environment.getRootDirectory();
        StatFs sf = new StatFs(path.getPath());
        final long blockSize;
        final long allBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
            allBlocks = sf.getBlockCountLong();
        } else {
            blockSize = sf.getBlockSize();
            allBlocks = sf.getBlockCount();
        }
        return blockSize * allBlocks;
    }

}
