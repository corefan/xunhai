/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50171
Source Host           : localhost:3306
Source Database       : xh_gcc

Target Server Type    : MYSQL
Target Server Version : 50171
File Encoding         : 65001

Date: 2018-08-31 14:29:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(64) NOT NULL COMMENT '账号',
  `passWord` varchar(64) NOT NULL DEFAULT '123456' COMMENT '密码',
  `telephone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `tourist` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否游客  1：是 ',
  `serverListStr` varchar(255) DEFAULT NULL COMMENT '已创号的服务器列表',
  `realName` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `identity` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `createTime` datetime NOT NULL COMMENT '充值时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1137489973250 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='账号表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'a08762a4af5d29ab968ae8e9fb377669f15641e5', '123456', null, '1', '[1]', null, null, '2018-03-20 19:08:11', null);
INSERT INTO `account` VALUES ('2', '1cf74b02d8d3fb8a4ec6374af633b369ecb58f13', '123456', null, '1', '[2, 1]', null, null, '2018-03-20 19:36:45', null);
INSERT INTO `account` VALUES ('3', '15916222521', '123456', null, '0', '[1]', null, null, '2018-03-26 15:48:40', null);
INSERT INTO `account` VALUES ('4', '15916222522', '123456', null, '0', '[1]', null, null, '2018-03-26 20:21:42', null);
INSERT INTO `account` VALUES ('5', '15916222524', '123456', null, '0', '[1]', null, null, '2018-03-27 17:40:41', null);
INSERT INTO `account` VALUES ('6', '15916222526', '123456', null, '0', '[1]', null, null, '2018-03-28 15:19:56', null);
INSERT INTO `account` VALUES ('7', '15916222527', '123456', null, '0', '[1]', null, null, '2018-03-28 15:25:58', null);
INSERT INTO `account` VALUES ('8', '15916222529', '123456', null, '0', '[1]', null, null, '2018-03-29 16:27:07', null);
INSERT INTO `account` VALUES ('9', '99d95b78844c48f10c789c2f6449f6fc6fbfbdaf', '123456', null, '1', '[1]', null, null, '2018-04-13 12:09:40', null);
INSERT INTO `account` VALUES ('10', '602208c2eac6e5ad45f650d1c57dcdad8a6f337f', '123456', null, '1', '[1]', null, null, '2018-04-19 20:05:51', null);
INSERT INTO `account` VALUES ('11', '15916222530', '123456', null, '0', '[1]', null, null, '2018-04-19 20:34:41', null);
INSERT INTO `account` VALUES ('12', 'ae58f577a51d0aaf242b539ec4d258bb131f872a', '123456', null, '1', '[1]', null, null, '2018-04-23 11:15:31', null);
INSERT INTO `account` VALUES ('13', '13322223333', '123456', null, '0', '[1]', null, null, '2018-04-24 20:36:10', null);
INSERT INTO `account` VALUES ('14', '13488881111', '123456', null, '0', '[1]', null, null, '2018-04-24 20:36:59', null);
INSERT INTO `account` VALUES ('15', '18811112222', '123456', null, '0', '[1]', null, null, '2018-05-02 10:49:44', null);
INSERT INTO `account` VALUES ('16', '18811113333', '123456', null, '0', '[1]', null, null, '2018-05-02 17:39:04', null);
INSERT INTO `account` VALUES ('17', '18833334444', '123456', null, '0', '[1]', null, null, '2018-05-04 18:14:28', null);
INSERT INTO `account` VALUES ('18', '13311112222', '123456', null, '0', '[1]', null, null, '2018-05-05 10:17:40', null);
INSERT INTO `account` VALUES ('19', 'e7b3ce96d60bc1424d27f064442800126ba98024', '123456', null, '1', '[1]', null, null, '2018-06-26 10:36:58', null);
INSERT INTO `account` VALUES ('20', '13545678911', '111111', null, '0', '[1]', null, null, '2018-06-26 17:59:25', null);
INSERT INTO `account` VALUES ('21', '13511223344', '111111', null, '0', '[1]', null, null, '2018-06-28 16:08:29', null);
INSERT INTO `account` VALUES ('22', '13545678912', '123456', null, '0', '[1]', null, null, '2018-07-10 15:26:01', null);
INSERT INTO `account` VALUES ('23', '13545678913', '123456', null, '0', '[1]', null, null, '2018-07-12 14:24:57', null);
INSERT INTO `account` VALUES ('24', 'b5521ce6473e95024fbc5fa03e930acdb40d96f4', '123456', null, '1', null, null, null, '2018-07-16 10:35:11', null);
INSERT INTO `account` VALUES ('25', '13111122220', '123456', null, '0', '[1]', null, null, '2018-07-16 10:46:30', null);
INSERT INTO `account` VALUES ('26', '13500000000', '123456', null, '0', '[1]', null, null, '2018-07-18 15:10:31', null);
INSERT INTO `account` VALUES ('27', '13588881111', '123456', null, '0', '[1]', null, null, '2018-07-18 18:56:42', null);
INSERT INTO `account` VALUES ('28', '18011112222', '123456', null, '0', '[1]', null, null, '2018-07-27 10:05:20', null);
INSERT INTO `account` VALUES ('29', '13500000001', '123456', null, '0', '[1]', null, null, '2018-08-09 18:14:12', null);
INSERT INTO `account` VALUES ('30', '18011113333', '123456', null, '0', '[1]', null, null, '2018-08-11 10:33:44', null);
INSERT INTO `account` VALUES ('1137489973249', '15916222520', '123456', null, '0', '[1]', null, null, '2018-08-23 15:30:59', null);

-- ----------------------------
-- Table structure for actcode
-- ----------------------------
DROP TABLE IF EXISTS `actcode`;
CREATE TABLE `actcode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '激活码编号',
  `code` varchar(64) NOT NULL COMMENT '激活码',
  `agent` varchar(16) NOT NULL COMMENT '运营商',
  `type` int(4) NOT NULL COMMENT '奖励类型',
  `rewardId` int(4) NOT NULL COMMENT '奖励编号',
  `exclusive` tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否专属运营商',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已使用(0.未使用 1.已使用)',
  `createTime` datetime NOT NULL,
  `useTime` datetime DEFAULT NULL COMMENT '使用时间',
  `userName` varchar(64) DEFAULT NULL COMMENT '使用者账号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1100308025355 DEFAULT CHARSET=utf8 COMMENT='激活码表';

-- ----------------------------
-- Records of actcode
-- ----------------------------
INSERT INTO `actcode` VALUES ('1100308025345', '26c659b80e8e', 'xh', '20', '2001', '1', '1', '2018-08-10 12:19:15', '2018-08-10 15:33:22', '15916222530');
INSERT INTO `actcode` VALUES ('1100308025346', '0c138fc70ba3', 'xh', '20', '2001', '1', '1', '2018-08-10 12:19:15', '2018-08-10 15:35:23', '15916222530');
INSERT INTO `actcode` VALUES ('1100308025347', 'c22b6013a0bc', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025348', 'f8821dd276d4', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025349', 'e1f480fa4778', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025350', '874089da124f', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025351', '345f34358ec8', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025352', 'fdbd38853fb2', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025353', '22dbab2f086e', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);
INSERT INTO `actcode` VALUES ('1100308025354', '2c11e8f0d3ee', 'xh', '20', '2001', '1', '0', '2018-08-10 12:19:15', null, null);

-- ----------------------------
-- Table structure for config_agent
-- ----------------------------
DROP TABLE IF EXISTS `config_agent`;
CREATE TABLE `config_agent` (
  `agent` varchar(50) NOT NULL COMMENT '代理商标识,和另外一张表关联',
  `loginKey` varchar(100) NOT NULL,
  `chargeKey` varchar(100) NOT NULL,
  `defaultLoginKey` varchar(50) DEFAULT NULL,
  `country` varchar(50) NOT NULL COMMENT '国家',
  `language` varchar(50) NOT NULL COMMENT '语言',
  `chargeIP` varchar(512) DEFAULT NULL COMMENT '充值IP列表',
  `nettyVersion` tinyint(5) DEFAULT '3',
  `actCodeUrl` varchar(64) NOT NULL COMMENT '激活码服url',
  `payUrl` varchar(64) NOT NULL COMMENT '支付地址',
  `accountUrl` varchar(64) NOT NULL COMMENT '登录服地址',
  `fcmSwitch` tinyint(1) NOT NULL DEFAULT '0' COMMENT '防沉迷开关',
  `testSwitch` tinyint(1) NOT NULL DEFAULT '0' COMMENT '内网开关',
  PRIMARY KEY (`agent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='平台配置';

-- ----------------------------
-- Records of config_agent
-- ----------------------------
INSERT INTO `config_agent` VALUES ('xh', 'QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777', 'QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2', 'DGAMEASDWDSHEN-16TQASDEDE-W33TT', 'CN', 'zh', '*', '4', 'http://192.168.0.200:9800/code', 'http://192.168.0.200:9000/pay', 'http://192.168.0.200:8000', '0', '1');

-- ----------------------------
-- Table structure for config_server
-- ----------------------------
DROP TABLE IF EXISTS `config_server`;
CREATE TABLE `config_server` (
  `serverNo` int(6) NOT NULL COMMENT '服务器编号',
  `serverName` varchar(32) NOT NULL COMMENT '服务器名称',
  `agent` varchar(32) NOT NULL COMMENT '代理商',
  `gameSite` varchar(128) NOT NULL COMMENT '游戏站点',
  `gameHost` varchar(64) NOT NULL COMMENT '游戏服的IP',
  `gameInnerIp` varchar(64) NOT NULL COMMENT '主机内网ＩＰ',
  `gamePort` int(6) NOT NULL COMMENT '游戏服的端口',
  `webPort` int(6) NOT NULL COMMENT 'WEB服务的端口',
  `assets` varchar(256) NOT NULL COMMENT '素材路径',
  `openServerDate` varchar(64) NOT NULL COMMENT '开服日期',
  `megerServerDate` varchar(64) DEFAULT NULL COMMENT '合服时间',
  `state` smallint(2) NOT NULL DEFAULT '1' COMMENT '状态（0停止使用 1正常使用）',
  `severState` smallint(2) NOT NULL DEFAULT '1' COMMENT '服务器状态(0.测试 1.流畅2：拥挤3.火爆  4.维护中 5.关闭)',
  `severType` smallint(2) NOT NULL DEFAULT '0' COMMENT '服务器类型(0.普通 1.新服 2.推荐)',
  `endStopDate` varchar(64) DEFAULT NULL COMMENT '维护结束时间',
  `megerFlag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否被合服'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器配置';

-- ----------------------------
-- Records of config_server
-- ----------------------------
INSERT INTO `config_server` VALUES ('1', '大唐诛仙-1服', 'xh', 'xh_0001', '192.168.0.200', '192.168.0.200', '8500', '9500', '', '2018-08-4 10:00:00', null, '1', '3', '2', null, '0');
INSERT INTO `config_server` VALUES ('2', '紫龙-2服', 'xh', 'xh_0002', '192.168.1.88', '192.168.1.88', '8500', '9500', '', '2018-03-19 10:00:00', '2018-03-19 10:00:00', '1', '3', '2', '2018-03-19 10:00:00', '0');

-- ----------------------------
-- Table structure for log_pay
-- ----------------------------
DROP TABLE IF EXISTS `log_pay`;
CREATE TABLE `log_pay` (
  `logId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL COMMENT '用户账户',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `paySite` varchar(30) NOT NULL COMMENT '充值站点',
  `outOrderNo` varchar(255) DEFAULT NULL COMMENT '自己的订单号',
  `orderNo` varchar(255) DEFAULT NULL COMMENT '支付平台订单号',
  `money` int(11) NOT NULL COMMENT '金额',
  `payType` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付类型 1:支付宝 2：微信 3：苹果  4：其他',
  `payItemId` varchar(50) NOT NULL COMMENT '购买的商品编号',
  `payUrl` text NOT NULL COMMENT '充值链接参数',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT ' 订单状态   0:支付唤起  1:支付关闭  2：支付成功 3：支付结束 ',
  `createTime` datetime NOT NULL COMMENT '充值时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`logId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付记录';

-- ----------------------------
-- Records of log_pay
-- ----------------------------
INSERT INTO `log_pay` VALUES ('1', '3', '1', 'xh_0001', '1', '1', '100', '1', '1', 'ddd', '0', '2018-03-28 14:56:21', null);
INSERT INTO `log_pay` VALUES ('2', '3', '1', 'xh_0001', '1', '1', '100', '1', '1', 'ddd', '0', '2018-03-28 14:56:21', '2018-03-27 14:56:46');
INSERT INTO `log_pay` VALUES ('3', '3', '1', 'xh_0002', '1', '1', '100', '1', '1', 'ddd', '0', '2018-03-28 14:56:21', '2018-03-14 14:56:54');

-- ----------------------------
-- Table structure for t_app_item
-- ----------------------------
DROP TABLE IF EXISTS `t_app_item`;
CREATE TABLE `t_app_item` (
  `APP_ITEM_ID` int(255) NOT NULL AUTO_INCREMENT,
  `APP_PLAYER_ID` int(255) NOT NULL COMMENT '申请人ID',
  `NAME` varchar(255) NOT NULL,
  `APP_TYPE` int(255) NOT NULL COMMENT '发送指定类型(0: 全服发送 1：单服 2： 指定玩家)',
  `GAME_SITE` varchar(255) NOT NULL COMMENT '游戏地址',
  `PLAYER_ID_LIST` varchar(255) NOT NULL COMMENT '玩家列表',
  `APP_DATE` datetime NOT NULL COMMENT '申请时间',
  `ITEMLIST` varchar(2048) NOT NULL,
  `CONTENT` varchar(5012) NOT NULL COMMENT '内容',
  `DETAIL` varchar(5012) NOT NULL COMMENT '详情',
  `REASON` varchar(2048) NOT NULL COMMENT '原因',
  `STATE` int(3) NOT NULL COMMENT '申请状态',
  `STATE_INFO` varchar(1024) DEFAULT NULL,
  `TITLE` varchar(1024) NOT NULL COMMENT '邮件标题',
  `AGENT` varchar(255) NOT NULL COMMENT '运营商',
  PRIMARY KEY (`APP_ITEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_app_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_authority`;
CREATE TABLE `t_authority` (
  `AUTHORITY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(64) NOT NULL,
  `FUNCTION_TYPE` int(11) DEFAULT NULL,
  `FUNCTION_NAME` varchar(64) NOT NULL COMMENT '功能名',
  `TYPE` int(11) NOT NULL,
  PRIMARY KEY (`AUTHORITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of t_authority
-- ----------------------------
INSERT INTO `t_authority` VALUES ('1', '用户管理', '101', '用户管理', '1');
INSERT INTO `t_authority` VALUES ('2', '用户管理', '102', '权限管理', '1');
INSERT INTO `t_authority` VALUES ('3', '用户管理', '103', '日志查询', '1');
INSERT INTO `t_authority` VALUES ('4', '玩家管理', '201', '玩家管理', '2');
INSERT INTO `t_authority` VALUES ('5', '玩家管理', '202', '封停IP', '2');
INSERT INTO `t_authority` VALUES ('6', '物品发放', '301', '发送物品申请', '3');
INSERT INTO `t_authority` VALUES ('7', '物品发放', '302', '发放审核管理', '3');
INSERT INTO `t_authority` VALUES ('8', '物品发放', '303', '发放日志列表', '3');
INSERT INTO `t_authority` VALUES ('9', '数据查询', '401', 'SQL执行', '4');
INSERT INTO `t_authority` VALUES ('10', '数据查询', '402', '查询列表', '4');
INSERT INTO `t_authority` VALUES ('11', '数据查询', '403', '操作日志', '4');
INSERT INTO `t_authority` VALUES ('12', '数据处理', '501', '数据处理', '5');
INSERT INTO `t_authority` VALUES ('13', '数据处理', '502', '及时系统公告', '5');
INSERT INTO `t_authority` VALUES ('14', '数据处理', '503', '定时系统公告', '5');
INSERT INTO `t_authority` VALUES ('15', '用户管理', '104', '角色管理', '1');
INSERT INTO `t_authority` VALUES ('16', '玩家管理', '203', '发送邮件', '2');
INSERT INTO `t_authority` VALUES ('17', '数据监控', '601', '聊天监控', '6');
INSERT INTO `t_authority` VALUES ('18', '服务器管理', '100', '服务器管理', '7');

-- ----------------------------
-- Table structure for t_opt_log
-- ----------------------------
DROP TABLE IF EXISTS `t_opt_log`;
CREATE TABLE `t_opt_log` (
  `LOG_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `USER_ID` int(11) NOT NULL COMMENT '用户编号',
  `USER_NAME` varchar(32) NOT NULL COMMENT '用户',
  `OPT` int(11) NOT NULL COMMENT '操作编号',
  `OPT_IP` varchar(32) NOT NULL COMMENT '操作IP',
  `CONTENT` varchar(1024) NOT NULL COMMENT '操作内容',
  `DETAIL` varchar(2048) NOT NULL COMMENT '详情',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`LOG_ID`),
  KEY `USER_ID` (`USER_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of t_opt_log
-- ----------------------------
INSERT INTO `t_opt_log` VALUES ('1', '1', 'qidian', '1', '192.168.1.200', '用户登陆', 'qidian登陆了', '2018-03-30 19:27:05');
INSERT INTO `t_opt_log` VALUES ('2', '1', 'qidian', '1', '192.168.1.200', '用户登陆', 'qidian登陆了', '2018-03-30 19:57:05');
INSERT INTO `t_opt_log` VALUES ('3', '1', 'qidian', '1', '192.168.1.200', '用户登陆', 'qidian登陆了', '2018-03-30 20:06:05');
INSERT INTO `t_opt_log` VALUES ('4', '1', 'qidian', '1', '192.168.1.200', '用户登陆', 'qidian登陆了', '2018-03-30 20:27:05');
INSERT INTO `t_opt_log` VALUES ('5', '1', 'qidian', '1', '192.168.1.200', '用户登陆', 'qidian登陆了', '2018-03-30 20:45:05');
INSERT INTO `t_opt_log` VALUES ('6', '1', 'qidian', '1', '192.168.1.200', '用户登陆', 'qidian登陆了', '2018-03-30 20:45:05');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `NAME` varchar(64) NOT NULL COMMENT '角色名字',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员');
INSERT INTO `t_role` VALUES ('2', '超级GM');
INSERT INTO `t_role` VALUES ('3', '运营');
INSERT INTO `t_role` VALUES ('4', '技术');
INSERT INTO `t_role` VALUES ('5', '运维');

-- ----------------------------
-- Table structure for t_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_role_authority`;
CREATE TABLE `t_role_authority` (
  `ROLE_AUTHORITY_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色权限编号',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色编号',
  `AUTHORITY_ID` int(11) NOT NULL COMMENT '权限编号',
  PRIMARY KEY (`ROLE_AUTHORITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=227 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of t_role_authority
-- ----------------------------
INSERT INTO `t_role_authority` VALUES ('150', '1', '1');
INSERT INTO `t_role_authority` VALUES ('151', '1', '2');
INSERT INTO `t_role_authority` VALUES ('152', '1', '3');
INSERT INTO `t_role_authority` VALUES ('153', '1', '4');
INSERT INTO `t_role_authority` VALUES ('154', '1', '5');
INSERT INTO `t_role_authority` VALUES ('155', '1', '6');
INSERT INTO `t_role_authority` VALUES ('156', '1', '7');
INSERT INTO `t_role_authority` VALUES ('157', '1', '8');
INSERT INTO `t_role_authority` VALUES ('158', '1', '9');
INSERT INTO `t_role_authority` VALUES ('159', '1', '10');
INSERT INTO `t_role_authority` VALUES ('160', '1', '11');
INSERT INTO `t_role_authority` VALUES ('161', '1', '12');
INSERT INTO `t_role_authority` VALUES ('162', '1', '13');
INSERT INTO `t_role_authority` VALUES ('163', '1', '14');
INSERT INTO `t_role_authority` VALUES ('164', '1', '15');
INSERT INTO `t_role_authority` VALUES ('165', '1', '16');
INSERT INTO `t_role_authority` VALUES ('166', '1', '17');
INSERT INTO `t_role_authority` VALUES ('167', '1', '18');
INSERT INTO `t_role_authority` VALUES ('190', '3', '4');
INSERT INTO `t_role_authority` VALUES ('191', '3', '5');
INSERT INTO `t_role_authority` VALUES ('192', '3', '6');
INSERT INTO `t_role_authority` VALUES ('193', '3', '7');
INSERT INTO `t_role_authority` VALUES ('194', '3', '8');
INSERT INTO `t_role_authority` VALUES ('195', '3', '13');
INSERT INTO `t_role_authority` VALUES ('196', '3', '14');
INSERT INTO `t_role_authority` VALUES ('197', '3', '16');
INSERT INTO `t_role_authority` VALUES ('198', '3', '17');
INSERT INTO `t_role_authority` VALUES ('199', '4', '4');
INSERT INTO `t_role_authority` VALUES ('200', '4', '5');
INSERT INTO `t_role_authority` VALUES ('201', '4', '6');
INSERT INTO `t_role_authority` VALUES ('202', '4', '8');
INSERT INTO `t_role_authority` VALUES ('203', '4', '13');
INSERT INTO `t_role_authority` VALUES ('204', '4', '14');
INSERT INTO `t_role_authority` VALUES ('205', '4', '16');
INSERT INTO `t_role_authority` VALUES ('206', '4', '17');
INSERT INTO `t_role_authority` VALUES ('207', '5', '4');
INSERT INTO `t_role_authority` VALUES ('208', '5', '5');
INSERT INTO `t_role_authority` VALUES ('209', '5', '6');
INSERT INTO `t_role_authority` VALUES ('210', '5', '8');
INSERT INTO `t_role_authority` VALUES ('211', '5', '13');
INSERT INTO `t_role_authority` VALUES ('212', '5', '14');
INSERT INTO `t_role_authority` VALUES ('213', '5', '16');
INSERT INTO `t_role_authority` VALUES ('214', '5', '17');
INSERT INTO `t_role_authority` VALUES ('215', '5', '18');
INSERT INTO `t_role_authority` VALUES ('216', '2', '1');
INSERT INTO `t_role_authority` VALUES ('217', '2', '2');
INSERT INTO `t_role_authority` VALUES ('218', '2', '3');
INSERT INTO `t_role_authority` VALUES ('219', '2', '4');
INSERT INTO `t_role_authority` VALUES ('220', '2', '5');
INSERT INTO `t_role_authority` VALUES ('221', '2', '6');
INSERT INTO `t_role_authority` VALUES ('222', '2', '7');
INSERT INTO `t_role_authority` VALUES ('223', '2', '8');
INSERT INTO `t_role_authority` VALUES ('224', '2', '13');
INSERT INTO `t_role_authority` VALUES ('225', '2', '15');
INSERT INTO `t_role_authority` VALUES ('226', '2', '16');

-- ----------------------------
-- Table structure for t_user_dataanalysis
-- ----------------------------
DROP TABLE IF EXISTS `t_user_dataanalysis`;
CREATE TABLE `t_user_dataanalysis` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `USER_NAME` varchar(32) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(32) NOT NULL COMMENT '密码',
  `AGENT` varchar(16) NOT NULL DEFAULT '广游' COMMENT '代理商',
  `SITE` varchar(32) NOT NULL DEFAULT 'qdgame' COMMENT '站点',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色编号',
  `STATE` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态(1.正常,2禁用)',
  `DELETE_FLAG` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除标记(0.未删 1.删除)',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`USER_ID`,`USER_NAME`),
  UNIQUE KEY `username` (`USER_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='数据分析账号表';

-- ----------------------------
-- Records of t_user_dataanalysis
-- ----------------------------
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'qidian', 'ken123', 'xh', 'xh', '1', '1', '0', '2017-08-12 18:20:22');
INSERT INTO `t_user_dataanalysis` VALUES ('2', 'admin', 'sktb2017', 'xh', 'xh', '1', '1', '0', '2017-08-18 17:23:02');

-- ----------------------------
-- Table structure for t_user_gcc
-- ----------------------------
DROP TABLE IF EXISTS `t_user_gcc`;
CREATE TABLE `t_user_gcc` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `USER_NAME` varchar(32) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(32) NOT NULL COMMENT '密码',
  `AGENT` varchar(512) NOT NULL DEFAULT 'sk' COMMENT '代理商',
  `SITE` varchar(32) NOT NULL DEFAULT 'sk' COMMENT '站点',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色编号',
  `STATE` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态(1.正常,2禁用)',
  `DELETE_FLAG` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除标记(0.未删 1.删除)',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`USER_ID`,`USER_NAME`),
  UNIQUE KEY `username` (`USER_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='后台账号表';

-- ----------------------------
-- Records of t_user_gcc
-- ----------------------------
INSERT INTO `t_user_gcc` VALUES ('1', 'qidian', 'ken123', 'xh', 'xh', '1', '1', '0', '2016-05-18 21:22:05');
INSERT INTO `t_user_gcc` VALUES ('2', 'admin', '123456', 'xh', 'xh', '2', '1', '0', '2017-07-25 18:59:36');
INSERT INTO `t_user_gcc` VALUES ('3', 'yunying', '123456', 'xh', 'xh', '3', '1', '0', '2017-07-25 19:00:43');
INSERT INTO `t_user_gcc` VALUES ('4', 'jishu', '123456', 'xh', 'xh', '4', '1', '0', '2017-07-25 19:01:35');
INSERT INTO `t_user_gcc` VALUES ('5', 'yunwei', '123456', 'xh', 'xh', '5', '1', '0', '2017-07-25 19:01:57');
