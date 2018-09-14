package com.domain.reward;

import com.domain.GameEntity;

/**
 * 全服奖励领取记录
 * @author ken
 * @date 2018年8月8日
 */
public class RewardRecord extends GameEntity {

	private static final long serialVersionUID = -4888615271413919291L;

	/**
	 * 奖励编号
	 */
	private int rewardId;
	/**
	 * 已领数量
	 */
	private int num;
	
	@Override
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO reward_record ");
		sql.append("(rewardId, num) VALUES");
		sql.append(" (");
		sql.append(rewardId);
		sql.append(",");	
		sql.append(num);	
		sql.append(")");		
		return sql.toString();
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder(1 << 8);
		sql.append("UPDATE reward_record SET ");
		sql.append(" num=");		
		sql.append(num);
		sql.append(" WHERE rewardId = ");
		sql.append(rewardId);
		
		return sql.toString();
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
