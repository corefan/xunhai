package com.service;

import java.util.Date;



/**
 * 日志系统
 * @author ken
 * @date 2017-8-18
 */
public interface ILogService {

	
	/** 消耗日志*/
	public void createCostLog(long playerId, int type, String costName, int value);
	
	/** 商城日志*/
	public void createMarketLog(long playerId, int itemId, String itemName, int price, int num);
	
	/** 登录日志*/
	public void createLoginLog(long userId, String agent, String gameSite);
	
	/** 节点日志*/
	public void createGameStepLog(long playerId, int taskId);
	
	/** 首冲等级统计*/
	public void createFirstPayLvLog(String gameSite, int level);
	
	/** 注册日志*/
	public void createRegisterLog(long userId, String gameSite);
	
	/** 创角日志*/
	public void createPlayerLog(long userId, long playerId, String gameSite);
	
	/** 创建5分钟日志*/
	public void createFiveOnlineLog();
	
	/** 每日在线时长日志*/
	public void createOnlineTimeLog(Date date);
	
}
