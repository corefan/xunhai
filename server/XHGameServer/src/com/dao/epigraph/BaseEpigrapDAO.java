package com.dao.epigraph;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.epigraph.BaseEpigraph;

/**
 * 铭文dao
 * @author ken
 * @date 2017-3-25
 */
public class BaseEpigrapDAO extends BaseSqlSessionTemplate {

	private static final String epigraphSql = "select * from inscription";
	
	/**
	 * 取铭文配置表
	 */
	public List<BaseEpigraph> listBaseEpigraph (){
		return this.selectList(epigraphSql, BaseEpigraph.class);
	}
}
