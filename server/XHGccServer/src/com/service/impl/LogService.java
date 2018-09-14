package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.common.CacheService;
import com.common.CacheSynDBService;
import com.common.DateService;
import com.constant.CacheConstant;
import com.dao.OptLogDAO;
import com.domain.OptLog;
import com.service.ILogService;
import com.util.LogUtil;

/**
 * @author ken
 * 2014-5-3
 * 日志service	
 */
public class LogService implements ILogService {

	private OptLogDAO optLogDAO = new OptLogDAO();
	@Override
	public void recordOptLog(Integer userID, String userName, int opt,
			String content, String detail, String optIP) {
		OptLog optLog = new OptLog();
		optLog.setUserID(userID);
		optLog.setUserName(userName);
		optLog.setOpt(opt);
		optLog.setOptIP(optIP);
		optLog.setDetail(detail);
		optLog.setCreateTime(DateService.getCurrentUtilDate());
		optLog.setContent(content);
		
		//optLogDAO.createOptLog(optLog);
		insert(userID, optLog);
	}

	public JSONObject getRecentOptLogList() {
		
		JSONObject jsonObject = new JSONObject();
		try {
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<Map<String,Object>> mapList = optLogDAO.getOptLogList();
			for (Map<String,Object> map : mapList) {
				JSONObject json = new JSONObject();
				
				json.put("userID", map.get("USER_ID"));
				json.put("userName", map.get("USER_NAME"));
				json.put("opt", map.get("OPT"));
				json.put("optIp", map.get("OPT_IP"));
				json.put("content", map.get("CONTENT"));
				json.put("createTime", map.get("CREATE_TIME"));
				
				jsonList.add(json);
			}
			
			jsonObject.put("dataList", jsonList.toString());
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			return jsonObject;
		}
		
		return jsonObject;
	}
	
	public JSONObject getRecentOptLogListByUserName(String userName) {
		
		JSONObject jsonObject = new JSONObject();
		try {
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<Map<String,Object>> mapList = optLogDAO.getRecentOptLogListByUserName(userName);
			for (Map<String,Object> map : mapList) {
				JSONObject json = new JSONObject();
				
				json.put("userID", map.get("USER_ID"));
				json.put("userName", map.get("USER_NAME"));
				json.put("opt", map.get("OPT"));
				json.put("optIp", map.get("OPT_IP"));
				json.put("content", map.get("CONTENT"));
				json.put("createTime", map.get("CREATE_TIME"));
				json.put("detail", map.get("DETAIL"));
				
				jsonList.add(json);
			}
			
			jsonObject.put("dataList", jsonList.toString());
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			return jsonObject;
		}
		
		return jsonObject;
	}

	@Override
	public void batchInsert() {
		
		
	}

	@Override
	public void insert(int userID,OptLog optLog) {
		
		@SuppressWarnings("unchecked")
		List<OptLog> optLogList = (List<OptLog>) CacheSynDBService.getFromOneSecondLogCache(CacheConstant.OPT_LOG_CACHE);
		optLogList.add(optLog);
		
		//日志缓存增加
		@SuppressWarnings("unchecked")
		List<OptLog> optLogs = (List<OptLog>) CacheService.getFromCache(CacheConstant.PLAYER_OPT_LOG_CACHE + userID);
		if(optLogs == null){
			optLogs = new ArrayList<OptLog>();
			CacheService.putToCache(CacheConstant.PLAYER_OPT_LOG_CACHE + userID, optLogs);
		}
		
		optLogs.add(optLog);
	}

	
	
}
