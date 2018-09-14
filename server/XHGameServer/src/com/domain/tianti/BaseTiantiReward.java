package com.domain.tianti;

import java.io.Serializable;
import java.util.List;

/**
 * 天梯排名奖励
 * @author ken
 * @date 2017-4-14
 */
public class BaseTiantiReward implements Serializable {

	private static final long serialVersionUID = 2929411815149439831L;

	private int minRank;
	private int maxRank;
	
	private String reward;
	private List<List<Integer>> rewardList;
	
	public int getMinRank() {
		return minRank;
	}
	public void setMinRank(int minRank) {
		this.minRank = minRank;
	}
	public int getMaxRank() {
		return maxRank;
	}
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public List<List<Integer>> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<List<Integer>> rewardList) {
		this.rewardList = rewardList;
	}
	
}
