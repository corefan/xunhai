package com.domain.base;

import java.io.Serializable;

/**
 * 系统参数配置表
 * @author ken
 * @date 2016-12-30
 */
public class BaseConstant implements Serializable {
	
	private static final long serialVersionUID = -1452993945783962225L;
	
	/** 编号*/
	private int id;
	/** 参数值*/
	private int value;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
