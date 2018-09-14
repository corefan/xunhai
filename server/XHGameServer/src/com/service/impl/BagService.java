package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.constant.RewardTypeConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.dao.bag.BaseItemDAO;
import com.dao.bag.PlayerBagDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseGift;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;
import com.domain.bag.PlayerEquipment;
import com.domain.family.PlayerFamily;
import com.domain.fashion.PlayerFashion;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.PlayerPuppet;
import com.domain.team.Team;
import com.message.BagProto.S_SynBagItem;
import com.message.BagProto.S_UseItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SceneProto.S_TransferNotice;
import com.service.IBagService;
import com.service.IEquipmentService;
import com.service.IFamilyService;
import com.service.IFashionService;
import com.service.IGuildService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ITeamService;
import com.util.ComparatorUtil;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 背包系统实现
 * @author ken
 * @date 2017-1-4
 */
public class BagService implements IBagService {

	private BaseItemDAO baseItemDAO = new BaseItemDAO();
	private PlayerBagDAO playerBagDAO = new PlayerBagDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseItem> baseItemMap = new HashMap<Integer, BaseItem>();
		List<BaseItem> items = baseItemDAO.listBaseItems();				
		for(BaseItem model : items){
			model.setUseExpendList(SplitStringUtil.getRewardInfo(model.getUseExpend()));
			baseItemMap.put(model.getId(), model);			
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_ITEM, baseItemMap);	
		
		Map<Integer, BaseGift> baseGiftMap = new HashMap<Integer, BaseGift>();
		List<BaseGift> gifts = baseItemDAO.listBaseGifts();
		for(BaseGift model : gifts){
			List<List<Integer>> lists = SplitStringUtil.getIntIntList(model.getReward());
			for(List<Integer> l:lists){
				int career = l.get(0);
				int groupId = l.get(1);
				Reward reward = new Reward(l.get(2), l.get(3), l.get(4), l.get(6), l.get(5));
				
				Map<Integer, List<Reward>> map = model.getRewardMap().get(career);
				if(map == null){
					map = new HashMap<Integer, List<Reward>>();
					model.getRewardMap().put(career, map);
				}
				List<Reward> rewards = map.get(groupId);
				if(rewards == null){
					rewards = new ArrayList<Reward>();
					map.put(groupId, rewards);
				}
				rewards.add(reward);
			}
			
			baseGiftMap.put(model.getGiftId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_GIFT, baseGiftMap);	
	}

	/**
	 * 礼包配置
	 */
	@SuppressWarnings("unchecked")
	private BaseGift getBaseGiftById(int giftId){
		Map<Integer, BaseGift> baseGiftMap = (Map<Integer, BaseGift>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GIFT);
		return baseGiftMap.get(giftId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseItem getBaseItemById(Integer itemId) {
		Map<Integer, BaseItem> baseItemMap = (Map<Integer, BaseItem>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_ITEM);
		
		return baseItemMap.get(itemId);
	}

	@Override
	public List<PlayerBag> getPlayerBagListByPlayerID(long playerId)
			throws Exception {
		List<PlayerBag> playerBagList = this.getAllPlayerBagListByPlayerID(playerId);

		List<PlayerBag> pbList = new ArrayList<PlayerBag>();
		for (PlayerBag pb : playerBagList) {
			if (pb.getState() == ItemConstant.STATE_BACKPACK) {
				pbList.add(pb);
			}
		}
		return pbList;
	}

	@Override
	public void tidyBag(long playerId) throws Exception {
		if(playerId < 1)  throw new GameException(ExceptionConstant.ERROR_10000);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG)) {
			
			List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);
			
			if (playerBackpackList.isEmpty()) return;
			
			//叠加
			List<PlayerBag> copyPbList = playerBackpackList;
			List<List<PlayerBag>> needAddPbListList = new ArrayList<List<PlayerBag>>();
			Set<Long> needAddIDList = new HashSet<Long>();
			
			List<PlayerBag> samePbList = null;
			for (PlayerBag pb : playerBackpackList) {
				pb.setNumChanged(0);
				if (pb.getGoodsType() > ItemConstant.GOODS_TYPE_EQUPMENT) {
					
					BaseItem item = getBaseItemById((int)pb.getItemId());
					
					if(item.getPileNumber() > 1){
						//可叠加
						if (pb.getNum() < item.getPileNumber() && !needAddIDList.contains(pb.getPlayerBagId())) {
							samePbList = new ArrayList<PlayerBag>();
							samePbList.add(pb);
							for (PlayerBag copyPb : copyPbList) {
								if (copyPb.getIsBinding() == pb.getIsBinding()
										&& copyPb.getItemId() ==pb.getItemId()
										&& copyPb.getPlayerBagId() != pb.getPlayerBagId()
										&& copyPb.getNum() < item.getPileNumber()){
									// 需要叠加
									samePbList.add(copyPb);
									
									needAddIDList.add(pb.getPlayerBagId());
									needAddIDList.add(copyPb.getPlayerBagId());
								}
							}
							if (samePbList.size() > 1) {
								needAddPbListList.add(samePbList);
							}
							
						}
					}
		
				}
			}
			
			samePbList = null;
			
			if (needAddPbListList.size() > 0) {
				for (List<PlayerBag> pbList2 : needAddPbListList) {
					Integer samNum = 0;
					for (PlayerBag pb : pbList2) {
						samNum += pb.getNum(); 
					}
					for (PlayerBag pb : pbList2) {
						BaseItem item = getBaseItemById((int)pb.getItemId());
						pb.setNum(Math.min(samNum, item.getPileNumber()));
						samNum = Math.max(0, samNum - item.getPileNumber());
						pb.setNumChanged(1);
						
						if(pb.getNum() < 1){
							pb.reset();
						}
					}
				}
				
				needAddPbListList = null;
				needAddIDList = null;
			}
			
			//排序
			for (PlayerBag pb : playerBackpackList) {
				pb.setSortItemId(0);
				if (pb.getNum() > 0) {
					if (pb.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT) {
						PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, pb.getItemId());
						if(playerEquipment == null){
							LogUtil.error("tidyBag PlayerEquipment is null with playerEquipmentId is "+ pb.getItemId());
							continue;
						}
						pb.setSortItemId(playerEquipment.getEquipmentId());
					} else {
						pb.setSortItemId((int)pb.getItemId());
					}
				}
			}
			
			Collections.sort(playerBackpackList, new ComparatorUtil());

			
			Integer itemIndex = 1;
			// 是否有变化
			boolean allChanged = false;
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			
			for (PlayerBag pb : playerBackpackList) {
				boolean oneChanged = false;
				
				// 更新位置
				if (!pb.getItemIndex().equals(itemIndex)) {
					pb.setItemIndex(itemIndex);

					allChanged = true;
					oneChanged = true;
				}
				
				if(pb.getNumChanged() == 1){
					allChanged = true;
					oneChanged = true;
				}
				
				if (oneChanged) {
					this.updatePlayerBag(pb);
					
					builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(pb));
				}
				
				itemIndex++;
			}

			if (allChanged) {
				builder.setTigTag(1);
				MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			}
		}
		
	}
	
	@Override
	public void sellItem(long playerId, long playerBagId) throws Exception {
		if(playerId < 1 || playerBagId < 1) throw new GameException(ExceptionConstant.ERROR_10000);

		IEquipmentService equipmentService = GameContext.getInstance().getServiceCollection().getEquipmentService();
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		
		Integer sellMoney = 0;
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG)) {

			PlayerBag playerBag = this.getPlayerBagById(playerId,playerBagId);
			if (playerBag == null) {
				throw new GameException(ExceptionConstant.BAG_1303);
			}
			if (playerBag.getPlayerId() != playerId) {
				throw new GameException(ExceptionConstant.ERROR_10000);
			}

			// 道具
			if (playerBag.getGoodsType() > ItemConstant.GOODS_TYPE_EQUPMENT) {

				BaseItem item = this.getBaseItemById((int)playerBag.getItemId());
				
				if(item == null) throw new GameException(ExceptionConstant.BAG_1303);
				
				if (item.getSellPrice() <= 0) {
					throw new GameException(ExceptionConstant.BAG_1301);
				}

				sellMoney = item.getSellPrice() * playerBag.getNum();

			} else {
				//装备
				PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerBag.getItemId());
				if (playerEquipment == null) {
					throw new GameException(ExceptionConstant.EQUIP_1400);
				}
				if (playerEquipment.getState() != ItemConstant.STATE_BACKPACK) {
					throw new GameException(ExceptionConstant.EQUIP_1401);
				}
				sellMoney = equipmentService.getBaseEquipmentById(playerEquipment.getEquipmentId()).getSellPrice();
			}
			
			
			// 加金币 
			playerService.addGold_syn(playerId, sellMoney);
			
			// 删除物品
			this.removePlayerBag(playerBag);
		}
		
	}

	@Override
	public PlayerBag useItem(long playerId, long playerBagId, int num) throws Exception {
		if(playerId < 1 || playerBagId < 1 || num < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG)) {
			PlayerBag playerBag = this.getPlayerBagById(playerId,playerBagId);
			if (playerBag == null) {
				throw new GameException(ExceptionConstant.BAG_1303);
			}
			if (playerBag.getPlayerId() != playerId) {
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			if (playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT) {
				throw new GameException(ExceptionConstant.BAG_1305);
			}
			int itemId = (int)playerBag.getItemId();
			BaseItem baseItem = this.getBaseItemById(itemId);
			if(baseItem == null) throw new GameException(ExceptionConstant.BAG_1303);
			if (num <= 0 || num > baseItem.getPileNumber()) {
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			if (baseItem.getUseType() == 0) {
				throw new GameException(ExceptionConstant.BAG_1305);
			}		
			
			if (playerBag.getNum() < num) {
				throw new GameException(ExceptionConstant.BAG_1306);
			}
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if (baseItem.getLevel() > playerProperty.getLevel()) {
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}
			
			// 使用某些物品限制
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(baseItem.getTinyType() == ItemConstant.ITEM_USE_LIMIT_TYPE_1){
				int DRUG_USE_LIMIT_1= serviceCollection.getCommonService().getConfigValue(ConfigConstant.DRUG_USE_LIMIT_1); 
				if(playerDaily.getUseDrugOneItem() >= DRUG_USE_LIMIT_1)throw new GameException(ExceptionConstant.BAG_1312);
							
			}else if(baseItem.getTinyType() == ItemConstant.ITEM_USE_LIMIT_TYPE_2){
				int DRUG_USE_LIMIT_2= serviceCollection.getCommonService().getConfigValue(ConfigConstant.DRUG_USE_LIMIT_2); 
				if(playerDaily.getUseDrugTwoItem() >= DRUG_USE_LIMIT_2)throw new GameException(ExceptionConstant.BAG_1312);
				
			}
			
			// 使用物品消耗		
			if(baseItem.getUseExpendList() != null){
				
				int costMoney = 0;
				int costDiamond = 0;
				int costStone = 0;
				List<List<Integer>> items = new ArrayList<List<Integer>>();
				
				for(Reward reward : baseItem.getUseExpendList()){
				 switch (reward.getType()) {			
					case RewardTypeConstant.ITEM:
						List<Integer> lists = new ArrayList<Integer>();
						lists.add(reward.getId());
						lists.add(reward.getNum());					
						items.add(lists);
						break;
					case RewardTypeConstant.MONEY:					
						costMoney += reward.getNum();
						break;
					case RewardTypeConstant.DIAMOND:
						costDiamond += reward.getNum();
						break;
					case RewardTypeConstant.STONE:
						costStone += reward.getNum();
						break;
					default:
						break;
					}
				}			
				
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
				if(playerWealth.getGold() < costMoney) throw new GameException(ExceptionConstant.PLAYER_1112);
				if(playerWealth.getDiamond() < costDiamond) throw new GameException(ExceptionConstant.PLAYER_1113);
				if(playerWealth.getStone() < costStone) throw new GameException(ExceptionConstant.PLAYER_1114);	
		
				rewardService.deductItemList(playerId, items);
				
				if(costMoney > 0){
					playerService.addGold_syn(playerId, -costMoney);
				}
				
				if(costDiamond > 0){
					playerService.addDiamond_syn(playerId, -costDiamond, InOutLogConstant.DIAMOND_OF_2);
				}

				if(costStone > 0){
					playerService.addStone_syn(playerId,  -costStone);
				}				
			}					
						
			if(baseItem.getTinyType() == ItemConstant.ITEM_USE_LIMIT_TYPE_1){
				playerDaily.setUseDrugOneItem(playerDaily.getUseDrugOneItem() + num);	
				playerService.updatePlayerDaily(playerDaily);
			}else if(baseItem.getTinyType() == ItemConstant.ITEM_USE_LIMIT_TYPE_2){
				playerDaily.setUseDrugTwoItem(playerDaily.getUseDrugTwoItem() + num);
				playerService.updatePlayerDaily(playerDaily);
			}		
						
		
			int effectValue = baseItem.getEffectValue();
			
			int result = 0;
			switch (baseItem.getEffectType()) {
			case ItemConstant.EFFECT_TYPE_1:
				result = this.useAddHpItem(playerId, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_2:
				result = this.useAddMpItem(playerId, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_3:
				playerService.addGold_syn(playerId, effectValue*num);
				break;
			case ItemConstant.EFFECT_TYPE_4:
				playerService.addDiamond_syn(playerId, effectValue*num, null);
				break;
			case ItemConstant.EFFECT_TYPE_5:
				playerService.addStone_syn(playerId, effectValue*num);
				break;
			case ItemConstant.EFFECT_TYPE_6:
				
				break;
			case ItemConstant.EFFECT_TYPE_7:
				
				break;
			case ItemConstant.EFFECT_TYPE_8:
				// 经验=基础值*(0.018*等级^2+0.35*等级+0.7)
				int value = (int)(effectValue * (Math.pow(playerProperty.getLevel(), 2)*0.018 + playerProperty.getLevel()*0.35 + 0.7));
				playerService.addPlayerExp(playerId, value * num);
				break;
			case ItemConstant.EFFECT_TYPE_10:
				result = this.usePKItem(playerId, effectValue*num);
				break;
			case ItemConstant.EFFECT_TYPE_11:
				this.useGiftItem(playerId, effectValue);
				break;			
			case ItemConstant.EFFECT_TYPE_14:
				serviceCollection.getTaskService().acceptHuntTask(playerId, effectValue);
				break;				
			case ItemConstant.EFFECT_TYPE_16:
				this.addBagGrid(playerId, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_17:
				serviceCollection.getBuffService().addBuffById(playerId, effectValue);
				break;
			case ItemConstant.EFFECT_TYPE_20:
				serviceCollection.getFurnaceService().addFurnacePiece(playerId, 1, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_21:
				serviceCollection.getFurnaceService().addFurnacePiece(playerId, 2, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_22:
				serviceCollection.getFurnaceService().addFurnacePiece(playerId, 3, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_23:
				serviceCollection.getFurnaceService().addFurnacePiece(playerId, 4, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_24:
				serviceCollection.getFurnaceService().addFurnacePiece(playerId, 5, effectValue * num);
				break;
			case ItemConstant.EFFECT_TYPE_25:
				this.useTransferItem(playerId, effectValue, baseItem.getIcon());
				break;
			default:
				break;
			}
			
			if(result != 0) return null;
			// 扣除道具
			this.updateNumByPlayerBagId(playerId, playerBag, playerBag.getNum()-num);
			
			// 使用道具返回
			S_UseItem.Builder builder = S_UseItem.newBuilder();
			builder.setItemId(itemId);
			builder.setNum(num);
			MessageObj msg = new MessageObj(MessageID.S_UseItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			
			return playerBag;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerBag> getAllPlayerBagListByPlayerID(long playerId) {
		List<PlayerBag> playerBackpackList = (List<PlayerBag>) CacheService.getFromCache(CacheConstant.PLAYER_BAG + playerId);
		if (playerBackpackList == null) {
			playerBackpackList = playerBagDAO.getPlayerBagListByPlayerID(playerId);
			CacheService.putToCache(CacheConstant.PLAYER_BAG+playerId, Collections.synchronizedList(playerBackpackList));
		}
		return playerBackpackList;
	}

	@Override
	public PlayerBag getPlayerBagById(long playerId, long playerBagId) {
		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);		
		for (PlayerBag pb : playerBackpackList) {		
			if (pb != null && pb.getPlayerBagId() == playerBagId && pb.getState() > ItemConstant.STATE_FREE) {
				return pb;
			}
		}
		return null;
	}

	@Override
	public PlayerBag getPlayerBagForEquipment(long playerId,
			long playerEquipmentId) {
		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);		
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT && pb.getItemId() == playerEquipmentId && pb.getState() > 0) {
				return pb;
			}
		}
		return null;
	}
	
	@Override
	public PlayerBag createPlayerBag(long playerId, long itemId, int goodsType,
			int isBinding, int num, Integer itemIndex) {

		List<PlayerBag> lists = this.getAllPlayerBagListByPlayerID(playerId);
		PlayerBag model = null;
		boolean bFind = false;
		for(PlayerBag pb : lists){
			if(pb.getItemIndex().equals(itemIndex)){
				model = pb;
				bFind = true;
				break;
			}
		}
		if(!bFind){
			model = new PlayerBag();
			model.setPlayerBagId(IDUtil.geneteId(PlayerBag.class));
		}
		model.setPlayerId(playerId);
		model.setIsBinding(isBinding);
		model.setItemId(itemId);
		model.setGoodsType(goodsType);
		model.setItemIndex(itemIndex);
		model.setState(ItemConstant.STATE_BACKPACK);
		model.setNum(num);

		if(!bFind){
			playerBagDAO.createPlayerBag(model);	
			lists.add(model);
		}else{
			this.updatePlayerBag(model);
		}

		return model;
	}

	@Override
	public Integer getNewItemIndexByPlayerId(long playerId) {
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG_INDEX)) {

			PlayerExt playerExt = playerService.getPlayerExtById(playerId);

			List<Integer> itemIndexList = this.getItemIndexListByPlayerId(playerId);
			if (itemIndexList.size() > 0) {
				Collections.sort(itemIndexList);
			}

			// 背包空
			if (itemIndexList == null || itemIndexList.size() == 0) {
				return 1;
			}
			// 背包满
			if (playerExt.getBagGrid() <= itemIndexList.size()) {
				return null;
			}

			Integer index = 1;
			// 找空位
			for (int i = 0; i < itemIndexList.size(); i++) {
				if (!itemIndexList.contains(index)) {
					return index;
				}
				index++;
			}

			// 找空位
			if (playerExt.getBagGrid() > itemIndexList.size()) {
				return itemIndexList.get(itemIndexList.size()-1)+1;
			}
		}

		return null;
	}

	/**
	 * 更新格子数量  <0 移除
	 */
	public PlayerBag updateNumByPlayerBagId(long playerId, PlayerBag playerBag, int num)throws Exception {
		if (playerBag == null) {
			throw new GameException(ExceptionConstant.BAG_1300);
		}

		if (num < 0) {
			throw new GameException(ExceptionConstant.ERROR_10000);
		}			

		playerBag.setNum(num);
		
		if (num == 0) {

			if(playerBag.getGoodsType() != ItemConstant.GOODS_TYPE_EQUPMENT){
				// 道具或药品
				int itemId = (int)playerBag.getItemId();
				BaseItem item = this.getBaseItemById(itemId);
				if(item == null){
					LogUtil.error("updateNumByPlayerBagId item is null with id is "+playerBag.getItemId());
					throw new GameException(ExceptionConstant.BAG_1300);
				}
				//移除药品栏
				int type = 0;
				if(item.getEffectType() == ItemConstant.EFFECT_TYPE_1){
					type = 1;
			
				}else if(item.getEffectType() == ItemConstant.EFFECT_TYPE_2){
					type = 2;
				}
				if(type > 0){
					if(this.getItemNumByPlayerIdAndItemId(playerId, itemId) < 1){
						List<PlayerDrug> lists = this.listPlayerDrugs(playerId);
						for(PlayerDrug model : lists){
							if(model.getType() == type){
								if(itemId == model.getItemId()){
									model.setItemId(0);
									this.updatePlayerDrug(model);
									break;
								}
							}
						}
					}					
				}
			}

			playerBag.reset();
		} 	
		this.updatePlayerBag(playerBag);
		return playerBag;
	}

	private PlayerBag getByPlayerIdItemIdBinding(long playerId,
			Integer itemId, int isBinding) {
		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);		
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getState() > ItemConstant.STATE_FREE && pb.getItemId() == itemId
					&& pb.getIsBinding() == isBinding) {

				BaseItem item = this.getBaseItemById(itemId);
				if (pb.getNum() < item.getPileNumber()) {
					return pb;
				}
			}
		}

		return null;
	}


	@Override
	public List<PlayerBag> addPlayerBag_check(long playerId, BaseItem item,
			int num, int isBinding) throws Exception{
		List<PlayerBag> playerBackpackList = new ArrayList<PlayerBag>();

		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		int bagGrid = playerExt.getBagGrid();
		int usedGridNum = this.getUsedGridNumByPlayerID(playerId);

		PlayerBag playerBackpack = this.getByPlayerIdItemIdBinding(playerId, item.getId(), isBinding);

		if (playerBackpack == null && bagGrid <= usedGridNum) {
			throw new GameException(ExceptionConstant.BAG_1304);
		}
		Integer newItemIndex = null;
		// 已有该物品
		if (playerBackpack != null && item.getPileNumber() > 1) {
			if (num >= item.getPileNumber()) {
				Integer needGrid = (num-1)/item.getPileNumber()+1;
				if (needGrid > (bagGrid - usedGridNum)) {
					throw new GameException(ExceptionConstant.BAG_1304);
				}

				for (int i=0;i< needGrid - 1;i++) {
					newItemIndex = this.getNewItemIndexByPlayerId(playerId);
					PlayerBag fullBackpack = this.createPlayerBag(playerId, item.getId(), item.getGoodsType(), isBinding, item.getPileNumber(), newItemIndex);
					playerBackpackList.add(fullBackpack);
				}

				num = Math.max(0, num - (needGrid-1)*item.getPileNumber());
			}

			if(num == item.getPileNumber()){
				newItemIndex = this.getNewItemIndexByPlayerId(playerId);
				if (newItemIndex == null) {
					throw new GameException(ExceptionConstant.BAG_1304);
				}
				// 添加背包物品
				PlayerBag playerBackpack2 = this.createPlayerBag(playerId, item.getId(), item.getGoodsType(), isBinding, num, newItemIndex);
				playerBackpackList.add(playerBackpack2);
			}else if (playerBackpack.getNum() + num > item.getPileNumber()) {
				Integer leftNum = playerBackpack.getNum() + num - item.getPileNumber();
				newItemIndex = this.getNewItemIndexByPlayerId(playerId);
				if (newItemIndex == null) {
					throw new GameException(ExceptionConstant.BAG_1304);
				}
				
				int newNum = playerBackpack.getNum()+item.getPileNumber() - playerBackpack.getNum();
				this.updateNumByPlayerBagId(playerId, playerBackpack, newNum);
				
				playerBackpackList.add(playerBackpack);

				// 添加背包物品
				PlayerBag playerBackpack2 = this.createPlayerBag(playerId, item.getId(), item.getGoodsType(), isBinding, leftNum, newItemIndex);
				playerBackpackList.add(playerBackpack2);

			} else {
				int newNum = playerBackpack.getNum()+num;
				this.updateNumByPlayerBagId(playerId, playerBackpack, newNum);
				playerBackpackList.add(playerBackpack);
			}
		} else {
			if (item.getPileNumber() == 1) {
				int freeGridNum = this.getFreeGridNumByPlayerID(playerId);
				if (freeGridNum < num) {
					throw new GameException(ExceptionConstant.BAG_1304);
				}
				for (int i=0;i<num;i++) {
					newItemIndex =this.getNewItemIndexByPlayerId(playerId);
					playerBackpack = this.createPlayerBag(playerId, item.getId(), item.getGoodsType(), isBinding, 1, newItemIndex);
					playerBackpackList.add(playerBackpack);
				}
			} else {
				// 添加新物品
				newItemIndex = this.getNewItemIndexByPlayerId(playerId);
				if (newItemIndex == null) {
					throw new GameException(ExceptionConstant.BAG_1304);
				}

				// 数量大于
				if (num >= item.getPileNumber()) {
					Integer needGrid = (num-1)/item.getPileNumber()+1;
					if (needGrid > (bagGrid - usedGridNum)) {
						throw new GameException(ExceptionConstant.BAG_1304);
					}

					for (int i=0;i< needGrid - 1;i++) {
						newItemIndex = this.getNewItemIndexByPlayerId(playerId);
						PlayerBag fullBackpack = this.createPlayerBag(playerId, item.getId(), item.getGoodsType(), isBinding, item.getPileNumber(), newItemIndex);
						playerBackpackList.add(fullBackpack);
					}

					num = Math.max(0, num - (needGrid-1)*item.getPileNumber());
				}

				// 添加背包物品
				newItemIndex =this.getNewItemIndexByPlayerId(playerId);
				playerBackpack = this.createPlayerBag(playerId, item.getId(), item.getGoodsType(), isBinding, num, newItemIndex);
				playerBackpackList.add(playerBackpack);
			}
		}

		return playerBackpackList;
	}

	@Override
	public List<PlayerBag> addPlayerBag_nocheck(long playerId, Integer itemId, int num, int isBinding) throws Exception {
		List<PlayerBag> playerBackpackList = new ArrayList<PlayerBag>();

		BaseItem item = this.getBaseItemById(itemId);

		Integer itemIndex = null;
		if (item.getPileNumber() == 1) {
			for (int i=0;i<num;i++) {
				itemIndex = this.getNewItemIndexByPlayerId(playerId);
				if (itemIndex == null) {
					// 发邮件
					this.backpackFull(playerId, itemId, RewardTypeConstant.ITEM, num, isBinding) ;
				} else {
					PlayerBag playerBackpack = this.createPlayerBag(playerId, itemId, item.getGoodsType(), isBinding, 1, itemIndex);
					playerBackpackList.add(playerBackpack);
				}
			}
		} else {
			PlayerBag pb =this.getByPlayerIdItemIdBinding(playerId, itemId, isBinding);
			if (pb == null) {
				itemIndex =this.getNewItemIndexByPlayerId(playerId);
				if (itemIndex == null) {
					// 发邮件
					this.backpackFull(playerId, itemId, RewardTypeConstant.ITEM, num, isBinding);
				} else {
					PlayerBag playerBackpack = this.createPlayerBag(playerId, itemId, item.getGoodsType(), isBinding, num, itemIndex);
					playerBackpackList.add(playerBackpack);
				}
			} else {
				if (pb.getNum() + num > item.getPileNumber()) {
					Integer leftNum = pb.getNum() + num - item.getPileNumber();

					int newNum = pb.getNum()+item.getPileNumber() - pb.getNum();
					this.updateNumByPlayerBagId(playerId, pb, newNum);
					
					// 添加背包物品
					itemIndex =this.getNewItemIndexByPlayerId(playerId);
					if (itemIndex == null) {
						// 发邮件
						this.backpackFull(playerId, itemId, RewardTypeConstant.ITEM, leftNum, isBinding);
					} else {
						PlayerBag pb2 = this.createPlayerBag(playerId, itemId, item.getGoodsType(), isBinding, leftNum, itemIndex);
						playerBackpackList.add(pb2);
					}
				} else {
					this.updateNumByPlayerBagId(playerId, pb, pb.getNum()+num);
				}
				playerBackpackList.add(pb);
			}
		}
		return playerBackpackList;
	}

	@Override
	public int getItemNumByPlayerIdAndItemId(long playerId,
			Integer itemId) {
		int num = 0;

		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getGoodsType() > ItemConstant.GOODS_TYPE_EQUPMENT && pb.getItemId() == itemId) {
				num += pb.getNum();
			}
		}
		return num;
	}

	@Override
	public List<PlayerBag> deductItem(long playerId, Integer itemId, int num, boolean isBinding) throws Exception{
		List<PlayerBag> pbList = new ArrayList<PlayerBag>();

		if (num < 1) return pbList;
		

		BaseItem item = this.getBaseItemById(itemId);
		if (item == null) {
			throw new GameException(ExceptionConstant.BAG_1300);
		}
		int tNum =this.getItemNumByPlayerIdAndItemId(playerId, itemId);
		if (tNum < num) {
			throw new GameException(ExceptionConstant.BAG_1306);
		} 

		if(isBinding){
			// 绑定物品
			Integer bindNum = this.getItemNumByIsBinding(playerId, itemId, ItemConstant.ITEM_IS_BINDING);
			if (bindNum > 0) {
				// 扣绑定
				List<PlayerBag> pbOneList = this.deductItemOne(playerId, item, ItemConstant.ITEM_IS_BINDING, num);
				if (pbOneList.size() > 0) {
					pbList.addAll(pbOneList);
				}
			}
			
			if (bindNum < num) {
				// 扣非绑定
				List<PlayerBag> pbTwoList = this.deductItemOne(playerId, item, ItemConstant.ITEM_NOT_BINDING, num-bindNum);
				if (pbTwoList.size() > 0) {
					pbList.addAll(pbTwoList);
				}
			} 
		}else{
			// 扣非绑定
			List<PlayerBag> pbTwoList = this.deductItemOne(playerId, item, ItemConstant.ITEM_NOT_BINDING, num);
			if (pbTwoList.size() > 0) {
				pbList.addAll(pbTwoList);
			}
		}
		
		return pbList;
	}
	
	@Override
	public List<PlayerBag> deductItemList(long playerId, List<List<Integer>> items) throws Exception {
		if(items == null || items.isEmpty()) return null;
		
		for(List<Integer> list : items){
			int ownNum = this.getItemNumByPlayerIdAndItemId(playerId, list.get(0));
			if(ownNum < list.get(1)){
				throw new GameException(ExceptionConstant.BAG_1306);	
			}
		}
		
		List<PlayerBag> pbList = new ArrayList<PlayerBag>();		
		
		for(List<Integer> list : items){						
			pbList.addAll(this.deductItem(playerId, list.get(0), list.get(1), true));	
		}
		return pbList;
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_BAG+playerId);
		CacheService.deleteFromCache(CacheConstant.PLAYER_DRUG+playerId);
	}

	@Override
	public void updatePlayerBag(PlayerBag playerBag) {
		// 同步缓存更新
		Set<GameEntity> updateBackpackCacheSet = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_BAG);
		if (!updateBackpackCacheSet.contains(playerBag)) {
			updateBackpackCacheSet.add(playerBag);
		}
	}
	
	/**
	 * 删除玩家道具
	 */
	public void removePlayerBag(PlayerBag playerBag) {
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		playerBag.setState(0);
		playerBag.setNum(0);
		
		S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
		
		if (playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT) {
			// 装备
			IEquipmentService equipmentService = serviceCollection.getEquipmentService();
			PlayerEquipment equipment = equipmentService.deletePlayerEquipmentByID(playerBag.getPlayerId(), playerBag.getItemId());
			if (equipment != null) {
				
				builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(equipment));
			}
		} 
		
		builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
		
		playerBag.reset();
		this.updatePlayerBag(playerBag);
		
		MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerBag.getPlayerId(), msg);
		
	}
	
	/**
	 * 玩家所有物品下标集合
	 */
	private List<Integer> getItemIndexListByPlayerId(long playerId) {

		List<Integer> itemIndexList = new ArrayList<Integer>();

		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);		
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getState() == ItemConstant.STATE_BACKPACK) {
				itemIndexList.add(pb.getItemIndex());
			}
		}
		return itemIndexList;
	}
	
	/**
	 * 已经用的格子数
	 */
	private int getUsedGridNumByPlayerID(long playerId) {

		int usedGridNum = 0;
		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getState() == ItemConstant.STATE_BACKPACK) {
				usedGridNum += 1;
			}
		}
		return usedGridNum;
	}
	
	
	/**
	 * 获取未用的格子数
	 */
	public int getFreeGridNumByPlayerID(long playerId) {

		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();

		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		int usedGridNum = getUsedGridNumByPlayerID(playerId);

		return Math.max(0, playerExt.getBagGrid() - usedGridNum);

	}
	
	/**
	 * 背包满  发送邮件
	 */
	private void backpackFull(long playerId, Integer itemId, int type, int num, int isBlind) throws JSONException {

		IMailService mailService = GameContext.getInstance().getServiceCollection().getMailService();
		int[][] rewards = new int[1][4];
		int[] items = new int[]{type,itemId,num,isBlind};
		rewards[0] = items;
		
		mailService.systemSendMail(playerId, ResourceUtil.getValue("bag_1"), ResourceUtil.getValue("bag_2"), 
				SplitStringUtil.getStringByIntIntList(rewards), 0);
	}
	
	/**
	 * 获取绑定或者不绑的数量
	 */
	@Override
	public Integer getItemNumByIsBinding(long playerId, Integer itemId, int isBinding) {
		int num = 0;

		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getGoodsType() > ItemConstant.GOODS_TYPE_EQUPMENT && pb.getItemId() == itemId && pb.getIsBinding() == isBinding) {
				num += pb.getNum();
			}
		}

		return num;
	}	

	/**
	 * 扣减道具
	 * @throws Exception 
	 * */
	private List<PlayerBag> deductItemOne(long playerId, BaseItem item, int isBinding, int num) throws Exception {
		List<PlayerBag> pbList = new ArrayList<PlayerBag>();

		int needNum = num;
		while (needNum > 0) {
			PlayerBag pbOne = this.getOtherOneByPlayerIdItemIdBinding(playerId, item.getId(), isBinding);
			if (pbOne != null) {
				int pbOneNum = pbOne.getNum();
				if (pbOne.getNum() < needNum) {
					needNum = needNum - pbOne.getNum();
					pbOneNum = 0;
				} else {
					pbOneNum = pbOneNum - needNum;
					needNum = 0;
				}
				this.updateNumByPlayerBagId(playerId, pbOne, pbOneNum);
				pbList.add(pbOne);
			} else {
				break;
			}
		}
		return pbList;
	}
	
	/**
	 * 根据玩家编号，物品编号,是否绑定得到玩家背包物品
	 * 注:不限数量,慎用
	 * */
	private PlayerBag getOtherOneByPlayerIdItemIdBinding(long playerId,  Integer itemId, int isBinding) {

		List<PlayerBag> playerBackpackList = this.getAllPlayerBagListByPlayerID(playerId);		
		for (PlayerBag pb : playerBackpackList) {
			if (pb.getState() > ItemConstant.STATE_FREE && pb.getItemId() == itemId
					&& pb.getIsBinding() == isBinding) {
				return pb;
			}
		}	

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerDrug> listPlayerDrugs(long playerId) {
		List<PlayerDrug> playerDrugList = (List<PlayerDrug>) CacheService.getFromCache(CacheConstant.PLAYER_DRUG + playerId);
		if (playerDrugList == null) {
			playerDrugList =playerBagDAO.listPlayerDrugs(playerId);
			if(playerDrugList.isEmpty()){
				for(int i = 0; i<3; i++){
					PlayerDrug model = new PlayerDrug(playerId, ItemConstant.HP_DRUG_TYPE, i, 0);
					playerBagDAO.createPlayerDrug(model);
					playerDrugList.add(model);
				}
				for(int i = 0; i<3; i++){
					PlayerDrug model = new PlayerDrug(playerId, ItemConstant.MP_DRUG_TYPE, i, 0);
					playerBagDAO.createPlayerDrug(model);
					playerDrugList.add(model);
				}
			}
			CacheService.putToCache(CacheConstant.PLAYER_DRUG+playerId, playerDrugList);
		}
		return playerDrugList;
	}
	
	private void updatePlayerDrug(PlayerDrug playerDrug) {
		// 同步缓存更新
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_DRUG);
		if (!lists.contains(playerDrug)) {
			lists.add(playerDrug);
		}
	}

	@Override
	public PlayerDrug putonDrug(long playerId, int type, int itemId) throws Exception {
		if(playerId < 1 || itemId < 1)  throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_DRUG)) {
			
			List<PlayerDrug> lists = this.listPlayerDrugs(playerId);
			
			PlayerDrug playerDrug = null;
			for(PlayerDrug model : lists){
				if(model.getType() == type){
					if(model.getItemId() == itemId) throw new GameException(ExceptionConstant.DRUG_1500);
					
					if(model.getItemId() == 0){
						playerDrug = model;
						break;
					}
				}
			}
			
			if(playerDrug == null) throw new GameException(ExceptionConstant.DRUG_1501);
			
			playerDrug.setItemId(itemId);
			this.updatePlayerDrug(playerDrug);
			
			// 触发任务
			GameContext.getInstance().getServiceCollection().getTaskService().executeTask(playerId, TaskConstant.TYPE_13, null);
			
			return playerDrug;
		}
	}

	@Override
	public PlayerDrug putdownDrug(long playerId, int type, int itemId) throws Exception {
		if(playerId < 1 || itemId < 1)  throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_DRUG)) {
			
			List<PlayerDrug> lists = this.listPlayerDrugs(playerId);
			PlayerDrug playerDrug = null;
			
			for(PlayerDrug model : lists){
				if(model.getType() == type){
					if(model.getItemId() == itemId){
						playerDrug = model;
						break;
					}
				}
			}
			
			if(playerDrug == null) throw new GameException(ExceptionConstant.DRUG_1502);
			
			playerDrug.setItemId(0);
			this.updatePlayerDrug(playerDrug);
			return playerDrug;
		}

	}

	@Override
	public PlayerBag deductItemByPlayerBagId(long playerId, long playerBagId, int num) throws Exception {	
		
		PlayerBag playerBag = this.getPlayerBagById(playerId, playerBagId);
		if (playerBag == null) return null;
		
		if (num < 1) return null;	
		
		if (playerBag.getNum() < num) {
			throw new GameException(ExceptionConstant.BAG_1306);
		} 
		
		if(playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){
			this.removePlayerBag(playerBag);
			return null;
		}
		this.updateNumByPlayerBagId(playerId, playerBag, playerBag.getNum() - num);
		
		return playerBag;
	}
	
	/**
	 * 加血药
	 */
	@Override
	public int useAddHpItem(long playerId, int addValue){
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		ISceneService sceneService = GameContext.getInstance().getServiceCollection().getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return -1;
		
		if(playerPuppet.getHp() >= playerPuppet.getHpMax()) return -1;
		
		playerPuppet.setHp(playerPuppet.getHp() + addValue);
		
		playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.HP, playerPuppet.getHp());
		
		//同步队员血量显示
		GameContext.getInstance().getServiceCollection().getTeamService().synHp(playerPuppet);
		
		return 0;
	}
	
	/**
	 * 加蓝药
	 */
	@Override
	public int useAddMpItem(long playerId, int addValue){
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		ISceneService sceneService = GameContext.getInstance().getServiceCollection().getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return -1;
		
		if(playerPuppet.getMp() >= playerPuppet.getMpMax()) return -1;
		
		playerPuppet.setMp(playerPuppet.getMp() + addValue);
		playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.MP, playerPuppet.getMp());
		
		return 0;
	}

	/**
	 * 使用减少pk值药
	 */
	private int usePKItem(long playerId, int value){
		
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		ISceneService sceneService = GameContext.getInstance().getServiceCollection().getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return -1;
		
		playerPuppet.setPkVlaue(playerPuppet.getPkVlaue() - value);
		playerService.synPlayerProperty(playerId, ProdefineConstant.PK_VALUE, playerPuppet.getPkVlaue());
		
		if(playerPuppet.getPkVlaue() < 300){ 
			playerPuppet.setNameColor(1);
			if(playerPuppet.getGrayUpdateTime() > 0){
				playerPuppet.setNameColor(2);
			}
			playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.NAME_COLOR, playerPuppet.getNameColor());
		}
		
		return 0;
	}

	/**
	 * 使用礼包
	 */
	private void useGiftItem(long playerId, int giftId)throws Exception{
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		IRewardService rewardService = GameContext.getInstance().getServiceCollection().getRewardService();
		IFashionService fashionService = GameContext.getInstance().getServiceCollection().getFashionService();
		Player player = playerService.getPlayerByID(playerId);
		
		BaseGift baseGift = this.getBaseGiftById(giftId);
		if(baseGift == null){
			LogUtil.error("BaseGift is null with id is "+giftId);
			throw new GameException(ExceptionConstant.BAG_1307);
		}
		
		List<Reward> rewardList = new ArrayList<Reward>();
		
		for(Map.Entry<Integer, Map<Integer, List<Reward>>> entry : baseGift.getRewardMap().entrySet()){
		
			int career = entry.getKey();
			Map<Integer, List<Reward>> map = entry.getValue();
			
			if(career > 0 && career != player.getCareer()) continue;
			
			for(Map.Entry<Integer, List<Reward>> entry2 : map.entrySet()){
				
				List<Reward> rewards = entry2.getValue();
				if(rewards.isEmpty()) continue;			
				
				Reward reward = rewardService.globalRandom(rewards);				
				if(reward.getType() == RewardTypeConstant.FASHION){
					PlayerFashion playerFashion = fashionService.getPlayerFashion(playerId, reward.getId());
					if(playerFashion != null) throw new GameException(ExceptionConstant.BAG_1307);
				}
				
				rewardList.add(reward);				
			}
		}
		rewardService.fetchRewardList(playerId, rewardList);
		
	}
	
	/** 使用背包格扩充卷*/
	private void addBagGrid(long playerId, int num) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		
		int BAG_GRID_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.BAG_GRID_LIMIT);
		if(playerExt.getBagGrid() >= BAG_GRID_LIMIT) throw new GameException(ExceptionConstant.BAG_1311);

		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) throw new GameException(ExceptionConstant.BAG_1307);
		
		playerExt.setBagGrid(playerExt.getBagGrid() + num);
		playerService.updatePlayerExt(playerExt);
	
		playerService.synPlayerProperty(playerId, ProdefineConstant.BAG_GRID, playerExt.getBagGrid());
	}
	
	/**
	 * 使用传送道具
	 */
	private void useTransferItem(Long playerId, int type, int iconId) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) throw new GameException(ExceptionConstant.BAG_1307);
		
		BaseMap baseMap = sceneService.getBaseMap(playerPuppet.getMapId());
		
		if(baseMap.getMapType() > SceneConstant.WORLD_SCENE) throw new GameException(ExceptionConstant.SCENE_1209);
		
		List<Long> playerIds = new ArrayList<Long>();
		if(type == 1){
			//队伍
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			ITeamService teamService = serviceCollection.getTeamService();
			Team team = teamService.getTeam(playerExt.getTeamId());
			if(team == null) throw new GameException(ExceptionConstant.TEAM_2301);
			
			playerIds = teamService.getOnlineTeamPlayerIds(team);
			
		}else if(type == 2){
			//家族
			IFamilyService familyService = serviceCollection.getFamilyService();
			PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);
			if(playerFamily.getPlayerFamilyId() < 1) throw new GameException(ExceptionConstant.FAMILY_2601);
			
			playerIds = familyService.getFamilyPlayerIds(playerFamily.getPlayerFamilyId());
		}else if(type == 3){
			//帮派
			IGuildService guildService = serviceCollection.getGuildService();
			PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() <= 0){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			Guild guild = guildService.getGuildById(playerGuild.getGuildId());
			playerIds.addAll(guild.getPlayerIds());
		}
		
		playerIds.remove(playerId);
		
		S_TransferNotice.Builder builder = S_TransferNotice.newBuilder();
		builder.setType(type);
		builder.setIconId(iconId);
		builder.setToMapId(baseMap.getMap_id());
		builder.setPlayerName(playerPuppet.getName());
		builder.setToPosition(protoBuilderService.buildVector3Msg(playerPuppet.getX(), playerPuppet.getY(), playerPuppet.getZ()));
		
		gameSocketService.sendDataToPlayerList(playerIds,  
				new MessageObj(MessageID.S_TransferNotice_VALUE, builder.build().toByteArray()));
	}
}
