package com.service.impl;

import java.util.List;
import java.util.Set;

import com.dao.BatchExcuteDAO;
import com.db.SimpleSqlSessionTemplate;
import com.service.IBatchExcuteService;

/**
 * @author barsk
 * 批量更新Service
 */
public class BatchExcuteService implements IBatchExcuteService {

	public BatchExcuteDAO batchExcuteDAO = new BatchExcuteDAO();
	

	@Override
	public void batchUpdate(Set<?> objectList) {
		if (objectList != null && objectList.size() > 0) {
			batchExcuteDAO.batchExcute(objectList, SimpleSqlSessionTemplate.EXCUTOR_UPDATE);
		}
	}

	@Override
	public void batchInsert(List<?> objectList) {
		if(objectList != null && !objectList.isEmpty()) {
			batchExcuteDAO.batchCreate(objectList);
		}
	}

}
