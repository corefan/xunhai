package com.dao.fashion;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.fashion.BaseFashion;

/**
 * 时装dao
 * @author ken
 * @date 2017-2-13
 */
public class BaseFashionDAO extends BaseSqlSessionTemplate {

	private static final String fashionSql = "select * from fashionable";
	
	/**
	 * 取物品配置表
	 */
	public List<BaseFashion> listBaseFashions(){
		return this.selectList(fashionSql, BaseFashion.class);
	}
}
