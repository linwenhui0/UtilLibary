package com.hlibrary.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {

    private static final int LOG_SIZE = 5 * 1024 * 1024;
    private static final String RETURN = "\r\n";
    private boolean DEBUG = true;
    private boolean FILE_DEBUG = true;
    private String packageName = "com.log.libaray";
    private File cacheFile;
    private final static int StackTraceIndex = 4;

    public enum TYPE {
        DEFAULT("默认"), ASCII("ASCII数据"), CODE16("16进制数据");

        String msg;

        TYPE(String msg) {
            this.msg = msg;
        }

    }

    private static Logger instance = new Logger();

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
    }

    public Logger setPackageName(Context context) {
        this.packageName = context.getPackageName();
        return this;
    }

    public Logger setDEBUG(boolean DEBUG, boolean FILE_DEBUG) {
        this.DEBUG = DEBUG;
        this.FILE_DEBUG = FILE_DEBUG;
        return this;
    }

    public void setCacheFile(String cacheFile) {
        this.cacheFile = new File(cacheFile);
    }

    private String parseByteArrToString(byte[] msgBytes, TYPE type) {
        String msg;
        switch (type) {
            case ASCII:
                msg = new StringBuffer().append(type.msg).append(" ").append(HexUtil.asciiByteToString(msgBytes)).toString();
                break;
            case CODE16:
                msg = new StringBuffer().append(type.msg).append(" ").append(HexUtil.bytesToHexString(msgBytes)).toString();
                break;
            case DEFAULT:
            default:
                msg = new StringBuffer().append(type.msg).append(" ").append(new String(msgBytes)).toString();
                break;
        }
        return msg;
    }


    private synchronized Logger log(int level, @NonNull String... msg) {

        if (msg.length <= 0)
            return this;

        String TAG = msg[0];
        int index;
        StringBuffer msgBuffer = null;
        if (DEBUG) {
            index = 0;
            StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
            String methodName = sElements[StackTraceIndex].getMethodName();
            int lineNumber = sElements[StackTraceIndex].getLineNumber();
            if (lineNumber < 0) {
                lineNumber = 0;
            }
            msgBuffer = new StringBuffer();
            msgBuffer.append(TAG).append(RETURN)
                    .append("┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────").append(RETURN)
                    .append("| 类名：").append(sElements[StackTraceIndex].getClassName()).append(RETURN)
                    .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                    .append("| 方法名：").append(methodName).append(" , 第 ").append(lineNumber).append(" 行 输出日志").append(RETURN)
                    .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                    .append("|   ");

            for (String m : msg) {
                if (index++ > 0) {
                    msgBuffer.append(m).append(" ");
                }
            }
            msgBuffer.append(RETURN);
            msgBuffer.append("└────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            switch (level) {
                case Log.INFO:
                    Log.i(TAG, msgBuffer.toString());
                    break;
                case Log.VERBOSE:
                    Log.v(TAG, msgBuffer.toString());
                    break;
                case Log.WARN:
                    Log.v(TAG, msgBuffer.toString());
                    break;
                case Log.ERROR:
                    Log.w(TAG, msgBuffer.toString());
                    break;
                case Log.DEBUG:
                    Log.d(TAG, msgBuffer.toString());
                    break;
            }
        }

        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                index = 0;
                msgBuffer = new StringBuffer();

                StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
                String methodName = sElements[StackTraceIndex].getMethodName();
                int lineNumber = sElements[StackTraceIndex].getLineNumber();
                if (lineNumber < 0) {
                    lineNumber = 0;
                }

                msgBuffer.append(TAG).append(RETURN)
                        .append("┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────").append(RETURN)
                        .append("| 类名：").append(sElements[StackTraceIndex].getClassName()).append(RETURN)
                        .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                        .append("| 方法名：").append(methodName).append(" , 第 ").append(lineNumber).append(" 行 输出日志").append(RETURN)
                        .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                        .append("|   ");

                for (String m : msg)
                    if (index++ > 0)
                        msgBuffer.append(m).append(" ");
                msgBuffer.append(RETURN);
                msgBuffer.append("└────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            }
            writeLog(TAG, msgBuffer.toString());
        }
        return this;
    }

    private synchronized Logger log(int level, @NonNull String TAG, @NonNull Object... msg) {
        if (msg.length <= 0)
            return this;

        StringBuffer msgBuffer = null;
        if (DEBUG) {
            StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
            String methodName = sElements[StackTraceIndex].getMethodName();
            int lineNumber = sElements[StackTraceIndex].getLineNumber();
            if (lineNumber < 0) {
                lineNumber = 0;
            }

            msgBuffer = new StringBuffer();
            msgBuffer.append(TAG).append(RETURN)
                    .append("┌──────────────────────────────────────────────────────────────────────────────────").append(RETURN)
                    .append("| 类名：").append(sElements[StackTraceIndex].getClassName()).append(RETURN)
                    .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                    .append("| 方法名：").append(methodName).append(" , 第 ").append(lineNumber).append(" 行 输出日志").append(RETURN)
                    .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                    .append("|   ");

            for (Object m : msg) {
                if (m instanceof String) {
                    msgBuffer.append((String) m).append(" ");
                } else {
                    msgBuffer.append(JSON.toJSONString(m)).append(" ");
                }
            }
            msgBuffer.append(RETURN).append("└──────────────────────────────────────────────────────────────────────────────────").append(RETURN);

            switch (level) {
                case Log.INFO:
                    Log.i(TAG, msgBuffer.toString());
                    break;
                case Log.VERBOSE:
                    Log.v(TAG, msgBuffer.toString());
                    break;
                case Log.WARN:
                    Log.v(TAG, msgBuffer.toString());
                    break;
                case Log.ERROR:
                    Log.w(TAG, msgBuffer.toString());
                    break;
                case Log.DEBUG:
                    Log.d(TAG, msgBuffer.toString());
                    break;
            }
        }
        if (FILE_DEBUG) {
            if (msgBuffer == null) {
                msgBuffer = new StringBuffer();
                StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
                String methodName = sElements[StackTraceIndex].getMethodName();
                int lineNumber = sElements[StackTraceIndex].getLineNumber();
                if (lineNumber < 0) {
                    lineNumber = 0;
                }

                msgBuffer.append(TAG).append(RETURN)
                        .append("┌──────────────────────────────────────────────────────────────────────────────────").append(RETURN)
                        .append("| 类名：").append(sElements[StackTraceIndex].getClassName()).append(RETURN)
                        .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                        .append("| 方法名：").append(methodName).append(" , 第 ").append(lineNumber).append(" 行 输出日志").append(RETURN)
                        .append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄").append(RETURN)
                        .append("|   ");
                for (Object m : msg) {
                    if (m instanceof String) {
                        msgBuffer.append(m).append(" ");
                    } else {
                        msgBuffer.append(JSON.toJSONString(m)).append(" ");
                    }
                }
                msgBuffer.append(RETURN)
                        .append("└──────────────────────────────────────────────────────────────────────────────────");
            }
            writeLog(TAG, msgBuffer.toString());
        }
        return this;
    }

    private synchronized String getTag() {
        StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
        String[] classNameInfo = sElements[StackTraceIndex].getClassName().split("\\.");
        final String TAG;
        if (classNameInfo.length > 0)
            TAG = classNameInfo[classNameInfo.length - 1];
        else
            TAG = Thread.currentThread().getClass().getName();
        return TAG;
    }

    //TODO i
    public synchronized Logger defaultTagI(@NonNull String... msg) {
        final String TAG = getTag();
        StringBuffer msgBuffer = new StringBuffer();
        for (String m : msg) {
            msgBuffer.append(m).append(" ");
        }
        log(Log.INFO, TAG, msgBuffer.toString());
        return this;
    }

    public synchronized Logger defaultTagI(@NonNull Object... msg) {
        return log(Log.INFO, getTag(), msg);
    }


    public synchronized Logger i(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.INFO, TAG, msg);
    }

    public synchronized Logger i(@NonNull String... msg) {
        return log(Log.INFO, msg);
    }

    public synchronized Logger i(byte[] msgBytes, TYPE type) {
        final String TAG = getTag();
        return i(TAG, msgBytes, type);
    }

    public synchronized Logger i(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return i(TAG, msg);
    }


    //TODO v
    public synchronized Logger defaultTagV(@NonNull String... msg) {
        final String TAG = getTag();
        StringBuffer msgBuffer = new StringBuffer();
        for (String m : msg) {
            msgBuffer.append(m).append(" ");
        }
        log(Log.VERBOSE, TAG, msgBuffer.toString());
        return this;
    }

    public synchronized Logger defaultTagV(@NonNull Object... msg) {
        return log(Log.VERBOSE, getTag(), msg);
    }


    public synchronized Logger v(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.VERBOSE, TAG, msg);
    }

    public synchronized Logger v(@NonNull String... msg) {
        return log(Log.VERBOSE, msg);
    }

    public synchronized Logger v(byte[] msgBytes, TYPE type) {
        return v(getTag(), msgBytes, type);
    }

    public synchronized Logger v(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return v(TAG, msg);
    }


    //TODO v
    public synchronized Logger defaultTagW(@NonNull String... msg) {
        final String TAG = getTag();
        StringBuffer msgBuffer = new StringBuffer();
        for (String m : msg) {
            msgBuffer.append(m).append(" ");
        }
        log(Log.WARN, TAG, msgBuffer.toString());
        return this;
    }

    public synchronized Logger defaultTagW(@NonNull Object... msg) {
        return log(Log.WARN, getTag(), msg);
    }


    public synchronized Logger w(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.WARN, TAG, msg);
    }

    public synchronized Logger w(@NonNull String... msg) {
        return log(Log.WARN, msg);
    }

    public synchronized Logger w(byte[] msgBytes, TYPE type) {
        return w(getTag(), msgBytes, type);
    }

    public synchronized Logger w(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return w(TAG, msg);
    }


    //TODO d
    public synchronized Logger defaultTagD(@NonNull String... msg) {
        final String TAG = getTag();
        StringBuffer msgBuffer = new StringBuffer();
        for (String m : msg) {
            msgBuffer.append(m).append(" ");
        }
        log(Log.DEBUG, TAG, msgBuffer.toString());
        return this;
    }

    public synchronized Logger defaultTagD(@NonNull Object... msg) {
        return log(Log.DEBUG, getTag(), msg);
    }


    public synchronized Logger d(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.DEBUG, TAG, msg);
    }

    public synchronized Logger d(@NonNull String... msg) {
        return log(Log.DEBUG, msg);
    }

    public synchronized Logger d(byte[] msgBytes, TYPE type) {
        return d(getTag(), msgBytes, type);
    }

    public synchronized Logger d(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return w(TAG, msg);
    }

    //TODO    e
    public synchronized Logger defaultTagE(@NonNull String... msg) {
        final String TAG = getTag();
        StringBuffer msgBuffer = new StringBuffer();
        for (String m : msg) {
            msgBuffer.append(m).append(" ");
        }
        log(Log.ERROR, TAG, msgBuffer.toString());
        return this;
    }

    public synchronized Logger defaultTagE(@NonNull Object... msg) {
        final String TAG = getTag();
        return log(Log.ERROR, TAG, msg);
    }


    public synchronized Logger e(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.ERROR, TAG, msg);
    }

    public synchronized Logger e(@NonNull String... msg) {
        return log(Log.ERROR, msg);
    }

    public synchronized Logger e(byte[] msgBytes, TYPE type) {
        return e(getTag(), msgBytes, type);
    }

    public synchronized Logger e(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return e(TAG, msg);
    }


    private synchronized File createFile(String filename) throws IOException {
        File file = new File(filename);
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
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private synchronized void writeLog(String TAG, String msg) {
        if (SDUtil.ExistSDCard()) {
            try {
                FileOutputStream logsFOS;

                StringBuilder filenameBuilder = new StringBuilder();
                if (cacheFile != null) {
                    filenameBuilder.append(cacheFile.getAbsoluteFile().toString());
                } else {
                    filenameBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                    filenameBuilder.append(File.separator).append("Android").append(File.separator).append("data");
                }
                filenameBuilder.append(File.separator).append(packageName);
                filenameBuilder.append(File.separator).append("logs").append(File.separator);
                filenameBuilder.append("allLog_").append(DateFormatUtil.getDate("yyyyMMdd"));
                StringBuffer filenameTmp = new StringBuffer();
                filenameTmp.append(filenameBuilder.toString()).append(".txt");
                File file = createFile(filenameTmp.toString());
                if (file.length() > LOG_SIZE) {
                    filenameBuilder.append("_").append(DateFormatUtil.getDate("HHmmss")).append(".txt");
                    file = createFile(filenameBuilder.toString());
                }

                logsFOS = new FileOutputStream(file, true);
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
