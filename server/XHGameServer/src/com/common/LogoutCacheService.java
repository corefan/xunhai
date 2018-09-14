package com.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 登出缓存
 * @author ken
 * @date 2016-12-27
 */
public class LogoutCacheService {

	private static ConcurrentMap<Long, Long> cacheMap = new ConcurrentHashMap<Long, Long>();

	/**
	 * 数据放入缓存
	 * @param key
	 * @param value
	 */
	public static void putToCache(Long key, Long value) {
		cacheMap.put(key, value);
	}

	/**
	 * 移除清除缓存(再次登陆时)
	 */
	public static void removeCacheForLogin(Long key) {
		cacheMap.remove(key);
	}

	/**
	 * 校验到期缓存
	 * type 1:(4小时) 2:(24小时) 3:(2天)
	 */
	public static void checkExpirationCache(Integer type){
		List<Long> removeList = new ArrayList<Long>();
		Date nowDate = DateService.getCurrentUtilDate();
		for(Map.Entry<Long, Long> entry : cacheMap.entrySet()){
			Long logoutTime = entry.getValue();
			if (logoutTime != null) {
				if (type == 1) {
					if((nowDate.getTime() - logoutTime)/1000 >= 14400){ //60*60*4
						removeList.add(entry.getKey());
						cacheMap.remove(entry.getKey());
					}
				} else if (type == 2) {
					if((nowDate.getTime() - logoutTime) >= DateService.DAY_MILLISECOND){
						removeList.add(entry.getKey());
						cacheMap.remove(entry.getKey());
					}
				} else if (type == 3) {
					if((nowDate.getTime() - logoutTime) >= DateService.DAY_MILLISECOND * 2){
						removeList.add(entry.getKey());
						cacheMap.remove(entry.getKey());
					}
				}
			}
		}
		for(long playerId : removeList){
			if (type == 1) {
				deleteOnlineCache_one(playerId);
			} else if (type == 2){
				deleteOnlineCache_two(playerId);
			} else if (type == 3) {
				deleteOnlineCache_three(playerId);
			}
		}
	}

	/**
	 * 删除玩家在线缓存
	 * 4小时
	 */
	public static void deleteOnlineCache_one(long playerId) {
		// 删除玩家独立锁
		LockService.deleteFromPlayerSelfLock(playerId);
	}

	/**
	 * 删除玩家在线缓存
	 * 24小时
	 * 
	 */
	public static void deleteOnlineCache_two(long playerId) {
		
	}

	/**
	 * 删除玩家在线缓存
	 * 2天
	 */
	public static void deleteOnlineCache_three(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		serviceCollection.getPlayerService().deleteCache(playerId); //玩家
		serviceCollection.getBagService().deleteCache(playerId); //背包
		serviceCollection.getEquipmentService().deletePlayerEquipmentCacheInfo(playerId); //装备
		serviceCollection.getMailService().removeMailCache(playerId);// 邮件
		serviceCollection.getFashionService().deleteCache(playerId);//时装
		serviceCollection.getSkillService().deleteCache(playerId);//技能
		serviceCollection.getFriendService().deleteCache(playerId); //好友
		serviceCollection.getTaskService().deleteCache(playerId); //任务
		serviceCollection.getInstanceService().deleteCache(playerId);//副本
		serviceCollection.getEpigraphService().deleteCache(playerId);//铭文
		serviceCollection.getTiantiService().deleteCache(playerId);//天梯
		serviceCollection.getWakanService().deleteCache(playerId);//注灵
		serviceCollection.getFamilyService().deleteCache(playerId);//家族
		serviceCollection.getMarketService().deleteCache(playerId);//商城
		serviceCollection.getWingService().deleteCache(playerId);//羽翼
		serviceCollection.getEnemyService().deleteCache(playerId);//仇敌
		serviceCollection.getVipService().deleteCache(playerId);//vip
		serviceCollection.getSignService().deleteCache(playerId);//签到
		serviceCollection.getActivityService().deleteCache(playerId);//运营活动
		serviceCollection.getGuildService().deleteCache(playerId);//帮派
		serviceCollection.getFurnaceService().deleteCache(playerId);//熔炉
	}
}
