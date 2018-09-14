package com.domain.instance;


import com.domain.GameEntity;

/**
 * 玩家副本记录
 * @author ken
 * @date 2017-3-7
 */
public class PlayerInstance extends GameEntity {

	private static final long serialVersionUID = 758077415692129642L;

	/** 自增编号*/
	private long id;
	/** 玩家编号*/
	private long playerId;
	/** 地图编号*/
	private int mapId;
	/** 已进入次数*/
	private int enterCount;

	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 10);
		
		sql.append("INSERT INTO player_instance ");
		sql.append("(id, playerId, mapId, enterCount) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(mapId);
		sql.append(",");
		sql.append(enterCount);	
		sql.append(")");		
		return sql.toString();
	}
	/**
	 * 获得更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);			
		sql.append("UPDATE player_instance SET ");
		sql.append(" enterCount=");
		sql.append(enterCount);		
		sql.append(" WHERE id=");
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
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getEnterCount() {
		return enterCount;
	}
	public void setEnterCount(int enterCount) {
		this.enterCount = enterCount;
	}
	
}
