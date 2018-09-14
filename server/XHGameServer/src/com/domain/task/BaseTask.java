package com.domain.task;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 任务配置
 * @author ken
 * @date 2017-2-20
 */
public class BaseTask implements Serializable {
	
	private static final long serialVersionUID = 1783380140518721239L;

	/** 任务编号*/
	private int id;
	/** 任务类型 @TaskConstant*/
	private int type;
	/** 触发等级*/
	private int touchLevel;
	/** 后置任务列表*/
	private String nextTask;
	private List<Integer> nextTaskList;
	
	/** 任务名称*/
	private String taskName;
	/** 提交npc编号*/
	private int submitNpc;
	/** 条件类型 @TaskConstant */
	private int conditionType;
	
	/** 指定条件参数列表*/
	private String condition;
	private List<Integer> conditionList;
	
	/** 任务奖励*/
	private String reward;
	private List<Reward> rewardList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTouchLevel() {
		return touchLevel;
	}
	public void setTouchLevel(int touchLevel) {
		this.touchLevel = touchLevel;
	}
	public String getNextTask() {
		return nextTask;
	}
	public void setNextTask(String nextTask) {
		this.nextTask = nextTask;
	}
	public List<Integer> getNextTaskList() {
		return nextTaskList;
	}
	public void setNextTaskList(List<Integer> nextTaskList) {
		this.nextTaskList = nextTaskList;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getSubmitNpc() {
		return submitNpc;
	}
	public void setSubmitNpc(int submitNpc) {
		this.submitNpc = submitNpc;
	}
	public int getConditionType() {
		return conditionType;
	}
	public void setConditionType(int conditionType) {
		this.conditionType = conditionType;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<Integer> getConditionList() {
		return conditionList;
	}
	public void setConditionList(List<Integer> conditionList) {
		this.conditionList = conditionList;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public List<Reward> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}
	
}
