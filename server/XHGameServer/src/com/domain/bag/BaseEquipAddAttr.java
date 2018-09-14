package com.domain.bag;

import java.io.Serializable;
import java.util.List;

/**
 * 装备附加属性基础表
 * @author jiangqin
 * @date 2017-4-2
 */
public class BaseEquipAddAttr implements Serializable {
	
	private static final long serialVersionUID = -5689258810180602205L;
	
	/** 段位ID*/
	private int id;
	/** 属性子串*/
	private String addAttr;
	/** 属性列表*/
	private List<AddAttr> addAttrList;

	public String getAddAttr() {
		return addAttr;
	}

	public void setAddAttr(String addAttr) {
		this.addAttr = addAttr;
	}

	public List<AddAttr> getAddAttrList() {
		return addAttrList;
	}

	public void setAddAttrList(List<AddAttr> addAttrList) {
		this.addAttrList = addAttrList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
