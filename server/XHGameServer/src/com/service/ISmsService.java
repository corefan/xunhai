package com.service;


/**
 * 短信服务
 * @author ken
 * @date 2017-7-24
 */
public interface ISmsService {
	
	/** 获取账号绑定信息 */
	void getBindInfo(long playerId);
	
	/** 获取手机验证码 */
	void getValidateCode(long playerId, long telephone) throws Exception;
	
	/** 绑定*/
	void bindPhone(long userId, long playerId, long telephone, int code, String bizId) throws Exception;
	
	/** 领取绑定奖励 */
	void getBindReward(long playerId) throws Exception;
}
