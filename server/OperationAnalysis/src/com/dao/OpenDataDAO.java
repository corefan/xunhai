package com.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.common.DateService;
import com.db.DAOTemplate;

/**
 * @author ken
 * 2015-12-1
 * 开放数据	
 */
public class OpenDataDAO extends DAOTemplate {

	/** 当前在线人数 */
	public int getCurrentOnlineNum(String agent) {
		
		Date maxTime = jdbcTempldate.queryForObject("select max(createTime) from t_five_online_log where agent='"+agent+"'", Date.class);
		if (maxTime == null) return 0;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(maxTime);
		
		String searchTime = DateService.dateFormatHMS(cal.getTime());
		
		String sql = "select sum(num) oNum from t_five_online_log where agent = '"+agent+"' " +
				"and DATE_FORMAT(createTime,'%Y-%m-%d %H:%i') = '"+searchTime+"'";
		
		int num = jdbcTempldate.queryForInt(sql);
		
		return num;
	}
	
	/** 
	 * 当前在线人数 
	 * 分区服
	 * */
	public List<Map<String, Object>> getCurrentOnlineNum_agent(String agent) {
		
		Date maxTime = jdbcTempldate.queryForObject("select max(createTime) from t_five_online_log where agent='"+agent+"'", Date.class);
		if (maxTime == null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(maxTime);
		
		String searchTime = DateService.dateFormatHMS(cal.getTime());
		
		String sql = "select gameSite, num from t_five_online_log where agent = '"+agent+"' " +
		"and DATE_FORMAT(createTime,'%Y-%m-%d %H:%i') = '"+searchTime+"'";
		
		List<Map<String, Object>> mapList = jdbcTempldate.queryForList(sql);
		
		return mapList;
	}
	
	/** 
	 * 当前在线人数 
	 * 某区服
	 * */
	public int getCurrentOnlineNum_site(String gameSite) {
		
		Date nowTime = jdbcTempldate.queryForObject("select now()", Date.class);
		Date maxTime = jdbcTempldate.queryForObject("select max(createTime) from t_five_online_log where gameSite='"+gameSite+"'", Date.class);
		if (maxTime == null) return -1;
		
		// 超过20分钟表示服务器有问题
		int subTime = (int) (nowTime.getTime()/1000 - maxTime.getTime()/1000);
		if (subTime >= 20 * 60) {
			return -1;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(maxTime);
		
		String searchTime = DateService.dateFormatHMS(cal.getTime());
		// CONVERT(char(10), CREATE_TIME, 120)
		String sql = "select NUM from t_five_online_log where gameSite = '"+gameSite+"' " +
		"and DATE_FORMAT(createTime,'%Y-%m-%d %H:%i') = '"+searchTime+"'";
		
		return jdbcTempldate.queryForInt(sql);
	}
	
	/** 执行DBA函数 */
	public void execDBAFunc_1(int gameId, String gameSite) {
		
		String str = "exec sp_combineReset "+gameId+", '"+gameSite+"'";
		jdbcTempldate.executeProc(str, null);

	}
}
