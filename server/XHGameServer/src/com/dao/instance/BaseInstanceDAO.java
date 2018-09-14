package com.dao.instance;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.instance.BaseInstanceReward;

/**
 * 副本奖励
 * @author jiangqin
 * @date 2017-6-13
 */
public class BaseInstanceDAO extends BaseSqlSessionTemplate{
	
	/** 取副本奖励配置 */
	public List<BaseInstanceReward> listBaseInstanceReward(){
		String sql = "select * from instancereward";
		return this.selectList(sql, BaseInstanceReward.class);
	}
}
