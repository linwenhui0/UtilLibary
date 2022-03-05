package com.hlibrary.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeoutException

/**
 * @author linwenhui
 * @date 2018/3/5
 */
object CommandUtils {

    fun execCommand(commands: Array<String>, isRoot: Boolean): List<String>? {

        if (commands.isEmpty()) {
            return null
        }
        try {
            val process = if (isRoot) {
                Runtime.getRuntime().exec("su")
            } else {
                Runtime.getRuntime().exec("sh")
            }
            val os = DataOutputStream(process?.outputStream)
            for (command in commands) {
                os.writeBytes(command)
                os.writeBytes("\n")
                os.flush()
            }
            os.writeBytes("exit\n")
            os.flush()

            val exitCode = process.waitFor()
            val lines = ArrayList<String>()
            val successReader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String? = successReader.readLine()
            while (line != null) {
                lines.add(line)
                line = successReader.readLine()
            }

            if (exitCode == -257) {
                throw TimeoutException()
            }

            try {
                os.close()
                successReader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            process.destroy()
            return lines
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
