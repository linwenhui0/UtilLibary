package com.hlibrary.util

import com.hlibrary.util.constants.Constants

/**
 * 通用工具
 */
class Utils {

    companion object {
        /**
         * @param mobiles 手机号码
         * @return true 手机号码格式合法,false 手机号码格式不合法
         */
        fun isMobileNO(mobiles: String): Boolean {
            return mobiles.matches(Constants.PHONE_REGEX.toRegex())
        }

        /**
         * @param mobiles 手机号码
         * @param separator 分隔符
         * @return true 手机号码格式合法,false 手机号码格式不合法
         */
        fun isFormatMobileNO(mobiles: String, separator: String): Boolean {
            val telRegex = String.format(Constants.PHONE_FORMAT_REGEX, separator, separator)
            return mobiles.matches(telRegex.toRegex())
        }
    }

}