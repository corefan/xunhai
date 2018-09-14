package com.dao.sign;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.sign.BaseConSignReward;
import com.domain.sign.BaseSign;

public class BaseSignDAO extends BaseSqlSessionTemplate{
	/**
	 * 取签到奖励
	 */
	public List<BaseSign> listBaseSign(){
		String baseSignSql = "select * from signreward";
		return this.selectList(baseSignSql, BaseSign.class);
	}
	
	/** 连续签到奖励*/
	public List<BaseConSignReward> listBaseConSignReward(){
		String baseConSignRewardSql = "select * from consignreward";
		return this.selectList(baseConSignRewardSql, BaseConSignReward.class);
	}
}
