package com.dao.reward;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.reward.RewardRecord;

/**
 * 奖励记录DAO
 * @author ken
 * @date 2018年8月8日
 */
public class RewardDAO extends GameSqlSessionTemplate {

	/**
	 * 奖励记录
	 */
	public void createRewardRecord(RewardRecord model){
		this.insert_noreturn(model.getInsertSql());
	}

	/**
	 * 奖励记录
	 */
	public List<RewardRecord> listRewardRecords(){
		String sql = "select * from reward_record ";
		return this.selectList(sql, RewardRecord.class);
	}
}
