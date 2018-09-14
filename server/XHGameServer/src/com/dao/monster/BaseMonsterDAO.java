package com.dao.monster;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.monster.BaseAiDetermine;
import com.domain.monster.BaseMonster;
import com.domain.monster.BaseRefreshMonster;

/**
 * 怪物dao
 * @author ken
 * @date 2017-1-7
 */
public class BaseMonsterDAO extends BaseSqlSessionTemplate {
	
	private static final String baseMonsterSql = "select * from monster";
	
	private static final String baseRefreshMonsterSql = "select * from refreshmonster";
	
	private static final String baseAiDetermineSql = "select * from aidetermination";
	
	
	/**
	 * 取怪物配置表
	 */
	public List<BaseMonster> listBaseMonsters(){
		return this.selectList(baseMonsterSql, BaseMonster.class);
	}
	
	/**
	 * 刷怪配置
	 */
	public List<BaseRefreshMonster> listBaseRefreshMonsters(){
		return this.selectList(baseRefreshMonsterSql, BaseRefreshMonster.class);
	}
	
	/**
	 * ai判定表
	 */
	public List<BaseAiDetermine> listBaseAiDetermines(){
		return this.selectList(baseAiDetermineSql, BaseAiDetermine.class);
	}
	
}
