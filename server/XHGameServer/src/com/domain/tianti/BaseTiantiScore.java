package com.domain.tianti;

import java.io.Serializable;
import java.util.List;

/**
 * 天梯分数
 * @author ken
 * @date 2017-4-14
 */
public class BaseTiantiScore implements Serializable {

	private static final long serialVersionUID = 4408297933796296571L;

	/** 自增编号*/
	private int id;
	/** 段位*/
	private int stage;
	/** 星数*/
	private int star;
	/** 最小积分区间*/
	private int minScore;
	/** 最大积分区间*/
	private int maxScore;
	
	/** 排位奖励*/
	private String rankReward;
	private List<List<Integer>> rankRewardList;
	
	/** 段位奖励*/
	private String stageReward;
	private List<List<Integer>> stageRewardList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getMinScore() {
		return minScore;
	}
	public void setMinScore(int minScore) {
		this.minScore = minScore;
	}
	public int getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	public String getRankReward() {
		return rankReward;
	}
	public List<List<Integer>> getRankRewardList() {
		return rankRewardList;
	}
	public String getStageReward() {
		return stageReward;
	}
	public List<List<Integer>> getStageRewardList() {
		return stageRewardList;
	}
	public void setRankReward(String rankReward) {
		this.rankReward = rankReward;
	}
	public void setRankRewardList(List<List<Integer>> rankRewardList) {
		this.rankRewardList = rankRewardList;
	}
	public void setStageReward(String stageReward) {
		this.stageReward = stageReward;
	}
	public void setStageRewardList(List<List<Integer>> stageRewardList) {
		this.stageRewardList = stageRewardList;
	}
	
}
