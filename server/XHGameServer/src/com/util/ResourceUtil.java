package com.util;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.common.Config;

/**
 * 2014-7-8
 * 资源文件	
 */
public class ResourceUtil {

	private static Properties prop = null;
	
	private ResourceUtil(){}
	
	
	public static void init() {
		String baseName = "config"+File.separator+"local"+File.separator+"szwz"+"_"+Config.LANGUAGE+"_"+Config.COUNTRY;
		try {
			prop = new Properties();
			
			/**
			 *  ClassLoader.getResourceAsStream 加载的路径是类路径（也就是项目编译后的路径 bin）
			 */
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(baseName+".properties");
			prop.load(is);
		} catch (Exception e) {
			LogUtil.error("异常:",e);
		}
	}
	
	public static String getValue(String key) {
		if (prop == null) {
			init();
		}
		
		String value = prop.getProperty(key);
		if (value == null) {
			return "未知内容";
		}
		return value;
	}
	
	/**
	 * 带参数
	 * */
	public static String getValue(String key, Object...params) {
		
		String value = prop.getProperty(key);
		if (value == null) {
			return "未知内容";
		}
		return MessageFormat.format(value, params);
	}
	
}
