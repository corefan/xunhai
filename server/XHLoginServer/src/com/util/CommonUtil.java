package com.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.common.MD5Service;

/**
 * 公共帮助类
 * @author ken
 * @date 2018年9月18日
 */
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
	
	
	/** 
     * 大陆号码或香港号码均可 
     */  
    public static boolean isPhoneLegal(String str)throws PatternSyntaxException {  
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);  
    }  
  
    /** 
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 
     * 此方法中前三位格式有： 
     * 13+任意数 
     * 15+除4的任意数 
     * 18+除1和4的任意数 
     * 17+除9的任意数 
     * 147 
     */  
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {  
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }  
  
    /** 
     * 香港手机号码8位数，5|6|8|9开头+7位任意数 
     */  
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {  
        String regExp = "^(5|6|8|9)\\d{7}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(str);  
        return m.matches();  
    }  
      
    
	/** 随机登陆key */
	public static String randomLoginKey(String userName) {
		
		double num = Math.random()*10000;
		
		String key = "aaa";
		try {
			key = MD5Service.encryptToUpperString(userName+"###"+num+"xhgame");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return key;
	}
	
}
