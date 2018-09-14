package com.service;

import java.util.Map;

import com.domain.epigraph.PlayerWeaponEffect;

/**
 * 铭文系统
 * @author ken
 * @date 2018年7月11日
 */
public interface IEpigraphService {
	
	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 清除缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 铭文洗练
	 * @return 
	 */
	public void epigraph(long playerId, long playerEquipmentID, int holeId, long playerBagId) throws Exception;
	
	/**
	 * 更新玩家的铭文信息
	 * @param equipEpigraphMap 改变的装备铭文map
	 * @param state 改变的铭文的状态
	 */
	public void clearPlayerWeaponEffect(long playerId);
	
	/**
	 *获取玩家的铭文信息
	 * @param equipEpigraphMap 改变的装备铭文map
	 * @param state 改变的铭文的状态
	 */
	public Map<Integer, PlayerWeaponEffect> getPlayerWeaponEffectMap(long playerId);
	
}