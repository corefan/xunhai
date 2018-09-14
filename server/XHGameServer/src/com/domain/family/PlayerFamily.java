package com.domain.family;

import com.domain.GameEntity;

/**
 * 玩家家族信息
 * @author jiangqin
 * @date 2017-4-6
 */
public class PlayerFamily  extends GameEntity {

	private static final long serialVersionUID = -3490157028261428976L;	
	
	/** 角色编号*/
	private long playerId;
	
	/** 家族唯一ID*/
	private long playerFamilyId;
	
	/** 角色家族职位*/
	private int familyPosId;
	
	/** 角色家族排序位*/
	private int familySortId;	
	
	/** 角色家族称谓*/
	private String familyTitle;
	
	/** 临时位置排序*/
	private int position;
	
	@Override
	public String getInsertSql() {
		return null;
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_family SET ");
		sql.append("playerFamilyId = ");
		sql.append(playerFamilyId);
		sql.append(",");
		sql.append("familyPosId = ");
		sql.append(familyPosId);
		sql.append(",");
		sql.append("familySortId = ");
		sql.append(familySortId);
		sql.append(",");
		sql.append("familyTitle = ");
		if (familyTitle == null) {
			sql.append(familyTitle);
		} else {
			sql.append("'");
			sql.append(familyTitle);
			sql.append("'");
		}	
		sql.append(" WHERE playerId = ");
		sql.append(playerId);
		
		return sql.toString();
	}
	
	public void reset(){
		playerFamilyId = 0;
		familyPosId = 0;
		familySortId= 0;		
		familyTitle = "";
		position = 0;
	}	

	public long getPlayerFamilyId() {
		return playerFamilyId;
	}

	public void setPlayerFamilyId(long playerFamilyId) {
		this.playerFamilyId = playerFamilyId;
	}

	public int getFamilyPosId() {
		return familyPosId;
	}
	
	public int getFamilySortId() {
		return familySortId;
	}	
	
	public void setPlayerFamilyId(int playerFamilyId) {
		this.playerFamilyId = playerFamilyId;
	}
	
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setFamilyPosId(int familyPosId) {
		this.familyPosId = familyPosId;
	}
	
	public void setFamilySortId(int familySortId) {
		this.familySortId = familySortId;
	}	

	public String getFamilyTitle() {
		return familyTitle;
	}

	public void setFamilyTitle(String familyTitle) {
		this.familyTitle = familyTitle;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	
}
