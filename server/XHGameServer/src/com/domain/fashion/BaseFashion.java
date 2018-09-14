package com.domain.fashion;

import java.io.Serializable;
import java.util.List;

/**
 * 时装配置
 * @author ken
 * @date 2017-2-13
 */
public class BaseFashion implements Serializable {

	private static final long serialVersionUID = 3719218796864571857L;

	/** 时装编号*/
	private int fashionId;
	/** 名称*/
	private String name;
	/** 所属职业*/
	private int career;
	/** 外形编号*/
	private int dressStyle;
	/** 增加属性*/
	private String baseProperty;
	private List<List<Integer>> basePropertyList;
	/** 增加BUFF*/
	private String buffId;
	private List<Integer> buffIdList;
	
	public String getBaseProperty() {
		return baseProperty;
	}
	public void setBaseProperty(String baseProperty) {
		this.baseProperty = baseProperty;
	}
	public List<List<Integer>> getBasePropertyList() {
		return basePropertyList;
	}
	public void setBasePropertyList(List<List<Integer>> basePropertyList) {
		this.basePropertyList = basePropertyList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFashionId() {
		return fashionId;
	}
	public void setFashionId(int fashionId) {
		this.fashionId = fashionId;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getDressStyle() {
		return dressStyle;
	}
	public void setDressStyle(int dressStyle) {
		this.dressStyle = dressStyle;
	}
	public String getBuffId() {
		return buffId;
	}
	public void setBuffId(String buffId) {
		this.buffId = buffId;
	}
	public List<Integer> getBuffIdList() {
		return buffIdList;
	}
	public void setBuffIdList(List<Integer> buffIdList) {
		this.buffIdList = buffIdList;
	}
		
}
