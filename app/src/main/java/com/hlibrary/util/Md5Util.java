package com.hlibrary.util;

/**
 * @author linwenhui
 * @date 2015-01-01
 * @updateDate 20190216
 */
public class Md5Util {


    /**
     * md5加密
     *
     * @param info
     * @return
     */
    public static String digest(String info) {
        try {
            java.security.MessageDigest alga = java.security.MessageDigest.getInstance("MD5");
            alga.update(info.getBytes());
            byte[] digesta = alga.digest();
            return HexUtil.bytesToHexString(digesta);
        } catch (Exception ex) {
            return null;
        }
    }


}
