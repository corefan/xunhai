package com.util;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *  正则表达式工具类
 */
public class PatternUtil {
	
	//ip表达式
	public static String ipRegex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

	
    //mail表达式
	@SuppressWarnings("unused")
	private static String mainRegex = "a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	
	
	//匹配验证
	public static boolean isHardRegexpValidate(String source, String regexp){
		
		return Pattern.matches(regexp, source);
		
	}
	
	//身份证格式的正则校验
    public static boolean verForm(String identity) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (identity.matches(reg)) {
        	
        	// 最后一位验证
        	char[] id = {};
            for (int i = 0; i < identity.length(); i++) {
                id = Arrays.copyOf(id, id.length + 1);
                id[id.length - 1] = identity.charAt(i);
            }
            
           int sum = 0;
           int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
           char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
           for (int i = 0; i < id.length - 1; i++) {
               sum += (id[i] - '0') * w[i];
           }
           int c = sum % 11;
           char code = ch[c];
           char last = id[id.length-1];
           last = last == 'x' ? 'X' : last;
           return last == code;
        	
        }        
        return false;
    }
   
}
