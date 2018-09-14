package com.dao.wakan;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.wakan.BaseAwake;
import com.domain.wakan.BaseWakan;

/**
 * 注灵
 * @author jiangqin
 * @date 2017-2-21
 */
public class BaseWakanDao  extends BaseSqlSessionTemplate{
	private static final String wakanSql = "select * from attup";
	
	private static final String awakeSql = "select * from attawakening";
	
	/**
	 * 取觉醒配置表
	 */
	public List<BaseAwake> listBaseAwakes(){
		return this.selectList(awakeSql, BaseAwake.class);
	}
	/**
	 * 取注灵配置表
	 */
	public List<BaseWakan> listBaseWakans(){
		return this.selectList(wakanSql, BaseWakan.class);
	}
}
