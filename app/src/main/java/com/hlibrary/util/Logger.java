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
import java.util.ArrayList;
import java.util.List;

/**
 * @author linwenhui
 */
public class Logger {

    private static final int LOG_SIZE = 5 * 1024 * 1024;
    private static final int LINE_LIMIT = 900;
    private static final String RETURN = "\r\n";
    private final static int STACK_TRACE_INDEX = 4;

    private volatile int level = Log.VERBOSE;
    private boolean debug = true;
    private boolean fileDebug = true;
    private String packageName = "com.log.libaray";
    private File cacheFile;

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

    public Logger setDebug(boolean debug, boolean fileDebug) {
        this.debug = debug;
        if (fileDebug && SDUtil.ExistSDCard()) {
            this.fileDebug = true;
        } else {
            this.fileDebug = false;
        }
        return this;
    }

    public Logger setLevel(int level) {
        this.level = level;
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

    private synchronized Logger log(int level, @NonNull String TAG, @NonNull Object... msg) {

        if (msg.length <= 0 || this.level > level) {
            return this;
        }


        if (debug) {
            logOnLogcat(level, TAG, msg);
        }
        if (fileDebug) {
            StringBuffer msgBuffer = new StringBuffer();
            StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
            String methodName = sElements[STACK_TRACE_INDEX].getMethodName();
            int lineNumber = sElements[STACK_TRACE_INDEX].getLineNumber();
            if (lineNumber < 0) {
                lineNumber = 0;
            }

            String headerTmp = String.format("****************** %s ******************", TAG);
            msgBuffer.append(headerTmp).append(RETURN)
                    .append(" 类名：").append(sElements[STACK_TRACE_INDEX].getClassName()).append(RETURN)
                    .append(" 方法名：").append(methodName).append(" , 第 ").append(lineNumber).append(" 行 输出日志")
                    .append(" , 输出时间 ").append(DateFormatUtil.getDate("yyyy-MM-dd HH:mm:ss.SSS")).append(RETURN)
                    .append(" 日志详情：").append(RETURN)
                    .append("    ");
            for (Object m : msg) {
                if (m instanceof String) {
                    msgBuffer.append(m).append(" ");
                } else {
                    msgBuffer.append(JSON.toJSONString(m)).append(" ");
                }
            }
            msgBuffer.append(RETURN);
            writeLog(msgBuffer.toString());
        }
        return this;
    }

    private synchronized void logOnLogcat(int level, @NonNull String TAG, @NonNull Object... msg) {
        StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
        String methodName = sElements[STACK_TRACE_INDEX].getMethodName();
        int lineNumber = sElements[STACK_TRACE_INDEX].getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }

        List<String> msgList = new ArrayList<>();

        String prefix = String.format("%s.%s.%d ", sElements[STACK_TRACE_INDEX].getClassName(), methodName, lineNumber);
        StringBuffer contentBuffer = new StringBuffer();
        contentBuffer.append(prefix);
        for (Object m : msg) {
            if (m instanceof String) {
                contentBuffer.append((String) m).append(" ");
            } else {
                contentBuffer.append(JSON.toJSONString(m)).append(" ");
            }
            if (contentBuffer.length() > LINE_LIMIT) {
                String tmp = contentBuffer.toString();
                while (tmp.length() > LINE_LIMIT) {
                    msgList.add(tmp.substring(0, LINE_LIMIT));
                    tmp = tmp.substring(LINE_LIMIT);
                }
                contentBuffer.setLength(0);
                contentBuffer.append(prefix);
                contentBuffer.append(tmp);
            }
        }
        if (contentBuffer.length() > 0) {
            msgList.add(contentBuffer.toString());
            contentBuffer.setLength(0);
        }
        for (String t : msgList) {
            switch (level) {
                case Log.INFO:
                    Log.i(TAG, t);
                    break;
                case Log.VERBOSE:
                    Log.v(TAG, t);
                    break;
                case Log.WARN:
                    Log.v(TAG, t);
                    break;
                case Log.ERROR:
                    Log.w(TAG, t);
                    break;
                case Log.DEBUG:
                    Log.d(TAG, t);
                    break;
                default:
            }
        }
    }

    private synchronized String getTag() {
        StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
        String[] classNameInfo = sElements[STACK_TRACE_INDEX].getClassName().split("\\.");
        final String TAG;
        if (classNameInfo.length > 0) {
            TAG = classNameInfo[classNameInfo.length - 1];
        } else {
            TAG = Thread.currentThread().getClass().getName();
        }
        return TAG;
    }

    //TODO i

    /**
     * @param msg
     * @return
     */
    public synchronized Logger defaultTagI(@NonNull Object... msg) {
        return log(Log.INFO, getTag(), msg);
    }


    public synchronized Logger i(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.INFO, TAG, msg);
    }

    public synchronized Logger defaultTagI(byte[] msgBytes, TYPE type) {
        final String TAG = getTag();
        return i(TAG, msgBytes, type);
    }

    public synchronized Logger i(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return i(TAG, msg);
    }


    //TODO v

    /**
     * @param msg
     * @return
     */
    public synchronized Logger defaultTagV(@NonNull Object... msg) {
        return log(Log.VERBOSE, getTag(), msg);
    }

    public synchronized Logger v(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.VERBOSE, TAG, msg);
    }

    public synchronized Logger defaultTagV(byte[] msgBytes, TYPE type) {
        return v(getTag(), msgBytes, type);
    }

    public synchronized Logger v(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return v(TAG, msg);
    }


    //TODO w

    /**
     * @param msg
     * @return
     */
    public synchronized Logger defaultTagW(@NonNull Object... msg) {
        return log(Log.WARN, getTag(), msg);
    }


    public synchronized Logger w(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.WARN, TAG, msg);
    }

    public synchronized Logger defaultTagW(byte[] msgBytes, TYPE type) {
        return w(getTag(), msgBytes, type);
    }

    public synchronized Logger w(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return w(TAG, msg);
    }


    //TODO d

    public synchronized Logger defaultTagD(@NonNull Object... msg) {
        return log(Log.DEBUG, getTag(), msg);
    }


    public synchronized Logger d(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.DEBUG, TAG, msg);
    }


    public synchronized Logger defaultTagD(byte[] msgBytes, TYPE type) {
        return d(getTag(), msgBytes, type);
    }

    public synchronized Logger d(String TAG, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return w(TAG, msg);
    }

    //TODO e

    /**
     * @param msg
     * @return
     */

    public synchronized Logger defaultTagE(@NonNull Object... msg) {
        final String TAG = getTag();
        return log(Log.ERROR, TAG, msg);
    }

    public synchronized Logger e(@NonNull String TAG, @NonNull Object... msg) {
        return log(Log.ERROR, TAG, msg);
    }

    public synchronized Logger defaultTagE(byte[] msgBytes, TYPE type) {
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
            if (file.isDirectory()) {
                file.delete();
                file.createNewFile();
            }
        } else {
            file.createNewFile();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private synchronized void writeLog(String msg) {
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
                filenameBuilder.append(File.separator).append(packageName)
                        .append(File.separator).append("logs").append(File.separator)
                        .append(DateFormatUtil.getDate("yyyyMMdd"));
                File dFile = new File(filenameBuilder.toString());
                filenameBuilder.append(File.separator);
                int index = 1;
                if (dFile.exists()) {
                    if (dFile.isDirectory()) {
                        String[] dFileStrings = dFile.list();
                        if (dFileStrings != null && dFileStrings.length > 0) {
                            index = dFileStrings.length;
                        }
                    } else {
                        dFile.delete();
                        dFile.mkdirs();
                    }
                }
                File file = createFile(String.format("%s%06d.txt", filenameBuilder.toString(), index));
                if (file.length() > LOG_SIZE) {
                    file = createFile(String.format("%s%06d.txt", filenameBuilder.toString(), index + 1));
                }

                logsFOS = new FileOutputStream(file, true);
                logsFOS.write(msg.getBytes());
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
