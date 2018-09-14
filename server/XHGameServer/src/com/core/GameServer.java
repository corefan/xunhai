package com.core;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.disruptor.DisruptorServer;
import com.core.jedis.RedisServer;
import com.core.jetty.web.WebServer;
import com.core.netty4.Netty4SocketServer;
import com.quartz.QuartzService;
import com.util.LogUtil;


/**
 * 服务器管理
 * @author ken
 * @date 2016-12-27
 */
public class GameServer {

	private static GameServer instance;
	
	private GameServer() {}
	
	public static GameServer getInstance() {
		if (instance == null) instance = new GameServer();
		return instance;
	}
	
	public static void main(String[] args) {
		try {
			getInstance().startServer();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * 启动服务器
	 */
	public void startServer() throws Exception {
		
		System.out.println("start sever:"+DateService.dateFormat(DateService.getCurrentUtilDate()));
		
		/** 启动redis*/
		RedisServer.getInstance().startServer();
		// 初始化配置    
		Config.init();
		// 初始化数据
		GameContext.getInstance().getServiceCollection().initialize();
		// 任务调度开启
		QuartzService.start();
		// 机器人开启
		//RobotUtil.startRobot();
		// web服务开启
		WebServer.start();
		
		// 开启socket服务
		// 启动disruptor
		DisruptorServer.getInstance().start();
		// netty4
		Netty4SocketServer.getInstance().start();
		
	}
	
	/**
	 * 关闭服务器
	 */
	public void stopServer() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		try {
			
			try {
				System.out.println("更新玩家数据");
				// 在线玩家编号列表 
				List<Long> playerIDList = serviceCollection.getGameSocketService().getOnLinePlayerIDList();
				serviceCollection.getPlayerService().closeServerDealExit(playerIDList);
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}
			
			try {
				System.out.println("开始同步缓存数据。。。");
				serviceCollection.getSynDataService().synCache_beforeClose();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}

			try {
				System.out.println("关闭socket服务。。。");
				Netty4SocketServer.getInstance().stop();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}

			try {
				System.out.println("第二次同步缓存数据。。。");
				serviceCollection.getSynDataService().synCache_beforeClose();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}
			
			
			try {
				System.out.println("关闭Web服务。。。");
				WebServer.stop();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}
			
			
			try {
				// 等待2秒，避免还有消息未处理完
				TimeUnit.SECONDS.sleep(2);
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}
			
			
			try {
				System.out.println("第三次同步缓存数据。。。");
				serviceCollection.getSynDataService().synCache_beforeClose();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}
			

			try {
				System.out.println("关闭调度服务。。。");
				QuartzService.stop();
			} catch (Exception e) {
				LogUtil.error("停服异常:",e);
			}

			System.out.println("服务器成功关闭。。。");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("第四次同步缓存数据。。。");
			serviceCollection.getSynDataService().synCache_beforeClose();
			System.out.println("关闭socket服务发生错误。。。");
		} finally {
			System.out.println("game stop complete");
			System.exit(-1);
		}
	}
	
	public static boolean isLocalAddress(String remote) throws SocketException {

		Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();

		while (ni.hasMoreElements()) {
			Enumeration<InetAddress> ips = ni.nextElement().getInetAddresses();

			while (ips.hasMoreElements()) {
				InetAddress address = ips.nextElement();
				if (address.getHostAddress().equals(remote)) {
					return true;
				}
			}
		}

		return false;
	}
	
}
