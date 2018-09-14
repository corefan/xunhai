package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.RewardConstant;
import com.constant.RewardTypeConstant;
import com.constant.VipConstant;
import com.dao.vip.BaseVipDAO;
import com.dao.vip.PlayerVipDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.reward.BaseReward;
import com.domain.vip.BaseVip;
import com.domain.vip.BaseVipPrivilege;
import com.domain.vip.PlayerVip;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.VipProto.S_ActiviteVip;
import com.message.VipProto.S_GetDailyRewardState;
import com.message.VipProto.S_GetPlayerVip;
import com.message.VipProto.S_GetVipWelfare;
import com.message.VipProto.S_GetVipWelfareState;
import com.service.IBuffService;
import com.service.IPlayerService;
import com.service.IRewardService;
import com.service.IVipService;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

public class VipService implements IVipService{
	private BaseVipDAO baseVipDAO = new BaseVipDAO();
	private PlayerVipDAO playerVipDAO = new PlayerVipDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseVip> baseVipMap = new HashMap<Integer, BaseVip>();
		List<BaseVip> listBaseVip = baseVipDAO.listBaseVip();
		for(BaseVip baseVip : listBaseVip){
			baseVip.setRewardList(SplitStringUtil.getRewardInfo(baseVip.getActivateReward()));
			baseVipMap.put(baseVip.getId(), baseVip);
		}	
		BaseCacheService.putToBaseCache(CacheConstant.BASE_VIP, baseVipMap);
		
		Map<Integer, BaseVipPrivilege> baseVipPrivilegeMap = new HashMap<Integer, BaseVipPrivilege>();
		List<BaseVipPrivilege> listBaseVipPrivilege = baseVipDAO.listBaseVipPrivilege();
		for(BaseVipPrivilege baseVipPrivilege : listBaseVipPrivilege){			
			baseVipPrivilegeMap.put(baseVipPrivilege.getId(), baseVipPrivilege);
		}	
		BaseCacheService.putToBaseCache(CacheConstant.BASE_VIP_PRIVILEGE, baseVipPrivilegeMap);	
	}

	@Override
	public void ActiviteVip(long playerId, int vipId){
		if(playerId <= 0 || vipId <= 0) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();		
		IBuffService buffService = serviceCollection.getBuffService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_VIP)) {
			
			BaseVip baseVip = this.getBaseVip(vipId);	
			
			long validTime = 0; 
			PlayerVip playerVip = this.getPlayerVip(playerId);	
			if(playerVip.getVaildTime() < 1){
				playerVip.setVaildTime(System.currentTimeMillis());
			}
			
			if(baseVip.getVipLevel() > playerVip.getLevel()){
				playerVip.setLevel(baseVip.getVipLevel());
			}
		
			validTime = playerVip.getVaildTime() + DateService.DAY_MILLISECOND * baseVip.getValidTime();
			playerVip.setVaildTime(validTime);
			
			// vip buff添加
			BaseVipPrivilege baseVipPrivilege = this.getBaseVipPrivilege(VipConstant.VIP_PRIVILEGE_1);
			BaseVipPrivilege baseVipPrivilege1 = this.getBaseVipPrivilege(VipConstant.VIP_PRIVILEGE_4);
			int buffId = 0;
			int itemId = 0;
			if(baseVip.getVipLevel() == 1){
				buffId = baseVipPrivilege.getVip1();
				itemId = baseVipPrivilege1.getVip1();
				if(playerVip.getVipLv1State() == 0){
					playerVip.setVipLv1State(1);
				}
				
			}else if(baseVip.getVipLevel() == 2){
				buffId = baseVipPrivilege.getVip2();
				itemId = baseVipPrivilege1.getVip2();
				if(playerVip.getVipLv2State() == 0){
					playerVip.setVipLv2State(1);
				}
				
			}else if(baseVip.getVipLevel() == 3){
				buffId = baseVipPrivilege.getVip3();
				itemId = baseVipPrivilege1.getVip3();
				if(playerVip.getVipLv3State() == 0){
					playerVip.setVipLv3State(1);
				}
			}		
			
			buffService.addBuffById(playerId, buffId);			
			this.updatePlayerVip(playerVip);
			
			// vip获取道具				
			int[][] rewards = new int[1][4];
			int[] item = new int[]{RewardTypeConstant.ITEM, itemId, 1, 0};
			rewards[0] = item;
			String rewardStr = SplitStringUtil.getStringByIntIntList(rewards);
			serviceCollection.getMailService().systemSendMail(playerId, ResourceUtil.getValue("mail_1"), ResourceUtil.getValue("bag_3"),  rewardStr, 0);
			
			S_ActiviteVip.Builder builder = S_ActiviteVip.newBuilder();
			builder.setVipId(vipId);
			builder.setInvalidTime(playerVip.getVaildTime());
			builder.setRewardState(1);
			builder.setPlayerVipLevel(playerVip.getLevel());
			
			MessageObj msg = new MessageObj(MessageID.S_ActiviteVip_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}		
	}

	@Override
	public void GetVipActReward(long playerId, int vipId) throws Exception {
		if(playerId <= 0 || vipId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_VIP)) {			
			BaseVip baseVip = this.getBaseVip(vipId);
			PlayerVip playerVip = this.getPlayerVip(playerId);
			if(playerVip == null) throw new GameException(ExceptionConstant.VIP_3300);
			if(baseVip.getVipLevel() > playerVip.getLevel()) throw new GameException(ExceptionConstant.VIP_3301);
			
			if(baseVip.getVipLevel() == 1){
				if(playerVip.getVipLv1State() != 1) throw new GameException(ExceptionConstant.VIP_3300);
			}else if(baseVip.getVipLevel() == 2){
				if(playerVip.getVipLv2State() != 1) throw new GameException(ExceptionConstant.VIP_3300);			
			}else if(baseVip.getVipLevel() == 3){
				if(playerVip.getVipLv3State() != 1) throw new GameException(ExceptionConstant.VIP_3300);			
			}
			
			IRewardService rewardService = GameContext.getInstance().getServiceCollection().getRewardService();				
			rewardService.fetchRewardList(playerId, baseVip.getRewardList());
			
			if(baseVip.getVipLevel() == 1){			
				playerVip.setVipLv1State(2);
			}else if(baseVip.getVipLevel() == 2){			
				playerVip.setVipLv2State(2);
			}else if(baseVip.getVipLevel() == 3){
				playerVip.setVipLv3State(2);
			}			
			this.updatePlayerVip(playerVip);			
		}
	}

	
	/**  同步缓存更新 */	
	private void updatePlayerVip(PlayerVip playerVip) {
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_VIP);
		if (!lists.contains(playerVip)) {
			lists.add(playerVip);
		}
	}	
	
	/** 获取玩家vip数据*/
	public PlayerVip getPlayerVip(long playerId){
		PlayerVip playerVip = (PlayerVip)CacheService.getFromCache(CacheConstant.PLAYER_VIP + playerId);
		if(playerVip == null){					
			playerVip = playerVipDAO.getPlayerVip(playerId);
			if(playerVip == null) {
				playerVip = new PlayerVip();
				playerVip.setPlayerId(playerId);
				playerVipDAO.createPlayerVip(playerVip);
			}
			CacheService.putToCache(CacheConstant.PLAYER_VIP + playerId, playerVip);
		}
		
		return playerVip;
	}
	
	
	/** 获取玩家vip数据*/
	@SuppressWarnings("unchecked")
	private BaseVip getBaseVip(int vipId){
		Map<Integer, BaseVip> baseVipMap = (Map<Integer, BaseVip>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_VIP);
		
		return baseVipMap.get(vipId);	
	}
	
	
	
	/** 获取vip特权数据*/
	@SuppressWarnings("unchecked")
	@Override
	public BaseVipPrivilege getBaseVipPrivilege(int vipPrivilegeId){
		Map<Integer, BaseVipPrivilege> baseVipPrivilegeMap = (Map<Integer, BaseVipPrivilege>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_VIP_PRIVILEGE);
		
		return baseVipPrivilegeMap.get(vipPrivilegeId);	
	}

	@Override
	public void GetDailyReward(long playerId) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_VIP)) {	
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			IRewardService rewardService = GameContext.getInstance().getServiceCollection().getRewardService();
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);			
			if(playerDaily.getDailyRewardState() == 1) throw new GameException(ExceptionConstant.VIP_3303);			
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_DAILY, 101);			
			PlayerVip playerVip = this.getPlayerVip(playerId);			
			if(playerVip == null || playerVip.getLevel() < 1){
				rewardService.fetchRewardList(playerId, baseReward.getRewardList());
				
			}else{
				int isDouble = this.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_7);
				if(isDouble == 1)	{
					List<Reward> list = new ArrayList<Reward>();
					for(Reward reward : baseReward.getRewardList()){
						int num = reward.getNum() * 2;
						list.add(new Reward(reward.getType(), reward.getId(), num, reward.getRate(), reward.getBlind()));
					}
					rewardService.fetchRewardList(playerId, list);
				}else{
					rewardService.fetchRewardList(playerId, baseReward.getRewardList());
				}
			}
			
			playerDaily.setDailyRewardState(1);
			playerService.updatePlayerDaily(playerDaily);
		}
	}
	
	@Override
	public void quartzPlayerVip(){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		List<Long> playerIDSetClone = new ArrayList<Long>(playerService.getPlayerIDCache());
		for(long playerId : playerIDSetClone){
			PlayerVip playerVip = (PlayerVip)CacheService.getFromCache(CacheConstant.PLAYER_VIP + playerId);						
			if(playerVip == null || playerVip.getLevel() < 1) continue;			
			if(playerVip.getVaildTime() <= 0 || playerVip.getVaildTime() > System.currentTimeMillis()) continue;
			
			playerVip.setLevel(0);
			playerVip.setVaildTime(0);
			
			if(gameSocketService.checkOnLine(playerId)){
				
				S_GetPlayerVip.Builder builder = S_GetPlayerVip.newBuilder();		
				builder.setVipLevel(0);
				builder.setInvalidTime(0);			

				builder.addRewardState(playerVip.getVipLv1State());
				builder.addRewardState(playerVip.getVipLv2State());
				builder.addRewardState(playerVip.getVipLv3State());
				
				MessageObj msg = new MessageObj(MessageID.S_GetPlayerVip_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			}
		}		
	}

	@Override
	public void getGetDailyRewardState(long playerId) {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		
		S_GetDailyRewardState.Builder builder = S_GetDailyRewardState.newBuilder();
		builder.setState(playerDaily.getDailyRewardState());
		
		MessageObj msg = new MessageObj(MessageID.S_GetDailyRewardState_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_VIP + playerId);
	}

	/** 获取vip特权值 */
	@Override
	public int getVipPrivilegeValue(long playerId, int vipPrivilegeId) {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IVipService vipService = serviceCollection.getVipService();
		PlayerVip playerVip = vipService.getPlayerVip(playerId);
		int value = 0;
		if(playerVip != null && playerVip.getLevel() > 0){
			BaseVipPrivilege baseVipPrivilege = vipService.getBaseVipPrivilege(vipPrivilegeId);
			int vipLevel = playerVip.getLevel();
			if(vipLevel == 1){
				value = baseVipPrivilege.getVip1();	
			}else if(vipLevel == 2){
				value = baseVipPrivilege.getVip2();	
			}else if(vipLevel == 3){
				value = baseVipPrivilege.getVip3();	
			}				
		}
		return value;
	}

	@Override
	public void dealLogin(PlayerExt playerExt) {		
		PlayerVip playerVip = this.getPlayerVip(playerExt.getPlayerId());						
		if(playerVip == null || playerVip.getLevel() < 1) return;			
		if(playerVip.getVaildTime() <= 0 || playerVip.getVaildTime() > System.currentTimeMillis()) return;
		
		playerVip.setLevel(0);
		playerVip.setVaildTime(0);
		this.updatePlayerVip(playerVip);		
	}

	@Override
	public void GetVipWelfare(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_VIP)) {
			
			PlayerVip playerVip = this.getPlayerVip(playerId);
			if(playerVip == null) throw new GameException(ExceptionConstant.VIP_3300);
			if(playerVip.getLevel() < 1) throw new GameException(ExceptionConstant.VIP_3301);
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily.getVipWelfareState() == 1)throw new GameException(ExceptionConstant.VIP_3304);
			
			int value = this.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_18);
			playerService.addDiamond_syn(playerId, value, null);
			
			playerDaily.setVipWelfareState(1);
			playerService.updatePlayerDaily(playerDaily);
			
			S_GetVipWelfare.Builder builder = S_GetVipWelfare.newBuilder();
			builder.setState(playerDaily.getVipWelfareState());
			MessageObj msg = new MessageObj(MessageID.S_GetVipWelfare_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void GetVipWelfareState(long playerId){
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		
		S_GetVipWelfareState.Builder builder = S_GetVipWelfareState.newBuilder();
		builder.setState(playerDaily.getVipWelfareState());
		
		MessageObj msg = new MessageObj(MessageID.S_GetVipWelfareState_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		
	}
}
