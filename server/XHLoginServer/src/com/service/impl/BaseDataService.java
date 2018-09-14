package com.service.impl;

import java.util.List;

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

		List<BaseServerConfig> gcvList = baseDataDAO.listBaseServerConfigs();
		
		List<BaseAgentConfig> gccList = baseDataDAO.listBaseAgentConfigs();
		
		CacheService.putToCache(CacheConstant.B_GAME_CONFIG_VAR_LIST, gcvList);
		CacheService.putToCache(CacheConstant.B_GAME_CONFIG_CONS_LIST, gccList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BaseServerConfig> listServers() {
		return (List<BaseServerConfig>)CacheService.getFromCache(CacheConstant.B_GAME_CONFIG_VAR_LIST);
	}
	
	
	@SuppressWarnings("unchecked")
	public BaseAgentConfig getGameConfigConstantByAgent(String agent) {
		
		List<BaseAgentConfig> gccList = (List<BaseAgentConfig>) CacheService.getFromCache(CacheConstant.B_GAME_CONFIG_CONS_LIST);
		if (gccList != null) {
			for (BaseAgentConfig gcc : gccList) {
				if (gcc.getAgent().equals(agent)){
					return gcc;
				} 
			}
			BaseAgentConfig gcc = baseDataDAO.getBaseAgentConfig(agent);
			if(gcc != null){
				gccList.add(gcc);
				return gcc;
			}
		}
		
		return null;
	}

}
