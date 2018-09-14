package com.domain.wing;

public class BaseWingUpStar {	
	/** 星级*/
	private int star;	
	/** 升星需求羽灵值*/
	private int needExp;		
	/** 提升属性值(万分比)*/
	private int addAttr;

	public BaseWingUpStar(){
		
	}
	
	public BaseWingUpStar(int star, int needExp, int addAttr){
		this.star = star;
		this.needExp = needExp;		
		this.addAttr = addAttr;
	}
	
	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	public int getAddAttr() {
		return addAttr;
	}

	public void setAddAttr(int addAttr) {
		this.addAttr = addAttr;
	}
}
