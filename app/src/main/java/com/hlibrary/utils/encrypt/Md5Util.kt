package com.hlibrary.utils.encrypt

import com.hlibrary.utils.HexUtils

/**
 * @author linwenhui
 * @date 2015-01-01
 * @updateDate 20190216
 */
object Md5Util {


    /**
     * md5加密
     *
     * @param info
     * @return
     */
    fun digest(info: String): String? {
        return digest(info.toByteArray())
    }

    /**
     * md5加密
     *
     * @param info
     * @return
     */
    fun digest(data: ByteArray): String? {
        try {
            val alga = java.security.MessageDigest.getInstance("MD5")
            alga.update(data)
            val digesta = alga.digest()
            return HexUtils.bytesToHexString(digesta)
        } catch (ex: Exception) {
            return null
        }
    }


}
