package com.domain.monster;

import java.io.Serializable;
import java.util.List;

/**
 * 刷怪表
 * @author ken
 * @date 2017-2-8
 */
public class BaseRefreshMonster implements Serializable {

	private static final long serialVersionUID = 3066622392961917762L;
	
	/** 刷怪配置编号*/
	private int ID;
	/** 刷怪半径*/
	private int refreshRange;
	/** 巡逻半径*/
	private int patrolRange;
	/** 巡逻时间间隔*/
	private String patrolTime;
	private List<Integer> patrolTimeList;
	
	/** 警戒半径*/
	private int warmRange;
	/** 追击半径*/
	private int pursuitRange;
	/** 刷新时间  <0 只刷新一次*/
	private int refreshTime;
	/** 组怪*/
	private String monsterInput;
	private List<List<Integer>> monsterInputList;
	
	/** 刷新点*/
	private int xRefresh;
	private int yRefresh;
	private int zRefresh;
	
	/** 刷新朝向*/
	private int direction;
	
	/** 是否只随机一种怪物*/
	private int random;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getRefreshRange() {
		return refreshRange;
	}
	public void setRefreshRange(int refreshRange) {
		this.refreshRange = refreshRange;
	}
	public int getPatrolRange() {
		return patrolRange;
	}
	public void setPatrolRange(int patrolRange) {
		this.patrolRange = patrolRange;
	}
	public String getPatrolTime() {
		return patrolTime;
	}
	public void setPatrolTime(String patrolTime) {
		this.patrolTime = patrolTime;
	}
	public List<Integer> getPatrolTimeList() {
		return patrolTimeList;
	}
	public void setPatrolTimeList(List<Integer> patrolTimeList) {
		this.patrolTimeList = patrolTimeList;
	}
	public int getWarmRange() {
		return warmRange;
	}
	public void setWarmRange(int warmRange) {
		this.warmRange = warmRange;
	}
	public int getPursuitRange() {
		return pursuitRange;
	}
	public void setPursuitRange(int pursuitRange) {
		this.pursuitRange = pursuitRange;
	}
	public int getRefreshTime() {
		return refreshTime;
	}
	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}
	public String getMonsterInput() {
		return monsterInput;
	}
	public void setMonsterInput(String monsterInput) {
		this.monsterInput = monsterInput;
	}
	public List<List<Integer>> getMonsterInputList() {
		return monsterInputList;
	}
	public void setMonsterInputList(List<List<Integer>> monsterInputList) {
		this.monsterInputList = monsterInputList;
	}
	public int getxRefresh() {
		return xRefresh;
	}
	public void setxRefresh(int xRefresh) {
		this.xRefresh = xRefresh;
	}
	public int getyRefresh() {
		return yRefresh;
	}
	public void setyRefresh(int yRefresh) {
		this.yRefresh = yRefresh;
	}
	public int getzRefresh() {
		return zRefresh;
	}
	public void setzRefresh(int zRefresh) {
		this.zRefresh = zRefresh;
	}
	public int getRandom() {
		return random;
	}
	public void setRandom(int random) {
		this.random = random;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
