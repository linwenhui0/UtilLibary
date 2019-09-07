package com.hlibrary.util.constants

/**
 * @author linwh
 * @date 2019.01.19
 */
object Constants {

    const val KB = "KB"
    const val MB = "MB"
    const val GB = "GB"
    const val TB = "TB"

    /**
     * 图片缓存文件夹名
     */
    const val PICTURE_CACHE = "picture_cache"
    /**
     * 图片缓存缩
     */
    const val PICTURE_MIN_CACHE = "picture_min_cache"

    /**
     * 手机号正则表达式
     */
    const val PHONE_REGEX = "[1]\\d{10}"
    const val PHONE_FORMAT_REGEX = "[1]\\d{2}[%s]\\d{4}[%s]\\d{4}"

    /**
     * 默认表情正式表达式
     */
    const val FACE_REGEX = "\\[[^\\]]+\\]"

}
