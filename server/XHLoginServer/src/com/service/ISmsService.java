/**
 * 
 */
package com.service;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;


/**
 * 短信验证系统
 * @author jiangqin
 * @date 2017-7-20
 */
public interface ISmsService {

	/**
	 * 发送验证码(阿里云验证)
	 */
	SendSmsResponse sendSms(String telePhone, String code) throws ClientException;
	
	
	/**
	 * 查明细(阿里云验证)
	 */
	QuerySendDetailsResponse querySendDetails(String telePhone, String bizId) throws ClientException;
	
	
	/**
	 * 短信通知账号密码(阿里云)
	 */
	SendSmsResponse sendSmsRetPwd(String telePhone, String userName, String password) throws ClientException;
}
