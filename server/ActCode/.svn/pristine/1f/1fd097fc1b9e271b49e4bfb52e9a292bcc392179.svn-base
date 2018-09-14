package com.common;

import com.service.IBatchExcuteService;
import com.service.ICodeService;
import com.service.ISynDataService;
import com.service.impl.BatchExcuteService;
import com.service.impl.CodeService;
import com.service.impl.SynDataService;

/**
 * @author barsk
 * 2014-3-10
 * service容器
 */
public class ServiceCollection {

	private ICodeService codeService = new CodeService();
	private IBatchExcuteService batchExcuteService = new BatchExcuteService();
	private ISynDataService synDataService = new SynDataService();
	
	/**
	 * 初始化缓存数据
	 * */
	public void initCache() {
		codeService.initActCodeCache();
	}

	public ICodeService getCodeService() {
		return codeService;
	}

	public IBatchExcuteService getBatchExcuteService() {
		return batchExcuteService;
	}

	public ISynDataService getSynDataService() {
		return synDataService;
	}
	
	
}
