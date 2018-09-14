package com.domain.chat;

import java.io.Serializable;

/**
 * 消息配置
 * @author jiangqin
 * @date 2017-6-20
 */
public class BaseNotice implements Serializable{
	
	private static final long serialVersionUID = -7555035793802257667L;

	/** 消息ID*/
	private int msgId;	
	
	/** 消息内容*/
	private String msgContent;
	
	/** 广播频道*/
	private int chatChannel;
	
	/** 广播群体*/
	private int isEverybody;
	
	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getChatChannel() {
		return chatChannel;
	}

	public void setChatChannel(int chatChannel) {
		this.chatChannel = chatChannel;
	}

	public int getIsEverybody() {
		return isEverybody;
	}

	public void setIsEverybody(int isEverybody) {
		this.isEverybody = isEverybody;
	}
}
