package com.hlibrary.util.crash;

import com.hlibrary.util.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;


/**
 * 应用程序异常处理类：用于捕获异常和提示错误信息
 *
 * @author 林文辉
 * @version 1.0
 * @created 2015-5-1
 */
public class AppCrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "AppCrashHandler";
    private static final String RETURN = "\r\n";
    private static AppCrashHandler instance;
    private UncaughtExceptionHandler mDefaultHandler;

    private AppCrashHandler() {
        init();
    }

    public static AppCrashHandler getInstance() {
        if (instance == null) {
            instance = new AppCrashHandler();
        }
        return instance;
    }

    private void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }

        // 错误信息
        // 这里还可以加上当前的系统版本，机型型号 等等信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        Logger.getInstance().e(TAG, result);


        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
        return true;
    }
}