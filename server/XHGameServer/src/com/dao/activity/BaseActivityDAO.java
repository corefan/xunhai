package com.dao.activity;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.activity.BaseChargeActivity;
import com.domain.activity.BaseTurntable;
import com.domain.activity.BaseTomb;

/**
 * 运营活动DAO
 * @author ken
 * @date 2017-10-25
 */
public class BaseActivityDAO extends BaseSqlSessionTemplate{
	
	/** 取转盘奖励配置 */
	public List<BaseTurntable> listBaseTurntable(){
		String sql = "select * from payactivityturntablecfg";
		return this.selectList(sql, BaseTurntable.class);
	}
	
	/** 取陵墓配置 */
	public List<BaseTomb> listBaseTombs(){
		String sql = "select * from tomb";
		return this.selectList(sql, BaseTomb.class);
	}
	
	/** 取七天累计充值配置 */
	public List<BaseChargeActivity> listBaseChargeActivity(){
		String sql = "select * from chargeactivity";
		return this.selectList(sql, BaseChargeActivity.class);
	}
}








