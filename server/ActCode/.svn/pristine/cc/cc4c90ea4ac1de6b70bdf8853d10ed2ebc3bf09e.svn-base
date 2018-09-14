package com.core;

import com.common.Config;
import com.common.CodeContext;
import com.quartz.QuartzService;
import com.util.LogUtil;

/**
 * @author barsk
 * 2014-3-8
 * 激活码服务
 */
public class ActCodeServer {

	public static void start() {
		try {
			// 初始化配置
			Config.init();
			// 初始化数据
			CodeContext.getInstance().getServiceCollection().initCache();
			// 调度开启
			QuartzService.start();
			// web服务开启
			WebServer.startServer();
		} catch (Exception e) {
			LogUtil.error("服务启动失败:",e);
		}
		
	}
	
	public static void main(String[] args) {
		start();
	}
}
