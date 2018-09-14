/**
 * 
 */
package com.domain.tianti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.Reward;

/**
 * 天梯pk奖励配置
 * @author jiangqin
 * @date 2017-9-29
 */
public class BaseTiantiPKReward implements Serializable{
	
	private static final long serialVersionUID = 432617291884476651L;
	
	/** 奖励ID(等级）*/
	private int id;
	 /** 竞技场初始物品*/
	private String initItem;
	 /** 胜利者奖励*/
	private String winReward;	
	/** 失败者奖励*/
	private String failReward;
	
	private List<Reward> initItemList;
	private Map<Integer, List<Reward>> winRewardMap = new HashMap<Integer,List<Reward>>();
	private Map<Integer, List<Reward>>  failRewardMap = new HashMap<Integer,List<Reward>>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInitItem() {
		return initItem;
	}
	public String getWinReward() {
		return winReward;
	}
	public String getFailReward() {
		return failReward;
	}
	public List<Reward> getInitItemList() {
		return initItemList;
	}
	public Map<Integer, List<Reward>> getWinRewardMap() {
		return winRewardMap;
	}
	public Map<Integer, List<Reward>> getFailRewardMap() {
		return failRewardMap;
	}
	public void setInitItem(String initItem) {
		this.initItem = initItem;
	}
	public void setWinReward(String winReward) {
		this.winReward = winReward;
	}
	public void setFailReward(String failReward) {
		this.failReward = failReward;
	}
	public void setInitItemList(List<Reward> initItemList) {
		this.initItemList = initItemList;
	}
	public void setWinRewardMap(Map<Integer, List<Reward>> winRewardMap) {
		this.winRewardMap = winRewardMap;
	}
	public void setFailRewardMap(Map<Integer, List<Reward>> failRewardMap) {
		this.failRewardMap = failRewardMap;
	}

}
