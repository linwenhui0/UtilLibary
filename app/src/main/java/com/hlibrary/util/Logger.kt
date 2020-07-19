package com.hlibrary.util

import android.content.Context
import android.os.Environment
import android.util.Log
import com.alibaba.fastjson.JSON
import com.hlibrary.util.command.CommandTool
import com.hlibrary.util.date.DateFormatUtil
import com.hlibrary.util.file.FileManager
import com.hlibrary.util.file.SdUtil
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * @author linwenhui
 */
class Logger private constructor() {

    @Volatile
    private var level = Log.VERBOSE
    private var debug = true
    private var fileDebug = true
    private var packageName = "com.log.libaray"
    private var cacheFile: File? = null

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

    /**
     * 格式类型
     *
     * @author linwenhui
     */
    enum class TYPE
    /**
     * 格式类型
     *
     * @param msg
     */
    private constructor(
            /**
             * 格式类型
             */
            internal var msg: String) {
        //具体枚举数据
        DEFAULT("默认"),
        ASCII("ASCII数据"),
        CODE16("16进制数据")
    }

    fun setPackageName(context: Context): Logger {
        this.packageName = context.packageName
        return this
    }

    fun setDebug(debug: Boolean): Logger {
        this.debug = debug
        return this
    }

    fun setFileDebug(fileDebug: Boolean): Logger {
        this.fileDebug = fileDebug
        return this
    }

    fun setLevel(level: Int): Logger {
        this.level = level
        return this
    }

    fun setCacheFile(cacheFile: String) {
        this.cacheFile = File(cacheFile)
    }

    private fun parseByteArrToString(msgBytes: ByteArray, type: TYPE): String {
        return when (type) {
            TYPE.ASCII -> StringBuffer().append(type.msg).append(" ").append(HexUtil.asciiByteToString(msgBytes)).toString()
            TYPE.CODE16 -> StringBuffer().append(type.msg).append(" ").append(HexUtil.bytesToHexString(msgBytes)).toString()
            TYPE.DEFAULT -> StringBuffer().append(type.msg).append(" ").append(String(msgBytes)).toString()
            else -> StringBuffer().append(type.msg).append(" ").append(String(msgBytes)).toString()
        }
    }

    @Synchronized
    private fun log(level: Int, tag: String, vararg msg: Any): Logger {
        if (msg.isEmpty() || this.level > level) {
            return this
        }

        if (debug) {
            logOnLogcat(level, tag, *msg)
        }
        if (fileDebug) {
            val msgBuffer = StringBuffer()
            val sElements = Thread.currentThread().stackTrace
            val methodName = if (STACK_TRACE_INDEX < sElements.size) {
                sElements[STACK_TRACE_INDEX].methodName
            } else {
                ""
            }
            var lineNumber =
                    if (STACK_TRACE_INDEX < sElements.size) {
                        sElements[STACK_TRACE_INDEX].lineNumber
                    } else {
                        0
                    }
            if (lineNumber < 0) {
                lineNumber = 0
            }

            if (level != Log.ASSERT) {
                msgBuffer.append(tag).append(" ")
                        .append(DateFormatUtil.getDate("yyyy-MM-dd HH:mm:ss.SSS")).append(" ")
                        .append(sElements[STACK_TRACE_INDEX].className).append(" ")
                        .append(methodName).append(" ")
                        .append(lineNumber).append(" ")
            }
            for (m in msg) {
                if (m is String) {
                    msgBuffer.append(m).append(" ")
                } else {
                    msgBuffer.append(JSON.toJSONString(m)).append(" ")
                }
            }
            msgBuffer.append(RETURN)
            writeLog(msgBuffer.toString(), if (level == Log.ASSERT) "exception" else "logs")
        }
        return this
    }

    @Synchronized
    private fun logOnLogcat(level: Int, tag: String, vararg msg: Any) {
        val sElements = Thread.currentThread().stackTrace
        val methodName = sElements[STACK_TRACE_INDEX].methodName
        var lineNumber = sElements[STACK_TRACE_INDEX].lineNumber
        if (lineNumber < 0) {
            lineNumber = 0
        }

        val msgList = ArrayList<String>()

        val prefix = String.format("%s.%s.%d ", sElements[STACK_TRACE_INDEX].className, methodName, lineNumber)
        val contentBuffer = StringBuffer()
        contentBuffer.append(prefix)
        for (m in msg) {
            if (m is String) {
                contentBuffer.append(m).append(" ")
            } else {
                contentBuffer.append(JSON.toJSONString(m)).append(" ")
            }
            if (contentBuffer.length > LINE_LIMIT) {
                var tmp = contentBuffer.toString()
                while (tmp.length > LINE_LIMIT) {
                    msgList.add(tmp.substring(0, LINE_LIMIT))
                    tmp = tmp.substring(LINE_LIMIT)
                }
                contentBuffer.setLength(0)
                contentBuffer.append(prefix)
                contentBuffer.append(tmp)
            }
        }
        if (contentBuffer.length > 0) {
            msgList.add(contentBuffer.toString())
            contentBuffer.setLength(0)
        }
        for (t in msgList) {
            when (level) {
                Log.INFO -> Log.i(tag, t)
                Log.VERBOSE -> Log.v(tag, t)
                Log.WARN -> Log.v(tag, t)
                Log.ERROR -> Log.w(tag, t)
                Log.DEBUG -> Log.d(tag, t)
            }
        }
    }

    //TODO i

    /**
     * @param msg
     * @return
     */
    @Synchronized
    fun defaultTagI(vararg msg: Any): Logger {
        return log(Log.INFO, tag, *msg)
    }


    @Synchronized
    fun i(tag: String, vararg msg: Any): Logger {
        return log(Log.INFO, tag, *msg)
    }

    @Synchronized
    fun defaultTagI(msgBytes: ByteArray, type: TYPE): Logger {
        val tag = tag
        return i(tag, msgBytes, type)
    }

    @Synchronized
    fun i(tag: String, msgBytes: ByteArray, type: TYPE): Logger {
        val msg = parseByteArrToString(msgBytes, type)
        return i(tag, msg)
    }


    //TODO v

    /**
     * @param msg
     * @return
     */
    @Synchronized
    fun defaultTagV(vararg msg: Any): Logger {
        return log(Log.VERBOSE, tag, *msg)
    }

    @Synchronized
    fun v(tag: String, vararg msg: Any): Logger {
        return log(Log.VERBOSE, tag, *msg)
    }

    @Synchronized
    fun defaultTagV(msgBytes: ByteArray, type: TYPE): Logger {
        return v(tag, msgBytes, type)
    }

    @Synchronized
    fun v(tag: String, msgBytes: ByteArray, type: TYPE): Logger {
        val msg = parseByteArrToString(msgBytes, type)
        return v(tag, msg)
    }


    //TODO w

    /**
     * @param msg
     * @return
     */
    @Synchronized
    fun defaultTagW(vararg msg: Any): Logger {
        return log(Log.WARN, tag, *msg)
    }


    @Synchronized
    fun w(tag: String, vararg msg: Any): Logger {
        return log(Log.WARN, tag, *msg)
    }

    @Synchronized
    fun defaultTagW(msgBytes: ByteArray, type: TYPE): Logger {
        return w(tag, msgBytes, type)
    }

    @Synchronized
    fun w(tag: String, msgBytes: ByteArray, type: TYPE): Logger {
        val msg = parseByteArrToString(msgBytes, type)
        return w(tag, msg)
    }


    //TODO d

    @Synchronized
    fun defaultTagD(vararg msg: Any): Logger {
        return log(Log.DEBUG, tag, *msg)
    }


    @Synchronized
    fun d(tag: String, vararg msg: Any): Logger {
        return log(Log.DEBUG, tag, *msg)
    }


    @Synchronized
    fun defaultTagD(msgBytes: ByteArray, type: TYPE): Logger {
        return d(tag, msgBytes, type)
    }

    @Synchronized
    fun d(tag: String, msgBytes: ByteArray, type: TYPE): Logger {
        val msg = parseByteArrToString(msgBytes, type)
        return d(tag, msg)
    }

    //TODO e

    /**
     * @param msg
     * @return
     */

    @Synchronized
    fun defaultTagE(vararg msg: Any): Logger {
        val tag = tag
        return log(Log.ERROR, tag, *msg)
    }

    @Synchronized
    fun e(tag: String, vararg msg: Any): Logger {
        return log(Log.ERROR, tag, *msg)
    }

    @Synchronized
    fun defaultTagE(msgBytes: ByteArray, type: TYPE): Logger {
        return e(tag, msgBytes, type)
    }

    @Synchronized
    fun e(tag: String, msgBytes: ByteArray, type: TYPE): Logger {
        val msg = parseByteArrToString(msgBytes, type)
        return e(tag, msg)
    }

    @Synchronized
    fun exception(tag: String, msg: String): Logger {
        return log(Log.ASSERT, tag, msg)
    }

    @Synchronized
    @Throws(IOException::class)
    private fun createFile(filename: String): File {
        val file = File(filename)
        val parentFile = file.parentFile
        if (parentFile.exists() && parentFile.isFile) {
            if (parentFile.isFile) {
                parentFile.delete()
                parentFile.mkdirs()
            }
        } else {
            parentFile.mkdirs()
        }
        if (file.exists()) {
            if (file.isDirectory) {
                file.delete()
                file.createNewFile()
            }
        } else {
            file.createNewFile()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    @Synchronized
    fun clearLog() {
        val filenameBuilder = StringBuilder()
        if (cacheFile != null) {
            filenameBuilder.append(cacheFile!!.absoluteFile.toString())
        } else {
            filenameBuilder.append(Environment.getExternalStorageDirectory().absolutePath)
            filenameBuilder.append(File.separator).append("Android").append(File.separator).append("data")
        }
        filenameBuilder.append(File.separator).append(packageName).append(File.separator).append("logs")
        Thread(Runnable {
            try {
                CommandTool().execCommand(arrayOf("rm -fr ${filenameBuilder.toString()}"), false)
            } catch (e: Exception) {
            }
        }).start()
    }

    @Synchronized
    private fun writeLog(msg: String, logs: String) {
        if (SdUtil.existSDCard()) {
            try {
                val filenameBuilder = StringBuilder()
                if (cacheFile != null) {
                    filenameBuilder.append(cacheFile!!.absoluteFile.toString())
                } else {
                    filenameBuilder.append(Environment.getExternalStorageDirectory().absolutePath)
                    filenameBuilder.append(File.separator).append("Android").append(File.separator).append("data")
                }
                filenameBuilder.append(File.separator).append(packageName)
                        .append(File.separator).append(logs).append(File.separator)
                        .append(DateFormatUtil.getDate("yyyyMMdd"))
                val dFile = File(filenameBuilder.toString())
                filenameBuilder.append(File.separator)
                var index = 1
                if (dFile.exists()) {
                    if (dFile.isDirectory) {
                        val dFileStrings = dFile.list()
                        if (dFileStrings != null && dFileStrings.isNotEmpty()) {
                            index = dFileStrings.size
                        }
                    } else {
                        dFile.delete()
                        dFile.mkdirs()
                    }
                }
                var file = createFile(String.format("%s%06d.txt", filenameBuilder.toString(), index))
                if (file.length() > LOG_SIZE) {
                    file = createFile(String.format("%s%06d.txt", filenameBuilder.toString(), index + 1))
                }
                FileManager.writeFile(file, msg)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        private const val LOG_SIZE = 5 * 1024 * 1024
        private const val LINE_LIMIT = 900
        private const val RETURN = "\r\n"
        private const val STACK_TRACE_INDEX = 5

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Logger()
        }
    }
}
