package com.dao;

import java.util.List;
import java.util.Map;

import com.db.DAOTemplate;

/** 
 * 用户游戏信息分析
 * @author ken
 * @date 2017-7-27
 */
public class BehaviorAnalysisDAO extends DAOTemplate {
	
	/**
	 * 精力使用情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> energyUse(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_energy_using_count_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_energy_using_count_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 每日副本情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> dailyInstance(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_carbon_count_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_carbon_count_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 多人副本情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> multiplayerInstance(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_mult_task_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_mult_task_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 恶魔城情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> castlevania(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_monster_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_monster_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 恶魔城BOSS击杀情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> castlevaniaBoss(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_monster_lev_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_monster_lev_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 王者争霸情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> kingCompetition(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_king_heg_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_king_heg_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 竞技场情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> arena(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_arena_num_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_arena_num_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 炼金情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> alchemy(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_alchemy_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_alchemy_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 每日任务情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> dailyTask(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_daily_task_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_daily_task_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 公会任务情况
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> guildTask(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_guild_task_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_cn_guild_task_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
	
	/**
	 * 诅咒殿堂时长
	 * @param startTime
	 * @param endTime
	 * @param gameSite
	 * @param agent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> temple(String startTime, String endTime, String gameSite, String agent) {
		if (gameSite == null || gameSite == "") {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_temple_of_damned_agent '"+startTime+"', '"+endTime+"', '"+agent+"'", null);
		} else {
			return (List<Map<String, Object>>) jdbcTempldate.executeProc("exec sp_temple_of_damned_mult '"+startTime+"', '"+endTime+"', '"+gameSite+"'", null);
		}
	}
}
