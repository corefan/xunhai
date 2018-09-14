package com.domain.collect;

import java.io.Serializable;


/**
 * 采集信息(记录到senceModel采集列表信息)
 * @author jiangqin
 * @date 2017-3-7
 */
public class CollectItemInfo implements Serializable{

	private static final long serialVersionUID = 6173339323399642773L;

	/** 采集唯一编号*/
	private int playerCollectId;	
	/** 采集ID*/
	private int collectId;		
	/** 目标采集者*/
	private int targetPlayerId;	
	/** 是否全体可见*/
	private int visible;
	/** 采集次数*/
	private int collectNum;	
	/** 状态 @BattleConstant*/
	private int state;
	/** 刷新采集时间戳*/
	private long refreshDate;
	
	/** 采集目标位置*/
	private int x;
	private int y;
	private int z;	
	
	public Integer getCollectId() {
		return collectId;
	}
	
	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}	

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the playerCollectId
	 */
	public Integer getPlayerCollectId() {
		return playerCollectId;
	}

	/**
	 * @param playerCollectId the playerCollectId to set
	 */
	public void setPlayerCollectId(Integer playerCollectId) {
		this.playerCollectId = playerCollectId;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public long getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(long refreshDate) {
		this.refreshDate = refreshDate;
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

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public int getTargetPlayerId() {
		return targetPlayerId;
	}

	public void setTargetPlayerId(int targetPlayerId) {
		this.targetPlayerId = targetPlayerId;
	}	
}
