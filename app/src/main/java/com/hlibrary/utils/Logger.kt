package com.hlibrary.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.DiskLogAdapter

/**
 * @author linwenhui
 */
object Logger {
    private const val STACK_TRACE_INDEX = 5

    private val tag: String
        @Synchronized get() {
            try {
                val sElements = Thread.currentThread().stackTrace
                return if (sElements?.size != null && sElements?.size > STACK_TRACE_INDEX) {
                    val names = sElements[STACK_TRACE_INDEX - 1].className.split(".")
                    if (names?.isNotEmpty() == true) {
                        val size = names.size
                        names[size - 1]
                    } else {
                        "Logger"
                    }
                } else {
                    "Logger"
                }
            } catch (e: Exception) {
            }
            return "Logger"
        }


    fun init(debug: Boolean) {
        com.orhanobut.logger.Logger.addLogAdapter(
            DiskLogAdapter()
        )
        com.orhanobut.logger.Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return debug
            }
        })
    }

    private fun buildMessage(msg: String): String {
        return "$tag $msg"
    }

    @Synchronized
    fun defaultTagI(msg: String) {
        com.orhanobut.logger.Logger.i(buildMessage(msg))
    }

    @Synchronized
    fun i(tag: String, msg: String) {
        com.orhanobut.logger.Logger.i("$tag $msg")
    }

    @Synchronized
    fun defaultTagV(msg: String) {
        com.orhanobut.logger.Logger.v(buildMessage(msg))
    }

    @Synchronized
    fun v(tag: String, msg: String) {
        com.orhanobut.logger.Logger.v("$tag $msg")
    }

    @Synchronized
    fun defaultTagW(msg: String) {
        com.orhanobut.logger.Logger.w(buildMessage(msg))
    }

    @Synchronized
    fun w(tag: String, msg: String) {
        com.orhanobut.logger.Logger.w("$tag $msg")
    }


    @Synchronized
    fun defaultTagD(msg: String) {
        com.orhanobut.logger.Logger.d(buildMessage(msg))
    }

    @Synchronized
    fun d(tag: String, msg: String) {
        com.orhanobut.logger.Logger.d("$tag $msg")
    }

    @Synchronized
    fun defaultTagE(msg: String) {
        com.orhanobut.logger.Logger.e(buildMessage(msg))
    }

    @Synchronized
    fun e(tag: String, msg: String) {
        com.orhanobut.logger.Logger.e("$tag $msg")
    }

}
