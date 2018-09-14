package com.domain.puppet;

import java.io.Serializable;

/**
 * 单位属性基类
 * @author ken
 * @date 2017-4-6
 */
public class AbstractPuppet implements Serializable {

	private static final long serialVersionUID = -6600807044441936789L;

	/** 当前血量 */
	private volatile int hp;
	/** 当前魔法 */
	private volatile int mp; 
	
	/** 血量最大值 */
	private int hpMax;
	/** 魔法最大值 */
	private int mpMax;
	/** 物理攻击 */
	private int p_attack;
	/** 魔法攻击 */
	private int m_attack;
	/** 物理防御 */
	private int p_damage;
	/** 魔法防御 */
	private int m_damage;
	/** 暴击 */
	private int crit;
	/** 韧性 */
	private int tough;
	
	/** 伤害加深万分比 */
	private int dmgDeepPer;
	/** 伤害减免万分比 */
	private int dmgReductPer;
	/** 伤害暴击万分比 */
	private int dmgCritPer;
	/** 移动速度 */
	private int moveSpeed;
	
	/** 是否定身*/
	private int fixed;
	/** 是否眩晕*/
	private int vertigo;
	/** 是否隐身*/
	private int invisible;	
	/** 经验倍率*/
	private int expRate;	
	/** 是否免疫*/
	private int immune;
	
	public int getMoveSpeed() {
		return moveSpeed;
	}
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		if(hp < 0){
			hp = 0;
		}
		if(hp > this.hpMax){
			hp = this.hpMax;
		}
		this.hp = hp;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		if(mp < 0){
			 mp = 0;
		}
		if(mp > this.mpMax) {
			mp = this.mpMax;
		}
		this.mp = mp;
	}
	public int getHpMax() {
		return hpMax;
	}
	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
		if(this.hp > hpMax){
			this.hp = hpMax;
		}
	}
	public int getMpMax() {
		return mpMax;
	}
	public void setMpMax(int mpMax) {
		this.mpMax = mpMax;
		if(this.mp > mpMax){
			this.mp = mpMax;
		}
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
	public int getCrit() {
		return crit;
	}
	public void setCrit(int crit) {
		this.crit = crit;
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
	public int getFixed() {
		return fixed;
	}
	public void setFixed(int fixed) {
		this.fixed = fixed;
	}
	public int getVertigo() {
		return vertigo;
	}
	public void setVertigo(int vertigo) {
		this.vertigo = vertigo;
	}
	public int getInvisible() {
		return invisible;
	}
	public void setInvisible(int invisible) {
		this.invisible = invisible;
	}
	public int getExpRate() {
		return expRate;
	}
	public void setExpRate(int expRate) {
		this.expRate = expRate;
	}
	public int getImmune() {
		return immune;
	}
	public void setImmune(int immune) {
		this.immune = immune;
	}
	
}
