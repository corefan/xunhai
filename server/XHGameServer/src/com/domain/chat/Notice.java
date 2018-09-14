package com.domain.chat;

import java.io.Serializable;

import com.message.ChatProto.ParamType;
/**
 * 公告
 * @author jiangqin
 * @date 2017-5-25
 */
public class Notice implements Serializable{

	private static final long serialVersionUID = -6978155091972612640L;
	
	/** 参数类型*/
	private ParamType type;  
	/** 参数ID*/
	private long paramInt; 
	/** 参数ID2*/
	private Integer paramInt2;
	/** 参数字串*/
	private String paramStr;
	
	public Notice(){
		
	}
	
	public Notice(ParamType type, long paramInt, Integer paramInt2,
			String paramStr) {
		this.type = type;
		this.paramInt = paramInt;
		this.paramInt2 = paramInt2;
		this.paramStr = paramStr;
	}

	public ParamType getType() {
		return type;
	}
	public void setType(ParamType type) {
		this.type = type;
	}

	public long getParamInt() {
		return paramInt;
	}

	public void setParamInt(long paramInt) {
		this.paramInt = paramInt;
	}

	public Integer getParamInt2() {
		return paramInt2;
	}
	public void setParamInt2(Integer paramInt2) {
		this.paramInt2 = paramInt2;
	}
	public String getParamStr() {
		return paramStr;
	}
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}
	
}
