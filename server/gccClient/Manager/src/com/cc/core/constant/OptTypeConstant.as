package com.cc.core.constant
{
	/**
	 * 操作类型常量
	 * */
	public class OptTypeConstant
	{
		
		/** 异常信息 */
		public static const EXCEPTION:int = 1;
		/** 用户登陆 */
		public static const USER_LOGIN:int = 10;
		/** 总览 */
		public static const ALL_DATA:int = 100;
		
		/************* 玩家管理操作常量  ***********/
		/** 查看在线 */ 
		public static const PLAYER_1:int = 101;
		/** 查看玩家信息 */ 
		public static const PLAYER_2:int = 102;
		/** 封号 */
		public static const PLAYER_3:int = 103;
		/** 解封 */
		public static const PLAYER_4:int = 104;
		/** 删除玩家缓存 */
		public static const PLAYER_5:int = 105;
		
		/** 玩家背包物品查询 */
		public static const PLAYER_6:int = 106;
		/** 玩家登陆日志查询 */
		public static const PLAYER_7:int = 107;
		/** 玩家消费记录查询 */
		public static const PLAYER_8:int = 108;
		/** 玩家充值日志查询 */
		public static const PLAYER_9:int = 109;
		
		/** 发送物品查询玩家 */
		public static const PLAYER_10:int = 112;
		/** 修改玩家类型 */
		public static const PLAYER_15:int = 115;
        /** 禁言 */
        public static const PLAYER_17:int = 117;
        /** 解禁 */
        public static const PLAYER_18:int = 118;
        /** 发送邮件 */
        public static const PLAYER_19:int = 119;
		
		/** 玩家女神日志查询 */
		public static const PLAYER_LOG_30:int = 130;
		/** 玩家翅膀日志查询 */
		public static const PLAYER_LOG_31:int = 131;
		/** 玩家坐骑日志查询 */
		public static const PLAYER_LOG_32:int = 132;
		/** 玩家精灵日志查询 */
		public static const PLAYER_LOG_33:int = 133;
		/** 玩家魔法阵日志查询 */
		public static const PLAYER_LOG_34:int = 134;
		/** 玩家装备附魔日志查询 */
		public static const PLAYER_LOG_35:int = 135;
		
		/************* 数据分析操作常量  ***********/
		/** 查看5分钟在线 */ 
		public static const DATA_ANALYSIS_2:int = 202;
		/** 查看留存 */ 
		public static const DATA_ANALYSIS_4:int = 204;
		/** 每日在线时长 */ 
		public static const DATA_ANALYSIS_5:int = 205;
		/** 注册分析 */
		public static const DATA_ANALYSIS_6:int = 206;
		/** 5分钟注册 */
		public static const DATA_ANALYSIS_7:int = 207;
		/**5分钟充值*/
		public static const DATA_ANALYSIS_8:int = 208;
		/** 流失玩家停留任务分析 */
		public static const DATA_ANALYSIS_10:int = 210;
		/** 流失玩家在线时长分布 */
		public static const DATA_ANALYSIS_11:int = 211;
		/** 玩家生命周期 */
		public static const DATA_ANALYSIS_12:int = 212;
		/** 游戏节点分析 */
		public static const DATA_ANALYSIS_15:int = 215;
		
		
		
		/** 游戏每日钻石库存 */
		public static const DATA_ANALYSIS_16:int = 216;
		/** 游戏活跃玩家统计 */
		public static const DATA_ANALYSIS_17:int = 217;
		
		/************* 付费分析操作常量  ***********/
		/** 商城销量统计 */
		public static const PAY_ANALYSIS_31:int = 231;
		/** 钻石消耗分布 */
		public static const PAY_ANALYSIS_32:int = 232;
		
		/** 首次付费分析 */
		public static const PAY_ANALYSIS_41:int = 251;
		/** 首次付费等级分析 */
		public static const PAY_ANALYSIS_42:int = 252;
		
		
		
		/************* 物品管理操作常量  ***********/
		/** 得到物品基础数据列表 */
		public static const ITEM_1:int = 301;
		/** 发送物品 */
		public static const ITEM_2:int = 302;
		/** 全服发送物品 */
		public static const ITEM_3:int = 303;
		/** 申请发送物品*/
		public static const ITEM_4:int = 304;
		/** 审核发送物品*/
		public static const ITEM_5:int = 305;
		/** 申请列表*/
		public static const ITEM_6:int = 306;
		/** 物品查询*/
		public static const ITEM_7:int = 307;
        /** 发送邮件*/
        public static const ITEM_8:int = 308;
		/** 发送日志列表*/
		public static const ITEM_9:int = 309;
		
		
		/************* 数据处理操作常量  ***********/
		/** 刷新基础数据库缓存 */
		public static const DEAL_DATA_1:int = 801;
		/** 刷新基础配置缓存 */
		public static const DEAL_DATA_2:int = 802;
		/** 同步缓存数据到数据库 */
		public static const DEAL_DATA_3:int = 803;
		/** 热更新class */
		public static const DEAL_DATA_8:int = 808;
		/** 停服维护 */
		public static const DEAL_DATA_10:int = 810;
		
		/**************** 监控操作常量 **************/
		/** 聊天监控 */
		public static const CHAT_MONITOR_1:int = 820;
		
		/************* 系统公告操作常量  ***********/
		/** 发送即时系统公告 */
		public static const NOTICE_1:int = 851;
		/** 增加公告 */
		public static const NOTICE_2:int = 852;
		/** 删除公告 */
		public static const NOTICE_3:int = 853;
		/** 公告列表 */
		public static const NOTICE_4:int = 854;
		
		/**************** 用户管理 **************/
		/** 创建用户 */
		public static const SYSTEM_1:int = 901; 
		/** 更新用户 */
		public static const SYSTEM_2:int = 902;
		/** 删除用户 */
		public static const SYSTEM_3:int = 903;
		/** 得到用户列表 */
		public static const SYSTEM_4:int = 904;
		/** 创建角色 */
		public static const SYSTEM_5:int = 905;
		/** 更新角色 */
		public static const SYSTEM_6:int = 906;
		/** 删除角色 */
		public static const SYSTEM_7:int = 907;
		/** 得到角色列表 */
		public static const SYSTEM_8:int = 908;
		/** 创建权限 */
		public static const SYSTEM_11:int = 911;
		/** 更新权限 */
		public static const SYSTEM_12:int = 912;
		/** 删除权限 */
		public static const SYSTEM_13:int = 913;
		/** 得到权限列表 */
		public static const SYSTEM_14:int = 914;
		/** 最近操作日志 */
		public static const OPT_LOG_1:int = 951;
		/** 玩家操作日志 */
		public static const OPT_LOG_2:int = 952;
		
		/**封停IP*/
		public static const BAN_IP_OPERATE:int = 1001;  //IP操作
		/**封停ID列表*/
		public static const BAN_IP_LIST:int = 1002;  //IP列表

		/**************** 服务器管理 **************/
		/** 创建服务器 */
		public static const SERVER_1:int = 1101; 
		/** 更新服务器 */
		public static const SERVER_2:int = 1102;
		/** 删除服务器 */
		public static const SERVER_3:int = 1103;
		/** 得到服务器列表 */
		public static const SERVER_4:int = 1104;
		
	}
}