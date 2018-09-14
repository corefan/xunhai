package com.util;

/** 校验sql工具类*/
public final class CheckSqlUtil {
	
	public static boolean checkSelectSql(String selectSql){
		
		  selectSql=selectSql.toUpperCase();//测试用sql语句
		  String column="(\\w+\\s*(\\w+\\s*){0,1})";//一列的正则表达式 匹配如 product p
		  String columns=column+"(,\\s*"+column+")*"; //多列正则表达式 匹配如 product p,category c,warehouse w
		  String ownerenable="((\\w+\\.){0,1}\\w+\\s*(\\w+\\s*){0,1})";//一列的正则表达式 匹配如 a.product p
		  String ownerenables=ownerenable+"(,\\s*"+ownerenable+")*";//多列正则表达式 匹配如 a.product p,a.category c,b.warehouse w
	
		  String from="FROM\\s+"+columns;
		  String condition="(\\w+\\.){0,1}\\w+\\s*(=|LIKE|IS)\\s*'?(\\w+\\.){0,1}[\\w%]+'?";//条件的正则表达式 匹配如 a=b 或 a is b..
		  String conditions=condition+"(\\s+(AND|OR)\\s*"+condition+"\\s*)*";//多个条件 匹配如 a=b and c like 'r%' or d is null 
		  String where="(WHERE\\s+"+conditions+"){0,1}";
		  String pattern="SELECT\\s+(\\*|"+ownerenables+"\\s+"+from+")\\s+"+where+"\\s*"; //匹配最终sql的正则表达式
		  
		  /** 是否匹配*/
		  return selectSql.matches(pattern);
	}
	
}
