package com.common;

import com.db.DBConfigService;


/**
 * 系统配置
 *
 */
public class Config {

    /** 国家 */
    public static String COUNTRY;
    /** 语言 */
    public static String LANGUAGE;

    /** 激活码服端口 */
    public static int ACT_CODE_PORT;
    
    /** 数据库 */
    public static String DB_ACT_CODE;
    /** 游戏数据库 */
    public static String DB_ACT_CODE_USER;
    /** 游戏数据库 */
    public static String DB_ACT_CODE_PW;
    
    public static void init() throws Exception {
    	// 初始化游戏配置
    	initGame();
    	
        // 初始化DB配置文件
        initDB();
        
    }
    private static void initGame() throws Exception {
    	GameConfigService configService = GameConfigService.getInstance();
    	Config.ACT_CODE_PORT = Integer.parseInt(configService.getValue("ActCodePort"));
    }
    
    private static void initDB() throws Exception {
    	
    	DBConfigService configService = DBConfigService.getInstance();
    	
    	Config.DB_ACT_CODE = configService.getValue("DBACTCODE");
        Config.DB_ACT_CODE_USER = configService.getValue("DBACTCODEUser");
        Config.DB_ACT_CODE_PW = configService.getValue("DBACTCODEPW");
	}
    
}
