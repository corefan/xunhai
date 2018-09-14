package com.service;

import com.domain.base.BaseNewRole;
import com.domain.base.BaseProperty;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;

/**
 * 全局配置系统
 * @author ken
 * @date 2016-12-29
 */
public interface ICommonService {

	/**
	 * 初始缓存数据
	 */
	void initBaseCache();
	
	/**
	 * 玩家初始表
	 */
	BaseNewRole getBaseNewRole(int career);
	
	/**
	 * 取升级配置
	 */
	BaseProperty getBaseProperty(int career, int level);
	
	/**
	 * 参数编号取参数值
	 */
	int getConfigValue(int codeId);
	
	/**
	 * 初始裸体属性
	 */
	void dealInitProperty(PlayerProperty playerProperty, PlayerExt playerExt, PlayerWealth playerWealth, int career);
	
	/**
	 * 发送提示消息
	 */
	void sendNoticeMsg(long playerId, String msg);
	
}
