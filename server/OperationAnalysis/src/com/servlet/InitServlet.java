package com.servlet;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

import com.common.GCCContext;
import com.common.ServiceCollection;
import com.common.jedis.RedisServer;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = -5652071148837106939L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		//初始redis
		/** 启动redis*/
		RedisServer.getInstance().startServer();
		// 初始化数据
		GCCContext.getInstance();
		GCCContext.getInstance().getServiceCollection().initCache();

		String prefix = getServletContext().getRealPath("/");
		String file = getInitParameter("log4j");

		if (System.getProperty("catalina.base") == null) {
			System.setProperty("catalina.base", prefix);
		}
		
		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
		}
		
		refreshBaseData();
	}
	
	public void refreshBaseData() {
		
		 ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

	        long initialDelay1 = 15;
	        long period1 = 30;
	        // 从现在开始5钟之后，每隔30分钟执行一次RefreshJob
	        service.scheduleAtFixedRate(
	                new RefreshJob(), initialDelay1,
	                period1, TimeUnit.MINUTES);
	}
	
	private final class RefreshJob implements Runnable {
		
		public void run() {
			ServiceCollection serviceCollection = GCCContext.getInstance().getServiceCollection();
			serviceCollection.getUserService().initUserCache();
			serviceCollection.getBaseDataService().initData();
		}
		
	}
}
