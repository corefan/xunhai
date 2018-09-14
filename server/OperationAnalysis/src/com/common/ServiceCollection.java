package com.common;

import com.service.IBaseDataService;
import com.service.IBatchExcuteService;
import com.service.IBehaviorAnalysisService;
import com.service.IDataAnalysisService;
import com.service.IOpenService;
import com.service.IUserService;
import com.service.impl.BaseDataService;
import com.service.impl.BatchExcuteService;
import com.service.impl.BehaviorAnalysisService;
import com.service.impl.DataAnalysisService;
import com.service.impl.OpenService;
import com.service.impl.UserService;

/**
 * @author ken
 * 2014-3-10
 * service容器
 */
public class ServiceCollection {
	private IUserService userService = new UserService();
	private IBaseDataService baseDataService = new BaseDataService();
	private IBatchExcuteService batchExcuteService = new BatchExcuteService();
	private IDataAnalysisService dataAnalysisService = new DataAnalysisService();
	private IBehaviorAnalysisService behaviorAnalysisService = new BehaviorAnalysisService();
	private IOpenService openService = new OpenService();
	
	/**
	 * 初始化缓存数据
	 * */
	public void initCache() {
		userService.initUserCache();
		baseDataService.initData();
		CacheSynDBService.initCacheMap();
	}

	public IUserService getUserService() {
		return userService;
	}

	public IBaseDataService getBaseDataService() {
		return baseDataService;
	}

	public IDataAnalysisService getDataAnalysisService() {
		return dataAnalysisService;
	}
	
	public IBehaviorAnalysisService getBehaviorAnalysisService() {
		return behaviorAnalysisService;
	}

	public IBatchExcuteService getBatchExcuteService() {
		return batchExcuteService;
	}

	public void setBatchExcuteService(IBatchExcuteService batchExcuteService) {
		this.batchExcuteService = batchExcuteService;
	}

	public IOpenService getOpenService() {
		return openService;
	}
}
