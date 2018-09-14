package com.domain.family;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.domain.GameEntity;

/**
 * 家族
 * @author jiangqin
 * @date 2017-4-6
 */
public class Family extends GameEntity {	

	private static final long serialVersionUID = -1549315941314483943L;
	
	/** 人员进退锁*/
	private Object lock = new Object();
	
	/** 家族唯一ID*/
	private long playerFamilyId;	
	
	/** 家族名称*/
	private String familyName;
	
	/** 家族宣言*/
	private String familyNotice;
	
	/** 家族创建时间*/
	private long familyCreateTime;	

	/** 家族解散倒计时*/
	private long familyDisbandTime;
	
	/** 今日是否开启了副本*/
	private int openFB;
	
	/** 删除标识*/
	private int deleteFlag;
	
	/** 家族成员*/
	private Map<Long, PlayerFamily> playerMap = new ConcurrentHashMap<Long, PlayerFamily>();
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO family ");
		sql.append("(playerFamilyId, familyName, familyNotice, familyCreateTime, familyDisbandTime, openFB, deleteFlag) VALUES"); 
		sql.append(" (");	
		sql.append(playerFamilyId);
		sql.append(",");
		if (familyName == null) {
			sql.append(familyName);
		} else {
			sql.append("'");
			sql.append(familyName);
			sql.append("'");
		}
		sql.append(",");
		if (familyNotice == null) {
			sql.append(familyNotice);
		} else {
			sql.append("'");
			sql.append(familyNotice);
			sql.append("'");
		}
		sql.append(",");
		sql.append(familyCreateTime);
		sql.append(",");
		sql.append(familyDisbandTime);
		sql.append(",");
		sql.append(openFB);
		sql.append(",");
		sql.append(deleteFlag);
		sql.append(")");
		
		return sql.toString();
	}
	

	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE family SET ");
		sql.append("familyName = ");
		if (familyName == null) {
			sql.append(familyName);
		} else {
			sql.append("'");
			sql.append(familyName);
			sql.append("'");
		}
		sql.append(",");
		sql.append("familyNotice = ");
		if (familyNotice == null) {
			sql.append(familyNotice);
		} else {
			sql.append("'");
			sql.append(familyNotice);
			sql.append("'");
		}
		sql.append(",");
		sql.append("familyCreateTime = ");
		sql.append(familyCreateTime);
		sql.append(",");
		sql.append("familyDisbandTime = ");
		sql.append(familyDisbandTime);
		sql.append(",");
		sql.append("openFB = ");
		sql.append(openFB);
		sql.append(",");
		sql.append("deleteFlag = ");
		sql.append(deleteFlag);
		sql.append(" WHERE playerFamilyId = ");
		sql.append(playerFamilyId);
		
		return sql.toString();
	}

	public long getPlayerFamilyId() {
		return playerFamilyId;
	}

	public void setPlayerFamilyId(long playerFamilyId) {
		this.playerFamilyId = playerFamilyId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyNotice() {
		return familyNotice;
	}

	public void setFamilyNotice(String familyNotice) {
		this.familyNotice = familyNotice;
	}
	

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public long getFamilyCreateTime() {
		return familyCreateTime;
	}


	public void setFamilyCreateTime(long familyCreateTime) {
		this.familyCreateTime = familyCreateTime;
	}


	public long getFamilyDisbandTime() {
		return familyDisbandTime;
	}


	public void setFamilyDisbandTime(long familyDisbandTime) {
		this.familyDisbandTime = familyDisbandTime;
	}


	public Map<Long, PlayerFamily> getPlayerMap() {
		return playerMap;
	}


	public void setPlayerMap(Map<Long, PlayerFamily> playerMap) {
		this.playerMap = playerMap;
	}

	public Object getLock() {
		return lock;
	}


	public void setLock(Object lock) {
		this.lock = lock;
	}


	public int getOpenFB() {
		return openFB;
	}


	public void setOpenFB(int openFB) {
		this.openFB = openFB;
	}


}
