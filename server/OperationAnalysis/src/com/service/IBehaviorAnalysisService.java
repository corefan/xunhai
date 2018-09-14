package com.service;

import org.json.JSONObject;

public interface IBehaviorAnalysisService {
	
	/**
	 * 精力使用情况
	 * */
	public JSONObject energyUse(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 每日副本情况
	 * */
	public JSONObject dailyInstance(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 多人副本
	 * */
	public JSONObject multiplayerInstance(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 恶魔城
	 * */
	public JSONObject castlevania(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 恶魔城Boss
	 * */
	public JSONObject castlevaniaBoss(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 王者争霸
	 * */
	public JSONObject kingCompetition(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 竞技场
	 * */
	public JSONObject arena(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 炼金
	 * */
	public JSONObject alchemy(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 日常任务环数
	 * */
	public JSONObject dailyTask(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 公会任务环数
	 * */
	public JSONObject guildTask(String startTime, String endTime, String gameSite, String agent);
	
	/**
	 * 诅咒殿堂时长分布
	 * */
	public JSONObject temple(String startTime, String endTime, String gameSite, String agent);
}
