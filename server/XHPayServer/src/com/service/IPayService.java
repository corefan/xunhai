package com.service;

import com.domain.PayLog;

/**
 * 支付
 * @author ken
 * @date 2017-6-20
 */
public interface IPayService {

	void initCache();
	
	/**
	 * 插入记录
	 */
	PayLog insertPayLog(Long userId, long playerId, String outOrderNo, String orderNo, int money, int payType, String payItemId, String paySite, String payUrl);
	
	/**
	 * 更新记录
	 */
	void updatePayLog(PayLog payLog);
	
	/**
	 * 根据订单号取记录
	 */
	PayLog getPayLogByOutOrderNo(String outOrderNo);
	
}
