package com.constant;


/**
 * 聊天常量
 *
 */
public class ChatConstant {

	/** 世界频道 */
	public static final int CHAT_WORLD = 1;
	/** 家族频道 */
	public static final int CHAT_FAMILY = 2;
	/** 组队频道 */
	public static final int CHAT_TEAM = 3;
	/** 私人频道 */
	public static final int CHAT_PRIVATE = 4;
	/** 系统 */
	public static final int CHAT_SYSTEM = 5;
	/** 附近 */
	public static final int CHAT_NEARBY = 6;
	/** 大喇叭 */
	public static final int CHAT_BIG_LOUDSPEAKER = 7;
	/** 帮派 */
	public static final int CHAT_GUILD = 8;

	/** 参数类型(与协议pb ParamType对应)*/	
	/**　玩家*/	
	public static final int TYPE_PLAYER = 1;
	/**　物品*/
	public static final int TYPE_ITEM = 2;
	/**　装备*/	
	public static final int TYPE_EQUIPMENT = 3;
	/**　组队*/	
	public static final int TYPE_TEAM = 4;
	/**　好友*/
	public static final int TYPE_FRIEND = 5;
	/**　家族*/
	public static final int TYPE_FAMILY = 6;

	/** 语音缓存上限 */
	public static final int CHAT_VOICE_LIMIT = 200;
	
	/** 玩家离线信息缓存上限 */
	public static final int CHAT_OFFLINE_LIMIT = 10;
	
	
	/** 信息ID (对应BaseNotice表)*/	
	/** 恭喜{0}在挑战{1}时，鸿运当头，幸运获得了{2}*/
	public static final int CHAT_NOTICE_MAG_1 = 1;
	/** 恭喜{0}注灵段位达到{1}段，战力突破天际，可喜可贺！*/
	public static final int CHAT_NOTICE_MAG_3 = 3;	
	/** 恭喜{0}的羽翼{1}经过千锤百炼，星级全满，霸气十足，战力大幅飙升！*/
	public static final int CHAT_NOTICE_MAG_4 = 4;	
	/** 成功创建家族{0}*/
	public static final int CHAT_NOTICE_MAG_6 = 6;	
	/** {0}成为族长*/
	public static final int CHAT_NOTICE_MAG_7 = 7;	
	/** {0}进入家族*/
	public static final int CHAT_NOTICE_MAG_8 = 8;	
	/** {0}离开家族*/
	public static final int CHAT_NOTICE_MAG_9 = 9;	
	/** 队伍目标更改为：{0}*/
	public static final int CHAT_NOTICE_MAG_10 = 10;	
	/** 队伍最低等级更改为：{0}*/
	public static final int CHAT_NOTICE_MAG_11 = 11;	
	/** {0}成为队长*/
	public static final int CHAT_NOTICE_MAG_12 = 12;	
	/** {0}进入队伍*/
	public static final int CHAT_NOTICE_MAG_13 = 13;
	/** {0}离开队伍*/
	public static final int CHAT_NOTICE_MAG_14 = 14;
	/** 恭喜{0}成功满级：{1}级*/
	public static final int CHAT_NOTICE_MAG_17 = 17;
	/** 玩家{0}在{1}地图杀死了玩家{2}*/
	public static final int CHAT_NOTICE_MAG_18 = 18;
	/** 系统即将在{0}秒后停服！请做好下线准备*/
	public static final int CHAT_NOTICE_MAG_19 = 19;
	/** 恭喜{0}成功激活霸气时装{1}，风度翩翩！威武霸气!*/
	public static final int CHAT_NOTICE_MAG_20 = 20;	
	/** 恭喜{0}成功激活炫酷羽翼{1}，惊艳无比！贵气逼人！*/
	public static final int CHAT_NOTICE_MAG_21 = 21;	
	/** 恭{0}被请离队伍*/
	public static final int CHAT_NOTICE_MAG_22 = 22;
	/** 恭喜{0}将{1}注灵等级提升到{2}级，战力突破天际，可喜可贺！*/
	public static final int CHAT_NOTICE_MAG_23 = 23;	
	/** {0}邀请你加入家族：{1}*/
	public static final int CHAT_NOTICE_MAG_24 = 24;	
	/** 队长发起了副本召集，请到主城集合*/
	public static final int CHAT_NOTICE_MAG_25 = 25;	
	/** {0}被请离家族*/
	public static final int CHAT_NOTICE_MAG_26 = 26;
	/** {0}创建了都护府*/
	public static final int CHAT_NOTICE_MAG_27 = 27;
	/** {0}加入了都护府*/
	public static final int CHAT_NOTICE_MAG_28 = 28;
	/** {0}离开了都护府*/
	public static final int CHAT_NOTICE_MAG_29 = 29;
	/** {0}被逐出了都护府*/
	public static final int CHAT_NOTICE_MAG_30 = 30;
	/** {0}成为了新的【都护】*/
	public static final int CHAT_NOTICE_MAG_31 = 31;
	/** {0}被任免为【{1}】*/
	public static final int CHAT_NOTICE_MAG_32 = 32;
	/** {0}见习期表现优秀，转正为【{1}】*/
	public static final int CHAT_NOTICE_MAG_33 = 33;
	/** {0}捐献了{1}元宝*/
	public static final int CHAT_NOTICE_MAG_34 = 34;
	/** {0}将都护府等级升为：{1}*/
	public static final int CHAT_NOTICE_MAG_35 = 35;
	/** 都护府等级降为：{1}*/
	public static final int CHAT_NOTICE_MAG_36 = 36;
	/** 都护府因建设度不足，被迫解散！*/
	public static final int CHAT_NOTICE_MAG_37 = 37;
	/** {0}见习失败，被请出都护府*/
	public static final int CHAT_NOTICE_MAG_38 = 38;
	/** 都护府进入战争状态，敌对都护府为{0}！*/
	public static final int CHAT_NOTICE_MAG_39 = 39;
	/** 日常维护消耗了{0}都护府资金，{1}建设度。*/
	public static final int CHAT_NOTICE_MAG_40 = 40;
	/** 轰隆隆一声，BOSS{0}在{1}地图出现了！*/
	public static final int CHAT_NOTICE_MAG_41 = 41;
	/** 高倍经验地图【幻境】已经开启，入口开放{0}分钟，请大家前往狩猎。*/
	public static final int CHAT_NOTICE_MAG_42 = 42;
	/** 高倍经验地图【幻境】已经关闭，入口关闭{0}分钟，请大家等待下轮开放。*/
	public static final int CHAT_NOTICE_MAG_43 = 43;
	/** {0}在{1}地图杀死了BOSS{2}。*/
	public static final int CHAT_NOTICE_MAG_44 = 44;
	/** {0}在{1}地图杀死了BOSS{2}，掉落：{3}*/
	public static final int CHAT_NOTICE_MAG_45 = 45;
	/** 【{0}】都护府请求与贵都护府进行联盟， 请前往“长安城战长老”处理*/
	public static final int CHAT_NOTICE_MAG_46 = 46;
	/** 大唐都护府城战已开放报名，请各都护前往“长安城战长老”处进行报名*/
	public static final int CHAT_NOTICE_MAG_47 = 47;
	/** 大唐都护府城战将于20:00开始！攻城方【{0}】，防守方【{1}】，请做好准备。*/
	public static final int CHAT_NOTICE_MAG_48 = 48;
	/** 大唐都护府城战正式开始，请踊跃参加。*/
	public static final int CHAT_NOTICE_MAG_49 = 49;
	/** 长安城的城门已被攻破*/
	public static final int CHAT_NOTICE_MAG_50 = 50;
	/** 大唐都护府城战已经结束，长安城尚未被占领*/
	public static final int CHAT_NOTICE_MAG_51 = 51;
	/** 【{0}】都护府成功占领长安城*/
	public static final int CHAT_NOTICE_MAG_52 = 52;
	/** 【{0}】都护府成功从【{1}】都护府手中夺取长安城*/
	public static final int CHAT_NOTICE_MAG_53 = 53;
	/** 都护府加入了【{0}】联盟 */
	public static final int CHAT_NOTICE_MAG_54 = 54;
	/** 伟大的【{0}】都护府开启了【凌烟阁】副本，请占城都护府成员前往狩猎 */
	public static final int CHAT_NOTICE_MAG_55 = 55;
	/** 【{0}】家族开启了【瓦岗寨】副本，请该家族成员前往狩猎 */
	public static final int CHAT_NOTICE_MAG_56 = 56;
	/** 大唐都护府城战即将在3分钟后结束 */
	public static final int CHAT_NOTICE_MAG_57 = 57;
	/** 大唐都护府城战已经结束，【{0}】都护府占领了长安城*/
	public static final int CHAT_NOTICE_MAG_58 = 58;
	/** 玩家{0}在长安城战中击杀了【{1}】都护府的{2}{3}*/
	public static final int CHAT_NOTICE_MAG_59 = 59;
	/** 【{0}】都护府在都护府领地召唤出强大的BOSS{1}*/
	public static final int CHAT_NOTICE_MAG_60 = 60;
}
