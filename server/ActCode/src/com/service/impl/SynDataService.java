package com.service.impl;

import java.util.Set;

import com.common.CacheService;
import com.common.CodeContext;
import com.constant.CacheConstant;
import com.service.IBatchExcuteService;
import com.service.ISynDataService;



/**
 * @author barsk
 * 2013-11-7
 * 从缓存中同步数据到数据库Service接口
 */
public class SynDataService implements ISynDataService {


	@Override
	public void update_fiveMinData() {
		synUpdateData((Set<?>) CacheService.getFromCache(CacheConstant.SYN_ACT_CODE));
	}
	
	private void synUpdateData(Set<?> dataList) {
		if (dataList != null && dataList.isEmpty()) {
			return;
		}

		IBatchExcuteService batchExcuteService = CodeContext.getInstance().getServiceCollection().getBatchExcuteService();
		batchExcuteService.batchUpdate(dataList);
	}
	
}
