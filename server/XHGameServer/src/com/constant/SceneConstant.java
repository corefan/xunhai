package com.constant;

/**
 * 场景常量
 * @author ken
 * @date 2017-1-6
 */
public class SceneConstant {

	
	/** 地图小格子大小 */
	public static final int CELL_SIZE = 50;
	/** 阻挡块 */
	public static final int IS_BLOAK = 1; 
	/** 区域格子大小 */
	public static final int GRID_SIZE = 1200;
	/** 区域格子转化成地图多少个小格子 */
	public static final int GRID_CELL_SIZE = 24;
	
	/** 副本区域格子大小 */
	public static final int INSTANCE_GRID_SIZE = 10000000;
	
	/** 最大线路*/
	public static final int MAX_LINE = 1;
	
	/** 世界场景*/
	public static final int TYPE_CITY = 1;
	/** 野外地图*/
	public static final int TYPE_WORLD = 2;
	/** 单人副本*/
	public static final int TYPE_SINGLE = 3;
	/** 组队副本*/
	public static final int TYPE_TEAM = 4;
	/** 大荒塔*/
	public static final int TYPE_TOWER = 5;
	/** 侍魂殿*/
	public static final int TYPE_TIANTI = 6;
	/** 帮派*/
	public static final int TYPE_GUILD = 7;
	/** 家族*/
	public static final int TYPE_FAMILY = 8;
	
	/** 主城*/
	public static final int MAIN_CITY = 1;
	/** 野外地图*/
	public static final int WORLD_SCENE = 2;
	/** 大荒塔地图*/
	public static final int TOWER_SCENE = 3;
	/** 副本地图*/
	public static final int INSTANCE_SCENE = 4;
	/** 侍魂殿地图*/
	public static final int TIANTI_SCENE = 5;
	/** 都护府地图*/
	public static final int GUILD_SCENE = 6;
	
	/** 侍魂殿地图*/
	public static final int[] TIANTI_MAP_IDS = new int[]{5101, 5102, 5103};
	
	/** 侍魂殿pkBuff*/
	public static final int PK_BUFF_1 = 3001;
	public static final int PK_BUFF_2 = 3002;
	public static final int PK_BUFF_3 = 3003;
	/** 侍魂殿pkBuff掉落时间(秒)*/
	public static final int ADD_PK_BUFF_SCE_1= 90;
	public static final int ADD_PK_BUFF_SCE_2 = 120;
	public static final int ADD_PK_BUFF_SCE_3 = 150;
	
	/** 副本状态-正常*/
	public static final int SCENE_STATE_COMMON = 0;
	/** 副本状态-结束*/
	public static final int SCENE_STATE_END = 1;
	/** 副本状态-销毁*/
	public static final int SCENE_STATE_DISTROY = 2;
	
	/** 地图模式-和平*/
	public static final int PK_MODEL_PEACE = 1;
	/** 地图模式-安全*/
	public static final int PK_MODEL_SAFE = 2;
	/** 地图模式-pk*/
	public static final int PK_MODEL_PK = 3;
	/** 地图模式-PVP pk不掉落*/
	public static final int PK_MODEL_PVP = 4;
	
	/** 一般副本和场景*/
	public static final int INSTANCE_TYPE_0 = 0;
	/** 副本类型-正常任务副本*/
	public static final int INSTANCE_TYPE_41 = 41;
	/** 副本类型-环任务副本*/
	public static final int INSTANCE_TYPE_42 = 42;	
	/** 秘境副本*/
	public static final int INSTANCE_TYPE_43 = 43;
	/** 活动副本（NPC弹出）*/
	public static final int INSTANCE_TYPE_44 = 44;
}
