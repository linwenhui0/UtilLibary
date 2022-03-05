package com.hlibrary.utils.file

import android.content.Context
import android.os.Build
import android.os.Environment
import com.hlibrary.utils.constants.Constants
import java.io.*

/**
 * @author linwh
 * @date 2015/01/23
 */
object FileManager {

    /**
     * @param context
     * @return 如果存在SD卡返回SD路径，不存在SD卡返回内部访问空间路径
     */
    fun getSdCardPath(context: Context): String {
        if (Build.VERSION.SDK_INT <= 29 && SdUtil.existSDCard()) {
            val path = Environment.getExternalStorageDirectory()
            if (path != null) {
                return path.absolutePath
            }
        }
        return context.cacheDir.absolutePath
    }

    fun deleteCache(mCtx: Context): Boolean {
        val path = (getSdCardPath(mCtx) + File.separator + "Android"
                + File.separator + "data" + File.separator + mCtx.packageName)
        return deleteDirectory(File(path))
    }

    fun deleteDirectory(path: File): Boolean {
        if (path.exists()) {
            val files = path.listFiles() ?: return true
            for (i in files.indices) {
                if (files[i].isDirectory) {
                    deleteDirectory(files[i])
                } else {
                    files[i].delete()
                }
            }
        }
        return path.delete()
    }


    fun writeFile(file: File, content: String) {
        var randomAccessFile: RandomAccessFile? = null
        try {
            randomAccessFile = RandomAccessFile(file, "rw")
            val data = content.toByteArray()
            val position = randomAccessFile.length()
            randomAccessFile.seek(position)
            randomAccessFile.write(data)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    @Throws(IOException::class)
    fun copyFile(srcFile: File?, dstFile: File?) {
        if (srcFile == null || dstFile == null || !srcFile.exists()) {
            return
        }

        if (dstFile.exists()) {
            if (!dstFile.isFile) {
                dstFile.delete()
            } else {
                return
            }
        }
        dstFile.createNewFile()

        val srcIn = FileInputStream(srcFile)
        val data = StreamTool.readStream(srcIn)
        srcIn.close()
        val dstOut = FileOutputStream(dstFile)
        dstOut.write(data)
        dstOut.close()
    }


}
