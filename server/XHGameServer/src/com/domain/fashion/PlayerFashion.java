package com.domain.fashion;


import com.domain.GameEntity;

/**
 * 玩家时装翅膀
 * @author ken
 * @date 2017-2-13
 */
public class PlayerFashion extends GameEntity {

	private static final long serialVersionUID = -1262520865275031946L;

	/** 玩家时装编号*/
	private long id;
	/** 玩家编号*/
	private long playerId;
	/** 时装编号*/
	private int fashionId;	
	/** 是否装备*/
	private int dressFlag;
	/** 拥有日期*/
	private long ownDate;

	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_fashion ");
		sql.append("(id, playerId, fashionId, dressFlag, ownDate) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(fashionId);
		sql.append(",");
		sql.append(dressFlag);
		sql.append(",");	
		sql.append(ownDate);		
		sql.append(")");		
		return sql.toString();
	}
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_fashion SET ");
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
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getFashionId() {
		return fashionId;
	}
	public void setFashionId(int fashionId) {
		this.fashionId = fashionId;
	}
	public int getDressFlag() {
		return dressFlag;
	}
	public void setDressFlag(int dressFlag) {
		this.dressFlag = dressFlag;
	}
	public long getOwnDate() {
		return ownDate;
	}
	public void setOwnDate(long ownDate) {
		this.ownDate = ownDate;
	}
	
}
