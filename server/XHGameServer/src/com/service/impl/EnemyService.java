package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.dao.enemy.PlayerEnemyDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.enemy.PlayerEnemy;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.message.EnemyProto.S_TrackEnemy;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IEnemyService;
import com.service.IPlayerService;
import com.service.IRewardService;
import com.util.IDUtil;


public class EnemyService implements IEnemyService{
	private PlayerEnemyDAO playerEnemyDAO = new PlayerEnemyDAO();


	@Override
	public void deleteEnemy(long playerId, long enemyPlayerId) throws Exception {
		if(playerId <= 0 || enemyPlayerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ENEMY)) {
			 PlayerEnemy playerEnemy = this.getPlayerEnemy(playerId, enemyPlayerId);			 
			 if(playerEnemy == null) throw new GameException(ExceptionConstant.EMEMY_3200);
			 playerEnemy.setDeleteFlag(1);		 
			 this.updateEnemy(playerEnemy);		 
		}
	}

	@Override
	public void trackEnemy(long playerId, long enemyPlayerId) throws Exception {
		if(playerId <= 0 || enemyPlayerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		IRewardService rewardService = serviceCollection.getRewardService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ENEMY)) {
			 PlayerEnemy playerEnemy = this.getPlayerEnemy(playerId, enemyPlayerId);	
			 if(playerEnemy == null) throw new GameException(ExceptionConstant.EMEMY_3200);
			 if(!gameSocketService.checkOnLine(enemyPlayerId)) throw new GameException(ExceptionConstant.PLAYER_1111);
			 
			 // 消耗物品追魂香
			 int PLAYER_ENEMG_2 = serviceCollection.getCommonService().getConfigValue(ConfigConstant.PLAYER_ENEMG_2);				
			 rewardService.deductItem(playerId, PLAYER_ENEMG_2, 1, true);				 
			 IPlayerService playerService = serviceCollection.getPlayerService();
			 PlayerExt playerExt = playerService.getPlayerExtById(enemyPlayerId);
			 Player player = playerService.getPlayerByID(enemyPlayerId);
			 
			 S_TrackEnemy.Builder builder = S_TrackEnemy.newBuilder();
			 builder.setMapId(playerExt.getMapId());
			 builder.setPlayerName(player.getPlayerName());			
			 MessageObj msg = new MessageObj(MessageID.S_TrackEnemy_VALUE, builder.build().toByteArray());
			 gameSocketService.sendDataToPlayerByUserId(player.getUserId(), msg);
		}
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_ENEMY+playerId);			
	}
	
	@Override
	public void quartzDeleteEnemys() {
		playerEnemyDAO.quartzDelete();
	}	

	/** 同步缓存更新 */	
	private void updateEnemy(PlayerEnemy playerEnemy){
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_ENEMY);
		if (!lists.contains(playerEnemy)) {
			lists.add(playerEnemy);
		}
	}	
	
	@SuppressWarnings("unchecked")
	public BlockingQueue<PlayerEnemy> getPlayerEnemyList(long playerId){
		BlockingQueue<PlayerEnemy> playerEnemyList = (BlockingQueue<PlayerEnemy>)CacheService.getFromCache(CacheConstant.PLAYER_ENEMY + playerId);
		 if(playerEnemyList == null){
			 playerEnemyList = new LinkedBlockingQueue<PlayerEnemy>();
			 List<PlayerEnemy> list = playerEnemyDAO.listPlayerEnemy(playerId);			 
			 playerEnemyList.addAll(list);
			 
			 CacheService.putToCache(CacheConstant.PLAYER_ENEMY + playerId, playerEnemyList);
		 }
		 return playerEnemyList;
	}
	
	/** 添加仇敌*/
	public void addEnemy(long playerId, long enemyPlayerId){
		if(playerId <= 0 || enemyPlayerId <= 0) return;
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		// 仇敌是否已在玩家的列表中
		PlayerEnemy playerEnemy = this.getPlayerEnemy(playerId, enemyPlayerId);
		BlockingQueue<PlayerEnemy> playerEnemyList = this.getPlayerEnemyList(playerId);		
		List<PlayerEnemy> changeList = new ArrayList<PlayerEnemy>();		
		if(playerEnemy != null){
			playerEnemyList.remove(playerEnemy);
			playerEnemy.setAddTime(System.currentTimeMillis());
			this.updateEnemy(playerEnemy);				
		}else{
			playerEnemy = new PlayerEnemy();
			playerEnemy.setId(IDUtil.geneteId(PlayerEnemy.class));
			playerEnemy.setPlayerId(playerId);
			playerEnemy.setEnemyPlayerId(enemyPlayerId);
			playerEnemy.setDeleteFlag(0);
			playerEnemy.setAddTime(System.currentTimeMillis());
			playerEnemyDAO.createPlayerEnemy(playerEnemy);
		}		
		changeList.add(playerEnemy);

		// 上限
		int PLAYER_ENEMG_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.PLAYER_ENEMG_LIMIT);		 
		if(playerEnemyList.size() > PLAYER_ENEMG_LIMIT){
			PlayerEnemy removePlayerEnemy = playerEnemyList.poll();	
			changeList.add(removePlayerEnemy);			
			
			removePlayerEnemy.setDeleteFlag(1);
			
			this.updateEnemy(removePlayerEnemy);
		}
		
		// 添加仇敌
		playerEnemyList.add(playerEnemy);	
	}
	
	
	/** 获取玩家某个仇敌的信息*/
	private PlayerEnemy getPlayerEnemy(long playerId, long enemyPlayerId){
		BlockingQueue<PlayerEnemy> playerEnemyList = this.getPlayerEnemyList(playerId);
		for(PlayerEnemy playerEnemy : playerEnemyList){
			if (playerEnemy.getEnemyPlayerId() == enemyPlayerId){
				return playerEnemy;
			}
		}
		return null;
	}
}
