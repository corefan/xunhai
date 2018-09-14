package com.util;

import java.text.DecimalFormat;
import java.util.Random;

import com.common.MD5Service;

public class CommonUtil {

	private static Random random = new Random();
	
	/**
	 * 保留小数位
	 * type:小数位
	 * */
	public static double formatDouble(double value, Integer type) {
		DecimalFormat df = new DecimalFormat();
		
		switch (type) {
		case 1:
			df.applyPattern("#.0");
			break;
		case 2:
			df.applyPattern("#.00");
			break;
		case 3:
			df.applyPattern("#.000");
			break;
		}
		
		return Double.parseDouble(df.format(value));
	}
	
	/** 随机登陆key */
	public static String randomLoginKey(String userName) {
		
		int num = random.nextInt(10000);
		
		String key = "aaa";
		try {
			key = MD5Service.encryptToUpperString(userName+"###"+num+"qidian");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return key;
	}
	
}
