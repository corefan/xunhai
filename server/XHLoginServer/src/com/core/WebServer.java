package com.core;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import com.common.Config;
import com.constant.PathConstant;
import com.servlet.ActCodeServlet;
import com.servlet.AppIdServlet;
import com.servlet.BindPhoneServlet;
import com.servlet.BindingServlet;
import com.servlet.ChangePasswordServlet;
import com.servlet.CreatePlayerServlet;
import com.servlet.IdentityCheckServlet;
import com.servlet.IdentityStateServlet;
import com.servlet.LoginServlet;
import com.servlet.RegisterServlet;
import com.servlet.RetrievePasswordServlet;
import com.servlet.SendSmsServlet;
import com.servlet.StopSeverServlet;

/**
 * @author ken
 * 2014-3-7
 * web服务
 */
public class WebServer {

	private static Server server;

	private static int REQUEST_SIZE = 4096;

	/**
	 * 开启服务
	 * */
	public static void startServer() {

		try {
			
			server = new Server();
			
//			String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
			
			// https
//			SslSocketConnector ssl_connector = new SslSocketConnector();
//	        ssl_connector.setPort(Config.HTTPS_PORT);
//	        ssl_connector.setAcceptors(2);
//	        ssl_connector.setAcceptQueueSize(100);
//	        ssl_connector.setMaxIdleTime(30000);
//	        SslContextFactory cf = ssl_connector.getSslContextFactory();
//	       /* cf.setKeyStorePath(path+"config/keystore");
//	        cf.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
//	        cf.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");*/
//	        
//	        cf.setKeyStorePath(path+"config/jetty.keystore");
//	        cf.setKeyStorePassword("123456");
//	        cf.setKeyManagerPassword("123456");
//	        server.addConnector(ssl_connector);
	        
	        // http
	        SelectChannelConnector sc_connector = new SelectChannelConnector();
	        sc_connector.setPort(Config.HTTP_PORT);
	        //每个请求被accept前允许等待的连接数  
	        sc_connector.setAcceptQueueSize(100);  
	        //同事监听read事件的线程数  
	        sc_connector.setAcceptors(2);  
	        //连接最大空闲时间，默认是200000，-1表示一直连接  
	        sc_connector.setMaxIdleTime(30000); 
	        server.addConnector(sc_connector);
			
	        for (int i=0;i <server.getConnectors().length;i++) {
	        	server.getConnectors()[i].setRequestHeaderSize(REQUEST_SIZE);
	        	server.getConnectors()[i].setRequestBufferSize(WebConstant.REQ_BUFFER_SIZE);
	        	server.getConnectors()[i].setResponseHeaderSize(WebConstant.REQ_BUFFER_SIZE);
	        	server.getConnectors()[i].setResponseBufferSize(WebConstant.RESP_BUFFER_SIZE);
	        }
			
			// 线程
			ExecutorThreadPool executorThreadPool = new ExecutorThreadPool(20, 100, 10, TimeUnit.MINUTES);
			
			ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			contextHandler.setContextPath("/");
			
			//contextHandler.addFilter(FilterServlet.class, "/*", null);
			// 注册servlet
			//contextHandler.addServlet(new ServletHolder(new CrossDomainServlet()), "/crossdomain.xml");
			
			contextHandler.addServlet(new ServletHolder(new StopSeverServlet()), PathConstant.STOPSERVER);
			
			contextHandler.addServlet(new ServletHolder(new RegisterServlet()), PathConstant.REGISTER);
			contextHandler.addServlet(new ServletHolder(new LoginServlet()), PathConstant.LOGIN);
			contextHandler.addServlet(new ServletHolder(new BindingServlet()), PathConstant.BINDING);
			contextHandler.addServlet(new ServletHolder(new CreatePlayerServlet()), PathConstant.CREATE_PLAYER);
			contextHandler.addServlet(new ServletHolder(new SendSmsServlet()), PathConstant.SEND_SMS);
			contextHandler.addServlet(new ServletHolder(new BindPhoneServlet()), PathConstant.BIND_PHONE);			
			contextHandler.addServlet(new ServletHolder(new RetrievePasswordServlet()), PathConstant.RET_PASSWORD);
			//contextHandler.addServlet(new ServletHolder(new ClientErrorLogServlet()), PathConstant.CLIENT_ERROR_LOG);
			contextHandler.addServlet(new ServletHolder(new ChangePasswordServlet()), PathConstant.CHANGE_PASSWORD);
			contextHandler.addServlet(new ServletHolder(new ActCodeServlet()), PathConstant.ACTCODE);
			contextHandler.addServlet(new ServletHolder(new IdentityStateServlet()), PathConstant.IDENTITY_STATE);
			contextHandler.addServlet(new ServletHolder(new IdentityCheckServlet()), PathConstant.IDENTITY_CHECK);
			
			contextHandler.addServlet(new ServletHolder(new AppIdServlet()), PathConstant.APPID);
			
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
