package com.domain.skill;

import java.io.Serializable;

/**
 * 范围技模块
 * @author ken
 * @date 2017-1-18
 */
public class RangeModel implements Serializable {

	private static final long serialVersionUID = -3531394464095215565L;
	
	/** 技能模块编号*/
	private int  un32SkillModelID;
	/** 模块类型 @SkillConstant*/
	private int  eSkillModelType;
	/** 有效时间*/
	private int n32LifeTime;
	
	
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
	public int getN32LifeTime() {
		return n32LifeTime;
	}
	public void setN32LifeTime(int n32LifeTime) {
		this.n32LifeTime = n32LifeTime;
	}
	
}
