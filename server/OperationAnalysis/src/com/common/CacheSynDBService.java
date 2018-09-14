package com.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.service.IBatchExcuteService;


/**
 * 缓存同步到数据库
 * @author ken
 *
 */
public class CacheSynDBService {

	/** 定时一分钟写日志 */
	public static Map<String, List<?>> oneMinuteInsertCacheLogMap = new HashMap<String, List<?>>();


	/**
	 * 缓存初始化
	 * */
	public static void initCacheMap() {

		initOneSecondLogCacheMap();

	}


	/**
	 * 日志1分钟缓存初始化-新增
	 * 注:日志库
	 * */
	private static void initOneSecondLogCacheMap() {

	}


	/**
	 * 获得1秒钟日志缓存数据
	 * 注:日志库
	 */
	public static Object getFromOneSecondLogCache(String key) {
		return oneMinuteInsertCacheLogMap.get(key);
	}



	/**
	 * 获得当前日志缓存中所有数据后并清空-新增(1fen钟)
	 * @return 当前缓存中的所有数据
	 */
	public static List<List<?>> getAllAndClearLogCache_one_second_insert() {
		IBatchExcuteService batchExcuteService = GCCContext.getInstance().getServiceCollection().getBatchExcuteService();
		synchronized (oneMinuteInsertCacheLogMap) {

			List<List<?>> dataList = getAllInsetLogCache(oneMinuteInsertCacheLogMap);
			for (List<?> list : dataList) {
				batchExcuteService.batchInsert(list);
			}
			initOneSecondLogCacheMap();

			return dataList;
		}

	}


	private static List<List<?>> getAllInsetLogCache(Map<String, List<?>> cacheLogMap) {

		List<List<?>> dataList = new ArrayList<List<?>>();

		for (List<?> value : cacheLogMap.values()) {
			dataList.add(value);
		}

		return dataList;
	}

}
