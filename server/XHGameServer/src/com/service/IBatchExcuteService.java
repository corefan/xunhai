package com.service;

import java.util.Set;

import com.domain.GameEntity;

/**
 * 2013-11-7
 * 批量更新Service
 */
public interface IBatchExcuteService {

	/**
	 * 批量更新
	 */
	public void batchUpdate(Set<GameEntity> objcectList);
	
}
