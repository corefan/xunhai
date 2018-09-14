package com.domain.task;

import java.io.Serializable;

/**
 * 环任务特殊奖励
 * @author ken
 * @date 2017-5-11
 */
public class BaseWeekTaskReward implements Serializable {

	private static final long serialVersionUID = 3804962721390478964L;

	/** 特殊环数*/
	private int weekTaskNum;
	/** 物品奖励*/
	private int itemId;
	
	public int getWeekTaskNum() {
		return weekTaskNum;
	}
	public void setWeekTaskNum(int weekTaskNum) {
		this.weekTaskNum = weekTaskNum;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
}
