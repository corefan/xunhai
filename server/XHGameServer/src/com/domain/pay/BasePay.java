package com.domain.pay;

import java.io.Serializable;

/**
 * 充值配置
 * @author ken
 * @date 2017-6-23
 */
public class BasePay implements Serializable {

	private static final long serialVersionUID = -8626239504686617034L;

	/** 商品编号*/
	private Integer id;
	/** 支付金额*/
	private int price;
	/** 获得元宝*/
	private int gold;
	/** 首充获得元宝*/
	private int premium;
	/** 是否月卡*/
	private int type;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getPremium() {
		return premium;
	}
	public void setPremium(int premium) {
		this.premium = premium;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
