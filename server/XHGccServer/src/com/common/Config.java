package com.common;

import com.db.DBConfigService;
import com.util.RSAUtil;


/**
 * 系统配置
 *
 */
public class Config {

	/** 国家 */
	public static String COUNTRY;
	/** 语言 */
	public static String LANGUAGE;

	/** 中控服端口号 */
	public static int GAME_CC_PORT;

	/** 后台数据库 */
	public static String DB_GCC;
	/** 后台数据库 */
	public static String DB_GCC_USER;
	/** 后台数据库 */
	public static String DB_GCC_PW;

	
	/** 基础数据库 */
	public static String DBBASE;
	/** 基础数据库 */
	public static String DBBASEUser;
	/** 基础数据库 */
	public static String DBBASEPW;

	public static void init() throws Exception {
		// 初始化游戏配置
		initGame();

		// 初始化DB配置文件
		initDB();

	}
	private static void initGame() throws Exception {
		GameConfigService configService = GameConfigService.getInstance();

		Config.GAME_CC_PORT = Integer.parseInt(configService.getValue("GameCCPort"));
	}

	private static void initDB() throws Exception {

		DBConfigService configService = DBConfigService.getInstance();

		Config.DB_GCC = configService.getValue("DBGCC");
		Config.DB_GCC_USER = configService.getValue("DBGCCUser");
		Config.DB_GCC_PW = RSAUtil.decrypt(configService.getValue("DBGCCPW"));
		
		Config.DBBASE = configService.getValue("DBBASE");
		Config.DBBASEUser = configService.getValue("DBBASEUser");
		Config.DBBASEPW = RSAUtil.decrypt(configService.getValue("DBBASEPW"));
		
	}

}
