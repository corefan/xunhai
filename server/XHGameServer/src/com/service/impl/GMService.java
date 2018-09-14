package com.service.impl;

import java.util.List;

import com.cache.CacheService;
import com.constant.CacheConstant;
import com.dao.player.PlayerDAO;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.service.IGMService;

/**
 * gm处理
 * @author ken
 * @date 2017-7-24
 */
public class GMService implements IGMService {
	
	private PlayerDAO playerDAO = new PlayerDAO();
	
	@Override
	public Player getPlayerByName_GM(String playerName) {
		return playerDAO.getPlayerByPlayerName_GM(playerName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> listPlayerByUserId_GM(long userId) {
		List<Player> lists = (List<Player>) CacheService.getFromCache(CacheConstant.ACCOUNT_PLAYER_CACHE + userId);
		if(lists == null){
			lists = playerDAO.listPlayerByUserId_GM(userId);
		}
		return lists;
	}

	@Override
	public Player getPlayerByID_GM(long playerId) {
		Player player = (Player) CacheService.getFromCache(CacheConstant.PLAYER_CACHE + playerId);
		if(player == null){
			player = playerDAO.getPlayerByPlayerId_GM(playerId);
		}
		return player;
	}

	@Override
	public PlayerProperty getPlayerPropertyById_GM(long playerId) {
		PlayerProperty playerProperty = (PlayerProperty) CacheService
				.getFromCache(CacheConstant.PLAYER_PROPERTY_CACHE + playerId);
		if (playerProperty == null) {
			playerProperty = playerDAO.getPlayerPropertyById_GM(playerId);
		}
		return playerProperty;
	}

	@Override
	public PlayerExt getPlayerExtById_GM(long playerId) {
		PlayerExt playerExt = (PlayerExt) CacheService
				.getFromCache(CacheConstant.PLAYER_EXT_CACHE + playerId);
		if (playerExt == null) {
			playerExt = playerDAO.getPlayerExtById_GM(playerId);
		}
		return playerExt;
	}

	@Override
	public PlayerWealth getPlayerWealthById_GM(long playerId) {
		PlayerWealth playerWealth = (PlayerWealth) CacheService.getFromCache(CacheConstant.PLAYER_WEALTH_CACHE + playerId);
		if (playerWealth == null) {
			playerWealth = playerDAO.getPlayerWealthById_GM(playerId);
		}
		return playerWealth;
	}

	@Override
	public List<PlayerExt> listPlayerIdByExitTime(int day) {
		return playerDAO.getActivePlayerExtList(day);
	}

}
