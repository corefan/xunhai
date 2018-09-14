package com.domain;

public class User {
	/** 用户编号 */
	private int userID;
	
	/** 用户名 */
	private String userName;
	
	/** 密码 */
	private String password;
	
	/** 运营商 */
	private String agent;
	
	/** 站点 */
	private String site;
	
	/** 角色 */
	private int roleID;
	
	/** 状态(1.正常 2.禁用) */
	private int state;
	
	/** 删除标记(0.正常 1.删除) */
	private int deleteFlag;

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
