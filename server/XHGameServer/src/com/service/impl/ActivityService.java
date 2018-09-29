package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.json.JSONException;
import org.json.JSONObject;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.MD5Service;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.HttpConstant;
import com.constant.InOutLogConstant;
import com.constant.LockConstant;
import com.constant.RewardConstant;
import com.constant.RewardTypeConstant;
import com.dao.activity.BaseActivityDAO;
import com.dao.activity.PlayerActivityDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.activity.BaseChargeActivity;
import com.domain.activity.BaseTomb;
import com.domain.activity.BaseTurntable;
import com.domain.activity.PlayerTomb;
import com.domain.activity.TruntableRecord;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.reward.BaseReward;
import com.domain.reward.RewardRecord;
import com.message.ActivityProto.S_BuyArtifactData;
import com.message.ActivityProto.S_BuyGrowthFound;
import com.message.ActivityProto.S_ChangeTomb;
import com.message.ActivityProto.S_GetBVAwardData;
import com.message.ActivityProto.S_GetFristPayData;
import com.message.ActivityProto.S_GetGiftAward;
import com.message.ActivityProto.S_GetIdentCheckInfo;
import com.message.ActivityProto.S_GetLevelAwardData;
import com.message.ActivityProto.S_GetOpenServerData;
import com.message.ActivityProto.S_GetOpenServerReward;
import com.message.ActivityProto.S_GetPayActData;
import com.message.ActivityProto.S_GetSevenPayData;
import com.message.ActivityProto.S_GetTombData;
import com.message.ActivityProto.S_GetTurnRecList;
import com.message.ActivityProto.S_GetTurntableData;
import com.message.ActivityProto.S_SynRewardList;
import com.message.ActivityProto.S_Tomb;
import com.message.ActivityProto.S_TurntableDraw;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IActivityService;
import com.service.IBagService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.util.HttpUtil;
import com.util.LogUtil;
import com.util.PatternUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 运营活动系统
 * @author ken
 * 
 * @date 2017-5-11
 */
public class ActivityService implements IActivityService{
	private BaseActivityDAO baseActivityDAO = new BaseActivityDAO();
	private PlayerActivityDAO playerActivityDAO = new PlayerActivityDAO();
	
	@Override
	public void initBaseActivity() {
		Map<Integer, BaseTurntable> baseTruntableMap = new HashMap<>();
		List<BaseTurntable>	baseTruntableList = baseActivityDAO.listBaseTurntable();
		
		for(BaseTurntable model : baseTruntableList){
			List<Integer> list = SplitStringUtil.getIntList(model.getReward());
			model.setTurnReward(new Reward(list.get(0), list.get(1), list.get(2), 0, list.get(3)));
			baseTruntableMap.put(model.getId(), model);			
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TURNTABLE, baseTruntableMap);	
		
		//陵墓配置表
		Map<Integer, List<BaseTomb>> groupTombMap = new LinkedHashMap<Integer, List<BaseTomb>>();
		Map<Integer, BaseTomb> baseTombMap = new HashMap<Integer, BaseTomb>();
		List<BaseTomb> listBaseTombs = baseActivityDAO.listBaseTombs();
		for(BaseTomb model : listBaseTombs){
			baseTombMap.put(model.getId(), model);
			
			List<BaseTomb> lists = groupTombMap.get(model.getGroup());
			if(lists == null){
				lists = new ArrayList<BaseTomb>();
				groupTombMap.put(model.getGroup(), lists);
			}
			lists.add(model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TOMB, baseTombMap);
		BaseCacheService.putToBaseCache(CacheConstant.BASE_GROUP_TOMB, groupTombMap);
		
		// 七天累计充值
		List<BaseChargeActivity> listBaseChargeActivity = baseActivityDAO.listBaseChargeActivity();
		for(BaseChargeActivity model : listBaseChargeActivity){
			model.setRewardList(SplitStringUtil.getIntList(model.getRewardStr()));		
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_CHARGE_ACTIVITY, listBaseChargeActivity);
		
		// 初始转盘奖励
		initTrunReward();		
	}
	
	/**
	 * 获取转盘抽奖物品记录
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ConcurrentLinkedDeque<TruntableRecord> getTruntableRecordQueue(){
		ConcurrentLinkedDeque<TruntableRecord> trlist = (ConcurrentLinkedDeque<TruntableRecord>) CacheService.getFromCache(CacheConstant.TRUNTABLE_DRAW_RECODE);
		if(trlist == null){
			trlist = new ConcurrentLinkedDeque();			
			CacheService.putToCache(CacheConstant.TRUNTABLE_DRAW_RECODE, trlist);
		}
		
		return trlist;	
	}
	
	/**
	 * 获取七天累计充值活动数据
	 */
	@SuppressWarnings("unchecked")
	private List<BaseChargeActivity> getBaseChargeActivityList(){		
		return (List<BaseChargeActivity>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_CHARGE_ACTIVITY);
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_TOMB + playerId);
	}
	
	@Override
	public void getOnlineReward(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ONLINE_REWARD)) {
			IPlayerService playerService = serviceCollection.getPlayerService();
			IRewardService rewardService = serviceCollection.getRewardService();
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_ONLINE, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
		
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);		
			
			if(playerDaily.getRewardIdList().contains(rewardId)) throw new GameException(ExceptionConstant.ONLINE_3001);
			int difTime = playerService.difTime(playerExt.getLoginTime());
			
			int onlineTime = (playerDaily.getEveryOnlineTime() + difTime) / 60; 
			if(onlineTime < baseReward.getCondition())  throw new GameException(ExceptionConstant.ONLINE_3000);
			
			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerDaily.getRewardIdList().add(rewardId);
			playerDaily.setRewardIdList(playerDaily.getRewardIdList());
			playerService.updatePlayerDaily(playerDaily);			
		}
	}

	@Override
	public void getRewardList(long playerId){	
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ONLINE_REWARD)) {
			IPlayerService playerService = serviceCollection.getPlayerService();			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);	
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			int difTime = playerService.difTime(playerExt.getLoginTime());
			
			S_SynRewardList.Builder builder = S_SynRewardList.newBuilder();
			builder.setOnlineTime(playerDaily.getEveryOnlineTime() + difTime);
			if(playerDaily.getRewardIdList() != null){
				for(Integer rewardId : playerDaily.getRewardIdList()){				
					builder.addRewardList(protoBuilderService.buildRewardMsg(rewardId, 2));
				}
			}
			
			MessageObj msg = new MessageObj(MessageID.S_SynRewardList_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}

	@Override
	public void getPayActData(long playerId){
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			
			S_GetPayActData.Builder builder = S_GetPayActData.newBuilder();
			builder.setBuyGrowthFundNum(playerService.getBuyGrowthFundNum());
			builder.setDailyRrecharge(playerDaily.getTodayPay());		
			builder.setTotalRrecharge(playerWealth.getTotalPay());
			builder.setTotalSpend(playerWealth.getTotalSpend());
			builder.setIsbuyGrowthFund(playerOptional.getIsBuyGrowthFund());
			builder.addAllDrRewardList(playerDaily.getDrRrewardIdList());
			builder.addAllGfRewardList(playerOptional.getGfRewardIdList());
			builder.addAllNwRewardList(playerOptional.getNwRewardIdList());
			builder.addAllTrRewardList(playerOptional.getTrRewardIdList());
			builder.addAllTsRewardList(playerOptional.getTsRewardIdList());
			
			MessageObj msg = new MessageObj(MessageID.S_GetPayActData_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	
	}

	@Override
	public void getFristPayReward(long playerId,  int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIST_PAY)) {
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_FRIST_PAY, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getFristPayRewardState() != 1) throw new GameException(ExceptionConstant.ACTIVITY_2808);
			
			
			List<Reward> rewards = new ArrayList<Reward>();
			for(Reward reward : baseReward.getRewardList()){
				if(reward.getType() == RewardTypeConstant.EQUIPMENT){
					BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
					Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
					if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
						continue;
					}	
				}				
				rewards.add(reward);				
			}
			
			rewardService.fetchRewardList(playerId, rewards);
			
			playerOptional.setFristPayRewardState(2);
			
			playerService.updatePlayerOptional(playerOptional);
		}
	}

	@Override
	public void getTotalRrechargeReward(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_TOTAL_RECHARGE, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);			
			if(playerOptional.getTrRewardIdList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if(playerWealth.getTotalPay() < baseReward.getCondition())throw new GameException(ExceptionConstant.ACTIVITY_2808);
			
			List<Reward> rewards = new ArrayList<Reward>();
			for(Reward reward : baseReward.getRewardList()){
				if(reward.getType() == RewardTypeConstant.EQUIPMENT){
					BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
					Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
					if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
						continue;
					}	
				}				
				
				rewards.add(reward);
			}
			
			rewardService.fetchRewardList(playerId, rewards);
			
			playerOptional.getTrRewardIdList().add(rewardId);
			playerOptional.setTrRewardIdList(playerOptional.getTrRewardIdList());
			
			playerService.updatePlayerOptional(playerOptional);
		}		
	}

	@Override
	public void getDailyRrechargeReward(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_DAILY_RECHARGE, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily.getDrRrewardIdList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			if(playerDaily.getTodayPay() < baseReward.getCondition())throw new GameException(ExceptionConstant.ACTIVITY_2808);
			
			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerDaily.getDrRrewardIdList().add(rewardId);
			playerDaily.setDrRrewardIdList(playerDaily.getDrRrewardIdList());
			
			playerService.updatePlayerDaily(playerDaily);
		}	
	}

	@Override
	public void getGrowthFund(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_GROWTH_FUND, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getGfRewardIdList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if(playerProperty.getLevel() < baseReward.getCondition())throw new GameException(ExceptionConstant.ACTIVITY_2808);
			
			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerOptional.getGfRewardIdList().add(rewardId);
			playerOptional.setGfRewardIdList(playerOptional.getGfRewardIdList());
			
			playerService.updatePlayerOptional(playerOptional);
		}			
	}
 
	@Override
	public void getNationalWelfare(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_NATIONAL_WELFARE, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getNwRewardIdList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			if(playerService.getBuyGrowthFundNum() < baseReward.getCondition())throw new GameException(ExceptionConstant.ACTIVITY_2808);
			
			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerOptional.getNwRewardIdList().add(rewardId);
			playerOptional.setNwRewardIdList(playerOptional.getNwRewardIdList());
			playerService.updatePlayerOptional(playerOptional);
		}		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getTurntableData(long playerId){
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();

		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		if(playerDaily == null) return;
		
		Map<Integer, BaseTurntable> turnRewardMap = (Map<Integer, BaseTurntable>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TURNTABLE_REWARD);
		if(turnRewardMap == null) {
			LogUtil.error("getTurntableData turnRewardMap null");
		}
		
		S_GetTurntableData.Builder builder = S_GetTurntableData.newBuilder();
		builder.setFristTurntableState(playerDaily.getFristTurntableState());
		builder.addAllTrIdList(turnRewardMap.keySet());
		
		MessageObj msg = new MessageObj(MessageID.S_GetTurntableData_VALUE, builder.build().toByteArray());
 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void turntableDraw(long playerId, int type) throws Exception {
		if(playerId < 1 || type < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		IBagService bagService = serviceCollection.getBagService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			// 判断背包空间
			int freeGridNum = bagService.getFreeGridNumByPlayerID(playerId);
			if (freeGridNum < 1) {
				throw new GameException(ExceptionConstant.BAG_1304);
			}
			
			if(type == RewardConstant.TURNTABLE_DRAW_TYPE_1){
				PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
				if(playerDaily.getFristTurntableState() == 1) throw new GameException(ExceptionConstant.ACTIVITY_2809); 
				playerDaily.setFristTurntableState(1);
				playerService.updatePlayerDaily(playerDaily);
			}else if(type == RewardConstant.TURNTABLE_DRAW_TYPE_2){
				int TRUNTABLE_ITEM_ID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRUNTABLE_ITEM_ID);	
				if(bagService.getItemNumByPlayerIdAndItemId(playerId, TRUNTABLE_ITEM_ID) < 1) new GameException(ExceptionConstant.BAG_1306);
				rewardService.deductItem(playerId, TRUNTABLE_ITEM_ID, 1, true);
			}else if(type == RewardConstant.TURNTABLE_DRAW_TYPE_3){
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);			
				int TRUNTABLE_COST_DIAMOND= serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRUNTABLE_COST_DIAMOND);
				if(playerWealth.getDiamond() < TRUNTABLE_COST_DIAMOND) throw new GameException(ExceptionConstant.PLAYER_1113);
				playerService.addDiamond_syn(playerId, -TRUNTABLE_COST_DIAMOND, InOutLogConstant.DIAMOND_OF_8);
			}
			
			// 随机奖励
			Map<Integer, BaseTurntable> turntableMap =(Map<Integer, BaseTurntable>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_TURNTABLE_REWARD);
			List<Reward> rewards = new ArrayList<>();
			for(BaseTurntable baseTurntable : turntableMap.values()){
				Reward reward =	new Reward();
				reward.setId(baseTurntable.getId());			
				
				if(type == RewardConstant.TURNTABLE_DRAW_TYPE_1){
					reward.setRate(baseTurntable.getFristDraw());
				}else if(type == RewardConstant.TURNTABLE_DRAW_TYPE_2){
					reward.setRate(baseTurntable.getFreeDraw());
				}else if(type == RewardConstant.TURNTABLE_DRAW_TYPE_3){
					reward.setRate(baseTurntable.getGoldDraw());
				}
				
				rewards.add(reward);				
			}			

			BaseTurntable turnReward = turntableMap.get(rewardService.globalRandom(rewards).getId());
			rewardService.fetchRewardOne(playerId, turnReward.getTurnReward().getType(),
					turnReward.getTurnReward().getId(), turnReward.getTurnReward().getNum(), 
					turnReward.getTurnReward().getBlind(), 1);		
			
			// 抽奖数据存入缓存
			if(turnReward.getIsBroadcast() == 1){
				TruntableRecord trunRecord = null;
				ConcurrentLinkedDeque<TruntableRecord> deque = this.getTruntableRecordQueue();		
				
				int TRUNTABLE_RECORD_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRUNTABLE_RECORD_NUM);
							
				if(deque.size() >= TRUNTABLE_RECORD_NUM){
					trunRecord = deque.pollLast();
					
				}else{
					trunRecord = new TruntableRecord();
				}
				
				trunRecord.setPlayerId(playerId);
				trunRecord.setRewardId(turnReward.getId());
				deque.addFirst(trunRecord);
				
				CacheService.putToCache(CacheConstant.TRUNTABLE_DRAW_RECODE, deque);
			}
			
			S_TurntableDraw.Builder builder = S_TurntableDraw.newBuilder();
			builder.setRewardId(turnReward.getId());			
			MessageObj msg = new MessageObj(MessageID.S_TurntableDraw_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);			
		}				
	}

	@SuppressWarnings("unchecked")
	private void initTrunReward() {	
		Map<Integer, BaseTurntable> baseTruntableMap =(Map<Integer, BaseTurntable>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_TURNTABLE);
		List<BaseTurntable> turnRewards = new ArrayList<>(baseTruntableMap.values());
		
		Map<Integer, BaseTurntable> rewardMap =(Map<Integer, BaseTurntable>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TURNTABLE_REWARD);
		if(rewardMap == null){
			rewardMap = new ConcurrentHashMap<Integer, BaseTurntable>();
		}else{
			rewardMap.clear();
		}
		
		for(BaseTurntable baseTurntable : RandomService.getArrayFromArrayByNum(turnRewards, 12)){
			rewardMap.put(baseTurntable.getId(), baseTurntable);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TURNTABLE_REWARD, rewardMap);
	}

	@Override
	public void getTurnRecList(long playerId, int start, int offset) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		ConcurrentLinkedDeque<TruntableRecord> trQueue = this.getTruntableRecordQueue();
		
		// 拿出队列里面所有的数据, 存入列表
		List<TruntableRecord> trlist = new ArrayList<>(trQueue);		
		List<TruntableRecord> pageList = new ArrayList<>();
		if (trlist != null && trlist.size() > 0) {	
			
			int value = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRUNTABLE_SHOW_NUM);	
			
			if(start <= trlist.size()) {
				int fromIndex = start - 1;
				if(fromIndex < 0) fromIndex = 0;
				int toIndex = fromIndex + offset;
				if(toIndex > value) toIndex = value;
				if(toIndex > trlist.size()) toIndex = trlist.size();
				
				pageList = trlist.subList(fromIndex, toIndex);
			}
		}
		
		S_GetTurnRecList.Builder builder = S_GetTurnRecList.newBuilder();	
		for(TruntableRecord model : pageList){
			Player player = serviceCollection.getPlayerService().getPlayerByID(model.getPlayerId());
			if(player == null) continue;
			
			builder.addTurnRecList(protoBuilderService.buildTurnRecMsg(playerId, player.getPlayerName(), model.getRewardId()));
		}		
		MessageObj msg = new MessageObj(MessageID.S_GetTurnRecList_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId,  msg);	
	}	

	/********************************陵墓**************************************/
	/**
	 * 陵墓奖励配置
	 * 
	 */
	@SuppressWarnings("unchecked")
	private BaseTomb getBaseTomb(int id){
		Map<Integer, BaseTomb> baseTombMap = (Map<Integer, BaseTomb>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TOMB);
		return baseTombMap.get(id);
	}
	
	/**
	 * 获取玩家陵墓数据
	 */
	public PlayerTomb getPlayerTomb(long playerId){
		PlayerTomb playerTomb = (PlayerTomb) CacheService
				.getFromCache(CacheConstant.PLAYER_TOMB + playerId);
		if (playerTomb == null) {
			playerTomb = playerActivityDAO.getPlayerTomb(playerId);
			if (playerTomb != null) {
				
				CacheService.putToCache(CacheConstant.PLAYER_TOMB + playerId, playerTomb);
			}
		}
		return playerTomb;
	}
	
	/** 
	 * 同步缓存
	 */
	private void updatePlayerTomb(PlayerTomb playerTomb){
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_TOMB);
		if (!lists.contains(playerTomb)) {
			lists.add(playerTomb);
		}
	}
	
	/**
	 * 奖励池中随机生成12个  每个分组4个
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> extractTombIds(){
		List<BaseTomb> tombs = new ArrayList<BaseTomb>();
		Map<Integer, List<BaseTomb>> map = (Map<Integer, List<BaseTomb>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GROUP_TOMB);
		for(Map.Entry<Integer, List<BaseTomb>> entry : map.entrySet()){
			List<BaseTomb> lists = entry.getValue();
			tombs.addAll(RandomService.getArrayFromArrayByNum(lists, 1));
		}
		
		List<Integer> tombIds = new ArrayList<Integer>();
		for(int i = 0; i < 9 ; i++){
			if(tombs.size() == 1){
				tombIds.add(tombs.get(0).getId());
				break;
			}	
			int randomSum = 0;
			for(BaseTomb model : tombs){
				randomSum += model.getRate();
			}
			
			int random = RandomService.getRandomNum(randomSum);
			int rate = 0;
			int pos = 0;
			for(int index = 0; index < tombs.size(); index ++){
				BaseTomb model = tombs.get(index);
				if(random <= model.getRate() + rate){
					tombIds.add(model.getId());
					pos = index;
					break;
				}
				rate += model.getRate();
			}
			tombs.remove(pos);
		}

		tombs = null;
		
		return tombIds;
	}
	
	@Override
	public void getTombData(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TOMB)) {
			PlayerTomb playerTomb = this.getPlayerTomb(playerId);
			if(playerTomb == null){
				playerTomb = new PlayerTomb();
				playerTomb.setPlayerId(playerId);
				playerTomb.setTombIdList(this.extractTombIds());
				List<Integer> usedList = new ArrayList<Integer>();
				for(int i = 0; i< 9; i++){
					usedList.add(0);
				}
				playerTomb.setUsedTombIdList(usedList);
				playerActivityDAO.createPlayerTomb(playerTomb);
				
				CacheService.putToCache(CacheConstant.PLAYER_TOMB + playerId, playerTomb);
			}
			
			List<Integer> randList = new ArrayList<Integer>(playerTomb.getTombIdList());
			Collections.shuffle(randList);
			
			S_GetTombData.Builder builder = S_GetTombData.newBuilder();
			builder.addAllTombIdList(randList);
			builder.addAllUsedTombIdList(playerTomb.getUsedTombIdList());
			builder.setTombNum(playerTomb.getTombNum());
			builder.setGreenNum(playerTomb.getGreenNum());
			builder.setBlueNum(playerTomb.getBlueNum());
			builder.setVioletNum(playerTomb.getVioletNum());
			builder.setOrangeNum(playerTomb.getOrangeNum());
			MessageObj msg = new MessageObj(MessageID.S_GetTombData_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
	
	@Override
	public void tomb(long playerId, int tombIndex) throws Exception {
		if(playerId < 1 || tombIndex < 0 || tombIndex > 8) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TOMB)) {
			PlayerTomb playerTomb = this.getPlayerTomb(playerId);
			if(playerTomb == null){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			List<Integer> usedList = playerTomb.getUsedTombIdList();
			Integer id = usedList.get(tombIndex);
			if(id > 0){
				throw new GameException(ExceptionConstant.ACTIVITY_2804);	
			}
			
			//扣除次数对应摸金令
			int size = 0;
			for(Integer tbid : usedList){
				if(tbid > 0){
					size ++;
				}
			}
			
			//获取随机出来的陵墓编号
			List<Integer> tombIds = playerTomb.getTombIdList();
			int tombId = tombIds.get(size);
			BaseTomb baseTomb = this.getBaseTomb(tombId);
			if(baseTomb == null) throw new GameException(ExceptionConstant.ERROR_10000);
			
			BaseItem baseItem = serviceCollection.getBagService().getBaseItemById(baseTomb.getItemId());
			if(baseItem == null) throw new GameException(ExceptionConstant.BAG_1300); 
			
			int TOMB_ITEM_ID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TOMB_ITEM_ID_3);
			if(size < 3){
				TOMB_ITEM_ID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TOMB_ITEM_ID_1);
			}else if(size < 6){
				TOMB_ITEM_ID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TOMB_ITEM_ID_2);
			}
			
			int itemNum = (int)Math.pow(2, size % 3);
			serviceCollection.getRewardService().deductItem(playerId, TOMB_ITEM_ID, itemNum, true);
			
			serviceCollection.getRewardService().fetchRewardOne(playerId, RewardTypeConstant.ITEM, baseItem.getId(), baseTomb.getCount(), 0);
			
			//加入已探索列表
			usedList.set(tombIndex, tombId);
			playerTomb.setUsedTombIdList(usedList);
			
			playerTomb.setTombNum(playerTomb.getTombNum() + 1);
			
			//根据不同品阶添加对应陵墓室
			switch (baseItem.getRare()) {
			case 2:
				playerTomb.setGreenNum(playerTomb.getGreenNum() + 1);
				break;
			case 3:
				playerTomb.setBlueNum(playerTomb.getBlueNum() + 1);	
				break;
			case 4:
				playerTomb.setVioletNum(playerTomb.getVioletNum() + 1);
				break;
			case 5:
				playerTomb.setOrangeNum(playerTomb.getOrangeNum() + 1);
				break;
			default:
				break;
			}
			this.updatePlayerTomb(playerTomb);
			
			S_Tomb.Builder builder = S_Tomb.newBuilder();
			builder.setTombIndex(tombIndex);
			builder.setTombId(tombId);
			builder.setTombNum(playerTomb.getTombNum());
			builder.setGreenNum(playerTomb.getGreenNum());
			builder.setBlueNum(playerTomb.getBlueNum());
			builder.setVioletNum(playerTomb.getVioletNum());
			builder.setOrangeNum(playerTomb.getOrangeNum());
			MessageObj msg = new MessageObj(MessageID.S_Tomb_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void changeTomb(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TOMB)) {
			PlayerTomb playerTomb = this.getPlayerTomb(playerId);
			if(playerTomb == null){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			List<Integer> usedList = playerTomb.getUsedTombIdList();
			
			int size = 0;
			for(Integer tbid : usedList){
				if(tbid > 0){
					size ++;
				}
			}
			
			if(size < 9){
				int TOMB_CHANGE_DIAMOND = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TOMB_CHANGE_DIAMOND);
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
				if(playerWealth.getDiamond() < TOMB_CHANGE_DIAMOND) throw new GameException(ExceptionConstant.PLAYER_1113);
				playerService.addDiamond_syn(playerId, -TOMB_CHANGE_DIAMOND, InOutLogConstant.DIAMOND_OF_7);
			}
			
			playerTomb.setTombIdList(this.extractTombIds());
			List<Integer> newUsedList = new ArrayList<Integer>();
			for(int i = 0; i< 9; i++){
				newUsedList.add(0);
			}
			playerTomb.setUsedTombIdList(newUsedList);
			
			this.updatePlayerTomb(playerTomb);
			
			List<Integer> randList = new ArrayList<Integer>(playerTomb.getTombIdList());
			Collections.shuffle(randList);
			
			S_ChangeTomb.Builder builder = S_ChangeTomb.newBuilder();
			builder.addAllTombIdList(randList);
			builder.addAllUsedTombIdList(playerTomb.getUsedTombIdList());
			MessageObj msg = new MessageObj(MessageID.S_ChangeTomb_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void buyGrowthFund(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BUY_GROUTH_FOUND)) {
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getIsBuyGrowthFund() == 1) throw new GameException(ExceptionConstant.ACTIVITY_2810); 
		
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			int GROWTH_FOUND_PRICE= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GROWTH_FOUND_PRICE); 
			if(playerWealth.getDiamond() < GROWTH_FOUND_PRICE) throw new GameException(ExceptionConstant.PLAYER_1113);
			
			playerOptional.setIsBuyGrowthFund(1);
			playerService.updatePlayerOptional(playerOptional);
			
			playerService.addDiamond_syn(playerId, -GROWTH_FOUND_PRICE, InOutLogConstant.DIAMOND_OF_10);
			
			S_BuyGrowthFound.Builder builder = S_BuyGrowthFound.newBuilder();
			builder.setState(0);
			MessageObj msg = new MessageObj(MessageID.S_BuyGrowthFound_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}

	@Override
	public void getTotalSpendReward(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_TOTAL_SPEND, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getTrRewardIdList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if(playerWealth.getTotalSpend() < baseReward.getCondition())throw new GameException(ExceptionConstant.ACTIVITY_2808);

			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerOptional.getTsRewardIdList().add(rewardId);
			playerOptional.setTsRewardIdList(playerOptional.getTsRewardIdList());
			
			playerService.updatePlayerOptional(playerOptional);
		}			
	}	

	@Override
	public void buyArtifact(long playerId) {
		if(playerId < 1) return;
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IMailService mailService = serviceCollection.getMailService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ARTIFACT)) {
			int BUY_ARTIFACT_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.BUY_ARTIFACT_VALID_TIME); 
			int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
			if(day > BUY_ARTIFACT_VALID_TIME) return;
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getIsBuyArtifact() == 1) return;
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_BUY_ARTIFACT, 1201);
			if(baseReward == null){
				LogUtil.error("buyArtifact  baseReward id null, id is" + 1201);
				return;
			}
			
			List<Reward> rewards = new ArrayList<Reward>();
			for(Reward reward : baseReward.getRewardList()){
				if(reward.getType() == RewardTypeConstant.EQUIPMENT){
					BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
					Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
					if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
						continue;
					}	
				}				
				rewards.add(reward);
				
			}			
			
			int[][] mailItems = new int[rewards.size()][];			
			int index = 0;
			for(Reward reward : rewards){
					mailItems[index] = new int[]{reward.getType(),reward.getId(),reward.getNum(),reward.getBlind()};
					index++;
			}			
			
			mailService.systemSendMail(playerId, ResourceUtil.getValue("buy_artifact_theme"),  ResourceUtil.getValue("buy_actifect_content"), SplitStringUtil.getStringByIntIntList(mailItems), 0);
		
			playerOptional.setIsBuyArtifact(1);
			playerService.updatePlayerOptional(playerOptional);
			
			S_BuyArtifactData.Builder builder = S_BuyArtifactData.newBuilder();
			builder.setBuyArtActState(1);
			builder.setBuyArtifactState(1);
			MessageObj msg = new MessageObj(MessageID.S_BuyArtifactData_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}
	

	@Override
	public void getOpenServerReward(long playerId, int rewardId) throws Exception {
		if(playerId < 1 || rewardId < 1)throw new GameException(ExceptionConstant.ERROR_10000);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			int OPEN_SERVER_SEVEN_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.OPEN_SERVER_SEVEN_VALID_TIME); 
			int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
			if(day > OPEN_SERVER_SEVEN_VALID_TIME)throw new GameException(ExceptionConstant.ACTIVITY_2801);
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_OPEN_SERVER, rewardId);
			if(baseReward == null) throw new GameException(ExceptionConstant.ACTIVITY_2807);
		
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt == null) return;
			
			if(playerExt.getAddLoginDay() < baseReward.getCondition()) throw new GameException(ExceptionConstant.ACTIVITY_2808);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);			
			if(playerOptional.getOsRewardIdList().contains(rewardId)) throw new GameException(ExceptionConstant.ACTIVITY_2806);
		
			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerOptional.getOsRewardIdList().add(rewardId);
			playerOptional.setOsRewardIdList(playerOptional.getOsRewardIdList());
			playerService.updatePlayerOptional(playerOptional);
			
			S_GetOpenServerReward.Builder builder = S_GetOpenServerReward.newBuilder();
			builder.setId(rewardId);
			MessageObj msg = new MessageObj(MessageID.S_GetOpenServerReward_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void getOpenServerData(long playerId){
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);	
			
			S_GetOpenServerData.Builder builder = S_GetOpenServerData.newBuilder();
			int OPEN_SERVER_SEVEN_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.OPEN_SERVER_SEVEN_VALID_TIME); 
			int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
			if(day <= OPEN_SERVER_SEVEN_VALID_TIME){
				builder.setState(1);
			}			
			builder.setAddLoginDay(playerExt.getAddLoginDay());
			builder.addAllRewardList(playerOptional.getOsRewardIdList());
			MessageObj msg = new MessageObj(MessageID.S_GetOpenServerData_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public BaseChargeActivity getBaseChargeActivity() {
		BaseChargeActivity baseChargeActivity = null;
		for(BaseChargeActivity model : this.getBaseChargeActivityList()){
			if(DateService.isInTime(DateService.getDateString(model.getStartDate()), DateService.getDateString(model.getEndDate()))){
				baseChargeActivity = model;
				break; 
			}
		}
		
		return baseChargeActivity;
	}

	@Override
	public void getSevenPayData(long playerId) throws Exception{		
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			
			// 判断当前时间是否在活动时间内
			BaseChargeActivity baseChargeActivity = this.getBaseChargeActivity();
			if(baseChargeActivity == null) throw new GameException(ExceptionConstant.ACTIVITY_2801);
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);		
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);	
			
			S_GetSevenPayData.Builder builder = S_GetSevenPayData.newBuilder();
			builder.setSevenPay(playerWealth.getSevenPay());
			builder.addAllRewardList(playerOptional.getSpRewardIdList());
			MessageObj msg = new MessageObj(MessageID.S_GetSevenPayData_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}

	/**
	 * 判断是否达到七天累计充值奖励条件
	 */
	@Override
	public void getSevenPayReward(long playerId, BaseChargeActivity baseChargeActivity) {		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IMailService mailService = serviceCollection.getMailService();
		IRewardService rewardService = serviceCollection.getRewardService();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTIVITY)) {
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);		
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);	
			
			for(int rewardId : baseChargeActivity.getRewardList()){
				BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_SEVEN_RECHARGE, rewardId);
				if(baseReward == null){
					LogUtil.error("getSevenPayReward  baseReward id null, id is" + rewardId);
					return;
				}
				
				if(playerWealth.getSevenPay() < baseReward.getCondition()) continue;
				if(playerOptional.getSpRewardIdList().contains(rewardId)) continue;
				
				List<Reward> rewards = new ArrayList<Reward>();
				for(Reward reward : baseReward.getRewardList()){
					if(reward.getType() == RewardTypeConstant.EQUIPMENT){
						BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
						if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
							continue;
						}	
					}				
					rewards.add(reward);
				}			
				
				int[][] mailItems = new int[rewards.size()][];			
				int index = 0;
				for(Reward reward : rewards){
						mailItems[index] = new int[]{reward.getType(),reward.getId(),reward.getNum(),reward.getBlind()};
						index++;
				}			
				
				mailService.systemSendMail(playerId, ResourceUtil.getValue("mail_1"), ResourceUtil.getValue("pay_seven_content", DateService.dateFormatYMD(new Date()), baseReward.getCondition()), SplitStringUtil.getStringByIntIntList(mailItems), 0);
			
				playerOptional.getSpRewardIdList().add(rewardId);
				playerOptional.setSpRewardIdList(playerOptional.getSpRewardIdList());
				playerService.updatePlayerOptional(playerOptional);
				
				S_GetSevenPayData.Builder builder = S_GetSevenPayData.newBuilder();
				builder.setSevenPay(playerWealth.getSevenPay());
				builder.addAllRewardList(playerOptional.getSpRewardIdList());
				MessageObj msg = new MessageObj(MessageID.S_GetSevenPayData_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			}			
		}		
	}
	
	@Override
	public void quartzDaily() {
		// 每天0点调度重置转盘奖励
		initTrunReward();
	}
	
	@Override
	public void quartzDailyAfter() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		for(BaseChargeActivity model : this.getBaseChargeActivityList()){
			boolean isCurrentDay = DateService.isCurrentDay(DateService.getDateByString(model.getEndDate()));
			if(!isCurrentDay) continue;			
			playerActivityDAO.updateAllPlayerSevenPayData();
			
			
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();		
			List<Long> playerIDSetClone = new ArrayList<Long>(playerService.getPlayerIDCache());
			
			for(Long playerId : playerIDSetClone){
				PlayerWealth playerWealth = (PlayerWealth) CacheService.getFromCache(CacheConstant.PLAYER_WEALTH_CACHE + playerId);
				if (playerWealth != null) {	
					
					PlayerOptional playerOptional = (PlayerOptional) CacheService.getFromCache(CacheConstant.PLAYER_OPTIONAL_CACHE + playerId);
					playerOptional.getSpRewardIdList().clear();
					playerOptional.setSpRewardIdList(playerOptional.getSpRewardIdList());
					
					playerWealth.setSevenPay(0);
					
					boolean online = gameSocketService.checkOnLine(playerId);
					if(online){							
						S_GetSevenPayData.Builder builder = S_GetSevenPayData.newBuilder();
						builder.setSevenPay(playerWealth.getSevenPay());
						builder.addAllRewardList(playerOptional.getSpRewardIdList());
						MessageObj msg = new MessageObj(MessageID.S_GetSevenPayData_VALUE, builder.build().toByteArray());
						gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);						
					}				
				}			
			}
			
			break;
		}		
	}

	@Override
	public void buyArtifactData(long playerId) {
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ARTIFACT)) {
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);			
			S_BuyArtifactData.Builder builder = S_BuyArtifactData.newBuilder();				
			int BUY_ARTIFACT_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.BUY_ARTIFACT_VALID_TIME); 
			int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
			if(day <= BUY_ARTIFACT_VALID_TIME){
				builder.setBuyArtActState(1);
			}		
			builder.setBuyArtifactState(playerOptional.getIsBuyArtifact());	
			
			MessageObj msg = new MessageObj(MessageID.S_BuyArtifactData_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
		
	}

	@Override
	public void getFristPayData(long playerId) {
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIST_PAY)) {
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);			
			S_GetFristPayData.Builder builder = S_GetFristPayData.newBuilder();				
			builder.setFristPayRewardState(playerOptional.getFristPayRewardState());			
			MessageObj msg = new MessageObj(MessageID.S_GetFristPayData_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}	
	@Override
	public int getLevelAward(long playerId, int rewardId) throws Exception{
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_LEVEL_AWARD)) {
			// 活动日期
			int ACT_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.ACT_VALID_TIME); 
			int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
			if(day > ACT_VALID_TIME) throw new GameException(ExceptionConstant.ACTIVITY_2801);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getLevelRewardList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_LEVEL, rewardId);
			if(baseReward == null){
				LogUtil.error("getLevelAward  baseReward id null, id is" + rewardId);
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			synchronized (LockConstant.PLAYER_LEVEL_AWARD) {
				RewardRecord rewardRecord = rewardService.getRewardRecord(rewardId);
				if(rewardRecord == null){
					rewardRecord = new RewardRecord();
					rewardRecord.setRewardId(rewardId);
					rewardRecord.setNum(0);
					rewardService.createRewardRecord(rewardRecord);
				}else{
					if(rewardRecord.getNum() >= baseReward.getResCondition()) throw new GameException(ExceptionConstant.ACTIVITY_2813);
				}
				
				List<Reward> rewards = new ArrayList<Reward>();
				for(Reward reward : baseReward.getRewardList()){
					if(reward.getType() == RewardTypeConstant.EQUIPMENT){
						BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
						if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
							continue;
						}	
					}				
					rewards.add(reward);				
				}	
				
				rewardService.fetchRewardList(playerId, rewards);
				
				playerOptional.getLevelRewardList().add(rewardId);
				playerOptional.setLevelRewardList(playerOptional.getLevelRewardList());
				playerService.updatePlayerOptional(playerOptional);
				
				rewardRecord.setNum(rewardRecord.getNum() + 1);
				rewardService.updateRewardRecord(rewardRecord);
				
				return rewardRecord.getNum();
			}

		}		
	}
	@Override
	public int getBattleValueAward(long playerId, int rewardId) throws Exception{
		if(playerId < 1 || rewardId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_LEVEL_AWARD)) {
			// 活动日期
			int ACT_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.ACT_VALID_TIME); 
			int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
			if(day > ACT_VALID_TIME) throw new GameException(ExceptionConstant.ACTIVITY_2801);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			if(playerOptional.getBvRewardList().contains(rewardId))throw new GameException(ExceptionConstant.ACTIVITY_2806);
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_BATTLE_VALUE, rewardId);
			if(baseReward == null){
				LogUtil.error("GetLevelAwardData  baseReward id null, id is" + rewardId);
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			synchronized (LockConstant.PLAYER_LEVEL_AWARD) {
				RewardRecord rewardRecord = rewardService.getRewardRecord(rewardId);
				if(rewardRecord == null){
					rewardRecord = new RewardRecord();
					rewardRecord.setRewardId(rewardId);
					rewardRecord.setNum(0);
					rewardService.createRewardRecord(rewardRecord);
				}else{
					if(rewardRecord.getNum() >= baseReward.getResCondition()) throw new GameException(ExceptionConstant.ACTIVITY_2813);
				}
				
				List<Reward> rewards = new ArrayList<Reward>();
				for(Reward reward : baseReward.getRewardList()){
					if(reward.getType() == RewardTypeConstant.EQUIPMENT){
						BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
						if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
							continue;
						}	
					}				
					rewards.add(reward);				
				}	
				
				rewardService.fetchRewardList(playerId, rewards);
				
				playerOptional.getBvRewardList().add(rewardId);
				playerOptional.setBvRewardList(playerOptional.getBvRewardList());
				playerService.updatePlayerOptional(playerOptional);
				
				rewardRecord.setNum(rewardRecord.getNum() + 1);
				rewardService.updateRewardRecord(rewardRecord);
				
				return rewardRecord.getNum();
			}
			
		}		
	}
	@Override
	public void getLevelAwardData(long playerId) {
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_LEVEL_AWARD)) {
			
			S_GetLevelAwardData.Builder builder = S_GetLevelAwardData.newBuilder();	
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);	
			builder.addAllOwnRewardList(playerOptional.getLevelRewardList());
			
			Map<Integer, BaseReward> map = rewardService.listBaseRewards(RewardConstant.REWARD_LEVEL);
			if(map != null){
				for(Map.Entry<Integer, BaseReward> entry : map.entrySet()){
					int num = 0;
					RewardRecord rewardRecord = rewardService.getRewardRecord(entry.getKey());
					if(rewardRecord != null){
						num = rewardRecord.getNum();
					}
					builder.addAllRewardList(protoBuilderService.buildRewardDataMsg(entry.getKey(), num));
				}
			}
						
			MessageObj msg = new MessageObj(MessageID.S_GetLevelAwardData_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}	
	}
	@Override
	public void getBVAwardData(long playerId) {
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IRewardService rewardService = serviceCollection.getRewardService();	
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BATTLE_VALUE_AWARD)) {
				
			S_GetBVAwardData.Builder builder = S_GetBVAwardData.newBuilder();
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
			builder.addAllOwnRewardList(playerOptional.getBvRewardList());
			
			Map<Integer, BaseReward> map = rewardService.listBaseRewards(RewardConstant.REWARD_BATTLE_VALUE);
			if(map != null){
				for(Map.Entry<Integer, BaseReward> entry : map.entrySet()){
					int num = 0;
					RewardRecord rewardRecord = rewardService.getRewardRecord(entry.getKey());
					if(rewardRecord != null){
						num = rewardRecord.getNum();
					}
					builder.addAllRewardList(protoBuilderService.buildRewardDataMsg(entry.getKey(), num));
				}
			}		
			
			MessageObj msg = new MessageObj(MessageID.S_GetBVAwardData_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
		
	}

	@Override
	public void useActCode(long playerId, String code) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IMailService mailService = serviceCollection.getMailService();
		IRewardService rewardService = serviceCollection.getRewardService();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ACTCODE)) {
			Player player  = playerService.getPlayerByID(playerId);
			if(player == null) return;
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", player.getUserId());
			jsonObject.put("code", code);
			jsonObject.put("agent", Config.AGENT);
			jsonObject.put("sign", MD5Service.encryptToUpperString(player.getUserId() + code + Config.AGENT + Config.WEB_LOGIN_KEY));
			
			String result = HttpUtil.httpsRequest(Config.ACCOUNT_URL + HttpConstant.ACTCODE, "POST", jsonObject.toString());
			if(result == null) throw new GameException(ExceptionConstant.ERROR_2);
			
			JSONObject resultJson = new JSONObject(result);
			int state = resultJson.getInt("result");
			if(state != 0){
				if(state == 1){
					throw new GameException(ExceptionConstant.ERROR_10000);
				}else if(state == 2){
					throw new GameException(ExceptionConstant.ACTIVITY_2814);
				}else if(state == 3){
					throw new GameException(ExceptionConstant.ACTIVITY_2815);
				}
				return;
			}
			
			int type = resultJson.getInt("type");
			int rewardId = resultJson.getInt("rewardId");
			BaseReward baseReward = rewardService.getBaseReward(type, rewardId);
			if(baseReward == null){
				LogUtil.error("actCode  baseReward id null, id is" + rewardId);
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			List<Reward> rewards = new ArrayList<Reward>();
			for(Reward reward : baseReward.getRewardList()){
				if(reward.getType() == RewardTypeConstant.EQUIPMENT){
					BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
					if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
						continue;
					}	
				}				
				rewards.add(reward);
			}			
			
			int[][] mailItems = new int[rewards.size()][];			
			int index = 0;
			for(Reward reward : rewards){
					mailItems[index] = new int[]{reward.getType(),reward.getId(),reward.getNum(),reward.getBlind()};
					index++;
			}
			
			mailService.systemSendMail(playerId, baseReward.getDes(), ResourceUtil.getValue("act_code_2"), SplitStringUtil.getStringByIntIntList(mailItems), 0);
	
			S_GetGiftAward.Builder builder = S_GetGiftAward.newBuilder();	
			builder.setState(state);	
			MessageObj msg = new MessageObj(MessageID.S_GetGiftAward_VALUE, builder.build().toByteArray());
	 		gameSocketService.sendDataToPlayerByUserId(player.getUserId(), msg);
		}
	}
	
	@Override
	public void getIdentCheckInfo(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
	
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		Player player = playerService.getPlayerByID(playerId);
		
		int icState = 0;
		if(playerOptional.getIcRewardState() > 0){
			icState = 1;
		}else{
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", player.getUserId());
				String result = HttpUtil.httpsRequest(Config.ACCOUNT_URL + HttpConstant.IDENTITY_STATE, "POST", jsonObject.toString());
				if(result != null) {
					JSONObject resultJson = new JSONObject(result);
					icState = resultJson.getInt("result");
					
					playerOptional.setIcRewardState(icState);
					playerService.updatePlayerOptional(playerOptional);
				}
			} catch (JSONException e) {
				
			}
		
		}
		
		S_GetIdentCheckInfo.Builder builder = S_GetIdentCheckInfo.newBuilder();
		builder.setIcState(icState);
		builder.setRewardState(playerOptional.getIcRewardState());
		
		MessageObj msg = new MessageObj(MessageID.S_GetIdentCheckInfo_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByUserId(player.getUserId(), msg);	
	}

	@Override
	public int identityCheck(long playerId, String identity, String realName) throws Exception {
	
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
				
		IPlayerService playerService = serviceCollection.getPlayerService();
		Player player = playerService.getPlayerByID(playerId);
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		
		if(playerOptional.getIcRewardState() > 0) throw new GameException(ExceptionConstant.ACTIVITY_2816);
		
		// 检测真实姓名长度
		if(realName == null || realName.trim().equals("") || realName.length() < 2 || realName.length() > 6) throw new GameException(ExceptionConstant.ACTIVITY_2817);
		
		// 检测身份证ID
		if(!PatternUtil.verForm(identity)) throw new GameException(ExceptionConstant.ACTIVITY_2818); 
				
		// 实名认证请求
		JSONObject jsonObject = new JSONObject();
		long time = System.currentTimeMillis();
		jsonObject.put("userId", player.getUserId());
		jsonObject.put("realName", realName.trim());
		jsonObject.put("identity", identity.trim());
		jsonObject.put("time", time);
		jsonObject.put("sign", MD5Service.encryptToUpperString(player.getUserId() + realName + identity + time + Config.WEB_LOGIN_KEY));
		String result = HttpUtil.httpsRequest(Config.ACCOUNT_URL + HttpConstant.IDENTITY_CHECK, "POST", jsonObject.toString());
		if(result == null) throw new GameException(ExceptionConstant.ERROR_2); 
	
		JSONObject resultJson = new JSONObject(result);
		int state = resultJson.getInt("result");
		if(state != 0){
			if(state == 2){
				throw new GameException(ExceptionConstant.ACTIVITY_2816); 
			}else if(state == 4){
				throw new GameException(ExceptionConstant.ACTIVITY_2819); 
			}else{
				LogUtil.error("identityCheck 内部错误 ：" + state);
			}			
		}else{			
			playerOptional.setIcRewardState(1);
			playerService.updatePlayerOptional(playerOptional);
		}
		
		return playerOptional.getIcRewardState();
	}

	@Override
	public void getIdCheckAward(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		if(playerOptional.getIcRewardState() == 0){
			throw new GameException(ExceptionConstant.ACTIVITY_2820);
		}else if(playerOptional.getIcRewardState() == 2){
			throw new GameException(ExceptionConstant.ACTIVITY_2806); 
		}
		
		BaseReward baseReward = rewardService.getBaseReward(RewardConstant.IDENTITY_CHECK_VALUE, 1501);
		rewardService.fetchRewardList(playerId, baseReward.getRewardList());
		
		playerOptional.setIcRewardState(2);
		playerService.updatePlayerOptional(playerOptional);
		
	}
}
