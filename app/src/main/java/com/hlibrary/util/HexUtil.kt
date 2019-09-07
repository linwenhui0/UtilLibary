package com.hlibrary.util

import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * @author linwenhui
 * @date 2017/3/2
 */

object HexUtil {

    /**
     * 16进制字符串转成字符数据
     *
     * @param hex 16进制字符串
     * @return
     */
    fun hexStringToByte(hex: String): ByteArray {
        val len = hex.length / 2
        val result = ByteArray(len)
        val hexChars = hex.toUpperCase().toCharArray()

        for (i in 0 until len) {
            val pos = i * 2
            try {
                result[i] = java.lang.Byte.parseByte(hexChars[pos].toString() + hexChars[pos + 1].toString(), 16)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

        }

        return result
    }

    /**
     * 转换成指定长度的字节数组
     *
     * @param hex  16进制字符串
     * @param num  字符长度
     * @param fill 填充字符
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun hexStringToByte(hex: String, num: Int, fill: Byte): ByteArray {
        val len = hex.length
        val bos = ByteArrayOutputStream()
        bos.write(hexStringToByte(hex))
        val iLen = num - len
        if (iLen > 0) {
            for (i in 0 until iLen) {
                bos.write(fill.toInt())
            }
        }
        val result = bos.toByteArray()
        bos.close()
        return result
    }

    /**
     * byte数组转成16进制字符串
     *
     * @param bytes
     * @return
     */
    fun bytesToHexString(bytes: ByteArray?): String {
        if (bytes == null) {
            return ""
        }
        val hexBuffer = StringBuffer(bytes.size * 2)

        for (b in bytes) {
            hexBuffer.append(String.format("%02x", b).toUpperCase())
        }

        return hexBuffer.toString()
    }

    /**
     * byte数组转int
     *
     * @param b
     * @return
     */
    fun bytes2int(b: ByteArray): Int {
        var res = 0
        val len = b.size
        for (i in 0 until len) {
            res = res shl 8
            val var5 = b[i].toInt() and 0xFF
            res = res or var5
        }

        return res
    }

    fun asciiByteToString(data: ByteArray): String {
        val len = data.size
        val chars = CharArray(len)
        for (i in 0 until len) {
            chars[i] = data[i].toChar()
        }
        return String(chars)
    }

    @Throws(IOException::class)
    fun asciiStringToAscii(source: String, minLen: Int, fillByte: Byte): ByteArray {
        val asciiChars = source.toCharArray()
        val len = asciiChars.size

        var asciiBytes = ByteArray(len)
        System.arraycopy(asciiBytes, 0, asciiBytes, 0, len)
        if (minLen > 0 && len < minLen) {
            val lessLen = minLen - len
            val bos = ByteArrayOutputStream()
            bos.write(asciiBytes)
            for (i in 0 until lessLen) {
                bos.write(fillByte.toInt())
            }
            asciiBytes = bos.toByteArray()
            bos.close()
        }
        return asciiBytes
    }

    @Throws(IOException::class)
    fun intToAscii(data: Int, minLen: Int): ByteArray {
        val s = String.format("%" + minLen + "d", data).replace(" ", "0")
        return asciiStringToAscii(s, 0, ' '.toByte())
    }

    /**
     * 右靠，左补空格
     *
     * @param source
     * @param len
     * @param fill
     * @return
     */
    fun formatLeftAlign(source: String, len: Int, fill: String): String {
        return String.format("%" + len + "s", source).replace(" ", fill)
    }

    /**
     * 左靠，右补空格
     *
     * @param source
     * @param len
     * @param fill
     * @return
     */
    fun formatRightAlign(source: String, len: Int, fill: String): String {
        return String.format("%-" + len + "s", source).replace(" ", fill)
    }

    fun fillChar(len: Int, fill: String): String {
        val temp = String.format("%" + len + "s", fill)
        return temp.replace(" ", fill)
    }

    fun int2bytes(a: Int): ByteArray {
        return byteArrayOf((a shr 24 and 0xFF).toByte(), (a shr 16 and 0xFF).toByte(), (a shr 8 and 0xFF).toByte(), (a and 0xFF).toByte())
    }

    /**
     * 字符串转ascii码
     *
     * @param source
     * @return
     */
    fun toByteArray(source: String): ByteArray {
        val sbu = StringBuffer()
        val chars = source.toCharArray()
        for (c in chars) {
            sbu.append(Integer.toHexString(c.toInt()))
        }
        return hexStringToByte(sbu.toString())
    }

}