package com.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author barsk
 * 2013-10-30
 * 缓存数据service 
 */
public class CacheService {

	private static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<String, Object>(1 << 8, 0.75f, 1 << 3);
	
	/**
	 * 数据放入缓存
	 * @param key
	 * @param value
	 */
	public static void putToCache(String key, Object value) {
		cacheMap.put(key, value);
	}
	
	/**
	 * 缓存中获得数据
	 * @param key
	 * @return
	 */
	public static Object getFromCache(String key) {
		return cacheMap.get(key);
	}
	
	/**
	 * 删除缓存数据
	 * */
	public static void deleteFromCache(String key) {
		cacheMap.remove(key);
	}
	
}
