package com.domain.puppet;

import java.io.Serializable;

/**
 * 对自己造成伤害的敌人
 * @author ken
 * @date 2017-2-8
 */
public class EnemyModel implements Serializable {

	private static final long serialVersionUID = -1953441097722374514L;

	/** 敌人*/
	private int targetType;
	private String targetGuid;
	
	/** 对自己造成伤害的玩家*/
	private long playerId;
	/** 造成伤害总和*/
	private volatile int totalDmg;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTotalDmg() {
		return totalDmg;
	}
	public void setTotalDmg(int totalDmg) {
		this.totalDmg = totalDmg;
	}
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public String getTargetGuid() {
		return targetGuid;
	}
	public void setTargetGuid(String targetGuid) {
		this.targetGuid = targetGuid;
	}
	
}
