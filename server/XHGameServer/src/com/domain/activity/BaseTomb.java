package com.domain.activity;

import java.io.Serializable;

/**
 * 陵墓配置表
 * @author ken
 * @date 2017-10-25
 */
public class BaseTomb implements Serializable {

	private static final long serialVersionUID = -8820101298467626709L;

	/** 奖励编号*/
	private int id;
	/** 名称*/
	private String name;
	/** 分组*/
	private int group;
	/** 物品编号*/
	private int itemId;
	/** 物品数量*/
	private int count;
	/** 概率*/
	private int rate;
	/** 是否广播*/
	private int notice;
	
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
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
	
}
