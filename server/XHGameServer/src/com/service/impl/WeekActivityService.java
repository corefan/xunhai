package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.BaseCacheService;
import com.common.DateService;
import com.common.GameContext;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.ActivityConstant;
import com.constant.CacheConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.VipConstant;
import com.dao.weekActivity.BaseWeekActivityDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.instance.PlayerInstance;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerProperty;
import com.domain.weekactivity.BaseWeekActivity;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.WeekactivityProto.ActivityMsg;
import com.message.WeekactivityProto.S_GetActivityList;
import com.service.IInstanceService;
import com.service.IPlayerService;
import com.service.IVipService;
import com.service.IWeekActivityService;
import com.util.LogUtil;

/**
 * 周活动系统
 * @author ken
 * @date 2017-5-12
 */
public class WeekActivityService implements IWeekActivityService {

	private BaseWeekActivityDAO baseWeekActivityDAO = new BaseWeekActivityDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, Map<Integer, List<BaseWeekActivity>>> map = new HashMap<Integer, Map<Integer, List<BaseWeekActivity>>>();
		
		List<BaseWeekActivity> lists = baseWeekActivityDAO.listBaseWeekActivitys();
		for(BaseWeekActivity model : lists){
			Map<Integer, List<BaseWeekActivity>> weekMap = map.get(model.getType());
			if(weekMap == null){
				weekMap = new HashMap<Integer, List<BaseWeekActivity>>();
				map.put(model.getType(), weekMap);
			}
			List<BaseWeekActivity> list = weekMap.get(model.getWeek());
			if(list == null){
				list = new ArrayList<BaseWeekActivity>();
				weekMap.put(model.getWeek(), list);
			}
			list.add(model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_WEEK_ACTIVITY, map);	
		
	}

	/**
	 * 根据活动编号取活动配置
	 */
	@SuppressWarnings("unchecked")
	public BaseWeekActivity getBaseWeekActivity(int activityId){
		Map<Integer, Map<Integer, List<BaseWeekActivity>>> map = (Map<Integer, Map<Integer, List<BaseWeekActivity>>>)
				BaseCacheService.getFromBaseCache(CacheConstant.BASE_WEEK_ACTIVITY);
		for(Map.Entry<Integer, Map<Integer, List<BaseWeekActivity>>> entry : map.entrySet()){
			for(Map.Entry<Integer, List<BaseWeekActivity>> entry2 : entry.getValue().entrySet()){
				for(BaseWeekActivity model : entry2.getValue()){
					if(model.getId() == activityId){
						return model;
					}
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getActivityList(long playerId) {
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		IPlayerService playerService = serviceCollection.getPlayerService();	
		IVipService vipService = serviceCollection.getVipService();
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		
		S_GetActivityList.Builder builder = S_GetActivityList.newBuilder();
		
		Map<Integer, Map<Integer, List<BaseWeekActivity>>> map = (Map<Integer, Map<Integer, List<BaseWeekActivity>>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_WEEK_ACTIVITY);
		Map<Integer, List<BaseWeekActivity>> map1 = map.get(1);
		for(Map.Entry<Integer, List<BaseWeekActivity>> entry : map1.entrySet()){
			List<BaseWeekActivity> lists = entry.getValue();
			for(BaseWeekActivity model : lists){
				int enterCount = 0;
				int state = 0;
				
				if(model.getLimitLevel() > playerProperty.getLevel()){
					state = 1;
				}
				
				PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);	
				
				// 日常任务处理		
				if(model.getId() == ActivityConstant.ACTIVITY_101){				
					enterCount = playerDaily.getDailyTaskNum();	
					
					int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_16);
					//悬赏任务
					if(enterCount >= model.getMaxCount() + value) {
						state = 1;
					}
				}else if(model.getId() == ActivityConstant.ACTIVITY_102){
					enterCount = playerDaily.getHuntTaskNum();
					
					int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_15);
					
					//猎妖任务
					if(enterCount >= model.getMaxCount() + value) {
						state = 1;
					}					
					
				}else if(model.getId() == ActivityConstant.ACTIVITY_103){
					enterCount = playerDaily.getInstanceNum();
					
					int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_14);
					int INSTANCE_MAX_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.INSTANCE_MAX_NUM) + value;
					if(enterCount >= INSTANCE_MAX_NUM) {
						state = 1;
					}
				}else if(model.getId() == ActivityConstant.ACTIVITY_104){
					//侍魂殿
					
					enterCount = playerDaily.getTiantiNum();
					if(model.getMaxCount() > 0 && enterCount >= model.getMaxCount()) {
						state = 1;
					}	
				}
				
				ActivityMsg.Builder msg = ActivityMsg.newBuilder();
				msg.setId(model.getId());
				msg.setEnterCount(enterCount);
				msg.setState(state);
				builder.addActivitys(msg);
			}
		}
		
		Map<Integer, List<BaseWeekActivity>> map2 = map.get(2);
		int week = DateService.getCurrWeekDay();
		List<BaseWeekActivity> lists = map2.get(week);		
		Map<Integer, PlayerInstance> playerInstanceMap = instanceService.listPlayerInstances(playerId);
		for(BaseWeekActivity model : lists){
			
			int enterCount = 0;
			PlayerInstance pInstance = playerInstanceMap.get(model.getMapId());
			if(pInstance != null){
				enterCount = pInstance.getEnterCount();
			}
			
			int state = 0;
			if(model.getLimitLevel() > playerProperty.getLevel()){
				state = 1;
			}else{
				if(!DateService.isInTime(model.getStartHour(), model.getStartMin(), model.getEndHour(), model.getEndMin())){
					state = 1;
				}else{
					if(model.getMaxCount() > 0 && enterCount >= model.getMaxCount()){
						state = 1;
					}
				}
			}			
			
			ActivityMsg.Builder msg = ActivityMsg.newBuilder();
			msg.setId(model.getId());
			msg.setEnterCount(enterCount);
			msg.setState(state);
			builder.addActivitys(msg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetActivityList_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void enterActivity(long playerId, int activityId)  throws Exception{
		if(playerId < 1 || activityId < 1) throw new GameException(ExceptionConstant.ERROR_10000);			
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		
		BaseWeekActivity baseWeekActivity = this.getBaseWeekActivity(activityId);
		if(baseWeekActivity == null){
			LogUtil.error("baseWeekActivity is null with id is "+ activityId);
			return;
		}
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WEEK_ACTIVITY)) {
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if(baseWeekActivity.getLimitLevel() > playerProperty.getLevel()){
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}
			
			if(!DateService.isInTime(baseWeekActivity.getStartHour(), baseWeekActivity.getStartMin(), baseWeekActivity.getEndHour(), baseWeekActivity.getEndMin())){
				throw new GameException(ExceptionConstant.ACTIVITY_2801);
			}
			
			instanceService.enterInstance(playerId, baseWeekActivity.getMapId());
		}
	}
	
}
