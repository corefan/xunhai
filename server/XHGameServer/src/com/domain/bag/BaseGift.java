package com.domain.bag;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.Reward;

/**
 * 礼包
 * @author ken
 * @date 2017-5-2
 */
public class BaseGift implements Serializable {

	private static final long serialVersionUID = 1710714604916197468L;

	/** 礼包编号*/
	private int giftId;
	
	 /** 礼包物品*/
	private String reward;
	private Map<Integer, Map<Integer, List<Reward>>> rewardMap = new HashMap<Integer, Map<Integer,List<Reward>>>();
	
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public Map<Integer, Map<Integer, List<Reward>>> getRewardMap() {
		return rewardMap;
	}
	public void setRewardMap(Map<Integer, Map<Integer, List<Reward>>> rewardMap) {
		this.rewardMap = rewardMap;
	}

}
