package com.domain;

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
			//this.data = GameSocketService.encryptForDis(data); TODO
			this.data = data;
			initBuffer();
		}
	}
	
	public void initBuffer() {

		byte[] packetBytes = new byte[this.data.length + 8];
		
		// 添加包长
		int length = packetBytes.length;
		packetBytes[0] = (byte) (length >> 24);
		packetBytes[1] = (byte) (length >> 16);
		packetBytes[2] = (byte) (length >> 8);
		packetBytes[3] = (byte) (length);
		
		//添加消息编号
		packetBytes[4] = (byte) (msgID >> 24);
		packetBytes[5] = (byte) (msgID >> 16);
		packetBytes[6] = (byte) (msgID >> 8);
		packetBytes[7] = (byte) (msgID);
		System.arraycopy(data, 0, packetBytes, 8, data.length);
		
		// 1024*1024 = 1048576;
		//this.buf = Unpooled.copiedBuffer(packetBytes);
		this.buf = Unpooled.directBuffer(128,1048576).writeBytes(packetBytes);
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

}
