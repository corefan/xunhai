package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.BaseItem;

/**
 * 取基础表DAO
 * @author ken
 * @date 2017-7-24
 */
public class BaseDAO extends BaseSqlSessionTemplate {

	/**
	 * 取道具和装备表
	 */
	public List<BaseItem> listBaseItems(){
		List<BaseItem> lists = new ArrayList<BaseItem>();
		String sql ="select * from item";
		lists.addAll(this.selectList(sql, BaseItem.class));
		
		String sql2 ="select * from equipment";
		lists.addAll(this.selectList(sql2, BaseItem.class));
		return lists;
	}
}
