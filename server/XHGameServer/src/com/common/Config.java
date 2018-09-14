package com.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;
import com.service.IGameConfigCacheService;
import com.util.RSAUtil;
import com.util.ResourceUtil;


/**
 * 系统配置
 *
 */
public class Config {

	/********************平台配置***************/
	/** 运营商*/
	public static String AGENT;
	/** 登陆校验密钥 */
	public static String WEB_LOGIN_KEY;
	/** 充值校验密钥 */
	public static String WEB_CHARGE_KEY;
	/** 默认登陆秘钥 */
	public static String DEFAULT_LOGIN_KEY;
	/** 国家 */
	public static String COUNTRY;
	/** 语言 */
	public static String LANGUAGE;
	/** 充值IP白名单 */
	public static List<String> WEB_CHARGE_IP_LIST;
	/** netty版本(测试) */
	public static int NETTY_VERSION = 4;
	
	/** gcc数据库地址 */
	public static String DB_GCC;
	/** 游戏基础库 */
	public static String DB_BASE;
	/** 游戏日志库 */
	public static String DB_LOG;
	/** 数据库用户名 */
	public static String DB_CONFIG_USER;
	/** 数据库密码 */
	public static String DB_CONFIG_PW;
	
	/** 游戏数据库url */
	public static String DB_GAME;
	/** 游戏数据库用户名 */
	public static String DB_USER;
	/** 游戏数据库密码 */
	public static String DB_PW;
	
	/** 激活码地址 */
	public static String ACT_CODE_URL;
	/** 支付地址 */
	public static String PAY_URL;
	/** 账号服地址 */
	public static String ACCOUNT_URL;
	
	/** 防沉迷开关 */
	public static boolean FCM_SWITCH = false;
	/** 测试开关 */
	public static boolean TEST_SWITCH = false;
	
	/********************游戏服配置***************/
	/** 服务器编号 */
	public static int SEVER_NO;
	/** 游戏站点 */
	public static String GAME_SITE;
	/** 游戏公网ip */
	public static String GAME_HOST;
	/** 游戏内网ip */
	public static String GAME_INNER_IP;
	/** 游戏端口 */
	public static int GAME_PORT;
	/** web端口 */
	public static int WEB_PORT;

	/** 资源路径 */
	public static String ASSETS;

	/** 开服时间 */
	public static Date OPEN_SERVER_DATE;
	/** 合服时间*/
	public static Date MEGER_SERVER_DATE;
	
	/** 服务器是否开启(1.开启 0.关闭) */
	public static int STATE = 1;
	/** 服务器状态(0.测试 1.流畅2：拥挤3.火爆  4.维护中 5.关闭) */
	public static int SEVER_STATE = 1;
	/** 服务器类型(0.普通 1.新服 2.推荐) */
	public static int SEVER_TYPE = 0;
	
	/** 热更新目录*/
	public static String HOT_BIN_DIR;
	
	public static void init() throws Exception {
		//初始化游戏的基础设置, 服务器IP，数据库地址等.
		initMetadata();
		// 初始化国际化
		ResourceUtil.init();
	}

	private static void initMetadata() throws Exception {
		// 初始化DB部分设置
		GameConfigService configService =  GameConfigService.getInstance();
		DB_GCC = configService.getValue("DBGccURL");
		DB_BASE = configService.getValue("DBBaseURL");
		DB_LOG = configService.getValue("DBLogURL");
		DB_CONFIG_USER  = configService.getValue("DBConfigUser");
		DB_CONFIG_PW = RSAUtil.decrypt(configService.getValue("DBConfigPW"));
		 
		AGENT = configService.getValue("Agent");
		GAME_SITE = configService.getValue("GameSite");
		DB_GAME = configService.getValue("DBGameURL");
		DB_USER = configService.getValue("DBGameUser");
		DB_PW = RSAUtil.decrypt(configService.getValue("DBGamePW"));
		
		//初始数据库配置
		IGameConfigCacheService gcService = GameContext.getInstance().getServiceCollection().getGameCfgCacheService();
		gcService.initBaseCache();
		
		BaseAgentConfig agentConfig = gcService.getBaseAgentConfig(AGENT);
		
		WEB_LOGIN_KEY = agentConfig.getLoginKey();
		WEB_CHARGE_KEY = agentConfig.getChargeKey();
		DEFAULT_LOGIN_KEY = agentConfig.getDefaultLoginKey();
		COUNTRY = agentConfig.getCountry();
		LANGUAGE = agentConfig.getLanguage();
		WEB_CHARGE_IP_LIST = new ArrayList<String>();
		String[] ips = agentConfig.getChargeIP().split("\\|");
		for (int i=0;i<ips.length;i++) {
			Config.WEB_CHARGE_IP_LIST.add(ips[i]);
		}
		NETTY_VERSION = agentConfig.getNettyVersion() == 0 ? 4 : agentConfig.getNettyVersion();
		
		ACT_CODE_URL = agentConfig.getActCodeUrl();
		PAY_URL = agentConfig.getPayUrl();
		ACCOUNT_URL = agentConfig.getAccountUrl();
		
		FCM_SWITCH = agentConfig.getFcmSwitch() == 1;
		TEST_SWITCH = agentConfig.getTestSwitch() == 1;
		
		BaseServerConfig serverConfig = gcService.getBaseServerConfig(GAME_SITE);
		
		SEVER_NO = serverConfig.getServerNo();
		GAME_HOST = serverConfig.getGameHost();
		GAME_INNER_IP = serverConfig.getGameInnerIp();
		GAME_PORT = serverConfig.getGamePort();
		WEB_PORT = serverConfig.getWebPort();

		ASSETS = serverConfig.getAssets();
		if(serverConfig.getOpenServerDate() == null || "".equals(serverConfig.getOpenServerDate().trim())){
			OPEN_SERVER_DATE = DateService.getCurrentUtilDate();
		}else{
			OPEN_SERVER_DATE = DateService.parseDate(serverConfig.getOpenServerDate());
		}
		
		if(serverConfig.getMegerServerDate() == null || "".equals(serverConfig.getMegerServerDate().trim())){
			MEGER_SERVER_DATE = null;
		}else{
			MEGER_SERVER_DATE = DateService.parseDate(serverConfig.getMegerServerDate());
		}
		
		STATE = serverConfig.getState();
		SEVER_STATE = serverConfig.getSeverState();
		SEVER_TYPE = serverConfig.getSeverType();

	}

}
