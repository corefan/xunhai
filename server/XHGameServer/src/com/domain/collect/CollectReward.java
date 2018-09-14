package com.domain.collect;

public class CollectReward {
	/** 职业*/
	private int career;	
	/** 物品编号*/
	private int id;	
	/** 权重 >0概率  分母10000  */
	private int rate;
	
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
