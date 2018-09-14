package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.LockConstant;
import com.constant.RewardTypeConstant;
import com.constant.SignConstant;
import com.constant.VipConstant;
import com.dao.sign.BaseSignDAO;
import com.dao.sign.PlayerSignDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.player.Player;
import com.domain.player.PlayerWealth;
import com.domain.sign.BaseConSignReward;
import com.domain.sign.BaseSign;
import com.domain.sign.PlayerSign;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SignProto.S_GetConSignReward;
import com.message.SignProto.S_SynSign;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISignService;
import com.service.IVipService;
import com.util.SplitStringUtil;

public class SignService implements ISignService{
	private BaseSignDAO baseSignDAO = new BaseSignDAO();
	private PlayerSignDAO playerSignDAO = new PlayerSignDAO();

	@Override
	public void initBaseSign() {
		Map<Integer, BaseSign> baseSignMap = new HashMap<Integer, BaseSign>();
		List<BaseSign> baseSignList = baseSignDAO.listBaseSign();
		for(BaseSign baseSign : baseSignList){
			baseSign.setRewardList(SplitStringUtil.getRewardInfo(baseSign.getReward()));
			baseSignMap.put(baseSign.getSignDay(), baseSign);
		}
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_SIGN, baseSignMap);
		
		
		Map<Integer, BaseConSignReward> baseConSignRewardMap = new HashMap<Integer, BaseConSignReward>();
		List<BaseConSignReward> baseConSignRewardList = baseSignDAO.listBaseConSignReward();
		for(BaseConSignReward baseConSignReward : baseConSignRewardList){
			baseConSignReward.setRewardList(SplitStringUtil.getRewardInfo(baseConSignReward.getReward()));
			baseConSignRewardMap.put(baseConSignReward.getDay(), baseConSignReward);
		}
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_CON_SIGN_REWARD, baseConSignRewardMap);
	}

	@Override
	public void sign(long playerId) throws Exception {	
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IVipService vipService = serviceCollection.getVipService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SIGN)) {			
			PlayerSign playerSign = this.getPlayerSign(playerId);
			if(playerSign == null){
				playerSign = new PlayerSign();
				playerSign.setPlayerId(playerId);			
				playerSignDAO.createPlayerSign(playerSign);
				
				CacheService.putToCache(CacheConstant.PLAYER_SIGN + playerId, playerSign);
			}			
			
			int expDiamond = 0;
			int isReSign = 0;
			if(playerSign.getState() == SignConstant.SIGN_OK){
				// 补签，消耗元宝
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
				expDiamond = (playerSign.getReSignNum() + 1) * 10;
				if(playerWealth.getDiamond() < expDiamond) throw new GameException(ExceptionConstant.PLAYER_1113);
				
				isReSign = 1;
			}
						
			// 签到奖励
			BaseSign baseSign = this.getSignReward(playerSign.getSignNum() + 1);	
			if(baseSign == null) return;
			
			if(baseSign.getRewardList() != null && !baseSign.getRewardList().isEmpty()){
				List<Reward> rewards = new ArrayList<Reward>();
				for(Reward reward : baseSign.getRewardList()){
					if(reward.getType() == RewardTypeConstant.EQUIPMENT ){
						BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
						if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
							continue;
						}						
					}else{
						if(reward.getType() == RewardTypeConstant.ITEM){
							BaseItem baseItem = serviceCollection.getBagService().getBaseItemById(reward.getId());
							Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
							if (baseItem.getNeedJob() > 0 && baseItem.getNeedJob() != player.getCareer()){
								continue;
							}
						}
					}
					
					// vip是否双倍
					int num = reward.getNum();
					if(baseSign.getDoubleReward() == 1){
						int isDouble = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_6);
						if(isDouble == 1){
							num = num*2;
						}
					}
					
					Reward r = new Reward(reward.getType(), reward.getId(), num, reward.getRate(), reward.getBlind());
					rewards.add(r);
				}
				
				
				rewardService.fetchRewardList(playerId, rewards);
			}		
			
			if(isReSign == 1){
				playerService.addDiamond_syn(playerId, -expDiamond, InOutLogConstant.DIAMOND_OF_1);
				playerSign.setReSignNum(playerSign.getReSignNum() + 1);
			}else{
				playerSign.setState(SignConstant.SIGN_OK);
				playerSign.setConSignDay(playerSign.getConSignDay() + 1);
			}
			
			playerSign.setSignNum(playerSign.getSignNum() + 1);	
			this.updatePlayerSign(playerSign);		
		
			S_SynSign.Builder builder = S_SynSign.newBuilder();
			builder.setSignMsg(protoBuilderService.buildSignMsg(playerSign));
			MessageObj msg = new MessageObj(MessageID.S_SynSign_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}


	@Override
	public void getConSignReward(long playerId, int signNum) throws Exception{
		if(playerId < 1 || signNum < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SIGN)) {			
			PlayerSign playerSign = this.getPlayerSign(playerId);
			if(playerSign == null) throw new GameException(ExceptionConstant.ERROR_10000);
			if(playerSign.getConSignRewardList().contains(signNum)) throw new GameException(ExceptionConstant.SIGN_2802);
			if(playerSign.getConSignDay() < signNum) throw new GameException(ExceptionConstant.SIGN_2803);
			
			BaseConSignReward baseConSignReward = this.getConSignReward(signNum);
			if(baseConSignReward == null) throw new GameException(ExceptionConstant.ERROR_10000);
			if(baseConSignReward.getRewardList() != null && !baseConSignReward.getRewardList().isEmpty()){
				List<Reward> rewards = new ArrayList<Reward>();
				for(Reward reward : baseConSignReward.getRewardList()){
					if(reward.getType() == RewardTypeConstant.EQUIPMENT ){
						BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
						if(baseEquipment == null)throw new GameException(ExceptionConstant.EQUIP_1400);
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
						if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
							continue;
						}						
					}else{
						if(reward.getType() == RewardTypeConstant.ITEM){
							BaseItem baseItem = serviceCollection.getBagService().getBaseItemById(reward.getId());
							if(baseItem == null)throw new GameException(ExceptionConstant.BAG_1300);
							Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
							if (baseItem.getNeedJob() > 0 && baseItem.getNeedJob() != player.getCareer()){
								continue;
							}
						}
					}
					
					rewards.add(reward);					
				}
				
				rewardService.fetchRewardList(playerId, rewards);
			}	
			
			playerSign.getConSignRewardList().add(signNum);
			playerSign.setConSignRewardList(playerSign.getConSignRewardList());
			this.updatePlayerSign(playerSign);
			
			
			S_GetConSignReward.Builder builder = S_GetConSignReward.newBuilder();
			builder.setSignNum(signNum);
			MessageObj msg = new MessageObj(MessageID.S_GetConSignReward_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void quartzDaily() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();

		try {
			playerSignDAO.quartzDaliyPlayerSign();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Long> playerIDSetClone = new ArrayList<Long>(playerService.getPlayerIDCache());
		for(long playerId : playerIDSetClone){
			//取缓存
			PlayerSign playerSign = (PlayerSign)CacheService.getFromCache(CacheConstant.PLAYER_SIGN + playerId); 
			if(playerSign == null) continue;
			
			// 如果当天没签到,则连续签到天数置0,领奖状态置null
			if (playerSign.getState() == SignConstant.SIGN_NO){
				playerSign.setConSignDay(0);
				playerSign.getConSignRewardList().clear();
				playerSign.setConSignRewardList(playerSign.getConSignRewardList());
			}else{
				playerSign.setState(SignConstant.SIGN_NO);		
			}
			
			// 若玩家在线则同步
			if(gameSocketService.checkOnLine(playerSign.getPlayerId())){
				S_SynSign.Builder builder = S_SynSign.newBuilder();
				builder.setSignMsg(protoBuilderService.buildSignMsg(playerSign));
				MessageObj msg = new MessageObj(MessageID.S_SynSign_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerSign.getPlayerId(), msg);
			}			
		}	
	}
	
	/** 根据签到天数获取奖励数据*/
	@SuppressWarnings("unchecked")
	private BaseSign getSignReward(int day){
		Map<Integer, BaseSign> baseSignMap = (Map<Integer, BaseSign>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_SIGN);
		
		return baseSignMap.get(day);
	}
	
	
	/** 根据连续签到天数获取奖励数据*/
	@SuppressWarnings("unchecked")
	private BaseConSignReward getConSignReward(int day){
		Map<Integer, BaseConSignReward> baseConSignMap = (Map<Integer, BaseConSignReward>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_CON_SIGN_REWARD);
		
		return baseConSignMap.get(day);
	}
	
	/** 获取玩家签到数据*/
	@Override
	public PlayerSign getPlayerSign(long playerId){
		PlayerSign playerSign = (PlayerSign)CacheService.getFromCache(CacheConstant.PLAYER_SIGN + playerId);
		if(playerSign == null){
			playerSign = playerSignDAO.getPlayerSign(playerId);
			if(playerSign != null){
				CacheService.putToCache(CacheConstant.PLAYER_SIGN + playerId, playerSign);
			}			
		}	
		
		return playerSign;
	}

	
	/** 更新玩家签到数据*/
	private void updatePlayerSign(PlayerSign playerSign) {
		// 同步缓存更新
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_SIGN);
		if (!lists.contains(playerSign)) {
			lists.add(playerSign);
		}
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_SIGN + playerId);
	}
	
	@Override
	public void quartzMonth() {		
		
		try {
			playerSignDAO.quartzMonthPlayerSign();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		List<Long> playerIDSetClone = new ArrayList<Long>(playerService.getPlayerIDCache());
		for(long playerId : playerIDSetClone){
			PlayerSign playerSign = (PlayerSign)CacheService.getFromCache(CacheConstant.PLAYER_SIGN + playerId);
			if(playerSign == null) continue;
			playerSign.setSignNum(0);
			playerSign.setReSignNum(0);
			playerSign.setConSignDay(0);
			playerSign.getConSignRewardList().clear();	
			playerSign.setConSignRewardList(playerSign.getConSignRewardList());
			
			// 若玩家在线则同步
			if(gameSocketService.checkOnLine(playerSign.getPlayerId())){
				S_SynSign.Builder builder = S_SynSign.newBuilder();
				builder.setSignMsg(protoBuilderService.buildSignMsg(playerSign));
				MessageObj msg = new MessageObj(MessageID.S_SynSign_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerSign.getPlayerId(), msg);
			}		
		}	
	}
}
