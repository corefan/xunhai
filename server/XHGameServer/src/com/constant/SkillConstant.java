package com.constant;

/**
 * 技能常量
 * @author ken
 * @date 2017-1-16
 */
public class SkillConstant {

	/** 对自己*/
	public static final int TARGET_ME = 1; 
	/** 对所有队员*/
	public static final int TARGET_TEAM_ALL = 2; 
	/** 对所有队员 除了自己*/
	public static final int TARGET_TEAM_NO_ME = 3; 
	/** 对所有敌人*/
	public static final int TARGET_ENEMY = 4; 
	/** 对所有单位*/
	public static final int TARGET_ALL = 5; 
	/** 对所有友方*/
	public static final int TARGET_US_ALL = 6; 

	/** 技能类型-发射*/
	public static final int SKILL_TYPE_1 = 1; 
	/** 技能类型-移动*/
	public static final int SKILL_TYPE_2 = 2; 
	/** 技能类型-范围*/
	public static final int SKILL_TYPE_3 = 3; 
	/** 技能类型-召唤*/
	public static final int SKILL_TYPE_4 = 4; 
	
	
	
	/** 伤害计算  物攻*/
	public static final int DAMAGE_P_ATTACK = 1; 
	/** 伤害计算 魔攻*/
	public static final int DAMAGE_M_ATTACK = 2; 
	/** 伤害计算  魔攻转hp回复*/
	public static final int DAMAGE_M_ATTACK_RESUME = 97; 
	
}
