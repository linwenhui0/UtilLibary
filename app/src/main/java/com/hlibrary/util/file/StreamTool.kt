package com.hlibrary.util.file

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author linwh
 * @date 2015/01/23
 */
object StreamTool {

    @Throws(IOException::class)
    fun readStream(filename: String): ByteArray {
        val `in` = FileInputStream(filename)
        return readStream(`in`)
    }

    @Throws(IOException::class)
    fun readStream(inputStream: InputStream): ByteArray {
        val data = ByteArray(1024)
        val bos = ByteArrayOutputStream()
        var len: Int = inputStream.read(data)
        while (len != -1) {
            bos.write(data, 0, len)
            len = inputStream.read(data)
        }
        return bos.toByteArray()
    }

}
