package com.dao;

import java.util.List;

import com.db.GccSqlSessionTemplate;
import com.domain.PayLog;

/**
 * 支付日志DAO
 * @author ken
 * @date 2017-6-20
 */
public class PayLogDAO extends GccSqlSessionTemplate {

	/**
	 * 创建支付订单日志
	 * */
	public void createPayLog(PayLog payLog) {
		this.insert_noreturn(payLog.getInsertSql());
	}
	
	/**
	 * 更新支付订单日志
	 */
	public void updatePayLog(PayLog payLog) {
		this.update(payLog.getUpdateSql());
	}
	 
	/**
	 * 取所有订单
	 */
	public List<PayLog> listPayLogs(){
		String sql = "select * from log_pay where state  > 0";
		return this.selectList(sql, PayLog.class);
	}
	
	/**
	 * 根据订单取记录
	 */
	public PayLog getPayLogByOutOrderNo(String outOrderNo){
		String sql = "select * from log_pay where outOrderNo ='"+outOrderNo+"'";
		return this.selectOne(sql, PayLog.class);
	}
}
