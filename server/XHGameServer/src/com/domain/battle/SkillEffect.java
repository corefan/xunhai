package com.domain.battle;

import java.io.Serializable;

/**
 * 技能单位受击效果
 * @author ken
 * @date 2017-1-12
 */
public class SkillEffect implements Serializable {

	private static final long serialVersionUID = 7188983146552468344L;

	/** 受击者*/
	private String targetId;
	/** 造成伤害*/
	private int dmg;
	/** 受击者剩余血量*/
	private int hp;
	/** 技能结果  0-成功 1-MISS 2-跳闪 4-暴击 6-跳闪+暴击 8-格挡 12-格挡+暴击 16 无敌 32 死亡中被打 64 被秒杀*/
	private int fightResult;
	
	
	
	public SkillEffect() {
		
	}
	
	public SkillEffect(String targetId, int dmg, int hp, int fightResult) {
		this.targetId = targetId;
		this.dmg = dmg;
		this.hp = hp;
		this.fightResult = fightResult;
	}


	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public int getDmg() {
		return dmg;
	}
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getFightResult() {
		return fightResult;
	}
	public void setFightResult(int fightResult) {
		this.fightResult = fightResult;
	}
	
}
