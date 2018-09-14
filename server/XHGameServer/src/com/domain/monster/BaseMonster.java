package com.domain.monster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.domain.Reward;

/**
 * 怪物配置
 * @author ken
 * @date 2017-1-7
 */
public class BaseMonster implements Serializable {

	private static final long serialVersionUID = 5219720220282261537L;

	/** 怪物编号*/
	private int id;
	/** 名称*/
	private String name;
	/** 怪物类型   (1普通小怪,2精英 3.BOSS,4只跑不打 5木桩类怪物)*/
	private int monsterType;
	/** 怪物攻击模式(1主动攻击, 2被动攻击 )*/
	private int evasiveStyle;
	/** 怪物造型*/
	private int sculptResid;
	/** 击杀怪物所得经验的基础数值*/
	private int experience;
	/** 怪物等级*/
	private int lv;
	/** 生命上限*/
	private int hp;
	/** 物攻*/
	private int p_attack;
	/** 魔攻*/
	private int m_attack;
	/** 物防*/
	private int p_damage;
	/** 魔防*/
	private int m_damage;
	/** 暴击*/
	private int crt;
	/** 韧性 */
	private int tough;
	/** 伤害加深万分比 */
	private int dmgDeepPer;
	/** 伤害减免万分比 */
	private int dmgReductPer;
	/** 伤害暴击万分比 */
	private int dmgCritPer;
	/** 移动速度*/
	private int speed;
	
	/** 怪物ai组*/	
	private String ai;
	private List<BaseAiDetermine> aiList = new ArrayList<BaseAiDetermine>();
	
	/** 掉落归属类型  0：伤害最高者及其队友   1：所有参与者    2 所有者，无归属  */
	private int dropType;
	/** 掉落信息*/
	private String dropInfo;
	private List<Reward> dropInfoList;
	
	/** 是否免疫debuff*/	
	private int immuneDebuff;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(int monsterType) {
		this.monsterType = monsterType;
	}
	public int getEvasiveStyle() {
		return evasiveStyle;
	}
	public void setEvasiveStyle(int evasiveStyle) {
		this.evasiveStyle = evasiveStyle;
	}
	public int getSculptResid() {
		return sculptResid;
	}
	public void setSculptResid(int sculptResid) {
		this.sculptResid = sculptResid;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getP_attack() {
		return p_attack;
	}
	public void setP_attack(int p_attack) {
		this.p_attack = p_attack;
	}
	public int getM_attack() {
		return m_attack;
	}
	public void setM_attack(int m_attack) {
		this.m_attack = m_attack;
	}
	public int getP_damage() {
		return p_damage;
	}
	public void setP_damage(int p_damage) {
		this.p_damage = p_damage;
	}
	public int getM_damage() {
		return m_damage;
	}
	public void setM_damage(int m_damage) {
		this.m_damage = m_damage;
	}
	public int getCrt() {
		return crt;
	}
	public void setCrt(int crt) {
		this.crt = crt;
	}
	public int getTough() {
		return tough;
	}
	public void setTough(int tough) {
		this.tough = tough;
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
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public String getAi() {
		return ai;
	}
	public void setAi(String ai) {
		this.ai = ai;
	}
	public List<BaseAiDetermine> getAiList() {
		return aiList;
	}
	public void setAiList(List<BaseAiDetermine> aiList) {
		this.aiList = aiList;
	}
	public String getDropInfo() {
		return dropInfo;
	}
	public void setDropInfo(String dropInfo) {
		this.dropInfo = dropInfo;
	}
	public List<Reward> getDropInfoList() {
		return dropInfoList;
	}
	public void setDropInfoList(List<Reward> dropInfoList) {
		this.dropInfoList = dropInfoList;
	}
	public int getImmuneDebuff() {
		return immuneDebuff;
	}
	public void setImmuneDebuff(int immuneDebuff) {
		this.immuneDebuff = immuneDebuff;
	}
	public int getDropType() {
		return dropType;
	}
	public void setDropType(int dropType) {
		this.dropType = dropType;
	}
	
}
