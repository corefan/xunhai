package com.dao.bag;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.bag.BaseGift;
import com.domain.bag.BaseItem;

/**
 * 物品基础DAO
 * @author ken
 * @date 2017-1-4
 */
public class BaseItemDAO extends BaseSqlSessionTemplate {

	private static final String itemSql = "select * from item";
	
	
	/**
	 * 取物品配置表
	 */
	public List<BaseItem> listBaseItems(){
		return this.selectList(itemSql, BaseItem.class);
	}
	
	/**
	 * 取礼包配置表
	 */
	public List<BaseGift> listBaseGifts(){
	    String sql = "select * from gift";
		return this.selectList(sql, BaseGift.class);	
	}
}
