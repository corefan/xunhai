package com.util;

import java.text.DecimalFormat;

public class CommonUtil {

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
}
