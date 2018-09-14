package com.dao.skill;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.skill.BaseSkill;
import com.domain.skill.BaseSkillUp;
import com.domain.skill.DamagModel;
import com.domain.skill.RangeModel;

/**
 * 技能配置DAO
 * @author ken
 * @date 2017-1-19
 */
public class BaseSkillDAO extends BaseSqlSessionTemplate {

	private static final String baseSkillSql = "select * from cellnewskillcfg";
	
	private static final String damageModelSql = "select * from sskillmodelaccountcfg";
	
	private static final String rangeModelSql = "select * from sskillmodelrangecfg where n32LifeTime > 0";
	
	private static final String skillUpSql = "select * from skillup";
	
	/**
	 * 取技能配置表
	 */
	public List<BaseSkill> listBaseSkills(){
		return this.selectList(baseSkillSql, BaseSkill.class);
	}
	
	/**
	 * 取技能结算模块表
	 */
	public List<DamagModel> listDamagModels(){
		return this.selectList(damageModelSql, DamagModel.class);
	}
	
	/**
	 * 取持续对地范围技能
	 */
	public List<RangeModel> listRangeModels(){
		return this.selectList(rangeModelSql, RangeModel.class);
	}
	
	/**
	 * 技能升级配置表
	 */
	public List<BaseSkillUp> listBaseSkillUps(){
		return this.selectList(skillUpSql, BaseSkillUp.class);
	}
}
