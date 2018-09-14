package com.service;

import java.util.List;

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
	 * 服务器列表
	 */
	List<BaseServerConfig> listServers();
	
	public BaseAgentConfig getGameConfigConstantByAgent(String agent);
	
}
