package com.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.constant.CacheSynConstant;
import com.domain.GameEntity;


/**
 * 异步缓存到数据库
 * @author ken
 * @date 2016-12-20
 */
public class CacheSynDBService {

	/** 定时更新缓存-5分钟 */
	private static Map<String, Set<GameEntity>> fiveMinuteUpdateOneCacheMap = new ConcurrentHashMap<String, Set<GameEntity>>();
	private static Map<String, Set<GameEntity>> fiveMinuteUpdateTwoCacheMap = new ConcurrentHashMap<String, Set<GameEntity>>();
	private static Map<String, Set<GameEntity>> fiveMinuteUpdateThreeCacheMap = new ConcurrentHashMap<String, Set<GameEntity>>();

	/**
	 * 缓存初始化
	 * */
	public static void initCacheMap() {
		initFiveMinuteOneCacheMap();
		initFiveMinuteTwoCacheMap();
		initFiveMinuteThreeCacheMap();
	}

	
	/**
	 * 五分钟缓存初始化-更新
	 * */
	private static void initFiveMinuteOneCacheMap() {
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER_EXT, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER_PROPERTY, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER_DAILY, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER_WEALTH, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER_OPTIONAL, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.GUILD, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.PLAYER_GUILD, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.GUILD_WAR, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.GUILD_FIGHT, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateOneCacheMap.put(CacheSynConstant.GUILD_BUY, Collections.synchronizedSet(new HashSet<GameEntity>()));
	}

	/**
	 * 五分钟缓存初始化-更新
	 * */
	private static void initFiveMinuteTwoCacheMap() {
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_BAG, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_EQUIPMENT, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_DRUG, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_SKILL, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_FRIEND, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_WAKAN, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_INSTANCE, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_WEAPON_EFFECT, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_FAMILY, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.FAMILY, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_TRADE_BAG, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_WING, Collections.synchronizedSet(new HashSet<GameEntity>()));		
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_ENEMY, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateTwoCacheMap.put(CacheSynConstant.PLAYER_VIP, Collections.synchronizedSet(new HashSet<GameEntity>()));
	}
	
	/**
	 * 五分钟缓存初始化-更新
	 * */
	private static void initFiveMinuteThreeCacheMap() {
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_FASHION, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.MAIL_INBOX, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_TASK, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_TIANTI, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_MARKET, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_SIGN, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_TOMB, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.PLAYER_FURNACE, Collections.synchronizedSet(new HashSet<GameEntity>()));
		fiveMinuteUpdateThreeCacheMap.put(CacheSynConstant.REWARD_RECORD, Collections.synchronizedSet(new HashSet<GameEntity>()));
	}

	 /**
	  * 缓存五分钟缓存数据
	  * @param key
	  * @return
	  */
	 public static Set<GameEntity> getFromFiveUpdateOneCache(String key) {
		 return fiveMinuteUpdateOneCacheMap.get(key);
	 }

	 /**
	  * 缓存五分钟缓存数据
	  */
	 public static Set<GameEntity> getFromFiveUpdateTwoCache(String key) {
		 return fiveMinuteUpdateTwoCacheMap.get(key);
	 }
	 
	 /**
	  * 缓存五分钟缓存数据
	  */
	 public static Set<GameEntity> getFromFiveUpdateThreeCache(String key) {
		 return fiveMinuteUpdateThreeCacheMap.get(key);
	 }

	 /**
	  * 获得当前缓存中所有数据后并清空-更新
	  * @return 当前缓存中的所有数据
	  */
	 public static List<Set<GameEntity>> getAllAndClearCache_five_one_update() {
		 synchronized (fiveMinuteUpdateOneCacheMap) {

			 List<Set<GameEntity>> dataList = getAllUpdateCahce(fiveMinuteUpdateOneCacheMap);

			 initFiveMinuteOneCacheMap();

			 return dataList;
		 }
	 }

	 /**
	  * 获得当前缓存中所有数据后并清空-更新
	  * @return 当前缓存中的所有数据
	  */
	 public static List<Set<GameEntity>> getAllAndClearCache_five_two_update() {

		 synchronized (fiveMinuteUpdateTwoCacheMap) {

			 List<Set<GameEntity>> dataList = getAllUpdateCahce(fiveMinuteUpdateTwoCacheMap);

			 initFiveMinuteTwoCacheMap();

			 return dataList;
		 }
	 }
	 
	 /**
	  * 获得当前缓存中所有数据后并清空-更新
	  * @return 当前缓存中的所有数据
	  */
	 public static List<Set<GameEntity>> getAllAndClearCache_five_three_update() {
		 
		 synchronized (fiveMinuteUpdateThreeCacheMap) {
			 
			 List<Set<GameEntity>> dataList = getAllUpdateCahce(fiveMinuteUpdateThreeCacheMap);
			 
			 initFiveMinuteThreeCacheMap();
			 
			 return dataList;
		 }
	 }

	 private static List<Set<GameEntity>> getAllUpdateCahce(Map<String, Set<GameEntity>> cacheMap) {

		 List<Set<GameEntity>> dataList = new ArrayList<Set<GameEntity>>();

		 for (Set<GameEntity> dataSet : cacheMap.values()) {
			 if (dataSet.size() > 0) {
				 dataList.add(dataSet);
			 }
		 }

		 return dataList;

	 }

}
