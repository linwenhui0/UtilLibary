package com.hlibrary.utils

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager

object NetworkUtils {
    /**
     * 获得路由mac 地址
     */
    fun getConnectedWifiMacAddress(context: Context): String {
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        Logger.defaultTagI("getConnectedWifiMacAddress")

        val wifiList: List<ScanResult> = wifiManager?.scanResults
        Logger.defaultTagI("getConnectedWifiMacAddress wifiList")
        var info: WifiInfo = wifiManager?.connectionInfo
        Logger.defaultTagI("getConnectedWifiMacAddress wifiList(${wifiList?.size}) info ")
        wifiList?.forEach { result ->
            Logger.defaultTagI("getConnectedWifiMacAddress info ${info?.bssid} = ${result?.BSSID}")
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
}