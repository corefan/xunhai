package com.domain.monster;

import java.io.Serializable;

/**
 * AI判定表
 * @author ken
 * @date 2017-1-19
 */
public class BaseAiDetermine implements Serializable {

	private static final long serialVersionUID = -5569676395945734346L;
	
	/** AI编号*/
	private int id;
	/** 判定类型
	  	0=无条件
	  	1=自身血量低于%
		2=玩家血量低于%
		3=怪物血量低于%
		4=与玩家距离小于
		5=与玩家距离大于*/
	private int decisionType;
	/** 判定参数
		0：0
		1：1~100
		2：1~100
		3：1~100
		4：格子数
		5：格子数*/
	private int decisionValue;
	/** 技能ID*/
	private int actionId;
	/** 行为级*/
	private int actionLevel;
	/** 恒定权重*/
	private int weight;
	/** 行为CD  毫秒*/
	private int actionCd;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDecisionType() {
		return decisionType;
	}
	public void setDecisionType(int decisionType) {
		this.decisionType = decisionType;
	}
	public int getDecisionValue() {
		return decisionValue;
	}
	public void setDecisionValue(int decisionValue) {
		this.decisionValue = decisionValue;
	}
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public int getActionLevel() {
		return actionLevel;
	}
	public void setActionLevel(int actionLevel) {
		this.actionLevel = actionLevel;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getActionCd() {
		return actionCd;
	}
	public void setActionCd(int actionCd) {
		this.actionCd = actionCd;
	}
	
}
