package com.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.common.DateService;

/**
 * @author ken
 * 2014-3-10
 * 用户
 */
public class User implements Serializable {

	private static final long serialVersionUID = 3012895356956334594L;
	
	/** 用户编号 */
	private int userID;
	/** 用户名 */
	private String userName;
	/** 密码 */
	private String password;
	/** 代理商 */
	private String 	agent;
	/** 站点 */
	private String site;
	/** 角色 */
	private int roleID;
	/** 状态(1.正常 2.禁用) */
	private int state;
	/** 删除标记(0.正常 1.删除) */
	private int deleteFlag;
	
	
	/**
	 * 得到创建sql
	 * */
	public String getInsertSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO t_user_gcc(USER_NAME,PASSWORD,AGENT,SITE,ROLE_ID,STATE,DELETE_FLAG,CREATE_TIME) VALUES ");
		sb.append("(");
		sb.append("'");
		sb.append(userName);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(password);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(agent);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(site);
		sb.append("'");
		sb.append(",");
		sb.append(roleID);
		sb.append(",");
		sb.append(state);
		sb.append(",");
		sb.append(deleteFlag);
		sb.append(",");
		sb.append("'");
		sb.append(new Timestamp(DateService.getCurrentUtilDate().getTime()));
		sb.append("'");
		sb.append(")");
		
		return sb.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE t_user_gcc SET ");
		sb.append("USER_NAME = ");
		sb.append("'");
		sb.append(userName);
		sb.append("'");
		sb.append(",");
		sb.append("PASSWORD = ");
		sb.append("'");
		sb.append(password);
		sb.append("'");
		sb.append(",");
		sb.append("SITE = ");
		sb.append("'");
		sb.append(site);
		sb.append("'");
		sb.append(",");
		sb.append("ROLE_ID = ");
		sb.append(roleID);
		sb.append(",");
		sb.append("STATE = ");
		sb.append(state);
		sb.append(",");
		sb.append("DELETE_FLAG = ");
		sb.append(deleteFlag);
		sb.append(" WHERE USER_ID=");
		sb.append(userID);
		
		return sb.toString();
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

}
