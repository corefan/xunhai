package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 注册日志
 * @author ken
 * @date 2017-8-15
 */
public class RegisterLog implements Serializable {

	private static final long serialVersionUID = 1842978505865446441L;

	/** 自增编号*/
	private long logId;
	/** 账户编号*/
	private long userId;
	/** 创建时间*/
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


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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
