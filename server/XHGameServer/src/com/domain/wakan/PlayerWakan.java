package com.domain.wakan;


import com.domain.GameEntity;

/**
 * 注灵数据结构
 * @author jiangqin
 * @date 2017-2-21
 */
public class PlayerWakan extends GameEntity {

	private static final long serialVersionUID = -5757658689584483224L;

	private long id;  //注灵自增编号
	private long playerId; //角色编号ID
	private int posId; //装备位ID
	private int wakanLevel; //注灵等级
	private int wakanValue; //灵力值
	
	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_wakan ");
		sql.append("(id, playerId, posId, wakanLevel, wakanValue) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(posId);
		sql.append(",");
		sql.append(wakanLevel);
		sql.append(",");
		sql.append(wakanValue);		
		sql.append(")");		
		return sql.toString();
	}
	
	/** 得到更新sql */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_wakan SET ");
		sql.append("posId = ");
		sql.append(posId);
		sql.append(",");
		sql.append("wakanLevel = ");
		sql.append(wakanLevel);
		sql.append(",");
		sql.append("wakanValue = ");
		sql.append(wakanValue);		
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getPosId() {
		return posId;
	}
	public void setPosId(int posId) {
		this.posId = posId;
	}
	public int getWakanLevel() {
		return wakanLevel;
	}
	public void setWakanLevel(int wakanLevel) {
		this.wakanLevel = wakanLevel;
	}
	public int getWakanValue() {
		return wakanValue;
	}
	public void setWakanValue(int wakanValue) {
		this.wakanValue = wakanValue;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
