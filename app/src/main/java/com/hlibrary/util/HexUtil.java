package com.hlibrary.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by linwenhui on 2017/3/2.
 */

public class HexUtil {
    private HexUtil() {
    }

    public static byte[] hexStringToByte(String hex) {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toUpperCase().toCharArray();

        for (int i = 0; i < len; ++i) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

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

    public static final String bytesToHexString(byte[] bArray) {
        if (bArray == null)
            return null;
        StringBuffer sb = new StringBuffer(bArray.length);
        int j = 0;

        for (int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
            ++j;
        }

        return sb.toString();
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static byte[] int2bytes(int num) {
        byte[] b = new byte[4];
        boolean mask = true;

        for (int i = 0; i < 4; ++i) {
            b[i] = (byte) (num >>> 24 - i * 8);
        }

        return b;
    }

    public static int bytes2int(byte[] b) {
        short mask = 255;
        boolean temp = false;
        int res = 0;

        for (int i = 0; i < 4; ++i) {
            res <<= 8;
            int var5 = b[i] & mask;
            res |= var5;
        }

        return res;
    }

    public static int bytes2short(byte[] b) {
        short mask = 255;
        boolean temp = false;
        int res = 0;

        for (int i = 0; i < 2; ++i) {
            res <<= 8;
            int var5 = b[i] & mask;
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

    public static String asciiByteToString(byte[] datas) {
        int len = datas.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) datas[i];
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
            for (int i = 0; i < lessLen; i++)
                bos.write(fillByte);
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
    public static String formatLeftAign(String source, int len, String fill) {
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
    public static String formatRightAign(String source, int len, String fill) {
        return String.format("%-" + len + "s", source).replace(" ", fill);
    }

    public static String fillChar(int len, String fill) {
        String temp = String.format("%" + len + "s", fill);
        return temp.replace(" ", fill);
    }

    public static String subString(String source, int len, String fill) {
        final int iLen = source.length();
        if (iLen > len) {
            return source.substring(0, len);
        }
        return String.format("%" + len + "s", source).replace(" ", fill);
    }

    public static byte[] intToByteArray(int a) {
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