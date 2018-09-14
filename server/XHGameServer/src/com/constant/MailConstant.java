package com.constant;

import com.util.ResourceUtil;

/**
 * 邮件常量
 * @author ken
 */
public class MailConstant {
	/** 已收取附件 */
	public static final int HAD_RECEIVE_ATTACHMENT = 1;
	/** 未收取附件 */
	public static final int NOT_RECEIVE_ATTACHMENT = 0; 
	
	/** 邮件类型:系统 */
	public static final int MAIL_TYPE_SYSTEM = 1;
	
	/** 系统邮件发送名字 */
	public static final String SYSTEM_MAIL_SENDER_NAME = ResourceUtil.getValue("mail_1");
	
	/** 含有附件 */
	public static final int HAVE_ATTACHMENT = 1;
	/** 没有附件 */
	public static final int NOT_HAVE_ATTACHMENT = 0;
	
	
	/** 收件状态:新邮件 */
	public static final int MAIL_STATE_NEW = 0;
	/** 收件状态:已读 */
	public static final int MAIL_STATE_READED = 1;
	
}
