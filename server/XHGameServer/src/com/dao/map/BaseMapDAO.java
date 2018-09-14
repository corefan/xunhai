package com.dao.map;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.map.BaseMap;
import com.domain.tianti.BaseDropItem;

/**
 * 地图基础dao
 * @author ken
 * @date 2016-12-30
 */
public class BaseMapDAO extends BaseSqlSessionTemplate {

	private static final String baseMapSql = "select * from mapmanger";
	
	private static final String sql5 = "select * from dropitem";
	
	/**
	 * 取地图配置表
	 */
	public List<BaseMap> listBaseMaps(){
		return this.selectList(baseMapSql, BaseMap.class);
	}
	
	/**
	 * 场景掉落配置
	 */
	public List<BaseDropItem> listBaseDropItem(){
		return this.selectList(sql5, BaseDropItem.class);
	}
}
