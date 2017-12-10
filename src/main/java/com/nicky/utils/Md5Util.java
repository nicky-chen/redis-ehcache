
package com.nicky.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * MD5的工具类
 * 
 */
public class Md5Util {

	/**
	 * 生成MD5的字节数组
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static byte[] genMd5(String text) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(text.getBytes("utf-8"));
	}

	/**
	 * 生成MD5的BASE64编码
	 * @param text
	 * @return
	 */
	public static String genMd5Base64(String text) {
		String str = "";
		if (text != null && !text.equals("")) {
			try {
				//加密后的字符串
				str = Base64.encodeBase64String(genMd5(text));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 生成MD5的16进制编码
	 * @param text
	 * @return
	 */
	public static String genMd5Hex(String text) {
		String str = "";
		if (text != null && !text.equals("")) {
			try {
				//加密后的字符串
				str = byteArrayToString(genMd5(text));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 字节转16进制
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		StringBuilder sb = new StringBuilder();
		int i = b;
		if (i < 0)
			i += 256;
		if (i < 16)
			sb.append("0");
		sb.append(Integer.toHexString(i));
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(Md5Util.genMd5Hex("123456").toUpperCase());
	}
}
