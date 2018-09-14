package com.dao.reward;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.reward.BaseReward;

public class BaseRewardDAO extends BaseSqlSessionTemplate {

	/** 取综合奖励基础数据*/
	public List<BaseReward> listBaseReward(){
		String baseRewardSql = "select * from reward";
		return this.selectList(baseRewardSql, BaseReward.class);
	}
	
}
