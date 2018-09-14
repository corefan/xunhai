package com.domain.guild;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 玩家帮会信息 
 * @author ken
 * @date 2018年3月31日
 */
public class PlayerGuild extends GameEntity {

	private static final long serialVersionUID = -6363276334993681498L;

	/** 玩家编号*/
	private long playerId;
	/** 帮会编号*/
	private long guildId;
	/** 帮会角色 */
	private int roleId; 
	/** 进入帮会时间 */
	private Date joinTime;
	/** 继任票数*/
	private int ticket;
	/** 贡献*/
	private int contribution;
	/** 本周资金*/
	private int weekMoney;
	/** 本周建设*/
	private int weekBuildNum;
	/** 已学习帮派技能*/
	private String skillInfo;
	private Map<Integer, Integer> skillMap = new HashMap<Integer, Integer>();
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_guild ");
		sql.append("(playerId, guildId, roleId, joinTime, ticket, contribution, weekMoney, weekBuildNum, skillInfo) VALUES"); 
		sql.append(" (");	
		sql.append(playerId);
		sql.append(",");
		sql.append(guildId);
		sql.append(",");
		sql.append(roleId);
		sql.append(",");
		if(joinTime == null){
			sql.append(joinTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(joinTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append(ticket);
		sql.append(",");
		sql.append(contribution);
		sql.append(",");
		sql.append(weekMoney);
		sql.append(",");
		sql.append(weekBuildNum);
		sql.append(",");
		if (skillInfo == null) {
			sql.append(skillInfo);
		} else {
			sql.append("'");
			sql.append(skillInfo);
			sql.append("'");
		}
		sql.append(")");
		
		return sql.toString();
	}
	

	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_guild SET ");
		sql.append("guildId = ");
		sql.append(guildId);
		sql.append(",");
		sql.append("roleId = ");
		sql.append(roleId);
		sql.append(",");
		sql.append("joinTime = ");
		if(joinTime == null){
			sql.append(joinTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(joinTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("ticket = ");
		sql.append(ticket);
		sql.append(",");
		sql.append("contribution = ");
		sql.append(contribution);
		sql.append(",");
		sql.append("weekMoney = ");
		sql.append(weekMoney);
		sql.append(",");
		sql.append("weekBuildNum = ");
		sql.append(weekBuildNum);
		sql.append(",");
		sql.append("skillInfo = ");
		if (skillInfo == null) {
			sql.append(skillInfo);
		} else {
			sql.append("'");
			sql.append(skillInfo);
			sql.append("'");
		}
		sql.append(" WHERE playerId = ");
		sql.append(playerId);
		
		return sql.toString();
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getGuildId() {
		return guildId;
	}
	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	public int getTicket() {
		return ticket;
	}
	public void setTicket(int ticket) {
		this.ticket = ticket;
	}
	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		this.contribution = contribution;
	}


	public int getWeekMoney() {
		return weekMoney;
	}


	public void setWeekMoney(int weekMoney) {
		this.weekMoney = weekMoney;
	}


	public int getWeekBuildNum() {
		return weekBuildNum;
	}


	public void setWeekBuildNum(int weekBuildNum) {
		this.weekBuildNum = weekBuildNum;
	}


	public String getSkillInfo() {
		return skillInfo;
	}


	public void setSkillInfo(String skillInfo) {
		this.skillInfo = skillInfo;
		
		Map<Integer, Integer> map = SplitStringUtil.getIntIntMap(skillInfo);
		if(map != null){
			this.skillMap = map;
		}else{
			this.skillMap.clear();
		}
	}


	public Map<Integer, Integer> getSkillMap() {
		return skillMap;
	}


	public void setSkillMap(Map<Integer, Integer> skillMap) {
		this.skillMap = skillMap;
		
		this.skillInfo = SplitStringUtil.getStringByIntIntMap(skillMap);
	}


}
