package com.domain.team;

import java.io.Serializable;

/**
 * 队员数据
 * @author ken
 * @date 2017-3-2
 */
public class TeamPlayer implements Serializable {

	private static final long serialVersionUID = 4938809193556095797L;

	/**
	 * 玩家编号
	 */
	private long playerId;
	/**
	 * 位置下标
	 */
	private int teamIndex;
	/**
	 * 是否队长
	 */
	private boolean captain;
	
	/** 自动跟随标识(0:不自动跟随, 1:自动跟随)*/
	private int follow;		

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTeamIndex() {
		return teamIndex;
	}
	public void setTeamIndex(int teamIndex) {
		this.teamIndex = teamIndex;
	}
	public boolean isCaptain() {
		return captain;
	}
	public void setCaptain(boolean captain) {
		this.captain = captain;
	}
	public int getFollow() {
		return follow;
	}
	public void setFollow(int follow) {
		this.follow = follow;
	}
}
