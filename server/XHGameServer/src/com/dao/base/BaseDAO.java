package com.dao.base;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.base.BaseConstant;
import com.domain.base.BaseNewRole;
import com.domain.base.BaseProperty;

/**
 * 成长属性表
 * @author ken
 * @date 2016-12-29
 */
public class BaseDAO extends BaseSqlSessionTemplate {

	private static final String propertySql = "select * from property";
	
	
	private static final String newRoleSql = "select * from newroledefaultvalue";
	
	/**
	 * 取成长属性配置表
	 */
	public List<BaseProperty> listPropertys(){
		return this.selectList(propertySql, BaseProperty.class);
	}
	
	/**
	 * 系统参数配置表
	 */
	public List<BaseConstant> listBaseConfigs(){
		String configSql = "select * from constant";
		return this.selectList(configSql, BaseConstant.class);
	}
	
	/**
	 * 初始角色配置表
	 */
	public List<BaseNewRole> listBaseNewRoles(){
		return this.selectList(newRoleSql, BaseNewRole.class);
	}

}
