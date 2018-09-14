package com.domain.skill;

import java.io.Serializable;

/** 
 * 技能升级配置
 * @author ken
 * @date 2017-2-6
 */
public class BaseSkillUp implements Serializable {

	private static final long serialVersionUID = -4202096902047876667L;

	/** 唯一编号*/
	private int id;
	/** 技能下标*/
	private int skillIndex;
	/** 所属职业*/
	private int clanId;
	/** 所需金币*/
	private int needMoney;
	/** 需要熟练度*/
	private int needMastery;
	/** 所需等级*/
	private int needLevel;
	/** 熟练度系数*/
	private int ratio;
	/** 释放技能所加熟练度*/
	private int addMastery;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSkillIndex() {
		return skillIndex;
	}
	public void setSkillIndex(int skillIndex) {
		this.skillIndex = skillIndex;
	}
	public int getClanId() {
		return clanId;
	}
	public void setClanId(int clanId) {
		this.clanId = clanId;
	}
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	public int getNeedMastery() {
		return needMastery;
	}
	public void setNeedMastery(int needMastery) {
		this.needMastery = needMastery;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	public int getRatio() {
		return ratio;
	}
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	public int getAddMastery() {
		return addMastery;
	}
	public void setAddMastery(int addMastery) {
		this.addMastery = addMastery;
	}

}
