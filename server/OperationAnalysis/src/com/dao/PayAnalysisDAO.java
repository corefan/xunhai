package com.dao;

import java.util.List;
import java.util.Map;

import com.db.DAOTemplate;

/**
 * 付费分析DAO
 * @author ken 2014-3-28 
 */
public class PayAnalysisDAO extends DAOTemplate {

	/**
	 * 5分钟充值分布
	 * */
	public List<Map<String, Object>> fivePay(String startTime, String gameSite, String agent) {

		String sql = "";
		if (gameSite == null || gameSite == "") {
			agent = formatAgent(agent);
			
			sql = "select FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(createTime)/5/60)*60* 5) date, IFNULL(SUM(money),0) num " +
					"from log_pay  WHERE DATE_FORMAT(createTime,'%Y-%m-%d') = '"+startTime+"' GROUP BY date";
			return jdbcTempldate.selectList(sql);
		} else {
			
			String[] params = gameSite.split(",");
			String gameSiteStr = "";
			
			for (int i = 0; i < params.length; i++) {
				gameSiteStr += "'" + params[i] + "',";
			}
			
			if (gameSiteStr.length() > 0) {
				gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
			}
			
			sql = "select FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(createTime)/5/60)*60* 5) date, IFNULL(SUM(money),0) num " +
					"from log_pay  WHERE DATE_FORMAT(createTime,'%Y-%m-%d') = '"+startTime+"' AND paySite IN " + gameSiteStr +" GROUP BY date";

			return jdbcTempldate.selectList(sql);
		}

	}


	/** 统计注册, 充值人数 ， 充值总额 */
	public List<Map<String, Object>> sumNum(String agent, String gameSite) {

		String sql = "";
		
		if (gameSite == null || gameSite.equals("")) {
			agent = formatAgent(agent);
			
			sql = "select paySite, count(distinct userId)  payNum, IFNULL(SUM(money),0) payMoney from log_pay GROUP BY paySite";
			return jdbcTempldate.selectList(sql);
		} else {
			String[] params = gameSite.split(",");
			String gameSiteStr = "";
			
			for (int i = 0; i < params.length; i++) {
				gameSiteStr += "'" + params[i] + "',";
			}
			
			if (gameSiteStr.length() > 0) {
				gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
			}
			
			sql = "select paySite, count(distinct userId)  payNum, IFNULL(SUM(money),0) payMoney from log_pay WHERE paySite IN " + gameSiteStr +" GROUP BY paySite";
			return jdbcTempldate.selectList(sql);
		}
	}
	
	/** 充值记录 */
	public List<Map<String, Object>> sumNum(String date, String agent, String gameSite) {

		String sql = "";
		
		if (gameSite == null || gameSite.equals("")) {
			agent = formatAgent(agent);
			
			sql = "select userId, money from log_pay where DATE_FORMAT(createTime,'%Y-%m-%d') = '"+date+"'";
			return jdbcTempldate.selectList(sql);
		} else {
			String[] params = gameSite.split(",");
			String gameSiteStr = "";
			
			for (int i = 0; i < params.length; i++) {
				gameSiteStr += "'" + params[i] + "',";
			}
			
			if (gameSiteStr.length() > 0) {
				gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
			}
			
			sql = "select userId, money from log_pay where DATE_FORMAT(createTime,'%Y-%m-%d') = '"+date+"' AND paySite IN " + gameSiteStr;
			return jdbcTempldate.selectList(sql);
		}
	}
	
	/** 支付订单查询 */
	public List<Map<String, Object>> payOrder(String startTime, String endTime, String gameSite, String agent) {
		String sql = "";
		
		startTime += " 00:00:00";
		endTime += " 23:59:59";
		
		if (gameSite == null || gameSite.equals("")) {
			agent = formatAgent(agent);
			
			sql = "SELECT logId, userId, playerId, paySite, outOrderNo, orderNo, money, payType, payItemId, payUrl, state, createTime FROM log_pay WHERE createTime >= "
					+ "'" + startTime + "' AND createTime <= '" + endTime + "'";
			
			return jdbcTempldate.selectList(sql);
		} else {
			String[] params = gameSite.split(",");
			String gameSiteStr = "";
			
			for (int i = 0; i < params.length; i++) {
				gameSiteStr += "'" + params[i] + "',";
			}
			
			if (gameSiteStr.length() > 0) {
				gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
			}
			
			sql = "SELECT logId, userId, playerId, paySite, outOrderNo, orderNo, money, payType, payItemId, payUrl, state, createTime FROM log_pay WHERE createTime >= "
					+ "'" + startTime + "' AND createTime <= '" + endTime + "' AND paySite IN " + gameSiteStr ;
			return jdbcTempldate.selectList(sql);
		}
	}
	
	/** 大客户管理*/
	public List<Map<String, Object>> keyAccount(String gameSite, String agent) {
		
		String sql = "";
		
		if (gameSite == null || gameSite.equals("")) {
			agent = formatAgent(agent);
			
			sql = "SELECT userId uid, GROUP_CONCAT(DISTINCT paySite) paySites, IFNULL(SUM(money),0) summoney, " +
					"(select IFNULL(SUM(money),0) from log_pay p where uid = p.userId and createTime >= DATE_SUB(NOW(),INTERVAL 7 DAY)) sevenPay," +
					"(select IFNULL(SUM(money),0) from log_pay p where uid = p.userId and createTime >= DATE_SUB(NOW(),INTERVAL 30 DAY)) monthPay," +
					"MAX(createTime) lastTime FROM log_pay group by uid order by summoney desc limit 100";
			
			return jdbcTempldate.selectList(sql);
		} else {
			String[] params = gameSite.split(",");
			String gameSiteStr = "";
			
			for (int i = 0; i < params.length; i++) {
				gameSiteStr += "'" + params[i] + "',";
			}
			
			if (gameSiteStr.length() > 0) {
				gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
			}
			
			sql = "SELECT userId uid, GROUP_CONCAT(DISTINCT paySite) paySites, IFNULL(SUM(money),0) summoney, " +
					"(select IFNULL(SUM(money),0) from log_pay p where uid = p.userId and paySite IN " + gameSiteStr+" and createTime >= DATE_SUB(NOW(),INTERVAL 7 DAY)) sevenPay," +
					"(select IFNULL(SUM(money),0) from log_pay p where uid = p.userId and paySite IN " + gameSiteStr+" and createTime >= DATE_SUB(NOW(),INTERVAL 30 DAY)) monthPay," +
					"MAX(createTime) lastTime FROM log_pay WHERE paySite IN " + gameSiteStr+" group by uid order by summoney desc limit 100" ;
			return jdbcTempldate.selectList(sql);
		}
	}
	
	/** 区服付费数据*/
	public List<Map<String, Object>> serverPay(String startTime, String endTime, String gameSite, String agent) {
		String sql = "";
		startTime += " 00:00:00";
		endTime += " 23:59:59";
		if (gameSite == null || gameSite == "") {
			agent = formatAgent(agent);
			
			sql = "SELECT paySite as site_name, DATE_FORMAT(createTime,'%Y-%m-%d') as date, IFNULL(SUM(money),0) summoney FROM log_pay WHERE createTime >= "
					+ "'" + startTime + "' AND createTime <= '" + endTime + "'  GROUP BY site_name,date ORDER BY site_name,date ;";
			
			return jdbcTempldate.selectList(sql);
		} else {
			
			String[] params = gameSite.split(",");
			String gameSiteStr = "";
			
			for (int i = 0; i < params.length; i++) {
				gameSiteStr += params[i] + ",";
			}
			
			if (gameSiteStr.length() > 0) {
				gameSiteStr = "(" + gameSiteStr.substring(0, gameSiteStr.length() - 1) + ")";
			}
			
			sql = "SELECT paySite as site_name, DATE_FORMAT(createTime,'%Y-%m-%d') as date, IFNULL(SUM(money),0) summoney FROM log_pay WHERE createTime >= "
					+ "'" + startTime + "' AND createTime <= '" + endTime + "' AND paySite IN " + gameSiteStr +" GROUP BY site_name,date ORDER BY site_name,date ;";
			
			return jdbcTempldate.selectList(sql);
		}
	}
	
	public List<Map<String, Object>> accountInfo(String gameSite, String playerId) {
		String sql = "SELECT * FROM t_da_user_info WHERE PLAYER_ID = '" + playerId + "' AND GAME_SITE = '" + gameSite + "'";
		return jdbcTempldate.selectList(sql);
	}
	
	
}
