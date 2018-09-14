package com.service;

import java.util.concurrent.BlockingQueue;

import com.domain.enemy.PlayerEnemy;


public interface IEnemyService {	
	
	/** 获取仇敌列表*/
	BlockingQueue<PlayerEnemy> getPlayerEnemyList(long playerId);	
	
	/** 删除仇敌*/
	void deleteEnemy(long playerId, long enemyPlayerId) throws Exception;	
	
	/** 追踪仇敌*/
	void trackEnemy(long playerId, long enemyPlayerId) throws Exception;	
	
	/** 添加仇敌*/
	void addEnemy(long playerId, long enemyPlayerId);
	
	/** 调度删除缓存*/
	void deleteCache(long playerId);
	
	/** 调度清理无效数据*/
	void quartzDeleteEnemys();
}

