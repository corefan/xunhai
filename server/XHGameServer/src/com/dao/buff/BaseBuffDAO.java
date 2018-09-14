package com.dao.buff;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.buff.BaseBuff;

/**
 * buff配置
 * @author ken
 * @date 2017-4-1
 */
public class BaseBuffDAO extends BaseSqlSessionTemplate {

	/**
	 * 取buff配置表
	 */
	public List<BaseBuff> listBaseBuffs(){
		String sql = "select * from buff";
		
		return this.selectList(sql, BaseBuff.class);
	}
}
