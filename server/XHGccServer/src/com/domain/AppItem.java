package com.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.common.DateService;

/** 申请物品 (发送)*/
public class AppItem implements Serializable{
	
	private static final long serialVersionUID = 7459422479285196280L;
	
	/** 申请ID*/
	private int appItemId;
	/** 申请人ID*/
	private int appPlayerID;
	/** 用户名称*/
	private String name;
	/** 发送制定类型*/
	private int appType; //(  1：单服所有玩家 2： 单服制定玩家)
	/** gameSite*/
	private String gameSite;
	/** 制定玩家列表*/
	private String playerIDList;
	/** 申请时间*/
	private Date appDate;
	/** 物品*/
	private String itemList;
	/** 内容*/
	private String content;
	/** 详情*/
	private String detail;
	/** 原因*/
	private String reason;
	/** 申请状态(1: 已申请 2： 审核通过 3： 拒绝)*/
	private int state;
	/** 状态信息 */
	private String stateInfo;
	/** 标题*/
	private String title;
	/** 运营商*/
	private String agent;
	
	


	/** 插入sql*/
	public String getInsertSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO t_app_item(APP_TYPE, APP_PLAYER_ID, NAME, GAME_SITE, PLAYER_ID_LIST,APP_DATE,ITEMLIST,CONTENT,DETAIL,REASON,STATE,TITLE,AGENT) VALUES(");
		sb.append(appType);
		sb.append(",");
		sb.append(appPlayerID);
		sb.append(",");
		sb.append("'");
		sb.append(name);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(gameSite);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(playerIDList);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(new Timestamp(DateService.getCurrentUtilDate().getTime()));
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(itemList);
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
		sb.append(reason);
		sb.append("'");
		sb.append(",");
		sb.append(state);
		sb.append(",");
		sb.append("'");
		sb.append(title);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(agent);
		sb.append("'");
		sb.append(")");
		
		
		return sb.toString();
	}
	
	
	public int getAppItemId() {
		return appItemId;
	}
	public void setAppItemId(int appItemId) {
		this.appItemId = appItemId;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}
	public String getGameSite() {
		return gameSite;
	}
	public void setGameSite(String gameSite) {
		this.gameSite = gameSite;
	}
	public String getPlayerIDList() {
		return playerIDList;
	}
	public void setPlayerIDList(String playerIDList) {
		this.playerIDList = playerIDList;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public String getItemList() {
		return itemList;
	}
	public void setItemList(String itemList) {
		this.itemList = itemList;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String getStateInfo() {
		return stateInfo;
	}


	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getAppPlayerID() {
		return appPlayerID;
	}


	public void setAppPlayerID(int appPlayerID) {
		this.appPlayerID = appPlayerID;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	


	public String getAgent() {
		return agent;
	}


	public void setAgent(String agent) {
		this.agent = agent;
	}

}
