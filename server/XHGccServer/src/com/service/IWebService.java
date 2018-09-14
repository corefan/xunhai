package com.service;

import org.eclipse.jetty.client.HttpEventListener;
import org.json.JSONObject;

/**
 * web请求游戏端
 * @author ken
 *
 */
public interface IWebService {

	/**
	 * 发送web请求
	 */
	public void sendDateForWeb(JSONObject jsonObject, String url, HttpEventListener eventListener) throws Exception;
	
}
