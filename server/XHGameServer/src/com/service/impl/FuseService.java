package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.BaseCacheService;
import com.common.GameContext;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.RewardTypeConstant;
import com.constant.TaskConstant;
import com.dao.fuse.BaseFuseDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.fuse.BaseCompose;
import com.message.BagProto.S_Decompose;
import com.message.BagProto.S_Refine;
import com.message.BagProto.S_SynBagItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBagService;
import com.service.IEquipmentService;
import com.service.IFuseService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.util.LogUtil;
import com.util.SplitStringUtil;

/**
 * 合成 分解 提炼
 * @author ken
 * @date 2017-3-25
 */
public class FuseService implements IFuseService{
	
	BaseFuseDAO baseFuseDAO = new BaseFuseDAO();
	@Override
	public void initBaseCache() {	
		Map<Integer, BaseCompose> baseComposeMap = new HashMap<Integer, BaseCompose>();
		List<BaseCompose> listBaseCompose = baseFuseDAO.listBaseCompose();
		for(BaseCompose baseCompose : listBaseCompose){
			baseCompose.setComposeList(SplitStringUtil.getRewardInfo(baseCompose.getComposeStr()));
			baseCompose.setDecomposeList(SplitStringUtil.getRewardInfo(baseCompose.getDecomposeStr()));	
			baseCompose.setRefineList(SplitStringUtil.getRewardInfo(baseCompose.getRefineStr()));
			baseComposeMap.put(baseCompose.getId(), baseCompose);
		}	
		BaseCacheService.putToBaseCache(CacheConstant.BASE_COMPOSE, baseComposeMap);
	}

	@SuppressWarnings("unchecked")
	private BaseCompose getBaseComposeById(int itemId){
		Map<Integer, BaseCompose> baseComposeMap = (Map<Integer, BaseCompose>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_COMPOSE);
		
		return baseComposeMap.get(itemId);
	}

	@Override
	public void compose(long playerId, int itemId) throws Exception {
		if(playerId <= 0 || itemId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FUSE)) {
			IBagService bagService = serviceCollection.getBagService();
			IRewardService rewardService = serviceCollection.getRewardService();
			
			int freeGridNum = bagService.getFreeGridNumByPlayerID(playerId);
			if (freeGridNum < 1) {
				throw new GameException(ExceptionConstant.BAG_1304);
			}
			
			BaseCompose baseCompose = this.getBaseComposeById(itemId);
			if(baseCompose.getIsCompose() != 1) throw new GameException(ExceptionConstant.COMPOSE_2900);	
		
			List<Reward> list = baseCompose.getComposeList();
			rewardService.expendJudgment(playerId, list, true, InOutLogConstant.DIAMOND_OF_2);
			
			int blindType = 0;
			for(Reward item : list){
				int bindNum = bagService.getItemNumByIsBinding(playerId, item.getId(), ItemConstant.ITEM_IS_BINDING);
				
				// 如果绑定物品数量大于0
				if (bindNum > 0) {
					
					blindType = ItemConstant.ITEM_IS_BINDING;
					break;
					
				}
			}	
			
			BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(itemId);	
			
			int rewardType = baseEquipment != null ? RewardTypeConstant.EQUIPMENT : RewardTypeConstant.ITEM;
			
			rewardService.fetchRewardOne(playerId, rewardType, baseCompose.getId(), 1, blindType);	
			
			try {
				//合成任务
				List<Integer> conditionList = new ArrayList<Integer>();
				conditionList.add(1);
				serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_15, conditionList);
			} catch (Exception e) {
				LogUtil.error("执行升级任务异常：", e);
			}
		}
	}

	@Override
	public void decompose(long playerId, List<Long> playerBagIds) throws Exception {
		if(playerId < 1 || playerBagIds.isEmpty()) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IBagService bagService = serviceCollection.getBagService();		
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG)) {
			
			List<Long> pbList = new ArrayList<Long>();	
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			
			for(Long playerBagId : playerBagIds){
				PlayerBag playerBag = bagService.getPlayerBagById(playerId, playerBagId);
				if(playerBag == null) throw new GameException(ExceptionConstant.BAG_1303);
				
				if(playerBag.getState() != ItemConstant.STATE_BACKPACK){
					continue;
				}
				
				int id = 0;
				long playerEquipmentId = 0;
				if(playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){				
					PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerBag.getItemId());
					if(playerEquipment == null){
						LogUtil.error("playerId is : " + playerId + ",   decompose is error with id is "+playerBag.getItemId());
					}
					id = playerEquipment.getEquipmentId();
					playerEquipmentId = playerBag.getItemId();
				}else{
					id = (int)playerBag.getItemId();
				}
				
				BaseCompose baseCompose = this.getBaseComposeById(id);
				if(baseCompose == null){
					LogUtil.error("BaseCompose is null with id is "+id);
					continue;
				} 
				
				if(baseCompose.getIsDecompose() != 1){
					LogUtil.error("BaseCompose can not decompose with id is "+id);
					continue;
				}
				
				Reward reward = baseCompose.getDecomposeList().get(0);
				if(reward == null){
					LogUtil.error("BaseCompose decomposeList is null with id is "+id);
					continue;
				}
				
				BaseItem baseItem = bagService.getBaseItemById(reward.getId());
				if(baseItem == null){
					LogUtil.error("decomposeList is error with id is "+id);
					continue;
				}
				
				if(playerEquipmentId > 0){
					PlayerEquipment pe = equipmentService.deletePlayerEquipmentByID(playerId, playerEquipmentId);
					if (pe != null) {
						
						builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(pe));
					}
				}
				playerBag.setItemId(reward.getId());
				playerBag.setGoodsType(baseItem.getGoodsType());
				playerBag.setNum(reward.getNum());
				playerBag.setIsBinding(reward.getBlind());
				
				bagService.updatePlayerBag(playerBag);
				
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
				
				pbList.add(playerBagId);
			}
			
			if(!pbList.isEmpty()){
				S_Decompose.Builder builder1 = S_Decompose.newBuilder();
				builder1.addAllPlayerBagId(pbList);
				MessageObj msg1 = new MessageObj(MessageID.S_Decompose_VALUE, builder1.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg1);
				
				MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
				
				bagService.tidyBag(playerId);
			}
			
		}
	}
	
	@Override
	public void autoDecompose(long playerId, List<Integer> rareIdList) throws Exception {
		if(playerId <= 0 || rareIdList.isEmpty()) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IBagService bagService = serviceCollection.getBagService();		
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FUSE)) {
			List<PlayerBag> playerBags = bagService.getPlayerBagListByPlayerID(playerId);
			
			List<Long> playerBagIds = new ArrayList<Long>();
			//找出可分解的物品或装备
			
			int id = 0;
			
			for (PlayerBag playerBag : playerBags){				
				if(playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){				
					PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerBag.getItemId());
					if(playerEquipment == null){
						LogUtil.error("autoDecompose playerEquipment is null "+playerBag.getItemId());
						continue;
					}
					BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(playerEquipment.getEquipmentId());	
					if(baseEquipment == null){
						LogUtil.error("autoDecompose baseEquipment is null "+playerEquipment.getEquipmentId());
						continue;
					}
					if (!rareIdList.contains(baseEquipment.getRare())) continue;
					
					id = playerEquipment.getEquipmentId();
				}else{					
					BaseItem baseItem = bagService.getBaseItemById((int)playerBag.getItemId());	
					if (!rareIdList.contains(baseItem.getRare())) continue;	
					
					id = (int)playerBag.getItemId();
				}	
				
				BaseCompose baseCompose = this.getBaseComposeById(id);
				if(baseCompose == null){
					continue;
				} 
				
				if(baseCompose.getIsDecompose() != 1){
					continue;
				}
				
				playerBagIds.add(playerBag.getPlayerBagId());
				
			}
			
			if(!playerBagIds.isEmpty()){
				this.decompose(playerId, playerBagIds);
			}		
			
		}
	}

	@Override
	public void refine(long playerId, List<Long> playerBagIds) throws Exception {
		if(playerId < 1 || playerBagIds.isEmpty()) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IBagService bagService = serviceCollection.getBagService();		
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG)) {
			
			List<Long> pbList = new ArrayList<Long>();	
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			
			for(Long playerBagId : playerBagIds){
				PlayerBag playerBag = bagService.getPlayerBagById(playerId, playerBagId);
				if(playerBag == null) throw new GameException(ExceptionConstant.BAG_1303);
				
				if(playerBag.getState() != ItemConstant.STATE_BACKPACK){
					continue;
				}
				
				int id = 0;
				long playerEquipmentId = 0;
				if(playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){				
					PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerBag.getItemId());
					if(playerEquipment == null){
						LogUtil.error("playerId is : " + playerId + ",   decompose is error with id is "+playerBag.getItemId());
					}
					id = playerEquipment.getEquipmentId();
					playerEquipmentId = playerBag.getItemId();
				}else{
					id = (int)playerBag.getItemId();
				}
				
				BaseCompose baseCompose = this.getBaseComposeById(id);
				if(baseCompose == null){
					LogUtil.error("BaseCompose is null with id is "+id);
					continue;
				} 
				
				if(baseCompose.getIsRefine() != 1){
					LogUtil.error("BaseCompose can not refine with id is "+id);
					continue;
				}
				
				Reward reward = baseCompose.getRefineList().get(0);
				if(reward == null){
					LogUtil.error("BaseCompose refineList is null with id is "+id);
					continue;
				}
				
				BaseItem baseItem = bagService.getBaseItemById(reward.getId());
				if(baseItem == null){
					LogUtil.error("refineList is error with id is "+id);
					continue;
				}
				
				if(playerEquipmentId > 0){
					PlayerEquipment pe = equipmentService.deletePlayerEquipmentByID(playerId, playerEquipmentId);
					if (pe != null) {
						
						builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(pe));
					}
				}
				playerBag.setItemId(reward.getId());
				playerBag.setGoodsType(baseItem.getGoodsType());
				playerBag.setNum(reward.getNum());
				playerBag.setIsBinding(reward.getBlind());
				
				bagService.updatePlayerBag(playerBag);
				
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
				
				pbList.add(playerBagId);
			}
			
			if(!pbList.isEmpty()){
				S_Refine.Builder builder1 = S_Refine.newBuilder();
				builder1.addAllPlayerBagId(pbList);
				MessageObj msg1 = new MessageObj(MessageID.S_Refine_VALUE, builder1.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg1);
				
				MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
				
				bagService.tidyBag(playerId);
			}
			
		}
		
	}

	@Override
	public void autoRefine(long playerId, List<Integer> rareIdList) throws Exception {
		if(playerId <= 0 || rareIdList.isEmpty()) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IBagService bagService = serviceCollection.getBagService();		
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FUSE)) {
			List<PlayerBag> playerBags = bagService.getPlayerBagListByPlayerID(playerId);
			
			List<Long> playerBagIds = new ArrayList<Long>();
			//找出可分解的物品或装备
			
			int id = 0;
			
			for (PlayerBag playerBag : playerBags){				
				if(playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){				
					PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerBag.getItemId());
					if(playerEquipment == null){
						LogUtil.error("autoRefine playerEquipment is null "+playerBag.getItemId());
						continue;
					}
					BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(playerEquipment.getEquipmentId());	
					if(baseEquipment == null){
						LogUtil.error("autoRefine baseEquipment is null "+playerEquipment.getEquipmentId());
						continue;
					}
					if (!rareIdList.contains(baseEquipment.getRare())) continue;
					
					id = playerEquipment.getEquipmentId();
				}else{					
					BaseItem baseItem = bagService.getBaseItemById((int)playerBag.getItemId());	
					if (!rareIdList.contains(baseItem.getRare())) continue;	
					
					id = (int)playerBag.getItemId();
				}	
				
				BaseCompose baseCompose = this.getBaseComposeById(id);
				if(baseCompose == null){
					continue;
				} 
				
				if(baseCompose.getIsRefine() != 1){
					continue;
				}
				
				playerBagIds.add(playerBag.getPlayerBagId());
				
			}
			
			if(!playerBagIds.isEmpty()){
				this.refine(playerId, playerBagIds);
			}		
			
		}
		
	}

}
	 


