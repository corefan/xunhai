package com.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁服务
 *
 */
public class LockService {
	
	/**
	 * 玩家独立锁，用来解决玩家重复操作时的数据问题
	 */
	private static Map<Integer, Map<String, Object>> playerSelfLockMap = new ConcurrentHashMap<Integer, Map<String, Object>>();
	
	/**
	 * 放入玩家独立锁
	 * @param key
	 * @param value
	 */
	public static void putToPlayerSelfLock(Integer playerID) {
		playerSelfLockMap.put(playerID, new HashMap<String, Object>());
	}
	
	/**
	 * 删除玩家独立锁
	 * */
	public static void deleteFromPlayerSelfLock(Integer playerID) {
		playerSelfLockMap.remove(playerID);
	}
	
	/**
	 * 根据类型获得玩家独立锁
	 */
	public static Object getPlayerLockByType(Integer playerID, String lockType) {
		Map<String, Object> lockCol = playerSelfLockMap.get(playerID);
		if (lockCol == null) {
			lockCol = new HashMap<String, Object>();
			playerSelfLockMap.put(playerID, lockCol);
		}
		Object lock = lockCol.get(lockType);
		if(lock == null){
			lock = new Object();
			lockCol.put(lockType, lock);
		}
		return lock;
	}

}
