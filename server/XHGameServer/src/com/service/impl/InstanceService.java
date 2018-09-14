package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.FamilyConstant;
import com.constant.GuildConstant;
import com.constant.LockConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.constant.VipConstant;
import com.dao.instance.BaseInstanceDAO;
import com.dao.instance.PlayerInstanceDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.instance.BaseInstanceReward;
import com.domain.instance.PlayerInstance;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.domain.team.Team;
import com.domain.team.TeamPlayer;
import com.message.InstanceProto.IntancePanelMsg;
import com.message.InstanceProto.S_GetOpenMapList;
import com.message.InstanceProto.S_InstanceEnd;
import com.message.InstanceProto.S_SynTeamState;
import com.message.InstanceProto.TeamInsStateMsg;
import com.message.MessageProto.MessageEnum.MessageID;
import com.scene.SceneModel;
import com.service.IChatService;
import com.service.ICommonService;
import com.service.IInstanceService;
import com.service.IPlayerService;
import com.service.ISceneService;
import com.service.ITeamService;
import com.service.IVipService;
import com.util.ComparatorUtil;
import com.util.IDUtil;
import com.util.PlayerUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 副本系统
 * @author ken
 * @date 2017-2-28
 */
public class InstanceService implements IInstanceService {

	private PlayerInstanceDAO playerInstanceDAO = new PlayerInstanceDAO();	
	private BaseInstanceDAO baseInstanceDAO = new BaseInstanceDAO();

	@Override
	public void initBaseCache() {
		Map<Integer, List<BaseInstanceReward>> map = new HashMap<Integer, List<BaseInstanceReward>>();	
		List<BaseInstanceReward> baseRewardList = baseInstanceDAO.listBaseInstanceReward();
		for(BaseInstanceReward baseInstanceReward : baseRewardList){
			List<BaseInstanceReward> list = map.get(baseInstanceReward.getRewardType());
			if(list == null){
				list = new ArrayList<BaseInstanceReward>();
				map.put(baseInstanceReward.getRewardType(), list);
			}
			baseInstanceReward.setRewardList(SplitStringUtil.getDropInfo(baseInstanceReward.getRewardStr()));			
			List<Integer> position = SplitStringUtil.getPosition(baseInstanceReward.getPosition());
			if(position != null && !position.isEmpty()){
				baseInstanceReward.setX(position.get(0));
				baseInstanceReward.setY(position.get(1));
				baseInstanceReward.setZ(position.get(2));
			}		
			
			list.add(baseInstanceReward);
		}	
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_INSTANCE_REWARD, map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, PlayerInstance> listPlayerInstances(long playerId) {
		Map<Integer, PlayerInstance> map = (Map<Integer, PlayerInstance>)CacheService.getFromCache(CacheConstant.PLAYER_INSTANCE_CACHE + playerId);
		if(map == null){
			map = new ConcurrentHashMap<Integer, PlayerInstance>();
			List<PlayerInstance> lists = playerInstanceDAO.listPlayerInstances(playerId);
			for(PlayerInstance model : lists){
				map.put(model.getMapId(), model);
			}
			CacheService.putToCache(CacheConstant.PLAYER_INSTANCE_CACHE + playerId, map);
		}
		return map;
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_INSTANCE_CACHE + playerId);
	}
	
	/**
	 * 同步数据库
	 */
	private void updatePlayerInstance(PlayerInstance model){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_INSTANCE);
		if (!lists.contains(model)) {
			lists.add(model);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getOpenMapList(long playerId) {		
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		Map<Integer, BaseMap> map = (Map<Integer, BaseMap>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_MAP);
		
		Map<Integer, PlayerInstance> instanceMap = this.listPlayerInstances(playerId);
		
		S_GetOpenMapList.Builder builder = S_GetOpenMapList.newBuilder();
		for(Map.Entry<Integer, BaseMap> entry : map.entrySet()){
			BaseMap baseMap = entry.getValue();
			if(baseMap.getMapType() != SceneConstant.INSTANCE_SCENE) continue;
			
			if(baseMap.getOpenTask() > 0) continue;
			
			if(playerProperty.getLevel() < baseMap.getOpenLevel()) continue;
			
			int enterCount = 0;
			if(instanceMap.containsKey(entry.getKey())){
				enterCount = instanceMap.get(entry.getKey()).getEnterCount();
			}
			IntancePanelMsg.Builder msg = IntancePanelMsg.newBuilder();
			msg.setMapId(entry.getKey());
			msg.setEnterCount(baseMap.getMaxCount() - enterCount);
			builder.addIntancePanelMsgs(msg);
		}
		MessageObj msg = new MessageObj(MessageID.S_GetOpenMapList_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void enterInstance(long playerId, int mapId) throws Exception {
		if(playerId < 1 || mapId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ICommonService commonService = serviceCollection.getCommonService();
		ITeamService teamService = serviceCollection.getTeamService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.INSTANCE)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			BaseMap baseMap = sceneService.getBaseMap(mapId);
			
			int result = this.checkEnter(playerId, baseMap);
			if(result > 0) throw new GameException(result);			
			
			String sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_SINGLE, playerId);	
			
			if(playerExt.getTeamId() > 0){
				Team team = teamService.getTeam(playerExt.getTeamId());
				if(team != null){
					TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
					if(teamPlayer == null || !teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.SCENE_1202);
					
					List<Long> playerIds = teamService.getOnlineTeamPlayerIds(team);
					if(playerIds.size() > baseMap.getPlayernum()){
						throw new GameException(ExceptionConstant.SCENE_1207);
					}
					
					if(playerIds.size() > 1){
						Map<Long, Integer> insPlayerIdMap = team.getInsPlayerIdMap();
						insPlayerIdMap.clear();
						team.setMapId(0);
						
						insPlayerIdMap.put(playerId, 0);
						
						//检查队员进入条件
						for(Long id : playerIds){
							
							if(id.equals(playerId))continue;
							
							int check = this.checkEnter(id, baseMap);
							if(check > 0){								
								if(check == ExceptionConstant.SCENE_1208){
									IChatService chatService = serviceCollection.getChatService();										
									chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_25, null, playerIds);
									 return;
								}								
								
								Player p = playerService.getPlayerByID(id);
								commonService.sendNoticeMsg(playerId, ResourceUtil.getValue("instance_1", p.getPlayerName()));
								return;
							}				
							
							insPlayerIdMap.put(id, 0);
						}
					
						team.setMapId(mapId);
						//发送副本准备通知
						this.synTeamInsState(insPlayerIdMap, mapId);
						return;
					}
				}
			}
			
			int curLayerId = 0;
			if(baseMap.isTaskInstance() && baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_42){
				curLayerId = playerExt.getWeekTaskNum();
			}else if(baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_43 || baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_44){
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
				curLayerId = playerProperty.getLevel();
			}
			sceneService.enterScene(playerId, mapId, 0, false, sceneGuid, curLayerId);
		}

	}
	
	@Override
	public void quitInstance(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.INSTANCE)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			String sceneGuid = playerExt.getSceneGuid();
			if(sceneGuid == null || sceneGuid.equals("")) return;
			
			BaseMap baseMap = sceneService.getBaseMap(playerExt.getMapId());
			
			if(!baseMap.isInstance()) throw new GameException(ExceptionConstant.SCENE_1205);
			
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null) throw new GameException(ExceptionConstant.PLAYER_1111);
					
			if(playerPuppet.getHp() <= 0){
				sceneService.resetPuppet(playerPuppet);
			}
			
			sceneService.enterScene(playerId, playerExt.getLastMapId(), 0, false, null, 0);	
		}

	}	
	
	
	/**
	 * 推送副本准备通知
	 */
	private void synTeamInsState(Map<Long, Integer> insPlayerIdMap, int mapId){

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		S_SynTeamState.Builder builder = S_SynTeamState.newBuilder();
		
		List<Long> offerPlayerIds = new ArrayList<Long>();
		for(Map.Entry<Long, Integer> entry : insPlayerIdMap.entrySet()){
			builder.addTeamInsStates(this.buildTeamInsStateMsg(entry.getKey(), entry.getValue()));
			builder.setMapId(mapId);
			offerPlayerIds.add(entry.getKey());
		}
		MessageObj msg = new MessageObj(MessageID.S_SynTeamState_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerList(offerPlayerIds, msg);
	}
	
	/**
	 * 检查进入副本条件
	 */
	private int checkEnter(long playerId, BaseMap baseMap) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		if(playerProperty.getLevel() < baseMap.getOpenLevel()) return ExceptionConstant.PLAYER_1110;
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);		
		BaseMap baseMap1 = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
		if(baseMap1.isInstance()) return ExceptionConstant.SCENE_1203;
		
		// 如果是组队副本，玩家必须在主场景里面
		if(baseMap.getMapType() == SceneConstant.INSTANCE_SCENE &&
				(baseMap.getOpenTask() ==  SceneConstant.INSTANCE_TYPE_0 ||baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_44)){			
			if(baseMap1.getMapType() != SceneConstant.MAIN_CITY){
				return ExceptionConstant.SCENE_1208;
			}			
		}		
		
		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		if(baseMap.getOpenTask() == 0){			
			IVipService vipService = serviceCollection.getVipService();
			int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_14);
			int INSTANCE_MAX_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.INSTANCE_MAX_NUM) + value;
			if(playerDaily.getInstanceNum() >= INSTANCE_MAX_NUM) return ExceptionConstant.SCENE_1201;
		}			

		Map<Integer, PlayerInstance> map = this.listPlayerInstances(playerId);
		PlayerInstance playerInstance = map.get(baseMap.getMap_id());	
		if(playerInstance == null){			
			playerInstance = new PlayerInstance();
			playerInstance.setId(IDUtil.geneteId(PlayerInstance.class));
			playerInstance.setPlayerId(playerId);
			playerInstance.setMapId(baseMap.getMap_id());
			playerInstanceDAO.createPlayerInstance(playerInstance);				
			map.put(baseMap.getMap_id(), playerInstance);
		}
		
		if(baseMap.getMaxCount() > 0){
			//任务副本不扣次数
			boolean bTask = false;
			PlayerTask mainTask = serviceCollection.getTaskService().getPlayerTaskByType(playerId, TaskConstant.MAIN_TASK);
			if(mainTask != null && mainTask.getTaskState() == TaskConstant.TASK_STATE_NO && mainTask.getConditionType() == TaskConstant.TYPE_3){
				BaseTask baseTask = serviceCollection.getTaskService().getBaseTask(mainTask.getTaskId());
				if(baseTask.getConditionList().get(0).equals(baseMap.getMap_id())) {	
					bTask = true;
				}
			}
			
			if(!bTask){
				if(playerInstance.getEnterCount() >= baseMap.getMaxCount()) return ExceptionConstant.SCENE_1201;
			}	
		}		
		
		return 0;
	}
	
	@Override
	public void costCount(long playerId, BaseMap baseMap){
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		//任务副本不扣次数
		PlayerTask mainTask = serviceCollection.getTaskService().getPlayerTaskByType(playerId, TaskConstant.MAIN_TASK);
		if(mainTask != null && mainTask.getTaskState() == TaskConstant.TASK_STATE_NO && mainTask.getConditionType() == TaskConstant.TYPE_3){
			BaseTask baseTask = serviceCollection.getTaskService().getBaseTask(mainTask.getTaskId());
			if(baseTask.getConditionList().get(0).equals(baseMap.getMap_id())) {	
				return;
			}
		}			
		
		Map<Integer, PlayerInstance> map = this.listPlayerInstances(playerId);
		PlayerInstance playerInstance = map.get(baseMap.getMap_id());		
		if(playerInstance == null) return;
		
		playerInstance.setEnterCount(playerInstance.getEnterCount() + 1);
		this.updatePlayerInstance(playerInstance);	
		
		IPlayerService playerService =  serviceCollection.getPlayerService();
		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		// 记录每日副本总次数
		if(baseMap.getOpenTask() == 0){
			playerDaily.setInstanceNum(playerDaily.getInstanceNum() + 1);
			playerService.updatePlayerDaily(playerDaily);	
		}
	}
	
	/**
	 * 队员同意进入状况
	 */
	private TeamInsStateMsg.Builder buildTeamInsStateMsg(long teamPlayerId, int state){
		TeamInsStateMsg.Builder msg = TeamInsStateMsg.newBuilder();
		msg.setTeamPlayerId(teamPlayerId);
		msg.setState(state);
		return msg;
	}
	
	@Override
	public void agreeEnter(long playerId, int state) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ITeamService teamService = serviceCollection.getTeamService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		Team team = teamService.getTeam(playerExt.getTeamId());
		if(team == null || team.getMapId() == 0) return;
		
		synchronized (team.getAgreeLock()) {

			Map<Long, Integer> map = team.getInsPlayerIdMap();
			if(!map.containsKey(playerId)) return;
			
			map.put(playerId, state);
			
			//发送副本准备通知
			this.synTeamInsState(map, team.getMapId());
			
			if(state == 1){
				boolean bEnter = true;
				for(Map.Entry<Long, Integer> entry : map.entrySet()){
					if(entry.getValue() != 1){
						bEnter = false;
						break;
					}	
				}
				if(bEnter){
					BaseMap baseMap = sceneService.getBaseMap(team.getMapId());
					
					int curLayerId = 0;
					if(baseMap.isTaskInstance() && baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_42){
						//当前环数
						curLayerId = playerExt.getWeekTaskNum();
					}else if(baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_43 || baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_44){
						//最大两位等级取平均
						List<Integer> lvs = new ArrayList<Integer>();
						for(Map.Entry<Long, Integer> entry : map.entrySet()){
							PlayerProperty playerProperty = playerService.getPlayerPropertyById(entry.getKey());
							lvs.add(playerProperty.getLevel());
						}
						if(lvs.size() > 2){
							Collections.sort(lvs, new ComparatorUtil(ComparatorUtil.DOWN));
						}
						curLayerId = (int)Math.ceil((lvs.get(0) + lvs.get(1))/2.0);
						
					}
					
					for(Map.Entry<Long, Integer> entry : map.entrySet()){
						sceneService.enterScene(entry.getKey(), team.getMapId(), 0, false, PlayerUtil.getSceneGuid(SceneConstant.TYPE_TEAM, playerExt.getTeamId()), curLayerId);
					}
					team.setMapId(0);
				}
				
			}if(state == 2){
				Player p = playerService.getPlayerByID(playerId);
				for(Map.Entry<Long, Integer> entry : map.entrySet()){
					serviceCollection.getCommonService().sendNoticeMsg(entry.getKey(), ResourceUtil.getValue("instance_2", p.getPlayerName()));		
				}
				
				map.clear();
			}
		}
	}
	
	@Override
	public void end(SceneModel sceneModel, int result) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		sceneModel.setSceneState(SceneConstant.SCENE_STATE_END);
		sceneModel.setEndTime(System.currentTimeMillis());
			
		if(sceneModel.getMapId() == GuildConstant.MAP_GUILD_7003){
			//凌烟阁
			serviceCollection.getGuildService().endGuildFB();
		}else if(sceneModel.getMapId() == FamilyConstant.MAP_FAMILY_8001){
			//家族副本
			long familyId = PlayerUtil.getLongId(sceneModel.getSceneGuid());
			serviceCollection.getFamilyService().endFamilyFB(familyId);
		}
		
		List<Long> playerIds = sceneService.getScenePlayerIds(sceneModel);
		if(!playerIds.isEmpty()){
			
			// 移除怪物通知
			for( Map.Entry<Integer, Map<String, MonsterPuppet>> entry : sceneModel.getMonsterPuppetMap().entrySet()){
				for(String mGuid : entry.getValue().keySet()){
					sceneService.removeSelfToNearby(mGuid, playerIds);
				}
			}
			
			//推送结束包
			S_InstanceEnd.Builder builder = S_InstanceEnd.newBuilder();
			builder.setResult(result);
			builder.setDestroyTime(sceneModel.getWaitingTime());
			MessageObj msg = new MessageObj(MessageID.S_InstanceEnd_VALUE, builder.build().toByteArray());
			GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerList(playerIds, msg);
			
		}
		
		sceneModel.getMonsterPuppetMap().clear();
	}
	

	@Override
	public void quarztDaily() {
		playerInstanceDAO.quarztDaily();
	}
	
	/**
	 * 取奖励信息
	 * @param rewardType:奖励类型
	 * @param lv:取奖励的所在的区间值
	 */
	@SuppressWarnings("unchecked")
	public BaseInstanceReward getBaseInstanceReward(int rewardType, int lv){
		Map<Integer, List<BaseInstanceReward>> map = (Map<Integer, List<BaseInstanceReward>>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_INSTANCE_REWARD);
		List<BaseInstanceReward> rewards = map.get(rewardType);
		for(BaseInstanceReward model : rewards){
			if(lv >= model.getMinLv() && lv <= model.getMaxLv()){
				return model;
			}
		}
		return null;
	}

}
