package com.hlibrary.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author linwenhui
 * @date 2017/3/2
 */

public class HexUtil {
    private HexUtil() {
    }

    /**
     * 16进制字符串转成字符数据
     *
     * @param hex 16进制字符串
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] hexChars = hex.toUpperCase().toCharArray();

        for (int i = 0; i < len; ++i) {
            int pos = i * 2;
            try {
                result[i] = Byte.parseByte(String.valueOf(hexChars[pos]) + String.valueOf(hexChars[pos + 1]), 16);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 转换成指定长度的字节数组
     *
     * @param hex  16进制字符串
     * @param num  字符长度
     * @param fill 填充字符
     * @return
     * @throws IOException
     */
    public static byte[] hexStringToByte(String hex, int num, byte fill) throws IOException {
        final int len = hex.length();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(hexStringToByte(hex));
        final int iLen = num - len;
        if (iLen > 0) {
            for (int i = 0; i < iLen; i++) {
                bos.write(fill);
            }
        }
        byte[] result = bos.toByteArray();
        bos.close();
        return result;
    }

    /**
     * byte数组转成16进制字符串
     *
     * @param bytes
     * @return
     */
    public static final String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuffer hexBuffer = new StringBuffer(bytes.length * 2);

        for (byte b : bytes) {
            hexBuffer.append(String.format("%02x", b).toUpperCase());
        }

        return hexBuffer.toString();
    }

    /**
     * byte数组转int
     *
     * @param b
     * @return
     */
    public static int bytes2int(byte[] b) {
        int res = 0;
        final int len = b.length;
        for (int i = 0; i < len; ++i) {
            res <<= 8;
            int var5 = b[i] & 0xFF;
            res |= var5;
        }

        return res;
    }

    public static String bcd2str(byte[] bcds) {
        if (bcds == null) {
            return "";
        } else {
            char[] ascii = "0123456789abcdef".toCharArray();
            byte[] temp = new byte[bcds.length * 2];

            for (int res = 0; res < bcds.length; ++res) {
                temp[res * 2] = (byte) (bcds[res] >> 4 & 15);
                temp[res * 2 + 1] = (byte) (bcds[res] & 15);
            }

            StringBuffer var5 = new StringBuffer();

            for (int i = 0; i < temp.length; ++i) {
                var5.append(ascii[temp[i]]);
            }

            return var5.toString().toUpperCase();
        }
    }

    public static String asciiByteToString(byte[] data) {
        int len = data.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) data[i];
        }
        return String.valueOf(chars);
    }

    public static byte[] asciiStringToAscii(String source, int minLen, byte fillByte) throws IOException {
        char[] asciiChars = source.toCharArray();
        final int len = asciiChars.length;

        byte[] asciiBytes = new byte[len];
        System.arraycopy(asciiBytes, 0, asciiBytes, 0, len);
        if (minLen > 0 && len < minLen) {
            final int lessLen = minLen - len;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(asciiBytes);
            for (int i = 0; i < lessLen; i++) {
                bos.write(fillByte);
            }
            asciiBytes = bos.toByteArray();
            bos.close();
        }
        return asciiBytes;
    }

    public static byte[] intToAscii(int data, int minLen) throws IOException {
        String s = String.format("%" + minLen + "d", data).replace(" ", "0");
        return asciiStringToAscii(s, 0, (byte) ' ');
    }

    /**
     * 右靠，左补空格
     *
     * @param source
     * @param len
     * @param fill
     * @return
     */
    public static String formatLeftAlign(String source, int len, String fill) {
        return String.format("%" + len + "s", source).replace(" ", fill);
    }

    /**
     * 左靠，右补空格
     *
     * @param source
     * @param len
     * @param fill
     * @return
     */
    public static String formatRightAlign(String source, int len, String fill) {
        return String.format("%-" + len + "s", source).replace(" ", fill);
    }

    public static String fillChar(int len, String fill) {
        String temp = String.format("%" + len + "s", fill);
        return temp.replace(" ", fill);
    }

    public static byte[] int2bytes(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * 字符串转ascii码
     *
     * @param source
     * @return
     */
    public static byte[] toByteArray(String source) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = source.toCharArray();
        for (char c : chars) {
            sbu.append(Integer.toHexString((int) c));
        }
        return hexStringToByte(sbu.toString());
    }

}