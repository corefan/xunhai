package com.domain.guild;

import java.io.Serializable;

/**
 * 优惠购买
 * @author ken
 * @date 2018年8月2日
 */
public class BaseGuildBuy implements Serializable {

	private static final long serialVersionUID = -2662163308247718146L;

	private Object buyLock = new Object();
	
	/** 物品编号*/
	private int itemId;
	/** 当前价格*/
	private int curPrice;
	/** 限制数量*/
	private int limitNum;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(int curPrice) {
		this.curPrice = curPrice;
	}
	public int getLimitNum() {
		return limitNum;
	}
	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}
	public Object getBuyLock() {
		return buyLock;
	}
	public void setBuyLock(Object buyLock) {
		this.buyLock = buyLock;
	}
	
}
