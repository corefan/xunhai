package com.domain.tower;

import java.io.Serializable;
import java.util.List;

import com.domain.Reward;

/**
 * 大荒塔道具奖励
 * @author ken
 * @date 2017-3-24
 */
public class BaseTowerReward implements Serializable {

	private static final long serialVersionUID = 2647876331582483262L;

	/** 最小层数*/
	private int minLv;
	/** 最大层数*/
	private int maxLv;
	
	/** 随机奖励  （全局随机）*/
	private String items;
	private List<Reward> itemList;
	private int allRate;

	public int getMinLv() {
		return minLv;
	}
	public void setMinLv(int minLv) {
		this.minLv = minLv;
	}
	public int getMaxLv() {
		return maxLv;
	}
	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public List<Reward> getItemList() {
		return itemList;
	}
	public void setItemList(List<Reward> itemList) {
		this.itemList = itemList;
	}
	public int getAllRate() {
		return allRate;
	}
	public void setAllRate(int allRate) {
		this.allRate = allRate;
	}
	
}
