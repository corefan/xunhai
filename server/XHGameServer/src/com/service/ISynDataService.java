package com.service;

/**
 * 从缓存中同步数据到数据库
 * @author ken
 * @date 2016-12-27
 */
public interface ISynDataService {

	/**
	 * 停服前同步缓存
	 * */
	public void synCache_beforeClose();
	
	/**
	 * 批量同步缓存到数据库(更新，5分钟更新数据)
	 */
	public void update_fiveOneData();
	
	/**
	 * 批量同步缓存到数据库(更新，5分钟更新数据)
	 */
	public void update_fiveTwoData();
	
	/**
	 * 批量同步缓存到数据库(更新，5分钟更新数据)
	 */
	public void update_fiveThreeData();
	
}
