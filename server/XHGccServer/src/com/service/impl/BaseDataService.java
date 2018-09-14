package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.CacheService;
import com.common.DateService;
import com.common.GameException;
import com.constant.CacheConstant;
import com.dao.BaseDAO;
import com.dao.BaseDataDAO;
import com.domain.BaseItem;
import com.domain.BaseServerConfig;
import com.service.IBaseDataService;

/**
 * @author ken
 * 2014-3-24
 * 基础数据
 */
@SuppressWarnings("unchecked")
public class BaseDataService implements IBaseDataService {

	private BaseDataDAO baseDataDAO = new BaseDataDAO();
	private BaseDAO baseDAO = new BaseDAO();
	
	public void initData() {
		List<BaseServerConfig> gameSiteVariableList = baseDataDAO.getGameSiteVariableList();
		Map<String, BaseServerConfig> serverConfMap = new HashMap<String, BaseServerConfig>();

		
		for (BaseServerConfig gsv : gameSiteVariableList) {
			serverConfMap.put(gsv.getGameSite(), gsv);
		}
		gameSiteVariableList = null;

		CacheService.putToCache(CacheConstant.B_SERVER_CONF_MAP, serverConfMap);
		
		List<BaseItem> items = baseDAO.listBaseItems();
		Map<Integer, BaseItem> itemMap = new HashMap<Integer, BaseItem>();
		for(BaseItem model : items){
			itemMap.put(model.getId(), model);
		}
		CacheService.putToCache(CacheConstant.B_ITEM_MAP, itemMap);
		
	}

	private Map<String, BaseServerConfig> getServerConfMap(){
		
		return (Map<String, BaseServerConfig>)CacheService.getFromCache(CacheConstant.B_SERVER_CONF_MAP);
	}
	
	
	public List<String> getServerConfList() {
		List<String> gameSiteList = new ArrayList<String>();
		List<BaseServerConfig> lists = new ArrayList<BaseServerConfig>();
		lists.addAll(getServerConfMap().values());
		Collections.sort(lists, new Comparator<BaseServerConfig>() {

			@Override
			public int compare(BaseServerConfig o1, BaseServerConfig o2) {
				return o2.getServerNo() - o1.getServerNo();
			}
		});
		for(BaseServerConfig gsv : lists){
			gameSiteList.add(gsv.getGameSite()  + "|" + gsv.getAgent() + "|" + gsv.getServerName() + "|"+ DateService.getDateByString(gsv.getOpenServerDate()).getTime() / 1000);
		}
		return gameSiteList;
	}
	

	@Override
	public Map<String, List<String>> getAgentMap() {
		Map<String, List<String>> agentMap = new HashMap<String, List<String>>();
		Map<String, BaseServerConfig> serverConfMap = getServerConfMap();
		
		Map<String, Integer> mergeMap = new HashMap<String, Integer>();
		for(Map.Entry<String, BaseServerConfig> entry : serverConfMap.entrySet()){
			BaseServerConfig gsv = entry.getValue();
			
			List<String> gsiteList = agentMap.get(gsv.getAgent());
			if(gsiteList == null){
				gsiteList = new ArrayList<String>();
				agentMap.put(gsv.getAgent(), gsiteList);
			}
			
			String key = gsv.getGameInnerIp()+"_"+gsv.getGamePort();
			if(!mergeMap.containsKey(key)){
				mergeMap.put(key, 1);
				
				gsiteList.add(gsv.getGameSite());
			}
			
		}
		return agentMap;
	}
	
	public BaseServerConfig getServerConfByGameSite(String gameSite) {
		Map<String, BaseServerConfig> serverConfMap = (Map<String, BaseServerConfig>) CacheService.getFromCache(CacheConstant.B_SERVER_CONF_MAP);
		return serverConfMap.get(gameSite);
	}
	
	public String getUrlByGameSitePath(String gameSite, String path) {
		
		BaseServerConfig gsv = getServerConfByGameSite(gameSite);
		
		return "http://"+gsv.getGameInnerIp()+":"+gsv.getWebPort()+"/"+path;
	}
	
	public String getNameByGameStepID(int stepID) {
		Map<Integer, String> gameStepMap = (Map<Integer, String>) CacheService.getFromCache(CacheConstant.B_GAME_STEP_NAME_MAP);
		return gameStepMap.get(stepID);
	}

	@Override
	public void createServer(BaseServerConfig model)throws Exception {
		if(model.getGameSite() == null || model.getGameSite().equals("")) throw new GameException("服务器站点未输入！");
		BaseServerConfig server = getServerConfByGameSite(model.getGameSite());
		if(server != null) throw new GameException("服务器已存在！");
		try {
			baseDataDAO.createServer(model);

			Map<String, BaseServerConfig> serverConfMap = getServerConfMap();
			serverConfMap.put(model.getGameSite(), model);
		
		} catch (Exception e) {
			throw new GameException("插入服务器记录错误");
		}
		
	}

	@Override
	public void updateServer(BaseServerConfig model, String gameSite)throws Exception {
		if(model.getGameSite() == null || model.getGameSite().equals("")) throw new GameException("服务器站点未输入！");
		BaseServerConfig server = getServerConfByGameSite(gameSite);
		if(server == null) throw new GameException("服务器不存在！");
		try {
			baseDataDAO.updateServer(model, gameSite);
			
			Map<String, BaseServerConfig> serverConfMap = getServerConfMap();
			
			serverConfMap.remove(gameSite);
			serverConfMap.put(model.getGameSite(), model);
		} catch (Exception e) {
			throw new GameException("更新服务器记录错误");
		}
	}

	@Override
	public void deleteServer(String gameSite) throws Exception{
		if(gameSite == null || gameSite.equals("")) throw new GameException("服务器站点未输入！");
		BaseServerConfig server = getServerConfByGameSite(gameSite);
		if(server == null) throw new GameException("服务器不存在！");
		
		try {
			baseDataDAO.deleteServer(gameSite);
			getServerConfMap().remove(gameSite);
		} catch (Exception e) {
			throw new GameException("删除服务器记录错误");
		}
	}

	@Override
	public List<BaseServerConfig> getServerList()throws Exception {
		
		List<BaseServerConfig> lists = new ArrayList<BaseServerConfig>();
		Map<String, BaseServerConfig> serverConfMap = getServerConfMap();
		lists.addAll(serverConfMap.values());
		Collections.sort(lists, new Comparator<BaseServerConfig>() {

			@Override
			public int compare(BaseServerConfig o1, BaseServerConfig o2) {
				return o2.getServerNo() - o1.getServerNo();
			}
		});
		return lists;
	}

}
