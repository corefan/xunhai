package com.domain.team;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 队伍
 * @author ken
 * @date 2017-3-2
 */
public class Team implements Serializable {

	private static final long serialVersionUID = 3559415154978734673L;

	/** 进退锁*/
	private Object lock = new Object();
	/** 进副本确认锁*/
	private Object agreeLock = new Object(); 
	
	/** 创建者*/
	private int teamId;
	/** 活动编号*/
	private int activityId;
	/** 队员*/
	private Map<Long, TeamPlayer> teamPlayerMap = new ConcurrentHashMap<Long, TeamPlayer>();
	/** 最低等级*/
	private int minLevel;
	/** 创建时间*/
	private long createTime;
	
	/** 组队副本玩家准备状态*/
	private Map<Long, Integer> insPlayerIdMap = new ConcurrentHashMap<Long, Integer>();
	/** 准备进入的组队副本*/
	private int mapId; 	
	
	/** 队伍自动匹配标识(0:自动匹配, 1:不自动匹配)*/
	private int autoMatch;
	/** 开始自动匹配时间*/
	private long autoMatchTime;
	
	/** 队伍申请信息*/
	private ConcurrentLinkedDeque<Long> teamApplyIdList = new ConcurrentLinkedDeque<Long>();
	
	/** 队伍自动同意加入申请(0:不自动同意加入, 1:自动同意加入)*/
	private int autoAgreeApply;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public Map<Long, TeamPlayer> getTeamPlayerMap() {
		return teamPlayerMap;
	}

	public void setTeamPlayerMap(Map<Long, TeamPlayer> teamPlayerMap) {
		this.teamPlayerMap = teamPlayerMap;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Map<Long, Integer> getInsPlayerIdMap() {
		return insPlayerIdMap;
	}

	public void setInsPlayerIdMap(Map<Long, Integer> insPlayerIdMap) {
		this.insPlayerIdMap = insPlayerIdMap;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getAutoMatch() {
		return autoMatch;
	}

	public void setAutoMatch(int autoMatch) {
		this.autoMatch = autoMatch;
	}

	public long getAutoMatchTime() {
		return autoMatchTime;
	}

	public void setAutoMatchTime(long autoMatchTime) {
		this.autoMatchTime = autoMatchTime;
	}

	public ConcurrentLinkedDeque<Long> getTeamApplyIdList() {
		return teamApplyIdList;
	}

	public void setTeamApplyIdList(ConcurrentLinkedDeque<Long> teamApplyIdList) {
		this.teamApplyIdList = teamApplyIdList;
	}

	public int getAutoAgreeApply() {
		return autoAgreeApply;
	}

	public void setAutoAgreeApply(int autoAgreeApply) {
		this.autoAgreeApply = autoAgreeApply;
	}

	public Object getLock() {
		return lock;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

	public Object getAgreeLock() {
		return agreeLock;
	}

	public void setAgreeLock(Object agreeLock) {
		this.agreeLock = agreeLock;
	}	
	
}
