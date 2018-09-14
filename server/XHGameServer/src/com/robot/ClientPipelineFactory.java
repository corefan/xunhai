package com.robot;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;


public class ClientPipelineFactory extends ChannelInitializer<NioSocketChannel> {

	/** 数据包最大长度 */
	public static final int MAX_LENGH = 1 << 16;
	
	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		//解码
		//pipeline.addLast("lengthDecode", new LengthFieldBasedFrameDecoder(MAX_LENGH,0,4,0,4)); 
		pipeline.addLast("dataDecode", new TestDataDecode());  
		//pipeline.addLast("lengthDecode", new LengthDecoder(MAX_LENGH,0,4,0,4)); 
		//编码
		//pipeline.addLast("dataEncode", new Netty4DataEncode());
		//数据处理
		pipeline.addLast(new RobotClientHandler());
	}
}
