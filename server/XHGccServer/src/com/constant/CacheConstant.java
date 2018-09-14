package com.constant;

/**
 * @author ken
 * 2014-3-10
 * 缓存常量
 */
public class CacheConstant {
	
	/** 物品缓存配置 */
	public static final String B_ITEM_MAP = "B_ITEM_MAP";
	
	/** 服务器缓存配置 */
	public static final String B_SERVER_CONF_MAP = "serverConfMap";
	
	/** 游戏节点列表 */
	public static final String B_GAME_STEP_NAME_MAP = "gameStepNameMap";
	
	/** 权限缓存 */
	public static final String AUTHORITY_CACHE = "authorityListCache";
	/** 角色缓存 */
	public static final String ROLE_CACHE = "roleListCache";
	/** 角色-权限列表缓存 */
	public static final String ROLE_ID_AUTHORITY_IDLIST = "roleIDAuthorityIDList"; 
	/** 用户缓存 */
	public static final String USER_CACHE = "userListCache";
	
	/** 玩家日志缓存*/
	public static final String PLAYER_OPT_LOG_CACHE = "playerOptLogCache";
	
	/** 日志缓存*/
	public static final String OPT_LOG_CACHE = "optLogCache";
	
	/** 用户-Socket连接缓存 */
	public static final String CONNECTIONID_CONNECTION_MAP = "connectionIDMapCache";
	
	/** 聊天信息缓存 */
	public static final String CHAT_INFO_CACHE = "cacheInfoCache";
	/** 聊天信息变化 */
	public static final String CHAT_CHANGE_CACHE = "cacheChangeCache";
	/** 连接编号-聊天记录标示 */
	public static final String CONNECTIONID_CHAT_POS = "connectionIDChatPos";
	
}
