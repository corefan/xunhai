package com.constant;

/**
 * @author ken
 * 2014-3-18
 * 操作常量
 */
public class OptTypeConstant {

	/** 异常 */
	public static final int EXCEPTION = 1;
	
	/** 登陆 */
	public static final int LOGIN = 10;
	
	/** 总览 */
	public static final int ALL_DATA = 100;
	
	/************* 玩家管理操作常量  ***********/
	/** 查看实时在线 */ 
	public static final int PLAYER_1 = 101;
	/** 查看玩家信息 */ 
	public static final int PLAYER_2 = 102;
	/** 封号 */
	public static final int PLAYER_3 = 103;
	/** 解封 */
	public static final int PLAYER_4 = 104;
	/** 删除玩家缓存 */
	public static final int PLAYER_5 = 105;
	/** 玩家背包物品查询  */
	public static final int PLAYER_6 = 106;
	/** 玩家登陆日志查询 */
	public static final int PLAYER_7 = 107;
	/** 玩家消费日志查询 */
	public static final int PLAYER_8 = 108;
	/** 玩家充值日志查询 */
	public static final int PLAYER_9 = 109;
	/** 查看玩家信息 */ 
	public static final int PLAYER_10 = 112;
	/** 修改玩家类型 */
	public static final int PLAYER_15 = 115;
	/** 查询平台列表*/
	public static final int PLAYER_16 = 116;
	/** 禁言*/
	public static final int PLAYER_17 = 117;
	/** 解禁*/
	public static final int PLAYER_18 = 118;
	
	/************* 数据分析操作常量  ***********/
	/** 查看5分钟在线 */ 
	public static final int DATA_ANALYSIS_2 = 202;
	/** 查看留存 */ 
	public static final int DATA_ANALYSIS_4 = 204;
	/** 每日在线时长 */ 
	public static final int DATA_ANALYSIS_5 = 205;
	/** 注册分析 */
	public static final int DATA_ANALYSIS_6 = 206;
	/** 5分钟注册 */
	public static final int DATA_ANALYSIS_7 = 207;
	/** 查看5分钟充值  */
	public static final int DATA_ANALYSIS_8 = 208;
	
	/** 流失玩家停留任务分析 */
	public static final int DATA_ANALYSIS_10 = 210;
	/** 流失玩家在线时长分布 */
	public static final int DATA_ANALYSIS_11 = 211;
	/** 玩家登陆天数分布 */
	public static final int DATA_ANALYSIS_12 = 212;
	/** 游戏节点分析 */
	public static final int DATA_ANALYSIS_15 = 215;
	/** 游戏每日钻石库存 */
	public static final int DATA_ANALYSIS_16 = 216;
	/** 游戏活跃玩家统计 */
	public static final int DATA_ANALYSIS_17 = 217;
	
	/************* 付费分析操作常量  ***********/
	/** 商城销量统计 */
	public static final int PAY_ANALYSIS_31 = 231;
	/** 元宝消耗分布 */
	public static final int PAY_ANALYSIS_32 = 232;
	
	/** 首次付费分析 */
	public static final int PAY_ANALYSIS_41 = 251;
	/** 首次付费等级分析 */
	public static final int PAY_ANALYSIS_42 = 252;
	
	
	/************* 物品管理操作常量  ***********/
	/** 得到物品基础数据列表 */
	public static final int ITEM_1 = 301;
	/** 部分玩家发送物品 */
	public static final int ITEM_2 = 302;
	/** 全服发送物品 */
	public static final int ITEM_3 = 303;
	/** 申请发送物品*/
	public static final int ITEM_4 = 304;
	/** 审核发送物品*/
	public static final int ITEM_5 = 305;
	/** 申请列表*/
	public static final int ITEM_6 = 306;
	/** 物品查询*/
	public static final int ITEM_7 = 307;
	/** 发送邮件（不带附件）*/
	public static final int ITEM_8 = 308;
	/** 发放日志列表 */
	public static final int ITEM_9 = 309;
	
	
	/************* 处理数据操作常量  ***********/
	/** 刷新基础缓存 */
	public static final int DEAL_DATA_1 = 801;
	/** 刷新配置文件 */
	public static final int DEAL_DATA_2 = 802;
	/** 同步缓存数据 */
	public static final int DEAL_DATA_3 = 803;
	/** 停服 */
	public static final int DEAL_DATA_4 = 804;
	/** 热更新class */
	public static final int DEAL_DATA_8 = 808;
	/** 停服维护 */
	public static final int DEAL_DATA_10 = 810;
	
	/************* 聊天监控  ***********/
	public static final int CHAT_MONITOR = 820;
	
	/************* 系统公告操作常量  ***********/
	/** 发送即时系统公告 */
	public static final int NOTICE_1 = 851;  //及时公告
	public static final int NOTICE_2 = 852;  //增加公告
	public static final int NOTICE_3 = 853;  //删除公告
	public static final int NOTICE_4 = 854;  //公告列表
	
	/**************** 系统设置 **************/
	/** 创建用户 */
	public static final int SYSTEM_1 = 901; 
	/** 更新用户 */
	public static final int SYSTEM_2 = 902;
	/** 删除用户 */
	public static final int SYSTEM_3 = 903;
	/** 得到用户列表 */
	public static final int SYSTEM_4 = 904;
	/** 创建角色 */
	public static final int SYSTEM_5 = 905;
	/** 更新角色 */
	public static final int SYSTEM_6 = 906;
	/** 删除角色 */
	public static final int SYSTEM_7 = 907;
	/** 得到角色列表 */
	public static final int SYSTEM_8 = 908;
	/** 创建权限 */
	public static final int SYSTEM_11 = 911;
	/** 更新权限 */
	public static final int SYSTEM_12 = 912;
	/** 删除权限 */
	public static final int SYSTEM_13 = 913;
	/** 得到权限列表 */
	public static final int SYSTEM_14 = 914;
	
	/** 最近操作日志 */
	public static final int OPT_LOG_1 = 951;
	/** 玩家操作日志 */
	public static final int OPT_LOG_2 = 952;
	
	/**  IP操作 */
	public static final int BAN_IP_OPERATE = 1001;  //IP操作
	public static final int BAN_IP_LIST = 1002;  //IP列表
	
	/**************** 服务器管理 **************/
	/** 创建服务器 */
	public static final int SERVER_1 = 1101; 
	/** 更新服务器 */
	public static final int SERVER_2 = 1102;
	/** 删除服务器 */
	public static final int SERVER_3 = 1103;
	/** 得到服务器列表 */
	public static final int SERVER_4 = 1104;
}

