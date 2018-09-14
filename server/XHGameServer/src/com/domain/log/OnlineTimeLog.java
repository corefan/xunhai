/**
 * 
 */
package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线时长统计日志
 * @author ken
 * @date 2018年3月28日
 */
public class OnlineTimeLog implements Serializable{

	private static final long serialVersionUID = 2121254625361205738L;
	
	/** 自增ID*/
	private long logId; 	
	/** 登录玩家数*/
	private int loginNum;
	/** 一分钟人数*/
	private int num1;
	private int num5;
	private int num10;
	private int num20;
	private int num30;
	private int num40;
	private int num50;
	private int num60;
	/** 五小时人数*/
	private int h5;
	/** 十小时人数*/
	private int h10;
	/** 十小时以上人数*/
	private int uph10;
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
	public int getLoginNum() {
		return loginNum;
	}
	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}
	public int getNum1() {
		return num1;
	}
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	public int getNum5() {
		return num5;
	}
	public void setNum5(int num5) {
		this.num5 = num5;
	}
	public int getNum10() {
		return num10;
	}
	public void setNum10(int num10) {
		this.num10 = num10;
	}
	public int getNum20() {
		return num20;
	}
	public void setNum20(int num20) {
		this.num20 = num20;
	}
	public int getNum30() {
		return num30;
	}
	public void setNum30(int num30) {
		this.num30 = num30;
	}
	public int getNum40() {
		return num40;
	}
	public void setNum40(int num40) {
		this.num40 = num40;
	}
	public int getNum50() {
		return num50;
	}
	public void setNum50(int num50) {
		this.num50 = num50;
	}
	public int getNum60() {
		return num60;
	}
	public void setNum60(int num60) {
		this.num60 = num60;
	}
	public int getH5() {
		return h5;
	}
	public void setH5(int h5) {
		this.h5 = h5;
	}
	public int getH10() {
		return h10;
	}
	public void setH10(int h10) {
		this.h10 = h10;
	}
	public int getUph10() {
		return uph10;
	}
	public void setUph10(int uph10) {
		this.uph10 = uph10;
	}	
	
}
