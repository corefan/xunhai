package com.core.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.Attribute;

import com.common.GameSocketService;
import com.core.Connection;
import com.domain.MessageObj;
import com.util.LogUtil;

public class LengthDecoder extends LengthFieldBasedFrameDecoder {

	public LengthDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		
		if (in == null) return null;
		
		Object obj = super.decode(ctx, in);
		if (obj == null) return null;
		
		ByteBuf buf = (ByteBuf) obj;
		int readableLen = buf.readableBytes(); // 可读的数据大小

		if (readableLen > Netty4CodeFactory.MAX_LENGH) { // 数据异常
			Attribute<Connection> attr = ctx.channel().attr(Netty4MessageHandler.STATE);
			if (attr != null) {
				logError(attr.get());
			} else {
				logError(null);
			}
			ctx.channel().close();
			return null;
		}

		Object result = null;
		if (readableLen >= 4 && readableLen < Netty4CodeFactory.MAX_LENGH) { // 消息头占4个字节
			//in.markReaderIndex();
			byte[] body = new byte[readableLen - 4];  //  我们读到的长度，满足我们的要求了，把传送过来的数据取出来
			// 消息编号
			int msgID = buf.readInt();
			buf.readBytes(body);
			result = convertToObject(msgID, body);  // 将byte数据转化为我们需要的对象。
			
			try {
				buf.release(); // 不释放会导致内存溢出
			} catch (Exception e) {
				LogUtil.error("收到的数据包没释放: ",e);
			}
		}
		
		return result;
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
