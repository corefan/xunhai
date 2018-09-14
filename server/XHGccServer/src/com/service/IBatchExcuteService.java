package com.service;

import java.util.List;


/**
 * @author ken
 * 2013-11-7
 * 批量更新Service
 */
public interface IBatchExcuteService {

	
	
	/**
	 * 批量插入
	 */
	public void batchInsert(List<?> objectList);
	
	
}
