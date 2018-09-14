/**
 * 
 */
package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * @author jiangqin
 * @date 2017-8-8
 */
public class LoginLog implements Serializable{

	private static final long serialVersionUID = 2865650320458812218L;
	/** 自增ID*/
	private long logId; 	
	/** 账号编号*/
	private int userId;
	/** 登录时间*/
	private Date createTime;
	/** 运营商*/
	private String agent;
	/** 站点*/
	private String gameSite;
	
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
}
