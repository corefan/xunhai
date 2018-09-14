package com.dao.wing;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.wing.BaseWing;

public class BaseWingDAO extends BaseSqlSessionTemplate{
	/** 取在线奖励配置 */
	public List<BaseWing> listBaseWing(){
		String sql = "select * from wing";
		return this.selectList(sql, BaseWing.class);
	}
}
