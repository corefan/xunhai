/**
 * 
 */
package com.domain.activity;

import java.io.Serializable;

/**
 * 转盘抽奖记录
 * @author jiangqin
 * @date 2017-10-29
 */
public class TruntableRecord  implements Serializable{	
	
	private static final long serialVersionUID = 8852243233490484259L;
	
	/** 玩家编号*/
	private long playerId;	
	/** 奖励编号 */
	private int rewardId;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

}
