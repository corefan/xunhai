/**
 * 
 */
package com.domain.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城日志表
 * @author jiangqin
 * @date 2017-8-7
 */
public class MarketLog implements Serializable{
	
	private static final long serialVersionUID = 4787434492915011873L;
	
	/** 自增ID*/
	private long logId; 	
	/** 账号编号*/
	private long userId;
	/** 玩家编号*/
	private long playerId;
	/** 角色名称*/
	private String playerName;
	/** 物品编号*/
	private int itemId;
	/** 物品名称*/
	private String itemName;
	/** 花费元宝（数量*单价）*/
	private int price;
	/** 购买数量*/
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
