package com.domain.friend;


import com.domain.GameEntity;

/**
 * 玩家好友数据
 * @author ken
 * @date 2016-12-27
 */
public class PlayerFriend extends GameEntity {

		
	private static final long serialVersionUID = -252208408667161392L;
	
	/** 唯一编号*/
	private long id;
	/** 玩家编号 */
	private long playerId;
	/** 玩家好友编号 */
	private long friendPlayerId;
	/** 玩家好友类型1: 好友, 2: 江湖好友*/
	private int type;	
	/** 是否删除*/
	private int deleteFlag;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 11);
		
		sql.append("INSERT INTO player_friend ");
		sql.append("(id, playerId, friendPlayerId, type, deleteFlag) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(friendPlayerId);
		sql.append(",");
		sql.append(type);	
		sql.append(",");
		sql.append(deleteFlag);
		sql.append(")");		
		return sql.toString();
	}
	/**
	 * 获得更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();			
		sql.append("UPDATE player_friend SET ");
		sql.append(" friendPlayerId=");
		sql.append(friendPlayerId);		
		sql.append(",");			
		sql.append(" type=");
		sql.append(type);		
		sql.append(",");			
		sql.append(" deleteFlag=");
		sql.append(deleteFlag);	
		sql.append(" WHERE id=");
		sql.append(id);
		
		return sql.toString();
	} 
	

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getFriendPlayerId() {
		return friendPlayerId;
	}
	public void setFriendPlayerId(long friendPlayerId) {
		this.friendPlayerId = friendPlayerId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}