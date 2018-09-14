package com.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.MD5Service;

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
	
	/** 解析消息 */
	public  static JSONObject dealMsg(HttpServletRequest req) {
		
		JSONObject jsonObject = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			String msg = null;

			os = new ByteArrayOutputStream();
			is = req.getInputStream();
			if (is != null) {
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = is.read(b)) != -1) {
					os.write(b,0,len);
				}
				msg = os.toString();
			}
			
			String result = new String(msg.getBytes(Charset.defaultCharset()), "UTF-8");
			jsonObject = new JSONObject(result);
			
		} catch (Exception e) {
			LogUtil.error("异常:",e);
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				LogUtil.error("异常:",e);
			}
		}

		return jsonObject;
	}
	
	/**
	 * 发送信息
	 */
	public  static void postData(HttpServletResponse response, String result) throws IOException {
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
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
