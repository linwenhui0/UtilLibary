package com.hlibrary.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Drawable


object ApkInfoUtil {

    /**
     * 获取程序 图标
     */
    fun getAppIcon(context: Context, packname: String): Drawable? {
        val pm = context.packageManager
        try {
            val info = pm.getApplicationInfo(packname, 0)
            return info.loadIcon(pm)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取程序的版本名称
     */
    fun getAppVersion(context: Context): String? {
        val pm = context.packageManager
        try {
            val packinfo = pm.getPackageInfo(context.packageName, 0)
            return packinfo.versionName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取程序的版本号
     */
    fun getAppCode(context: Context): Long {
        val pm = context.packageManager
        try {
            val packinfo = pm.getPackageInfo(context.packageName, 0)
            return packinfo.longVersionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 1
    }

    /**
     * 获取程序的名字
     */
    fun getAppName(context: Context): String? {
        val pm = context.packageManager
        try {
            val info = pm.getApplicationInfo(context.packageName, 0)
            return info.loadLabel(pm).toString()
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取程序的权限
     */
    fun getAppPremission(context: Context, packname: String): Array<String>? {
        val pm = context.packageManager
        try {
            val packinfo = pm.getPackageInfo(packname,
                    PackageManager.GET_PERMISSIONS)
            // 获取到所有的权限
            return packinfo.requestedPermissions

        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取程序的签名
     */
    fun getAppSignature(context: Context, packname: String): String? {
        val pm = context.packageManager
        try {
            val packinfo = pm.getPackageInfo(packname,
                    PackageManager.GET_SIGNATURES)
            // 获取到所有的权限
            return packinfo.signatures[0].toCharsString()

        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取程序的版本的包名
     */
    fun getAppPackage(context: Context): String? {
        val info: PackageInfo
        try {
            info = context.packageManager.getPackageInfo(
                    context.packageName, 0)
            return info.packageName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


}
