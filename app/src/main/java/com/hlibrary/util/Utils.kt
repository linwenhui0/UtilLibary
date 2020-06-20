package com.hlibrary.util

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
import com.hlibrary.util.constants.Constants
import java.io.File
import java.net.NetworkInterface
import java.util.*


/**
 * 通用工具
 */
object Utils {

    /**
     * @param mobiles 手机号码
     * @return true 手机号码格式合法,false 手机号码格式不合法
     */
    fun isMobileNO(mobiles: String): Boolean {
        return mobiles.matches(Constants.PHONE_REGEX.toRegex())
    }

    /**
     * @param mobiles 手机号码
     * @param separator 分隔符
     * @return true 手机号码格式合法,false 手机号码格式不合法
     */
    fun isFormatMobileNO(mobiles: String, separator: String): Boolean {
        val telRegex = String.format(Constants.PHONE_FORMAT_REGEX, separator, separator)
        return mobiles.matches(telRegex.toRegex())
    }

    /**
     * 是否root
     */
    fun isRoot(): Boolean {
        var f: File? = null
        val kSuSearchPaths = arrayOf("/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/")
        try {
            for (i in kSuSearchPaths.indices) {
                f = File(kSuSearchPaths[i] + "su")
                if (f?.exists()) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 是否正在使用VPN
     */
    fun isVpnConnected(): Boolean {
        try {
            val niList = NetworkInterface.getNetworkInterfaces()
            if (niList != null) {
                for (intf in Collections.list(niList)) {
                    if (!intf.isUp || intf.interfaceAddresses.size == 0) {
                        continue
                    }
                    if ("tun0".equals(intf.name) || "ppp0".equals(intf.name)) {
                        return true
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 判断设备 是否使用代理上网
     */
    fun isWifiProxy(context: Context): Boolean {
        // 是否大于等于4.0
        var proxyAddress: String = ""
        var proxyPort: Int
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                var host = System.getProperty("http.proxyHost")
                if (!TextUtils.isEmpty(host)) {
                    proxyAddress = "$host"
                }
                val portStr = System.getProperty("http.proxyPort")
                proxyPort = Integer.parseInt(portStr ?: "-1")
            } else {
                proxyAddress = android.net.Proxy.getHost(context)
                proxyPort = android.net.Proxy.getPort(context)
            }
        } catch (e: Exception) {
            proxyAddress = ""
            proxyPort = -1
        }
        return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
    }


    /**
     * 获得路由mac 地址
     */
    fun getConnectedWifiMacAddress(context: Context): String {
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        Logger.instance.defaultTagI("getConnectedWifiMacAddress")

        val wifiList: List<ScanResult> = wifiManager?.scanResults
        Logger.instance.defaultTagI("getConnectedWifiMacAddress wifiList")
        var info: WifiInfo = wifiManager?.connectionInfo
        Logger.instance.defaultTagI("getConnectedWifiMacAddress wifiList(${wifiList?.size}) info ")
        wifiList?.forEach { result ->
            Logger.instance.defaultTagI("getConnectedWifiMacAddress info ${info?.bssid} = ${result?.BSSID}")
            if (info?.bssid.equals(result?.BSSID)) {
                return result.BSSID
            }
        }
        return ""
    }

    fun getMacAddress(context: Context): String {
        var mac = ""
        try {
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            mac = wifi.connectionInfo.macAddress
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mac
    }

    /**
     * 获得代理ip
     */
    fun getProxyHost(context: Context): String {
        var proxyAddress: String = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                val host = System.getProperty("http.proxyHost")
                if (!TextUtils.isEmpty(host)) {
                    proxyAddress = "$host"
                }
            } else {
                proxyAddress = android.net.Proxy.getHost(context)
            }
        } catch (e: Exception) {
        }
        return proxyAddress
    }

    /**
     * 获得代码端口
     */
    fun getProxyPort(context: Context): String {
        var proxyPort: String = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                val host = System.getProperty("http.proxyPort")
                if (!TextUtils.isEmpty(host)) {
                    proxyPort = "$host"
                }
            } else {
                proxyPort = "${android.net.Proxy.getPort(context)}"
            }
        } catch (e: Exception) {
        }
        return proxyPort
    }

    /**
     * 判断是否打开辅助功能
     */
    open fun isAccessibilitySettingsOn(context: Context, serviceName: String): Boolean {
        var accessibilityEnabled = 0
        val service = "${context.packageName}/$serviceName"
        try {
            accessibilityEnabled = Secure.getInt(context.applicationContext.contentResolver, "accessibility_enabled")
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }

        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Secure.getString(context.applicationContext.contentResolver, "enabled_accessibility_services")
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

    /**
     * 电源优化白名单
     */
    fun ignoreBatteryOptimization(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager: PowerManager? = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
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