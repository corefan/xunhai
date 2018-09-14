package com.domain.friend;

import java.io.Serializable;

public class PlayerApply implements Serializable {
	private static final long serialVersionUID = 7111687023920131577L;
	/**玩家编号*/
	private long playerId;	
	/**申请人的玩家编号 */
	private long applyPlayerId;	
	/**申请人的玩家名称 */
	private String applyPlayerName;
	/**申请人的玩家职业 */
	private int applyPlayerCareer;
	/**申请人的玩家等级 */
	private int applyPlayerLevel;
	/**申请人是否在线 */
	private int online;
	/** 消息申请时间 */
	private long applyTime;
	

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}

	public long getApplyPlayerId() {
		return applyPlayerId;
	}
	public void setApplyPlayerId(long applyPlayerId) {
		this.applyPlayerId = applyPlayerId;
	}
	public int getApplyPlayerCareer() {
		return applyPlayerCareer;
	}
	public void setApplyPlayerCareer(int applyPlayerCareer) {
		this.applyPlayerCareer = applyPlayerCareer;
	}
	public int getApplyPlayerLevel() {
		return applyPlayerLevel;
	}
	public void setApplyPlayerLevel(int applyPlayerLevel) {
		this.applyPlayerLevel = applyPlayerLevel;
	}
	public String getApplyPlayerName() {
		return applyPlayerName;
	}
	public void setApplyPlayerName(String applyPlayerName) {
		this.applyPlayerName = applyPlayerName;
	}
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
}
