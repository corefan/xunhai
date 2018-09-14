package com.service;

import java.util.List;
import java.util.Map;

import com.domain.bag.BaseEquipment;
import com.domain.bag.PlayerEquipment;

/**
 * 装备系统
 * @author ken
 * @date 2017-1-4
 */
public interface IEquipmentService {

	/**
	 * 初始配置数据
	 */
	void initBaseCache();
	
	/**
	 * 初始缓存
	 */
	void initCache();
	
	/**
	 * 取装备配置根据装备基础编号
	 */
	BaseEquipment getBaseEquipmentById(int equipmentId);
	
	/**
	 * 获取pk掉落概率
	 */
	int getPkDropRate(int posId, int nameColor);
	
	/**
	 * 根据编号获得玩家装备(玩家缓存)
	 */
	PlayerEquipment getPlayerEquipmentByID(long playerId, long playerEquipmentID);
	
	/**
	 * 获得玩家装备列表
	 */
	List<PlayerEquipment> getPlayerEquipmentList(long playerId);	

	/**
	 * 穿装备
	 */
	void putOnEquipment(long playerId, long onEquipmentID)throws Exception;
	
	/**
	 * 脱装备
	 */
	void putDownEquipment(long playerId, long downEquipmentID)throws Exception;
	
	/**
	 * 更新玩家装备
	 */
	void updatePlayerEquipment(PlayerEquipment playerEquipment);
	
	/**
	 * 删除玩家装备
	 */
	PlayerEquipment deletePlayerEquipmentByID(long playerId, long playerEquipmentId);
	
	/**
	 * 创建玩家装备
	 */
	PlayerEquipment createPlayerEquipment(long playerId, Integer equipmentID, int isBinding);
	
	/**
	 * 根据状态获得玩家装备列表
	 */
	List<PlayerEquipment> getPlayerEquipmentListByState(long playerId, int state);
	
	/**
	 * 删除玩家装备缓存
	 */
	void deletePlayerEquipmentCacheInfo(long playerId);
	
	/**
	 * 调度删除玩家装备
	 */
	void quartzDeletePlayerEquipment();
	
	/**
	 * 判断玩家某装备位上是否已穿戴装备
	 */
	boolean isTakeEquipment(long playerId, int posId);
	
	/**
	 * 处理装备对玩家的属性变化
	 */
	Map<Integer, Integer> handleInfoWhenChangeEquipment(PlayerEquipment playerEquipment, int sign, boolean offerBattleValue);
	
	/**
	 * 加入掉落装备缓存
	 */
	void addDropEquipmentCache(PlayerEquipment playerEquipment);
	
	/**
	 * 移除掉落装备缓存
	 */
	PlayerEquipment removeDropEquipmentCache(long playerEquipmentId);
	
	/**
	 * 删除掉落装备
	 */
	void deleteDropEquipment(long playerEquipmentId);
	
	/**
	 *  根据属性算评分（装备和羽翼）
	 */
	int calculateScore(Map<Integer, Integer> addProMap);
	
	/**********************************锻造****************************/
	
	/**
	 * 强化装备
	 */
	int strongEquip(long playerId, long playerEquipId, int luckItem) throws Exception;
	
	/**
	 * 合成装备
	 */
	void composeEquip(long playerId, List<Long> playerEquipIds) throws Exception;
	
	/**
	 * 传承装备
	 */
	void inheritEquip(long playerId, long playerEquipId, long targetEquipId) throws Exception;
}
