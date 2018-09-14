package com.service;

import java.util.Map;

import com.domain.wing.PlayerWing;


public interface IWingService {
	/** 初始数据*/
	void initBaseWing();
	
	/** 调度删除缓存*/
	void deleteCache(long playerId);
	
	/** 穿戴羽翼*/
	void putOnWing(long playerId, int wingId) throws Exception;	
	
	/** 卸载羽翼*/
	void putDownWing(long playerId, int wingId) throws Exception;	
	
	/** 羽化*/
	void evolve(long playerId, int type, int wingId, int itemId) throws Exception;	
	
	/** 添加翅膀*/
	void addWing_syn(long playerId, int wingId) throws Exception;
	
	/** 获取玩家羽翼*/
	Map<Integer, PlayerWing> getPlayerWingMap(long playerId);
	
	/** 羽翼降解*/
	void unEvolve(long playerId, int wingId) throws Exception;	
}
