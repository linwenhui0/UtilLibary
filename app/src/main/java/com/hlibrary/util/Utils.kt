package com.hlibrary.util

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
            val telRegex = "[1]\\d{10}"
            return mobiles.matches(telRegex.toRegex())
        }

        /**
         * @param mobiles 手机号码
         * @param separator 分隔符
         * @return true 手机号码格式合法,false 手机号码格式不合法
         */
        fun isFormatMobileNO(mobiles: String, separator: String): Boolean {
            val telRegex = String.format("[1]\\d{2}[%s]\\d{4}[%s]\\d{4}",separator,separator)
            return mobiles.matches(telRegex.toRegex())
        }
    }

}