package com.hlibrary.util

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

class SIMCardInfo(context: Context) {

    private val context: Context
    /**
     * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。
     * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
     * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。
     */
    private val telephonyManager: TelephonyManager
    /**
     * 国际移动用户识别码
     */
    private var imsi: String? = null

    /**
     * 获取当前设置的电话号码
     */
    val nativePhoneNumber: String
        get() = telephonyManager.line1Number

    /**
     * Telecom service providers获取手机服务商信息 <BR></BR>
     * 需要加入权限<uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission> <BR></BR>
     */
    // 返回唯一的用户ID;就是这张卡的编号神马的
    // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
    val providersName: String?
        get() {
            var providersName: String? = null
            imsi = telephonyManager.subscriberId
            Logger.instance.d(TAG, " === getProvidersName() === " + imsi!!)
            if (imsi!!.startsWith("46000") || imsi!!.startsWith("46002")) {
                providersName = "中国移动"
            } else if (imsi!!.startsWith("46001")) {
                providersName = "中国联通"
            } else if (imsi!!.startsWith("46003")) {
                providersName = "中国电信"
            }
            return providersName
        }

    val imei: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId
        }

    init {
        this.context = context.applicationContext
        telephonyManager = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    companion object {

        private const val TAG = "SIMCardInfo"
    }
}