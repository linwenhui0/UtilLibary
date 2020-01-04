package com.hlibrary.util

import android.content.Context
import android.net.ConnectivityManager
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
            telephonyManager.deviceId
        } else {
            telephonyManager.deviceId
        }

    /**
     * 获取网络类型
     *
     * @return
     */
    fun getCurrentNetworkType(): String {
        val networkClass = getNetworkClass()
        var type = "未知"
        when (networkClass) {
            NETWORK_CLASS_UNAVAILABLE -> type = "无"
            NETWORK_CLASS_WIFI -> type = "Wi-Fi"
            NETWORK_CLASS_2_G -> type = "2G"
            NETWORK_CLASS_3_G -> type = "3G"
            NETWORK_CLASS_4_G -> type = "4G"
        }
        return type
    }


    private fun getNetworkClass(): Int {
        var networkType = NETWORK_TYPE_UNKNOWN
        try {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
            val network = connectivityManager.activeNetworkInfo
            if (network != null && network.isAvailable && network.isConnected) {
                val type = network.type
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    val telephonyManager = context.getSystemService(
                            Context.TELEPHONY_SERVICE) as TelephonyManager
                    networkType = telephonyManager.networkType
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return getNetworkClassByType(networkType)

    }


    private fun getNetworkClassByType(networkType: Int): Int {
        when (networkType) {
            NETWORK_TYPE_UNAVAILABLE -> return NETWORK_CLASS_UNAVAILABLE
            NETWORK_TYPE_WIFI -> return NETWORK_CLASS_WIFI
            NETWORK_TYPE_GPRS, NETWORK_TYPE_EDGE, NETWORK_TYPE_CDMA, NETWORK_TYPE_1xRTT, NETWORK_TYPE_IDEN -> return NETWORK_CLASS_2_G
            NETWORK_TYPE_UMTS, NETWORK_TYPE_EVDO_0, NETWORK_TYPE_EVDO_A, NETWORK_TYPE_HSDPA, NETWORK_TYPE_HSUPA, NETWORK_TYPE_HSPA, NETWORK_TYPE_EVDO_B, NETWORK_TYPE_EHRPD, NETWORK_TYPE_HSPAP -> return NETWORK_CLASS_3_G
            NETWORK_TYPE_LTE -> return NETWORK_CLASS_4_G
            else -> return NETWORK_CLASS_UNKNOWN
        }
    }

    init {
        this.context = context.applicationContext
        telephonyManager = context
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    companion object {

        private const val TAG = "SIMCardInfo"

        const val NETWORK_TYPE_UNAVAILABLE = -1
        // private static final int NETWORK_TYPE_MOBILE = -100;
        const val NETWORK_TYPE_WIFI = -101

        const val NETWORK_CLASS_WIFI = -101
        const val NETWORK_CLASS_UNAVAILABLE = -1
        /** Unknown network class.  */
        const val NETWORK_CLASS_UNKNOWN = 0
        /** Class of broadly defined "2G" networks.  */
        const val NETWORK_CLASS_2_G = 1
        /** Class of broadly defined "3G" networks.  */
        const val NETWORK_CLASS_3_G = 2
        /** Class of broadly defined "4G" networks.  */
        const val NETWORK_CLASS_4_G = 3

        // 适配低版本手机
        /** Network type is unknown  */
        const val NETWORK_TYPE_UNKNOWN = 0
        /** Current network is GPRS  */
        const val NETWORK_TYPE_GPRS = 1
        /** Current network is EDGE  */
        const val NETWORK_TYPE_EDGE = 2
        /** Current network is UMTS  */
        const val NETWORK_TYPE_UMTS = 3
        /** Current network is CDMA: Either IS95A or IS95B  */
        const val NETWORK_TYPE_CDMA = 4
        /** Current network is EVDO revision 0  */
        const val NETWORK_TYPE_EVDO_0 = 5
        /** Current network is EVDO revision A  */
        const val NETWORK_TYPE_EVDO_A = 6
        /** Current network is 1xRTT  */
        const val NETWORK_TYPE_1xRTT = 7
        /** Current network is HSDPA  */
        const val NETWORK_TYPE_HSDPA = 8
        /** Current network is HSUPA  */
        const val NETWORK_TYPE_HSUPA = 9
        /** Current network is HSPA  */
        const val NETWORK_TYPE_HSPA = 10
        /** Current network is iDen  */
        const val NETWORK_TYPE_IDEN = 11
        /** Current network is EVDO revision B  */
        const val NETWORK_TYPE_EVDO_B = 12
        /** Current network is LTE  */
        const val NETWORK_TYPE_LTE = 13
        /** Current network is eHRPD  */
        const val NETWORK_TYPE_EHRPD = 14
        /** Current network is HSPA+  */
        const val NETWORK_TYPE_HSPAP = 15
    }
}