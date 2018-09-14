package com.core;

import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.common.Config;
import com.common.GameConfigService;
import com.common.MD5Service;
import com.util.HttpUtil;
import com.util.LogUtil;

public class LoginServerClose {

	/**
	 * args 两个参数的就是 ip+port.
	 * @param args
	 * @throws Exception
	 * void
	 */
	public static void main(String[] args) throws Exception {
		try {


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
			
			HttpUtil.sendData_noWaitBack(jsonObject, getUrl());
		} catch (Exception e) {
			LogUtil.error("停服失败:",e);
		}
	}
	
	/** web地址 */
	private static String getUrl() {
		GameConfigService configService = GameConfigService.getInstance();

		Config.HTTP_PORT = Integer.parseInt(configService.getValue("HTTP_PORT"));
		return "http://127.0.0.1:"+Config.HTTP_PORT+"/stop";
	}
}

