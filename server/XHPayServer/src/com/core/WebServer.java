package com.core;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import com.common.Config;
import com.constant.PathConstant;
import com.servlet.AliPayServlet;
import com.servlet.ApplePayServlet;
import com.servlet.PayServlet;
import com.servlet.StopSeverServlet;
import com.servlet.WXPayServlet;

/**
 * @author ken
 * 2014-3-7
 * web服务
 */
public class WebServer {

	private static Server server;

	private static int REQUEST_SIZE = 40960;

	/**
	 * 开启服务
	 * */
	public static void startServer() {

		try {
			
			server = new Server(Config.PAY_SERVER_PORT);
			server.getConnectors()[0].setRequestHeaderSize(REQUEST_SIZE);
			server.getConnectors()[0].setRequestBufferSize(WebConstant.REQ_BUFFER_SIZE);
			server.getConnectors()[0].setResponseHeaderSize(WebConstant.REQ_BUFFER_SIZE);
			server.getConnectors()[0].setResponseBufferSize(WebConstant.RESP_BUFFER_SIZE);
			
			// 线程
			ExecutorThreadPool executorThreadPool = new ExecutorThreadPool(10, 50, 10, TimeUnit.MINUTES);
			
			ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			contextHandler.setContextPath("/");
			
			// 停服
			contextHandler.addServlet(new ServletHolder(new StopSeverServlet()), PathConstant.STOPSERVER);
			
			// 支付
			contextHandler.addServlet(new ServletHolder(new PayServlet()), PathConstant.PAY);
			
			// 支付宝支付
			contextHandler.addServlet(new ServletHolder(new AliPayServlet()), PathConstant.ALI_PAY);
			// 微信支付
			contextHandler.addServlet(new ServletHolder(new WXPayServlet()), PathConstant.WX_PAY);
			// 苹果支付
			contextHandler.addServlet(new ServletHolder(new ApplePayServlet()), PathConstant.IAP_PAY);
			
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
