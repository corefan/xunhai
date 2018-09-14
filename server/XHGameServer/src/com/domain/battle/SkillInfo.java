package com.domain.battle;

import java.io.Serializable;
import java.util.List;

import com.domain.puppet.BasePuppet;

/**
 * 释放技能信息
 * @author ken
 * @date 2017-1-12
 */
public class SkillInfo implements Serializable {

	private static final long serialVersionUID = -9205332368596851149L;

	/**  技能使用者*/
	private String guid;
	/**  技能编号*/
	private int skillId; 
	/** 技能受击列表*/
	private List<BasePuppet> targetList;
	/** 结算模块编号*/
	private int accountModelId;
	/** 释放时间 */
	private long releaseTime;
	/** 地效唯一编号*/
	private int wigId;
	
	public SkillInfo() {
		
	}
	
	public SkillInfo(String guid, int skillId,List<BasePuppet> targetList, int accountModelId, int wigId) {
		this.guid = guid;
		this.skillId = skillId;
		this.targetList = targetList;
		this.accountModelId = accountModelId;
		this.wigId = wigId;
		this.releaseTime = System.currentTimeMillis();
	}


	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public List<BasePuppet> getTargetList() {
		return targetList;
	}
	public void setTargetList(List<BasePuppet> targetList) {
		this.targetList = targetList;
	}
	public long getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

	public int getAccountModelId() {
		return accountModelId;
	}

	public void setAccountModelId(int accountModelId) {
		this.accountModelId = accountModelId;
	}

	public int getWigId() {
		return wigId;
	}

	public void setWigId(int wigId) {
		this.wigId = wigId;
	} 
	
}
