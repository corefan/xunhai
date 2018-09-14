package com.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 2013-11-9
 * 获得唯一数字编号
 */
public class SerialNumberUtil {

	/** long值 */
	private static AtomicLong longNum = new AtomicLong(1l);
	
	/** int值 */
	private static AtomicInteger intNum = new AtomicInteger(1);
	
	/** 战斗编号 */
	private static AtomicInteger battleID = new AtomicInteger(1);
	
	/** 生成机器人*/
	private static AtomicInteger robotPlayerId = new AtomicInteger(50000000 + 100000);
	
	/**生成机器其他编号*/
	private static AtomicInteger robotOtherID = new AtomicInteger(50000000);

	/** 公会事件编号 */
	private static AtomicInteger eventID = new AtomicInteger(1);
	
	/** 掉落编号*/
	private static AtomicInteger dropId = new AtomicInteger(1);
	
	
	/** 场景编号*/
	private static AtomicInteger sceneId = new AtomicInteger(1);
	
	/** 采集编号*/
	private static AtomicInteger collectId = new AtomicInteger(1);
	
	/** buff编号*/
	private static AtomicInteger buffId = new AtomicInteger(1);
	
	/** 队伍编号*/
	private static AtomicInteger teamId = new AtomicInteger(1);
	
	/**生成机器人名称编号*/
	private static AtomicInteger robotOtherNameID = new AtomicInteger(10000);
	
	/**
	 * 得到事件编号
	 * */
	public static int getEventID() {
		return eventID.getAndIncrement();
	}
	
	/**
	 * 得到longNum
	 * */
	public static long getLongNum() {
		return longNum.getAndIncrement();
	}
	
	/**
	 * 得到intNum
	 * */
	public static int getIntNum() {
		return intNum.getAndIncrement();
	}
	
	/**
	 * 生成战斗编号
	 * */
	public static int getBattleID() {
		return battleID.getAndIncrement();
	}
	
	/**
	 * 生成机器人ID
	 */
	public static int getRobotPlayerId(){
		return robotPlayerId.getAndIncrement();
	}
	
	/**
	 * 生成机器其他编号
	 * */
	public static int getRobotOtherID() {
		return robotOtherID.getAndIncrement();
	}

	/**
	 * 生成掉落编号
	 */
	public static int getDropId() {
		return dropId.getAndIncrement();
	}

	/**
	 * 生成场景编号
	 */
	public static int getSceneId() {
		return sceneId.getAndIncrement();
	}
	
	/**
	 * 生成采集唯一编号
	 */
	public static int getCollectId() {
		return collectId.getAndIncrement();
	}

	/**
	 * 生成buff唯一编号
	 */
	public static int getBuffId() {
		return buffId.getAndIncrement();
	}

	/**
	 * 生成队伍唯一编号
	 */
	public static int getTeamId() {
		return teamId.getAndIncrement();
	}

	/**
	 * 生成机器人名称编号
	 */
	public static int getRobotOtherNameID() {
		return robotOtherNameID.getAndIncrement();
	}

}
