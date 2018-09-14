package com.robot;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;

import java.util.List;

import com.core.Connection;
import com.domain.MessageObj;
import com.util.LogUtil;

public class TestDataDecode extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {

		int readableLen = in.readableBytes(); // 可读的数据大小

		if (readableLen > ClientPipelineFactory.MAX_LENGH) { // 数据异常
			Attribute<Connection> attr = ctx.channel().attr(RobotClientHandler.STATE);
			if (attr != null) {
				logError(attr.get());
			} else {
				logError(null);
			}
			ctx.channel().close();
			return;
		}
		
		if (readableLen >= 4 && readableLen < ClientPipelineFactory.MAX_LENGH) { // 消息头占4个字节
			//in.markReaderIndex();
			int len = in.readInt();//总长度
			// 消息编号
			int msgID = in.readInt();
			
			byte[] body = new byte[len - 8];  //  我们读到的长度，满足我们的要求了，把传送过来的数据取出来
			in.readBytes(body);
			Object obj = convertToObject(msgID, body);  // 将byte数据转化为我们需要的对象。
			out.add(obj);  
			
			if (in instanceof UnpooledUnsafeDirectByteBuf) {
				// 释放内存空间
				in.discardReadBytes();
			}
			//in.release(); // 这样的写法会导致消息丢失或报错
		}
	}
	
	private MessageObj convertToObject(int msgID, byte[] data) {

		MessageObj obj = new MessageObj();
		obj.setMsgID(msgID);
		obj.setData(data);

		return obj;
	}

	/**
	 * 记录错误
	 * */
	private void logError(Connection con) {
		if (con != null) {
			LogUtil.error("data tool long Exception! playerID:"+con.getPlayerId());
		} else {
			LogUtil.error("data tool long Exception!");
		}
	}
}
