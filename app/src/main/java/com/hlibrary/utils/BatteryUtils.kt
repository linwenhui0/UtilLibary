package com.hlibrary.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.Secure
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.text.TextUtils.SimpleStringSplitter
import com.hlibrary.utils.constants.Constants
import java.io.File
import java.net.NetworkInterface
import java.util.*


/**
 * 电源工具
 */
object BatteryUtils {

    /**
     * 电源优化白名单
     */
    fun ignoreBatteryOptimization(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager: PowerManager? =
                context.getSystemService(Context.POWER_SERVICE) as PowerManager?
            val hasIgnore = powerManager?.isIgnoringBatteryOptimizations(context.packageName)
            if (hasIgnore != true) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:${context.packageName}")
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }
    }


}