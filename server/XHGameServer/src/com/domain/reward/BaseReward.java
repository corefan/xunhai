package com.domain.reward;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

public class BaseReward implements Serializable{

	private static final long serialVersionUID = 2044930035257337919L;

	/**奖励编号*/		
	private int id;
	/**奖励类型*/
	private int type;	
	/**达成条件*/
	private int condition;	
	/**领奖限制条件*/
	private int resCondition;
	/** 奖励描述*/
	private String des;
	/**奖励*/	
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
	public List<Reward> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public int getResCondition() {
		return resCondition;
	}
	public void setResCondition(int resCondition) {
		this.resCondition = resCondition;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
}
