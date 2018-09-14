package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.dao.wing.BaseWingDAO;
import com.dao.wing.PlayerWingDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseItem;
import com.domain.chat.Notice;
import com.domain.player.Player;
import com.domain.player.PlayerWealth;
import com.domain.puppet.PlayerPuppet;
import com.domain.wing.BaseWing;
import com.domain.wing.BaseWingUpStar;
import com.domain.wing.PlayerWing;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.WingProto.S_AddWing;
import com.message.WingProto.S_Evolve;
import com.message.WingProto.S_UnEvolve;
import com.service.IBagService;
import com.service.IChatService;
import com.service.IEquipmentService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.IWingService;
import com.util.IDUtil;
import com.util.SplitStringUtil;

public class WingService implements IWingService {
	BaseWingDAO baseWingDAO = new BaseWingDAO();
	PlayerWingDAO playerWingDAO = new PlayerWingDAO();	

	@Override
	public void initBaseWing() {		
		Map<Integer, BaseWing> baseWingMap = new HashMap<Integer, BaseWing>();
		List<BaseWing> listBaseWing = baseWingDAO.listBaseWing();
		for(BaseWing baseWing : listBaseWing){	
			List<List<Integer>> declists = SplitStringUtil.getIntIntList(baseWing.getDecomposeStr());
			List<Reward> decomposeList = new ArrayList<Reward>();
			for(List<Integer> l : declists){			
				Reward reward = new Reward(l.get(0), l.get(1), l.get(2), l.get(3), 0);
				decomposeList.add(reward);
			}			
			baseWing.setDecomposeList(decomposeList);
			baseWing.setBasePropertyList(SplitStringUtil.getIntIntList(baseWing.getBaseProperty()));
			baseWing.setExpendStrList(SplitStringUtil.getIntIntList(baseWing.getExpendStr()));
			List<List<Integer>> lists = SplitStringUtil.getIntIntList(baseWing.getUpStarStr());
			for(List<Integer> exp : lists){
				int star = exp.get(0);
				BaseWingUpStar baseWingUpStar = new BaseWingUpStar(star, exp.get(1), exp.get(2));
				baseWing.getUpStarMap().put(star, baseWingUpStar);
			}			
			baseWingMap.put(baseWing.getWingId(), baseWing);
		}			
		BaseCacheService.putToBaseCache(CacheConstant.BASE_WING, baseWingMap);	
	}


	@Override
	public void putOnWing(long playerId, int wingId) throws Exception {
		if(playerId <= 0 || wingId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WING)) {
			PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
			if(playerPuppet == null) return;
			
			BaseWing baseWing = this.getBaseWing(wingId);
			if(baseWing == null){
				throw new GameException(ExceptionConstant.WING_3103);
			}	
			
			Map<Integer, PlayerWing> playerWingMap = this.getPlayerWingMap(playerId);			
			PlayerWing playerWing = playerWingMap.get(wingId);
			if(playerWing == null) throw new GameException(ExceptionConstant.WING_3102);
			if(playerWing.getDressFlag() == 1) throw new GameException(ExceptionConstant.WING_3100);
			playerWing.setDressFlag(1);			
			this.updatePlayerWing(playerWing);			
		
			// 卸载已穿戴翅膀
			Map<Integer, Integer> minusProMap = null;
			if(playerPuppet.getWingStyle() > 0){				
				for(Map.Entry<Integer, PlayerWing> entry : playerWingMap.entrySet()){
					PlayerWing downPlayerWing = entry.getValue();	
					BaseWing downBaseWing = this.getBaseWing(downPlayerWing.getWingId());
					if(downBaseWing == null){
						throw new GameException(ExceptionConstant.WING_3103);
					}	
					
					if(downPlayerWing.getDressFlag() == 1 && downPlayerWing.getWingId() != wingId){
						downPlayerWing.setDressFlag(0);
						this.updatePlayerWing(downPlayerWing);
						
						//取掉属性
						minusProMap = getWingAddProMap(downPlayerWing, downBaseWing, -1);	
						break;
					}
				}				
			}	
			
			//穿戴属性
			Map<Integer, Integer> addProMap = getWingAddProMap(playerWing, baseWing, 1);			
			if(minusProMap == null){
				serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);
			}else{
				//改变玩家属性值
				Map<Integer, Integer> proMap = this.dealWingProValue(minusProMap, addProMap);
				serviceCollection.getPropertyService().addProValue(playerId, proMap, true, true);
			}
			
			// 同步翅膀外形			
			playerPuppet.setWingStyle(baseWing.getDressStyle());		
			playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.WING_STYLE, baseWing.getDressStyle());
		}	
	}

	@Override
	public void putDownWing(long playerId, int wingId) throws Exception {
		if(playerId <= 0 || wingId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WING)) {
			BaseWing baseWing = this.getBaseWing(wingId);
			if(baseWing == null){
				throw new GameException(ExceptionConstant.WING_3103);
			}	
			
			PlayerWing playerWing = this.getPlayerWingMap(playerId).get(wingId);	
			if(playerWing == null) throw new GameException(ExceptionConstant.WING_3102);
			if(playerWing.getDressFlag() == 0) throw new GameException(ExceptionConstant.WING_3101);
			playerWing.setDressFlag(0);			
			this.updatePlayerWing(playerWing);	
			
			// 改变玩家属性
			Map<Integer, Integer> minusProMap = getWingAddProMap(playerWing, baseWing, -1);		
			//改变玩家属性值
			serviceCollection.getPropertyService().addProValue(playerId, minusProMap, true, true);
			
			// 同步翅膀外形
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
			
			if(playerPuppet != null){
				playerPuppet.setWingStyle(0);
				playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.WING_STYLE, 0);		
			}
		}		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, PlayerWing> getPlayerWingMap(long playerId){
		Map<Integer, PlayerWing> playerWingMap = (Map<Integer, PlayerWing>)CacheService.getFromCache(CacheConstant.PLAYER_WING + playerId);
		if(playerWingMap == null){
			playerWingMap = new ConcurrentHashMap<Integer, PlayerWing>();			
			List<PlayerWing> list = playerWingDAO.listPlayerWing(playerId);
			for(PlayerWing playerWing : list){
				playerWingMap.put(playerWing.getWingId(), playerWing);
			}			
			CacheService.putToCache(CacheConstant.PLAYER_WING + playerId, playerWingMap);
		}
		
		return playerWingMap;		
	}

	@Override
	public void evolve(long playerId, int type, int wingId, int itemId) throws Exception {
		if(playerId <= 0 || wingId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WING)) {
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();			
			IRewardService rewardService = serviceCollection.getRewardService(); 
			
			IBagService bagService = serviceCollection.getBagService();
			IPlayerService playerService = serviceCollection.getPlayerService();
			
			//获取玩家羽翼信息
			PlayerWing playerWing = this.getPlayerWingMap(playerId).get(wingId);
			if(playerWing == null) throw new GameException(ExceptionConstant.WING_3102);			
		
			BaseWing baseWing = this.getBaseWing(wingId);	
			if(playerWing.getStar() >= baseWing.getUpStarMap().size() - 1) throw new GameException(ExceptionConstant.WING_3104);
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if(type == ItemConstant.EVOLVE_TYPE_1){
				int needValue = baseWing.getUpStarMap().get(playerWing.getStar() + 1).getNeedExp() - baseWing.getUpStarMap().get(playerWing.getStar()).getNeedExp();
				if(playerWealth.getWingValue() + playerWing.getWingValue() < needValue){
					playerWing.setWingValue(playerWing.getWingValue() + playerWealth.getWingValue());
					playerWealth.setWingValue(0);
				}else{
					playerWealth.setWingValue(playerWealth.getWingValue() - (needValue - playerWing.getWingValue()));
					playerService.updatePlayerWealth(playerWealth);	
					playerWing.setWingValue(0);
					
					this.upPlayerWing(playerId, playerWing, baseWing);
				}				
				playerService.updatePlayerWealth(playerWealth);
			}else if(type == ItemConstant.EVOLVE_TYPE_2){
				
				//获取物品信息 
				BaseItem baseItem = bagService.getBaseItemById(itemId);
				int addValue = baseItem.getEffectValue();
				
				//扣除道具
				rewardService.deductItem(playerId, itemId, 1, true);
				//扣除羽化消耗			
				rewardService.deductItemList(playerId, baseWing.getExpendStrList());
				
				int newValue = playerWing.getWingValue() + addValue;			
				//根据玩家当前的等级获取升级需要的羽灵值， 判断是否能升级	
			
				while (newValue >= baseWing.getUpStarMap().get(playerWing.getStar() + 1).getNeedExp() - baseWing.getUpStarMap().get(playerWing.getStar()).getNeedExp()) {				
					//改变羽翼星级
				    newValue = newValue - (baseWing.getUpStarMap().get(playerWing.getStar() + 1).getNeedExp() - baseWing.getUpStarMap().get(playerWing.getStar()).getNeedExp());
				    
				    this.upPlayerWing(playerId, playerWing, baseWing);
				    
				    if(playerWing.getStar() >= baseWing.getUpStarMap().size() - 1){
						break;
					}		   
				}	
				
				playerWing.setWingValue(newValue);
			}			
			
			if(playerWing.getStar() >= baseWing.getUpStarMap().size() - 1){
				playerWing.setWingValue(baseWing.getUpStarMap().get(playerWing.getStar()).getNeedExp());
				
				// 羽翼满星级广播
				IChatService chatService = serviceCollection.getChatService();
				Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
				List<Notice> paramList = new ArrayList<Notice>();
				
				Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
				Notice notice2 = new Notice(ParamType.WING, baseWing.getWingId(), 0, "");
				
				paramList.add(notice1);
				paramList.add(notice2);
				
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_4, paramList, gameSocketService.getOnLinePlayerIDList());
			}			 
						
			this.updatePlayerWing(playerWing);	
			
			S_Evolve.Builder builder = S_Evolve.newBuilder();			
			builder.setWingMsg(protoBuilderService.buildWingMsg(playerWing));	
			builder.setTotalWingValue(playerWealth.getWingValue());
			MessageObj msg = new MessageObj(MessageID.S_Evolve_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	/** 羽翼升星处理*/
	private void upPlayerWing(long playerId, PlayerWing playerWing, BaseWing baseWing){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		playerWing.setStar(playerWing.getStar() + 1);					
		
		// 如果装备在身上，则改变属性值
		if(playerWing.getDressFlag() == 1){
			// 升星后的数据
			BaseWingUpStar baseWingUpStar = baseWing.getUpStarMap().get(playerWing.getStar());
			// 升星前的数据
			BaseWingUpStar baseWingUpStarLast = baseWing.getUpStarMap().get(playerWing.getStar() - 1);
			
			//升星后的属性 - 升星前的属性 
			List<List<Integer>> basePropertyList = baseWing.getBasePropertyList();
			Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
			for(int i = 0; i < basePropertyList.size(); i++){
				List<Integer> lists = basePropertyList.get(i);
				int propertyId = lists.get(0);
				int propertyValue = lists.get(1);
				
				int addProValue = 0;
				if(baseWingUpStarLast != null){
					addProValue = (int)(propertyValue * baseWingUpStar.getAddAttr() * 0.0001) - (int)(propertyValue * baseWingUpStarLast.getAddAttr() * 0.0001);	
				}else{
					addProValue = (int)(propertyValue * baseWingUpStar.getAddAttr() * 0.0001);
				}
				
				addProMap.put(propertyId, addProValue);							
			}	
			
			//改变玩家属性值
			serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);
		}
		
		// 计算评分
		Map<Integer, Integer> proMap = this.getWingAddProMap(playerWing, baseWing, 1);	
		int wingScore = equipmentService.calculateScore(proMap);				
		playerWing.setWingScore(wingScore);			
	}

	private void updatePlayerWing(PlayerWing playerWing) {
		// 同步缓存更新
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_WING);
		if (!lists.contains(playerWing)) {
			lists.add(playerWing);
		}
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_WING+playerId);			
	}
	
	@SuppressWarnings("unchecked")
	private BaseWing getBaseWing(int wingId){
		Map<Integer, BaseWing> baseWingMap = (Map<Integer, BaseWing>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_WING);
		
		return baseWingMap.get(wingId);
	}
	
	@Override
	public void addWing_syn(long playerId, int wingId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		BaseWing baseWing = this.getBaseWing(wingId);
		if(baseWing == null){				
			 throw new GameException(ExceptionConstant.WING_3103);
		}
		
		PlayerWing playerWing = this.getPlayerWingMap(playerId).get(wingId);					
		// 自动分解
		if(playerWing != null){
			rewardService.fetchRewardList(playerId, baseWing.getDecomposeList());
		}else{
			playerWing = new PlayerWing();
			playerWing.setId(IDUtil.geneteId(PlayerWing.class));
			playerWing.setPlayerId(playerId);
			playerWing.setDressFlag(0);
			playerWing.setStar(0);
			playerWing.setWingValue(0);
			playerWing.setWingId(wingId);
			
			// 计算评分
			Map<Integer, Integer> proMap = this.getWingAddProMap(playerWing, baseWing, 1);	
			if(proMap != null){
				int wingScore = equipmentService.calculateScore(proMap);				
				playerWing.setWingScore(wingScore);
			}			
			
			playerWingDAO.createPlayerWing(playerWing);
			this.getPlayerWingMap(playerId).put(wingId, playerWing);
			
			// 获得羽翼飘字
			IChatService chatService = serviceCollection.getChatService();
			Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
			List<Notice> paramList = new ArrayList<Notice>();
			
			Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
			Notice notice2 = new Notice(ParamType.WING, baseWing.getWingId(), 0, "");
			
			paramList.add(notice1);
			paramList.add(notice2);
			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_21, paramList, gameSocketService.getOnLinePlayerIDList());
		}
		
		S_AddWing.Builder builder = S_AddWing.newBuilder();			
		builder.setWingId(wingId);			
		MessageObj msg = new MessageObj(MessageID.S_AddWing_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);	
	}
	
	/** 获取羽翼综合属性 
	 * @param sign 增减标识 (1:加, -1:减)
	 */
	private Map<Integer, Integer> getWingAddProMap(PlayerWing playerWing, BaseWing baseWing, int sign) {		
		BaseWingUpStar baseWingUpStar = null;
		if(playerWing.getStar() > 0){
			baseWingUpStar = baseWing.getUpStarMap().get(playerWing.getStar());
		}
		
		List<List<Integer>> basePropertyList = baseWing.getBasePropertyList();
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		for(int i = 0; i < basePropertyList.size(); i++){
			List<Integer> lists = basePropertyList.get(i);
			int propertyId = lists.get(0);
			int propertyValue = lists.get(1);
			
			int addProValue = 0;
			if(baseWingUpStar != null){
				addProValue = propertyValue * sign + (int)(propertyValue * baseWingUpStar.getAddAttr() * 0.0001) * sign;
			}else{
				addProValue = propertyValue * sign;
			}
			addProMap.put(propertyId, addProValue);							
		}
		return addProMap;
	}

	/**
	 * 组合翅膀属性
	 * @throws JSONException 
	 */
	private Map<Integer, Integer> dealWingProValue(Map<Integer, Integer> addProMap1, Map<Integer, Integer> addProMap2) throws JSONException{	
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();		
		for(Map.Entry<Integer, Integer> entry : addProMap1.entrySet()){		
			int value;
			if(addProMap.containsKey(entry.getKey())){				
				value = addProMap.get(entry.getKey()) + entry.getValue();			
			}else{
				value = entry.getValue();
			}			
			addProMap.put(entry.getKey(), value);
		}			

		for(Map.Entry<Integer, Integer> entry : addProMap2.entrySet()){		
			int value; 
			if(addProMap.containsKey(entry.getKey())){				
				value = addProMap.get(entry.getKey()) + entry.getValue();			
			}else{
				value =entry.getValue();
			}			
			addProMap.put(entry.getKey(), value);
		}			
		return addProMap;
	}

	@Override
	public void unEvolve(long playerId, int wingId) throws Exception {
		if(playerId <= 0 || wingId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WING)) {
			
			BaseWing baseWing = this.getBaseWing(wingId);
			if(baseWing == null) throw new GameException(ExceptionConstant.WING_3103);
			
			PlayerWing playerWing = this.getPlayerWingMap(playerId).get(wingId);					
			if(playerWing == null) throw new GameException(ExceptionConstant.WING_3102);
			if(playerWing.getStar() < 1) throw new GameException(ExceptionConstant.WING_3105);	
					
			// 如果装备在身上，则改变属性值
			if(playerWing.getDressFlag() == 1){
				// 改变玩家属性	
				List<List<Integer>> basePropertyList = baseWing.getBasePropertyList();
				Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
				for(int i = 0; i < basePropertyList.size(); i++){
					List<Integer> lists = basePropertyList.get(i);
					int propertyId = lists.get(0);
					int propertyValue = lists.get(1);
					
					BaseWingUpStar baseWingUpStar = baseWing.getUpStarMap().get(playerWing.getStar());
					int addProValue = (int)(propertyValue * baseWingUpStar.getAddAttr() * 0.0001);
					addProMap.put(propertyId, -addProValue);							
				}
				
				//改变玩家属性值
				serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);
			}
			// 羽灵值		
			int UNEVOLVE_PERCENT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.UNEVOLVE_PERCENT);
			
			int totalValue = baseWing.getUpStarMap().get(playerWing.getStar()).getNeedExp() + playerWing.getWingValue();
			// 满星则不附加羽翼本身的羽灵值
			if(playerWing.getStar() == (baseWing.getUpStarMap().size() - 1)){
				totalValue =  baseWing.getUpStarMap().get(playerWing.getStar()).getNeedExp();
			}
			
			int wingValue = (int) Math.ceil(totalValue * UNEVOLVE_PERCENT * 0.01);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			playerWealth.setWingValue(playerWealth.getWingValue() + wingValue);
			playerService.updatePlayerWealth(playerWealth);
			
			playerWing.setStar(0);	
			playerWing.setWingValue(0);
			
			// 计算评分
			Map<Integer, Integer> proMap = this.getWingAddProMap(playerWing, baseWing, 1);	
			int wingScore = equipmentService.calculateScore(proMap);				
			playerWing.setWingScore(wingScore);
			this.updatePlayerWing(playerWing);	
			
			S_UnEvolve.Builder builder = S_UnEvolve.newBuilder();			
			builder.setWingMsg(protoBuilderService.buildWingMsg(playerWing));	
			builder.setTotalWingValue(playerWealth.getWingValue());
			MessageObj msg = new MessageObj(MessageID.S_UnEvolve_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}			
	}	
}
