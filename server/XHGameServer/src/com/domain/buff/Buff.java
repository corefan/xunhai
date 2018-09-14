package com.domain.buff;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.domain.puppet.BasePuppet;

/**
 * buff
 * @author ken
 * @date 2017-4-5
 */
public class Buff implements Serializable {

	private static final long serialVersionUID = 6294808670283830903L;

	/** 唯一编号*/
	private int id;
	/** 释放者*/
    private BasePuppet fighter;
    /** 目标编号 */
    private String targetGuid;
    /** buff编号*/
    private int buffId;
	/** 类型   @BuffConstant*/
	private int type;
	/** 结束时间   -1：永久*/
	private long endTime;
	
	/** 持续加减血飘字*/
	private int hpShow;
    
	/** 间隔时间*/
	private transient int periodTime;
	/** 间隔更新时间*/
	private transient long updateTime;
    
	private Map<Integer, Integer> addValueMap = new HashMap<Integer, Integer>();
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BasePuppet getFighter() {
		return fighter;
	}
	public void setFighter(BasePuppet fighter) {
		this.fighter = fighter;
	}
	public String getTargetGuid() {
		return targetGuid;
	}
	public void setTargetGuid(String targetGuid) {
		this.targetGuid = targetGuid;
	}
	public int getBuffId() {
		return buffId;
	}
	public void setBuffId(int buffId) {
		this.buffId = buffId;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPeriodTime() {
		return periodTime;
	}
	public void setPeriodTime(int periodTime) {
		this.periodTime = periodTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public Map<Integer, Integer> getAddValueMap() {
		return addValueMap;
	}
	public void setAddValueMap(Map<Integer, Integer> addValueMap) {
		this.addValueMap = addValueMap;
	}
	public int getHpShow() {
		return hpShow;
	}
	public void setHpShow(int hpShow) {
		this.hpShow = hpShow;
	}
	
}
