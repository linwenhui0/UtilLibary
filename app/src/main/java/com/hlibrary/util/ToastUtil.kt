package com.hlibrary.util

import android.content.Context
import android.support.annotation.IntRange
import android.support.annotation.StringRes
import android.widget.Toast

import java.lang.ref.WeakReference

/**
 * @author linwenhui
 */
class ToastUtil private constructor() {

    @Synchronized
    fun showToast(context: Context, text: String, duration: Int) {
        var mToast: Toast? = null
        if (mWeakToast != null) {
            mToast = mWeakToast!!.get()
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration)
            mWeakToast = null
        } else {
            mToast.setText(text)
            mToast.duration = duration
        }
        if (mWeakToast == null) {
            mWeakToast = WeakReference<Toast>(mToast)
        }
        mToast!!.show()
    }

    @Synchronized
    fun showToast(context: Context, @StringRes resId: Int, duration: Int) {
        var mToast: Toast? = null
        if (mWeakToast != null) {
            mToast = mWeakToast!!.get()
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, duration)
            mWeakToast = null
        } else {
            mToast.setText(resId)
            mToast.duration = duration
        }
        if (mWeakToast == null)
            mWeakToast = WeakReference<Toast>(mToast)
        mToast!!.show()
    }

    companion object {


        private var instance: ToastUtil? = null
        private var mWeakToast: WeakReference<Toast>? = null

        fun getInstance(): ToastUtil? {
            if (instance == null) {
                synchronized(ToastUtil::class.java) {
                    if (instance == null) {
                        instance = ToastUtil()
                    }
                }
            }
            return instance
        }

        fun showLongTime(context: Context, text: String) {
            ToastUtil.getInstance()?.showToast(context, text, Toast.LENGTH_LONG)
        }

        fun showShortTime(context: Context, text: String) {
            ToastUtil.getInstance()?.showToast(context, text, Toast.LENGTH_SHORT)
        }

        fun showCustomTime(context: Context, text: String, @IntRange(from = 1) duration: Int) {
            if (duration > 0)
                ToastUtil.getInstance()?.showToast(context, text, duration)
        }

        fun showLongTime(context: Context, @StringRes resId: Int) {
            ToastUtil.getInstance()?.showToast(context, resId, Toast.LENGTH_LONG)
        }

        fun showShortTime(context: Context, @StringRes resId: Int) {
            ToastUtil.getInstance()?.showToast(context, resId, Toast.LENGTH_SHORT)
        }

        fun showCustomTime(context: Context, @StringRes resId: Int, @IntRange(from = 1) duration: Int) {
            ToastUtil.getInstance()?.showToast(context, resId, duration)
        }
    }

}
