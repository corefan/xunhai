package com.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.CacheService;
import com.common.DateService;
import com.constant.CacheConstant;
import com.dao.PayLogDAO;
import com.domain.PayLog;
import com.service.IPayService;
import com.util.IDUtil;
import com.util.LogUtil;

/**
 * 支付
 * @author ken
 * @date 2017-6-21
 */
public class PayService implements IPayService {

	private PayLogDAO payLogDAO = new PayLogDAO();
	
	
	@Override
	public void initCache() {
		Map<String, PayLog> map = new ConcurrentHashMap<String, PayLog>();
		List<PayLog> lists = payLogDAO.listPayLogs();
		for(PayLog model : lists){
			map.put(model.getOutOrderNo(), model);
		}
		
		CacheService.putToCache(CacheConstant.PAY_LOG_MAP, map);
 	}

	@SuppressWarnings("unchecked")
	private Map<String, PayLog> getPayLogMap(){
		return (Map<String, PayLog>)CacheService.getFromCache(CacheConstant.PAY_LOG_MAP);
	}
	
	
	@Override
	public PayLog insertPayLog(Long userId, long playerId, String outOrderNo, String orderNo,
			int money, int payType, String payItemId, String paySite, String payUrl) {
		try {
			PayLog payLog = new PayLog();
			payLog.setLogId(IDUtil.geneteId(PayLog.class));
			payLog.setUserId(userId);
			payLog.setPlayerId(playerId);
			payLog.setPaySite(paySite);
			payLog.setOutOrderNo(outOrderNo);
			payLog.setOrderNo(orderNo);
			payLog.setMoney(money);
			payLog.setPayType(payType);
			payLog.setPayItemId(payItemId);
			payLog.setPayUrl(payUrl);
			payLog.setState(0);
			payLog.setCreateTime(DateService.getCurrentUtilDate());

			payLogDAO.createPayLog(payLog);
			
			this.getPayLogMap().put(outOrderNo, payLog);
			
			return payLog;
		} catch (Exception e) {
			LogUtil.error("插入订单异常: ",e);
		}
		return null;
	}
	
	@Override
	public void updatePayLog(PayLog payLog) {
		try {
			payLogDAO.updatePayLog(payLog);
		} catch (Exception e) {
			LogUtil.error("更新订单异常: ",e);
		}
	}

	@Override
	public PayLog getPayLogByOutOrderNo(String outOrderNo) {
		PayLog payLog = getPayLogMap().get(outOrderNo);
		if(payLog == null){
			payLog = payLogDAO.getPayLogByOutOrderNo(outOrderNo);
			
			if(payLog != null){
				this.getPayLogMap().put(outOrderNo, payLog);
			}
		}
		return payLog;
	}

}
