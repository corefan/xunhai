/**
 * 
 */
package com.domain.chat;

import java.io.Serializable;

import com.google.protobuf.ByteString;

/**
 * @author jiangqin
 * @date 2017-8-28
 */
public class Voice implements Serializable{

	private static final long serialVersionUID = 5311410422408024503L;

	private String id;

	private ByteString voice;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ByteString getVoice() {
		return voice;
	}

	public void setVoice(ByteString voice) {
		this.voice = voice;
	}

}
