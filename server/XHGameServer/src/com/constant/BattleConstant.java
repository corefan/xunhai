package com.constant;

/**
 * 战斗常量
 * @author ken
 * @date 2017-1-10
 */
public class BattleConstant {

	/** 状态-正常 */
	public static final int STATE_NORMAL = 0;
	/** 状态-死亡 */
	public static final int STATE_DEAD = 1;
    
	/** 怪物状态-正常 */
	public static final int AI_STATE_NORMAL = 0;
	/** 怪物状态-巡逻 */
	public static final int AI_STATE_PATROL = 1;
	/** 怪物状态-追击 */
	public static final int AI_STATE_BATTLE = 2;
	/** 怪物状态-复位*/
    public static final int AI_STATE_GOBACK = 3;
	/** 怪物状态-免疫*/
    public static final int AI_STATE_IMMUNE = 4;
    
	/** 怪物类型-boss */
	public static final int MONSTER_TYPE_3 = 3;
    
	/** 掉落物品状态-正常 */
	public static final int DROP_NORMAL = 0;
	/** 掉落物品状态-被拾取 */
	public static final int DROP_PICKUP = 1;
	/** 掉落物品状态-过期 */
	public static final int DROP_TIMEOUT = 2;
	
	
	/** 采集-正常 */
	public static final int COLLECT_NORMAL = 0;
	/** 采集-被移除 */
	public static final int COLLECT_REMOVE = 1;
	
	/** 普通采集*/
	public static final int COLLECT_GENGRAL = 1;
	/** 高级采集*/
	public static final int COLLECT_SENIOR = 2;
	/** 任务采集*/
	public static final int COLLECT_TASK = 3;
	
	/************* 技能结果  *************/
	
	/** 正常*/
	public static final int FIGHT_RESULT_NOMAL = 0;
	/** miss*/
	public static final int FIGHT_RESULT_MISS = 1;
	/** 暴击*/
	public static final int FIGHT_RESULT_CRIT = 2;
	
	
	/************* 战斗(PK)模式  *************/
	/** 和平模式 */
	public static final int PK_MODE_PEACE = 1;  
	/** 善恶模式 */
	public static final int PK_MODE_GOODBAD = 2;
	/** 帮派模式 */
	public static final int PK_MODE_GUILD = 3;
	/** 家族模式 */
	public static final int PK_MODE_FAMILY = 4;
	/** 全体模式 */
	public static final int PK_MODE_ALL = 5;
}
