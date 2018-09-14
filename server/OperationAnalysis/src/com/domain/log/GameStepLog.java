/**
 * 
 */
package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏节点日志
 * @author jiangqin
 * @date 2017-8-9
 */
public class GameStepLog implements Serializable{
	
	private static final long serialVersionUID = 7788029575657777924L;

	/** 自增ID*/
	private long logId; 	
	/** 玩家编号*/
	private long playerId;
	/** 主线任务编号*/
	private int taskId;
	/** 创建时间*/
	private Date createTime;
	/** 运营商*/
	private String agent;
	/** 站点*/
	private String gameSite;
	
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
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
