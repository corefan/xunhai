package com.domain.bag;

import java.io.Serializable;

/**
 * 装备附加属性
 * @author jiangqin
 * @date 2017-4-2
 */
public class AddAttr  implements Serializable{
	
	private static final long serialVersionUID = -7963710206785438149L;
	
	private int attrId;
	private int minAttrValue;
	private int maxAttrValue;
	
	public int getAttrId() {
		return attrId;
	}
	public void setAttrId(int attrId) {
		this.attrId = attrId;
	}
	public int getMinAttrValue() {
		return minAttrValue;
	}
	public void setMinAttrValue(int minAttrValue) {
		this.minAttrValue = minAttrValue;
	}
	public int getMaxAttrValue() {
		return maxAttrValue;
	}
	public void setMaxAttrValue(int maxAttrValue) {
		this.maxAttrValue = maxAttrValue;
	}
}
