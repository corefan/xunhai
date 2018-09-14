package com.core;

import com.common.Config;
import com.common.GCCContext;
import com.quartz.QuartzService;
import com.util.LogUtil;

/**
 * @author ken
 * 2014-3-8
 * 游戏控制中心
 */
public class LoginServer {

	private static LoginServer instance;
	
	private LoginServer() {}
	
	public static LoginServer getInstance() {
		if (instance == null) instance = new LoginServer();
		return instance;
	}
	
	public static void main(String[] args) {
		try {
			getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void start() {
		try {
			// 初始化配置
			Config.init();
			// 初始化数据
			GCCContext.getInstance().getServiceCollection().initCache();
			// 调度开启
			QuartzService.start();
			// web服务开启
			WebServer.startServer();
			
		} catch (Exception e) {
			LogUtil.error("服务启动失败:",e);
		}
		
	}
	
	/**
	 * 关闭服务器
	 */
	public void stopServer() {
		try {
			
			try {
				System.out.println("关闭Web服务。。。");
				WebServer.stopServer();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}

			try {
				System.out.println("关闭调度服务。。。");
				QuartzService.stop();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}

			System.out.println("登录服成功关闭。。。");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("关闭socket服务发生错误。。。");
		} finally {
			System.out.println("login stop complete");
			System.exit(-1);
		}
	}
	
}
