package com.domain.bag;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 装备传承配置
 * @author ken
 * @date 2018年8月22日
 */
public class BaseEquipInherit implements Serializable {

	private static final long serialVersionUID = 774814330592520716L;

	/** 目标装备品质*/
	private int rare;
	/** 当前装备强化等级*/
	private int strongLv;
	/** 传承消耗*/
	private String inheritCost;
	private List<Reward> inheritCostList;
	
	public int getRare() {
		return rare;
	}
	public void setRare(int rare) {
		this.rare = rare;
	}
	public int getStrongLv() {
		return strongLv;
	}
	public void setStrongLv(int strongLv) {
		this.strongLv = strongLv;
	}
	public String getInheritCost() {
		return inheritCost;
	}
	public void setInheritCost(String inheritCost) {
		this.inheritCost = inheritCost;
	}
	public List<Reward> getInheritCostList() {
		return inheritCostList;
	}
	public void setInheritCostList(List<Reward> inheritCostList) {
		this.inheritCostList = inheritCostList;
	}
	
}
