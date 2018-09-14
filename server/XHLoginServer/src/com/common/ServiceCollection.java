package com.common;


import com.service.IAccountService;
import com.service.IActCodeService;
import com.service.IBaseDataService;
import com.service.IBatchExcuteService;
import com.service.ISmsService;
import com.service.impl.AccountService;
import com.service.impl.ActCodeService;
import com.service.impl.BaseDataService;
import com.service.impl.BatchExcuteService;
import com.service.impl.SmsService;

/**
 * @author ken
 * 2014-3-10
 * service容器
 */
public class ServiceCollection {

	private IBaseDataService baseDataService = new BaseDataService();
	private IBatchExcuteService batchExcuteService = new BatchExcuteService();
	private IAccountService accountService = new AccountService();
	private ISmsService smsService = new SmsService();
	private IActCodeService actCodeService = new ActCodeService();

	/**
	 * 初始化缓存数据
	 * */
	public void initCache() {
		accountService.initCache();
		
		actCodeService.initCache();
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

	public IAccountService getAccountService() {
		return accountService;
	}

	public ISmsService getSmsService() {
		return smsService;
	}

	public IActCodeService getActCodeService() {
		return actCodeService;
	}

}
