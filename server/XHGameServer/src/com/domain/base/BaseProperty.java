package com.domain.base;

import java.io.Serializable;

/**
 * 成长属性配置表
 * @author ken
 * @date 2016-12-29
 */
public class BaseProperty implements Serializable {

	private static final long serialVersionUID = -2120950572165567761L;

	/** 千位数为职业编号  余数为等级 */
	private int levelId;
	/** 升级需要经验 */
	private int needexp;
	/** 自由点数 */
	private int freebiePoints;
	
	/** 力量*/
	private int strength;
	/** 智慧*/
	private int intelligence;
	/** 耐力*/
	private int endurance;
	/** 灵力*/
	private int spirit;
	/** 幸运*/
	private int lucky;
	/** 伤害加深万分比 */
	private int dmgDeepPer;
	/** 伤害减免万分比 */
	private int dmgReductPer;
	/** 伤害暴击万分比 */
	private int dmgCritPer;
	
	
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public int getNeedexp() {
		return needexp;
	}
	public void setNeedexp(int needexp) {
		this.needexp = needexp;
	}
	public int getFreebiePoints() {
		return freebiePoints;
	}
	public void setFreebiePoints(int freebiePoints) {
		this.freebiePoints = freebiePoints;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public int getIntelligence() {
		return intelligence;
	}
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}
	public int getEndurance() {
		return endurance;
	}
	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}
	public int getSpirit() {
		return spirit;
	}
	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}
	public int getLucky() {
		return lucky;
	}
	public void setLucky(int lucky) {
		this.lucky = lucky;
	}
	public int getDmgDeepPer() {
		return dmgDeepPer;
	}
	public void setDmgDeepPer(int dmgDeepPer) {
		this.dmgDeepPer = dmgDeepPer;
	}
	public int getDmgReductPer() {
		return dmgReductPer;
	}
	public void setDmgReductPer(int dmgReductPer) {
		this.dmgReductPer = dmgReductPer;
	}
	public int getDmgCritPer() {
		return dmgCritPer;
	}
	public void setDmgCritPer(int dmgCritPer) {
		this.dmgCritPer = dmgCritPer;
	}
	
}
