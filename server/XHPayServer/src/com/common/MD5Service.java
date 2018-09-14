package com.common;

import java.security.MessageDigest;

public class MD5Service {

	/**
	 * MD5加密字符串(大写)
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encryptToUpperString(String str) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] md5 = digest.digest(str.getBytes("UTF-8"));
		StringBuilder md5StringBuffer = new StringBuilder();
		String part = null;
		for (int i=0;i<md5.length;i++) {
			part = Integer.toHexString(md5[i] & 0xFF);
			if (part.length()==1) {
				part = "0"+part;
			}
			md5StringBuffer.append(part);
		}
		return md5StringBuffer.toString().toUpperCase();
	}

	/**
	 * MD5加密字符串(小写)
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encryptToLowerString(String str) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] md5 = digest.digest(str.getBytes("UTF-8"));
		StringBuffer md5StringBuffer = new StringBuffer();
		String part = null;
		for (int i=0;i<md5.length;i++) {
			part = Integer.toHexString(md5[i] & 0xFF);
			if (part.length()==1) {
				part = "0"+part;
			}
			md5StringBuffer.append(part);
		}
		return md5StringBuffer.toString().toLowerCase();
	}

}
