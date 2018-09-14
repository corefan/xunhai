package com.service.impl;

import java.util.List;
import java.util.Set;

import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.domain.GameEntity;
import com.service.IBatchExcuteService;
import com.service.ISynDataService;
import com.util.LogUtil;



/**
 * 2013-11-7
 * 从缓存中同步数据到数据库Service接口
 */
public class SynDataService implements ISynDataService {

	@Override
	public void synCache_beforeClose() {
		try {
			update_fiveOneData();
		} catch (Exception e) {
			LogUtil.error("异常: ",e);
		}
		try {
			update_fiveTwoData();
		} catch (Exception e) {
			LogUtil.error("异常: ",e);
		}
		try {
			update_fiveThreeData();
		} catch (Exception e) {
			LogUtil.error("异常: ",e);
		}
	}
	
	@Override
	public void update_fiveOneData() {
		synUpdateData(CacheSynDBService.getAllAndClearCache_five_one_update());
	}
	
	@Override
	public void update_fiveTwoData() {
		synUpdateData(CacheSynDBService.getAllAndClearCache_five_two_update());
	}

	public void update_fiveThreeData() {
		synUpdateData(CacheSynDBService.getAllAndClearCache_five_three_update());
	}
	
	private void synUpdateData(List<Set<GameEntity>> dataList) {
		if (dataList != null && dataList.isEmpty()) {
			return;
		}

		IBatchExcuteService batchExcuteService = GameContext.getInstance().getServiceCollection().getBatchExcuteService();
		for (Set<GameEntity> objectList : dataList) {
			try {
				batchExcuteService.batchUpdate(objectList);
			} catch (Exception e) {
				LogUtil.error("异常: ",e);
			}
		}
		
		dataList = null;
	}
	

}
