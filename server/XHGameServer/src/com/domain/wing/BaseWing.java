package com.domain.wing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.Reward;


public class BaseWing {
	
	/** 翅膀编号*/
	private int wingId;
	
	/** 翅膀名称*/
	private String name;
	
	/** 翅膀外形*/
	private int dressStyle;
	
	/** 翅膀分解*/
	private String decomposeStr;	
	private List<Reward> decomposeList;
	
	/** 基础属性*/
	private String baseProperty;
	private List<List<Integer>> basePropertyList;
	
	/** 羽化消耗*/
	private String expendStr;
	private List<List<Integer>> expendStrList;
	
	/** 升星消耗*/
	private String upStarStr;	
	private Map<Integer, BaseWingUpStar> upStarMap = new HashMap<Integer, BaseWingUpStar>();

	public int getWingId() {
		return wingId;
	}

	public void setWingId(int wingId) {
		this.wingId = wingId;
	}

	public String getBaseProperty() {
		return baseProperty;
	}

	public void setBaseProperty(String baseProperty) {
		this.baseProperty = baseProperty;
	}

	public List<List<Integer>> getBasePropertyList() {
		return basePropertyList;	}

	public void setBasePropertyList(List<List<Integer>> basePropertyList) {
		this.basePropertyList = basePropertyList;
	}

	public List<Reward> getDecomposeList() {
		return decomposeList;
	}

	public void setDecomposeList(List<Reward> decomposeList) {
		this.decomposeList = decomposeList;
	}

	public String getDecomposeStr() {
		return decomposeStr;
	}

	public void setDecomposeStr(String decomposeStr) {
		this.decomposeStr = decomposeStr;
	}

	public String getExpendStr() {
		return expendStr;
	}

	public void setExpendStr(String expendStr) {
		this.expendStr = expendStr;
	}

	public String getUpStarStr() {
		return upStarStr;
	}

	public void setUpStarStr(String upStarStr) {
		this.upStarStr = upStarStr;
	}

	public List<List<Integer>> getExpendStrList() {
		return expendStrList;
	}

	public void setExpendStrList(List<List<Integer>> expendStrList) {
		this.expendStrList = expendStrList;
	}

	public Map<Integer, BaseWingUpStar> getUpStarMap() {
		return upStarMap;
	}

	public void setUpStarMap(Map<Integer, BaseWingUpStar> upStarMap) {
		this.upStarMap = upStarMap;
	}

	public int getDressStyle() {
		return dressStyle;
	}

	public void setDressStyle(int dressStyle) {
		this.dressStyle = dressStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
