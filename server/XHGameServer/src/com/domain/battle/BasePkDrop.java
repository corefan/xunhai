package com.domain.battle;

import java.io.Serializable;

/**
 * 玩家pk装备掉落配置表
 * @author ken
 * @date 2017-4-1
 */
public class BasePkDrop implements Serializable {

	private static final long serialVersionUID = 789262163640431204L;

	/**
	 * 装备栏编号  0代表背包栏
	 */
	private int posId;
	
	/**
	 * 装备掉落万分值
	 */
	private int dropRate;
	
	/** 
	 * 红名掉落万分值
	 */
	private int redDropRate;

	public int getPosId() {
		return posId;
	}

	public void setPosId(int posId) {
		this.posId = posId;
	}

	public int getDropRate() {
		return dropRate;
	}

	public void setDropRate(int dropRate) {
		this.dropRate = dropRate;
	}

	public int getRedDropRate() {
		return redDropRate;
	}

	public void setRedDropRate(int redDropRate) {
		this.redDropRate = redDropRate;
	}
	
}
