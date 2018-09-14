package com.dao;

import java.util.Set;

import com.db.GameSqlSessionTemplate;
import com.domain.GameEntity;

/**
 * 2013-11-7
 * 批量执行DAO
 */
public class BatchExcuteDAO extends GameSqlSessionTemplate {

	public void batchExcute(Set<GameEntity> paramObjectList, String executor) {
		super.batchExcute(paramObjectList, executor);
	}

}
