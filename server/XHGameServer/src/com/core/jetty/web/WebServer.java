package com.core.jetty.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.common.Config;
import com.constant.HttpConstant;
import com.core.jetty.core.WebConstant;
import com.core.jetty.web.servlet.DealDataServlet;
import com.core.jetty.web.servlet.ItemServlet;
import com.core.jetty.web.servlet.MailServlet;
import com.core.jetty.web.servlet.PayServlet;
import com.core.jetty.web.servlet.PlayerServlet;
import com.core.jetty.web.servlet.StopSeverServlet;
import com.core.jetty.web.servlet.SystemNoticeServlet;
import com.util.LogUtil;

/**
 * 2014-3-7
 * web服务
 */
public class WebServer {

	private static Server server;

	private static int REQUEST_SIZE = 2048;

	/**
	 * 开启服务
	 * */
	public static void start() throws Exception {

		server = new Server(Config.WEB_PORT);
		server.getConnectors()[0].setRequestHeaderSize(REQUEST_SIZE);
		server.getConnectors()[0].setRequestBufferSize(WebConstant.REQ_BUFFER_SIZE);
		server.getConnectors()[0].setResponseHeaderSize(WebConstant.REQ_BUFFER_SIZE);
		server.getConnectors()[0].setResponseBufferSize(WebConstant.RESP_BUFFER_SIZE);

		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setContextPath("/");

		// 注册servlet
		//contextHandler.addServlet(new ServletHolder(new LoginServlet()), "/"+PathConstant.LOGIN);
//		contextHandler.addServlet(new ServletHolder(new LoginListServlet()), "/"+PathConstant.LOGIN_LIST);
		// 充值servlet
		contextHandler.addServlet(new ServletHolder(new PayServlet()), HttpConstant.PAY);
//		// 任务集市servlet
//		contextHandler.addServlet(new ServletHolder(new QuestMarketServlet()), "/"+PathConstant.QUEST);
//
//		// 注册GM的servlet
		contextHandler.addServlet(new ServletHolder(new SystemNoticeServlet()), HttpConstant.NOTICE);
//		contextHandler.addServlet(new ServletHolder(new IpServlet()), "/"+PathConstant.IP);
		contextHandler.addServlet(new ServletHolder(new DealDataServlet()), HttpConstant.DEAL_DATA);
		contextHandler.addServlet(new ServletHolder(new PlayerServlet()), HttpConstant.PLAYER);
		contextHandler.addServlet(new ServletHolder(new ItemServlet()), HttpConstant.ITEM);
		contextHandler.addServlet(new ServletHolder(new MailServlet()), HttpConstant.MAIL);
		contextHandler.addServlet(new ServletHolder(new StopSeverServlet()), HttpConstant.STOP_SERVER);
//		contextHandler.addServlet(new ServletHolder(new ChatLogServlet()), "/"+PathConstant.CHAT_LOG);
//
//		// 任务集市servlet
//		contextHandler.addServlet(new ServletHolder(new QuestMarketServlet()), "/"+PathConstant.QUEST);

		// 开启
		server.setHandler(contextHandler);
		server.start();

	}

	/**
	 * 停止服务
	 * */
	public static void stop() {

		try {
			server.stop();
		} catch (Exception e) {
			LogUtil.error("WEB服务停止失败: ",e);
		}
	}

}
