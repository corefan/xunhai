package com.constant;

/**
 * 物品常量
 * @author ken
 * @date 2017-1-4
 */
public class ItemConstant {

	/** 装备类型*/
	public final static int GOODS_TYPE_EQUPMENT = 1; 
	/** 药品类型*/
	public final static int GOODS_TYPE_DRUG = 2; 
	/** 材料类型*/
	public final static int GOODS_TYPE_MATERIAL = 3; 
	
	/** 物品-空置状态 */
	public static final int STATE_FREE = 0;
	/** 物品-背包状态 */
	public static final int STATE_BACKPACK = 1;
	
	/** 物品状态：绑定 */
	public static final int ITEM_IS_BINDING = 1;
	/** 物品状态：未绑定 */
	public static final int ITEM_NOT_BINDING = 0;
	
	/** 装备删除状态 */
	public static final int EQUIP_STATE_DELETE = 0;
	/** 装备背包状态 */
	public static final int EQUIP_STATE_BACKPACK = 1;
	/** 装备穿戴状态 */
	public static final int EQUIP_STATE_DRESS = 2;
	/** 装备附件状态 */
	public static final int EQUIP_STATE_ATTACHMENT = 3;
	/** 装备交易行状态 */
	public static final int EQUIP_STATE_TRADE = 4;

	/***********药品栏*******************/
	/** 红药栏*/
	public static final int HP_DRUG_TYPE = 1;
	/** 蓝药栏*/
	public static final int MP_DRUG_TYPE = 2;
	
	/******************物品效果类*******************/
	/** 加hp*/
	public static final int EFFECT_TYPE_1 = 1;
	/** 加mp*/
	public static final int EFFECT_TYPE_2 = 2;
	/** 加金币*/
	public static final int EFFECT_TYPE_3 = 3;
	/** 加钻石*/
	public static final int EFFECT_TYPE_4 = 4;
	/** 加宝玉*/
	public static final int EFFECT_TYPE_5 = 5; 
	/** 加贡献*/
	public static final int EFFECT_TYPE_6 = 6;
	/** 加荣誉*/
	public static final int EFFECT_TYPE_7 = 7;
	/** 加经验*/
	public static final int EFFECT_TYPE_8 = 8;
	/** 减少pk值*/
	public static final int EFFECT_TYPE_10 = 10;
	/** 礼包*/
	public static final int EFFECT_TYPE_11 = 11;
	/** 猎妖令*/
	public static final int EFFECT_TYPE_14 = 14;
	/** 背包格扩充券*/
	public static final int EFFECT_TYPE_16 = 16;
	/** 加buff*/
	public static final int EFFECT_TYPE_17 = 17;
	/** 熔炉碎片1*/
	public static final int EFFECT_TYPE_20 = 20;
	/** 熔炉碎片2*/
	public static final int EFFECT_TYPE_21 = 21;
	/** 熔炉碎片3*/
	public static final int EFFECT_TYPE_22 = 22;
	/** 熔炉碎片4*/
	public static final int EFFECT_TYPE_23 = 23;
	/** 熔炉碎片5*/
	public static final int EFFECT_TYPE_24 = 24;
	/** 传送道具-1队伍 2家族 3帮派*/
	public static final int EFFECT_TYPE_25 = 25;
	
	/******************物品使用限制类型*******************/
	/** 三倍经验药使用上限类型*/
	public static final int ITEM_USE_LIMIT_TYPE_1 = 8;
	/** 经验丹使用上限类型*/
	public static final int ITEM_USE_LIMIT_TYPE_2 = 9;
	
	
	/******************羽化类型*******************/
	/** 羽灵羽化*/
	public static final int EVOLVE_TYPE_1 = 1;
	/** 羽毛羽化*/
	public static final int EVOLVE_TYPE_2 = 2;
}
