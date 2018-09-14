package com.service;

import java.util.Map;

import com.domain.market.PlayerMarket;

/**
 * 商城系统
 * @author jiangqin
 * @date 2017-4-18
 */
public interface IMarketService {
	/**
	 * 初始基础表
	 */
	void initBaseCache();
	
	/** 根据类型获取商城信息列表*/
	Map<Integer, PlayerMarket> getPlayerMarketMap(long playerId);
	
	/** 商城购买*/
	int marketBuy(long playerId, int marketId, int num, boolean useFlag) throws Exception;
	
	/** 日结处理 */	
	void quartzDaily();
	
	/** 调度删除缓存*/
	void deleteCache(long playerId);
}
