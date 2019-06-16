package com.hlibrary.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author linwenhui
 * @date 2015/11/17
 */
object InputMethodTools {
    /**
     * 隐藏虚拟键盘
     *
     * @param v
     */
    fun hideKeyboard(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        }
    }

    /**
     * 显示虚拟键盘
     *
     * @param v
     */
    fun showKeyboard(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)

    }

    /**
     * 输入法是否显示着
     *
     * @param edittext
     * @return
     */
    fun keyBoard(edittext: EditText): Boolean {
        var bool = false
        val imm = edittext.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            bool = true
        }
        return bool

    }
}
