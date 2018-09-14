package com.dao;

import java.util.List;

import com.db.GccSqlSessionTemplate;
import com.domain.ActCode;

/**
 * 激活码DAO
 * @author ken
 * @date 2018年8月9日
 */
public class ActCodeDAO extends GccSqlSessionTemplate {

	/**
	 * 未使用的激活码列表
	 */
	public List<ActCode> listActCodes(){
		String sql = "select * from actcode where state = 0";
		return this.selectList(sql, ActCode.class);
	}
	
	/**
	 * 清理激活码表
	 */
	public void deleteActCode(){
		this.delete("delete from actcode");
	}
	
	/**
	 * 更新激活码
	 * */
	public void updateActCode(ActCode actCode) {
		this.update(actCode.getUpdateSql());
	}
}
