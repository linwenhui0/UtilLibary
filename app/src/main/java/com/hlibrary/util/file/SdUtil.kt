package com.hlibrary.util.file

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @version v1.0.0
 * @date 2015-01-27
 * @author linwenhui
 */
object SdUtil {

    /**
     * 获得手机内存，单位：字节
     */
    val oldRom: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long
            val totalBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                totalBlocks = stat.blockCountLong
            } else {
                blockSize = stat.blockSize.toLong()
                totalBlocks = stat.blockCount.toLong()
            }
            return totalBlocks * blockSize
        }

    /**
     * @return 查看SD卡的剩余空间，单位：字节
     */
    // 取得SD卡文件路径
    // 获取单个数据块的大小(Byte)
    // 空闲的数据块的数量
    val sdFreeSize: Long
        get() {
            val path = Environment.getExternalStorageDirectory()
            val sf = StatFs(path.path)
            val blockSize: Long
            val freeBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = sf.blockSizeLong
                freeBlocks = sf.availableBlocksLong
            } else {
                blockSize = sf.blockSize.toLong()
                freeBlocks = sf.availableBlocks.toLong()
            }

            return freeBlocks * blockSize
        }

    /**
     * @return 查看SD卡总容量，单位：字节
     */
    // 取得SD卡文件路径
    // 获取单个数据块的大小(Byte)
    // 获取所有数据块数
    // 返回SD卡大小
    val sdAllSize: Long
        get() {
            val path = Environment.getExternalStorageDirectory()
            val sf = StatFs(path.path)
            val blockSize: Long
            val allBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = sf.blockSizeLong
                allBlocks = sf.blockCountLong
            } else {
                blockSize = sf.blockSize.toLong()
                allBlocks = sf.blockCount.toLong()
            }
            return allBlocks * blockSize
        }

    val total: Long
        get() {
            val path = Environment.getRootDirectory()
            val sf = StatFs(path.path)
            val blockSize: Long
            val allBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = sf.blockSizeLong
                allBlocks = sf.blockCountLong
            } else {
                blockSize = sf.blockSize.toLong()
                allBlocks = sf.blockCount.toLong()
            }
            return blockSize * allBlocks
        }

    val totalRoot: Long
        get() {
            val path = Environment.getRootDirectory()
            val sf = StatFs(path.path)
            val blockSize: Long
            val allBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = sf.blockSizeLong
                allBlocks = sf.blockCountLong
            } else {
                blockSize = sf.blockSize.toLong()
                allBlocks = sf.blockCount.toLong()
            }
            return blockSize * allBlocks
        }

    /**
     * 获得手机内存
     */
    fun getRom(mCtx: Context): String {
        return Formatter.formatFileSize(mCtx.applicationContext, oldRom)
    }

    /**
     * 判断SD卡是否存在
     *
     * @return true:存在<br></br> false:不存在
     */
    fun existSDCard(): Boolean {
        val canRead = Environment.getExternalStorageDirectory().canRead()
        val onlyRead = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY
        val unMounted = Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED
        return !(!canRead || onlyRead || unMounted)
    }

    fun getTotalMemory(mCtx: Context): String {
        // 系统内存信息文件
        val str1 = "/proc/meminfo"
        val str2: String
        val arrayOfString: Array<String>
        var initialMemory: Long = 0
        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader = BufferedReader(
                    localFileReader, 8192)
            // 读取meminfo第一行，系统总内存大小
            str2 = localBufferedReader.readLine()

            arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            try {
                initialMemory = (Integer.valueOf(arrayOfString[1]).toInt() * 1024).toLong()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            localBufferedReader.close()

        } catch (e: IOException) {
        }

        // Byte转换为KB或者MB，内存大小规格化
        return Formatter.formatFileSize(mCtx.applicationContext, initialMemory)
    }

}
