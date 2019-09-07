package com.hlibrary.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.hlibrary.util.encrypt.Md5Util
import java.util.*


class GetDeviceId {

    companion object {

        /**
         * Pseudo-Unique ID, 这个在任何Android手机中都有效
         * 有一些特殊的情况，一些如平板电脑的设置没有通话功能，或者你不愿加入READ_PHONE_STATE许可。而你仍然想获得唯
         * 一序列号之类的东西。这时你可以通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出
         * 来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）。但应当明白的是，出现类似情况的可能性基
         * 本可以忽略。大多数的Build成员都是字符串形式的，我们只取他们的长度信息。我们取到13个数字，并在前面加上“35
         * ”。这样这个ID看起来就和15位IMEI一样了。
         *
         * @return PesudoUniqueID
         */
        @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
        private fun getPesudoUniqueID(): String {
            val devIdShort = "35" + //we make this look like a valid IMEI
                    Build.BOARD.length % 10 +
                    Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 +
                    Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 +
                    Build.HOST.length % 10 +
                    Build.ID.length % 10 +
                    Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 +
                    Build.TYPE.length % 10 +
                    Build.USER.length % 10
            val serial: String
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                serial = Build.SERIAL
            } else {
                serial = Build.getSerial()
            }
            return UUID(devIdShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        }

        /**
         * 只在有蓝牙的设备上运行。并且要加入android.permission.BLUETOOTH 权限.Returns: 43:25:78:50:93:38 .
         * 蓝牙没有必要打开，也能读取。
         *
         * @return m_szBTMAC
         */
        @RequiresPermission(android.Manifest.permission.BLUETOOTH)
        private fun getBTMACAddress(): String {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            return bluetoothAdapter.address
        }


        /**
         * Combined Device ID
         * 综上所述，我们一共有五种方式取得设备的唯一标识。它们中的一些可能会返回null，或者由于硬件缺失、权限问题等
         * 获取失败。但你总能获得至少一个能用。所以，最好的方法就是通过拼接，或者拼接后的计算出的MD5值来产生一个结果。
         * 通过算法，可产生32位的16进制数据:9DDDF85AFF0A87974CE4541BD94D5F55
         *
         * @return
         */
        @RequiresPermission(android.Manifest.permission.BLUETOOTH)
        fun getUniqueID(): String? {
            val szLongID = getPesudoUniqueID() + getBTMACAddress()
            Logger.instance.defaultTagD("getUniqueID $szLongID")
            return Md5Util.digest(szLongID)
        }

    }


}