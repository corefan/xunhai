package com.domain.furnace;


import com.domain.GameEntity;

/**
 * 玩家熔炉
 * @author ken
 * @date 2018年4月23日
 */
public class PlayerFurnace extends GameEntity {

	private static final long serialVersionUID = -8294117911081315741L;
	
	/** 唯一编号*/
	private long id;
	/** 玩家编号*/
	private long playerId;
	/** 熔炉编号*/
	private int furnaceId;
	/** 阶段*/
	private int stage;
	/** 星级*/	
	private int star;
	/** 碎片数*/
	private int piece;
	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_furnace ");
		sql.append("(id, playerId, furnaceId, stage, star, piece) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(furnaceId);
		sql.append(",");
		sql.append(stage);
		sql.append(",");	
		sql.append(star);	
		sql.append(",");	
		sql.append(piece);
		sql.append(")");		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_furnace SET ");
		sql.append("stage = ");
		sql.append(stage);
		sql.append(",");	
		sql.append("star = ");
		sql.append(star);
		sql.append(",");	
		sql.append("piece = ");
		sql.append(piece);
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
	public int getFurnaceId() {
		return furnaceId;
	}
	public void setFurnaceId(int furnaceId) {
		this.furnaceId = furnaceId;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getPiece() {
		return piece;
	}
	public void setPiece(int piece) {
		this.piece = piece;
	}
	
}
