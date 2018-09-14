package com.domain.instance;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

public class BaseInstanceReward implements Serializable{

	private static final long serialVersionUID = -9141540765021491021L;

	/** 副本奖励ID*/
	private int id;
	/** 奖励类型*/
	private int rewardType;		
	/** 最小层数*/
	private int minLv;	
	/** 最大层数*/
	private int maxLv;	
	/** 奖励随机类型 (1:全局	2:独立)*/
	private int randType;	
	/** 奖池(类型,id,数量,概率)*/
	private String rewardStr;	
	/** 奖池(类型,id,数量,概率)*/
	private List<Reward> rewardList;
	
	/** 奖励掉落位置*/
	private String position;	
	/** 坐标 横向*/
	private int x;	
	/** 坐标 高度*/
	private int y;
	/** 坐标 纵向*/
	private int z;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRewardType() {
		return rewardType;
	}
	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}
	public int getMinLv() {
		return minLv;
	}
	public void setMinLv(int minLv) {
		this.minLv = minLv;
	}
	public int getMaxLv() {
		return maxLv;
	}
	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}
	public int getRandType() {
		return randType;
	}
	public void setRandType(int randType) {
		this.randType = randType;
	}
	public String getRewardStr() {
		return rewardStr;
	}
	public void setRewardStr(String rewardStr) {
		this.rewardStr = rewardStr;
	}
	public List<Reward> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
