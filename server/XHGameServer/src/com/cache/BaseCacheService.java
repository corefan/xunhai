package com.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础数据缓存
 * @author ken
 * @date 2016-12-27
 */
public class BaseCacheService {

	/** 基础表数据缓存 */
	private static Map<String, Object> baseCacheMap = new HashMap<String, Object>(1 << 12);
	
	/**
	 * 数据放入缓存(基础)
	 * @param key
	 * @param value
	 */
	public static void putToBaseCache(String key, Object value) {
		baseCacheMap.put(key, value);
	}
	
	/**
	 * 缓存中获得数据(基础)
	 * @param key
	 * @return
	 */
	public static Object getFromBaseCache(String key) {
		return baseCacheMap.get(key);
	}
}
