package com.core;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class FlashSecurityServer {

	private static final int PORT = 843;
	
	private ServerBootstrap bootstrap;
	
	public static byte[] xmlByte;
	
	public FlashSecurityServer() {
		
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<cross-domain-policy>");
		xmlBuffer.append("<allow-access-from domain=\"");
		xmlBuffer.append("*");
		xmlBuffer.append("\" to-ports=\"");
		xmlBuffer.append("*");
		xmlBuffer.append("\"/>");
		xmlBuffer.append("</cross-domain-policy>");
		xmlBuffer.append("\0");
		String policyXml = xmlBuffer.toString();
		
		xmlByte = policyXml.getBytes();
	}
	
	public static void main(String[] args) {
		try {
			new FlashSecurityServer().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开启服务
	 * */
	public void start() throws Exception {

		/**
		 * Boss线程：(处理连接请求)
		 * 每个server服务器都会有一个boss线程，每绑定一个InetSocketAddress都会产生一个boss线程,
		 * 比如：我们开启了两个服务器端口8080和443，则我们会有两个boss线程。一个boss线程在端口绑定后，
		 * 会接收传进来的连接，一旦连接接收成功，boss线程会指派一个worker线程处理连接。
		 * Worker线程:(处理消息数据)
		 * 一个NioServerSocketChannelFactory会有一个或者多个worker线程。
		 * 一个worker线程在非阻塞模式下为一个或多个Channels提供非阻塞 读或写线程的生命周期和优雅的关闭
		 * */

		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory
				(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		
		bootstrap.setPipelineFactory(new MessageCodeFactory(ThreadPoolService.getInstance().getExecutionHandler()));

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		// 连接队列长度(非连接数)
		bootstrap.setOption("backlog", 2048);

		bootstrap.bind(new InetSocketAddress(PORT));

		System.out.println("flashSecurityServer start complete");
	}
}
