package com.service;

import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;

/**
 * @author ken
 * 2014-3-24
 * 基础数据
 */
public interface IBaseDataService {

	/**
	 * 初始化基础数据
	 * */
	public void initData(); 
	
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
	
}
