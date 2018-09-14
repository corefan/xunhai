package com.service.impl;

import java.util.Set;

import com.dao.BatchExcuteDAO;
import com.db.SimpleSqlSessionTemplate;
import com.domain.GameEntity;
import com.service.IBatchExcuteService;

/**
 * @author ken
 * 批量更新Service
 */
public class BatchExcuteService implements IBatchExcuteService {

	public BatchExcuteDAO batchExcuteDAO = new BatchExcuteDAO();
	

	@Override
	public void batchUpdate(Set<GameEntity> objectList) {
		if (objectList != null && objectList.size() > 0) {
			batchExcuteDAO.batchExcute(objectList, SimpleSqlSessionTemplate.EXCUTOR_UPDATE);
		}
	}

}
