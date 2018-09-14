package com.service;

import java.util.List;
import java.util.Map;

import com.domain.ServerConf;

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
	 * 运营商对应服务器列表
	 */
	public Map<String, List<ServerConf>> scListToMap(List<ServerConf> serverConfList, int gameID);
	
	/**
	 * 获取服务器站点列表
	 */
	public List<String> getGameSites(String gameSiteStr, String agentStr);
	
	/**
	 * 获取服务器站点列表
	 */
	public List<ServerConf> getServerConfs(String gameSiteStr, String agentStr);
}
