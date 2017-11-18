package com.hlibrary.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {

    private static final int LOG_SIZE = 5 * 1024 * 1024;
    private static boolean DEBUG = true;
    private static boolean FILE_DEBUG = true;
    private static String packageName;

    public enum TYPE {
        DEFAULT("默认"), ASCII("ASCII"), CODE16("16进制数据");
        String msg;

        TYPE(String msg) {
            this.msg = msg;
        }

    }

    public static void setPackageName(Context context) {
        Logger.packageName = context.getPackageName();
    }

    public static void setDEBUG(boolean DEBUG, boolean FILE_DEBUG) {
        Logger.DEBUG = DEBUG;
        Logger.FILE_DEBUG = FILE_DEBUG;
    }

    private static String parseByteArrToString(byte[] msgBytes, TYPE type) {
        String msg;
        switch (type) {
            case ASCII:
                msg = HexUtil.asciiByteToString(msgBytes);
                break;
            case CODE16:
                msg = HexUtil.bytesToHexString(msgBytes);
                break;
            case DEFAULT:
            default:
                msg = new String(msgBytes);
                break;
        }
        return msg;
    }

    public static void i(String... msg) {
        final String TAG = Thread.currentThread().getName();
        i(TAG, msg);
    }

    public static void i(String TAG, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.i(TAG, msgBuffer.toString());
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void i(Object msg) {
        final String TAG = Thread.currentThread().getClass().getName();
        i(TAG, msg);
    }

    public static void i(String TAG, Object msg) {
        String msgBuffer = null;
        if (DEBUG) {
            msgBuffer = JSON.toJSONString(msg);
            Log.i(TAG, msgBuffer);

        }
        if (FILE_DEBUG) {
            if (msgBuffer == null)
                msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void i(String msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        i(TAG, tr, msg);
    }

    public static void i(String TAG, Throwable tr, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.i(TAG, msgBuffer.toString(), tr);
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void i(Object msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        i(TAG, tr, msg);
    }

    public static void i(String TAG, Throwable tr, Object msg) {
        if (DEBUG) {
            String msgBuffer = JSON.toJSONString(msg);
            Log.i(TAG, msgBuffer, tr);
        }
        if (FILE_DEBUG) {
            String msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void i(byte[] msgBytes) {
        final String TAG = Thread.currentThread().getName();
        i(TAG, msgBytes, TYPE.DEFAULT);
    }

    public static void i(byte[] msgBytes, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        i(TAG, msgBytes, type);
    }

    public static void i(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        i(TAG, new String[]{msg});
    }

    public static void i(String TAG, String title, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        i(TAG, new String[]{title, msg});
    }

    public static void i(byte[] msgBytes, Throwable tr, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        i(TAG, msgBytes, tr, type);
    }

    public static void i(String TAG, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        i(TAG, tr, msg);
    }

    public static void i(String TAG, String title, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        i(TAG, tr, title, msg);
    }

    public static void v(String... msg) {
        final String TAG = Thread.currentThread().getName();
        v(TAG, msg);
    }

    public static void v(String TAG, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.v(TAG, msgBuffer.toString());
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void v(Object msg) {
        final String TAG = Thread.currentThread().getName();
        v(TAG, msg);
    }

    public static void v(String TAG, Object msg) {
        String msgBuffer = null;
        if (DEBUG) {
            msgBuffer = JSON.toJSONString(msg);
            Log.v(TAG, msgBuffer);

        }
        if (FILE_DEBUG) {
            if (msgBuffer == null)
                msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void v(String msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        v(TAG, tr, msg);
    }

    public static void v(String TAG, Throwable tr, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.v(TAG, msgBuffer.toString(), tr);
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void v(byte[] msgBytes) {
        final String TAG = Thread.currentThread().getName();
        v(TAG, msgBytes, TYPE.DEFAULT);
    }

    public static void v(byte[] msgBytes, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        v(TAG, msgBytes, type);
    }

    public static void v(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        v(TAG, new String[]{msg});
    }

    public static void v(String TAG, String title, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        v(TAG, new String[]{title, msg});
    }

    public static void v(byte[] msgBytes, Throwable tr, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        v(TAG, msgBytes, tr, type);
    }

    public static void v(String TAG, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        v(TAG, tr, msg);
    }

    public static void v(String TAG, String title, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        v(TAG, tr, title, msg);
    }

    public static void w(String... msg) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, msg);
    }

    public static void w(String TAG, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.w(TAG, msgBuffer.toString());
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void w(Object msg) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, msg);
    }

    public static void w(String TAG, Object msg) {
        String msgBuffer = null;
        if (DEBUG) {
            msgBuffer = JSON.toJSONString(msg);
            Log.w(TAG, msgBuffer);

        }
        if (FILE_DEBUG) {
            if (msgBuffer == null)
                msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void w(String msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, tr, msg);
    }

    public static void w(String TAG, Throwable tr, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.w(TAG, msgBuffer.toString(), tr);
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void w(Object msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, tr, msg);
    }

    public static void w(String TAG, Throwable tr, Object msg) {
        if (DEBUG) {
            String msgBuffer = JSON.toJSONString(msg);
            Log.w(TAG, msgBuffer, tr);
        }
        if (FILE_DEBUG) {
            String msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void w(byte[] msgBytes) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, msgBytes, TYPE.DEFAULT);
    }

    public static void w(byte[] msgBytes, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, msgBytes, type);
    }

    public static void w(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        w(TAG, new String[]{msg});
    }

    public static void w(String TAG, String title, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        w(TAG, new String[]{title, msg});
    }

    public static void w(byte[] msgBytes, Throwable tr, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        w(TAG, msgBytes, tr, type);
    }

    public static void w(String TAG, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        w(TAG, tr, msg);
    }

    public static void w(String TAG, String title, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        w(TAG, tr, title, msg);
    }

    public static void d(String... msg) {
        final String TAG = Thread.currentThread().getName();
        d(TAG, msg);
    }

    public static void d(String TAG, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.d(TAG, msgBuffer.toString());
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void d(Object msg) {
        final String TAG = Thread.currentThread().getName();
        d(TAG, msg);
    }

    public static void d(String TAG, Object msg) {
        String msgBuffer = null;
        if (DEBUG) {
            msgBuffer = JSON.toJSONString(msg);
            Log.d(TAG, msgBuffer);

        }
        if (FILE_DEBUG) {
            if (msgBuffer == null)
                msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void d(String msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        d(TAG, tr, msg);
    }

    public static void d(String TAG, Throwable tr, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.d(TAG, msgBuffer.toString(), tr);
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void d(byte[] msgBytes) {
        final String TAG = Thread.currentThread().getName();
        d(TAG, msgBytes, TYPE.DEFAULT);
    }

    public static void d(byte[] msgBytes, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        d(TAG, msgBytes, type);
    }

    public static void d(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        d(TAG, new String[]{msg});
    }

    public static void d(String TAG, String title, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        d(TAG, new String[]{title, msg});
    }

    public static void d(byte[] msgBytes, Throwable tr, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        d(TAG, msgBytes, tr, type);
    }

    public static void d(String TAG, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        d(TAG, tr, msg);
    }

    public static void d(String TAG, String title, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        d(TAG, tr, title, msg);
    }

    public static void e(String... msg) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, msg);
    }

    public static void e(String TAG, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.e(TAG, msgBuffer.toString());
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void e(Object msg) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, msg);
    }

    public static void e(String TAG, Object msg) {
        String msgBuffer = null;
        if (DEBUG) {
            msgBuffer = JSON.toJSONString(msg);
            Log.e(TAG, msgBuffer);

        }
        if (FILE_DEBUG) {
            if (msgBuffer == null)
                msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void e(String msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, tr, msg);
    }

    public static void e(String TAG, Throwable tr, String... msg) {
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            msgBuffer = new StringBuffer();
            for (String m : msg)
                msgBuffer.append(m);
            Log.e(TAG, msgBuffer.toString(), tr);
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                for (String m : msg)
                    msgBuffer.append(m);
            }
            writeLog(TAG, msgBuffer.toString());
        }
    }

    public static void e(Object msg, Throwable tr) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, tr, msg);
    }

    public static void e(String TAG, Throwable tr, Object msg) {
        if (DEBUG) {
            String msgBuffer = JSON.toJSONString(msg);
            Log.e(TAG, msgBuffer, tr);
        }
        if (FILE_DEBUG) {
            String msgBuffer = JSON.toJSONString(msg);
            writeLog(TAG, msgBuffer);
        }
    }

    public static void e(byte[] msgBytes) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, msgBytes, TYPE.DEFAULT);
    }

    public static void e(byte[] msgBytes, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, msgBytes, type);
    }

    public static void e(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        e(TAG, new String[]{msg});
    }

    public static void e(String TAG, String title, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        e(TAG, new String[]{title, msg});
    }

    public static void e(byte[] msgBytes, Throwable tr, TYPE type) {
        final String TAG = Thread.currentThread().getName();
        e(TAG, msgBytes, tr, type);
    }

    public static void e(String TAG, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        e(TAG, tr, msg);
    }

    public static void e(String TAG, String title, byte[] msgBytes, Throwable tr, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        e(TAG, tr, title, msg);
    }

    private static void writeLog(String TAG, String msg) {
        if (SDUtil.ExistSDCard()) {
            try {
                FileOutputStream logsFOS;


                StringBuilder filenameBuilder = new StringBuilder();
                filenameBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                filenameBuilder.append(File.separator).append("Android").append(File.separator).append("data");
                filenameBuilder.append(File.separator).append(packageName);
                filenameBuilder.append(File.separator).append("logs").append(File.separator);
                filenameBuilder.append("allLog_").append(DateFormatUtil.getDate("yyyyMMdd")).append(".log");
                init:
                {
                    File file = new File(filenameBuilder.toString());
                    File parentFile = file.getParentFile();
                    if (parentFile.exists() && parentFile.isFile()) {
                        if (parentFile.isFile()) {
                            parentFile.delete();
                            parentFile.mkdirs();
                        }
                    } else {
                        parentFile.mkdirs();
                    }
                    if (file.exists()) {
                        if (!file.canWrite() || !file.canRead() || file.isDirectory()) {
                            file.delete();
                            file.setReadable(true, false);
                            file.setWritable(true, false);
                            file.createNewFile();
                        }
                    } else {
                        file.setReadable(true, false);
                        file.setWritable(true, false);
                        file.createNewFile();
                    }
                    if (file.exists()) {
                    } else {
                        file.createNewFile();
                    }
                    logsFOS = new FileOutputStream(file, true);
                    if (file.length() > LOG_SIZE) {
                        filenameBuilder.append("_").append(DateFormatUtil.getDate("HHmmss")).append("_log");
                        break init;
                    }
                }
                logsFOS.write((TAG + " ").getBytes());
                logsFOS.write(DateFormatUtil.getDate("yyyy-MM-dd HH:mm:ss ").getBytes());
                logsFOS.write(msg.getBytes());
                logsFOS.write("\r\n".getBytes());
                logsFOS.flush();
                logsFOS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
