package com.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.common.CacheService;
import com.constant.CacheConstant;
import com.db.GccSqlSessionTemplate;
import com.domain.OptLog;

/**
 * @author ken
 * 2014-5-3
 * 操作日志	
 */
public class OptLogDAO extends GccSqlSessionTemplate {

	/**
	 *  创建操作日志 
	 * */
	public void createOptLog(int userID, String userName, int opt, String content, String detail, String optIP) {
		
		
		String sql = "INSERT INTO t_opt_log(USER_ID, USER_NAME, OPT, CONTENT, DETAIL, OPT_IP, CREATE_TIME) " +
				"VALUES ("+userID+",'"+userName+"',"+opt+",'"+content+"','"+detail+"','"+optIP+"','"+new Timestamp(System.currentTimeMillis())+"')";
		
		this.insert(sql);
	}
	
	/**
	 *  创建操作日志 
	 * */
	public void createOptLog(OptLog optLog) {
		
		
		String getInsertSql = optLog.getInsertSql();
		
		this.insert(getInsertSql);
	}
	
	/**
	 * 得到最近的操作日志列表
	 * */
	public List<Map<String,Object>> getOptLogList() {
		
		String sql = "SELECT * FROM t_opt_log ORDER BY LOG_ID DESC LIMIT 0,100";
		
		return this.selectList(sql);
	}
	
	/**
	 * 得到某个玩家最近操作日志
	 * */
	public List<Map<String,Object>> getRecentOptLogListByUserName(String userName) {
		
		String sql = "SELECT * FROM t_opt_log WHERE USER_NAME='"+userName+"' ORDER BY LOG_ID DESC LIMIT 0,100";
		
		return this.selectList(sql);
	} 
	
	
	public List<OptLog> getOptLogLists(){
		@SuppressWarnings("unchecked")
		List<OptLog> optLogList = (List<OptLog>) CacheService.getFromCache(CacheConstant.OPT_LOG_CACHE);
		if(optLogList == null){
			String selectSql = "SELECT LOG_ID AS logID, USER_ID AS userID, USER_NAME AS userName, OPT AS opt, OPT_IP AS optIP, CONTENT AS content, DETAIL AS detail, CREATE_TIME AS createTime FROM t_opt_log";
			optLogList = this.selectList(selectSql, OptLog.class);
			if(optLogList != null ){
				CacheService.putToCache(CacheConstant.OPT_LOG_CACHE, optLogList);
			}
		}
		
		return optLogList;
	}
	
	
	/**
	 * 得到某个玩家最近五十条数据操作日志
	 * */
	public List<OptLog> getRecentOptLogListByUName(int userID) {
		List<OptLog> optLogList = new ArrayList<OptLog>();
		
		@SuppressWarnings("unchecked")
		List<OptLog> optLog = (List<OptLog>) CacheService.getFromCache(CacheConstant.OPT_LOG_CACHE + userID);
		if(optLog == null){
			List<OptLog> optLogs = getOptLogLists();
			if(optLogs != null){
				for (OptLog log : optLogs) {
					if(log.getUserID() == userID){
						optLogList.add(log);
					}
				}
				
				int size = optLogList.size();
				if(size > 50) optLogList.subList(0, 50);
			}
		}
		
		return optLogList;

	} 
	
}
