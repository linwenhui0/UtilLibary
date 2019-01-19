package com.hlibrary.util.command

/**
 *
 * @author linwh
 * @date 2018/3/5
 */
class CommandResult {
    /** result of command  */
    var exitCode: Int = -1
    /** success message of command result  */
    var successMsg: String = ""
    /** error message of command result  */
    var errorMsg: String = ""

    constructor(result: Int) {
        this.exitCode = result
    }

    constructor(result: Int, successMsg: String, errorMsg: String) {
        this.exitCode = result
        this.successMsg = successMsg
        this.errorMsg = errorMsg
    }

    override fun toString(): String {
        return ("exitCode=" + exitCode + "; successMsg=" + successMsg
                + "; errorMsg=" + errorMsg)
    }
}
