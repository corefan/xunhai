package com.domain.config;

import java.io.Serializable;

/**
 * 游戏运营平台配置
 * @author ken
 * @date 2016-12-20
 */
public class BaseAgentConfig implements Serializable{
	
	private static final long serialVersionUID = 5412083611210446850L;
	
	/** 运营商 */
	private String agent;
	/** 登陆秘钥 */
	private String loginKey;
	/** 充值秘钥 */
	private String chargeKey;
	/** 默认登陆key */
	private String defaultLoginKey;
	/** 国家 */
	private String country;
	/** 语言 */
	private String language;
	/** 充值IP白名单*/
	private String chargeIP;
	/** netty版本 */
	private int nettyVersion;
	
	/**激活码服url*/
	private String actCodeUrl;
	/**支付地址*/
	private String payUrl;
	/**账号服地址*/
	private String accountUrl;
	
	/** 防沉迷开关*/
	private int fcmSwitch;
	/** 内网开关*/
	private int testSwitch;
	
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getLoginKey() {
		return loginKey;
	}
	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}
	public String getChargeKey() {
		return chargeKey;
	}
	public void setChargeKey(String chargeKey) {
		this.chargeKey = chargeKey;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getChargeIP() {
		return chargeIP;
	}
	public void setChargeIP(String chargeIP) {
		this.chargeIP = chargeIP;
	}
	public int getNettyVersion() {
		return nettyVersion;
	}
	public void setNettyVersion(int nettyVersion) {
		this.nettyVersion = nettyVersion;
	}
	public String getDefaultLoginKey() {
		return defaultLoginKey;
	}
	public void setDefaultLoginKey(String defaultLoginKey) {
		this.defaultLoginKey = defaultLoginKey;
	}
	public String getActCodeUrl() {
		return actCodeUrl;
	}
	public void setActCodeUrl(String actCodeUrl) {
		this.actCodeUrl = actCodeUrl;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getAccountUrl() {
		return accountUrl;
	}
	public void setAccountUrl(String accountUrl) {
		this.accountUrl = accountUrl;
	}
	public int getFcmSwitch() {
		return fcmSwitch;
	}
	public void setFcmSwitch(int fcmSwitch) {
		this.fcmSwitch = fcmSwitch;
	}
	public int getTestSwitch() {
		return testSwitch;
	}
	public void setTestSwitch(int testSwitch) {
		this.testSwitch = testSwitch;
	}
	
}
