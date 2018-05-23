package com.hlibrary.util.command;

/**
 * Created by linwenhui on 2018/3/5.
 */

public class CommandResult {
    /** result of command **/
    public int exitCode;
    /** success message of command result **/
    public String successMsg;
    /** error message of command result **/
    public String errorMsg;

    public CommandResult(int result) {
        this.exitCode = result;
    }

    public CommandResult(int result, String successMsg, String errorMsg) {
        this.exitCode = result;
        this.successMsg = successMsg;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "exitCode=" + exitCode + "; successMsg=" + successMsg
                + "; errorMsg=" + errorMsg;
    }
}
