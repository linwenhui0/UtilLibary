package com.hlibrary.util.encrypt

import com.hlibrary.util.HexUtil

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
        try {
            val alga = java.security.MessageDigest.getInstance("MD5")
            alga.update(info.toByteArray())
            val digesta = alga.digest()
            return HexUtil.bytesToHexString(digesta)
        } catch (ex: Exception) {
            return null
        }

    }


}
