/**
 * 
 */
package com.domain.chat;

import java.io.Serializable;

/**
 * @author jiangqin
 * @date 2017-11-29
 */
public class OfflineInfo implements Serializable{
	
	private static final long serialVersionUID = 7219895581918845516L;

	/** 消息编号 */
	private long id;		
	/** 消息内容 */
	private String content;
	/** 消息参数 */
	private String param;
	/** 消息创建时间 */
	private long createTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
