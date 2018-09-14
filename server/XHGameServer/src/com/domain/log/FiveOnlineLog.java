package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 五分钟在线记录
 * @author ken
 * @date 2018年3月27日
 */
public class FiveOnlineLog implements Serializable {
	
	private static final long serialVersionUID = -5244282513696312079L;
	
	/** 自增ID*/
	private long logId; 	
	/** 在线人数*/
	private int num;
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
