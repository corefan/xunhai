package com.domain.battle;

import java.io.Serializable;
import java.util.List;

/**
 * 任务物品配置
 * @author jiangqin
 * @date 2017-5-2
 */

public class BaseTaskItem implements Serializable{

	
	private static final long serialVersionUID = -6296321096895844804L;

	/** 物品ID */
	private int itemId;
	/** 物品名称 */
	private String itemName;	
	/** 掉落权重字串*/
	private String weight;	
	/** 掉落列表*/
	private List<List<Integer>> weightList;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}	

	public List<List<Integer>> getWeightList() {
		return weightList;
	}

	public void setWeightList(List<List<Integer>> weightList) {
		this.weightList = weightList;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
