package com.dao.task;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.battle.BaseTaskItem;
import com.domain.task.BaseActTask;
import com.domain.task.BaseTask;
import com.domain.task.BaseWeekTaskReward;

/**
 * 任务dao
 * @author ken
 * @date 2017-2-20
 */
public class BaseTaskDAO extends BaseSqlSessionTemplate {


	private static final String taskSql = "select * from task";
	
	/**
	 * 取任务配置表
	 */
	public List<BaseTask> listBaseTasks(){
		return this.selectList(taskSql, BaseTask.class);
	}

	/**
	 * 取任务配置表
	 */
	public List<BaseTaskItem> listBaseTaskItem(){
		String taskItemSql = "select * from taskitem";
		return this.selectList(taskItemSql, BaseTaskItem.class);
	}
	
	/**
	 * 活动任务配置
	 */
	public List<BaseActTask> listBaseActTasks(){
		String sql = "select * from taskact";
		return this.selectList(sql, BaseActTask.class);
	}
	
	/**
	 * 环任务特殊奖励配置
	 */
	public List<BaseWeekTaskReward> listBaseWeekTaskRewards(){
		String sql = "select * from taskweekreward";
		return this.selectList(sql, BaseWeekTaskReward.class);
	}
}
