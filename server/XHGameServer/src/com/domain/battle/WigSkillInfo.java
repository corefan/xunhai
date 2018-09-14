package com.domain.battle;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 地效持续技能
 * @author ken
 * @date 2017-1-21
 */
public class WigSkillInfo implements Serializable {

	private static final long serialVersionUID = 4140746827122268484L;

	/** 地效唯一编号*/
	private int wigId;
	/**  技能使用者*/
	private String guid;
	/**  技能编号*/
	private int skillId; 
	/** 释放坐标*/
	private int x;
	/** 释放坐标*/
	private int y;
	/** 释放坐标*/
	private int z;
	/**  结束时间 */
	private long endTime;
	/**  是否删除*/
	private boolean deleteFlag;
	
	/** 区域格子*/
	private int gridId;
	
	private static AtomicInteger WIGATOMIC = new AtomicInteger(1);
	
	public WigSkillInfo() {

	}
	public WigSkillInfo(String guid, int skillId, int x, int y, int z, int gridId,
			long endTime) {
		this.guid = guid;
		this.skillId = skillId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.gridId = gridId;
		this.endTime = endTime;
		this.wigId = WIGATOMIC.getAndIncrement();
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
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
	public boolean isDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public int getWigId() {
		return wigId;
	}
	public void setWigId(int wigId) {
		this.wigId = wigId;
	}
	public int getGridId() {
		return gridId;
	}
	public void setGridId(int gridId) {
		this.gridId = gridId;
	}
	
}
