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

	/** 代理商 */
	public static String AGENT;
	/** 充值服端口号 */
	public static int PAY_SERVER_PORT;
	
	/** 后台数据库 */
	public static String DB_GCC;
	/** 后台数据库 */
	public static String DB_GCC_USER;
	/** 后台数据库 */
	public static String DB_GCC_PW;

	
	//支付宝充值参数
	/** 支付宝支付地址 */
	public static String Ali_PAY_URL;
	/** 支付宝app编号 */
	public static String Ali_APP_ID;
	/** RSA私密 */
	public static String Ali_PRIVATE_KEY;
	/** RSA公密 */
	public static String Ali_PUBLIC_KEY;
	/** 回调地址 */
	public static String Ali_CALL_BACK_URL;
	
	/** 微信支付app编号*/
	public static String WX_APP_ID;
	/** 微信支付商户编号*/
	public static String WX_MCH_ID;
	/** 微信支付密钥*/
	public static String WX_KEY;
	/** 微信支付回调地址 */
	public static String WX_CALL_BACK_URL;

	public static void init() throws Exception {
		// 初始化游戏配置
		initGame();

		// 初始化DB配置文件
		initDB();

	}
	private static void initGame() throws Exception {
		GameConfigService configService = GameConfigService.getInstance();

		Config.PAY_SERVER_PORT = Integer.parseInt(configService.getValue("PayServerPort"));
		Config.AGENT = configService.getValue("agent");
		
		Config.Ali_PAY_URL = configService.getValue("Ali_PAY_URL");
		Config.Ali_APP_ID = configService.getValue("Ali_APP_ID");
		Config.Ali_PRIVATE_KEY = configService.getValue("Ali_PRIVATE_KEY");
		Config.Ali_PUBLIC_KEY = configService.getValue("Ali_PUBLIC_KEY");
		Config.Ali_CALL_BACK_URL = configService.getValue("Ali_CALL_BACK_URL");
		
		Config.WX_APP_ID = configService.getValue("WX_APP_ID");
		Config.WX_MCH_ID = configService.getValue("WX_MCH_ID");
		Config.WX_KEY = configService.getValue("WX_KEY");
		Config.WX_CALL_BACK_URL = configService.getValue("WX_CALL_BACK_URL");
	}

	private static void initDB() throws Exception {

		DBConfigService configService = DBConfigService.getInstance();

		Config.DB_GCC = configService.getValue("DBGCC");
		Config.DB_GCC_USER = configService.getValue("DBGCCUser");
		Config.DB_GCC_PW = RSAUtil.decrypt(configService.getValue("DBGCCPW"));
		
	}

}
