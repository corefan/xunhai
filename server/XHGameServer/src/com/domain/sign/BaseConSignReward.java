package com.domain.sign;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

public class BaseConSignReward  implements Serializable{
	
	private static final long serialVersionUID = -338025657865489745L;
	
	/** 连续签到天数*/
	private int day; 
	
	/** 签到奖励*/;
	private String reward;
	private List<Reward> rewardList;
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
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
