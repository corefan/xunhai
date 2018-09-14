package com.domain.guild;

import java.io.Serializable;

/**
 * 捐献配置表
 * @author ken
 * @date 2018年4月19日
 */
public class BaseGuildDonate implements Serializable {

	private static final long serialVersionUID = 4727543074531241907L;

	/** 编号*/
	private int id;
	/** 捐献类型  @RewardTypeConstant*/
	private int moneyType;
	/** 捐献值*/
	private int value;
	/** 个人获得贡献*/
	private int contribution;
	/** 帮派获得资金*/
	private int money;
	/** 帮派获得建设*/
	private int buildNum;
	/** 每天限制次数*/
	private int limitTimes;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getContribution() {
		return contribution;
	}
	public void setContribution(int contribution) {
		this.contribution = contribution;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getBuildNum() {
		return buildNum;
	}
	public void setBuildNum(int buildNum) {
		this.buildNum = buildNum;
	}
	public int getLimitTimes() {
		return limitTimes;
	}
	public void setLimitTimes(int limitTimes) {
		this.limitTimes = limitTimes;
	}
	
}
