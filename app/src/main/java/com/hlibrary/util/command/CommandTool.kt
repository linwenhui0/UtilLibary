package com.hlibrary.util.command

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeoutException

/**
 * Created by linwenhui on 2018/3/5.
 */

class CommandTool {

    @Throws(IOException::class, InterruptedException::class, TimeoutException::class)
    fun execCommand(commands: Array<String>, isRoot: Boolean): CommandResult {

        var exitCode = -1
        if (commands.isEmpty()) {
            return CommandResult(exitCode)
        }

        var process: Process? = null
        var successReader: BufferedReader? = null
        var errorReader: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var errorMsg: StringBuilder? = null

        var os: DataOutputStream? = null
        process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
        os = DataOutputStream(process!!.outputStream)
        for (command in commands) {
            // donnot use os.writeBytes(commmand), avoid chinese charset
            // error
            os.write(command.toByteArray())
            os.writeBytes("\n")
            os.flush()
        }
        os.writeBytes("exit\n")
        os.flush()

        exitCode = process.waitFor()
        successMsg = StringBuilder()
        errorMsg = StringBuilder()

        successReader = BufferedReader(InputStreamReader(process.inputStream))
        errorReader = BufferedReader(InputStreamReader(process.errorStream))
        var s: String? = successReader.readLine()
        while (s != null) {
            successMsg.append(s!! + "\n")
            s = successReader.readLine()
        }
        s = errorReader.readLine()
        while (s != null) {
            errorMsg.append(s!! + "\n")
            s = errorReader.readLine()
        }

        if (exitCode == -257) {
            throw TimeoutException()
        }

        try {
            os.close()
            successReader.close()
            errorReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        process.destroy()
        return CommandResult(exitCode, successMsg.toString(), errorMsg.toString())
    }
}
