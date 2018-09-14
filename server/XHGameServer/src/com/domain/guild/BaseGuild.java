package com.domain.guild;

import java.io.Serializable;

/**
 * 帮派基础配置
 * @author ken
 * @date 2018年4月3日
 */
public class BaseGuild implements Serializable {
	private static final long serialVersionUID = 2074681370022380113L;
	
	/** 都护府等级*/
	private int level;
	/** 所需资金*/
	private int needMoney;
	/** 所需建设度*/
	private int needBuildNum;
	/** 每天消耗资金*/
	private int costMoney;
	/** 每天消耗建设度*/
	private int costBuildNum;
	/** 最大人数*/
	private int maxNum;
	/** 副都护人数*/
	private int assistantNum;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	public int getNeedBuildNum() {
		return needBuildNum;
	}
	public void setNeedBuildNum(int needBuildNum) {
		this.needBuildNum = needBuildNum;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public int getAssistantNum() {
		return assistantNum;
	}
	public void setAssistantNum(int assistantNum) {
		this.assistantNum = assistantNum;
	}
	public int getCostMoney() {
		return costMoney;
	}
	public void setCostMoney(int costMoney) {
		this.costMoney = costMoney;
	}
	public int getCostBuildNum() {
		return costBuildNum;
	}
	public void setCostBuildNum(int costBuildNum) {
		this.costBuildNum = costBuildNum;
	}
}
