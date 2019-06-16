package com.hlibrary.util.crash

import com.hlibrary.util.Logger
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.UncaughtExceptionHandler


/**
 * 应用程序异常处理类：用于捕获异常和提示错误信息
 *
 * @author linwh
 * @version 1.0
 * @date 2015-5-1
 */
class AppCrashHandler private constructor() : UncaughtExceptionHandler {

    private var mDefaultHandler: UncaughtExceptionHandler? = null

    init {
        init()
    }

    private fun init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler!!.uncaughtException(thread, ex)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return true
        }

        // 错误信息
        // 这里还可以加上当前的系统版本，机型型号 等等信息
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        Logger.instance.exception(TAG, result)


        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(10)
        return true
    }

    companion object {
        const val TAG = "AppCrashHandler"
        val instance: AppCrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppCrashHandler()
        }
    }
}