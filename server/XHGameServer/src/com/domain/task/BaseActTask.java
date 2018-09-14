package com.domain.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动任务配置
 * @author ken
 * @date 2017-5-9
 */
public class BaseActTask implements Serializable {

	private static final long serialVersionUID = 6165350384394059970L;

	/** 自增编号*/
	private int id;
	/** 任务类型*/
	private int taskType;
	/** 等级区间*/
	private String levelLimit;
	private List<Integer> levelLimitList;
	/** 任务组*/
	private String taskIds;
	private List<List<Integer>> taskIdList = new ArrayList<List<Integer>>();
	
	private int allRate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public String getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(String levelLimit) {
		this.levelLimit = levelLimit;
	}
	public List<Integer> getLevelLimitList() {
		return levelLimitList;
	}
	public void setLevelLimitList(List<Integer> levelLimitList) {
		this.levelLimitList = levelLimitList;
	}
	public String getTaskIds() {
		return taskIds;
	}
	public void setTaskIds(String taskIds) {
		this.taskIds = taskIds;
	}
	public List<List<Integer>> getTaskIdList() {
		return taskIdList;
	}
	public void setTaskIdList(List<List<Integer>> taskIdList) {
		this.taskIdList = taskIdList;
	}
	public int getAllRate() {
		return allRate;
	}
	public void setAllRate(int allRate) {
		this.allRate = allRate;
	}
	
}
