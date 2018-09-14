package com.domain.vip;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;


/**
 * vip基础数据
 * @author jiangqin
 * @date 2017-6-15
 */
public class BaseVip implements Serializable{

	
	private static final long serialVersionUID = 8488084510571537620L;
	
	/** vip编号*/
	private int id;	
	/** vip等级*/
	private int vipLevel;
	/** 有效时间（天）*/
	private int validTime;
	/** 首次激活奖励*/
	private String activateReward;				
	private List<Reward> rewardList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValidTime() {
		return validTime;
	}

	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}

	public String getActivateReward() {
		return activateReward;
	}

	public void setActivateReward(String activateReward) {
		this.activateReward = activateReward;
	}

	public List<Reward> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
}
