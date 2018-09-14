package com.constant;

/**
 * 2014-3-18
 * 操作常量
 */
public class OptTypeConstant {

	/** 总览 */
	public static final int ALL_DATA = 100;
	
	/************* 玩家管理操作常量  ***********/
	/** 查看在线 */ 
	public static final int PLAYER_1 = 101;
	/** 查看玩家信息 */ 
	public static final int PLAYER_2 = 102;
	/** 封号 */
	public static final int PLAYER_3 = 103;
	/** 解封 */
	public static final int PLAYER_4 = 104;
	/** 删除玩家缓存 */
	public static final int PLAYER_5 = 105;
	
	/** 玩家背包数据 */
	public static final int PLAYER_LOG_6 = 106;
	/** 玩家登陆日志查询 */
	public static final int PLAYER_LOG_7 = 107;
	/** 玩家消费日志查询 */
	public static final int PLAYER_LOG_8 = 108;
	/** 玩家充值日志查询 */
	public static final int PLAYER_LOG_9 = 109;
	
	/** 查看玩家信息 */ 
	public static final int PLAYER_10 = 112;
	/** 修改玩家类型 */
	public static final int PLAYER_15 = 115;
	/** 禁言*/
	public static final int PLAYER_17 = 117;
	/** 解禁*/
	public static final int PLAYER_18 = 118;
	
	/************* 物品管理操作常量  ***********/
	/** 发送物品 */
	public static final int ITEM_2 = 302;
	/** 全服发送物品 */
	public static final int ITEM_3 = 303;
	
	/** 发送邮件(不带附件) */
	public static final int ITEM_8 = 308;
	
	
	/************* 处理数据操作常量  ***********/
	/** 刷新基础缓存 */
	public static final int DEAL_DATA_1 = 801;
	/** 刷新配置文件 */
	public static final int DEAL_DATA_2 = 802;
	/** 同步缓存数据 */
	public static final int DEAL_DATA_3 = 803;
	/** 热更新class */
	public static final int DEAL_DATA_8 = 808;
	
	/************* 系统公告操作常量  ***********/
	/** 发送即时系统公告 */
	public static final int NOTICE_1 = 851;  //及时公告
	public static final int NOTICE_2 = 852;  //增加公告
	public static final int NOTICE_3 = 853;  //删除公告
	public static final int NOTICE_4 = 854;  //公告列表

	
	/**  IP操作 */
	public static final int BAN_IP_OPERATE = 1001;  //IP操作
	public static final int BAN_IP_LIST = 1002;  //IP列表
	
}

