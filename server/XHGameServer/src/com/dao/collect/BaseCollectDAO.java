package com.dao.collect;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.collect.BaseCollect;

/**
 * 成长属性表
 * @author ken
 * @date 2016-12-29
 */
public class BaseCollectDAO extends BaseSqlSessionTemplate {

	private static final String collectSql = "select * from collect";	
	
	/**
	 * 取成长属性配置表
	 */
	public List<BaseCollect> listBaseCollect(){
		return this.selectList(collectSql, BaseCollect.class);
	}
	
	
}
