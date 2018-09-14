package com.domain.enemy;

import com.domain.GameEntity;

public class PlayerEnemy  extends GameEntity {

	private static final long serialVersionUID = 910597105877734916L;
	
	/** 自增编号 */
	private long id;
	/** 玩家编号 */
	private long playerId;	
	/** 玩家仇敌编号 */
	private long enemyPlayerId;
	/** 添加时间 */
	private long addTime;
	/** 删除标识 */
	private int deleteFlag;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_enemy ");
		sql.append("(id, playerId, enemyPlayerId, addTime, deleteFlag) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(enemyPlayerId);
		sql.append(",");
		sql.append(addTime);
		sql.append(",");
		sql.append(deleteFlag);		
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_enemy SET ");
		sql.append("enemyPlayerId = ");
		sql.append(enemyPlayerId);
		sql.append(",");
		sql.append("addTime = ");
		sql.append(addTime);
		sql.append(",");
		sql.append("deleteFlag = ");
		sql.append(deleteFlag);		
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

	public long getEnemyPlayerId() {
		return enemyPlayerId;
	}

	public void setEnemyPlayerId(long enemyPlayerId) {
		this.enemyPlayerId = enemyPlayerId;
	}

	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}	
}
