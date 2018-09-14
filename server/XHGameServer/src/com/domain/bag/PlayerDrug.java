package com.domain.bag;

import com.domain.GameEntity;
import com.util.IDUtil;

/**
 * 玩家药品栏
 * @author ken
 * @date 2017-1-24
 */
public class PlayerDrug extends GameEntity {

	private static final long serialVersionUID = 6335888084787099423L;

	/** 唯一编号*/
	private long id;
	/** 玩家编号*/
	private long playerId;
	/** 1：红药栏  2：蓝药栏*/
	private int type;
	/** 栏下标*/
	private int itemIndex;
	/** 存放物品编号*/
	private int itemId;
	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder(1 << 5);
		
		sql.append("INSERT INTO player_drug ");
		sql.append("(id, playerId, type, itemIndex, itemId) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(type);
		sql.append(",");
		sql.append(itemIndex);
		sql.append(",");
		sql.append(itemId);
		sql.append(")");		
		return sql.toString();
	}
	
	
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_drug SET ");
		sql.append("itemId = ");
		sql.append(itemId);
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}
	
	
	public PlayerDrug() {
		
	}

	public PlayerDrug(long playerId, int type, int itemIndex, int itemId) {
		this.id = IDUtil.geneteId(PlayerDrug.class);
		this.playerId = playerId;
		this.type = type;
		this.itemIndex = itemIndex;
		this.itemId = itemId;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getItemIndex() {
		return itemIndex;
	}
	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
}
