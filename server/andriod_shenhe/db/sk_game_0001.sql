/*
Navicat MySQL Data Transfer

Source Server         : 安卓审核服106.15.180.133
Source Server Version : 50173
Source Host           : 106.15.180.133:3306
Source Database       : sk_game_0001

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-10-20 14:46:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for family
-- ----------------------------
DROP TABLE IF EXISTS `family`;
CREATE TABLE `family` (
  `playerFamilyId` bigint(20) NOT NULL DEFAULT '0' COMMENT '家族唯一ID',
  `familyName` varchar(50) NOT NULL COMMENT '家族名称',
  `familyNotice` varchar(500) DEFAULT NULL COMMENT '家族宣言',
  `familyCreateTime` bigint(11) NOT NULL DEFAULT '0' COMMENT '家族创建时间',
  `familyDisbandTime` bigint(11) NOT NULL DEFAULT '0' COMMENT '家族解散倒计时',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`playerFamilyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='家族信息表';

-- ----------------------------
-- Table structure for mail_inbox
-- ----------------------------
DROP TABLE IF EXISTS `mail_inbox`;
CREATE TABLE `mail_inbox` (
  `mailInboxID` bigint(20) NOT NULL DEFAULT '0' COMMENT '邮件唯一编号',
  `mailType` tinyint(2) NOT NULL COMMENT '邮件类型(1:系统2:玩家)',
  `senderID` bigint(20) DEFAULT NULL COMMENT '发送者编号',
  `senderName` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '发送玩家名称',
  `receiverID` bigint(20) NOT NULL COMMENT '接收者编号',
  `theme` varchar(128) NOT NULL COMMENT '主题',
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '邮件内容',
  `haveAttachment` tinyint(2) NOT NULL COMMENT '是否含有附件(0:否1:是)',
  `haveReceiveAttachment` tinyint(2) NOT NULL COMMENT '是否领取附件(0:否1:是)',
  `attachment` varchar(1024) DEFAULT NULL,
  `state` tinyint(2) NOT NULL COMMENT '状态(0:未读1:已读2:保存)',
  `receiveTime` datetime NOT NULL COMMENT '邮件接收时间',
  `remainDays` tinyint(4) NOT NULL COMMENT '剩余天数',
  `fromType` int(11) DEFAULT NULL,
  `deleteFlag` tinyint(2) NOT NULL COMMENT '是否已删除(1.已删 0.未删)',
  `deleteTime` datetime DEFAULT NULL,
  PRIMARY KEY (`mailInboxID`),
  KEY `remainDays` (`remainDays`) USING BTREE,
  KEY `receiverID` (`receiverID`,`receiveTime`) USING BTREE,
  KEY `deleteFlag` (`deleteFlag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邮件记录表';

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `playerName` varchar(50) NOT NULL COMMENT '玩家昵称',
  `userId` int(11) NOT NULL COMMENT '账号编号',
  `site` varchar(50) NOT NULL COMMENT '游戏站点',
  `serverNo` int(6) NOT NULL DEFAULT '1' COMMENT '游戏编号',
  `guid` varchar(100) NOT NULL DEFAULT '' COMMENT '全局唯一标示',
  `career` smallint(2) NOT NULL DEFAULT '0' COMMENT '职业',
  `type` smallint(2) NOT NULL DEFAULT '1' COMMENT '1:玩家 2.GM 3.引导员 4.内部账号 5.机器人 6.封停',
  `telePhone` bigint(20) NOT NULL DEFAULT '0' COMMENT '绑定手机号码',
  `createTime` datetime NOT NULL COMMENT '创号时间',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`playerId`),
  UNIQUE KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家角色信息表';

-- ----------------------------
-- Table structure for player_bag
-- ----------------------------
DROP TABLE IF EXISTS `player_bag`;
CREATE TABLE `player_bag` (
  `playerBagId` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL,
  `goodsType` tinyint(2) NOT NULL DEFAULT '1' COMMENT '物品类别',
  `itemId` bigint(20) NOT NULL COMMENT '道具编号',
  `itemIndex` smallint(6) NOT NULL COMMENT '物品索引',
  `num` smallint(5) NOT NULL COMMENT '数量',
  `isBinding` tinyint(2) NOT NULL COMMENT '是否绑定',
  `state` tinyint(2) NOT NULL COMMENT '状态(0: 已删除  1:背包 2:穿戴 )',
  PRIMARY KEY (`playerBagId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家背包';

-- ----------------------------
-- Table structure for player_daily
-- ----------------------------
DROP TABLE IF EXISTS `player_daily`;
CREATE TABLE `player_daily` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `everyOnlineTime` int(11) NOT NULL DEFAULT '0' COMMENT '每日在线时长 秒',
  `rewardIdStr` varchar(255) DEFAULT NULL COMMENT '在线奖励',
  `dailyTaskNum` tinyint(2) NOT NULL DEFAULT '0' COMMENT '已接取每日任务次数',
  `dailyRefNum` tinyint(2) NOT NULL DEFAULT '0' COMMENT '每日任务免费刷新次数',
  `huntTaskNum` tinyint(2) NOT NULL DEFAULT '0' COMMENT '已接取猎妖任务次数',
  `instanceNum` smallint(4) NOT NULL DEFAULT '0' COMMENT '每日进入副本次数',
  `dailyRewardState` tinyint(2) NOT NULL DEFAULT '0' COMMENT '每日福利奖励领取状态',
  `monthCardAwardState` tinyint(2) NOT NULL DEFAULT '0' COMMENT '月卡奖励领取状态',
  `monthCardVaildTime` bigint(11) NOT NULL DEFAULT '0' COMMENT '月卡到期时间',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家每日数据';

-- ----------------------------
-- Table structure for player_drug
-- ----------------------------
DROP TABLE IF EXISTS `player_drug`;
CREATE TABLE `player_drug` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1：红药栏  2：蓝药栏',
  `itemIndex` tinyint(2) NOT NULL COMMENT '栏下标',
  `itemId` int(11) NOT NULL COMMENT '道具编号',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家药品栏';

-- ----------------------------
-- Table structure for player_enemy
-- ----------------------------
DROP TABLE IF EXISTS `player_enemy`;
CREATE TABLE `player_enemy` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '自增编号',
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `enemyPlayerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家仇敌编号',
  `addTime` bigint(11) NOT NULL DEFAULT '0' COMMENT '添加时间',
  `deleteFlag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家仇敌表';

-- ----------------------------
-- Table structure for player_equipment
-- ----------------------------
DROP TABLE IF EXISTS `player_equipment`;
CREATE TABLE `player_equipment` (
  `playerEquipmentId` bigint(20) NOT NULL DEFAULT '0' COMMENT '装备唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `equipmentId` int(11) NOT NULL COMMENT '装备编号',
  `equipType` tinyint(2) NOT NULL DEFAULT '1' COMMENT '装备部位',
  `isBinding` tinyint(2) NOT NULL COMMENT '是否绑定',
  `state` tinyint(2) NOT NULL COMMENT '状态(0: 已删除  1:背包 2:穿戴 )',
  `deleteTime` datetime DEFAULT NULL COMMENT '删除时间',
  `holeNum` int(11) NOT NULL DEFAULT '0' COMMENT '装备孔位数',
  `addAttr` varchar(500) DEFAULT NULL COMMENT '附加属性值',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '评分',
  PRIMARY KEY (`playerEquipmentId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家装备';

-- ----------------------------
-- Table structure for player_ext
-- ----------------------------
DROP TABLE IF EXISTS `player_ext`;
CREATE TABLE `player_ext` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `loginTime` datetime DEFAULT NULL COMMENT '最近登录时间',
  `exitTime` datetime DEFAULT NULL COMMENT '最近离线时间',
  `onLine` smallint(2) NOT NULL DEFAULT '1' COMMENT '是否在线',
  `weaponStyle` int(11) NOT NULL DEFAULT '0' COMMENT '武器样式',
  `dressStyle` int(11) NOT NULL DEFAULT '0' COMMENT '服装样式',
  `wingStyle` int(11) NOT NULL DEFAULT '0' COMMENT '羽翼外形',
  `line` int(2) NOT NULL DEFAULT '1' COMMENT '线路',
  `mapId` int(11) NOT NULL DEFAULT '1001' COMMENT '所在地图',
  `x` int(8) NOT NULL DEFAULT '0' COMMENT 'x坐标',
  `y` int(8) NOT NULL DEFAULT '0' COMMENT 'y坐标',
  `z` int(8) NOT NULL DEFAULT '0' COMMENT 'z坐标',
  `lastMapId` int(8) NOT NULL DEFAULT '0',
  `lastX` int(8) NOT NULL DEFAULT '0',
  `lastY` int(8) NOT NULL DEFAULT '0',
  `lastZ` int(8) NOT NULL DEFAULT '0',
  `direction` int(8) NOT NULL DEFAULT '0' COMMENT '方向',
  `hp` int(11) NOT NULL DEFAULT '0' COMMENT '当前生命',
  `mp` int(11) NOT NULL DEFAULT '0' COMMENT '当前魔法',
  `bagGrid` smallint(4) NOT NULL DEFAULT '0' COMMENT '背包格子数',
  `pkMode` tinyint(2) NOT NULL DEFAULT '1' COMMENT 'pk模式',
  `pkVlaue` int(11) NOT NULL DEFAULT '0' COMMENT ' pk值',
  `teamId` int(11) NOT NULL DEFAULT '0' COMMENT '组队编号',
  `tradeGridNum` tinyint(2) NOT NULL DEFAULT '8' COMMENT '玩家装备货架上限',
  `curLayerId` smallint(4) NOT NULL DEFAULT '1' COMMENT '大荒塔当前层',
  `weekTaskNum` smallint(4) NOT NULL DEFAULT '0' COMMENT '已接环任务次数',
  `weekTotalNum` int(4) NOT NULL DEFAULT '0' COMMENT '本周已接环任务次数',
  `monthCardVaildTime` bigint(11) DEFAULT NULL COMMENT '月卡到期时间',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家扩展信息';

-- ----------------------------
-- Table structure for player_family
-- ----------------------------
DROP TABLE IF EXISTS `player_family`;
CREATE TABLE `player_family` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `playerFamilyId` bigint(20) NOT NULL DEFAULT '0' COMMENT '家族唯一编号',
  `familyPosId` tinyint(2) NOT NULL DEFAULT '0' COMMENT '角色家族职位',
  `familySortId` int(1) NOT NULL DEFAULT '0' COMMENT '角色家族排序位',
  `familyTitle` varchar(50) DEFAULT NULL COMMENT '角色家族称谓',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家家族信息表';

-- ----------------------------
-- Table structure for player_fashion
-- ----------------------------
DROP TABLE IF EXISTS `player_fashion`;
CREATE TABLE `player_fashion` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `fashionId` int(11) NOT NULL COMMENT ' 时装编号',
  `dressFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否装备',
  `ownDate` bigint(20) DEFAULT NULL COMMENT '拥有日期',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家时装翅膀';

-- ----------------------------
-- Table structure for player_friend
-- ----------------------------
DROP TABLE IF EXISTS `player_friend`;
CREATE TABLE `player_friend` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '自增标号',
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `friendPlayerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家好友编号',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '玩家好友类型',
  `deleteFlag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家好友表';

-- ----------------------------
-- Table structure for player_instance
-- ----------------------------
DROP TABLE IF EXISTS `player_instance`;
CREATE TABLE `player_instance` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `mapId` int(11) NOT NULL COMMENT '地图编号',
  `enterCount` tinyint(2) NOT NULL DEFAULT '0' COMMENT '进入次数',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家副本';

-- ----------------------------
-- Table structure for player_market
-- ----------------------------
DROP TABLE IF EXISTS `player_market`;
CREATE TABLE `player_market` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '自增编号',
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `marketId` int(11) NOT NULL DEFAULT '0' COMMENT '商城物品编号',
  `curBuyNum` int(11) NOT NULL DEFAULT '0' COMMENT '当前购买次数',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家商城交易信息';

-- ----------------------------
-- Table structure for player_optional
-- ----------------------------
DROP TABLE IF EXISTS `player_optional`;
CREATE TABLE `player_optional` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `fristPayIdStr` varchar(100) DEFAULT NULL COMMENT '玩家首冲id子串',
  `bindRewardState` tinyint(2) NOT NULL DEFAULT '0' COMMENT '手机绑定状态',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家操作表';

-- ----------------------------
-- Table structure for player_property
-- ----------------------------
DROP TABLE IF EXISTS `player_property`;
CREATE TABLE `player_property` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `level` smallint(4) NOT NULL DEFAULT '1' COMMENT '角色等级',
  `exp` int(11) NOT NULL DEFAULT '0' COMMENT '角色当前经验',
  `battleValue` int(11) NOT NULL DEFAULT '0' COMMENT '玩家战斗力',
  `skillLv` smallint(4) NOT NULL DEFAULT '0' COMMENT '技能等级之和',
  `strength` int(11) NOT NULL DEFAULT '0' COMMENT '力量',
  `intelligence` int(11) NOT NULL DEFAULT '0' COMMENT '智慧',
  `endurance` int(11) NOT NULL DEFAULT '0' COMMENT '耐力',
  `spirit` int(11) NOT NULL DEFAULT '0',
  `lucky` int(11) NOT NULL DEFAULT '0' COMMENT '幸运',
  `hpMax` int(11) NOT NULL DEFAULT '0' COMMENT '血量最大值',
  `mpMax` int(11) NOT NULL DEFAULT '0' COMMENT '魔法最大值',
  `p_attack` int(11) NOT NULL DEFAULT '0' COMMENT '物理攻击',
  `m_attack` int(11) NOT NULL DEFAULT '0' COMMENT '魔法攻击',
  `p_damage` int(11) NOT NULL DEFAULT '0' COMMENT '物理防御',
  `m_damage` int(11) NOT NULL DEFAULT '0' COMMENT '魔法防御',
  `crit` int(11) NOT NULL DEFAULT '0' COMMENT '暴击',
  `tough` int(11) NOT NULL DEFAULT '0' COMMENT '韧性',
  `dmgDeepPer` int(11) NOT NULL DEFAULT '0' COMMENT '伤害加深',
  `dmgReductPer` int(11) NOT NULL DEFAULT '0' COMMENT '伤害减免',
  `dmgCritPer` int(11) NOT NULL DEFAULT '0' COMMENT '伤害暴击',
  `moveSpeed` int(11) NOT NULL DEFAULT '0' COMMENT '移动速度',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家属性信息';

-- ----------------------------
-- Table structure for player_sign
-- ----------------------------
DROP TABLE IF EXISTS `player_sign`;
CREATE TABLE `player_sign` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `signNum` tinyint(2) NOT NULL DEFAULT '0' COMMENT '签到次数',
  `reSignNum` tinyint(2) NOT NULL DEFAULT '0' COMMENT '已补签次数',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '签到状态',
  `conSignDay` tinyint(2) NOT NULL DEFAULT '0' COMMENT '连续签到次数',
  `conSignRewardStr` varchar(100) DEFAULT NULL COMMENT '已领取连续签到奖励列表',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家签到数据';

-- ----------------------------
-- Table structure for player_skill
-- ----------------------------
DROP TABLE IF EXISTS `player_skill`;
CREATE TABLE `player_skill` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `skillId` int(11) NOT NULL COMMENT '技能编号',
  `level` smallint(4) NOT NULL DEFAULT '1' COMMENT '技能等级',
  `mastery` int(11) NOT NULL DEFAULT '0' COMMENT '熟练度',
  `skillIndex` int(11) NOT NULL DEFAULT '0' COMMENT '技能类型ID',
  `mwSkillId` int(11) NOT NULL DEFAULT '0' COMMENT '铭文技能ID',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家技能';

-- ----------------------------
-- Table structure for player_task
-- ----------------------------
DROP TABLE IF EXISTS `player_task`;
CREATE TABLE `player_task` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `taskId` int(11) NOT NULL COMMENT '技能编号',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '任务类型',
  `conditionType` smallint(4) NOT NULL DEFAULT '0' COMMENT '条件类型',
  `currentNum` smallint(4) NOT NULL DEFAULT '0' COMMENT '当前任务完成值',
  `taskState` tinyint(2) NOT NULL DEFAULT '0' COMMENT '任务状态（0：未完成  1：完成）',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已删除(1.已删 0.未删)',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家任务';

-- ----------------------------
-- Table structure for player_tianti
-- ----------------------------
DROP TABLE IF EXISTS `player_tianti`;
CREATE TABLE `player_tianti` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `rank` smallint(4) NOT NULL DEFAULT '0' COMMENT '排名',
  `killNum` int(11) NOT NULL DEFAULT '0' COMMENT '击杀数',
  `deadNum` int(11) NOT NULL DEFAULT '0' COMMENT '死亡数',
  `stage` tinyint(2) NOT NULL DEFAULT '0' COMMENT '段位',
  `star` smallint(6) NOT NULL DEFAULT '0' COMMENT '星级',
  `score` smallint(4) NOT NULL DEFAULT '0' COMMENT '积分',
  `updateTime` bigint(64) NOT NULL DEFAULT '0' COMMENT '变动时间',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家天梯记录';

-- ----------------------------
-- Table structure for player_trade_bag
-- ----------------------------
DROP TABLE IF EXISTS `player_trade_bag`;
CREATE TABLE `player_trade_bag` (
  `playerTradeBagId` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易背包唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `goodsType` tinyint(2) NOT NULL DEFAULT '1' COMMENT '物品类别',
  `itemId` bigint(20) NOT NULL COMMENT '道具编号',
  `num` smallint(5) NOT NULL COMMENT '数量',
  `isBinding` tinyint(2) NOT NULL COMMENT '是否绑定',
  `price` int(11) NOT NULL DEFAULT '0' COMMENT '售价',
  `state` tinyint(2) NOT NULL COMMENT '状态(0:空置 1:背包)',
  `overTime` bigint(11) NOT NULL DEFAULT '0' COMMENT '交易过期时间',
  PRIMARY KEY (`playerTradeBagId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家交易行背包';

-- ----------------------------
-- Table structure for player_vip
-- ----------------------------
DROP TABLE IF EXISTS `player_vip`;
CREATE TABLE `player_vip` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `level` tinyint(2) NOT NULL DEFAULT '0' COMMENT '  玩家vip等级',
  `vaildTime` bigint(11) NOT NULL DEFAULT '0' COMMENT 'vip过期时间',
  `vipLv1State` tinyint(2) NOT NULL DEFAULT '0' COMMENT 'VIP激活奖励领取状态 (0:未领取, 1:已领取)',
  `vipLv2State` tinyint(2) NOT NULL DEFAULT '0' COMMENT 'VIP激活奖励领取状态 (0:未领取, 1:已领取)',
  `vipLv3State` tinyint(2) NOT NULL DEFAULT '0' COMMENT 'VIP激活奖励领取状态 (0:未领取, 1:已领取)',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家vip表';

-- ----------------------------
-- Table structure for player_wakan
-- ----------------------------
DROP TABLE IF EXISTS `player_wakan`;
CREATE TABLE `player_wakan` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '注灵唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `posId` int(11) NOT NULL COMMENT '装备位ID',
  `wakanLevel` smallint(4) NOT NULL COMMENT '注灵等级',
  `wakanValue` int(1) NOT NULL COMMENT '灵力值',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家注灵表';

-- ----------------------------
-- Table structure for player_wealth
-- ----------------------------
DROP TABLE IF EXISTS `player_wealth`;
CREATE TABLE `player_wealth` (
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `deleteFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `gold` int(11) NOT NULL DEFAULT '0' COMMENT '玩家金币',
  `diamond` int(11) NOT NULL DEFAULT '0' COMMENT '钻石',
  `stone` int(11) NOT NULL DEFAULT '0' COMMENT '宝玉',
  PRIMARY KEY (`playerId`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家财富数据';

-- ----------------------------
-- Table structure for player_weapon_effect
-- ----------------------------
DROP TABLE IF EXISTS `player_weapon_effect`;
CREATE TABLE `player_weapon_effect` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '唯一编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `holdId` smallint(4) NOT NULL DEFAULT '1' COMMENT '孔位ID',
  `effectId` int(11) NOT NULL COMMENT '附加效果编号ID',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1:技能  2：属性',
  `baseId` int(11) NOT NULL DEFAULT '0' COMMENT '技能基础ID',
  `proValue` int(11) NOT NULL DEFAULT '0' COMMENT '属性值',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家铭文效果';

-- ----------------------------
-- Table structure for player_wing
-- ----------------------------
DROP TABLE IF EXISTS `player_wing`;
CREATE TABLE `player_wing` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '自增编号',
  `playerId` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家编号',
  `wingId` int(11) NOT NULL DEFAULT '0' COMMENT ' 翅膀编号',
  `star` smallint(4) NOT NULL DEFAULT '0' COMMENT '翅膀星级',
  `wingValue` int(6) NOT NULL DEFAULT '0' COMMENT '羽灵值',
  `wingScore` int(6) NOT NULL DEFAULT '0' COMMENT '翅膀评分',
  `dressFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '装备标识 1: 已装备',
  PRIMARY KEY (`id`),
  KEY `playerId` (`playerId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家羽翼表';

-- ----------------------------
-- Procedure structure for sp_user_pay
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_user_pay`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_user_pay`(in i_playerID int,
  in i_userName  varchar(30),  in i_playerName  varchar(30),  in i_playerLevel int,
   in i_orderID varchar(50),
   in i_money decimal(13,2),
   in i_moneytype varchar(10),
 in i_diamond int,  in i_payIP varchar(15),
   in i_site varchar(30),
   out o_status int)
    COMMENT '充值'
label1:begin

   DECLARE EXIT HANDLER FOR SQLWARNING 
   begin
      SET o_status=11;
      rollback;
   end;
   
   DECLARE EXIT HANDLER FOR SQLEXCEPTION
   begin
      SET o_status=12;
      rollback;
   end;
   
   set o_status = -1;
   
   if not exists(select 1 from t_player where PLAYER_ID = i_playerID) then
      set o_status = 1;
      leave label1;
   end if;
   
   if exists(select 1 from t_pay_log where ORDER_NO = i_orderID and PAY_SITE = i_site) then
      set o_status = 2;
      leave label1;
   end if;
   
   start transaction;
   
   insert into t_pay_log(`PLAYER_ID`,`USER_NAME`,`PLAYER_NAME`,`PLAYER_LEVEL`,`ORDER_NO`,`MONEY`,`MONEY_TYPE`,`DIAMOND`,`PAY_IP`,`PAY_SITE`,`CREATE_TIME`)
   select i_playerID `PLAYER_ID`,
				i_userName `USER_NAME`,
				i_playerName `PLAYER_NAME`,
        i_playerLevel `PLAYER_LEVEL`,
        i_orderID `ORDER_NO`,
        i_money `MONEY`,
        i_moneytype `MONEY_TYPE`,
        i_diamond `DIAMOND`,
        i_payIP `PAY_IP`,
        i_site `PAY_SITE`,
        now() `CREATE_TIME`;
   
   COMMIT;
   set o_status = 0;
end label1
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_user_player
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_user_player`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_user_player`(in i_playerId bigint,in i_userId int,in i_site varchar(20),in i_serverNo int,in i_player_name varchar(50),in i_career int,in i_telephone bigint,out o_status int)
label1:begin
   
   declare v_player_id bigint DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLWARNING 
   begin
      SET o_status=11;
      rollback;
   end;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
   begin
      SET o_status=12;
      rollback;
   end;
   
   set o_status = -1;

   if exists(select 1 from player where playerName = i_player_name LIMIT 1) then
      set o_status = 2;
      leave label1; 
   end if;
   
   start transaction;


   INSERT INTO player(`playerId`,`userId`,`site`,`serverNo`,`guid`, `playerName`,`career`,`telePhone`,`createTime`)
   select i_playerId `playerId`,
				i_userId `userId`,
        i_site `site`,
        i_serverNo `serverNo`,
				'1' `guid`,
        i_player_name `playerName`,
        i_career `career`,
        i_telephone `telePhone`,
        now() `createTime`;

   
   set v_player_id = i_playerId;

   if v_player_id = 0 then
      ROLLBACK;
      set o_status = 3;
      leave label1;
   end if;

	 update player set guid = CONCAT(site,'_2_',playerId) WHERE playerId = v_player_id;
	
 INSERT INTO player_ext(`playerId`,`loginTime`,`exitTime`)
	SELECT v_player_id `playerId`,
	now() `loginTime`,
	now() `exitTime`;

 INSERT INTO player_optional(`playerId`)
	SELECT v_player_id `playerId`;

 INSERT INTO player_property(`playerId`)
	SELECT v_player_id `playerId`;

 INSERT INTO player_daily(`playerId`)
	SELECT v_player_id `playerId`;

 INSERT INTO player_wealth(`playerId`)
	SELECT v_player_id `playerId`;

 INSERT INTO player_family(`playerId`)
	SELECT v_player_id `playerId`;

 INSERT INTO player_vip(`playerId`)
	SELECT v_player_id `playerId`;

 INSERT INTO player_tianti(`playerId`,`stage`,`score`)
	SELECT v_player_id `playerId`,
	1 `stage`,
	79 `score`;

   COMMIT;
   set o_status = 0;
end label1
;;
DELIMITER ;
