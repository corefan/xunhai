package com.core;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import com.common.Config;
import com.servlet.CrossDomainServlet;
import com.servlet.FilterServlet;

/**
 * @author ken
 * 2014-3-7
 * web服务
 */
public class WebServer {

	private static Server server;

	private static int REQUEST_SIZE = 99999;

	/**
	 * 开启服务
	 * */
	public static void startServer() {

		try {
			
			server = new Server(Config.GAME_CC_PORT);
			server.getConnectors()[0].setRequestHeaderSize(REQUEST_SIZE);
			server.getConnectors()[0].setRequestBufferSize(WebConstant.REQ_BUFFER_SIZE);
			server.getConnectors()[0].setResponseHeaderSize(WebConstant.REQ_BUFFER_SIZE);
			server.getConnectors()[0].setResponseBufferSize(WebConstant.RESP_BUFFER_SIZE);
			
			// 线程
			ExecutorThreadPool executorThreadPool = new ExecutorThreadPool(20, 100, 10, TimeUnit.MINUTES);
			
			ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			contextHandler.setContextPath("/");
			
			contextHandler.addFilter(FilterServlet.class, "/*", null);
			// 注册servlet
			contextHandler.addServlet(new ServletHolder(new CrossDomainServlet()), "/crossdomain.xml");
			
			server.setThreadPool(executorThreadPool);
			server.setHandler(contextHandler);
			
			server.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 停止服务
	 * */
	public static void stopServer() {
		
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
