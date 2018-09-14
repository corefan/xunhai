package com.domain.guild;

import java.util.Set;

import org.eclipse.jetty.util.ConcurrentHashSet;

import com.domain.GameEntity;

/**
 * 联盟
 * @author ken
 * @date 2018年7月12日
 */
public class Union  extends GameEntity {

	private static final long serialVersionUID = 7453053905660691594L;

	/**
	 * 联盟编号
	 */
	private long unionId;
	
	/**
	 * 联盟名称
	 */
	private String name;
	
	/**
	 * 创建者玩家编号
	 */
	private long creatorId;
	
	/**
	 * 创建联盟的帮派
	 */
	private long guildId;
	/**
	 * 创建联盟的帮派名称
	 */
	private String guildName;
	
	/** 是否满队*/
	private boolean full;
	
	/**
	 * 申请列表
	 */
	private Set<Long> applyList = new ConcurrentHashSet<Long>();
	
	
	@Override
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO guild_union ");
		sql.append("(unionId, name, creatorId, guildId, guildName) VALUES");
		sql.append(" (");
		sql.append(unionId);
		sql.append(",");
		if(name == null){
			sql.append(name);
		}else{
			sql.append("'");
			sql.append(name);
			sql.append("'");
		}
		sql.append(",");	
		sql.append(creatorId);	
		sql.append(",");	
		sql.append(guildId);	
		sql.append(",");
		if(guildName == null){
			sql.append(guildName);
		}else{
			sql.append("'");
			sql.append(guildName);
			sql.append("'");
		}
		sql.append(")");		
		return sql.toString();
	}

	@Override
	public String getUpdateSql() {
		return null;
	}
	

	public long getUnionId() {
		return unionId;
	}

	public void setUnionId(long unionId) {
		this.unionId = unionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public Set<Long> getApplyList() {
		return applyList;
	}

	public void setApplyList(Set<Long> applyList) {
		this.applyList = applyList;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

}
