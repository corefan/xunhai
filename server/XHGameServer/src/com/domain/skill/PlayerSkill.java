package com.domain.skill;


import com.domain.GameEntity;

/**
 * 玩家技能
 * @author ken
 * @date 2017-2-6
 */
public class PlayerSkill extends GameEntity {

	private static final long serialVersionUID = 7720053597195151483L;

	/** 唯一编号*/
	private long id;
	/** 玩家编号*/
	private long playerId;
	/** 技能编号*/
	private int skillId;
	/** 技能等级*/
	private int level;
	/** 当前熟练度*/
	private int mastery;	

	/** 技能类型*/
	private int skillIndex;
	/** 铭文技能ID*/
	private int mwSkillId;
	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder(1 << 4);
		
		sql.append("INSERT INTO player_skill ");
		sql.append("(id, playerId, skillId, level, mastery, skillIndex, mwSkillId) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(skillId);
		sql.append(",");
		sql.append(level);
		sql.append(",");
		sql.append(mastery);
		sql.append(",");
		sql.append(skillIndex);
		sql.append(",");
		sql.append(mwSkillId);
		sql.append(")");		
		return sql.toString();
	}
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_skill SET ");
		sql.append("skillId = ");
		sql.append(skillId);
		sql.append(",");
		sql.append("level = ");
		sql.append(level);
		sql.append(",");
		sql.append("mastery = ");
		sql.append(mastery);
		sql.append(",");
		sql.append("skillIndex = ");
		sql.append(skillIndex);
		sql.append(",");
		sql.append("mwSkillId = ");
		sql.append(mwSkillId);
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	public int getMastery() {
		return mastery;
	}
	public void setMastery(int mastery) {
		this.mastery = mastery;
	}
	public int getSkillIndex() {
		return skillIndex;
	}
	public void setSkillIndex(int skillIndex) {
		this.skillIndex = skillIndex;
	}
	public int getMwSkillId() {
		return mwSkillId;
	}
	public void setMwSkillId(int mwSkillId) {
		this.mwSkillId = mwSkillId;
	}
}
