package com.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author ken
 * 2015-12-2
 * 充值日志	
 */
public class PayLog implements Serializable {
	
	private static final long serialVersionUID = 8881608239547580053L;
	
	/** 自增编号*/
	private Long logId;
	/** 玩家账户 */
	private Long userId;
	/** 玩家编号 */
	private Long playerId;
	/** 充值站点 */
	private String paySite;
	/** 自己的订单号 */
	private String outOrderNo;
	/** 支付平台的订单号 */
	private String orderNo;
	/** 金额 */
	private Integer money;
	/** 支付方式 */
	private Integer payType;
	/** 购买的商品编号 */
	private String payItemId;
	/** 充值链接 */
	private String payUrl;
	/** 订单状态   0:支付成功  1:支付关闭  2：支付成功 3：支付结束  */
	private Integer state;
	/** 充值时间 */
	private Date createTime;
	/** 到账时间 */
	private Date updateTime;
	
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO log_pay ");
		sql.append("(logId, userId, playerId, paySite, outOrderNo, orderNo, money, payType, payItemId,  payUrl, state, createTime, updateTime) VALUES");
		sql.append(" (");
		sql.append(logId);
		sql.append(",");
		sql.append(userId);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append("'");
		sql.append(paySite);
		sql.append("'");
		sql.append(",");
		if(outOrderNo == null){
			sql.append(outOrderNo);
		}else{
			sql.append("'");
			sql.append(outOrderNo);
			sql.append("'");
		}
		sql.append(",");
		if(orderNo == null){
			sql.append(orderNo);
		}else{
			sql.append("'");
			sql.append(orderNo);
			sql.append("'");
		}
		sql.append(",");
		sql.append(money);
		sql.append(",");
		sql.append(payType);
		sql.append(",");
		if(payItemId == null){
			sql.append(payItemId);
		}else{
			sql.append("'");
			sql.append(payItemId);
			sql.append("'");
		}
		sql.append(",");
		sql.append("'");
		sql.append(payUrl);
		sql.append("'");
		sql.append(",");
		sql.append(state);
		sql.append(",");
		if (createTime == null) {
			sql.append(createTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(createTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		if (updateTime == null) {
			sql.append(updateTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(updateTime.getTime()));
			sql.append("'");
		}
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 获得更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);			
		sql.append("UPDATE log_pay SET ");
		sql.append(" outOrderNo=");
		sql.append("'");
		sql.append(outOrderNo);	
		sql.append("'");
		sql.append(",");	
		sql.append(" orderNo=");
		sql.append("'");
		sql.append(orderNo);	
		sql.append("'");
		sql.append(",");
		sql.append(" payUrl=");
		sql.append("'");
		sql.append(payUrl);		
		sql.append("'");
		sql.append(",");			
		sql.append(" state=");
		sql.append(state);	
		sql.append(",");			
		sql.append("updateTime = ");
		if(updateTime == null){
			sql.append(updateTime);
		}else{
			sql.append("'");
			sql.append(new java.sql.Timestamp(updateTime.getTime()));
			sql.append("'");
		}
		sql.append(" WHERE logId=");
		sql.append(logId);
		
		return sql.toString();
	} 
	
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getPayItemId() {
		return payItemId;
	}

	public void setPayItemId(String payItemId) {
		this.payItemId = payItemId;
	}

	public String getPaySite() {
		return paySite;
	}
	public void setPaySite(String paySite) {
		this.paySite = paySite;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getOutOrderNo() {
		return outOrderNo;
	}
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}
	
}
