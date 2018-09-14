package com.robot;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

import com.core.Connection;
import com.core.Connection.ConnectionState;
import com.core.disruptor.DisruptorProducerHandler;
import com.domain.MessageObj;
import com.message.MessageProto.MessageEnum.MessageID;

public class RobotClientHandler extends SimpleChannelInboundHandler<Object> {

	private DisruptorProducerHandler disruptorProducerHandler = new DisruptorProducerHandler();

	public static final AttributeKey<Connection> STATE = AttributeKey.valueOf("Connection");

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Connection con = ctx.channel().attr(STATE).get();
		if (con != null) {
			return;
		}
		
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String clientIP = insocket.getAddress().getHostAddress();
		
		Connection connection = new Connection(ctx.channel());
		connection.setConnectIP(clientIP);
		
		ctx.channel().attr(STATE).set(connection);

		super.channelActive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Connection con = ctx.channel().attr(STATE).get();
		if (con != null && con.getPlayerId() > 0 && !con.isReconnectd()) {
			MessageObj msgObj = new MessageObj(MessageID.C_ExitGame_VALUE, new byte[4]);
			disruptorProducerHandler.get().onData(msgObj, con);
			msgObj.getBuf().release();
		} else {
			ctx.channel().close();
		}

		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//System.out.println(cause.getMessage());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if (msg instanceof MessageObj) {
			MessageObj msgObj = (MessageObj) msg;
			Connection connection = (Connection) ctx.channel().attr(STATE).get();
			if (connection != null && connection.getState() != ConnectionState.EXIT) {
				int msgID = msgObj.getMsgID();
				switch (msgID) {
				case MessageID.S_LoginGame_VALUE:
					new RobotTest(ctx.channel()).createPlayer();
					break;
				case MessageID.S_CreatePlayer_VALUE:
					new RobotTest(ctx.channel()).s_createPlayer(msgObj);
					break;
				case MessageID.S_EnterGame_VALUE:
					new RobotTest(ctx.channel()).s_enterGame(msgObj);
					break;
				case MessageID.S_EnterScene_VALUE:
					new RobotTest(ctx.channel()).s_enterScene(msgObj);
					break;
				case MessageID.S_GetSceneElementList_VALUE:
					new RobotTest(ctx.channel()).s_getSceneElementList(msgObj);
					break;
				case MessageID.S_EnterComplete_VALUE:
					new RobotTest(ctx.channel()).s_enterComplete(msgObj);
					break;
				}
			}
			ReferenceCountUtil.release(msgObj); // 不调用会报refCnt: 0, decrement: 1
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}
}
