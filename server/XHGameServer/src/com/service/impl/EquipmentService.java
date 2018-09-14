package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.constant.TaskConstant;
import com.constant.WakanConstant;
import com.dao.bag.BaseEquipmentDAO;
import com.dao.bag.PlayerEquipmentDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.AddAttr;
import com.domain.bag.BaseEquipAddAttr;
import com.domain.bag.BaseEquipInherit;
import com.domain.bag.BaseEquipStrong;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.base.BaseNewRole;
import com.domain.battle.BasePkDrop;
import com.domain.player.Player;
import com.domain.player.PlayerProperty;
import com.domain.puppet.PlayerPuppet;
import com.message.BagProto.S_SynBagItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBagService;
import com.service.ICommonService;
import com.service.IEpigraphService;
import com.service.IEquipmentService;
import com.service.IPlayerService;
import com.service.IPropertyService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ITaskService;
import com.service.IWakanService;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.SplitStringUtil;

/**
 * 装备系统实现
 * @author ken
 * @date 2017-1-4
 */
public class EquipmentService implements IEquipmentService {

	private BaseEquipmentDAO baseEquipmentDAO = new BaseEquipmentDAO();
	private PlayerEquipmentDAO playerEquipmentDAO =  new PlayerEquipmentDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseEquipment> baseEquipmentMap = new HashMap<Integer, BaseEquipment>();
		List<BaseEquipment> equipments =baseEquipmentDAO.listBaseEquipments();
		for(BaseEquipment model : equipments){
			model.setBasePropertyList(SplitStringUtil.getIntIntList(model.getBaseProperty()));
			model.setComposeCostList(SplitStringUtil.getRewardInfo(model.getComposeCost()));
			
			baseEquipmentMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_EQUIPMENT, baseEquipmentMap);	
		
		Map<Integer, BasePkDrop> basePkDropMap = new HashMap<Integer, BasePkDrop>();
		List<BasePkDrop> drops = baseEquipmentDAO.listBasePkDrops();
		for(BasePkDrop model : drops){
			basePkDropMap.put(model.getPosId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_PKDROP, basePkDropMap);
		
		
		Map<Integer, List<AddAttr>> baseEquipAddAttrMap = new HashMap<Integer, List<AddAttr>>();
		List<BaseEquipAddAttr> baseEquipAddAttrs = baseEquipmentDAO.listBaseEquipAddAttr();
		for(BaseEquipAddAttr model : baseEquipAddAttrs){
			model.setAddAttrList(SplitStringUtil.getAddAttrInfo(model.getAddAttr()));
			baseEquipAddAttrMap.put(model.getId(), model.getAddAttrList());
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_EQUIP_ADD_ATTR, baseEquipAddAttrMap);
		
		Map<Integer, Map<Integer, BaseEquipStrong>> strongMap = new HashMap<Integer, Map<Integer,BaseEquipStrong>>();
		List<BaseEquipStrong> strongs = baseEquipmentDAO.listBaseEquipStrongs();
		for(BaseEquipStrong model : strongs){
			model.setStrongCostList(SplitStringUtil.getRewardInfo(model.getStrongCost()));
			
			Map<Integer, BaseEquipStrong> map = strongMap.get(model.getEquipType());
			if(map == null){
				map = new HashMap<Integer, BaseEquipStrong>();
				strongMap.put(model.getEquipType(), map);
			}
			map.put(model.getStrongLv(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_EQUIP_STRONG, strongMap);
		
		Map<Integer, Map<Integer, BaseEquipInherit>> inheritMap = new HashMap<Integer, Map<Integer,BaseEquipInherit>>();
		List<BaseEquipInherit> inherits = baseEquipmentDAO.listBaseEquipInherits();
		for(BaseEquipInherit model : inherits){
			model.setInheritCostList(SplitStringUtil.getRewardInfo(model.getInheritCost()));
			
			Map<Integer, BaseEquipInherit> map = inheritMap.get(model.getRare());
			if(map == null){
				map = new HashMap<Integer, BaseEquipInherit>();
				inheritMap.put(model.getRare(), map);
			}
			map.put(model.getStrongLv(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_EQUIP_INHERIT, inheritMap);
	}

	@Override
	public void initCache() {
		CacheService.putToCache(CacheConstant.EQUIPMENT_DROP, new ConcurrentHashMap<Integer, PlayerEquipment>());
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<Long, PlayerEquipment> getDropEquipmentMap(){
		return  (Map<Long, PlayerEquipment>)CacheService.getFromCache(CacheConstant.EQUIPMENT_DROP);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseEquipment getBaseEquipmentById(int equipmentId) {
		Map<Integer, BaseEquipment> baseEquipmentMap = (Map<Integer, BaseEquipment>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_EQUIPMENT);
		return baseEquipmentMap.get(equipmentId);
	}	

	/**
	 * 获取强化配置
	 */
	@SuppressWarnings("unchecked")
	private BaseEquipStrong getBaseEquipStrong(int equipType, int strongLv){
		Map<Integer, Map<Integer, BaseEquipStrong>> strongMap = (Map<Integer, Map<Integer, BaseEquipStrong>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_EQUIP_STRONG);
		Map<Integer, BaseEquipStrong> map = strongMap.get(equipType);
		if(map == null) return null;
		
		return map.get(strongLv);
	}
	
	/**
	 * 获取传承配置
	 */
	@SuppressWarnings("unchecked")
	private BaseEquipInherit getBaseEquipInherit(int rare, int strongLv){
		Map<Integer, Map<Integer, BaseEquipInherit>> inheritMap = (Map<Integer, Map<Integer, BaseEquipInherit>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_EQUIP_INHERIT);
		Map<Integer, BaseEquipInherit> map = inheritMap.get(rare);
		if(map == null) return null;
		
		return map.get(strongLv);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int getPkDropRate(int posId, int nameColor) {
		Map<Integer, BasePkDrop> basePkDropMap = (Map<Integer, BasePkDrop>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_PKDROP);
		BasePkDrop base = basePkDropMap.get(posId);
		if(nameColor == 3){
			return base.getRedDropRate();
		}
		return base.getDropRate();
	}
	
	@Override
	public PlayerEquipment getPlayerEquipmentByID(long playerId, long playerEquipmentID) {
		List<PlayerEquipment> playerEquipmentList = this.getPlayerEquipmentList(playerId);
		for(PlayerEquipment pe : playerEquipmentList){
			if(pe.getPlayerEquipmentId() == playerEquipmentID){
				return pe;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerEquipment> getPlayerEquipmentList(long playerId) {
		List<PlayerEquipment> playerEquipmentList = (List<PlayerEquipment>)CacheService.getFromCache(CacheConstant.PLAYER_EQUIPMENT+playerId);
		if(playerEquipmentList == null){
			playerEquipmentList = playerEquipmentDAO.getPlayerEquipmentList(playerId);
			CacheService.putToCache(CacheConstant.PLAYER_EQUIPMENT+playerId, Collections.synchronizedList(playerEquipmentList));
		}
		return playerEquipmentList;
	}

	@Override
	public void putOnEquipment(long playerId, long onEquipmentID) throws Exception {
		if(playerId < 1 || onEquipmentID < 1)throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EQUIPMENT)) {

			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			Player player = playerService.getPlayerByID(playerId);
			PlayerEquipment changeEquipment = this.getPlayerEquipmentByID(playerId, onEquipmentID);
			
			if(changeEquipment == null){
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}

			if(changeEquipment.getPlayerId() != playerId){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			if(changeEquipment.getState() != ItemConstant.STATE_BACKPACK){
				throw new GameException(ExceptionConstant.EQUIP_1401);
			}
			BaseEquipment equipment = this.getBaseEquipmentById(changeEquipment.getEquipmentId());
			
			if(equipment.getNeedJob() != 0 && equipment.getNeedJob() != player.getCareer()){
				throw new GameException(ExceptionConstant.EQUIP_1402);
			}

			if(equipment.getLevel() > playerProperty.getLevel()){
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}

			List<PlayerEquipment> mountedList = this.getPlayerEquipmentListByState(playerId, ItemConstant.EQUIP_STATE_DRESS);
			for(PlayerEquipment pe : mountedList){
				if(pe.getEquipType() == equipment.getEquipType()){
					this.changeEquipment(playerId, onEquipmentID);
					return;
				}
			}			

			//穿装备
			PlayerBag playerBag = bagService.getPlayerBagForEquipment(playerId, changeEquipment.getPlayerEquipmentId());

			playerBag.reset();
			bagService.updatePlayerBag(playerBag);

			//更新玩家装备
			changeEquipment.setState(ItemConstant.EQUIP_STATE_DRESS);
			this.updatePlayerEquipment(changeEquipment);

			IWakanService wakanService =  GameContext.getInstance().getServiceCollection().getWakanService();
			wakanService.changeProValueByEquipment(playerId, changeEquipment.getEquipType(), 1);
			
			//处理穿装备属性 
			this.handleInfoWhenChangeEquipment(changeEquipment, 1, true);
			
			//更换主武器外形
			if(changeEquipment.getEquipType() == WakanConstant.WEAPON01){
				if(equipment.getWeaponStyle() > 0){
					PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
					if(playerPuppet != null){
						playerPuppet.setWeaponStyle(equipment.getId());				
						playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.WEAPON_STYLE, equipment.getId());						
					}
					//同步队伍信息
					serviceCollection.getTeamService().synTeam(playerId);
				}
			}
			
			// 任务处理
			ITaskService taskService = GameContext.getInstance().getServiceCollection().getTaskService();
			List<Integer> conditionList = new ArrayList<Integer>();				
			conditionList.add(changeEquipment.getEquipType());
			taskService.executeTask(playerId, TaskConstant.TYPE_5, conditionList);
			
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(changeEquipment));
			builder.setTigTag(1);
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			
		}
	}

	@Override
	public void putDownEquipment(long playerId, long downEquipmentID) throws Exception {
		if(playerId < 1 || downEquipmentID < 1)throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IEpigraphService epigraphService = serviceCollection.getEpigraphService();
		IBagService bagService = serviceCollection.getBagService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EQUIPMENT)) {

			PlayerEquipment downEquipment = this.getPlayerEquipmentByID(playerId, downEquipmentID);
			if(downEquipment == null){
				
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}
			if(downEquipment.getPlayerId() != playerId){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			if(downEquipment.getState() != ItemConstant.EQUIP_STATE_DRESS){
				throw new GameException(ExceptionConstant.EQUIP_1401);
			}

			BaseEquipment equipment = this.getBaseEquipmentById(downEquipment.getEquipmentId());
			if(equipment == null) throw new GameException(ExceptionConstant.EQUIP_1400);
			
			//验证背包是否满
			Integer itemIndex =bagService.getNewItemIndexByPlayerId(playerId);
			if(itemIndex == null){
				throw new GameException(ExceptionConstant.BAG_1304);
			}

			// 脱装备
			PlayerBag playerBag = bagService.createPlayerBag(playerId, downEquipment.getPlayerEquipmentId(), ItemConstant.GOODS_TYPE_EQUPMENT, downEquipment.getIsBinding(), 1, itemIndex);
			
			//更新玩家船装备
			downEquipment.setState(ItemConstant.EQUIP_STATE_BACKPACK);
			this.updatePlayerEquipment(downEquipment);
			
			// 处理玩家装备位灵力值
			IWakanService wakanService =  serviceCollection.getWakanService();
			wakanService.changeProValueByEquipment(playerId, downEquipment.getEquipType(), -1);
			
			if(downEquipment.getEquipType() == WakanConstant.WEAPON01){
				//更换主武器外形
				if(equipment.getWeaponStyle() > 0){
					IPlayerService playerService = serviceCollection.getPlayerService();
					ICommonService commonService = serviceCollection.getCommonService();
					Player player = playerService.getPlayerByID(playerId);
					
					BaseNewRole baseNewRole = commonService.getBaseNewRole(player.getCareer());
					
					PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
					if(playerPuppet != null){
						playerPuppet.setWeaponStyle(baseNewRole.getWeaponStyle());
						playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.WEAPON_STYLE, baseNewRole.getWeaponStyle());	
					}
					//同步队伍信息
					serviceCollection.getTeamService().synTeam(playerId);
				}
				
				//更改玩家技能铭文信息
				epigraphService.clearPlayerWeaponEffect(playerId);
			}
			
			//处理脱装备副属性 
			this.handleInfoWhenChangeEquipment(downEquipment, -1, true);
			
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(downEquipment));
			builder.setTigTag(1);
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			
		}
	}
	
	/**
	 * 切换装备
	 */
	private void changeEquipment(long playerId, long onEquipmentID) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		IBagService bagService = serviceCollection.getBagService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EQUIPMENT)) {

			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			Player player = playerService.getPlayerByID(playerId);
			PlayerEquipment changeEquipment = this.getPlayerEquipmentByID(playerId, onEquipmentID);
			
			if(changeEquipment == null){
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}

			if(changeEquipment.getPlayerId() != playerId){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			if(changeEquipment.getState() != ItemConstant.STATE_BACKPACK){
				throw new GameException(ExceptionConstant.EQUIP_1401);
			}
			BaseEquipment equipment = this.getBaseEquipmentById(changeEquipment.getEquipmentId());
			
			if(equipment.getNeedJob() != 0 && equipment.getNeedJob() != player.getCareer()){
				throw new GameException(ExceptionConstant.EQUIP_1402);
			}

			if(equipment.getLevel() > playerProperty.getLevel()){
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}

			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			
			//穿装备
			PlayerBag playerBag = bagService.getPlayerBagForEquipment(playerId, changeEquipment.getPlayerEquipmentId());
			
	
			boolean bFind = false;
			List<PlayerEquipment> mountedList = this.getPlayerEquipmentListByState(playerId, ItemConstant.EQUIP_STATE_DRESS);
			for(PlayerEquipment pe : mountedList){
				if(pe.getEquipType() == equipment.getEquipType()){
					// 脱装备
					playerBag.setIsBinding(pe.getIsBinding());
					playerBag.setItemId(pe.getPlayerEquipmentId());
					bagService.updatePlayerBag(playerBag);
					
					//更新玩家装备
					pe.setState(ItemConstant.EQUIP_STATE_BACKPACK);
					this.updatePlayerEquipment(pe);
					
					//处理脱装备玩家铭文信息
					if(pe.getEquipType() == WakanConstant.WEAPON01){
						serviceCollection.getEpigraphService().clearPlayerWeaponEffect(playerId);
					}
					
					//处理脱装备副属性 
					this.handleInfoWhenChangeEquipment(pe, -1, false);
					
					bFind = true;
					builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(pe));
					break;
				}
			}

			if(!bFind)throw new GameException(ExceptionConstant.EQUIP_1401); 
			
			//更新玩家装备
			changeEquipment.setState(ItemConstant.EQUIP_STATE_DRESS);
			this.updatePlayerEquipment(changeEquipment);

			//处理穿装备属性 
			this.handleInfoWhenChangeEquipment(changeEquipment, 1, true);
			
			//更换主武器外形
			if(changeEquipment.getEquipType() == WakanConstant.WEAPON01){
				if(equipment.getWeaponStyle() > 0){
					PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
					if(playerPuppet != null){
						playerPuppet.setWeaponStyle(equipment.getId());
						playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.WEAPON_STYLE, equipment.getId());					
					}
					//同步队伍信息
					serviceCollection.getTeamService().synTeam(playerId);
				}
			}
			
			builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(changeEquipment));
			builder.setTigTag(1);
			
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();

			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			
		}
	}
	
	
	/**
	 * 处理装备对玩家的属性变化
	 * @throws JSONException 
	 */
	public Map<Integer, Integer> handleInfoWhenChangeEquipment(PlayerEquipment playerEquipment, int sign, boolean offerBattleValue){
		IPropertyService propertyService = GameContext.getInstance().getServiceCollection().getPropertyService();
		Map<Integer, Integer> addProMap = this.dealEquipmentProValue(playerEquipment, sign);
	
		propertyService.addProValue(playerEquipment.getPlayerId(), addProMap, true, offerBattleValue);
		
		return addProMap;
	}
	
	/**
	 * 组合装备属性
	 */
	private Map<Integer, Integer> dealEquipmentProValue(PlayerEquipment playerEquipment, int sign){	
		BaseEquipment baseEquipment = this.getBaseEquipmentById(playerEquipment.getEquipmentId());
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		
		// 装备基础属性
		List<List<Integer>> attrList =  baseEquipment.getBasePropertyList();
		if (attrList != null){
			
			//强化属性加成
			double strongPer = 1;
			if(playerEquipment.getStrongLv() > 0){
				BaseEquipStrong baseEquipStrong = this.getBaseEquipStrong(playerEquipment.getEquipType(), playerEquipment.getStrongLv());
				strongPer += baseEquipStrong.getStrongPer() * 0.01;
			}
			
			for(List<Integer> addAttr : attrList){		
				int proId = addAttr.get(0);
				int proValue = (int)(addAttr.get(1) * strongPer *sign);
				
				if(addProMap.containsKey(proId)){				
					proValue += addProMap.get(proId);			
				}		
				addProMap.put(proId, proValue);
			}	
		}	
		
		// 装备附加属性
		if(baseEquipment.getAddAttrNum() > 0) {
			for(Map.Entry<Integer, Integer> entry : playerEquipment.getAddAttrMap().entrySet()){
				int proId = entry.getKey();
				int proValue = entry.getValue()*sign; 
				
				if(addProMap.containsKey(proId)){				
					proValue += addProMap.get(proId);			
				}
				
				addProMap.put(proId, proValue);
			}
		}	
		
		return addProMap;
	}	

	@Override
	public void updatePlayerEquipment(PlayerEquipment playerEquipment) {
		Set<GameEntity> updateCacheList = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_EQUIPMENT);
		
		if (!updateCacheList.contains(playerEquipment)) {
			updateCacheList.add(playerEquipment);
		}
	}

	@Override
	public PlayerEquipment deletePlayerEquipmentByID(long playerId, long playerEquipmentId) {
		List<PlayerEquipment> playerEquipmentList = this.getPlayerEquipmentList(playerId);
		PlayerEquipment playerEquipment = null;
		for(PlayerEquipment pe : playerEquipmentList){
			if(pe.getPlayerEquipmentId() == playerEquipmentId){
				playerEquipment = pe;				
				break;
			}
		}
		if(playerEquipment != null){
			playerEquipment.setState(ItemConstant.EQUIP_STATE_DELETE);
			playerEquipment.setDeleteTime(DateService.getCurrentUtilDate());
			playerEquipmentList.remove(playerEquipment);
			this.updatePlayerEquipment(playerEquipment);
		}
		return playerEquipment;
	}

	@Override
	public PlayerEquipment createPlayerEquipment(long playerId,
			Integer equipmentID, int isBinding) {

		BaseEquipment equipment = this.getBaseEquipmentById(equipmentID);
		if(equipment == null)throw new GameException(ExceptionConstant.EQUIP_1400);
		
		List<PlayerEquipment> playerEquipmentList = this.getPlayerEquipmentList(playerId);
		
		PlayerEquipment playerEquipment = new PlayerEquipment();
		playerEquipment.setPlayerEquipmentId(IDUtil.geneteId(PlayerEquipment.class));
		playerEquipment.setPlayerId(playerId);
		playerEquipment.setEquipmentId(equipment.getId());
		playerEquipment.setEquipType(equipment.getEquipType());
		playerEquipment.setIsBinding(isBinding);
		playerEquipment.setState(ItemConstant.STATE_BACKPACK);
		playerEquipment.setHoleNum(Math.max(Math.min(this.openHoleNum(), equipment.getMaxHole()),  equipment.getMinHole()));			
		
		// 生成装备附加属性	
		playerEquipment.setAddAttrMap(this.randomAddAttr(equipment));
		
		this.calculateEquipmentScore(playerEquipment);
		playerEquipmentDAO.createPlayerEquipment(playerEquipment);
		
		playerEquipmentList.add(playerEquipment);
		
		return playerEquipment;
	}

	@Override
	public List<PlayerEquipment> getPlayerEquipmentListByState(
			long playerId, int state) {
		List<PlayerEquipment> playerEquipments = this.getPlayerEquipmentList(playerId);
		List<PlayerEquipment> returnList = new ArrayList<PlayerEquipment>();
		for(PlayerEquipment pe : playerEquipments){
			if(pe.getState() == state){
				returnList.add(pe);
			}
		}
		return returnList;
	}

	@Override
	public void deletePlayerEquipmentCacheInfo(long playerId) {

		CacheService.deleteFromCache(CacheConstant.PLAYER_EQUIPMENT+playerId);
	}

	@Override
	public void quartzDeletePlayerEquipment() {

		playerEquipmentDAO.quartzDeletePlayerEquipment();
	}
	
	
	@Override
	public void addDropEquipmentCache(PlayerEquipment playerEquipment) {
		this.getDropEquipmentMap().put(playerEquipment.getPlayerEquipmentId(), playerEquipment);
	}

	@Override
	public PlayerEquipment removeDropEquipmentCache(long playerEquipmentId) {
		return this.getDropEquipmentMap().remove(playerEquipmentId);
	}

	@Override
	public void deleteDropEquipment(long playerEquipmentId) {
		PlayerEquipment playerEquipment = this.removeDropEquipmentCache(playerEquipmentId);
		if(playerEquipment != null){
			playerEquipment.setState(ItemConstant.EQUIP_STATE_DELETE);
			playerEquipment.setDeleteTime(DateService.getCurrentUtilDate());
			playerEquipment.setPlayerId(0);
			this.updatePlayerEquipment(playerEquipment);
		}
	}

	/**
	 * 根据开孔几率，算出开孔数量
	 */
	private int openHoleNum(){
		Map<Integer, Integer> openHoleNumRandMap = new HashMap<Integer, Integer>();
		openHoleNumRandMap.put(0, 0);
		openHoleNumRandMap.put(1, 1);
		openHoleNumRandMap.put(2, 1);
		openHoleNumRandMap.put(3, 1);
	
		int m = 0;
		int n = RandomService.getRandomNum(3);		
		for(int i = 0; i < openHoleNumRandMap.size(); i++){				
			if(n< m + openHoleNumRandMap.get(i)){
				return  i;
			}
			 m += openHoleNumRandMap.get(i);  
		}	
		return 0;
	}	
	
	@Override
	public boolean isTakeEquipment(long playerId, int posId){
		List<PlayerEquipment> listEquipments = this.getPlayerEquipmentListByState(playerId, ItemConstant.EQUIP_STATE_DRESS);
		for(PlayerEquipment playerEquipment : listEquipments){
			if(playerEquipment.getEquipType() == posId){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 生成装备附加属性	
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> randomAddAttr(BaseEquipment baseEquipment){	
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		if(baseEquipment.getAddAttrNum() < 1) return map;
		
		// 属性ID
		Map<Integer, List<AddAttr>> baseEquipAddAttrMap = (Map<Integer, List<AddAttr>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_EQUIP_ADD_ATTR);
		List<Integer> attrIds = new ArrayList<>();
		List<AddAttr> list = baseEquipAddAttrMap.get(baseEquipment.getAddAttrLevel());
		for(AddAttr addAttr : list){
			attrIds.add(addAttr.getAttrId());
		}
		
		List<Integer> copyIds = new ArrayList<Integer>(attrIds);
		for(int i = 0; i < baseEquipment.getAddAttrNum(); i++){
			int index = RandomService.getRandomNum(copyIds.size());
			Integer attrId = copyIds.get(index);
			copyIds.remove(attrId);
			
			// 储存最后的装备附加属性信息
			AddAttr addAttr = this.getAddAttrByID(list, attrId);
			int attrValue = RandomService.getRandomNum(addAttr.getMinAttrValue(), addAttr.getMaxAttrValue());
			if(attrId == ProdefineConstant.DMG_DEEP_PER_PANEL || 
					attrId ==  ProdefineConstant.DMG_REDUCT_PER_PANEL || 
					attrId == ProdefineConstant.DMG_CRIT_PER_PANEL){
				attrValue =  (int)(Math.round(attrValue * 1.0 /10) * 10);
			}
			
			if(attrValue <= 0) continue;	
			
			map.put(attrId, attrValue);
		}	
		copyIds = null;
		return map;		
	}	
	

	/**
	 * 根据属性ID， 找addAttr
	 */
	private AddAttr getAddAttrByID(List<AddAttr> list, Integer attrId) {
		for(AddAttr addAttr : list){
			if (attrId.equals(addAttr.getAttrId())){
				return addAttr;
			}
		}
		return null;
	}

	/**
	 * 计算装备评分
	 */
	private void calculateEquipmentScore(PlayerEquipment playerEquipment){	
		Map<Integer, Integer> addProMap = this.dealEquipmentProValue(playerEquipment, 1);
		int score =  this.calculateScore(addProMap);
		playerEquipment.setScore(score);
	}

	/**
	 *  根据属性算评分（装备和评分）
	 */
	public int calculateScore(Map<Integer, Integer> addProMap) {
		double score = 0;
		if (addProMap != null){
			for(Map.Entry<Integer, Integer> entry : addProMap.entrySet()){
				int propertyId = entry.getKey();
				int propertyValue = entry.getValue();
				
				switch (propertyId) {
				case ProdefineConstant.STRENGTH:
					score += propertyValue * 14;
					break;
				case ProdefineConstant.INTELLIGENCE:
					score += propertyValue * 12;
					break;
				case ProdefineConstant.ENDURANCE:
					score += propertyValue * 20;					
					break;
				case ProdefineConstant.SPIRIT:
					score += propertyValue * 15;						
					break;
				case ProdefineConstant.LUCKY:
					score += propertyValue * 20;		
					break;					
				case ProdefineConstant.HP_MAX_PANEL:
					score += propertyValue;	
					break;
				case ProdefineConstant.MP_MAX_PANEL:
					score += propertyValue;	
					break;
				case ProdefineConstant.P_ATTACK_PANEL:
					score += propertyValue * 10;	
					break;
				case ProdefineConstant.M_ATTACK_PANEL:
					score += propertyValue * 10;
					break;
				case ProdefineConstant.P_DAMAGE_PANEL:
					score += propertyValue * 10;
					break;
				case ProdefineConstant.M_DAMAGE_PANEL:
					score += propertyValue * 10;
					break;
				case ProdefineConstant.CRIT_PANEL:
					score += propertyValue * 10;
					break;
				case ProdefineConstant.TOUGH_PANEL:
					score += propertyValue * 10;
					break;
				case ProdefineConstant.DMG_DEEP_PER_PANEL:
					score += propertyValue;
					break;
				case ProdefineConstant.DMG_REDUCT_PER_PANEL:
					score += propertyValue;
					break;
				case ProdefineConstant.DMG_CRIT_PER_PANEL:
					score += propertyValue * 0.5;
					break;				
				default:
					break;
				}
			}	
		}			
		
		return (int)(score);
	}

	@Override
	public int strongEquip(long playerId, long playerEquipmentId, int luckItem) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EQUIPMENT)) {
			PlayerEquipment playerEquipment = this.getPlayerEquipmentByID(playerId, playerEquipmentId);
			if(playerEquipment == null){
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}
			
			BaseEquipment baseEquipment = this.getBaseEquipmentById(playerEquipment.getEquipmentId());
			if(baseEquipment == null) throw new GameException(ExceptionConstant.EQUIP_1400);
			
			BaseEquipStrong baseStrong = this.getBaseEquipStrong(playerEquipment.getEquipType(), playerEquipment.getStrongLv() + 1);
			if(baseStrong == null){
				throw new GameException(ExceptionConstant.EQUIP_1407);
			}
			
			List<Reward> list = baseStrong.getStrongCostList();
			
			int rate = baseStrong.getStrongRate();
			if(luckItem > 0){
				int EQUIP_LUCK_ITEM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.EQUIP_LUCK_ITEM);
				
				BaseItem baseItem = bagService.getBaseItemById(EQUIP_LUCK_ITEM);
				rate += baseItem.getEffectValue();
				
				Reward reward = new Reward(2, EQUIP_LUCK_ITEM, 1, 0, 0);
				list.add(reward);
			}
			
			rewardService.expendJudgment(playerId, list, true, InOutLogConstant.DIAMOND_OF_0);
			
			int result = 0;
			if(RandomService.getRandomNum(100) < rate){
				//强化成功
				result = 1;
				
				if(playerEquipment.getState() == ItemConstant.EQUIP_STATE_DRESS){
					this.handleInfoWhenChangeEquipment(playerEquipment, -1, false);
					
					playerEquipment.setStrongLv(playerEquipment.getStrongLv() + 1);
					
					Map<Integer, Integer> addProMap = this.handleInfoWhenChangeEquipment(playerEquipment, 1, true);
					
					playerEquipment.setScore(this.calculateScore(addProMap));
				}else{
					playerEquipment.setStrongLv(playerEquipment.getStrongLv() + 1);
					this.calculateEquipmentScore(playerEquipment);
				}

				this.updatePlayerEquipment(playerEquipment);
				
				S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
				builder.addListPlayerEquipments(serviceCollection.getProtoBuilderService().buildPlayerEquipmentMsg(playerEquipment));
				builder.setTigTag(1);
				MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
			}
			
			return result;
		}
		
	}

	@Override
	public void composeEquip(long playerId, List<Long> playerEquipmentIds) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		IRewardService rewardService = serviceCollection.getRewardService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EQUIPMENT)) {
			if(playerEquipmentIds.size() < 3){
				throw new GameException(ExceptionConstant.EQUIP_1411);
			}
			
			int equipId = 0;
			for(Long playerEquipId : playerEquipmentIds){
				PlayerEquipment playerEquipment = this.getPlayerEquipmentByID(playerId, playerEquipId);
				if(playerEquipment == null){
					throw new GameException(ExceptionConstant.EQUIP_1400);
				}
				
				if(equipId == 0){
					equipId = playerEquipment.getEquipmentId();
					continue;
				}
				
				if(equipId != playerEquipment.getEquipmentId()){
					throw new GameException(ExceptionConstant.EQUIP_1412);
				}
				
			}
			
			BaseEquipment baseEquipment = this.getBaseEquipmentById(equipId);
			if(baseEquipment == null) throw new GameException(ExceptionConstant.EQUIP_1400);
			
			BaseEquipment newEquipment = this.getBaseEquipmentById(baseEquipment.getComposeEquip());
			if(newEquipment == null){
				LogUtil.error("composeEquip  BaseEquipment is null with id is "+equipId+" composeEquipId is " + baseEquipment.getComposeEquip());
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}
			
			rewardService.expendJudgment(playerId, baseEquipment.getComposeCostList(), true, InOutLogConstant.DIAMOND_OF_0);
			
			int blind = 0;
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			for(Long playerEquipId : playerEquipmentIds){
				PlayerBag playerBag = bagService.getPlayerBagForEquipment(playerId, playerEquipId);
				if(playerBag == null) continue;
				
				if(blind == 0){
					blind = playerBag.getIsBinding();
				}
				
				PlayerEquipment equipment = this.deletePlayerEquipmentByID(playerBag.getPlayerId(), playerBag.getItemId());
				if (equipment != null) {
					
					builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(equipment));
				}
				
				playerBag.setState(0);
				playerBag.setNum(0);
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
				
				playerBag.reset();
				bagService.updatePlayerBag(playerBag);
			}
			
			// 合成新的装备
			Integer itemIndex = bagService.getNewItemIndexByPlayerId(playerId);
			if(itemIndex == null){
				throw new GameException(ExceptionConstant.BAG_1304);
			}
			
			PlayerEquipment playerEquipment = this.createPlayerEquipment(playerId, baseEquipment.getComposeEquip(), blind);
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
			
			PlayerBag playerBag = bagService.createPlayerBag(playerId, playerEquipment.getPlayerEquipmentId(), ItemConstant.GOODS_TYPE_EQUPMENT, playerEquipment.getIsBinding(), 1, itemIndex);
			builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
			
		}
	}

	@Override
	public void inheritEquip(long playerId, long playerEquipmentId, long targetEquipmentId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IRewardService rewardService = serviceCollection.getRewardService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EQUIPMENT)) {
			PlayerEquipment playerEquipment = this.getPlayerEquipmentByID(playerId, playerEquipmentId);
			if(playerEquipment == null){
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}
			
			BaseEquipment baseEquipment = this.getBaseEquipmentById(playerEquipment.getEquipmentId());
			if(baseEquipment == null) throw new GameException(ExceptionConstant.EQUIP_1400);
			
			if(playerEquipment.getStrongLv() < 1){
				throw new GameException(ExceptionConstant.EQUIP_1408);
			}
			
			PlayerEquipment targetEquipment = this.getPlayerEquipmentByID(playerId, targetEquipmentId);
			if(targetEquipment == null){
				throw new GameException(ExceptionConstant.EQUIP_1400);
			}
			
			BaseEquipment baseTargetEquipment = this.getBaseEquipmentById(targetEquipment.getEquipmentId());
			if(baseTargetEquipment == null) throw new GameException(ExceptionConstant.EQUIP_1400);
			
			if(baseEquipment.getEquipType() != baseTargetEquipment.getEquipType()){
				throw new GameException(ExceptionConstant.EQUIP_1409);
			}
			
			if(playerEquipment.getStrongLv() < targetEquipment.getStrongLv()){
				throw new GameException(ExceptionConstant.EQUIP_1410);
			}
			
			BaseEquipInherit baseEquipInherit = this.getBaseEquipInherit(baseTargetEquipment.getRare(), playerEquipment.getStrongLv());
			if(baseEquipInherit == null) throw new GameException(ExceptionConstant.ERROR_10000);
			
			rewardService.expendJudgment(playerId, baseEquipInherit.getInheritCostList(), true, InOutLogConstant.DIAMOND_OF_0);
			
			//更新目标装备强化等级
			if(targetEquipment.getState() == ItemConstant.EQUIP_STATE_DRESS){
				this.handleInfoWhenChangeEquipment(targetEquipment, -1, false);
				
				targetEquipment.setStrongLv(playerEquipment.getStrongLv());
				
				Map<Integer, Integer> addProMap = this.handleInfoWhenChangeEquipment(targetEquipment, 1, true);
				
				targetEquipment.setScore(this.calculateScore(addProMap));
			}else{
				
				targetEquipment.setStrongLv(playerEquipment.getStrongLv());
				this.calculateEquipmentScore(targetEquipment);
			}
			this.updatePlayerEquipment(targetEquipment);
			
			//重置当前装备强化等级
			if(playerEquipment.getState() == ItemConstant.EQUIP_STATE_DRESS){
				this.handleInfoWhenChangeEquipment(playerEquipment, -1, false);
				
				playerEquipment.setStrongLv(0);
				
				Map<Integer, Integer> addProMap = this.handleInfoWhenChangeEquipment(playerEquipment, 1, true);
				
				playerEquipment.setScore(this.calculateScore(addProMap));
			}else{
				
				playerEquipment.setStrongLv(0);
				this.calculateEquipmentScore(playerEquipment);
			}
			this.updatePlayerEquipment(playerEquipment);
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(targetEquipment));
			builder.setTigTag(1);
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
			
		}		
	}
}
