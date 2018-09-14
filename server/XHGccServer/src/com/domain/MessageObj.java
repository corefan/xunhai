package com.domain;

import com.core.Connection;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author ken
 * 2013-11-2
 * 消息对象
 */
public class MessageObj {

	/** 消息编号 */
	private int msgID;

	/** 消息数据 */
	private byte[] data;
	
	
	private ByteBuf buf;
	
	/** 连接 */
	private Connection connection;

	public int getMsgID() {
		return msgID;
	}

	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public MessageObj() {}

	public MessageObj(int msgID, byte[] data) {
		super();
		this.msgID = msgID;
		// 加密
		if (data != null) {
//			this.data = GameCCSocketService.encryptForDis(data);
			this.data = data;
			initBuffer();
		}
	}

	public void initBuffer() {

		//添加消息编号
		byte[] packetBytes = new byte[this.data.length + 4];
		packetBytes[0] = (byte) (msgID >> 24);
		packetBytes[1] = (byte) (msgID >> 16);
		packetBytes[2] = (byte) (msgID >> 8);
		packetBytes[3] = (byte) (msgID);
		System.arraycopy(this.data, 0, packetBytes, 4, this.data.length);

		// 添加包长
		int length = packetBytes.length;
		byte[] buffData = new byte[length + 4];
		buffData[0] = (byte) (length >> 24);
		buffData[1] = (byte) (length >> 16);
		buffData[2] = (byte) (length >> 8);
		buffData[3] = (byte) (length);
		System.arraycopy(packetBytes, 0, buffData, 4, length);

		this.buf= Unpooled.directBuffer(128,1048576).writeBytes(buffData);

	}

	public void clear() {
		this.data = null;
		this.buf = null;
	}

	public ByteBuf getBuf() {
		return buf;
	}

	public void setBuf(ByteBuf buf) {
		this.buf = buf;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
