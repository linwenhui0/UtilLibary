package com.hlibrary.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import java.io.File
import java.net.NetworkInterface
import java.util.*

object VpnUtils {
    /**
     * 是否root
     */
    fun isRoot(): Boolean {
        val kSuSearchPaths =
            arrayOf("/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/")
        try {
            for (i in kSuSearchPaths.indices) {
                val f = File(kSuSearchPaths[i] + "su")
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
}