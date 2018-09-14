package com.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.CacheService;
import com.constant.CacheConstant;
import com.constant.UserConstant;
import com.dao.BaseDataDAO;
import com.domain.BaseGameStep;
import com.domain.ServerConf;
import com.service.IBaseDataService;
import com.util.DAOFactory;

/**
 * @author ken 2014-3-24 基础数据
 */
public class BaseDataService implements IBaseDataService {

	private BaseDataDAO baseDataDAO = DAOFactory.getBaseDataDAO();

	public void initData() {
		List<ServerConf> serverConfList = baseDataDAO.getServerConfList();
		for (ServerConf conf : serverConfList) {
			conf.setGameID(UserConstant.GAME_ID);
		}
		
		CacheService.putToCache(CacheConstant.B_GAME_LIST, serverConfList);
		
		
		List<BaseGameStep> gameSteps = baseDataDAO.getBaseGameSteps();
		
		CacheService.putToCache(CacheConstant.B_GAME_STEP, gameSteps);
	}
	
	/**
	 * 运营商对应服务器列表
	 */
	public Map<String, List<ServerConf>> scListToMap(List<ServerConf> serverConfList, int gameID) {
		Map<String, List<ServerConf>> map = new HashMap<String, List<ServerConf>>();
		for (ServerConf serverConf : serverConfList) {
			if (serverConf.getGameID() == gameID) {
				String agent = serverConf.getAgent();
				List<ServerConf> serverConfs = map.get(agent);
				if (serverConfs == null) {
					serverConfs = new ArrayList<ServerConf>();
					map.put(agent, serverConfs);
				}
				
				serverConfs.add(serverConf);
			}
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGameSites(String gameSiteStr, String agentStr) {
		List<ServerConf> serverConfList = (List<ServerConf>) CacheService.getFromCache(CacheConstant.B_GAME_LIST);
		
		List<String> servers = new ArrayList<String>();
		
		if (gameSiteStr == null || gameSiteStr == "") {
			List<String> agents = Arrays.asList(agentStr.split(","));
			for (ServerConf serverConf : serverConfList) {
				String agent = serverConf.getAgent();
				if(agents.contains(agent)){
					servers.add(serverConf.getGameSite());
				}
			}
			return servers;
		}else{
			return Arrays.asList(gameSiteStr.split(","));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerConf> getServerConfs(String gameSiteStr, String agentStr) {
		List<ServerConf> serverConfList = (List<ServerConf>) CacheService.getFromCache(CacheConstant.B_GAME_LIST);
		
		List<ServerConf> servers = new ArrayList<ServerConf>();
		
		if (gameSiteStr == null || gameSiteStr == "") {
			List<String> agents = Arrays.asList(agentStr.split(","));
			for (ServerConf serverConf : serverConfList) {
				String agent = serverConf.getAgent();
				if(agents.contains(agent)){
					servers.add(serverConf);
				}
			}

		}else{
			List<String> serverArr = Arrays.asList(gameSiteStr.split(","));
			for (ServerConf serverConf : serverConfList) {
				if(serverArr.contains(serverConf.getGameSite())){
					servers.add(serverConf);
					if(serverArr.size() == 1){
						break;
					}
				}
			}
		}
		
		return servers;
	}
}
