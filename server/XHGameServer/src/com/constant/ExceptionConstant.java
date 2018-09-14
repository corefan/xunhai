package com.constant;

/**
 * 2014-6-10
 * 异常常量	
 */
public class ExceptionConstant {
	
	/** 操作失败*/
	public static final int ERROR_10000= 1;
	/** 网络异常*/
	public static final int ERROR_2= 2;
	
	/** 游戏账号有误*/
	public static final int LOGIN_1000= 1000;
	/** 游戏站点有误*/
	public static final int LOGIN_1001= 1001;
	/** 账号已经存在*/
	public static final int LOGIN_1002= 1002;
	/** 创建账号异常*/
	public static final int LOGIN_1003= 1003;
	
	/** 账号不存在*/
	public static final int CREATE_1100 = 1100;
	/** 职业编号有误*/
	public static final int CREATE_1101 = 1101;
	/** 角色昵称不为空*/
	public static final int CREATE_1102 = 1102;
	/** 角色昵称为2-16字符*/
	public static final int CREATE_1103 = 1103;
	/** 角色昵称包含敏感字或特殊字符*/
	public static final int CREATE_1104 = 1104;
	/** 角色职业已存在*/
	public static final int CREATE_1105 = 1105;
	/** 角色昵称已存在*/
	public static final int CREATE_1106 = 1106;
	/** 角色创建有误*/
	public static final int CREATE_1107 = 1107;	
	
	/** 玩家等级不足*/
	public static final int PLAYER_1110 = 1110;
	/** 玩家不在线*/
	public static final int PLAYER_1111 = 1111;
	/** 玩家金币不足*/
	public static final int PLAYER_1112 = 1112;
	/** 玩家元宝不足*/
	public static final int PLAYER_1113 = 1113;
	/** 玩家宝玉不足*/
	public static final int PLAYER_1114 = 1114;	
	/** 玩家不存在*/
	public static final int PLAYER_1115 = 1115;
	/** 玩家贡献不足*/
	public static final int PLAYER_1116 = 1116;
	
	/** 已在场景内*/
	public static final int SCENE_1200 = 1200;
	/** 今日副本次数不足*/
	public static final int SCENE_1201 = 1201;
	/** 您不是队长，无法开启副本*/
	public static final int SCENE_1202 = 1202;
	/** 您在副本中，无法进入*/
	public static final int SCENE_1203 = 1203;
	/** 和平地图中，技能效果无效*/
	public static final int SCENE_1204 = 1204;
	/** 不在副本中*/
	public static final int SCENE_1205 = 1205;
	/** 在大荒塔中， 无法重置*/
	public static final int SCENE_1206 = 1206;
	/** 当前队伍人员数量超出副本人数上限*/
	public static final int SCENE_1207 = 1207;
	/** 不在主城无法进入组队副本*/
	public static final int SCENE_1208 = 1208;
	/** 副本中，无法使用召集令*/
	public static final int SCENE_1209 = 1209;
	/** 无法传送，神境·仙域地图需要vip */
	public static final int SCENE_1210 = 1210;
	/** 幻境已经关闭，请等待下一轮开启 */
	public static final int SCENE_1211 = 1211;
	/** 无法传送，幻境已经关闭 */
	public static final int SCENE_1212 = 1212;
	
	/** 物品不存在*/
	public static final int BAG_1300 = 1300;
	/** 该物品不可出售*/
	public static final int BAG_1301 = 1301;
	/** 购买数量有误 */
	public static final int BAG_1302= 1302;
	/** 背包物品不存在 */
	public static final int BAG_1303 = 1303;
	/** 背包空间不足   */
	public static final int BAG_1304 = 1304;
	/** 该物品不能直接使用*/
	public static final int BAG_1305 = 1305;
	/** 物品数量不足*/
	public static final int BAG_1306 = 1306;
	/** 无法使用物品*/
	public static final int BAG_1307 = 1307;
	/** 物品职业不符*/
	public static final int BAG_1308 = 1308;
	/** 您暂时没有拾取权*/
	public static final int BAG_1309 = 1309;	
	/** 每人只能开启一个宝箱*/
	public static final int BAG_1310 = 1310;
	/** 背包格已达上限,不能再使用背包卷*/
	public static final int BAG_1311 = 1311;
	/** 物品使用已达上限*/
	public static final int BAG_1312 = 1312;
	
	/** 装备不存在*/
	public static final int EQUIP_1400 = 1400;
	/** 装备状态有误*/
	public static final int EQUIP_1401 = 1401;
	/** 装备职业不符*/
	public static final int EQUIP_1402 = 1402;
	/** 该部位已穿装备*/
	public static final int EQUIP_1403 = 1403;
	/** 装备已绑定*/
	public static final int EQUIP_1404 = 1404;
	/** 装备类型不符*/
	public static final int EQUIP_1405 = 1405;
	/** 装备孔位未解锁*/
	public static final int EQUIP_1406 = 1406;
	/** 该装备已达到强化最高级*/
	public static final int EQUIP_1407 = 1407;
	/** 未强化过的装备无法进行传承*/
	public static final int EQUIP_1408 = 1408;
	/** 不同装备部位无法进行传承*/
	public static final int EQUIP_1409 = 1409;
	/** 强化等级低于目标装备强化等级，无法传承*/
	public static final int EQUIP_1410 = 1410;
	/** 必须放入三件装备才能合成*/
	public static final int EQUIP_1411 = 1411;
	/** 必须放入三件相同装备才能合成*/
	public static final int EQUIP_1412 = 1412;
	
	/** 药品已经装备*/
	public static final int DRUG_1500 = 1500;
	/** 药品栏已经装备满*/
	public static final int DRUG_1501 = 1501;
	/** 药品已经卸下*/
	public static final int DRUG_1502 = 1502;
	
	/** 技能未学会*/
	public static final int SKILL_1600 = 1600;
	/** 技能已经满级  */
	public static final int SKILL_1601 = 1601;
	/** 技能已经学会*/
	public static final int SKILL_1602 = 1602;
	/** 技能熟练度不足*/
	public static final int SKILL_1603 = 1603;
	/** 魔法不足，无法释放技能*/
	public static final int SKILL_1604 = 1604;
	
	/** 时装不存在*/
	public static final int FASHION_1700 = 1700;
	/** 该时装已拥有*/
	public static final int FASHION_1701 = 1701;
	/** 该时装未拥有*/
	public static final int FASHION_1702 = 1702;
	/** 该时装已装备*/
	public static final int FASHION_1703 = 1703;
	/** 该时装已卸下*/
	public static final int FASHION_1704 = 1704;	
	
	/** 好友已达上限*/
	public static final int FRIEND_1800 = 1800;
	/** 对方好友已达上限*/
	public static final int FRIEND_1801 = 1801;
	/** 双方已是好友*/
	public static final int FRIEND_1802 = 1802;		
	/** 不能添加自己为好友  */
	public static final int FRIEND_1803 = 1803;
	/** 好友申请消息已达上限 */
	public static final int FRIEND_1804 = 1804;		
	/** 玩家拒绝好友申请 */
	public static final int FRIEND_1805 = 1805;	
	
	/** 邮件不存在*/
	public static final int MAIL_1900 = 1900;
	/** 该邮件已读取*/
	public static final int MAIL_1901 = 1901;
	/** 该邮件没有附件*/
	public static final int MAIL_1902 = 1902;
	/** 该邮件附件已经领取*/
	public static final int MAIL_1903 = 1903;
	/** 该邮件已经删除*/
	public static final int MAIL_1904 = 1904;
	/** 等级不足，无法使用该聊天功能 */
	public static final int CHAT_2000 = 2000;
	/** 好友等级不足，无法使用该聊天功能 */
	public static final int CHAT_2001 = 2001;
	/** 消息内容过长 */
	public static final int CHAT_2002 = 2002;
	/** 玩家尚未加入帮派 */
	public static final int CHAT_2003 = 2003;
	/** 玩家尚未加入队伍 */
	public static final int CHAT_2004 = 2004;
	/** 消息内容为空 */
	public static final int CHAT_2005 = 2005;
	/** 聊天道具不足 */
	public static final int CHAT_2006 = 2006;
	/** 玩家拒绝和陌生人聊天 */
	public static final int CHAT_2007 = 2007;
	/** 语音已过期*/
	public static final int CHAT_2008 = 2008;
	/** 对方不在线，不能给陌生人发送消息*/
	public static final int CHAT_2009 = 2009;
	
	/** 任务编号有误    */
	public static final int TASK_2100 = 2100;
	/** 任务没有完成，无法提交*/
	public static final int TASK_2101 = 2101;
	/** 任务已经完成*/
	public static final int TASK_2102 = 2102;	
	/** 今日任务次数已用完*/
	public static final int TASK_2103 = 2103;	
	/** 今日免费刷新次数已用完*/
	public static final int TASK_2104 = 2104;
	/** 今日猎妖次数已满*/
	public static final int TASK_2105 = 2105;
	/** 您身上已有猎妖任务*/
	public static final int TASK_2106 = 2106;
	/** 本周环任务次数已满*/
	public static final int TASK_2107 = 2107;
	/** 您身上已有环任务*/
	public static final int TASK_2108 = 2108;
	/** 您身上已有悬赏任务*/
	public static final int TASK_2109 = 2109;
	
	/**注灵道具不足 */
	public static final int WAKAN_2200 = 2200;
	/**注灵等级已达上限 */
	public static final int WAKAN_2201 = 2201;	
	
	/** 已经组队，无法创建队伍 */
	public static final int TEAM_2300 = 2300;
	/** 尚未创建队伍 */
	public static final int TEAM_2301 = 2301;
	/** 您不是队长，无法操作*/
	public static final int TEAM_2302 = 2302;
	/** 目标玩家已有队伍*/
	public static final int TEAM_2303 = 2303;
	/** 队伍信息错误*/
	public static final int TEAM_2304 = 2304;
	/** 你已经有队伍了*/
	public static final int TEAM_2305 = 2305;
	/** 队伍已经满员 */
	public static final int TEAM_2306 = 2306;
	/** 你已经是队长 */
	public static final int TEAM_2307 = 2307;
	/** 副本中，无法退出队伍 */
	public static final int TEAM_2308 = 2308;
	/** 副本中，无法踢出队员 */
	public static final int TEAM_2309 = 2309;
	/** 副本中，无法邀请 */
	public static final int TEAM_2310 = 2310;
	/** 副本中，无法创建或加入队伍 */
	public static final int TEAM_2311 = 2311;
	/** 组队状态下，不能匹配 */
	public static final int TEAM_2312 = 2312;
	
	/** 采集点信息过期*/
	public static final int COLLECT_2400 = 2400;
	/** 采集次数已达上限*/
	public static final int COLLECT_2401 = 2401;	
	/** 采集消耗体力不足*/
	public static final int COLLECT_2402 = 2402;	
	/** 采集读条时间不足*/
	public static final int COLLECT_2403 = 2403;
	/** 采集人数已达上限*/
	public static final int COLLECT_2404 = 2404;
	/** 当前不可做此采集任务*/
	public static final int COLLECT_2405 = 2405;
	/** 已在采集点采集*/
	public static final int COLLECT_2406 = 2406;
	/** 未在采集范围内*/
	public static final int COLLECT_2407 = 2407;
	/** 您的铁铲不足， 无法采集矿物*/
	public static final int COLLECT_2408 = 2408;

	/** 物品在交易行不可出售*/
	public static final int Trade_2500 = 2500;	
	/** 交易物品数已达上限*/
	public static final int Trade_2501 = 2501;	
	/** 交易价格不合理*/
	public static final int Trade_2502 = 2502;
	/** 交易物品与角色职业不符*/
	public static final int Trade_2503 = 2503;
	/** 交易物品需求角色等级不够*/
	public static final int Trade_2504 = 2504;
	/** 交易行物品品阶不可出售*/
	public static final int Trade_2505 = 2505;	
	/** 交易行不可购买自己出售的物品*/
	public static final int Trade_2506 = 2506;	
	/** 交易的物品数量不足*/
	public static final int Trade_2507 = 2507;	
	/** 交易的物品已经过期*/
	public static final int Trade_2508 = 2508;
	/** 交易货架已达上限*/
	public static final int Trade_2509 = 2509;	
	/** 交易物品已下架*/
	public static final int Trade_2510 = 2510;	
	/** 不能下架别人的商品*/
	public static final int Trade_2511 = 2511;	
	/** 交易物品已不在货架上*/
	public static final int Trade_2512 = 2512;	

	/** 族长不能退出家族*/
	public static final int FAMILY_2600 = 2600;	
	/** 玩家尚未创建家族*/
	public static final int FAMILY_2601 = 2601;
	/** 玩家家族内权限不足*/
	public static final int FAMILY_2602 = 2602;
	/** 玩家已有家族信息*/
	public static final int FAMILY_2603 = 2603;	
	/** 家族名字已存在*/
	public static final int FAMILY_2604 = 2604;		
	/** 家族成员已达上限*/
	public static final int FAMILY_2605 = 2605;		
	/** 家族信息不匹配*/
	public static final int FAMILY_2606 = 2606;		
	/** 族长位置不能切换*/
	public static final int FAMILY_2607 = 2607;
	/** 家族已不存在*/
	public static final int FAMILY_2608 = 2608;	
	/** 家族名字过长*/
	public static final int FAMILY_2609 = 2609;
	/** 创建家族成员不足*/
	public static final int FAMILY_2610 = 2610;	
	/** 不是好友，不能加入或创建家族*/
	public static final int FAMILY_2611 = 2611;		
	/** 家族排序位有误*/
	public static final int FAMILY_2612 = 2612;	
	/** 不是好友，不能邀请*/
	public static final int FAMILY_2613 = 2613;	
	/** 请先解散家族, 再删除角色*/
	public static final int FAMILY_2614 = 2614;
	/** 家族副本已经开启过*/
	public static final int FAMILY_2615 = 2615;
	/** 家族副本尚未开启*/
	public static final int FAMILY_2616 = 2616;
	/** 今日家族副本已经结束*/
	public static final int FAMILY_2617 = 2617;
	
	/** 商城商品已下架*/
	public static final int MARKET_2700 = 2700;
	/** 商品交易数量超过上限*/
	public static final int MARKET_2701 = 2701;
	/** 激活VIP, 可购买此商品*/
	public static final int MARKET_2702 = 2702;
	
	/***************************** 活动相关 ******************/
	/** 已签到过*/
	public static final int SIGN_2800 = 2800;	
	/** 活动未开始或已结束*/
	public static final int ACTIVITY_2801 = 2801;
	/** 签到奖励已领过*/
	public static final int SIGN_2802 = 2802;
	/** 签到天数不足*/
	public static final int SIGN_2803 = 2803;
	/** 该墓室已经探索*/
	public static final int ACTIVITY_2804 = 2804;
	/** 该陵墓已经探索完毕*/
	public static final int ACTIVITY_2805 = 2805;
	/** 奖励已领取*/
	public static final int ACTIVITY_2806 = 2806;
	/** 奖励基础数据不存在*/
	public static final int ACTIVITY_2807 = 2807;
	/** 领奖条件不足*/
	public static final int ACTIVITY_2808 = 2808;
	/** 转盘首次抽奖已使用*/
	public static final int ACTIVITY_2809 = 2809;
	/** 成长基金已购买*/
	public static final int ACTIVITY_2810 = 2810;
	/** 神器已购买*/
	public static final int ACTIVITY_2811 = 2811;
	/** 活动已结束*/
	public static final int ACTIVITY_2812 = 2812;	
	/** 领奖人数已达上限*/
	public static final int ACTIVITY_2813 = 2813;
	/** 激活码不存在*/
	public static final int ACTIVITY_2814 = 2814;
	/** 激活码已使用过*/
	public static final int ACTIVITY_2815 = 2815;
	/** 玩家已实名认证*/
	public static final int ACTIVITY_2816 = 2816;
	/** 真实姓名格式不正确*/
	public static final int ACTIVITY_2817 = 2817;
	/** 身份证格式不正确*/
	public static final int ACTIVITY_2818 = 2818;
	/** 身份证已被认证*/
	public static final int ACTIVITY_2819 = 2819;
	/** 玩家未进行实名认证*/
	public static final int ACTIVITY_2820 = 2820;
	
	/** 不能合成*/
	public static final int COMPOSE_2900 = 2900;
	/** 不能分解*/
	public static final int DECOMPOSE_2901 = 2901;	
	
	/** 在线时间不足*/
	public static final int ONLINE_3000 = 3000;
	/** 在线奖励已领*/
	public static final int ONLINE_3001 = 3001;
	
	/**　羽翼已装备*/
	public static final int WING_3100 = 3100;
	/**　羽翼未穿戴*/
	public static final int WING_3101 = 3101;
	/**　羽翼未获得*/
	public static final int WING_3102 = 3102;	
	/**　羽翼不存在*/
	public static final int WING_3103 = 3103;
	/**　羽翼已达到星级*/
	public static final int WING_3104 = 3104;
	/**　羽翼星级不足*/
	public static final int WING_3105 = 3105;

	
	/**　未找到仇家*/
	public static final int EMEMY_3200 = 3200;
	
	/** 激活对应VIP等级, 可领取奖励*/
	public static final int VIP_3300 = 3300;
	/** VIP等级不足*/
	public static final int VIP_3301 = 3301;
	/** 该VIP等级奖励已领过*/
	public static final int VIP_3302 = 3302;
	/** 每日福利已领过*/
	public static final int VIP_3303 = 3303;	
	/** 每日vip元宝已领过*/
	public static final int VIP_3304 = 3304;	
	
	/** 没有充值信息*/
	public static final int PAY_3400 = 3400;	
	/** 没有月卡或月卡过期*/
	public static final int PAY_3401 = 3401;
	/** 月卡奖励已领取*/
	public static final int PAY_3402 = 3402;
	/** 内网无法充值*/
	public static final int PAY_3403 = 3403;
	
	
	/** 验证码有误*/
	public static final int BIND_3500 = 3500;
	/** 手机号码有误*/
	public static final int BIND_3501 = 3501;
	/** 手机绑定奖励已领过*/
	public static final int BIND_3502 = 3502;
	/** 手机未绑定*/
	public static final int BIND_3503 = 3503;
	/** 获取验证码有误*/
	public static final int BIND_3504 = 3504;
	/** 该手机号已经绑定过其他账号*/
	public static final int BIND_3505 = 3505;
	
	/** 竞技场段位奖励已领过*/
	public static final int TAINTI_3600 = 3600;
	/** 已在竞技场匹配列表中*/
	public static final int TAINTI_3601 = 3601;
	/** 野外过于危险，请回主城进行匹配战斗！*/
	public static final int TAINTI_3602 = 3602;
	/** 活动时间在每天19:00-21:00*/
	public static final int TAINTI_3603 = 3603;
	
	/** 都护府名称不为空 */
	public static final int GUILD_3700 = 3700;
	/** 都护府名2-6个汉字或4-12个字符*/
	public static final int GUILD_3701 = 3701;
	/** 都护府名称包含敏感字或特殊字符 */
	public static final int GUILD_3702 = 3702;
	/** 都护府名称已存在*/
	public static final int GUILD_3703 = 3703;
	/** 您已加入其它都护府 */
	public static final int GUILD_3704 = 3704;
	/** 都护府公告不能为空 */
	public static final int GUILD_3705 = 3705;
	/** 都护府不存在  */
	public static final int GUILD_3706 = 3706;
	/** 您已申请过该都护府  */
	public static final int GUILD_3707 = 3707;
	/** 都护府人数已满  */
	public static final int GUILD_3708 = 3708;
	/** 您未加入都护府  */
	public static final int GUILD_3709 = 3709;
	/** 职务权限不足，无法操作  */
	public static final int GUILD_3710 = 3710;
	/** 该玩家已加入其它都护府  */
	public static final int GUILD_3711 = 3711;
	/** 都护府还有其他成员,都护不能退出  */
	public static final int GUILD_3712 = 3712;
	/** 您拥有继任票数，无法退出  */
	public static final int GUILD_3713 = 3713;
	/** 只有都护府长才能转让都护府长*/
	public static final int GUILD_3714 = 3714;
	/** 目标玩家必须加入都护府超过三天。*/
	public static final int GUILD_3715 = 3715;
	/** 不能转让给自己 */
	public static final int GUILD_3716 = 3716;
	/** 不能踢出自己  */
	public static final int GUILD_3717 = 3717;
	/** 对方拥有继任票数，无法踢出 */
	public static final int GUILD_3718 = 3718;
	/** 不能任免自己  */
	public static final int GUILD_3719 = 3719;
	/** 玩家职务没有变化*/
	public static final int GUILD_3720 = 3720;
	/** 该职务的人数已达上限*/
	public static final int GUILD_3721 = 3721;
	/** 请勿重复设置*/
	public static final int GUILD_3722 = 3722;
	/** 都护府已经是最高级*/
	public static final int GUILD_3723 = 3723;
	/** 都护府资金不足*/
	public static final int GUILD_3724 = 3724;
	/** 都护府建设度不足*/
	public static final int GUILD_3725 = 3725;
	/** 您先退出都护府，再删除角色*/
	public static final int GUILD_3726 = 3726;
	/** 公告字数超过100*/
	public static final int GUILD_3727 = 3727;
	/** 内容包含敏感字或特殊字符 */
	public static final int GUILD_3728 = 3728;
	/** 今日捐献次数已经上限 */
	public static final int GUILD_3729 = 3729;
	/** 都护府等级不足，请先提升都护府等级 */
	public static final int GUILD_3730 = 3730;
	/** 该技能尚未研发 */
	public static final int GUILD_3731 = 3731;
	/** 该技能研发等级不足 */
	public static final int GUILD_3732 = 3732;
	/** 该都护府已经跟您是宣战状态*/
	public static final int GUILD_3733 = 3733;
	/** 都护府人数未达到要求*/
	public static final int GUILD_3735 = 3735;
	/** 都护府已经报名*/
	public static final int GUILD_3736 = 3736;
	/** 守城都护府无需报名*/
	public static final int GUILD_3737 = 3737;
	/** 必须先报名都护府城战才能创建联盟*/
	public static final int GUILD_3738 = 3738;
	/** 您的都护府已经加入了一个联盟*/
	public static final int GUILD_3739 = 3739;
	/** 联盟名称不为空 */
	public static final int GUILD_3740 = 3740;
	/** 联盟名4-12个字符*/
	public static final int GUILD_3741 = 3741;
	/** 联盟名称包含敏感字或特殊字符 */
	public static final int GUILD_3742 = 3742;
	/** 联盟名称已存在*/
	public static final int GUILD_3743 = 3743;
	/** 联盟不存在*/
	public static final int GUILD_3744 = 3744;
	/** 您的都护府不是联盟盟主*/
	public static final int GUILD_3745 = 3745;
	/** 该都护府已经加入别的联盟了*/
	public static final int GUILD_3746 = 3746;
	/** 城战尚未开始或已结束*/
	public static final int GUILD_3747 = 3747;
	/** 该时间不允许该操作*/
	public static final int GUILD_3748 = 3748;
	/** 该联盟已经组满两个都护府*/
	public static final int GUILD_3749 = 3749;
	/** 城战防守方无需提交攻城令*/
	public static final int GUILD_3750 = 3750;
	/** 您的都护府尚未报名长安城战*/
	public static final int GUILD_3751 = 3751;
	/** 都护府活动中，无法进行该操作*/
	public static final int GUILD_3752 = 3752;
	/**必须由都护府都护来领取*/
	public static final int GUILD_3753 = 3753;
	/**您的都护府目前没有占领长安城*/
	public static final int GUILD_3754 = 3754;
	/**没有可领税收*/
	public static final int GUILD_3755 = 3755;
	/**您今天已经领过了*/
	public static final int GUILD_3756 = 3756;
	/**今天长安城的俸禄已经领光了*/
	public static final int GUILD_3757 = 3757;
	/**今天长安城没有俸禄可领*/
	public static final int GUILD_3758 = 3758;
	/**周一、周三、周五才可领取*/
	public static final int GUILD_3759 = 3759;
	/**请在长安城战结束之后再来*/
	public static final int GUILD_3760 = 3760;
	/** 凌烟阁已经开启*/
	public static final int GUILD_3761 = 3761;
	/** 凌烟阁尚未开启*/
	public static final int GUILD_3762 = 3762;
	/** 今日凌烟阁副本已经结束*/
	public static final int GUILD_3763 = 3763;
	/** 战前准备阶段只有守城方可以进入*/
	public static final int GUILD_3764 = 3764;
	/** 今日已达召唤次数上限*/
	public static final int GUILD_3765 = 3765;
	/** BOSS还没到顶峰形态，不能进行召唤*/
	public static final int GUILD_3766 = 3766;
	/** BOSS尚未死亡，不能再次进行召唤*/
	public static final int GUILD_3767 = 3767;
	/** 当前BOSS已达到顶峰状态，无需再喂养，可召唤*/
	public static final int GUILD_3768 = 3768;
	
	/** 碎片不足 */
	public static final int FURNACE_3800 = 3800;
	/** 已达最高星阶 */
	public static final int FURNACE_3801 = 3801;
}
