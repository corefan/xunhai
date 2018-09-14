package com.domain.skill;

import java.io.Serializable;
import java.util.List;

/**
 * 技能总表
 * @author ken
 * @date 2017-1-16
 */
public class BaseSkill implements Serializable {

	private static final long serialVersionUID = 8945549929315109749L;

	/** 技能编号*/
	private int  un32SkillID;	
	/** 名称*/
	private String  name;
	/** 技能等级*/
	private int level;
	/** 技能最大等级*/
	private int levelMax;
	/** 是否普攻 1：是   2：否*/
	private int  bIfNomalAttack;
	/** 消耗魔法*/
	private int  n32UseMP;
	/** 消耗血量*/
	private int  n32UseHP;
	/** 霸体等级   跟打断等级作比较*/
	private int  interruptArmor;
	/** 技能冷却 毫秒*/
	private int  n32CoolDown;
	/** 吟唱时间  毫秒*/
	private int  singTime;
	/** 施法表现类型*/
	private int  previewType;
	/** 后摇时间*/
	private int  n32SkillLastTime;
	/** 攻击范围*/
	private int  fReleaseDist;
	/** 技能触发方式*/
	private int  eUseWay;
	/** 触发概率*/
	private int  n32TriggerRate;
	/** 技能施放时所选择的目标类型  @SkillConstant*/
	private int  eSkillTargetCate;
	/** 技能执行效果列表*/
	private String  asSkillModelList;
	/** 调用等待时间*/
	private String  n32Delay;
	
	/** 技能执行效果列表*/
	private List<Integer> asSkillModels;
	/** 调用等待时间*/
	private List<Integer> n32Delays;
	
	/** 有效时间*/
	private int n32LifeTime;
	
	/**技能类型(技能类型)*/
	private int skillIndex;
	
	/**技能基础编号*/
	private int baseSkillId;
	
	/** 技能类型*/
	private int skillType;
	
	public int getUn32SkillID() {
		return un32SkillID;
	}
	public void setUn32SkillID(int un32SkillID) {
		this.un32SkillID = un32SkillID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getbIfNomalAttack() {
		return bIfNomalAttack;
	}
	public void setbIfNomalAttack(int bIfNomalAttack) {
		this.bIfNomalAttack = bIfNomalAttack;
	}
	public int getN32UseMP() {
		return n32UseMP;
	}
	public void setN32UseMP(int n32UseMP) {
		this.n32UseMP = n32UseMP;
	}
	public int getN32UseHP() {
		return n32UseHP;
	}
	public void setN32UseHP(int n32UseHP) {
		this.n32UseHP = n32UseHP;
	}
	public int getInterruptArmor() {
		return interruptArmor;
	}
	public void setInterruptArmor(int interruptArmor) {
		this.interruptArmor = interruptArmor;
	}
	public int getN32CoolDown() {
		return n32CoolDown;
	}
	public void setN32CoolDown(int n32CoolDown) {
		this.n32CoolDown = n32CoolDown;
	}
	public int getSingTime() {
		return singTime;
	}
	public void setSingTime(int singTime) {
		this.singTime = singTime;
	}
	public int getPreviewType() {
		return previewType;
	}
	public void setPreviewType(int previewType) {
		this.previewType = previewType;
	}
	public int getN32SkillLastTime() {
		return n32SkillLastTime;
	}
	public void setN32SkillLastTime(int n32SkillLastTime) {
		this.n32SkillLastTime = n32SkillLastTime;
	}
	public int getfReleaseDist() {
		return fReleaseDist;
	}
	public void setfReleaseDist(int fReleaseDist) {
		this.fReleaseDist = fReleaseDist;
	}
	public int geteUseWay() {
		return eUseWay;
	}
	public void seteUseWay(int eUseWay) {
		this.eUseWay = eUseWay;
	}
	public int getN32TriggerRate() {
		return n32TriggerRate;
	}
	public void setN32TriggerRate(int n32TriggerRate) {
		this.n32TriggerRate = n32TriggerRate;
	}
	public int geteSkillTargetCate() {
		return eSkillTargetCate;
	}
	public void seteSkillTargetCate(int eSkillTargetCate) {
		this.eSkillTargetCate = eSkillTargetCate;
	}
	public String getAsSkillModelList() {
		return asSkillModelList;
	}
	public void setAsSkillModelList(String asSkillModelList) {
		this.asSkillModelList = asSkillModelList;
	}
	public String getN32Delay() {
		return n32Delay;
	}
	public void setN32Delay(String n32Delay) {
		this.n32Delay = n32Delay;
	}
	public List<Integer> getAsSkillModels() {
		return asSkillModels;
	}
	public void setAsSkillModels(List<Integer> asSkillModels) {
		this.asSkillModels = asSkillModels;
	}
	public List<Integer> getN32Delays() {
		return n32Delays;
	}
	public void setN32Delays(List<Integer> n32Delays) {
		this.n32Delays = n32Delays;
	}
	public int getN32LifeTime() {
		return n32LifeTime;
	}
	public void setN32LifeTime(int n32LifeTime) {
		this.n32LifeTime = n32LifeTime;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevelMax() {
		return levelMax;
	}
	public void setLevelMax(int levelMax) {
		this.levelMax = levelMax;
	}
	public int getSkillIndex() {
		return skillIndex;
	}
	public void setSkillIndex(int skillIndex) {
		this.skillIndex = skillIndex;
	}
	public int getBaseSkillId() {
		return baseSkillId;
	}
	public void setBaseSkillId(int baseSkillId) {
		this.baseSkillId = baseSkillId;
	}
	public int getSkillType() {
		return skillType;
	}
	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}
	
}
