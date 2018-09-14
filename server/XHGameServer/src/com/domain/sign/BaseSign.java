package com.domain.sign;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

public class BaseSign implements Serializable{

	private static final long serialVersionUID = -1122695176689156109L;
	
	/** 签到天数*/;
	private int signDay; 
	
	/** 签到奖励*/;
	private String reward;
	private List<Reward> rewardList;

	/** vip双倍奖励标识*/;
	private int doubleReward; 
	

	public int getSignDay() {
		return signDay;
	}

	public void setSignDay(int signDay) {
		this.signDay = signDay;
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

	public int getDoubleReward() {
		return doubleReward;
	}

	public void setDoubleReward(int doubleReward) {
		this.doubleReward = doubleReward;
	}
}
