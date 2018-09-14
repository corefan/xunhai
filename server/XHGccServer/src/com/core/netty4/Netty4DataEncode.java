package com.core.netty4;



import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.domain.MessageObj;

public class Netty4DataEncode extends MessageToByteEncoder<MessageObj> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageObj msg, ByteBuf out)
			throws Exception {

		byte[] body = convertToBytes(msg);  //将对象转换为byte，伪代码，具体用什么进行序列化，你们自行选择。可以使用我上面说的一些
		out.writeBytes(body);  //消息体中包含我们要发送的数据
		
		ctx.write(out);
		ctx.flush();
		
		out.release();
	}

	private byte[] convertToBytes(MessageObj msg) {

		//添加消息编号
		byte[] packetBytes = new byte[msg.getData().length + 4];
		packetBytes[0] = (byte) (msg.getMsgID() >> 24);
		packetBytes[1] = (byte) (msg.getMsgID() >> 16);
		packetBytes[2] = (byte) (msg.getMsgID() >> 8);
		packetBytes[3] = (byte) (msg.getMsgID());
		System.arraycopy(msg.getData(), 0, packetBytes, 4, msg.getData().length);

		// 添加包长
		int length = packetBytes.length;
		byte[] buffData = new byte[length + 4];
		buffData[0] = (byte) (length >> 24);
		buffData[1] = (byte) (length >> 16);
		buffData[2] = (byte) (length >> 8);
		buffData[3] = (byte) (length);
		System.arraycopy(packetBytes, 0, buffData, 4, length);

		return  buffData;
	}
}
