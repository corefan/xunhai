package com.service;

import java.util.List;
import java.util.Map;

import com.domain.wakan.BaseAwake;
import com.domain.wakan.BaseWakan;
import com.domain.wakan.PlayerWakan;


/**
 * 注灵系统
 * @author jiangqin
 * @date 2017-2-16
 */
public interface IWakanService {
	/**
	 * 初始化配置表
	 */
	void initBaseCache();

	/**
	 * 获取玩家装备位注灵数据
	 */
	 Map<Integer, PlayerWakan> getPlayerWakanMap(long playerId);
	 
	/**
	 * 获取玩家装某备位注灵数据
	 */
	PlayerWakan getPlayerWakanByPosId(long playerId, int posId);
	 
	 /**
	  * 注灵
	  */
	void takeWakan(long playerId, int posId, List<Integer> itemList) throws Exception;
    
    /**
	 * 删除缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 获取注灵基础信息
	 */
	BaseWakan getBaseWakan(int posId, int level);
	
	/**
	 * 根据玩家穿戴或卸下装备，改变玩家属性值
	 */
	void changeProValueByEquipment(long playerId, int posId, int sign);
	
	/**获取玩家觉醒数据**/
	public BaseAwake getBaseAwake(long playerId);

}
