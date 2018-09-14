package com.domain.epigraph;

import com.domain.GameEntity;

/**
 * 武器孔位附加效果信息
 * @author jiangqin
 * @date 2017-3-22
 */ 
public class PlayerWeaponEffect  extends GameEntity {

	private static final long serialVersionUID = -1396557492014157764L;
	
	/** 铭文唯一自增编号*/
	private long id;	
	/** 玩家角色编号*/
	private long playerId;	
	/** 孔位ID*/
	private int holdId;		
	/** 附加效果编号ID*/
	private int effectId;
	/** 1:技能  2：属性*/
	private int type;
	/** 技能基础编号或属性编号*/
	private int baseId;	
	/** 技能下标或属性值*/
	private int proValue;
	
	/** 
	 * 清理
	 */
	public void clear(){
		effectId = 0;
		type = 0;
		baseId = 0;
		proValue = 0;
	}
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_weapon_effect ");
		sql.append("(id, playerId, holdId, effectId, type, baseId, proValue) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");	
		sql.append(playerId);
		sql.append(",");		
		sql.append(holdId);	
		sql.append(",");		
		sql.append(effectId);
		sql.append(",");		
		sql.append(type);	
		sql.append(",");		
		sql.append(baseId);
		sql.append(",");		
		sql.append(proValue);
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_weapon_effect SET ");
		sql.append("holdId = ");		
		sql.append(holdId);		
		sql.append(",");
		sql.append("effectId = ");		
		sql.append(effectId);
		sql.append(",");	
		sql.append("type = ");		
		sql.append(type);
		sql.append(",");
		sql.append("baseId = ");		
		sql.append(baseId);	
		sql.append(",");		
		sql.append("proValue = ");		
		sql.append(proValue);	
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}
	
	public int getHoldId() {
		return holdId;
	}
	public void setHoldId(int holdId) {
		this.holdId = holdId;
	}

	public int getBaseId() {
		return baseId;
	}
	public void setBaseId(int baseId) {
		this.baseId = baseId;
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

	public int getEffectId() {
		return effectId;
	}
	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}
	public int getProValue() {
		return proValue;
	}
	public void setProValue(int proValue) {
		this.proValue = proValue;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
