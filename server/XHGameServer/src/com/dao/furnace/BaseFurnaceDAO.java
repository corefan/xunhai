package com.dao.furnace;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.furnace.BaseFurnace;

/**
 * 熔炉DAO
 * @author ken
 * @date 2018年4月23日
 */
public class BaseFurnaceDAO extends BaseSqlSessionTemplate {

	public List<BaseFurnace> listBaseFurnaces(){
		String sql = "select * from furnace";
		return this.selectList(sql, BaseFurnace.class);
	}
}
