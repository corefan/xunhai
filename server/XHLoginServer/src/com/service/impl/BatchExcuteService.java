package com.service.impl;

import java.util.List;

import com.dao.BatchExcuteDAO;
import com.domain.GameEntity;
import com.service.IBatchExcuteService;

/**
 * @author ken
 * 2013-11-7
 * 批量更新Service
 */
public class BatchExcuteService implements IBatchExcuteService {

	public BatchExcuteDAO batchExcuteDAO = new BatchExcuteDAO();
	
	@Override
	public void batchInsert(List<GameEntity> objectList) {
		if(objectList != null && !objectList.isEmpty()) {
			batchExcuteDAO.batchCreate(objectList);
		}
	}

}
