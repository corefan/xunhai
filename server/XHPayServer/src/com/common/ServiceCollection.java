package com.common;

import com.service.IBaseDataService;
import com.service.IBatchExcuteService;
import com.service.IPayService;
import com.service.impl.BaseDataService;
import com.service.impl.BatchExcuteService;
import com.service.impl.PayService;

/**
 * @author ken
 * 2014-3-10
 * service容器
 */
public class ServiceCollection {

	private IBaseDataService baseDataService = new BaseDataService();
	private IBatchExcuteService batchExcuteService = new BatchExcuteService();
	private IPayService payService = new PayService();
	

	/**
	 * 初始化缓存数据
	 * */
	public void initCache() {
		baseDataService.initData();
		
		payService.initCache();
	}
	
	public IBaseDataService getBaseDataService() {
		return baseDataService;
	}

	public IBatchExcuteService getBatchExcuteService() {
		return batchExcuteService;
	}

	public void setBatchExcuteService(IBatchExcuteService batchExcuteService) {
		this.batchExcuteService = batchExcuteService;
	}

	public IPayService getPayService() {
		return payService;
	}
	

}
