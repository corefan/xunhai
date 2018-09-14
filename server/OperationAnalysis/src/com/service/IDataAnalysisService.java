package com.service;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;


/**
 * @author ken
 * 2014-3-20
 * 数据分析service
 */
public interface IDataAnalysisService {

	/**
	 * 查看留存
	 * */
	public JSONObject showRetain(Date startTime, Date endTime, String gameSite, String agent);
	
	/**
	 * 查看5分钟在线
	 * */
	public JSONObject fiveMinOnLine(Date startTime,Date endTime, String gameSite, String agent);
	
	/**
	 * 5分钟注册
	 * */
	public JSONObject fiveRegister(Date startTime, Date endTime, String gameSite, String agent);
	
	/**  
	 * 5分钟充值
	 * */
	public JSONObject fivePay(String startTime,String targetTime, String gameSite, String agent);
	
	/**
	 * 钻石消耗分布
	 * */
	public JSONObject diamondCost(Date startTime, Date endTime, String gameSite, String agent);
	
	/**
	 * 商场销量统计
	 * */
	public JSONObject shopSell(Date startTime, Date endTime, String gameSite, String agent);
	
	/**
	 * 当天付费率
	 * */
	public JSONObject payOne(Date startTime, Date endTime, String gameSite, String agent);
	
	/**
	 * 付费等级分布
	 * */
	public JSONObject payTwo(String gameSite, String agent);
	
	/**
	 * 游戏节点分析
	 * */
	public JSONObject gameStep(String gameSite, String agent);
	
	/**  统计每日库存钻石 */
	public JSONObject diamondData(String dateTime,String endTime,String diamondData, String agent);
	
	/**游戏区看盘 */
	public JSONObject showGameData(String agent, String gameSite);
	
	/** 指标趋势看盘*/
	public JSONObject showIndexTrendPlate(Date startTime, Date endTime, String agent, String gameSite);
	
	/** 活跃度监控*/
	public JSONObject activeMonitor(String startTime, String endTime, String agent, String gameSite);

	/** 在线时长*/
	public JSONObject onlineTime(Date startTime, Date endTime, String agent, String gameSite);
	
	/** 登陆用户分布*/
	public JSONObject loginUserContent(Date startTime, Date endTime, String agent, String gameSite);
	
	/** 充值订单*/
	public JSONObject payOrder(String startTime, String endTime, String gameSite, String agent);
	
	/** 大客户管理*/
	public JSONObject keyAccount(String gameSite, String agent);
	
	/** 客户信息*/
	public JSONObject accountInfo(String gameSite, String playerId);
	
	/** 区服充值数据*/
	public JSONObject serverPay(String startTime, String endTime, String gameSite, String agent);
	
	/** 
	 * 缓存所有区服的5分钟在线数据
	 * 加快登陆速度
	 * @param agent TODO
	 * @param gameID TODO
	 *  */
	public void cacheFiveMinData(JSONObject fiveOnlineData, JSONObject fiveRegisterData, JSONObject fivePayData, String agent, int gameID);
	
	/**
	 * 验证5分钟数据
	 * @param agent TODO
	 * @param gameID TODO
	 * */
	public Map<String, Object> checkFiveMinData(String agent, int gameID);
	
}
