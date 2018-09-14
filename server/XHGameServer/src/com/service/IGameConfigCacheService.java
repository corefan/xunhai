package com.service;

import java.util.List;

import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;

/**
 * 游戏配置
 * @author ken
 * @date 2016-12-20
 */
public interface IGameConfigCacheService {
	/**
	 * 初始基础缓存
	 */
	void initBaseCache();
	
	/**
	 * 取平台配置
	 */
	BaseAgentConfig getBaseAgentConfig(String agent);
	
	/**
	 * 取服务器配置 根据服务器站点
	 */
	BaseServerConfig getBaseServerConfig(String gameSite);

	/**
	 * 取服务器配置 根据服务器编号
	 */
	BaseServerConfig getBaseServerConfigByServerNo(int serverNo);
	
	/**
	 * 取当前服务器的合服列表
	 */
	List<BaseServerConfig> listMergeServers();
	
	/**
	 * 更新维护结束时间
	 */
	void updateEndStopDate(String endStopDate);
}
