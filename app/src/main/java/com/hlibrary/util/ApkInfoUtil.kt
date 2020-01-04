package com.hlibrary.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Drawable
import com.hlibrary.util.file.StreamTool
import java.io.InputStream
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.util.zip.ZipFile


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
    fun getAppSignature(context: Context, algorithm: String = "MD5"): String? {
        val appInfo = context.applicationInfo
        val sourceDir = appInfo?.sourceDir
        try {
            if (sourceDir?.isNotEmpty() == true) {
                val zipFile = ZipFile(sourceDir)
                val entries = zipFile.entries()
                while (entries?.hasMoreElements() == true) {
                    val entry = entries.nextElement()
                    val entryName = entry.name
                    if (entryName == "META-INF/CERT.RSA") {
                        val inputStream: InputStream = zipFile.getInputStream(entry)
                        val cf = CertificateFactory.getInstance("X509")
                        val c = cf.generateCertificate(inputStream)
                        val md = MessageDigest.getInstance(algorithm)
                        val data = md.digest(c.encoded)
                        return HexUtil.bytesToHexString(data)
                    }
                }
            }
        } catch (e: Exception) {
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
