package com.service;

import java.util.Map;

import com.domain.trading.PlayerTradeBag;


/**
 * 交易行
 * @author jiangqin
 * @date 2017-3-16
 */
public interface ITradeService {	
	
	/**
	 * 初始交易行数据
	 */
	void initCache();
	
	/** 交易行出售*/
	void tradeSell(long playerId, long playerBagId, int num, int price) throws Exception;
	
	/** 交易行购买*/
	void tradeBuy(long playerId, long playerBagId, int num) throws Exception;	
	
	/** 总交易行物品信息*/
	Map<Long, PlayerTradeBag> getALLPlayerTradeBagMap();
	
	/** 获取某个玩家交易行物品信息*/
	Map<Long, PlayerTradeBag> getPlayerTradMap(long playerId);	
	
	/** 根据类型获取总交易行物品信息列表 @param typeId 交易类型id */
	void getTradeListPag(long playerId, int type, int typeId, int satrt, int offset);	
	
	/** 购买物品*/
	void systemItemBuy(long playerId, int itemId, int num) throws Exception;	
	
	/** 扩展货架*/
	int extendGrid(long playerId) throws Exception;
	
	/** 下架*/
	void offShelf(long playerId, long playerTradeBagId) throws Exception;

	/** 删除某个玩家的所有交易背包数据 */
	void removePlayerTradeBag(long playerId);
	
	/** 过期交易物品重新上架 */
	void reUpShelf(long playerId, long playerTradeBagId) throws Exception;	
}
