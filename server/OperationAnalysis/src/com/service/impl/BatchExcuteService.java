package com.service.impl;

import java.util.List;

import com.dao.BatchExcuteDAO;
import com.service.IBatchExcuteService;
import com.util.DAOFactory;

/**
 * @author ken
 * 2013-11-7
 * 批量更新Service
 */
public class BatchExcuteService implements IBatchExcuteService {
	public BatchExcuteDAO batchExcuteDAO = DAOFactory.getBatchExcuteDAO();

	@Override
	public void batchInsert(List<?> objectList) {
		if (objectList != null && !objectList.isEmpty()) {
			batchExcuteDAO.batchCreate(objectList);
		}
	}
}
