package com.domain.collect;

import java.io.Serializable;
import java.util.List;


/**
 * 采集信息(记录到senceModel采集列表信息)
 * @author jiangqin
 * @date 2017-3-7
 */
public class Collect implements Serializable{

	private static final long serialVersionUID = 6173339323399642773L;

	/** 采集锁*/
	private Object collectLock = new Object();
	
	/** 采集唯一编号*/
	private int playerCollectId;	
	/** 采集ID*/
	private int collectId;	
	/** 采集类型*/
	private int type;	
	/** 采集次数*/
	private int collectNum;		
	/** 采集人 ids*/
	private List<Long> playerIds;
	/** 状态 @BattleConstant*/
	private int state;
	/** 刷新采集时间戳*/
	private long refreshDate;
	
	/** 区域格子*/
	private int gridId;

	public int getPlayerCollectId() {
		return playerCollectId;
	}

	public void setPlayerCollectId(int playerCollectId) {
		this.playerCollectId = playerCollectId;
	}

	public int getCollectId() {
		return collectId;
	}

	public void setCollectId(int collectId) {
		this.collectId = collectId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(int collectNum) {
		this.collectNum = collectNum;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getRefreshDate() {
		return refreshDate;
	}

	public void setRefreshDate(long refreshDate) {
		this.refreshDate = refreshDate;
	}

	public int getGridId() {
		return gridId;
	}

	public void setGridId(int gridId) {
		this.gridId = gridId;
	}

	public Object getCollectLock() {
		return collectLock;
	}

	public void setCollectLock(Object collectLock) {
		this.collectLock = collectLock;
	}

	public List<Long> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(List<Long> playerIds) {
		this.playerIds = playerIds;
	}
	
}
