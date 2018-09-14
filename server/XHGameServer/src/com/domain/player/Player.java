package com.domain.player;

import java.util.Date;

import com.domain.GameEntity;

/**
 * 玩家基础信息
 * @author ken
 * @date 2016-12-22
 */
public class Player extends GameEntity {

	private static final long serialVersionUID = 1338712677956426882L;
	
	/** 玩家编号 */
	private long playerId;
	/** 账号编号 */
	private long userId;
	/** 登陆站点 */
	private String site;
	/** 服务器编号 */
	private int serverNo;
	/** 全局唯一标示 */
	private String guid;
	/** 玩家名称 */
	private String playerName;
	/** 职业  (1:龙卫, 2:冰剑, 3:暗巫)*/
	private int career;
	/** 类型(1:玩家 2.GM 3.引导员 4.内部账号 5.机器人 6.封停) */
	private int type;
	/** 创建时间 */
	private Date createTime;
	/** 是否已删除*/
	private int deleteFlag;
	/** 绑定手机号 */
	private long telePhone;
	
	@Override
	public String getInsertSql() {
		return null;
	}	
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("UPDATE player SET ");
		sql.append("playerName = ");
		if (playerName == null) {
			sql.append(playerName);
		} else {
			sql.append("'");
			sql.append(playerName);
			sql.append("'");
		}	
		sql.append(",");
		sql.append("career = ");
		sql.append(career);
		sql.append(",");
		sql.append("type = ");
		sql.append(type);
		sql.append(",");
		sql.append("telePhone = ");
		sql.append(telePhone);
		
		sql.append(" WHERE playerId = ");
		sql.append(playerId);
		
		return sql.toString();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getServerNo() {
		return serverNo;
	}
	public void setServerNo(int serverNo) {
		this.serverNo = serverNo;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public long getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(long telePhone) {
		this.telePhone = telePhone;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
}
