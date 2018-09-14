package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.common.GameContext;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.RewardTypeConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.dao.tower.BaseTowerDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.instance.BaseInstanceReward;
import com.domain.map.BaseMap;
import com.domain.map.Huanjing;
import com.domain.monster.BaseRefreshMonster;
import com.domain.monster.RefreshMonsterInfo;
import com.domain.player.PlayerExt;
import com.domain.puppet.PlayerPuppet;
import com.domain.tower.BaseTower;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TowerProto.S_GetShenjingData;
import com.message.TowerProto.S_TowerEnd;
import com.scene.SceneModel;
import com.service.IInstanceService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ITaskService;
import com.service.ITowerService;
import com.util.LogUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 大荒塔
 * @author ken
 * @date 2017-3-24
 */
public class TowerService implements ITowerService {

	private BaseTowerDAO baseTowerDAO = new BaseTowerDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseTower> towerMap = new HashMap<Integer, BaseTower>();
		
		List<BaseTower> lists = baseTowerDAO.listBaseTowers();
		for(BaseTower model : lists){
			List<Integer> l = SplitStringUtil.getIntList(model.getRefPoint());
			
			List<Integer> refIds = SplitStringUtil.getIntList(model.getMonList());
			if(refIds != null){
				for(Integer refId : refIds){
					BaseRefreshMonster baseRefreshMonster =  GameContext.getInstance().getServiceCollection().getMonsterService().getBaseRefreshMonster(refId);
					if(baseRefreshMonster == null){
						System.out.println("this baseRefreshMonster is null with id is "+refId+"   mapId is "+model.getMapId());
						continue;
					}
					baseRefreshMonster.setxRefresh(l.get(0));
					baseRefreshMonster.setyRefresh(l.get(1));
					baseRefreshMonster.setzRefresh(l.get(2));
					
					model.getRefMonList().add(baseRefreshMonster);
				}
			}

			towerMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TOWER, towerMap);
				
	}

	/**
	 * 取大荒塔配置
	 */
	@SuppressWarnings("unchecked")
	public BaseTower getBaseTowerById(int layerId){
		int id = layerId % 13;
		if(id == 0){
			id = 13;
		}
		
		Map<Integer, BaseTower> towerMap = (Map<Integer, BaseTower>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_TOWER);
		return towerMap.get(id);
	}

	@Override
	public void enterTower(long playerId)throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TOWER)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			BaseTower baseTower = this.getBaseTowerById(playerExt.getCurLayerId());
			serviceCollection.getSceneService().enterScene(playerId, baseTower.getMapId(), 0, false, null, playerExt.getCurLayerId());
			
			try {
				// 任务触发
				List<Integer> conditionList = new ArrayList<Integer>();				
				conditionList.add(playerExt.getCurLayerId());
				serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_22, conditionList);
			} catch (Exception e1) {
				LogUtil.error("enterTower task type_22 error" + e1);	
			}
			
		}
	}

	@Override
	public void quitTower(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TOWER)) {
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			ISceneService sceneService = serviceCollection.getSceneService();
			IPlayerService playerService = serviceCollection.getPlayerService();
			
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null) return;
			
			String sceneGuid = playerPuppet.getSceneGuid();
			if(sceneGuid == null || sceneGuid.equals("")) return;
			
			SceneModel sceneModel = sceneService.getSceneModel(sceneGuid);
			if(sceneModel.getMapType() != SceneConstant.TOWER_SCENE) throw new GameException(ExceptionConstant.SCENE_1205);
			
			if(playerPuppet.getHp() <= 0){
				sceneService.resetPuppet(playerPuppet);
			}
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			sceneService.enterScene(playerId, playerExt.getLastMapId(), 0, false, null, 0);
		}
	}

	@Override
	public void resetTower(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TOWER)) {
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			ISceneService sceneService = serviceCollection.getSceneService();
			IPlayerService playerService = serviceCollection.getPlayerService();
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			String sceneGuid = playerExt.getSceneGuid();
			if(sceneGuid == null || sceneGuid.equals("")) return;
			
			SceneModel sceneModel = sceneService.getSceneModel(sceneGuid);
			if(sceneModel.getMapType() == SceneConstant.TOWER_SCENE) throw new GameException(ExceptionConstant.SCENE_1206);
			
			playerExt.setCurLayerId(1);
			playerService.updatePlayerExt(playerExt);
		}
	}

	/**
	 * 副本结束  1：胜利  2：失败
	 */
	@Override
	public void end(long playerId, int result, SceneModel sceneModel){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		IMailService mailService = serviceCollection.getMailService();
		ITaskService taskService = serviceCollection.getTaskService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		BaseTower baseTower = this.getBaseTowerById(playerExt.getCurLayerId());
		
		int layerId = playerExt.getCurLayerId();		
		String rewardStr = "";
		if(result == 1){
			if(sceneModel.getCurMonNum() >= baseTower.getNum()){
				sceneModel.setSceneState(SceneConstant.SCENE_STATE_END);
				BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(sceneModel.getMapId());
				//通关
				int[][] rewards = new int[3][4];
				int[] items1 = new int[]{RewardTypeConstant.MONEY, 0, this.towerGold(layerId, baseTower.getBaseGold()),0};
				rewards[0] = items1;
				
				int[] items2 = new int[]{RewardTypeConstant.EXPERIENCE, 0, this.towerExp(layerId, baseTower.getBaseExp()),0};
				rewards[1] = items2;
				
				Reward reward = null;
				BaseInstanceReward baseInstanceReward = instanceService.getBaseInstanceReward(baseMap.getRewardType(), playerExt.getCurLayerId());
				
				if(baseInstanceReward.getRandType() == 1){
					reward = rewardService.globalRandom(baseInstanceReward.getRewardList());
				}
				
				int[] items3 = new int[]{reward.getType(),reward.getId(),reward.getNum(),reward.getBlind()};
				rewards[2] = items3;
				
				rewardStr = SplitStringUtil.getStringByIntIntList(rewards);
				
				playerExt.setCurLayerId(playerExt.getCurLayerId() + 1);
				playerService.updatePlayerExt(playerExt);
				
				try {
					List<Reward> rewardList = SplitStringUtil.getRewardInfo(rewardStr);
					if(rewardService.checkBackNum(playerId, rewardList)){
						rewardService.fetchRewardList(playerId, rewardList);
					}else{
						mailService.systemSendMail(playerId, ResourceUtil.getValue("bag_1"), ResourceUtil.getValue("bag_2"), rewardStr, 0);
					}
				} catch (Exception e) {
					LogUtil.error("tower end send mail error");
				}
				
				// 大荒塔任务触发			
				try {
					List<Integer> conditionList = new ArrayList<Integer>();	
					conditionList.add(playerExt.getCurLayerId());
					taskService.executeTask(playerId, TaskConstant.TYPE_26, conditionList);
				} catch (Exception e) {
					LogUtil.error("Tower task type_26 error");
				}
			
			}else{
				int refMonsterId = baseTower.getRefMonList().get(sceneModel.getCurMonNum()).getID();
				RefreshMonsterInfo info = new RefreshMonsterInfo();
				info.setRefMonsterId(refMonsterId);
				info.setRefreshDate(System.currentTimeMillis() + 3000);
				info.setCurLayerId(layerId);
				sceneModel.getRefMonsterMap().put(refMonsterId, info);
				
				sceneModel.setCurMonNum(sceneModel.getCurMonNum() + 1);
				return;
			}
		}else{
			playerExt.setCurLayerId(playerExt.getCurLayerId() - 2);
			playerService.updatePlayerExt(playerExt);
			
			PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
			if(playerPuppet != null){
				serviceCollection.getSceneService().resetPuppet(playerPuppet);	
			}
		}

		serviceCollection.getSceneService().destroy(sceneModel);
		
		//推送结束包  层数 奖励
		S_TowerEnd.Builder builder = S_TowerEnd.newBuilder();
		builder.setResult(result);
		builder.setCurLayerId(layerId);
		builder.setRewardInfo(rewardStr);
		MessageObj msg = new MessageObj(MessageID.S_TowerEnd_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	/**
	 * 返回大荒塔buff编号
	 */
	public int towerBuff(int curLayerId){
		int lun = (int)Math.ceil(curLayerId * 1.0 / 13);
		if(lun == 1){
			return 0;
		}else if(lun == 2){
			return 27;
		}
		return 28;
	}
	
	/**
	 * 经验奖励系数
	 * (0.01*((轮数-1)*4+10)^3+0.05*((轮数-1)*4+10)^2+1*((轮数-1)*4+10)+2)/27
	 */
	private int towerExp(int curLayerId, int baseExp){
		int lun = (int)Math.ceil(curLayerId * 1.0 / 13);
		int v = (lun-1)*4+10;
		double value = (0.01*Math.pow(v, 3) + 0.05*Math.pow(v, 2) + v + 2)/27;
		return (int)(value*baseExp);
	}
	
	/**
	 * 金币奖励系数
	 * 基础值*(1+(轮数-1)*0.2+0.03*(轮数-1)^2)	
	 */
	private int towerGold(int curLayerId, int baseGold){
		int lun = (int)Math.ceil(curLayerId * 1.0 / 13);	
		double value = 0.03 * Math.pow(lun - 1, 2) + (lun - 1) * 0.2 + 1;
		return (int)(value*baseGold);
	}

	@Override
	public void getShenjingData(long playerId) {
		Huanjing model = (Huanjing)CacheService.getFromCache(CacheConstant.HUANJING_CACHE);
		
		S_GetShenjingData.Builder builder = S_GetShenjingData.newBuilder();
		builder.setHuanjingState(model.getState());
		builder.setHuanjingEndTime(model.getEndTime());
		
		MessageObj msg = new MessageObj(MessageID.S_GetShenjingData_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}	

}
