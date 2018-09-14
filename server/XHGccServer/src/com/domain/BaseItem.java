package com.domain;

import java.io.Serializable;

/**
 * 物品表
 * @author ken
 * @date 2017-1-4
 */
public class BaseItem implements Serializable {

	private static final long serialVersionUID = 8446957006727431558L;

	/** 物品编号*/
	private Integer id;
	/** 物品名字*/
	private String name;
	/** 物品类1=装备 2=妙药 3=珍材*/
	private int goodsType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	
}
