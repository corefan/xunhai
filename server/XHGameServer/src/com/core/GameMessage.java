package com.core;



/**
 * 2013-10-30
 * 游戏消息类
 */
public class GameMessage {

	/** 消息编号 */ 
	private int msgID;
	/** 消息数据 */
	private byte[] data;
	/** 连接 */
	private Connection connection;
	
	public GameMessage() {}
	
	public int getMsgID() {
		return msgID;
	}

	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public GameMessage(int msgID, byte[] data, Connection connection) {
		super();
		this.msgID = msgID;
		this.data = data;
		this.connection = connection;
	}

}
