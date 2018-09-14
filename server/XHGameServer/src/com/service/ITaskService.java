package com.service;

import java.util.List;
import java.util.Map;

import com.domain.battle.BaseTaskItem;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 * 任务系统
 * @author ken
 * @date 2017-2-20
 */
public interface ITaskService {

	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 取任务配置
	 */
	BaseTask getBaseTask(int taskId);
	
	/**
	 * 删除缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 调度清理已删除的任务
	 */
	void quartzDeletePlayerTask();
	
	
	List<PlayerTask> createPlayerTask(long playerId, List<Integer> newTaskIds);
	 
	/**
	 * 玩家任务列表
	 */
	Map<Integer, PlayerTask> getPlayerTaskMapByPlayerId(long playerId);
	
	/**
	 * 取当前主线任务
	 */
	PlayerTask getPlayerTaskByType(long playerId, int taskType);
	
	/**
	 * 删除任务缓存
	 */
	void deletePlayerTask(long playerId, int taskId);
	
	/**
	 * 同步玩家任务
	 */
	void synPlayerTask(PlayerTask playerTask);
	
	/**
	 * 提交任务
	 */
	void submitTask(long playerId, int taskId) throws Exception;
	
	/**
	 * 直接完成任务
	 */
	void completeTask(long playerId, int taskId) throws Exception;
	
	/**
	 * 执行任务
	 */
	void executeTask(long playerId, int conditionType, List<Integer> conditionList) throws Exception;
	
	/**
	 * 升级触发任务
	 */
	void touchTaskByLevel(long playerId, int level);
	
	/**
	 * 触发新任务列表
	 */
	void acceptTask(long playerId, int curTaskId, List<Integer> newTaskList);
	
	/**
	 * 获取任务物品
	 */
	BaseTaskItem getBaseTaskItem(int itemId);
	
	
	/*************************每日任务***************************/
	/**
	 * 获取每日任务列表
	 */
	void getDailyTaskList(long playerId);
	
	/**
	 * 接受每日任务
	 */
	void acceptDailyTask(long playerId, int taskId) throws Exception;
	
	/**
	 * 手动刷新每日任务列表
	 */
	void refreshDailyTask(long playerId, int type) throws Exception;
	
	/**
	 * 放弃任务
	 */
	void abandonTask(long playerId, int taskId)throws Exception;
	
	
	/*************************环任务***************************/
	/**
	 * 接受环任务
	 */
	void acceptWeekTask(long playerId)throws Exception;
	
	
	/*************************猎妖任务***************************/
	/**
	 * 接受猎妖任务
	 */
	void acceptHuntTask(long playerId, int itemType)throws Exception;
}
