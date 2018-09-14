package com.service;

import java.util.List;
import java.util.Map;

import com.domain.player.PlayerProperty;

/**
 * 玩家属性变动
 * @author ken
 * @date 2017-2-16
 */
public interface IPropertyService {

	/**
	 * 加减属性
	 * @param addProList  变动属性值
	 * @param sign 1为增加  -1为扣除
	 * @param offer 是否同步客户端
	 * @param offerBattleValue 是否同步战力
	 */
	void addProValue(long playerId, List<List<Integer>> addProList, int sign,  boolean offer, boolean offerBattleValue);
	
	/**
	 * 加减属性
	 * @param addProMap  变动属性值
	 * @param offer 是否同步客户端
	 * @param offerBattleValue 是否同步战力
	 */
	void addProValue(long playerId, Map<Integer, Integer> addProMap, boolean offer, boolean offerBattleValue);
	
	/**
	 * 计算战力
	 */
	void calTotalBattleValue(PlayerProperty playerProperty);
	
	/**
	 * 同步战力
	 */
	void synBattleValue(PlayerProperty playerProperty);
}
