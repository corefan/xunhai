package com.dao.vip;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.vip.BaseVip;
import com.domain.vip.BaseVipPrivilege;

/**
 * vip基础数据
 * @author jiangqin
 * @date 2017-6-15
 */
public class BaseVipDAO extends BaseSqlSessionTemplate{
	
	/** 取vip基础数据*/
	public List<BaseVip> listBaseVip(){
		String vipSql = "select * from vip";
		return this.selectList(vipSql, BaseVip.class);
	}
	
	
	/** 取vip特权基础数据*/
	public List<BaseVipPrivilege> listBaseVipPrivilege(){
		String vipPrivilegeSql = "select * from vipprivilege";
		return this.selectList(vipPrivilegeSql, BaseVipPrivilege.class);
	}
}
