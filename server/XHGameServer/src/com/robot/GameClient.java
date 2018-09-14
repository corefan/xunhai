package com.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.common.GameConfigService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class GameClient {

	public static final String host = "192.168.1.200";
	public static final int port = 8500;
	
	private static ExecutorService executor = null;
	
	private static Bootstrap b = null;
	private static final int robotNum = 500;
	
	public static int serverNo = 1;
	
	public static void init(int sNo) {
		
		executor = Executors.newFixedThreadPool(20);
		
		EventLoopGroup group = new NioEventLoopGroup();
		
        b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, true)// 保存连接
        .handler(new ClientPipelineFactory());

		serverNo = sNo;
	}
	
	private static final class DoJob implements Runnable {
		
		private int userId;
		
		public DoJob(int userId) {
			this.userId = userId;
		}

		public void run() {
			
	          try {
				ChannelFuture future = b.connect(host, port).sync();

					future.awaitUninterruptibly();
					Channel channel = future.channel();
					if (!future.isSuccess()) {
						future.cause().printStackTrace();
						channel.closeFuture().awaitUninterruptibly();
						return;
					}
					
					if (channel.isActive()) {
						RobotTest test = new RobotTest(channel);
						test.mainTest(userId);
//						try {
//							TimeUnit.MILLISECONDS.sleep(200);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
					}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static void start() {
		
		try {
			int index = serverNo * 1000000;
			for (int i=1;i<robotNum;i++) {
				if (executor != null) {
					executor.execute(new DoJob(index + i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		GameConfigService configService =  GameConfigService.getInstance();
		String GAME_SITE = configService.getValue("GameSite");
		
		int serverNo = Integer.valueOf(GAME_SITE.split("_")[1]);
		
		init(serverNo);
		
		start();
	}
}
