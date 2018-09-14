package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.ItemConstant;
import com.constant.RewardTypeConstant;
import com.dao.reward.BaseRewardDAO;
import com.dao.reward.RewardDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.player.PlayerWealth;
import com.domain.reward.BaseReward;
import com.domain.reward.RewardRecord;
import com.message.BagProto.S_SynBagItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBagService;
import com.service.IBuffService;
import com.service.IEquipmentService;
import com.service.IFashionService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.IWingService;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 奖励通用实现
 * @author ken
 * @date 2017-2-16
 */
public class RewardService implements IRewardService {
	
	private BaseRewardDAO baseRewardDAO = new BaseRewardDAO();
	private RewardDAO rewardDAO = new RewardDAO();
	
	@Override
	public void initBaseCache() {
		// 综合奖励数据表
		Map<Integer, Map<Integer, BaseReward>> baseRewardMap = new HashMap<Integer, Map<Integer, BaseReward>>();
		List<BaseReward> listBaseReward = baseRewardDAO.listBaseReward();
		for(BaseReward baseReward : listBaseReward){
			baseReward.setRewardList(SplitStringUtil.getRewardInfo(baseReward.getReward()));
			Map<Integer, BaseReward> map = baseRewardMap.get(baseReward.getType());
			if(map == null){
				map =  new HashMap<Integer, BaseReward>();
				baseRewardMap.put(baseReward.getType(), map);
			}
					
			map.put(baseReward.getId(), baseReward);
		}	
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_REWARD, baseRewardMap);
	}
	
	@Override
	public void initCache() {
		Map<Integer, RewardRecord> map = new ConcurrentHashMap<Integer, RewardRecord>();
		
		List<RewardRecord> lists = rewardDAO.listRewardRecords();
		for(RewardRecord model : lists){
			map.put(model.getRewardId(), model);
		}
		
		CacheService.putToCache(CacheConstant.REWARD_RECORD_MAP, map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseReward getBaseReward(int type, int id){
		Map<Integer, Map<Integer, BaseReward>> baseRewardMap = (Map<Integer, Map<Integer, BaseReward>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_REWARD);
		
		Map<Integer, BaseReward> map = baseRewardMap.get(type);
		if(map == null) return null;
		 
		return map.get(id);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, BaseReward> listBaseRewards(int type) {
		Map<Integer, Map<Integer, BaseReward>> baseRewardMap = (Map<Integer, Map<Integer, BaseReward>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_REWARD);
		
		return baseRewardMap.get(type);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public RewardRecord getRewardRecord(int rewardId) {
		Map<Integer, RewardRecord> map = (Map<Integer, RewardRecord>)CacheService.getFromCache(CacheConstant.REWARD_RECORD_MAP);
		
		return map.get(rewardId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createRewardRecord(RewardRecord model) {
		rewardDAO.createRewardRecord(model);
		
		Map<Integer, RewardRecord> map = (Map<Integer, RewardRecord>)CacheService.getFromCache(CacheConstant.REWARD_RECORD_MAP);
		map.put(model.getRewardId(), model);
	}
	
	@Override
	public void updateRewardRecord(RewardRecord model) {
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.REWARD_RECORD);
		if (!lists.contains(model)) {
			lists.add(model);
		}		
	}
	
	@Override
	public List<List<Object>> fetchRewardOne(long playerId, int type, int id, int num, int isBlind) throws Exception{
		return this.fetchRewardOne(playerId, type, id, num, isBlind, 0);
	}

	@Override
	public List<List<Object>> fetchRewardOne(long playerId, int type, int id, int num,
			int isBlind, int tigTag) throws Exception {
		if(num < 1) return null;
		
		List<Reward> rewards = new ArrayList<Reward>();
		rewards.add(new Reward(type, id, num, 0, isBlind));
		
		return this.fetchRewardList(playerId, rewards, tigTag);
		
	}
	
	@Override
	public List<List<Object>> fetchRewardList(long playerId, List<Reward> rewards) throws Exception{
		return this.fetchRewardList(playerId, rewards, 0);
	}
	
	@Override
	public List<List<Object>> fetchRewardList(long playerId, List<Reward> rewards, int tigTag) throws Exception{
		if(rewards == null || rewards.isEmpty()) return null;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IWingService wingService = serviceCollection.getWingService();
		IFashionService fashionService = serviceCollection.getFashionService();
		IBuffService buffService = serviceCollection.getBuffService();
		
		if(!checkBackNum(playerId, rewards)){
			throw new GameException(ExceptionConstant.BAG_1304);
		}
		
		List<PlayerBag> bagLists = new ArrayList<PlayerBag>();
		List<PlayerEquipment> equipLists = new ArrayList<PlayerEquipment>();
		List<List<Object>> rusultList = new ArrayList<List<Object>>();
		for(Reward reward : rewards){
			List<Object> rList = new ArrayList<Object>();
			rList.add(reward.getType());
			rList.add(reward.getId());
			
			long pEquipId = 0;
			switch (reward.getType()) {
			case RewardTypeConstant.EQUIPMENT:
				// 判断背包是否已满
				Integer itemIndex = bagService.getNewItemIndexByPlayerId(playerId);
				if(itemIndex == null){
					throw new GameException(ExceptionConstant.BAG_1304);
				}
				
				PlayerEquipment playerEquipment = equipmentService.createPlayerEquipment(playerId, reward.getId(), reward.getBlind());
				equipLists.add(playerEquipment);
				// 增加背包道具
				PlayerBag playerBag = bagService.createPlayerBag(playerId, playerEquipment.getPlayerEquipmentId(), ItemConstant.GOODS_TYPE_EQUPMENT, playerEquipment.getIsBinding(), 1, itemIndex);
				bagLists.add(playerBag);
				
				pEquipId = playerEquipment.getPlayerEquipmentId();
				break;
			case RewardTypeConstant.ITEM:
				BaseItem item = bagService.getBaseItemById(reward.getId());
				if(item == null) {
					System.out.println("BaseItem itemId " + reward.getId()+ "is null");
					continue;
				}
				bagLists.addAll(bagService.addPlayerBag_check(playerId, item, reward.getNum(), reward.getBlind()));
				break;
			case RewardTypeConstant.MONEY:
				playerService.addGold_syn(playerId, reward.getNum());
				break;
			case RewardTypeConstant.DIAMOND:
				playerService.addDiamond_syn(playerId, reward.getNum(), null);
				break;				
			case RewardTypeConstant.STONE:
				playerService.addStone_syn(playerId, reward.getNum());
				break;				
			case RewardTypeConstant.BIND_DIAMOND:
				
				break;
			case RewardTypeConstant.CONTRIBUTION:
				
				break;
			case RewardTypeConstant.HONOR:
				
				break;
			case RewardTypeConstant.EXPERIENCE:
				playerService.addPlayerExp(playerId, reward.getNum());
				break;
			case RewardTypeConstant.WING:
				wingService.addWing_syn(playerId, reward.getId());
				break;
			case RewardTypeConstant.FASHION:
				fashionService.addFashion_syn(playerId, reward.getId());
				break;
			case RewardTypeConstant.BUFF:
				buffService.addBuffById(playerId, reward.getId());
				break;
			default:
				break;
			}
			rList.add(pEquipId);
			rList.add(reward.getNum());
			rusultList.add(rList);
		}
		
		if(!bagLists.isEmpty()){
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			
			builder.setTigTag(tigTag);
			for(PlayerBag playerBag : bagLists){
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			}
			if(!equipLists.isEmpty()){
				for(PlayerEquipment model : equipLists){
					builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(model));
				}
			}
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
		
		return rusultList;
	}
	
	@Override
	public void fetchRewardList_nocheck(long playerId, List<Reward> rewards) throws Exception {
		if(rewards == null || rewards.isEmpty()) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IWingService wingService = serviceCollection.getWingService();
		IFashionService fashionService = serviceCollection.getFashionService();
		IBuffService buffService = serviceCollection.getBuffService();
		IMailService mailService = serviceCollection.getMailService();
		
		if(checkBackNum(playerId, rewards)){
			this.fetchRewardList(playerId, rewards);
			return;
		}
		
		int[][] mailItems = new int[rewards.size()][];
		
		int index = 0;
		for(Reward reward : rewards){
			switch (reward.getType()) {
			case RewardTypeConstant.EQUIPMENT:
				mailItems[index] = new int[]{reward.getType(),reward.getId(),reward.getNum(),reward.getBlind()};
				index++;
				break;
			case RewardTypeConstant.ITEM:
				mailItems[index] = new int[]{reward.getType(),reward.getId(),reward.getNum(),reward.getBlind()};
				index++;
				break;
			case RewardTypeConstant.MONEY:
				playerService.addGold_syn(playerId, reward.getNum());
				break;
			case RewardTypeConstant.DIAMOND:
				playerService.addDiamond_syn(playerId, reward.getNum(), null);
				break;				
			case RewardTypeConstant.STONE:
				playerService.addStone_syn(playerId, reward.getNum());
				break;				
			case RewardTypeConstant.BIND_DIAMOND:
				
				break;
			case RewardTypeConstant.CONTRIBUTION:
				
				break;
			case RewardTypeConstant.HONOR:
				
				break;
			case RewardTypeConstant.EXPERIENCE:
				playerService.addPlayerExp(playerId, reward.getNum());
				break;
			case RewardTypeConstant.WING:
				wingService.addWing_syn(playerId, reward.getId());
				break;
			case RewardTypeConstant.FASHION:
				fashionService.addFashion_syn(playerId, reward.getId());
				break;
			case RewardTypeConstant.BUFF:
				buffService.addBuffById(playerId, reward.getId());
				break;
			default:
				break;
			}
		}
		
		if(mailItems != null && mailItems.length > 0){
			mailService.systemSendMail(playerId, ResourceUtil.getValue("bag_1"), ResourceUtil.getValue("bag_2"), 
					SplitStringUtil.getStringByIntIntList(mailItems), 0);
		}
	}
	
	/**
	 * 检查背包数量
	 */
	public boolean checkBackNum(long playerId, List<Reward> rewards){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		int freeGridNum = bagService.getFreeGridNumByPlayerID(playerId);		
		
		if(this.getBackNum(rewards) > freeGridNum){
			return false;
		}
		return true;
	}
	
	/**
	 * 计算奖励所需背包格(有相同奖励物品的情况)
	 */
	private int getBackNum(List<Reward> rewardList){
		IBagService bagService = GameContext.getInstance().getServiceCollection().getBagService();
		IEquipmentService equipmentService = GameContext.getInstance().getServiceCollection().getEquipmentService();
		int needNum = 0;		
		
		Map<Integer, Map<Integer,Reward>> rewardMap = new ConcurrentHashMap<Integer, Map<Integer,Reward>>();
		for (Reward model : rewardList) {	
			if(model.getId() < 1) continue;
			
			Map<Integer,Reward> map = rewardMap.get(model.getBlind());
			if(map == null){
				map = new ConcurrentHashMap<>();
				rewardMap.put(model.getBlind(), map);
			}
			
			Reward reward = map.get(model.getId());
			if(reward == null){
				map.put(model.getId(), new Reward(model.getType(), model.getId(), model.getNum(), model.getRate(), model.getBlind()));
			}else{
				reward.setNum(reward.getNum() + model.getNum());	
			}				
		}			

		for(Map.Entry<Integer, Map<Integer,Reward>> entry : rewardMap.entrySet()){
			Map<Integer,Reward> map = entry.getValue();
			for(Map.Entry<Integer,Reward> entry1 : map.entrySet()){				
				Reward model = entry1.getValue();				
				switch (model.getType()) {
				case RewardTypeConstant.EQUIPMENT:
					BaseEquipment equipment = equipmentService.getBaseEquipmentById(model.getId());
					if(equipment == null){
						System.out.println("getBackNum equipment is null with id is "+model.getId());
						continue;
					}
					needNum++;
					break;
				case RewardTypeConstant.ITEM:
					BaseItem item = bagService.getBaseItemById(model.getId());
					if(item == null){
						System.out.println("getBackNum item is null with id is "+model.getId());
						continue;
					}
					
					needNum = needNum + (model.getNum() - 1)/item.getPileNumber() + 1;
					break;
				}
			}			
		}
		return needNum;
	}
	
	@Override
	public void deductItem(long playerId, int itemId, int num, boolean isBinding) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		List<PlayerBag> pbList = bagService.deductItem(playerId, itemId, num, isBinding);
		if(!pbList.isEmpty()){
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			for(PlayerBag playerBag : pbList){
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			}
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}	
	
	@Override
	public void deductItemByPlayerBagId(long playerId, long playerBagId, int num) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		PlayerBag pb = bagService.deductItemByPlayerBagId(playerId, playerBagId, num);		
		if(pb != null){
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(pb));
			
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}


	@Override
	public List<PlayerBag> deductItemList(long playerId, List<List<Integer>> items) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		List<PlayerBag> pbList = bagService.deductItemList(playerId, items);
		if(pbList !=null && !pbList.isEmpty()){
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			for(PlayerBag playerBag : pbList){
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			}
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
		return pbList;
	}


	@Override
	public void fetchDropEquipment(long playerId, long playerEquipmentId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		Integer itemIndex = bagService.getNewItemIndexByPlayerId(playerId);
		if(itemIndex == null){
			throw new GameException(ExceptionConstant.BAG_1304);
		}
		PlayerEquipment playerEquipment = equipmentService.removeDropEquipmentCache(playerEquipmentId);
		if(playerEquipment != null){
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			playerEquipment.setState(ItemConstant.EQUIP_STATE_BACKPACK);
			playerEquipment.setPlayerId(playerId);
			equipmentService.updatePlayerEquipment(playerEquipment);
			equipmentService.getPlayerEquipmentList(playerId).add(playerEquipment);
			
			builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
			// 增加背包道具
			PlayerBag playerBag = bagService.createPlayerBag(playerId, playerEquipment.getPlayerEquipmentId(), ItemConstant.GOODS_TYPE_EQUPMENT, playerEquipment.getIsBinding(), 1, itemIndex);
			builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	/** 独立随机奖励*/
	public List<Reward> independRandom(List<Reward> rewardList) {	
		if ( rewardList == null || rewardList.isEmpty()) return null;		
		List<Reward> list = new ArrayList<Reward>();		
		for(Reward model : rewardList){
			int n = RandomService.getRandomNum(10000);	
			if(model.getRate() < n) continue;
			list.add(model);
		}
		
		return list;
	}

	
	/** 全局随机奖励*/
	public Reward globalRandom(List<Reward> rewardList){
		if(rewardList.size() == 1){
			return rewardList.get(0);
		}		
	
		int randomSum = 0;
		for(Reward model : rewardList){
			randomSum += model.getRate();
		}
		
		int random = RandomService.getRandomNum(randomSum);
		int rate = 0;
		for(Reward model : rewardList){
			if(random <= model.getRate() + rate){
				return model;			
			}
			rate += model.getRate();
		}
		
		return null;		
	}
	
	
	/**
	 * 消耗物品
	 * @param expendList 需要消耗的物品
	 * @param 使用的礼包数量-> 对应消耗物品组数
	 * @param flag 是否扣除物品标识
	 * @param num 倍数
	 */
	@Override
	public void expendJudgment(long playerId, List<Reward> expendList, boolean flag, String costName) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		IBagService bagService = serviceCollection.getBagService();
		
		if(expendList == null || expendList.isEmpty()) return;
		
		int costMoney = 0;
		int costDiamond = 0;
		int costStone = 0;
		List<List<Integer>> items = new ArrayList<List<Integer>>();
		for(Reward reward : expendList){
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
	
		for(List<Integer> list : items){
			int ownNum = bagService.getItemNumByPlayerIdAndItemId(playerId, list.get(0));
			if(ownNum < list.get(1)){
				throw new GameException(ExceptionConstant.BAG_1306);	
			}
		}
		
		if(flag){
			this.deductItemList(playerId, items);
			
			if(costMoney > 0){
				playerService.addGold_syn(playerId, -costMoney);
			}
			
			if(costDiamond > 0){
				playerService.addDiamond_syn(playerId, -costDiamond, costName);
			}

			if(costStone > 0){
				playerService.addStone_syn(playerId,  -costStone);
			}
		}
	}

}
