package com.util;

import com.domain.OptLog;


/**
 * 游戏sql帮助类
 * @author ken
 *
 */
public class GameSqlUtil {
	
	private GameSqlUtil() {
		
	}

	/**
	 * bean转换为插入sql
	 * */
	public static String beanToInsertSql(Object obj) {
		
		if (obj instanceof OptLog) {
			// 操作日志
			return ((OptLog)obj).getInsertSql();
		} 
		return null;
	}
	
	
	
}
