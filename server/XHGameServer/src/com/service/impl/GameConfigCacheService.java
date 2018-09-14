package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cache.BaseCacheService;
import com.common.Config;
import com.constant.CacheConstant;
import com.dao.GameConfigDao;
import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;
import com.service.IGameConfigCacheService;

/**
 * 游戏配置
 * @author ken
 * @date 2016-12-20
 */
public class GameConfigCacheService implements IGameConfigCacheService{
	
	private GameConfigDao gcdao = new GameConfigDao();
	
	@Override
	public void initBaseCache() {
		
		Map<String,BaseAgentConfig> agentMap = new HashMap<String,BaseAgentConfig>();
		List<BaseAgentConfig> agents = gcdao.listBaseAgentConfigs();
		for(BaseAgentConfig model : agents){
			agentMap.put(model.getAgent(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.B_AGENT_CONFIG_CACHE, agentMap);
		
		
		Map<String,BaseServerConfig> serverMap = new HashMap<String, BaseServerConfig>();
		List<BaseServerConfig> servers = gcdao.listBaseServerConfigs();
		for(BaseServerConfig model : servers){
			serverMap.put(model.getGameSite(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.B_SERVER_CONFIG_CACHE, serverMap);
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseAgentConfig getBaseAgentConfig(String agent) {
		Map<String,BaseAgentConfig> agentMap = (Map<String,BaseAgentConfig>)BaseCacheService.getFromBaseCache(CacheConstant.B_AGENT_CONFIG_CACHE);
		
		BaseAgentConfig model = agentMap.get(agent);
		if(model == null){
			model = gcdao.getBaseAgentConfig(agent);
			if(model != null){
				agentMap.put(agent, model);
			}
		}
		return model;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseServerConfig getBaseServerConfig(String gameSite) {
		Map<String,BaseServerConfig> serverMap = (Map<String,BaseServerConfig>)BaseCacheService.getFromBaseCache(CacheConstant.B_SERVER_CONFIG_CACHE);
		
		BaseServerConfig model = serverMap.get(gameSite);
		if(model == null){
			model = gcdao.getBaseServerConfig(gameSite);
			if(model != null){
				serverMap.put(gameSite, model);
			}
		}
		return model;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseServerConfig getBaseServerConfigByServerNo(int serverNo) {
		Map<String,BaseServerConfig> serverMap = (Map<String,BaseServerConfig>)BaseCacheService.getFromBaseCache(CacheConstant.B_SERVER_CONFIG_CACHE);
		
		for(Map.Entry<String, BaseServerConfig> entry : serverMap.entrySet()){
			BaseServerConfig model = entry.getValue();
			if(model.getServerNo() == serverNo){
				return model;
			}
		}
		BaseServerConfig model = gcdao.getBaseServerConfigByServerNo(serverNo);
		if(model != null){
			serverMap.put(model.getGameSite(), model);
		}
		return model;
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<BaseServerConfig> listMergeServers() {
		List<BaseServerConfig> lists = new ArrayList<BaseServerConfig>();
		Map<String,BaseServerConfig> serverMap = (Map<String,BaseServerConfig>)BaseCacheService.getFromBaseCache(CacheConstant.B_SERVER_CONFIG_CACHE);
		
		for(Map.Entry<String, BaseServerConfig> entry : serverMap.entrySet()){
			BaseServerConfig model = entry.getValue();
			if(model.getGameHost().equals(Config.GAME_HOST) && model.getGamePort() == Config.GAME_PORT){
				lists.add(model);
			}
		}
		
		return lists;
	}


	@Override
	public void updateEndStopDate(String endStopDate) {
		 if(endStopDate == null) return;
		
		 String gameSiteStr = "";
	
		 List<BaseServerConfig> lists = this.listMergeServers();
		 for(BaseServerConfig model : lists){
			 model.setEndStopDate(endStopDate);
			 
			 gameSiteStr += "'" + model.getGameSite() + "',";
		 }
		 
		if (gameSiteStr.length() > 0) {
			gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
		}
		gcdao.updateEndStopDate(endStopDate, gameSiteStr);
		
	}

}
