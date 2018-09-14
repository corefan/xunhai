package com.domain.guild;

import java.util.Set;

import org.eclipse.jetty.util.ConcurrentHashSet;

import com.domain.GameEntity;

/**
 * 城战数据
 * @author ken
 * @date 2018年7月25日
 */
public class GuildFight extends GameEntity {

	private static final long serialVersionUID = 374844639409153911L;

	/** 自增编号*/
	private long id;
	
	/**
	 * 0：平常状态 1：报名状态  2：战前准备  3：开始状态 
	 */
	private int state;
	
	/** 守城方的帮派编号*/
	private long guildId;
	/** 守城方的帮派名称*/
	private String defendName = "";
	
	/** 攻城方  联盟或帮派编号*/
	private long attackId;
	private String attackName = "";
	
	/** 进攻的盟主帮派*/
	private long atkGuildId;
	private String atkGuildName = "";
	
	/** 是否被攻城方占领了*/
	private boolean occupy;
	
	/** 昨日累计税收*/
	private int allRevenue;
	/** 今日当前累计税收*/
	private volatile int curRevenue;
	/** 未领取税收额*/
	private int revenue;
	/** 已领取俸禄份数*/
	private int salaryNum;
	/** 今日俸禄额*/
	private int salary;
	
	/** 今日是否开启了凌烟阁*/
	private int openFB;
	
	/** 已报名城战的帮派*/
	private Set<Long> applySet = new ConcurrentHashSet<Long>();
	
	/** 当前公告的时间*/
	private long noticeTime;
	
	/** 当前诛仙台进攻方人数*/
	private volatile int attackNum;
	/** 当前诛仙台防守方人数*/
	private volatile int defendNum;
	
	@Override
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO guild_fight ");
		sql.append("(id, state, guildId, defendName, attackId, attackName, atkGuildId, atkGuildName) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(state);
		sql.append(",");
		sql.append(guildId);
		sql.append(",");
		if(defendName == null){
			sql.append(defendName);
		}else{
			sql.append("'");
			sql.append(defendName);
			sql.append("'");
		}
		sql.append(",");	
		sql.append(attackId);	
		sql.append(",");
		if(attackName == null){
			sql.append(attackName);
		}else{
			sql.append("'");
			sql.append(attackName);
			sql.append("'");
		}
		sql.append(",");	
		sql.append(atkGuildId);	
		sql.append(",");
		if(atkGuildName == null){
			sql.append(atkGuildName);
		}else{
			sql.append("'");
			sql.append(atkGuildName);
			sql.append("'");
		}
		sql.append(")");		
		return sql.toString();
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE guild_fight SET ");
		sql.append("state = ");
		sql.append(state);
		sql.append(",");
		sql.append("guildId = ");
		sql.append(guildId);
		sql.append(",");	
		sql.append("defendName = ");
		if(defendName == null){
			sql.append(defendName);
		}else{
			sql.append("'");
			sql.append(defendName);
			sql.append("'");
		}
		sql.append(",");	
		sql.append("attackId = ");
		sql.append(attackId);
		sql.append(",");	
		sql.append("attackName = ");
		if(attackName == null){
			sql.append(attackName);
		}else{
			sql.append("'");
			sql.append(attackName);
			sql.append("'");
		}
		sql.append(",");	
		sql.append("atkGuildId = ");
		sql.append(atkGuildId);
		sql.append(",");	
		sql.append("atkGuildName = ");
		if(atkGuildName == null){
			sql.append(atkGuildName);
		}else{
			sql.append("'");
			sql.append(atkGuildName);
			sql.append("'");
		}
		sql.append(",");	
		sql.append("occupy = ");
		sql.append(occupy);
		sql.append(",");	
		sql.append("allRevenue = ");
		sql.append(allRevenue);
		sql.append(",");	
		sql.append("curRevenue = ");
		sql.append(curRevenue);
		sql.append(",");	
		sql.append("revenue = ");
		sql.append(revenue);
		sql.append(",");	
		sql.append("salaryNum = ");
		sql.append(salaryNum);
		sql.append(",");	
		sql.append("salary = ");
		sql.append(salary);
		sql.append(",");	
		sql.append("openFB = ");
		sql.append(openFB);
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDefendName() {
		return defendName;
	}

	public void setDefendName(String defendName) {
		this.defendName = defendName;
	}

	public Set<Long> getApplySet() {
		return applySet;
	}

	public void setApplySet(Set<Long> applySet) {
		this.applySet = applySet;
	}

	public long getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(long noticeTime) {
		this.noticeTime = noticeTime;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public String getAttackName() {
		return attackName;
	}

	public void setAttackName(String attackName) {
		this.attackName = attackName;
	}

	public long getAttackId() {
		return attackId;
	}

	public void setAttackId(long attackId) {
		this.attackId = attackId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		if(attackNum < 0) attackNum = 0;
		this.attackNum = attackNum;
	}

	public int getDefendNum() {
		return defendNum;
	}

	public void setDefendNum(int defendNum) {
		if(defendNum < 0) defendNum = 0;
		this.defendNum = defendNum;
	}

	public long getAtkGuildId() {
		return atkGuildId;
	}

	public void setAtkGuildId(long atkGuildId) {
		this.atkGuildId = atkGuildId;
	}

	public String getAtkGuildName() {
		return atkGuildName;
	}

	public void setAtkGuildName(String atkGuildName) {
		this.atkGuildName = atkGuildName;
	}

	public boolean isOccupy() {
		return occupy;
	}

	public void setOccupy(boolean occupy) {
		this.occupy = occupy;
	}

	public int getAllRevenue() {
		return allRevenue;
	}

	public void setAllRevenue(int allRevenue) {
		this.allRevenue = allRevenue;
	}

	public int getCurRevenue() {
		return curRevenue;
	}

	public void setCurRevenue(int curRevenue) {
		this.curRevenue = curRevenue;
	}

	public int getRevenue() {
		return revenue;
	}

	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}

	public int getSalaryNum() {
		return salaryNum;
	}

	public void setSalaryNum(int salaryNum) {
		this.salaryNum = salaryNum;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getOpenFB() {
		return openFB;
	}

	public void setOpenFB(int openFB) {
		this.openFB = openFB;
	}

}
