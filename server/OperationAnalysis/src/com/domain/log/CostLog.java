/**
 * 
 */
package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 消耗表
 * @author jiangqin
 * @date 2017-8-7
 */
public class CostLog implements Serializable{	
	
	private static final long serialVersionUID = -5829874170579592584L;

	/** 自增ID*/
	private long logId; 	
	/** 账号编号*/
	private int userId;
	/** 玩家编号*/
	private long playerId;
	/** 角色名称*/
	private String playerName;
	/** 资源类型(@RewardTypeConstant)*/
	private int type;
	/** 消耗描述(名称)*/
	private String costName;
	/** 消耗值*/
	private int value;
	/** 消耗时间*/
	private Date createTime;
	/** 运营商*/
	private String agent;
	/** 站点*/
	private String gameSite;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getGameSite() {
		return gameSite;
	}
	public void setGameSite(String gameSite) {
		this.gameSite = gameSite;
	}
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
}
