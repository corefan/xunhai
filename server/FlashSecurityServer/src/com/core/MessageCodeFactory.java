package com.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;

/**
 * @author barsk
 * 2013-11-2
 * 消息编码解码factory
 */
public class MessageCodeFactory implements ChannelPipelineFactory {

	/** 数据包最大长度 */
	public static final int MAX_LENGH = 1 << 12;
	
	private final ExecutionHandler executionHandler;

	public MessageCodeFactory(ExecutionHandler executionHandler) {
		this.executionHandler = executionHandler;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {

		ChannelPipeline pipeline = Channels.pipeline();
		
		//数据处理
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("handler", new GameMessageHandler());
		
		return pipeline;
	}

}
