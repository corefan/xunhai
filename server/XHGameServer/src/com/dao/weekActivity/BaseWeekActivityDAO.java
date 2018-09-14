package com.dao.weekActivity;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.weekactivity.BaseWeekActivity;

/**
 * 周活动配置DAO
 * @author ken
 * @date 2017-5-12
 */
public class BaseWeekActivityDAO extends BaseSqlSessionTemplate {

	/**
	 * 取周活动配置表
	 */
	public List<BaseWeekActivity> listBaseWeekActivitys(){
		String sql = "select * from weekactivity";
		return this.selectList(sql, BaseWeekActivity.class);
	}
	
}
