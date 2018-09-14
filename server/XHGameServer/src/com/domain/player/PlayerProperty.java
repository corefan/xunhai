package com.domain.player;

import com.domain.GameEntity;

/**
 * 角色属性
 * @author ken
 * @date 2016-12-26
 */
public class PlayerProperty extends GameEntity  {

	private static final long serialVersionUID = -8331271582426568865L;

	
	/** 玩家编号 */
	private long playerId;
	/** 玩家等级 */
	private int level;
	/** 经验 */
	private int exp;
	/** 角色战力 */
	private int battleValue;

	/** 技能等级之和*/
	private int skillLv;
	/** ***********************一级属性**********************/
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
	
	/** ***********************二级属性**********************/
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
	
	/** ***********************附加属性**********************/
	/** 伤害加深万分比 */
	private int dmgDeepPer;
	/** 伤害减免万分比 */
	private int dmgReductPer;
	/** 伤害暴击万分比 */
	private int dmgCritPer;
	/** 移动速度 */
	private int moveSpeed;
	
	@Override
	public String getInsertSql() {
		return null;
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {

		StringBuilder sql = new StringBuilder(1 << 18);
		sql.append("UPDATE player_property SET ");

		sql.append(" level = ");
		sql.append(level);
		sql.append(",");
		sql.append(" exp = ");
		sql.append(exp);
		sql.append(",");
		sql.append(" battleValue = ");
		sql.append(battleValue);
		sql.append(",");
		sql.append(" skillLv = ");
		sql.append(skillLv);
		sql.append(",");
		
		sql.append(" strength = ");
		sql.append(strength);
		sql.append(",");
		sql.append(" intelligence = ");
		sql.append(intelligence);
		sql.append(",");
		sql.append(" endurance = ");
		sql.append(endurance);
		sql.append(",");
		sql.append(" spirit = ");
		sql.append(spirit);
		sql.append(",");
		sql.append(" lucky = ");
		sql.append(lucky);
		sql.append(",");
		
		sql.append(" hpMax = ");
		sql.append(hpMax);
		sql.append(",");
		sql.append(" mpMax = ");
		sql.append(mpMax);
		sql.append(",");
		sql.append(" p_attack = ");
		sql.append(p_attack);
		sql.append(",");
		sql.append(" m_attack = ");
		sql.append(m_attack);
		sql.append(",");
		sql.append(" p_damage = ");
		sql.append(p_damage);
		sql.append(",");
		sql.append(" m_damage = ");
		sql.append(m_damage);
		sql.append(",");
		sql.append(" crit = ");
		sql.append(crit);
		sql.append(",");
		sql.append(" tough = ");
		sql.append(tough);
		sql.append(",");
		
		sql.append(" dmgDeepPer = ");
		sql.append(dmgDeepPer);
		sql.append(",");
		sql.append(" dmgReductPer = ");
		sql.append(dmgReductPer);
		sql.append(",");
		sql.append(" dmgCritPer = ");
		sql.append(dmgCritPer);
		sql.append(",");
		sql.append(" moveSpeed = ");
		sql.append(moveSpeed);
		
		sql.append(" WHERE playerId = ");
		sql.append(playerId);

		return sql.toString();
	}
	
	

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getLevel() {
		return level;
	}
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getBattleValue() {
		return battleValue;
	}

	public void setBattleValue(int battleValue) {
		this.battleValue = battleValue;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getMpMax() {
		return mpMax;
	}

	public void setMpMax(int mpMax) {
		this.mpMax = mpMax;
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

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}


	public void setLevel(int level) {
		this.level = level;
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

	public int getSkillLv() {
		return skillLv;
	}

	public void setSkillLv(int skillLv) {
		this.skillLv = skillLv;
	}
}
