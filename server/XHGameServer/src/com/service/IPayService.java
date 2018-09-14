package com.service;

/**
 * 支付
 * @author ken
 * @date 2017-6-20
 */
public interface IPayService {

	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 获取支付信息
	 */
	void getPayInfo(long playerId, int payItemId, int payType) throws Exception;
	
	/**
	 * 充值发货
	 */
	void pay(long playerId, Integer payItemId);
	
	/**
	 * 获取已首冲的列表
	 */
	void getFristPayIdList(long playerId) throws Exception;
	
}
