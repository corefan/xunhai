package com.constant;

/**
 * @author ken
 * 2014-6-20
 * 	
 */
public class PathConstant {

	/** 关闭 */
	public static final String STOPSERVER = "/stop";
	
	/** 注册 */
	public static final String REGISTER = "/register";
	/** 登陆 */
	public static final String LOGIN = "/login";
	/** 游客绑定 */
	public static final String BINDING = "/binding";
	/** 创号通知 */
	public static final String CREATE_PLAYER = "/createPlayer";
	/** 发送短信验证码 */
	public static final String SEND_SMS = "/sendSms";
	/** (绑定)短信验证码信息查询 */
	public static final String BIND_PHONE = "/bindPhone";
	/** 找回密码 */
	public static final String RET_PASSWORD = "/retPassword";
	/** 修改密码 */
	public static final String CHANGE_PASSWORD = "/changePassword";
	/** 客服端日志记录 */
	public static final String CLIENT_ERROR_LOG = "/clientErrorLog";
	/** 使用激活码*/
	public static final String ACTCODE = "/actcode";
	/** 实名认证状态*/
	public static final String IDENTITY_STATE = "/identityState";
	/** 实名认证*/
	public static final String IDENTITY_CHECK = "/identityCheck";
	/** 获取登录appid*/
	public static final String APPID = "/AppId";
}
