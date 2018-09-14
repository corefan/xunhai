package com.domain;

import java.io.Serializable;

/**
 * 奖励数据
 * @author ken
 * @date 2017-2-7
 */
public class Reward implements Serializable {

	private static final long serialVersionUID = 3884349045802141533L;

	/** 资源类型@RewardTypeConstant*/
	private int type;
	/** 物品编号 或装备编号*/
	private int id;
	/** 数量*/
	private int num;
	/** >0概率  分母10000  */
	private int rate;
	/** 是否绑定*/
	private int blind;
	
	
	public Reward() {
		
	}
	
	
	public Reward(int type, int id, int num, int rate, int blind) {
		this.type = type;
		this.id = id;
		this.num = num;
		this.rate = rate;
		this.blind = blind;
	}


	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getBlind() {
		return blind;
	}
	public void setBlind(int blind) {
		this.blind = blind;
	}
	
}
