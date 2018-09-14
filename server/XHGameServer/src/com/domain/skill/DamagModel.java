package com.domain.skill;

import java.io.Serializable;
import java.util.List;


/** 技能伤害模块*/
public class DamagModel implements Serializable {

	private static final long serialVersionUID = -6767252608511705843L;
	
	/** 技能模块编号*/
	private int  un32SkillModelID;
	/** 模块类型 @SkillConstant*/
	private int  eSkillModelType;
	/** 结算类型*/
	private int  eEffectCate;
	/** 伤害属相  */
	private int  elementType;
	/** 攻击者伤害增加值（可以是攻击、防御、血量等属性的固定值，默认0）*/
	private int  n32EffectAdd;
	/** 攻击者伤害增加比（可以是攻击、防御、血量等属性的千分比，默认1000）*/
	private int  n32EffectRate;
	/** 受击者伤害增加值*/
	private int  n32HurtAdd;
	/** 受击者伤害增加比*/
	private int  n32HurtRate;
	/** 打断等级*/
	private int  interrupt;
	
	/** 给别人触发buff列表*/
	private String buffIds;
	private List<Integer> buffIdList;
	
	/** 给自己buff*/
	private String myBuffIds;
	private List<Integer> myBuffIdList;
	
	public int getUn32SkillModelID() {
		return un32SkillModelID;
	}
	public void setUn32SkillModelID(int un32SkillModelID) {
		this.un32SkillModelID = un32SkillModelID;
	}
	public int geteSkillModelType() {
		return eSkillModelType;
	}
	public void seteSkillModelType(int eSkillModelType) {
		this.eSkillModelType = eSkillModelType;
	}
	public int geteEffectCate() {
		return eEffectCate;
	}
	public void seteEffectCate(int eEffectCate) {
		this.eEffectCate = eEffectCate;
	}
	public int getElementType() {
		return elementType;
	}
	public void setElementType(int elementType) {
		this.elementType = elementType;
	}
	public int getN32EffectAdd() {
		return n32EffectAdd;
	}
	public void setN32EffectAdd(int n32EffectAdd) {
		this.n32EffectAdd = n32EffectAdd;
	}
	public int getN32EffectRate() {
		return n32EffectRate;
	}
	public void setN32EffectRate(int n32EffectRate) {
		this.n32EffectRate = n32EffectRate;
	}
	public int getN32HurtAdd() {
		return n32HurtAdd;
	}
	public void setN32HurtAdd(int n32HurtAdd) {
		this.n32HurtAdd = n32HurtAdd;
	}
	public int getN32HurtRate() {
		return n32HurtRate;
	}
	public void setN32HurtRate(int n32HurtRate) {
		this.n32HurtRate = n32HurtRate;
	}
	public int getInterrupt() {
		return interrupt;
	}
	public void setInterrupt(int interrupt) {
		this.interrupt = interrupt;
	}
	public String getBuffIds() {
		return buffIds;
	}
	public void setBuffIds(String buffIds) {
		this.buffIds = buffIds;
	}
	public List<Integer> getBuffIdList() {
		return buffIdList;
	}
	public void setBuffIdList(List<Integer> buffIdList) {
		this.buffIdList = buffIdList;
	}
	public String getMyBuffIds() {
		return myBuffIds;
	}
	public void setMyBuffIds(String myBuffIds) {
		this.myBuffIds = myBuffIds;
	}
	public List<Integer> getMyBuffIdList() {
		return myBuffIdList;
	}
	public void setMyBuffIdList(List<Integer> myBuffIdList) {
		this.myBuffIdList = myBuffIdList;
	}
	
}
