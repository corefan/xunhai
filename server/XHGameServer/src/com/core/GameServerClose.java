package com.core;

import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.common.Config;
import com.common.GameConfigService;
import com.common.MD5Service;
import com.constant.HttpConstant;
import com.dao.GameConfigDao;
import com.domain.config.BaseServerConfig;
import com.util.HttpUtil;
import com.util.LogUtil;
import com.util.RSAUtil;

public class GameServerClose {

	/** 游戏主机 */
	public static String HOST = "127.0.0.1";
	/** 游戏WEB端口 */
	public static int WEB_PORT;

	/**
	 * args 两个参数的就是 ip+port.
	 * @param args
	 * @throws Exception
	 * void
	 */
	public static void main(String[] args) throws Exception {
		GameConfigDao gcDao = null;
		try {

			if(args != null && args.length > 1){
				HOST = args[0];
				WEB_PORT = Integer.parseInt(args[1]);
				
			}else{
				// 拿到cc 数据库的信息
				GameConfigService configService =  GameConfigService.getInstance();
				Config.DB_GCC = configService.getValue("DBGccURL");
				Config.DB_CONFIG_USER  = configService.getValue("DBConfigUser");
				Config.DB_CONFIG_PW = RSAUtil.decrypt(configService.getValue("DBConfigPW"));
				Config.GAME_SITE = configService.getValue("GameSite");
				
				//去拿服务器的信息
				GameConfigDao.initBoneCPSpecial(Config.DB_GCC, Config.DB_CONFIG_USER, Config.DB_CONFIG_PW);
				gcDao = new GameConfigDao();

				BaseServerConfig gcVar = gcDao.getBaseServerConfig(Config.GAME_SITE); 
				
				// 获取web端口
				WEB_PORT =gcVar.getWebPort();
				HOST = gcVar.getGameInnerIp();
				
				//关闭  bonecp 连接池
				if(gcDao != null){
					gcDao.shutdown();
				}
			}

			// 停服
			stopServer();

		} catch (Exception e) {
			LogUtil.error("停服失败:",e);
		} finally {
			TimeUnit.SECONDS.sleep(5);
			System.exit(-1);
		}

	}


	/** 停服更新 */
	private static void stopServer() {

		try {
			long time = System.currentTimeMillis();
			String key = MD5Service.encryptToUpperString("stopServer" + time);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("time", time);
			jsonObject.put("key", key);
			jsonObject.put("stopMin", 0);
			jsonObject.put("endStopMin", 0);
			
			HttpUtil.sendData_noWaitBack(jsonObject, getUrl());
		} catch (Exception e) {
			LogUtil.error("停服失败:",e);
		}
	}
	
	/** web地址 */
	private static String getUrl() {
		return "http://"+HOST+":"+WEB_PORT+HttpConstant.STOP_SERVER;
	}
}

