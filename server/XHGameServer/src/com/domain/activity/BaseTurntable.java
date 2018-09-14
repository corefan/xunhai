/**
 * 
 */
package com.domain.activity;

import java.io.Serializable;

import com.domain.Reward;

/**
 * 转盘抽奖
 * @author jiangqin
 * @date 2017-10-25
 */
public class BaseTurntable implements Serializable{
	
	private static final long serialVersionUID = -7817790283932019644L;
	
	/** 抽奖编号*/
	private int id;
	/** 抽奖物品*/
	private String reward;
	private Reward turnReward;
 	
	/** 首次抽奖权重*/
	private int fristDraw;
	/** 免费抽奖权重*/
	private int freeDraw;
	/** 元宝抽奖权重*/
	private int goldDraw;	
	/** 是否广播*/
	private int isBroadcast;
	
	public int getId() {
		return id;
	}
	public String getReward() {
		return reward;
	}
	public int getFristDraw() {
		return fristDraw;
	}
	public int getFreeDraw() {
		return freeDraw;
	}
	public int getGoldDraw() {
		return goldDraw;
	}
	public int getIsBroadcast() {
		return isBroadcast;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public void setFristDraw(int fristDraw) {
		this.fristDraw = fristDraw;
	}
	public void setFreeDraw(int freeDraw) {
		this.freeDraw = freeDraw;
	}
	public void setGoldDraw(int goldDraw) {
		this.goldDraw = goldDraw;
	}
	public void setIsBroadcast(int isBroadcast) {
		this.isBroadcast = isBroadcast;
	}
	public Reward getTurnReward() {
		return turnReward;
	}
	public void setTurnReward(Reward turnReward) {
		this.turnReward = turnReward;
	}
}
