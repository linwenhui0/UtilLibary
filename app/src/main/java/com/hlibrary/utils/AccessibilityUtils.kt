package com.hlibrary.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils

object AccessibilityUtils {
    /**
     * 判断是否打开辅助功能
     */
    open fun isAccessibilitySettingsOn(context: Context, serviceName: String): Boolean {
        var accessibilityEnabled = 0
        val service = "${context.packageName}/$serviceName"
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.applicationContext.contentResolver, "accessibility_enabled")
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(context.applicationContext.contentResolver, "enabled_accessibility_services")
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    if (mStringColonSplitter.next().equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        context.startActivity(Intent("android.settings.ACCESSIBILITY_SETTINGS"))
        return false
    }

    fun bindAccessibilityService(context: Context, serviceName: String): Boolean {
        if (isAccessibilitySettingsOn(context, serviceName)) {
            return true
        }
        val intent = Intent()
        intent.action = "android.settings.ACCESSIBILITY_SETTINGS"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return false
    }
}