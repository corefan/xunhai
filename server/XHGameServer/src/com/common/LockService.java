package com.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.constant.LockConstant;

/**
 * 锁服务
 *
 */
public class LockService {
	
	/**
	 * 玩家独立锁，用来解决玩家重复操作时的数据问题
	 */
	private static Map<Long, Map<String, Object>> playerSelfLockMap = new ConcurrentHashMap<Long, Map<String, Object>>();
	
	/** 锁常量 */
	private static Map<String, String> lockConsMap = new HashMap<String, String>();
	
	/**
	 * 初始化玩家锁
	 * */
	public static void initPlayerLockCache() {
		
		Field[] es = LockConstant.class.getDeclaredFields();
		for (Field f : es) {
			try {
				//System.out.println(f.getName()+" "+f.get(f.getName()));
				String value = (String)f.get(f.getName());
				lockConsMap.put(value, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 放入玩家独立锁
	 * @param key
	 * @param value
	 */
	public static void putToPlayerSelfLock(long playerId) {
		
		Map<String, Object> lockCol = playerSelfLockMap.get(playerId);
		if (lockCol == null) {
			lockCol = new ConcurrentHashMap<String, Object>();
			playerSelfLockMap.put(playerId, lockCol);
			if (lockConsMap != null) {
				for (Entry<String, String> entry : lockConsMap.entrySet()) {
					lockCol.put(entry.getKey(), new Object());
				}
			}
		}
	}
	
	/**
	 * 删除玩家独立锁
	 * */
	public static void deleteFromPlayerSelfLock(long playerId) {
		playerSelfLockMap.remove(playerId);
	}
	
	/**
	 * 根据类型获得玩家独立锁
	 */
	public static Object getPlayerLockByType(long playerId, String lockType) {
		Map<String, Object> lockCol = playerSelfLockMap.get(playerId);
		if (lockCol == null) {
			lockCol = new ConcurrentHashMap<String, Object>();
			playerSelfLockMap.put(playerId, lockCol);
		}
		Object lock = lockCol.get(lockType);
		if(lock == null){
			lock = new Object();
			lockCol.put(lockType, lock);
		}
		return lock;
	}

}
