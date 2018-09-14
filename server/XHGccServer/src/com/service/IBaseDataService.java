package com.service;

import java.util.List;
import java.util.Map;

import com.domain.BaseServerConfig;

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
	 * 得到服务器配置列表
	 * */
	public List<String> getServerConfList();
	
	/**
	 * 根据gameSite得到GameSiteVariable
	 * */
	public BaseServerConfig getServerConfByGameSite(String gameSite);
	
	/**
	 * 根据gameSite得到url
	 * */
	public String getUrlByGameSitePath(String gameSite, String path);
	
	/**
	 * 根据节点编号得到节点名称
	 * */
	public String getNameByGameStepID(int stepID);
	
	/**
	 * 取运营商服务器名称列表(合服算一个)
	 * @return
	 */
	Map<String, List<String>> getAgentMap();
	
	/**
	 * 创建服务器
	 * @param model
	 */
	void createServer(BaseServerConfig model)throws Exception;
	
	/**
	 * 更改服务器
	 * @param model
	 */
	void updateServer(BaseServerConfig model, String gameSite)throws Exception;
	
	/**
	 * 删除服务器
	 * @param gameSite
	 */
	void deleteServer(String gameSite)throws Exception;
	
	/**
	 * 获取服务器列表
	 * @return
	 */
	List<BaseServerConfig> getServerList()throws Exception;
}
