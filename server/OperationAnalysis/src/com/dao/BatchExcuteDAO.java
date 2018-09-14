package com.dao;

import java.util.List;
import java.util.Set;

import com.db.DAOTemplate;

/**
 * @author ken
 * 2013-11-7
 * 批量执行DAO
 */
public class BatchExcuteDAO extends DAOTemplate{

	public void batchExcute(Set<?> paramObjectList, String executor) {
		jdbcTempldate.batchExcute(paramObjectList, executor);
	}

	public void batchCreate(List<?> paramObjectList) {
		jdbcTempldate.batchInsert(paramObjectList);
	}
	
}
