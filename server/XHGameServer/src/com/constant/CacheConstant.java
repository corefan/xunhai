package com.constant;

/**
 * 2013-10-30
 * 缓存常量
 */
public class CacheConstant {
	
	/**----------------------连接 ---------------------------- */
	/** 玩家进入游戏连接缓存 */
	public static final String PLAYER_CONNECTION_MAP = "PLAYER_CONNECTION_MAP";
	
	/**----------------------账号 ---------------------------- */
	/** 账号玩家列表缓存 */
	public static final String ACCOUNT_PLAYER_CACHE = "ACCOUNT_PLAYER_CACHE";
	
	/**----------------------玩家 ---------------------------- */
	/** 玩家编号缓存 */
	public static final String LOGIN_PLAYER_ID_SET = "LOGIN_PLAYER_ID_SET";
	/** 玩家缓存 */
	public static final String PLAYER_CACHE = "PLAYER_CACHE";
	/** 玩家扩展缓存 */
	public static final String PLAYER_EXT_CACHE = "PLAYER_EXT_CACHE";	
	/** 玩家操作数据缓存 */
	public static final String PLAYER_OPTIONAL_CACHE = "PLAYER_OPTIONAL_CACHE";
	/** 玩家属性缓存 */
	public static final String PLAYER_PROPERTY_CACHE = "PLAYER_PROPERTY_CACHE";
	/** 玩家每日数据缓存 */
	public static final String PLAYER_DAILY_CACHE = "PLAYER_DAILY_CACHE";
	/** 玩家财富数据缓存 */
	public static final String PLAYER_WEALTH_CACHE = "PLAYER_WEALTH_CACHE";
	
	/**---------------------- 场景 ---------------------------- */
	/** 场景玩家缓存 */
	public static final String PLAYER_PUPPET_CACHE = "PLAYER_PUPPET_CACHE";
	/** 场景缓存 */
	public static final String SCENE_CACHE = "SCENE_CACHE";
	/** 副本缓存 */
	public static final String PLAYER_INSTANCE_CACHE = "PLAYER_INSTANCE_CACHE";
	
	/** 幻境缓存 */
	public static final String HUANJING_CACHE = "HUANJING_CACHE";
	
	/**---------------------- 背包 ---------------------------- */
	/** 玩家背包缓存 */
	public static final String PLAYER_BAG = "PLAYER_BAG";
	/** 玩家装备缓存 */
	public static final String PLAYER_EQUIPMENT = "PLAYER_EQUIPMENT";
	/** 药品栏缓存 */
	public static final String PLAYER_DRUG = "PLAYER_DRUG";

	
	/**---------------------- 技能 ---------------------------- */
	/** 玩家技能缓存 */
	public static final String PLAYER_SKILL = "PLAYER_SKILL";
	
	/**---------------------- 时装翅膀 ---------------------------- */
	/** 玩家时装缓存 */
	public static final String PLAYER_FASHION = "PLAYER_FASHION";
	
	/**---------------------- 好友 ---------------------------- */
	/** 玩家好友缓存 */
	public static final String PLAYER_FRIEND = "PLAYER_FRIEND";
	/** 好友申请缓存 */
	public static final String PLAYER_APPLY = "PLAYER_APPLY";

	/**---------------------- 邮件 ---------------------------- */
	/** 玩家邮件缓存 */
	public static final String MAIL_INBOX = "MAIL_INBOX";
	/** 全服邮件缓存 */
	public static final String SERVER_MAIL_INBOX = "SERVER_MAIL_INBOX";
	
	/**---------------------- 注灵 ---------------------------- */
	/** 注灵数据缓存 */
	public static final String PLAYER_WAKAN = "PLAYER_WAKAN";

	/**---------------------- 任务 ---------------------------- */
	/** 玩家任务缓存 */
	public static final String PLAYER_TASK = "PLAYER_TASK";
	/** 玩家每日任务缓存 */
	public static final String PLAYER_DAILY_TASK = "PLAYER_DAILY_TASK";
	
	/**---------------------- 组队 ---------------------------- */
	/** 组队缓存 */
	public static final String TEAM = "TEAM";	
	
	/**---------------------- 交易行 ---------------------------- */
	/** 掉落中装备缓存 */
	public static final String EQUIPMENT_DROP = "EQUIPMENT_DROP";	
	/** 玩家交易物品缓存 */
	public static final String PLAYER_TRADE_BAG = "PLAYER_TRADE_BAG";
	
	/**---------------------- 玩家武器铭文信息 ---------------------------- */
	/** 武器铭文缓存 */
	public static final String PLAYER_WEAPON_EFFECT = "PLAYER_WEAPON_EFFECT";
	

	/**---------------------- 采集 ---------------------------- */
	/** 已刷过的普通采集点 */
	public static final String GENERAL_COLLECT = "GENERAL_COLLECT";
	
	/**----------------------天梯 ---------------------------- */
	/** 玩家天梯缓存 */
	public static final String PLAYER_TIANTI = "PLAYER_TIANTI";
	/** 天梯排行缓存 */
	public static final String TIANTI_RANK = "TIANTI_RANK";
	/** 玩家天梯匹配缓存 */
	public static final String PLAYER_TIANTI_MATCH = "PLAYER_TIANTI_MATCH";
	
	/**---------------------- 家族 ---------------------------- */
	/** 玩家家族信息 */
	public static final String PLAYER_FAMILY = "PLAYER_FAMILY";
	/** 家族信息 */
	public static final String FAMILY = "FAMILY";
	
	
	/**---------------------- 商城 ---------------------------- */
	/** 玩家商城信息 */
	public static final String PLAYER_MARKET = "PLAYER_MARKET";
	
	/**---------------------- 签到 ---------------------------- */
	/** 玩家签到信息 */
	public static final String PLAYER_SIGN = "PLAYER_SIGN";
	
	/**---------------------- 羽翼 ---------------------------- */
	/** 玩家羽翼信息 */
	public static final String PLAYER_WING = "PLAYER_WING";

	/**---------------------- 排行榜 ---------------------------- */
	/** 战力榜 */
	public static final String BATTLEVALUE_RANK_MAP = "BATTLEVALUE_RANK_MAP";
	/** 装备榜 */
	public static final String EQUIP_RANK_MAP = "EQUIP_RANK_MAP";
	/** 财富榜 */
	public static final String GOLD_RANK_MAP = "GOLD_RANK_MAP";
	
	/**---------------------- 仇敌 ---------------------------- */
	/** 玩家仇敌信息 */
	public static final String PLAYER_ENEMY = "PLAYER_ENEMY";
	
	/**---------------------- vip ---------------------------- */
	/** 玩家VIP信息 */
	public static final String PLAYER_VIP = "PLAYER_VIP";
	
	/**---------------------- 语音 ---------------------------- */
	/** 语音信息 */
	public static final String VOICE = "VOICE_MAP";
	
	/**---------------------- 离线信息 ---------------------------- */
	/** 玩家离线信息 */
	public static final String PLAYER_OFFLINE_INFO = "PLAYER_OFFLINE_INFO";
	
	/**----------------------运营活动 ---------------------------- */
	/** 奖励记录 */
	public static final String REWARD_RECORD_MAP = "REWARD_RECORD_MAP";
	/** 购买基金玩家总数 */
	public static final String BUY_GROWTH_FUND_NUM_CACHE = "BUY_GROWTH_FUND_NUM_CACHE";
	/** 玩家陵墓 */
	public static final String PLAYER_TOMB = "PLAYER_TOMB";
	/** 转盘抽奖物品记录*/
	public static final String TRUNTABLE_DRAW_RECODE = "TRUNTABLE_DRAW_RECODE";
	
	/**----------------------帮派---------------------------- */
	public static final String GUILD_MAP = "GUILD_MAP";
	public static final String PLAYER_GUILD = "PLAYER_GUILD";
	public static final String GUILD_FIGHT_DATA = "GUILD_FIGHT_DATA";
	public static final String GUILD_UNION_LIST = "GUILD_UNION_LIST";
	public static final String GUILD_BUY_MAP = "GUILD_BUY_MAP";
	
	/**---------------------- 熔炉 ---------------------------- */
	/** 玩家熔炉缓存 */
	public static final String PLAYER_FURNACE = "PLAYER_FURNACE";
	
	/**---------------------- 基础表 ---------------------------- */
	/** 敏感字缓存 */
	public static final String B_DIRTY_NAME_LIST = "dirtyNameList";
	
	/** 成长属性配置缓存 */
	public static final String BASE_PROPERTY = "BASE_PROPERTY";
	/** 系统参数配置缓存 */
	public static final String BASE_CONSTANT = "BASE_CONSTANT";
	/** 初始角色配置缓存 */
	public static final String BASE_NEWROLE = "BASE_NEWROLE";
	/** 地图表配置缓存 */
	public static final String BASE_MAP = "BASE_MAP";
	/** 传送门配置缓存 */
	public static final String BASE_TRANSFER = "BASE_TRANSFER";
	/** 物品表配置缓存 */
	public static final String BASE_ITEM = "BASE_ITEM";
	/** 礼包表配置缓存 */
	public static final String BASE_GIFT = "BASE_GIFT";
	/** 装备表配置缓存 */
	public static final String BASE_EQUIPMENT = "BASE_EQUIPMENT";
	/** 装备附加属性配置缓存 */
	public static final String BASE_EQUIP_ADD_ATTR = "BASE_EQUIP_ADD_ATTR";
	/** 装备强化配置缓存 */
	public static final String BASE_EQUIP_STRONG = "BASE_EQUIP_STRONG";
	/** 装备传承配置缓存 */
	public static final String BASE_EQUIP_INHERIT = "BASE_EQUIP_INHERIT";
	/** Pk掉落配置缓存 */
	public static final String BASE_PKDROP = "BASE_PKDROP";
	/** 怪物表配置缓存 */
	public static final String BASE_MONSTER = "BASE_MONSTER";
	/** 刷怪配置缓存 */
	public static final String BASE_REFRESH_MONSTER = "BASE_REFRESH_MONSTER";
	/** 技能结算模块表配置缓存 */
	public static final String BASE_DAMAGE_MODEL = "BASE_DAMAGE_MODEL";
	/** 技能表配置缓存 */
	public static final String BASE_SKILL = "BASE_SKILL";
	/** 技能升级表配置缓存 */
	public static final String BASE_SKILL_UP = "BASE_SKILL_UP";
	/** ai判定缓存 */
	public static final String BASE_AI_DETERMINE = "BASE_AI_DETERMINE";
	/** 时装翅膀配置缓存 */
	public static final String BASE_FASHION = "BASE_FASHION";
	/** 注灵配置缓存 */
	public static final String BASE_WAKAN = "BASE_WAKAN";
	/** 铭文配置缓存 */
	public static final String BASE_EPIGRAPH = "BASE_EPIGRAPH";
	/** 觉醒配置缓存 */	
	public static final String BASE_AWAKE = "BASE_AWAKE";
	/** 任务配置缓存 */
	public static final String BASE_TASK = "BASE_TASK";
	/** 任务物品配置缓存 */
	public static final String BASE_TASK_ITEM = "BASE_TASK_ITEM";
	/** 任务活动配置缓存 */
	public static final String BASE_TASK_ACT = "BASE_TASK_ACT";
	/** 环任务奖励配置缓存 */
	public static final String BASE_WEEK_TASK_REWARD = "BASE_WEEK_TASK_REWARD";
	/** 组队配置缓存 */
	public static final String BASE_TEAM = "BASE_TEAM";	
	/** 采集配置缓存 */
	public static final String BASE_COLLECT = "BASE_COLLECT";
	/** 大荒塔配置缓存 */
	public static final String BASE_TOWER = "BASE_TOWER";
	/** 合成/分解配置缓存 */
	public static final String BASE_COMPOSE = "BASE_COMPOSE";
	/** buff配置缓存 */
	public static final String BASE_BUFF = "BASE_BUFF";
	/** 天梯配置缓存 */
	public static final String BASE_TIANTI_STAGE = "BASE_TIANTI_STAGE";
	/** 天梯配置缓存 */
	public static final String BASE_TIANTI_SCORE = "BASE_TIANTI_SCORE";
	/** 天梯配置缓存 */
	public static final String BASE_TIANTI_DATE_LIST = "BASE_TIANTI_DATE_LIST";
	/** 天梯赛季奖励配置缓存 */
	public static final String BASE_TIANTI_REWARD = "BASE_TIANTI_REWARD";	
	/** 天梯PK奖励配置缓存 */
	public static final String BASE_TIANTI_PK_REWARD = "BASE_TIANTI_PK_REWARD";	
	/** 商城 */
	public static final String BASE_MARKET = "BASE_MARKET";
	/** 签到 */
	public static final String BASE_SIGN = "BASE_SIGN";		
	/** 连续签到奖励配置 */
	public static final String BASE_CON_SIGN_REWARD = "BASE_CON_SIGN_REWARD";	
	/** 陵墓配置缓存 */
	public static final String BASE_TOMB = "BASE_TOMB";	
	/** 陵墓配置缓存 */
	public static final String BASE_GROUP_TOMB = "BASE_GROUP_TOMB";	
	/** 羽翼配置缓存 */
	public static final String BASE_WING = "BASE_WING";
	/** 周活动配置缓存 */
	public static final String BASE_WEEK_ACTIVITY = "BASE_WEEK_ACTIVITY";	
	/** 系统公告信息 */
	public static final String BASE_NOTICE = "BASE_NOTICE";	
	/** 功能开放(推送)信息 */
	public static final String BASE_PUSH_NOTICE = "BASE_PUSH_NOTICE";	
	/** 副本奖励配置缓存 */
	public static final String BASE_INSTANCE_REWARD = "BASE_INSTANCE_REWARD";	
	/** vip配置缓存 */
	public static final String BASE_VIP = "BASE_VIP";
	/** vip特权配置缓存 */
	public static final String BASE_VIP_PRIVILEGE = "BASE_VIP_PRIVILEGE";	
	/** 奖励配置缓存 */
	public static final String BASE_REWARD = "BASE_REWARD";
	/** 充值配置缓存 */
	public static final String BASE_PAY = "BASE_PAY";	
	/** PosId配置缓存 */
	public static final String BASE_POS_ID = "BASE_POS_ID";
	/** 机器人技能结算ID缓存 */
	public static final String BASE_REBOT_ID = "BASE_REBOT_ID";
	/** 掉落 */
	public static final String BASE_DROP_ITEM = "BASE_DROP_ITEM";
	/** 转盘抽奖基础表*/
	public static final String BASE_TURNTABLE = "BASE_TURNTABLE";
	/** 转盘奖励信息 */
	public static final String BASE_TURNTABLE_REWARD = "BASE_TURNTABLE_REWARD";
	/** 七天累计充值配置缓存 */
	public static final String BASE_CHARGE_ACTIVITY = "BASE_CHARGE_ACTIVITY";	
	/** 帮派配置缓存 */
	public static final String BASE_GUILD = "BASE_GUILD";	
	/** 帮派配置缓存 */
	public static final String BASE_GUILD_DONATE = "BASE_GUILD_DONATE";	
	/** 帮派配置缓存 */
	public static final String BASE_GUILD_SKILL = "BASE_GUILD_SKILL";
	/** 帮派配置缓存 */
	public static final String BASE_GUILD_BUY = "BASE_GUILD_BUY";
	/** 熔炉配置缓存 */
	public static final String BASE_FURNACE = "BASE_FURNACE";	
	
	/**---------------------- 记录游戏的一些配置--------------------**/
	public static final String B_AGENT_CONFIG_CACHE = "B_AGENT_CONFIG_CACHE";
	public static final String B_SERVER_CONFIG_CACHE = "B_SERVER_CONFIG_CACHE";
	
	
}
