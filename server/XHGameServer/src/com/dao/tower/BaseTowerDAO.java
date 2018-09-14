package com.dao.tower;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.tower.BaseTower;

/**
 * 大荒塔DAO
 * @author ken
 * @date 2017-3-24
 */
public class BaseTowerDAO extends BaseSqlSessionTemplate {

	private static final String sql = "select * from tower";
		
	/**
	 * 取大荒塔配置表
	 */
	public List<BaseTower> listBaseTowers(){
		return this.selectList(sql, BaseTower.class);
	}
	
}
