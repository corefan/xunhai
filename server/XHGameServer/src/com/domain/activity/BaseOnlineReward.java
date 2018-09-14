package com.domain.activity;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 在线奖励配置
 * @author jiangqin
 * @date 2017-5-5
 */
public class BaseOnlineReward implements Serializable{

	private static final long serialVersionUID = -1190294701052529143L;

	/** 奖励ID*/
	private int id;
	/** 在线时长(分)*/
	private int onlineTime; 
	/** 在线奖励*/
	private String reward;
	private List<Reward> rewardList;
	
	public int getId() {
		return id;
	}
	public int getOnlineTime() {
		return onlineTime;
	}
	public String getReward() {
		return reward;
	}
	public List<Reward> getRewardList() {
		return rewardList;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}	
}
