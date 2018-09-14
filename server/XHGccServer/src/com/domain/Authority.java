package com.domain;

import java.io.Serializable;

/**
 * @author ken
 * 2014-3-10
 * 权限
 */
public class Authority implements Serializable {

	private static final long serialVersionUID = -7780560042344461384L;

	/** 权限编号 */
	private int authorityID;
	/** 权限名字 */
	private String name;
	/** 功能类型*/
	private int functionType;
	/**功能名*/
	private String functionName;
	
	/** 类型*/
	private int type;

	/**
	 * 得到创建权限sql
	 * */
	public String getInsertSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO t_authority(NAME) VALUES ");
		sb.append("(");
		sb.append("'");
		sb.append(name);
		sb.append("'");
		sb.append(",");
		sb.append(functionType);
		sb.append(",");
		sb.append("'");
		sb.append(functionName);
		sb.append("'");
		sb.append(",");
		sb.append(type);
		sb.append(")");
		
		return sb.toString();
	}
	
	/**
	 * 得到更新权限sql
	 * */
	public String getUpdateSql() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE t_authority SET ");
		sb.append("NAME = ");
		sb.append("'");
		sb.append(name);
		sb.append("'");
		sb.append(" WHERE AUTHORITY_ID=");
		sb.append(authorityID);
		
		return sb.toString();
	}
	
	public int getAuthorityID() {
		return authorityID;
	}
	public void setAuthorityID(int authorityID) {
		this.authorityID = authorityID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public int getType() {
		return type;
	}
	
	public int getFunctionType() {
		return functionType;
	}

	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	public void setType(int type) {
		this.type = type;
	}
}
