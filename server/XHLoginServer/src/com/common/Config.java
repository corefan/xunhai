package com.common;

import com.db.DBConfigService;
import com.domain.config.BaseAgentConfig;
import com.service.IBaseDataService;
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
	/** 中控服端口号:HTTPS */
	public static int HTTPS_PORT;
	/** 中控服端口号:HTTP */
	public static int HTTP_PORT;
	
	/** 后台数据库 */
	public static String DB_GCC;
	/** 后台数据库 */
	public static String DB_GCC_USER;
	/** 后台数据库 */
	public static String DB_GCC_PW;

	/** 登陆校验密钥 */
	public static String WEB_LOGIN_KEY;
	
	public static void init() throws Exception {
		// 初始化游戏配置
		initGame();

	}
	private static void initGame() throws Exception {
		GameConfigService configService = GameConfigService.getInstance();

		Config.HTTPS_PORT = Integer.parseInt(configService.getValue("HTTPS_PORT"));
		Config.HTTP_PORT = Integer.parseInt(configService.getValue("HTTP_PORT"));
		Config.AGENT = configService.getValue("agent");
		
		DBConfigService dbConfigService = DBConfigService.getInstance();

		Config.DB_GCC = dbConfigService.getValue("DBGCC");
		Config.DB_GCC_USER = dbConfigService.getValue("DBGCCUser");
		Config.DB_GCC_PW = RSAUtil.decrypt(dbConfigService.getValue("DBGCCPW"));
		
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		baseDataService.initData();
		
		BaseAgentConfig baseAgentConfig = baseDataService.getGameConfigConstantByAgent(Config.AGENT);
		if(baseAgentConfig == null){
			WEB_LOGIN_KEY = "QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777";
		}
		WEB_LOGIN_KEY = baseAgentConfig.getLoginKey();
	}


}
