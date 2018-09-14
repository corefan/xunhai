package com.service;

import java.util.List;

import com.domain.fashion.PlayerFashion;

/**
 * 时装翅膀
 * @author ken
 * @date 2017-2-13
 */
public interface IFashionService {

	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 清理缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 获取时装列表
	 */
	List<PlayerFashion> getFashionList(long playerId);
	
	/**
	 * 取时装记录
	 */
	PlayerFashion getPlayerFashion(long playerId, int fashionId);
	
	/**
	 * 装备时装
	 */
	void putonFashion(long playerId, int fashionId) throws Exception;
	
	/**
	 * 卸下时装
	 */
	void putdownFashion(long playerId, int fashionId) throws Exception;
	
	/**
	 * 获得时装
	 */
	void addFashion_syn(long playerId, int fashionId) throws Exception;
	
	
	/**
	 * 上线处理时装buff
	 */
	void dealLogin(long playerId);
}
