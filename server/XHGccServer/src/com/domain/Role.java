package com.domain;

import java.io.Serializable;

/**
 * @author ken
 * 2014-3-10
 * 角色
 */
public class Role implements Serializable {

	private static final long serialVersionUID = 8750887913692369551L;
	
	/** 角色编号 */
	private int roleID;
	/** 角色名字 */
	private String name;
	
	/**
	 * 得到创建sql
	 * */
	public String getInsertSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO t_role(NAME) VALUES ");
		sb.append("(");
		sb.append("'");
		sb.append(name);
		sb.append("'");
		sb.append(")");
		
		return sb.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE t_role SET ");
		sb.append("NAME = ");
		sb.append("'");
		sb.append(name);
		sb.append("'");
		sb.append(" WHERE ROLE_ID=");
		sb.append(roleID);
		
		return sb.toString();
	}
	
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
