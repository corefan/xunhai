package com.service;

import java.util.List;

import com.domain.furnace.PlayerFurnace;

/**
 * 熔炉系统
 * @author ken
 * @date 2018年4月23日
 */
public interface IFurnaceService {

	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 清理缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 获取熔炉列表
	 */
	List<PlayerFurnace> getPlayerFurnaceList(long playerId);
	
	/**
	 * 取熔炉记录
	 */
	PlayerFurnace getPlayerFurnace(long playerId, int furnaceId);
	
	/**
	 * 升级熔炉
	 */
	PlayerFurnace upgradeFurnace(long playerId, int furnaceId) throws Exception;
	
	/**
	 * 添加熔炉碎片
	 */
	void addFurnacePiece(long playerId, int furnaceId, int value);
}
