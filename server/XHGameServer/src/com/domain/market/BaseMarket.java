/**
 * 
 */
package com.domain.market;

import java.io.Serializable;

/**
 * 商城基础表
 * @author jiangqin
 * @date 2017-4-18
 */
public class BaseMarket implements Serializable{
	
	private static final long serialVersionUID = -2986780729488297480L;
	
	/** 商城编号ID*/
	private int marketId;
	/** 页签编号ID*/
	private int pageId;
	/** 物品类型*/
	private int itemType;	
	/** 商城物品(装备)编号*/
	private int itemId;
	/** 消耗货币类型*/
	private int moneyType;
	/** 商品价格*/
	private int price;
	/** 商品打折(百分值)*/
	private int discount;
	/** 每日限购数量*/
	private int limitNum;
	/** 上架时间*/
	private String onTime;
	/** 下架时间*/
	private String downTime;
	/** vip限购*/
	private int vipLimit;
	
	
	public int getMarketId() {
		return marketId;
	}
	public int getPageId() {
		return pageId;
	}
	public int getItemId() {
		return itemId;
	}
	public int getMoneyType() {
		return moneyType;
	}
	public int getPrice() {
		return price;
	}
	public int getDiscount() {
		return discount;
	}
	public int getLimitNum() {
		return limitNum;
	}
	public String getOnTime() {
		return onTime;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}
	public void setOnTime(String onTime) {
		this.onTime = onTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public int getItemType() {
		return itemType;
	}
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
	public int getVipLimit() {
		return vipLimit;
	}
	public void setVipLimit(int vipLimit) {
		this.vipLimit = vipLimit;
	}
}
