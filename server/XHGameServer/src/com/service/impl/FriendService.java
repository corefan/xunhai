package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.TaskConstant;
import com.dao.friend.FriendDao;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.friend.PlayerApply;
import com.domain.friend.PlayerFriend;
import com.domain.player.Player;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.message.FriendProto.ApplyMsg;
import com.message.FriendProto.S_ApplyMsgList;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFriendService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.util.IDUtil;
import com.util.LogUtil;


/**
 * 好友系统
 * @author ken
 * @date 2017-1-19
 */
public class FriendService implements IFriendService {	
	private FriendDao friendDAO = new FriendDao();
	
	
	@SuppressWarnings("unchecked")
	public List<PlayerFriend> listPlayerFriend(long playerId) {
		List<PlayerFriend> lists = (List<PlayerFriend>)CacheService.getFromCache(CacheConstant.PLAYER_FRIEND + playerId);
		if(lists == null){
			lists = friendDAO.listPlayerFriend(playerId);
			CacheService.putToCache(CacheConstant.PLAYER_FRIEND+playerId, lists);
		}
		return lists;
	}

	/**
	 * 删除一条申请消息
	 */
	private void deletePlayerApply(long playerId, long deletePlayerId) {		
		List<PlayerApply> lists = this.listPlayerApplys(playerId);
		lists.remove(this.getPlayerApply(playerId, deletePlayerId));
	}		

	/**
	 * 获取一条申请消息
	 */
	private PlayerApply getPlayerApply(long playerId, long applyPlayerId) {		
		List<PlayerApply> lists = this.listPlayerApplys(playerId);
		if(lists == null)return null;
		for(PlayerApply model:lists){
			if(model.getApplyPlayerId() == applyPlayerId) return model;
		}		
		return null;	
	}

	/**
	 * 获取一条好友信息
	 */
	private PlayerFriend getPlayerFriend(long playerId, long friendPlayerId) {		
		List<PlayerFriend> lists = this.listPlayerFriend(playerId);
		if(lists == null)return null;
		for(PlayerFriend model:lists){
			if(model.getFriendPlayerId() == friendPlayerId) return model;
		}		
		return null;	
	}	
	
	
	@Override
	public PlayerApply addPlayerFriend(long playerId, long applyPlayerId)throws Exception{
		if(playerId < 1 || applyPlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();	
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIEND)) {	
			if(playerId == applyPlayerId) throw new GameException(ExceptionConstant.FRIEND_1803);
			
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(applyPlayerId);
			if(playerOptional.getIsAcceptApply() == 1) throw new GameException(ExceptionConstant.FRIEND_1805);
			
			//申请消息上限需配置		
			List<PlayerApply> lists = this.listPlayerApplys(applyPlayerId);
			int FRIEND_APPLY_LIST_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FRIEND_APPLY_LIST_LIMIT);
			if (lists.size() > FRIEND_APPLY_LIST_LIMIT)  throw new GameException(ExceptionConstant.FRIEND_1804);
			
						
			Player player = playerService.getPlayerByID(playerId);		
			if(player == null) throw new GameException(ExceptionConstant.PLAYER_1111);
			
			//判断接受人消息列表里面是否已有申请消息			
			PlayerApply playerApply = this.getPlayerApply(applyPlayerId, playerId);
			if(playerApply != null){
				playerApply.setApplyTime(DateService.getCurrentUtilDate().getTime());
				return playerApply;
			} else{
				playerApply = new PlayerApply();
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);			
				playerApply.setPlayerId(applyPlayerId);
				playerApply.setApplyPlayerId(playerId);
				playerApply.setApplyPlayerName(player.getPlayerName());
				playerApply.setApplyPlayerCareer(player.getCareer());
				playerApply.setApplyPlayerLevel(playerProperty.getLevel());			
				playerApply.setApplyTime(DateService.getCurrentUtilDate().getTime());
				this.listPlayerApplys(applyPlayerId).add(playerApply);
				return playerApply;
			}			
	   }		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerApply> listPlayerApplys(long playerId) {		
		List<PlayerApply> lists = (List<PlayerApply>)CacheService.getFromCache(CacheConstant.PLAYER_APPLY + playerId);
		if(lists == null){
			lists = Collections.synchronizedList(new ArrayList<PlayerApply>());
			CacheService.putToCache(CacheConstant.PLAYER_APPLY+playerId, lists);
		}
		return lists;		
	}
	
	/**
	 * 同步缓存更新
	 */	
	private void updatePlayerFriend(PlayerFriend playerFriend){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_FRIEND);
		if (!lists.contains(playerFriend)) {
			lists.add(playerFriend);
		}
	}
	
	@Override
	public void applyDeal(long playerId, long applyPlayerId, int state) throws Exception {
		if(playerId < 1 || applyPlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();			
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIEND)) {
			//消息是否已处理
			PlayerApply playerApply = this.getPlayerApply(playerId, applyPlayerId);
			//消息已处理，数据为空
			if (playerApply == null) return;
			
			//玩家拒绝申请
			if (state == 0){				
				/**删除申请消息*/			
				this.deletePlayerApply(playerId, applyPlayerId);					
				return;
			}				
			int PLAYER_FRIEND_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.PLAYER_FRIEND_LIMIT);	
			
			//接受人的好友是否已达上限			
			if (this.listPlayerFriend(playerId).size() > PLAYER_FRIEND_LIMIT) throw new GameException(ExceptionConstant.FRIEND_1800);

			//申请人的好友是否已达上限						
			if (this.listPlayerFriend(applyPlayerId).size() > PLAYER_FRIEND_LIMIT) throw new GameException(ExceptionConstant.FRIEND_1801);			
			
			//双方是否已是好友
			if(this.isFriend(playerId, applyPlayerId)){
				this.deletePlayerApply(playerId, applyPlayerId);
				throw new GameException(ExceptionConstant.FRIEND_1802);
			}
			
			PlayerFriend playerFriend = new PlayerFriend();		
			playerFriend.setId(IDUtil.geneteId(PlayerFriend.class));
			playerFriend.setPlayerId(playerId);
			playerFriend.setFriendPlayerId(applyPlayerId);
			playerFriend.setType(1);	
			friendDAO.createPlayerFriend(playerFriend);
			this.listPlayerFriend(playerId).add(playerFriend);			
			
			PlayerFriend applyFriend = new PlayerFriend();	
			applyFriend.setId(IDUtil.geneteId(PlayerFriend.class));
			applyFriend.setPlayerId(applyPlayerId);
			applyFriend.setFriendPlayerId(playerId);
			applyFriend.setType(1);
			friendDAO.createPlayerFriend(applyFriend);
			this.listPlayerFriend(applyPlayerId).add(applyFriend);
			
			/**删除申请消息*/			
			this.deletePlayerApply(playerId, applyPlayerId);
			
			// 任务触发
			List<Integer> conditionList = new ArrayList<Integer>();		
			conditionList.add(1);
			serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_19, conditionList);			
			
			serviceCollection.getTaskService().executeTask(applyPlayerId, TaskConstant.TYPE_19, conditionList);
		}
	}
		
	
	@Override
	public List<Long> getFriendList(long playerId, int type) {
		if(playerId < 1 || type < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIEND)) {			
			List<Long>  friendIds = new ArrayList<Long>();
			
			if(type == PlayerConstant.FRIEND_TYPE_3){
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
				
				/**推荐玩家*/
				List<Long> onlineList = gameSocketService.getOnLinePlayerIDList();
				
				List<Long> copyIds = new ArrayList<Long>(onlineList);				
				Collections.shuffle(copyIds);
				
				for(long onlinePlayerId : copyIds){		
					if(playerId == onlinePlayerId) continue;
					
					PlayerProperty onlinePlayerProperty = playerService.getPlayerPropertyById(onlinePlayerId);
					
					// 好友等级相差范围需配置	
					if (onlinePlayerProperty.getLevel() < playerProperty.getLevel() - 10) continue;
					if (onlinePlayerProperty.getLevel() > playerProperty.getLevel() + 10) continue;
					
					//过滤已在玩家申请消息列表里面的玩家					
					if (this.getPlayerApply(playerId, onlinePlayerId) != null) continue;
					
					//过滤已在玩家好友列表里面的玩家		
					PlayerFriend playerFriend = this.getPlayerFriend(playerId, onlinePlayerId);
					if (playerFriend != null && playerFriend.getDeleteFlag() != 1) continue;
					
					friendIds.add(onlinePlayerId);		
					
					if(friendIds.size() >= 5){
						break;
					}
				}
				
				if(friendIds.size() < 5){
					Collections.shuffle(copyIds);
					for(long onlinePlayerId : copyIds){		
						if(playerId == onlinePlayerId) continue;

						//过滤已在玩家申请消息列表里面的玩家					
						if (this.getPlayerApply(playerId, onlinePlayerId) != null) continue;
						
						//过滤已在玩家好友列表里面的玩家		
						PlayerFriend playerFriend = this.getPlayerFriend(playerId, onlinePlayerId);
						if (playerFriend != null && playerFriend.getDeleteFlag() != 1) continue;
						
						if(friendIds.contains(onlinePlayerId)) continue;
						
						friendIds.add(onlinePlayerId);		
						
						if(friendIds.size() >= 5){
							break;
						}
					}
				}

				return friendIds;				
			}
			
			//根据好友类型返回数据
			List<PlayerFriend> lists = this.listPlayerFriend(playerId);	
			if (lists == null) return null;
			for(PlayerFriend model : lists){
				if (model.getType() == type){					
					friendIds.add(model.getFriendPlayerId());
				}
			}			
			return friendIds;
		}
	}

	@Override
	public void deleteFriend(long playerId, long deletePlayerId){
		if(playerId < 1 || deletePlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIEND)) {
			//删除好友列表里面的玩家数据
			PlayerFriend playerModel = this.getPlayerFriend(deletePlayerId, playerId);
			if(playerModel == null) return;			
			if(playerModel.getDeleteFlag() == 1) return;			
			playerModel.setDeleteFlag(1);			
			this.updatePlayerFriend(playerModel);			
			this.listPlayerFriend(deletePlayerId).remove(playerModel);
			
			//删除玩家列表里面的好友数据
			PlayerFriend deleteModel = this.getPlayerFriend(playerId, deletePlayerId);
			if(deleteModel == null) return;			
			if(deleteModel.getDeleteFlag() == 1) return;			
			deleteModel.setDeleteFlag(1);
			this.updatePlayerFriend(deleteModel);			
			this.listPlayerFriend(playerId).remove(deleteModel);			
		}		
	}	
	
	
	@Override
	public void deleteAllApply(long playerId){
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIEND)) {
			this.listPlayerApplys(playerId).clear();
		}
	}

	@Override
	public void agreeAllApply(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FRIEND)) {
			List<PlayerApply> playerApplylists = this.listPlayerApplys(playerId);
			
			List<Long> playerIds = new ArrayList<Long>();			 
			for(PlayerApply playerApply : playerApplylists){
				playerIds.add(playerApply.getApplyPlayerId());
			}
			
			this.autoAddFriend(playerId, playerIds);
			
			this.listPlayerApplys(playerId).clear();
			
			List<PlayerApply> applyList = this.listPlayerApplys(playerId);
			
			S_ApplyMsgList.Builder builder = S_ApplyMsgList.newBuilder();
			for(PlayerApply playerApply : applyList){
				ApplyMsg.Builder applyMsgMsg = protoBuilderService.builderApplyMsg(playerApply);
				builder.addApplyMsgList(applyMsgMsg);			
			}		
			MessageObj msg = new MessageObj(MessageID.S_ApplyMsgList_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);		
		}
		
	}	

	@Override
	public Player searchFriend(String playerName){
		if (playerName == null || playerName.trim().equals("")) return null;
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		
		return playerService.getPlayerByName(playerName);
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_FRIEND+playerId);
	}

	@Override
	public void quartzDeleteFriends() {
		friendDAO.quartzDelete();
	}	
	
	/** 自动添加家族成员为好友 */
	public void autoAddFriend(long playerId, List<Long> playerIds) {	
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		
		int PLAYER_FRIEND_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.PLAYER_FRIEND_LIMIT);	
		
		for(Long applyPlayerId : playerIds){
			if(applyPlayerId.equals(playerId)) continue;
			if(this.isFriend(playerId, applyPlayerId)) continue;
			
			//接受人的好友是否已达上限			
			if (this.listPlayerFriend(playerId).size() > PLAYER_FRIEND_LIMIT) return;
			//申请人的好友是否已达上限						
			if (this.listPlayerFriend(applyPlayerId).size() > PLAYER_FRIEND_LIMIT) continue;			
			
			PlayerFriend playerFriend = new PlayerFriend();	
			playerFriend.setId(IDUtil.geneteId(PlayerFriend.class));
			playerFriend.setPlayerId(playerId);
			playerFriend.setFriendPlayerId(applyPlayerId);
			playerFriend.setType(1);	
			friendDAO.createPlayerFriend(playerFriend);
			this.listPlayerFriend(playerId).add(playerFriend);			
			
			PlayerFriend applyFriend = new PlayerFriend();	
			applyFriend.setId(IDUtil.geneteId(PlayerFriend.class));
			applyFriend.setPlayerId(applyPlayerId);
			applyFriend.setFriendPlayerId(playerId);
			applyFriend.setType(1);
			friendDAO.createPlayerFriend(applyFriend);
			this.listPlayerFriend(applyPlayerId).add(applyFriend);		
			
			try {
				// 任务触发
				List<Integer> conditionList = new ArrayList<Integer>();				
				conditionList.add(1);
				serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_19, conditionList);
			
				serviceCollection.getTaskService().executeTask(applyPlayerId, TaskConstant.TYPE_19, conditionList);
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}

	/**
	 * 是否已是好友判断
	 * 返回 boolean
	 */
	public Boolean isFriend(long playerId, long applyPlayerId){		
		List<PlayerFriend> lists = this.listPlayerFriend(playerId);
		for(PlayerFriend model:lists){
			if(model.getFriendPlayerId() == applyPlayerId && model.getDeleteFlag() == 0) return true;
		}
		return false;		
	}

	@Override
	public void setIsAcceptApply(long playerId, int state) {
		IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		
		playerOptional.setIsAcceptApply(state);	
		playerService.updatePlayerOptional(playerOptional);		
	}
}
