package com.dao.tianti;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.tianti.BaseTiantiDate;
import com.domain.tianti.BaseTiantiPKReward;
import com.domain.tianti.BaseTiantiReward;
import com.domain.tianti.BaseTiantiScore;

/**
 * 天梯配置dao
 * @author ken
 * @date 2017-4-14
 */
public class BaseTiantiDAO extends BaseSqlSessionTemplate {
	
	private static final String sql1 = "select * from tiantipkreward";
	
	private static final String sql2 = "select * from tiantiscore";
	
	private static final String sql3 = "select * from tiantidate";
	
	private static final String sql4 = "select * from tiantireward";


	/**
	 * 天梯PK奖励配置
	 */
	public List<BaseTiantiPKReward> listBaseTiantiPKRewards(){
		return this.selectList(sql1, BaseTiantiPKReward.class);
	}
	
	/**
	 * 分数配置
	 */
	public List<BaseTiantiScore> listBaseTiantiScores(){
		return this.selectList(sql2, BaseTiantiScore.class);
	}
	
	/**
	 * 赛季日期配置
	 */
	public List<BaseTiantiDate> getBaseTiantiDate(){
		return this.selectList(sql3, BaseTiantiDate.class);
	}
	
	/**
	 * 排名奖励配置
	 */
	public List<BaseTiantiReward> listBaseTiantiRewards(){
		return this.selectList(sql4, BaseTiantiReward.class);
	}	
}
