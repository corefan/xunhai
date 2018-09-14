package com.service;

import java.util.List;

import com.domain.chat.BaseNotice;
import com.domain.chat.Notice;
import com.domain.chat.Voice;
import com.google.protobuf.ByteString;


/**
 * 聊天系统
 * 
 * @author jiangqin
 * @date 2017-2-16
 */
public interface IChatService {	
	
	/** 初始化聊天数据 */
	void initBaseCache();
	
	/** 聊天 */
	void chat(long playerId, int msgId, String content, int type, long toPlayerId, String paramListStr) throws Exception;

	/**
	 * 公告
	 * @param 消息ID
	 * @param 消息内容
	 * @param 参数列表
	 * @param 需要广播的玩家列表
	 */
    void synNotice(int msgId, List<Notice> paramList, List<Long> playerIds);   
    
    /** 获取基础配置消息 */
    BaseNotice getBaseNotice(int msgId);
    
	/** 设置是否接受陌生人信息 */
    void setIsAcceptChat(long playerId, int state);
	
	/** 上传语音数据 */
	void postVoice(String id, ByteString voice);
	
	/** 获取语音数据 */
	Voice getVoice(String id);	
	
	/** 获取离线聊天信息 */
	void getOfflineInfo(long playerId);
}
