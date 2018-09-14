package com.service;

import com.domain.weekactivity.BaseWeekActivity;

/**
 * 周活动系统
 * @author ken
 * @date 2017-5-12
 */
public interface IWeekActivityService {

	/**
	 * 基础缓存
	 */
	void initBaseCache();
	
	/**
	 * 根据活动编号取活动配置
	 */
	BaseWeekActivity getBaseWeekActivity(int activityId);
	
	/**
	 * 取活动类型列表
	 */
	void getActivityList(long playerId);
	
	/**
	 * 进入活动
	 */
	void enterActivity(long playerId, int activityId) throws Exception;
}
