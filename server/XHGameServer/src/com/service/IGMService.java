package com.service;

import java.util.List;

import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;

/**
 * gm处理
 * @author ken
 * @date 2017-7-24
 */
public interface IGMService {
	
	/**
	 * 根据玩家名字去找玩家数据
	 */
	Player getPlayerByName_GM(String playerName);
	
	/**
	 * 获取账号角色缓存
	 */
	List<Player> listPlayerByUserId_GM(long userId);
	
	/**
	 * 根据玩家编号去玩家数据
	 */
	Player getPlayerByID_GM(long playerId);
	
	/**
	 * 根据玩家编号取属性信息
	 */
	PlayerProperty getPlayerPropertyById_GM(long playerId);
	
	/**
	 * 根据玩家编号取扩展信息
	 */
	PlayerExt getPlayerExtById_GM(long playerId);
	
	/**
	 * 获取玩家财富
	 */
	PlayerWealth getPlayerWealthById_GM(long playerId);
	
	
	/**
	 * 取N天内上线过的玩家编号
	 */
	List<PlayerExt> listPlayerIdByExitTime(int day);
}
