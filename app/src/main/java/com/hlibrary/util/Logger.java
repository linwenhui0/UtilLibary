package com.hlibrary.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.hlibrary.util.date.DateFormatUtil;
import com.hlibrary.util.file.FileManager;
import com.hlibrary.util.file.SdUtil;

import java.io.File;
import java.io.FileNotFoundException;
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

    /**
     * 格式类型
     *
     * @author linwenhui
     */
    public enum TYPE {
        //具体枚举数据
        DEFAULT("默认"), ASCII("ASCII数据"), CODE16("16进制数据");

        /**
         * 格式类型
         */
        String msg;

        /**
         * 格式类型
         *
         * @param msg
         */
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

    public Logger setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public Logger setFileDebug(boolean fileDebug) {
        this.fileDebug = fileDebug;
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

    private synchronized Logger log(int level, @NonNull String tag, @NonNull Object... msg) {

        if (msg.length <= 0 || this.level > level) {
            return this;
        }


        if (debug) {
            logOnLogcat(level, tag, msg);
        }
        if (fileDebug) {
            StringBuffer msgBuffer = new StringBuffer();
            StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
            String methodName = sElements[STACK_TRACE_INDEX].getMethodName();
            int lineNumber = sElements[STACK_TRACE_INDEX].getLineNumber();
            if (lineNumber < 0) {
                lineNumber = 0;
            }

            if (level != Log.ASSERT) {
                msgBuffer.append(tag).append(" ")
                        .append(DateFormatUtil.Companion.getDate("yyyy-MM-dd HH:mm:ss.SSS")).append(" ")
                        .append(sElements[STACK_TRACE_INDEX].getClassName()).append(" ")
                        .append(methodName).append(" ")
                        .append(lineNumber).append(" ");
            }
            for (Object m : msg) {
                if (m instanceof String) {
                    msgBuffer.append(m).append(" ");
                } else {
                    msgBuffer.append(JSON.toJSONString(m)).append(" ");
                }
            }
            msgBuffer.append(RETURN);
            writeLog(msgBuffer.toString(), level == Log.ASSERT ? "exception" : "logs");
        }
        return this;
    }

    private synchronized void logOnLogcat(int level, @NonNull String tag, @NonNull Object... msg) {
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
                    Log.i(tag, t);
                    break;
                case Log.VERBOSE:
                    Log.v(tag, t);
                    break;
                case Log.WARN:
                    Log.v(tag, t);
                    break;
                case Log.ERROR:
                    Log.w(tag, t);
                    break;
                case Log.DEBUG:
                    Log.d(tag, t);
                    break;
                default:
            }
        }
    }

    private synchronized String getTag() {
        StackTraceElement[] sElements = Thread.currentThread().getStackTrace();
        String[] classNameInfo = sElements[STACK_TRACE_INDEX].getClassName().split("\\.");
        final String tag;
        if (classNameInfo.length > 0) {
            tag = classNameInfo[classNameInfo.length - 1];
        } else {
            tag = Thread.currentThread().getClass().getName();
        }
        return tag;
    }

    //TODO i

    /**
     * @param msg
     * @return
     */
    public synchronized Logger defaultTagI(@NonNull Object... msg) {
        return log(Log.INFO, getTag(), msg);
    }


    public synchronized Logger i(@NonNull String tag, @NonNull Object... msg) {
        return log(Log.INFO, tag, msg);
    }

    public synchronized Logger defaultTagI(byte[] msgBytes, TYPE type) {
        final String tag = getTag();
        return i(tag, msgBytes, type);
    }

    public synchronized Logger i(String tag, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return i(tag, msg);
    }


    //TODO v

    /**
     * @param msg
     * @return
     */
    public synchronized Logger defaultTagV(@NonNull Object... msg) {
        return log(Log.VERBOSE, getTag(), msg);
    }

    public synchronized Logger v(@NonNull String tag, @NonNull Object... msg) {
        return log(Log.VERBOSE, tag, msg);
    }

    public synchronized Logger defaultTagV(byte[] msgBytes, TYPE type) {
        return v(getTag(), msgBytes, type);
    }

    public synchronized Logger v(String tag, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return v(tag, msg);
    }


    //TODO w

    /**
     * @param msg
     * @return
     */
    public synchronized Logger defaultTagW(@NonNull Object... msg) {
        return log(Log.WARN, getTag(), msg);
    }


    public synchronized Logger w(@NonNull String tag, @NonNull Object... msg) {
        return log(Log.WARN, tag, msg);
    }

    public synchronized Logger defaultTagW(byte[] msgBytes, TYPE type) {
        return w(getTag(), msgBytes, type);
    }

    public synchronized Logger w(String tag, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return w(tag, msg);
    }


    //TODO d

    public synchronized Logger defaultTagD(@NonNull Object... msg) {
        return log(Log.DEBUG, getTag(), msg);
    }


    public synchronized Logger d(@NonNull String tag, @NonNull Object... msg) {
        return log(Log.DEBUG, tag, msg);
    }


    public synchronized Logger defaultTagD(byte[] msgBytes, TYPE type) {
        return d(getTag(), msgBytes, type);
    }

    public synchronized Logger d(String tag, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return d(tag, msg);
    }

    //TODO e

    /**
     * @param msg
     * @return
     */

    public synchronized Logger defaultTagE(@NonNull Object... msg) {
        final String tag = getTag();
        return log(Log.ERROR, tag, msg);
    }

    public synchronized Logger e(@NonNull String tag, @NonNull Object... msg) {
        return log(Log.ERROR, tag, msg);
    }

    public synchronized Logger defaultTagE(byte[] msgBytes, TYPE type) {
        return e(getTag(), msgBytes, type);
    }

    public synchronized Logger e(String tag, byte[] msgBytes, TYPE type) {
        final String msg = parseByteArrToString(msgBytes, type);
        return e(tag, msg);
    }

    public synchronized Logger exception(String tag, String msg) {
        return log(Log.ASSERT, tag, msg);
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

    private synchronized void writeLog(String msg, String logs) {
        if (SdUtil.existSDCard()) {
            try {

                StringBuilder filenameBuilder = new StringBuilder();
                if (cacheFile != null) {
                    filenameBuilder.append(cacheFile.getAbsoluteFile().toString());
                } else {
                    filenameBuilder.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                    filenameBuilder.append(File.separator).append("Android").append(File.separator).append("data");
                }
                filenameBuilder.append(File.separator).append(packageName)
                        .append(File.separator).append(logs).append(File.separator)
                        .append(DateFormatUtil.Companion.getDate("yyyyMMdd"));
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
                FileManager.writeFile(file, msg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
