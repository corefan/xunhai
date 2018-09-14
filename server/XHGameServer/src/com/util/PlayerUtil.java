package com.util;

import com.common.Config;

public class PlayerUtil {
	
	/** 名称转换*/
	public static String convertName(String name, int serveNo){
		
		return "s" + serveNo + "." + name;
	}
	
	/**
	 * 获取全局标志
	 */
	public static String getGuid(int type, long objectID){
		return Config.GAME_SITE + "_" + type + "_" + objectID;
	}
	
	/**
	 * 获取单位类型
	 */
	public static int getType(String guid){
		String[] lists = guid.split("_");
		return Integer.valueOf(lists[lists.length-2]);
	}
	
	/**
	 * 取实体编号
	 */
	public static int getIntId(String guid){
		String[] lists = guid.split("_");
		return Integer.valueOf(lists[lists.length-1]);
	}
	
	/**
	 * 取实体编号
	 */
	public static long getLongId(String guid){
		String[] lists = guid.split("_");
		return Long.valueOf(lists[lists.length-1]);
	}
	
	/**
	 * 场景唯一编号
	 */
	public static String getSceneGuid(int type, int mapId, int line){
		return type + "_" + mapId + "_" + line;
	}
	
	/**
	 * 场景唯一编号
	 */
	public static String getSceneGuid(int type, long objectId){
		return type + "_" + objectId;
	}
	
	/**
	 * 取副本类型
	 */
	public static int getInstanceType(String sceneGuid){
		String[] lists = sceneGuid.split("_");
		return Integer.valueOf(lists[0]);
	}
}
