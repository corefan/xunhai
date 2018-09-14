package com.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 游戏内存缓存
 * @author ken
 * @date 2016-12-27
 */
public class CacheService {

	private static ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<String, Object>(1 << 16, 0.75f, 1 << 5);
	
	/** 检查玩家时间戳  防变速*/
	private static ConcurrentMap<Long, Long> timeCacheMap = new ConcurrentHashMap<Long, Long>();
	private static ConcurrentMap<Long, Integer> timeErrorMap = new ConcurrentHashMap<Long, Integer>();
	
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
	
	/**
	 * 替换缓存数据
	 * */
	public static void replaceObject(String key, Object newValue) {
		cacheMap.replace(key, newValue);
	}

	public static ConcurrentMap<Long, Long> getTimeCacheMap() {
		return timeCacheMap;
	}

	public static ConcurrentMap<Long, Integer> getTimeErrorMap() {
		return timeErrorMap;
	}

}
