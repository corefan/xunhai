package com.service;

import org.json.JSONObject;

import com.domain.OptLog;

/**
 * @author ken
 * 2014-5-3
 * 日志	
 */
public interface ILogService {

	/** 记录操作日志 
	 * @param detail TODO
	 * @param optIP TODO*/
	public void recordOptLog(Integer userID, String userName, int opt, String content, String detail, String optIP);

	/** 得到最近操作日志 */
	public JSONObject getRecentOptLogList();
	
	/** 得到某个用户最近操作日志 */
	public JSONObject getRecentOptLogListByUserName(String userName);
	
	public void batchInsert();
	
	public void insert(int userID, OptLog optLog);
	
}
