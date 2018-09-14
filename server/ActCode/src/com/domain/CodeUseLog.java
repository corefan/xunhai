package com.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author barsk
 * 2014-5-12
 * 激活码使用日志	
 */
public class CodeUseLog implements Serializable {

	private static final long serialVersionUID = -5617351785234388440L;

	/** 日志编号 */
	private int logID;
	/** 激活码 */
	private String code;
	/** 激活码类型 */
	private int type;
	/** 玩家编号 */
	private int playerID;
	/** 站点 */
	private String site;
	/** 使用时间 */
	private Date createTime;
	
	
	/** 得到创建sql */
	public String getInsertSql() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO T_CODE_USE_LOG(CODE,TYPE,PLAYER_ID,SITE,CREATE_TIME) VALUES ");
		sb.append("(");
		sb.append("'");
		sb.append(code);
		sb.append("'");
		sb.append(",");
		sb.append(type);
		sb.append(",");
		sb.append(playerID);
		sb.append(",");
		sb.append("'");
		sb.append(site);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(new Timestamp(createTime.getTime()));
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
