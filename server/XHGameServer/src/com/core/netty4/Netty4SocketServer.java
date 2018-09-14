package com.core.netty4;

import com.common.Config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Netty4SocketServer {

	private static ServerBootstrap serverBootstrap;
	
	private static EventLoopGroup bossGroup;
	private static EventLoopGroup workerGroup;

	public static Netty4SocketServer getInstance() {
		return SingletonHolder.instance;
	}
	
	private Netty4SocketServer() {}
	
	public void start() throws Exception {

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();

		try {	
			serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new Netty4CodeFactory())
			.option(ChannelOption.SO_BACKLOG, 1024)
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 内存池
			//.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT) // 动态内存分配
			
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 内存池
			.childOption(ChannelOption.TCP_NODELAY, true) 
			.childOption(ChannelOption.SO_KEEPALIVE, true); // 保存连接

			// 绑定并监听断开
			ChannelFuture cf = serverBootstrap.bind(Config.GAME_PORT).sync();
			
			System.out.println("game start complete");
			
			// 等待关闭事件
			cf.channel().closeFuture().sync();

		} finally {
			// 释放资源
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public void stop() {
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
	}

	private static final class SingletonHolder {
		private static final Netty4SocketServer instance = new Netty4SocketServer();
	}
}
