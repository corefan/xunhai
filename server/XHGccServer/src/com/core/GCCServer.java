package com.core;

import com.common.Config;
import com.common.GCCContext;
import com.core.disruptor.DisruptorServer;
import com.core.netty4.Netty4SocketServer;
import com.core.xsocket.FlashSecurityXMLServer;
import com.quartz.QuartzService;
import com.util.LogUtil;

/**
 * @author ken
 * 2014-3-8
 * 游戏控制中心
 */
public class GCCServer {

	public static void start() {
		try {
			// 初始化配置
			Config.init();
			// 初始化数据
			GCCContext.getInstance().getServiceCollection().initCache();
			// 调度开启
			QuartzService.start();
			// web服务开启
			//WebServer.startServer();
			
//			FlashSecurityXMLServer flashSecurityXMLServer = new FlashSecurityXMLServer();
//			flashSecurityXMLServer.start();
			
			// 开启socket服务
			// 启动disruptor
			DisruptorServer.getInstance().start();
			// netty4
			Netty4SocketServer.getInstance().start();
		} catch (Exception e) {
			LogUtil.error("服务启动失败:",e);
		}
		
	}
	
	public static void main(String[] args) {
		start();
	}
}
