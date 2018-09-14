package com.core.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;

import java.util.List;

import com.common.GameSocketService;
import com.core.Connection;
import com.domain.MessageObj;
import com.util.LogUtil;

public class Netty4DataDecode extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {

		int readableLen = in.readableBytes(); // 可读的数据大小

		if (readableLen > Netty4CodeFactory.MAX_LENGH) { // 数据异常
			Attribute<Connection> attr = ctx.channel().attr(Netty4MessageHandler.STATE);
			if (attr != null) {
				logError(attr.get());
			} else {
				logError(null);
			}
			ctx.channel().close();
			return;
		}

		if (readableLen >= 4 && readableLen < Netty4CodeFactory.MAX_LENGH) { // 消息头占4个字节
			//in.markReaderIndex();
			byte[] body = new byte[readableLen - 4];  //  我们读到的长度，满足我们的要求了，把传送过来的数据取出来
			// 消息编号
			int msgID = in.readInt();
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
		obj.setData(GameSocketService.decryptForDis(data));

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
