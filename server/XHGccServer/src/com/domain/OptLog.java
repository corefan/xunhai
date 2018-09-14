package com.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.common.DateService;

/** 操作日志*/
public class OptLog implements Serializable{
	private static final long serialVersionUID = -2705774470113053689L;
	
	/** 日志ID*/
	private int logID;
	/** 用户ID*/
	private int userID;
	/**用户名称*/
	private String userName;
	/** 操作类型*/
	private int opt;
	/** 操作IP*/
	private String optIP;
	/** 内容*/
	private String content;
	/** 详情*/
	private String detail;
	/** 创建时间*/
	private Date createTime;
	
	public String getInsertSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO t_opt_log(USER_ID,USER_NAME,OPT,OPT_IP,CONTENT,DETAIL,CREATE_TIME) VALUES(");
		sb.append(userID);
		sb.append(",");
		sb.append("'");
		sb.append(userName);
		sb.append("'");
		sb.append(",");
		sb.append(opt);
		sb.append(",");
		sb.append("'");
		sb.append(optIP);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(content);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(detail);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(new Timestamp(DateService.getCurrentUtilDate().getTime()));
		sb.append("'");
		sb.append(")");
		
		return sb.toString();
	}
	
	public int getLogID() {
		return logID;
	}
	public void setLogID(int logID) {
		this.logID = logID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getOpt() {
		return opt;
	}
	public void setOpt(int opt) {
		this.opt = opt;
	}
	public String getOptIP() {
		return optIP;
	}
	public void setOptIP(String optIP) {
		this.optIP = optIP;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
