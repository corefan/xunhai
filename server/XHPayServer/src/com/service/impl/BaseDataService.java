package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.CacheService;
import com.constant.CacheConstant;
import com.dao.BaseDataDAO;
import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;
import com.service.IBaseDataService;

/**
 * @author ken
 * 2014-3-24
 * 基础数据
 */
public class BaseDataService implements IBaseDataService {

	private BaseDataDAO baseDataDAO = new BaseDataDAO();

	public void initData() {
		
		Map<String,BaseAgentConfig> agentMap = new HashMap<String,BaseAgentConfig>();
		List<BaseAgentConfig> agents = baseDataDAO.listBaseAgentConfigs();
		for(BaseAgentConfig model : agents){
			agentMap.put(model.getAgent(), model);
		}
		CacheService.putToCache(CacheConstant.B_AGENT_CONFIG_CACHE, agentMap);
		
		
		Map<String,BaseServerConfig> serverMap = new HashMap<String, BaseServerConfig>();
		List<BaseServerConfig> servers = baseDataDAO.listBaseServerConfigs();
		for(BaseServerConfig model : servers){
			serverMap.put(model.getGameSite(), model);
		}
		CacheService.putToCache(CacheConstant.B_SERVER_CONFIG_CACHE, serverMap);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseAgentConfig getBaseAgentConfig(String agent) {
		Map<String,BaseAgentConfig> agentMap = (Map<String,BaseAgentConfig>)CacheService.getFromCache(CacheConstant.B_AGENT_CONFIG_CACHE);
		
		BaseAgentConfig model = agentMap.get(agent);
		if(model == null){
			model = baseDataDAO.getBaseAgentConfig(agent);
			if(model != null){
				agentMap.put(agent, model);
			}
		}
		return model;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseServerConfig getBaseServerConfig(String gameSite) {
		Map<String,BaseServerConfig> serverMap = (Map<String,BaseServerConfig>)CacheService.getFromCache(CacheConstant.B_SERVER_CONFIG_CACHE);
		
		BaseServerConfig model = serverMap.get(gameSite);
		if(model == null){
			model = baseDataDAO.getBaseServerConfig(gameSite);
			if(model != null){
				serverMap.put(gameSite, model);
			}
		}
		return model;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseServerConfig getBaseServerConfigByServerNo(int serverNo) {
		Map<String,BaseServerConfig> serverMap = (Map<String,BaseServerConfig>)CacheService.getFromCache(CacheConstant.B_SERVER_CONFIG_CACHE);
		
		for(Map.Entry<String, BaseServerConfig> entry : serverMap.entrySet()){
			BaseServerConfig model = entry.getValue();
			if(model.getServerNo() == serverNo){
				return model;
			}
		}
		BaseServerConfig model = baseDataDAO.getBaseServerConfigByServerNo(serverNo);
		if(model != null){
			serverMap.put(model.getGameSite(), model);
		}
		return model;
		
	}
}
