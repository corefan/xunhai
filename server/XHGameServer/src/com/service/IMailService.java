package com.service;

import java.util.List;

import com.domain.mail.MailInbox;

/**
 * 邮件系统
 * @author ken
 * @date 2017-2-14
 */
public interface IMailService {
	
	/**
	 * 初始化邮件缓存
	 */
	void initMailCache();
	
	/**
	 * 移除玩家收件缓存
	 */
	void removeMailCache(long playerId);
	
	/**
	 * 获得玩家所有收件
	 */
	List<MailInbox> getPlayerMailInboxList(long playerId);
	
	/**
	 * 根据邮件编号删除收件
	 * @param mailInboxID
	 */
	void deleteMailInboxByID(long playerId, long mailInboxID)throws Exception;
	
	/**
	 * 读取收件箱邮件
	 */
	MailInbox readInboxMail(long playerId, long mailInboxID)throws Exception;
	
	/**
	 * 根据玩家编号获得收件箱列表(分页功能)
	 */
	List<MailInbox> getMailInboxPageListByplayerId(long playerId,int start, int offset);
	
	/**
	 * 领取收件附件
	 * @throws Exception 
	 */
	void receiveAttachment(long playerId, long mailInboxID)throws Exception;
	
	
	/**
	 * 系统发送邮件
	 * @param fromType 邮件来源
	 */
	void systemSendMail(long receiverID,String theme,String content,String attachment, int fromType);
	
	/**
	 * 创建邮件
	 * */
	void batchCreateMailInBox(List<String> param);
	
	/**  全服邮件创建 */
	void sendItemToAll(String theme, String content, String attachment);
	
	/**
	 * 是否有新邮件
	 */
	int isHaveNewMail(long playerId);
	
	/** 新邮件列表 */
	List<MailInbox> getPlayerMailInboxByState(long playerId, int state);
	
	/**
	 * 调度清理邮件
	 * */
	void quartzDeleteMailInbox();
	
}
