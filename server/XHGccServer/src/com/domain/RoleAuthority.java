package com.domain;

import java.io.Serializable;

/**
 * @author ken
 * 2014-3-10
 * 角色权限
 */
public class RoleAuthority implements Serializable {

	private static final long serialVersionUID = 6292050795731134114L;

	/** 角色权限编号 */
	private int roleAuthorityID;
	/** 角色编号 */
	private int roleID;
	/** 权限编号 */
	private int authorityID;
	
	/**
	 * 得到创建sql
	 * */
	public String getInsertSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO t_role_authority(ROLE_ID, AUTHORITY_ID) VALUES ");
		sb.append("(");
		sb.append(roleID);
		sb.append(",");
		sb.append(authorityID);
		sb.append(")");
		
		return sb.toString();
	}
	
	public int getRoleAuthorityID() {
		return roleAuthorityID;
	}
	public void setRoleAuthorityID(int roleAuthorityID) {
		this.roleAuthorityID = roleAuthorityID;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public int getAuthorityID() {
		return authorityID;
	}
	public void setAuthorityID(int authorityID) {
		this.authorityID = authorityID;
	}
}
