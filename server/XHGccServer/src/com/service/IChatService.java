package com.service;


/**
 * @author ken
 * 2014-12-26
 * 聊天	
 */
public interface IChatService {

	/**
	 * 初始化聊天缓存
	 * */
	public void initChatCache();
	
	/**
	 * 向游戏服请求聊天信息
	 * */
	public void reqChatLogFromGS(String gameSite) throws Exception;
	
}
