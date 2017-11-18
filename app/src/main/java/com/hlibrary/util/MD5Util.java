package com.hlibrary.util;

public class MD5Util {


	public static String digest(String info) {
		try {
			java.security.MessageDigest alga = java.security.MessageDigest.getInstance("MD5");
			alga.update(info.getBytes());
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + "";
		}
		return hs.toLowerCase();
	}

}
