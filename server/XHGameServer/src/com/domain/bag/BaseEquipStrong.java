package com.domain.bag;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 装备强化配置
 * @author ken
 * @date 2018年8月22日
 */
public class BaseEquipStrong implements Serializable {

	private static final long serialVersionUID = 4537217021002479708L;

	/** 装备部位*/
	private int equipType;
	/** 强化等级*/	
	private int strongLv;
	/** 基础属性百分值（=基础属性*x%）*/	
	private int strongPer;
	/** 成功概率百分值*/	
	private int strongRate;
	/** 强化消耗*/	
	private String strongCost;
	private List<Reward> strongCostList;
	
	public int getEquipType() {
		return equipType;
	}
	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}
	public int getStrongLv() {
		return strongLv;
	}
	public void setStrongLv(int strongLv) {
		this.strongLv = strongLv;
	}
	public int getStrongPer() {
		return strongPer;
	}
	public void setStrongPer(int strongPer) {
		this.strongPer = strongPer;
	}
	public int getStrongRate() {
		return strongRate;
	}
	public void setStrongRate(int strongRate) {
		this.strongRate = strongRate;
	}
	public String getStrongCost() {
		return strongCost;
	}
	public void setStrongCost(String strongCost) {
		this.strongCost = strongCost;
	}
	public List<Reward> getStrongCostList() {
		return strongCostList;
	}
	public void setStrongCostList(List<Reward> strongCostList) {
		this.strongCostList = strongCostList;
	}
	
}
