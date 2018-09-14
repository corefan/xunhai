package com.util;

import com.domain.ActCode;

/**
 * @author barsk
 * 2014-5-10
 * sql帮助类	
 */
public class GameSqlUtil {

private GameSqlUtil() {
		
	}

	/**
	 * bean转换为插入sql
	 * */
	public static String beanToInsertSql(Object obj) {
		
		if (obj instanceof ActCode) {
			// 激活码
			return ((ActCode)obj).getInsertSql();
		} 
		
		return null;
	}
	/**
	 * bean转换为更新sql
	 * */
	public static String beanToUpdateSql(Object obj) {
		if (obj instanceof ActCode) {
			return ((ActCode)obj).getUpdateSql();
		}
		
		return null;
	}
}
