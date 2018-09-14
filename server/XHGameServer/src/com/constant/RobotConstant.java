package com.constant;

/**
 * 2014-2-20
 * 机器人常量
 */
public class RobotConstant {

	/** 机器人内城坐标:可移动 */
	public static final int[][] CITY_POS_MOVE = {
			{2672,1424},	
			{2544,1264},	
			{2928,1616},	
			{3152,1104},	
			{3664,816},	
			{2928,1968},	
			{3340,2340},	
			{3850,1840},	
			{2100,1936},	
			{1520,1552},	
			{1840,1420},	
			{1040,820},	
			{2768,2672},
			{2224,3024},
			{1136,2384},
			{1744,1776}
	};
	
	/** 机器人与女神相距*/
	public static final int DISTANCE = 60;
	
	/** 机器人: 创建女神等级*/
	public static final int CREATE_GODDESS_LEVEL = 15;
	/**机器人: 创建战兽等级*/
	public static final int CREATE_MOUNT_LEVEL = 25;
	
	/** 机器人: 制作武器装备等级*/
	public static final int MAKE_EQUIP_WEAPON_LEVEL = 26;
	
	/** 机器人: 创建衣服*/
	public static final int CREATE_CLOTHING = 27; //创建衣服
	
	/** 机器人: 制作武器衣服等级*/
	public static final int MAKE_EQUIP_CLOTHING_LEVEL = 37;
	/**机器人: 参加膜拜活动*/
	public static final int JOIN_WORSHIP_LEVEL = 35;
	
	/** 机器人: 时间随机数*/
	public static final int TIME_RANDOM_1 = 5;
	public static final int TIME_RANDOM_2 = 7;
	
	/** 升级,时间*/
	public static final int[][] LEVEL_TIME= {
		
		{41,6 * 60 * 60},
		{42,6 * 60 * 60 + 40 * 60},
		{43,6 * 60 * 60 + 40 * 60 * 2},
		{44,6 * 60 * 60 + 40 * 60 * 3},
		{45,6 * 60 * 60 + 40 * 60 * 4},
		{46,6 * 60 * 60 + 40 * 60 * 5},
		{47,6 * 60 * 60 + 40 * 60 * 6},
		{48,6 * 60 * 60 + 40 * 60 * 7},
		{49,6 * 60 * 60 + 40 * 60 * 8},
		{50,6 * 60 * 60 + 40 * 60 * 9},
		{51,12 * 60 * 60 + 40 * 60 * 9},
		{52,13 * 60 * 60 + 40 * 60 * 9},
		{53,14 * 60 * 60 + 40 * 60 * 9},
		{54,15 * 60 * 60 + 40 * 60 * 9},
		{55,16 * 60 * 60 + 40 * 60 * 9},
		{56,32 * 60 * 60 + 40 * 60 * 9},
		{57,33 * 60 * 60 + 40 * 60 * 9},
		{58,34 * 60 * 60 + 40 * 60 * 9},
		{59,35 * 60 * 60 + 40 * 60 * 9},
		{60,36 * 60 * 60 + 40 * 60 * 9},
		{61,52 * 60 * 60 + 40 * 60 * 9},
		{62,53 * 60 * 60 + 40 * 60 * 9},
		{63,54 * 60 * 60 + 40 * 60 * 9},
		{64,55 * 60 * 60 + 40 * 60 * 9},
		{65,56 * 60 * 60 + 40 * 60 * 9}
	};
	
	/** 机器人: 拥有二阶坐骑等级*/
	public static final int TWO_STEP_MOUNT_LEVEL = 46;
	/** 机器人: 拥有三阶坐骑等级*/
	public static final int THREE_STEP_MOUNT_LEVEL = 51;
	/** 机器人: 拥有四阶坐骑等级*/
	public static final int FOUR_STEP_MOUNT_LEVEL = 59;
	/** 机器人: 拥有五阶坐骑等级*/
	public static final int FIVE_STEP_MOUNT_LEVEL = 66;
	
	/** 机器人开关*/
	public static final boolean ROBOT_SWITCH = true;
}
