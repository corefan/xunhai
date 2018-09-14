package com.domain.weekactivity;

import java.io.Serializable;

/**
 * 周活动配置
 * @author ken
 * @date 2017-5-12
 */
public class BaseWeekActivity implements Serializable {

	private static final long serialVersionUID = 7058208043266691836L;

	/** 活动编号*/
	private int id;
	/** 活动类型  1：日常  2：即时*/
	private int type;
	/** 活动名称*/
	private String name;
	/** 地图编号*/
	private int mapId;
	/** 最大进入次数*/
	private int maxCount;
	/** 限制等级*/
	private int limitLevel;
	/** 周几*/
	private int week;
	/** 开始小时*/
	private int startHour;
	/** 开始分钟*/
	private int startMin;
	/** 结束小时*/
	private int endHour;
	/** 结束分钟*/
	private int endMin;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLimitLevel() {
		return limitLevel;
	}
	public void setLimitLevel(int limitLevel) {
		this.limitLevel = limitLevel;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartMin() {
		return startMin;
	}
	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getEndMin() {
		return endMin;
	}
	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}
	
}
