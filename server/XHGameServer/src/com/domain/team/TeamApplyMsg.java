package com.domain.team;

import java.io.Serializable;

public class TeamApplyMsg implements Serializable{

	private static final long serialVersionUID = 797662841807626184L;

	/** 玩家编号*/	 
	private int applyPlayerId;
	
	/** 申请时间*/
	private long applyTime;		

	public int getApplyPlayerId() {
		return applyPlayerId;
	}

	public void setApplyPlayerId(int applyPlayerId) {
		this.applyPlayerId = applyPlayerId;
	}

	public long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}

}
