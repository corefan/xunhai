package com.domain.rank;

import java.io.Serializable;

/**
 * 战力榜
 * @author ken
 * @date 2017-5-8
 */
public class BattleValueRank implements Serializable {

	private static final long serialVersionUID = -6491834176598660439L;

	private int rank;     //	排名
	private long playerId; // 玩家编号
	private String playerName; // 玩家名称
	private int career;     //玩家职业  
	private int level;      //玩家等级
	private String guildName = "";   //公会名称
	private int value;     //战力
	
//  可能跨服有用
//	/** 全局唯一标示 */
//	private String guid;
//	/** 所在区服编号*/
//	private int severNo;
//	/** site */
//	private String gameSite;
	
	
	public int getRank() {
		return rank;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getGuildName() {
		return guildName;
	}
	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
