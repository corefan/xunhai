package com.dao;

import java.util.List;
import java.util.Set;

import com.db.GccSqlSessionTemplate;
import com.domain.GameEntity;

/**
 * @author ken
 * 2013-11-7
 * 批量执行DAO
 */
public class BatchExcuteDAO extends GccSqlSessionTemplate {

	public void batchExcute(Set<GameEntity> paramObjectList, String executor) {
		super.batchExcute(paramObjectList, executor);
	}

	public void batchCreate(List<GameEntity> paramObjectList) {
		super.batchInsert(paramObjectList);
	}
	
}
