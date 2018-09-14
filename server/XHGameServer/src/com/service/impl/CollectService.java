package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.CacheConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.LockConstant;
import com.constant.RewardTypeConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.dao.collect.BaseCollectDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.collect.BaseCollect;
import com.domain.collect.Collect;
import com.domain.collect.CollectReward;
import com.domain.map.BaseMap;
import com.domain.puppet.PlayerPuppet;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.message.CollectProto.S_EndCollect;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SceneProto.S_AddCollectItemInfos;
import com.message.SceneProto.S_RemoveCollectItemInfos;
import com.scene.SceneModel;
import com.service.ICollectService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ITaskService;
import com.util.LogUtil;
import com.util.NineGridUtil;
import com.util.PlayerUtil;
import com.util.ResourceUtil;
import com.util.SerialNumberUtil;
import com.util.SplitStringUtil;

/**
 * 采集系统
 * @author jiangqin
 * @date 2017-3-29
 */
public class CollectService implements ICollectService{
	private BaseCollectDAO  baseCollectDao = new BaseCollectDAO();

	@Override
	public void initBaseCache() {
		Map<Integer, BaseCollect> baseCollectMap = new HashMap<Integer, BaseCollect>();
		List<BaseCollect> listBaseCollect = baseCollectDao.listBaseCollect();
		for(BaseCollect baseCollect : listBaseCollect){
			baseCollect.setCollectRewardMap(SplitStringUtil.getCollectRewardInfo(baseCollect.getReward()));
			baseCollect.setExpendList(SplitStringUtil.getRewardInfo(baseCollect.getExpendStr()));
			List<Integer> position = SplitStringUtil.getPosition(baseCollect.getPosition());
			baseCollect.setX(position.get(0));
			baseCollect.setY(position.get(1));
			baseCollect.setZ(position.get(2));
			
			baseCollectMap.put(baseCollect.getId(), baseCollect);
		}		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_COLLECT, baseCollectMap);	
		
		// 初始普通采集			
		this.initCollectMap(50);
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseCollect getBaseCollectById(int collectId){
		Map<Integer, BaseCollect> baseCollectMap = (Map<Integer, BaseCollect>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_COLLECT);
		if (baseCollectMap == null) return null;
		return baseCollectMap.get(collectId);
	}	

	@Override
	public int startCollect(long playerId, int playerCollectId) throws Exception{
		if(playerId < 1 || playerCollectId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();		
		IRewardService rewardService = serviceCollection.getRewardService();	
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_COLLECT)) {	
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return ExceptionConstant.ERROR_10000;
			
			SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());	
			if(sceneModel.getCollectMap() == null) return ExceptionConstant.ERROR_10000;
			Collect collect = getCollect(playerCollectId, playerPuppet,sceneModel);			
			if(collect == null) return ExceptionConstant.COLLECT_2407;
			BaseCollect baseCollect = this.getBaseCollectById(collect.getCollectId());			
			synchronized (collect.getCollectLock()){	
				
				// 判断采集信息是否过期
				if(collect.getState() == BattleConstant.COLLECT_REMOVE) return ExceptionConstant.COLLECT_2400;
				
				// 判断当前采集id, 是否和任务相关
				if(collect.getType() == BattleConstant.COLLECT_TASK){
					if(!this.isTaskCollect(playerId, collect.getCollectId())) return ExceptionConstant.COLLECT_2405;
				}
				
				// 已在采集列表中
				if (collect.getPlayerIds().contains(playerId)) return ExceptionConstant.COLLECT_2406;
				 
				// 采集人数限制
				if(baseCollect.getMaxNum() > 0){
					if (collect.getPlayerIds().size() >= baseCollect.getMaxNum()) return ExceptionConstant.COLLECT_2404;
				}
				
				// 采集次数限制
				if(baseCollect.getCount() > 0){
					if (collect.getCollectNum() >= baseCollect.getCount()) return ExceptionConstant.COLLECT_2401;
				}	
				
				// 采集消耗判定
				try {
					rewardService.expendJudgment(playerId, baseCollect.getExpendList(), false, "");
				} catch (Exception e1) {
					return ExceptionConstant.COLLECT_2408;
				}
				
				//采集范围   
				int x = baseCollect.getX();
				int z = baseCollect.getZ();
				int space = (x - playerPuppet.getX())*(x-playerPuppet.getX()) + (z-playerPuppet.getZ())*(z-playerPuppet.getZ());
				if(space > space + 1000) return ExceptionConstant.COLLECT_2407;					
				
				// 采集时间设置
				long currentTime = System.currentTimeMillis();				
				playerPuppet.setCollectTime(currentTime);				
				playerPuppet.setPlayerCollectId(playerCollectId);
				if (baseCollect.getType() == BattleConstant.COLLECT_SENIOR){
					collect.getPlayerIds().add(playerId);
				}
				
			}
		}		
		return 0;
	}

	@Override
	public Collect getCollect(int playerCollectId, PlayerPuppet playerPuppet,SceneModel sceneModel) {
		Collect collect = null;
		List<Integer> grids = sceneModel.getGridMap().get(playerPuppet.getGridId());
		for(Integer gridId : grids){
			Map<Integer, Collect> map = sceneModel.getCollectMap().get(gridId);
			if(map != null){
				collect = map.get(playerCollectId);	
				if(collect != null){
					return collect;
				}
			}							
		}
		
		return null;		 
	}

	@Override
	public void endCollect(SceneModel sceneModel, PlayerPuppet playerPuppet, Collect collect, BaseCollect baseCollect){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IRewardService rewardService = serviceCollection.getRewardService();
		ISceneService sceneService = serviceCollection.getSceneService();	
		 
		synchronized (LockService.getPlayerLockByType(playerPuppet.getEid(), LockConstant.PLAYER_COLLECT)) {
			// 采集消耗判定			
			if(baseCollect.getExpendList()!= null && !baseCollect.getExpendList().isEmpty()){
				try {
					rewardService.expendJudgment(playerPuppet.getEid(), baseCollect.getExpendList(), true, InOutLogConstant.DIAMOND_OF_13);
				} catch (Exception e1) {
					// 飘字					
					serviceCollection.getCommonService().sendNoticeMsg(playerPuppet.getEid(), ResourceUtil.getValue("collect_expend"));		
					this.InterruptCollect(playerPuppet.getEid(), collect.getPlayerCollectId());
					
					S_EndCollect.Builder builder = S_EndCollect.newBuilder();
					builder.setPlayerCollectId(collect.getPlayerCollectId());
					MessageObj msg = new MessageObj(MessageID.S_EndCollect_VALUE, builder.build().toByteArray());
					gameSocketService.sendDataToPlayerByPlayerId(playerPuppet.getEid(), msg);
					return;
				}							
			}
			
			if(playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;	
			long currentTime = System.currentTimeMillis();
			 switch (collect.getType()) {
				case BattleConstant.COLLECT_SENIOR:
					if(!collect.getPlayerIds().isEmpty()){
						Long playerId = playerPuppet.getEid();
						if (collect.getPlayerIds().contains(playerId)){
							collect.getPlayerIds().remove(playerId);
						}
					}
					
					//采集数据设置								
					if(collect.getCollectNum() >= baseCollect.getCount() && baseCollect.getRecover() > 0){						
						collect.setRefreshDate(currentTime + baseCollect.getRecover());
						collect.setState(BattleConstant.COLLECT_REMOVE);	
						
						List<Long> playerIds = sceneService.getNearbyPlayerIdsByGridId(sceneModel, collect.getGridId());
						List<Integer> collectIdList = new ArrayList<Integer>();
						collectIdList.add(collect.getPlayerCollectId());
						this.offerRemoveCollect(collectIdList, playerIds);
					}		
					break;
				case BattleConstant.COLLECT_GENGRAL:
					
					// 采集奖励					
					int itemId = this.randomCollectRewardId(baseCollect.getCollectRewardMap().get(playerPuppet.getCareer()));
					try {
						rewardService.fetchRewardOne(playerPuppet.getEid(), RewardTypeConstant.ITEM, itemId, 1, 0);
					} catch (Exception e) {
						// 飘字					
						serviceCollection.getCommonService().sendNoticeMsg(playerPuppet.getEid(), ResourceUtil.getValue("bag_1"));		
						this.InterruptCollect(playerPuppet.getEid(), collect.getPlayerCollectId());
						
						S_EndCollect.Builder builder = S_EndCollect.newBuilder();
						builder.setPlayerCollectId(collect.getPlayerCollectId());
						MessageObj msg = new MessageObj(MessageID.S_EndCollect_VALUE, builder.build().toByteArray());
						gameSocketService.sendDataToPlayerByPlayerId(playerPuppet.getEid(), msg);
						return;
					}
					
					sceneModel.getCollectMap().get(collect.getGridId()).remove(collect.getPlayerCollectId());
					
					this.getGeneralCollectCacheMap(playerPuppet.getLine()).remove(collect.getCollectId());
					List<Long> playerIds = sceneService.getNearbyPlayerIdsByGridId(sceneModel, collect.getGridId());
					List<Integer> collectIdList = new ArrayList<Integer>();
					collectIdList.add(collect.getPlayerCollectId());
					this.offerRemoveCollect(collectIdList, playerIds);
					
					// 刷新采集点
					this.refreshGeneralCollect(playerPuppet);
					break;
				case BattleConstant.COLLECT_TASK:
					//触发采集任务
					try {
						List<Integer> conditionList = new ArrayList<Integer>();				
						conditionList.add(collect.getCollectId());
						serviceCollection.getTaskService().executeTask(playerPuppet.getEid(), TaskConstant.TYPE_10, conditionList);
					} catch (Exception e) {
						LogUtil.error("触发采集任务异常：", e);
					}
					
					break;
				default:
					System.out.print("采集错误 Id is :" + collect.getPlayerCollectId());
					break;
				}
				
					
			// 情况玩家采集数据				
			playerPuppet.setPlayerCollectId(0);
			playerPuppet.setCollectTime(0);
			playerPuppet.setCollectRewardRefTime(0);						
			 
			S_EndCollect.Builder builder = S_EndCollect.newBuilder();
			builder.setPlayerCollectId(collect.getPlayerCollectId());
			MessageObj msg = new MessageObj(MessageID.S_EndCollect_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerPuppet.getEid(), msg);			
		}			
	}
	
	@Override
	public Collect createCollect(SceneModel sceneModel, BaseCollect baseCollect){
		
		Collect collect = new Collect();
		collect.setPlayerCollectId(SerialNumberUtil.getCollectId());
		collect.setCollectId(baseCollect.getId());
		collect.setType(baseCollect.getType());
		collect.setCollectNum(0);
		collect.setPlayerIds(new ArrayList<Long>());
		collect.setRefreshDate(0);
		collect.setState(BattleConstant.COLLECT_NORMAL);	
		collect.setGridId(NineGridUtil.calInGrid(baseCollect.getX(), baseCollect.getZ(), sceneModel.getColNum()));
		
		Map<Integer, Collect> map = sceneModel.getCollectMap().get(collect.getGridId());
		if(map == null){
			map = new ConcurrentHashMap<Integer, Collect>();
			sceneModel.getCollectMap().put(collect.getGridId(),map);
		}
		map.put(collect.getPlayerCollectId(), collect);			
		return collect;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseCollect> getBaseCollectByType(int type){
		Map<Integer, BaseCollect> baseCollectMap = (Map<Integer, BaseCollect>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_COLLECT);
		List<BaseCollect> list = new ArrayList<BaseCollect>();
		for(Map.Entry<Integer, BaseCollect> entry : baseCollectMap.entrySet()){
			BaseCollect baseCollect = entry.getValue();
			if(baseCollect.getType() != type) continue;
			list.add(baseCollect);
		}
		
		return list;
	}	
	
	/**
	 *  随机一个采集点
	 */
	private BaseCollect radomGeneralCollect(int line){		
		List<BaseCollect> baseCollectList = this.getBaseCollectByType(BattleConstant.COLLECT_GENGRAL);
		Collections.shuffle(baseCollectList);
		
		for (BaseCollect baseCollect : baseCollectList){
			if(!this.getGeneralCollectCacheMap(line).containsKey(baseCollect.getId())){
				this.getGeneralCollectCacheMap(line).put(baseCollect.getId(), true);
				return baseCollect;
			}
		}
		
		return null;
	}

	/**
	 *  随机多个普通采集点
	 */
	private void initCollectMap(int num){
		List<BaseCollect> baseCollectList = this.getBaseCollectByType(BattleConstant.COLLECT_GENGRAL);
		if(baseCollectList.size() < num) return;
		
		for(int line = 1; line <= SceneConstant.MAX_LINE; line++){
			Collections.shuffle(baseCollectList);
			for (int i = 0; i < num; i++){
				this.getGeneralCollectCacheMap(line).put(baseCollectList.get(i).getId(), true);
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Boolean> getGeneralCollectCacheMap(int line){		
		Map<Integer, Map<Integer, Boolean>>  cacheMap = (Map<Integer, Map<Integer, Boolean>>)CacheService.getFromCache(CacheConstant.GENERAL_COLLECT);
		if(cacheMap == null){
			cacheMap = new ConcurrentHashMap<Integer, Map<Integer,Boolean>>();	
			CacheService.putToCache(CacheConstant.GENERAL_COLLECT, cacheMap);
		}
		Map<Integer, Boolean> map = cacheMap.get(line);
		if(map == null){
			map = new ConcurrentHashMap<Integer, Boolean>();
			cacheMap.put(line, map);
		}
		return map;
	}
	

	@Override
	public void quartzRefreshSeniorCollectAward(SceneModel sceneModel, PlayerPuppet playerPuppet, Collect collect, BaseCollect baseCollect){		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		int itemId = this.randomCollectRewardId(baseCollect.getCollectRewardMap().get(playerPuppet.getCareer()));
		IRewardService rewardService = serviceCollection.getRewardService();
		
		if(playerPuppet != null){
			try {			
				rewardService.fetchRewardOne(playerPuppet.getEid(), RewardTypeConstant.ITEM, itemId, 1, 0);
			} catch (Exception e) {
				// 飘字					
				serviceCollection.getCommonService().sendNoticeMsg(playerPuppet.getEid(), ResourceUtil.getValue("bag_1"));		
				this.InterruptCollect(playerPuppet.getEid(), collect.getPlayerCollectId());				
			}	
			
		}
			
		
		collect.setCollectNum(collect.getCollectNum() + 1);
		if(collect.getCollectNum() == baseCollect.getCount()) {
			try {
				this.endCollect(sceneModel, playerPuppet, collect, baseCollect);
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}			
	}
	
	/**
	 *  获取一个采集点，刷新致场景内，同步场景内所有在线玩家
	 */
	private void refreshGeneralCollect(PlayerPuppet playerPuppet){		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		BaseCollect baseCollect = this.radomGeneralCollect(playerPuppet.getLine());		
		if(baseCollect == null) return;
		
		BaseMap baseMap = sceneService.getBaseMap(baseCollect.getMapId());
		if(baseMap == null) return;		
		
		// 获取地图场景信息
		String sceneGuid = null;	
		if (baseMap.getMapType() == SceneConstant.MAIN_CITY) {
			sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_CITY, baseCollect.getMapId(), playerPuppet.getLine());
		} else if (baseMap.getMapType() == SceneConstant.WORLD_SCENE) {
			sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_WORLD, baseCollect.getMapId(), playerPuppet.getLine());
		}
		
		SceneModel sceneModel = sceneService.getSceneModel(sceneGuid);
		if (sceneModel == null) return;	
		
		// 将采集点设置到场景内
		Collect collect = this.createCollect(sceneModel, baseCollect);
		
		// 同步场景内所有在线玩家	
		List<Long> playerIds = sceneService.getNearbyPlayerIdsByGridId(sceneModel, collect.getGridId());
		List<Collect> collectList = new ArrayList<Collect>();
		collectList.add(collect);		
		this.offerAddCollect(collectList, playerIds);
	}
	
	/**
	 *  根据权重计算出奖励物品Id
	 */
	private int randomCollectRewardId(List<CollectReward> listCollectReward){
		// 如数据只有一条，则直接返回
		if(listCollectReward.size() < 2){
			return listCollectReward.get(0).getId();
		}
		
		int m = 0;
		int n = RandomService.getRandomNum(10000);		
		for(int i = 0; i < listCollectReward.size(); i++){				
			if(n< m + listCollectReward.get(i).getRate()){
				return  listCollectReward.get(i).getId();
			}
			 m += listCollectReward.get(i).getRate();  
		}	
		return 0;
	}
	
	/**
	 *  添加采集点
	 */
	public void offerAddCollect(List<Collect> collectList, List<Long> playerIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_AddCollectItemInfos.Builder builder = S_AddCollectItemInfos.newBuilder();		
		for(Collect collect : collectList){
			builder.addListCollectItemInfos(protoBuilderService.buildCollectMsg(collect));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_AddCollectItemInfos_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerList(playerIds, msg);			
	}
	
	/**
	 *  移除采集点
	 */
	public void offerRemoveCollect(List<Integer> collectIds, List<Long> playerIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		S_RemoveCollectItemInfos.Builder builder = S_RemoveCollectItemInfos.newBuilder();		
		builder.addAllPlayerCollectIds(collectIds);
		
		MessageObj msg = new MessageObj(MessageID.S_RemoveCollectItemInfos_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerList(playerIds, msg);	
	}
	
	/**
	 *  判断玩家是否可做任务采集
	 */
	private boolean isTaskCollect(long playerId, int collectId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ITaskService taskService = serviceCollection.getTaskService();
		Map<Integer, PlayerTask> playerTaskMap = taskService.getPlayerTaskMapByPlayerId(playerId);
		
		for(Map.Entry<Integer, PlayerTask> entry : playerTaskMap.entrySet()){
			PlayerTask playerTask  = entry.getValue();
			if(playerTask.getConditionType() == TaskConstant.TYPE_10){
				BaseTask baseTask = taskService.getBaseTask(playerTask.getTaskId());
				if(baseTask.getConditionList().get(0).equals(collectId)) {	
					return true;
				}				
			}
		}		
		return false;		
	}
	
	/**
	 *  玩家下线, 清理采集信息
	 */
	@Override
	public void clearCollect(PlayerPuppet playerPuppet){
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();				
		ISceneService sceneService = serviceCollection.getSceneService();
		
		if(playerPuppet.getPlayerCollectId() > 0){
			SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());	
			Collect collect = null;
			List<Integer> grids = sceneModel.getGridMap().get(playerPuppet.getGridId());
			for(Integer gridId : grids){
				Map<Integer, Collect> map = sceneModel.getCollectMap().get(gridId);
				if(map != null){
					collect = map.get(playerPuppet.getPlayerCollectId());
					if(collect != null){
						break;
					}
				}				
			}	
			
			// 清理采集点的玩家信息
			if(collect != null && collect.getPlayerIds() != null && !collect.getPlayerIds().isEmpty()){
				Long playerId = playerPuppet.getEid();
				if(collect.getPlayerIds().contains(playerId)){
					collect.getPlayerIds().remove(playerId);
				}
			}
		}
		
		// 清理玩家的采集信息
		playerPuppet.setCollectTime(0);
		playerPuppet.setCollectRewardRefTime(0);
		playerPuppet.setPlayerCollectId(0);
	}

	@Override
	public void InterruptCollect(Long playerId, int playerCollectId){
		if(playerId < 1 || playerCollectId < 1) return;		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();				
		ISceneService sceneService = serviceCollection.getSceneService();			
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_COLLECT)) {	
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;
			
			SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());	
			Collect collect = getCollect(playerCollectId, playerPuppet,sceneModel);			
			if(collect == null) return;
			if (collect.getType() == BattleConstant.COLLECT_SENIOR){					
				if(!collect.getPlayerIds().isEmpty()){
					if (collect.getPlayerIds().contains(playerId)){
						collect.getPlayerIds().remove(playerId);
					}						
				}
			}
			// 清理玩家的采集信息
			playerPuppet.setCollectTime(0);
			playerPuppet.setCollectRewardRefTime(0);
			playerPuppet.setPlayerCollectId(0);
		}		
	}
}
