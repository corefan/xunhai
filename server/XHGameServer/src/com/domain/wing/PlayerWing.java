package com.domain.wing;

import com.domain.GameEntity;

/**
 * 羽翼
 * @author jiangqin
 * @date 2017-7-18
 */
public class PlayerWing  extends GameEntity {
	
	private static final long serialVersionUID = 786404585982075886L;
	
	/** 自增编号*/
	private long id;
	/** 角色编号*/
	private long playerId;
	/** 翅膀编号*/
	private int wingId;	
	/** 翅膀星级*/
	private int star;
	/** 翅膀灵力值*/
	private int wingValue;
	/** 翅膀评分*/
	private int wingScore;	
	/** 装备标识 1: 已装备*/
	private int dressFlag;
	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_wing ");
		sql.append("(id, playerId, wingId, star, wingValue, wingScore, dressFlag) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(wingId);
		sql.append(",");
		sql.append(star);
		sql.append(",");
		sql.append(wingValue);	
		sql.append(",");
		sql.append(wingScore);	
		sql.append(",");
		sql.append(dressFlag);	
		sql.append(")");		
		return sql.toString();
	}
	
	
	/** 更新sql */	
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_wing SET ");
		sql.append("playerId = ");
		sql.append(playerId);
		sql.append(",");
		sql.append("wingId = ");
		sql.append(wingId);
		sql.append(",");
		sql.append("star = ");
		sql.append(star);
		sql.append(",");
		sql.append("wingValue = ");
		sql.append(wingValue);
		sql.append(",");		
		sql.append("wingScore = ");
		sql.append(wingScore);
		sql.append(",");		
		sql.append("dressFlag = ");
		sql.append(dressFlag);
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

	public int getWingId() {
		return wingId;
	}
	public void setWingId(int wingId) {
		this.wingId = wingId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getWingValue() {
		return wingValue;
	}
	public void setWingValue(int wingValue) {
		this.wingValue = wingValue;
	}
	public int getDressFlag() {
		return dressFlag;
	}
	public void setDressFlag(int dressFlag) {
		this.dressFlag = dressFlag;
	}
	public int getWingScore() {
		return wingScore;
	}
	public void setWingScore(int wingScore) {
		this.wingScore = wingScore;
	}

}
