package com.domain.guild;

import java.sql.Timestamp;
import java.util.Date;

import com.domain.GameEntity;

/**
 * 宣战记录
 * @author ken
 * @date 2018年7月4日
 */
public class GuildWar extends GameEntity {

	private static final long serialVersionUID = 3438028310577561390L;

	/** 唯一编号*/
	private long id;
	/** 帮派编号*/
	private long guildId;
	/** 目标帮派*/
	private long targetGuildId;
	/** 结束宣战时间*/
	private Date endWarTime;
	/** 是否过期*/
	private int deleteFlag;
	
	@Override
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO guild_war ");
		sql.append("(id, guildId, targetGuildId, endWarTime, deleteFlag) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(guildId);
		sql.append(",");
		sql.append(targetGuildId);
		sql.append(",");
		if(endWarTime == null){
			sql.append(endWarTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(endWarTime.getTime()));
			sql.append("'");
		}
		sql.append(",");	
		sql.append(deleteFlag);	
		sql.append(")");		
		return sql.toString();
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE guild_war SET ");
		sql.append("guildId = ");
		sql.append(guildId);
		sql.append(",");	
		sql.append("targetGuildId = ");
		sql.append(targetGuildId);
		sql.append(",");
		sql.append("endWarTime = ");
		if(endWarTime == null){
			sql.append(endWarTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(endWarTime.getTime()));
			sql.append("'");
		}
		sql.append(",");	
		sql.append("deleteFlag = ");
		sql.append(deleteFlag);
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public long getTargetGuildId() {
		return targetGuildId;
	}

	public void setTargetGuildId(long targetGuildId) {
		this.targetGuildId = targetGuildId;
	}

	public Date getEndWarTime() {
		return endWarTime;
	}

	public void setEndWarTime(Date endWarTime) {
		this.endWarTime = endWarTime;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
