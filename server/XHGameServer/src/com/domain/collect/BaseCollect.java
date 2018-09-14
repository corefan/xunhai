package com.domain.collect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.domain.Reward;

/**
 * 采集
 * @author jiangqin
 * @date 2017-3-7
 */
public class BaseCollect implements Serializable{
	
	private static final long serialVersionUID = -2594545961055078380L;	
	
	/** 采集编号*/
	private int id;
	
	/** 采集类型*/
	private int type;
	
	/** 采集物所在地图*/
	private int mapId;
	
	/** 采集位置*/
	private String position;
	
	/** 采集所需(读条)时间 毫秒*/
	private int collectTime;
	
	/** 最大可采集人数*/
	private int maxNum;
	
	/** 可采集次数0：无限*/
	private int count;
	
	/** 采集物回复时间毫秒*/
	private int recover;
	
	/** 高级采集(刷奖间隔时间)*/
	private int cost;
	
	/** 采集奖励*/
	private String reward;
	
	/** 采集奖励 key:career */
	private Map<Integer, List<CollectReward>> collectRewardMap;

	
	/** 采集目标位置*/
	private int x;
	private int y;
	private int z;
	
	/** 采集消耗*/
	private String expendStr;
	private List<Reward> expendList;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(int collectTime) {
		this.collectTime = collectTime;
	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRecover() {
		return recover;
	}

	public void setRecover(int recover) {
		this.recover = recover;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
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

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Map<Integer, List<CollectReward>> getCollectRewardMap() {
		return collectRewardMap;
	}

	public void setCollectRewardMap(Map<Integer, List<CollectReward>> collectRewardMap) {
		this.collectRewardMap = collectRewardMap;
	}

	public List<Reward> getExpendList() {
		return expendList;
	}

	public void setExpendList(List<Reward> expendList) {
		this.expendList = expendList;
	}

	public String getExpendStr() {
		return expendStr;
	}

	public void setExpendStr(String expendStr) {
		this.expendStr = expendStr;
	}
}
