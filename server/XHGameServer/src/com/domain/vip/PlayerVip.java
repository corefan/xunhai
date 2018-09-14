package com.domain.vip;


import com.domain.GameEntity;

/**
 * 玩家vip数据
 * @author jiangqin
 * @date 2017-6-15
 */
public class PlayerVip extends GameEntity {

	private static final long serialVersionUID = 871809299007395960L;
	
	/** 玩家编号*/
	private long playerId;
	/** 玩家vip等级*/
	private int level;	
	/** vip过期时间*/
	private long vaildTime;
	
	/** VIP激活奖励领取状态 (0:未领取, 1:已领取)*/
	private int vipLv1State;
	private int vipLv2State;
	private int vipLv3State;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_vip ");
		sql.append("(playerId, level, vaildTime, vipLv1State, vipLv2State, vipLv3State) VALUES");
		sql.append(" (");
		sql.append(playerId);
		sql.append(",");
		sql.append(level);
		sql.append(",");
		sql.append(vaildTime);
		sql.append(",");
		sql.append(vipLv1State);	
		sql.append(",");
		sql.append(vipLv2State);
		sql.append(",");
		sql.append(vipLv3State);
		sql.append(")");
		
		return sql.toString();
	}
	
	
	/** 得到更新sql */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_vip SET ");
		sql.append("level = ");
		sql.append(level);
		sql.append(",");
		sql.append("vaildTime = ");
		sql.append(vaildTime);
		sql.append(",");
		sql.append("vipLv1State = ");
		sql.append(vipLv1State);	
		sql.append(",");
		sql.append("vipLv2State = ");
		sql.append(vipLv2State);	
		sql.append(",");
		sql.append("vipLv3State = ");
		sql.append(vipLv3State);	
		sql.append(" WHERE playerId = ");
		sql.append(playerId);
		
		return sql.toString();
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getVaildTime() {
		return vaildTime;
	}
	public void setVaildTime(long vaildTime) {
		this.vaildTime = vaildTime;
	}
	public int getVipLv1State() {
		return vipLv1State;
	}
	public void setVipLv1State(int vipLv1State) {
		this.vipLv1State = vipLv1State;
	}
	public int getVipLv2State() {
		return vipLv2State;
	}
	public void setVipLv2State(int vipLv2State) {
		this.vipLv2State = vipLv2State;
	}
	public int getVipLv3State() {
		return vipLv3State;
	}
	public void setVipLv3State(int vipLv3State) {
		this.vipLv3State = vipLv3State;
	}
}
