package com.core;

import com.common.Config;
import com.common.GCCContext;
import com.quartz.QuartzService;
import com.util.LogUtil;

/**
 * @author ken
 * 2014-3-8
 * 充值服
 */
public class PayServer {

	private static PayServer instance;
	
	private PayServer() {}
	
	public static PayServer getInstance() {
		if (instance == null) instance = new PayServer();
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

			System.out.println("支付服成功关闭。。。");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("关闭socket服务发生错误。。。");
		} finally {
			System.out.println("pay stop complete");
			System.exit(-1);
		}
	}
}
