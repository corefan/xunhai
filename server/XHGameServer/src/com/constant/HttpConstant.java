package com.constant;

/**
 * http请求常量
 * @author ken
 * @date 2017-3-17
 */
public class HttpConstant {

	/*********************接收******************/
	/** 充值 */
	public static final String PAY  = "/pay";
	/** 停服 */
	public static final String STOP_SERVER  = "/stop";
	/** 邮件 */
	public static final String MAIL  = "/mail";
	/** 处理数据 */
	public static final String DEAL_DATA = "/dealData";
	/** 玩家处理*/
	public static final String PLAYER = "/player";
	/** 物品处理*/
	public static final String ITEM = "/item";
	/** 公告处理*/
	public static final String NOTICE = "/notice";
	
	
	/*********************发送******************/
	/** 创号通知登录服 */
	public static final String CREATE_PLAYER = "/createPlayer";	
	/** 发送短信验证码 */
	public static final String SEND_SMS = "/sendSms";
	/** (绑定)短信验证码信息查询 */
	public static final String BIND_PHONE = "/bindPhone";	
	/** 使用激活码 */
	public static final String ACTCODE = "/actcode";
	/** 实名认证状态*/
	public static final String IDENTITY_STATE = "/identityState";
	/** 实名认证*/
	public static final String IDENTITY_CHECK = "/identityCheck";
	
}
