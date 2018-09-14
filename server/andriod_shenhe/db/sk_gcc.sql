/*
Navicat MySQL Data Transfer

Source Server         : 安卓审核服106.15.180.133
Source Server Version : 50173
Source Host           : 106.15.180.133:3306
Source Database       : sk_gcc

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-10-20 14:46:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(64) NOT NULL COMMENT '账号',
  `passWord` varchar(64) NOT NULL COMMENT '密码',
  `telephone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `tourist` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否游客  1：是 ',
  `serverListStr` varchar(255) DEFAULT NULL COMMENT '已创号的服务器列表',
  `createTime` datetime NOT NULL COMMENT '充值时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=10044 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='账号表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('10000', 'a8d56326d3a0a70033c943d901162c18488f2492', '123456', null, '1', null, '2017-07-26 14:18:21', null);
INSERT INTO `account` VALUES ('10001', '13654214589', '111111', null, '0', null, '2017-07-26 14:18:29', null);
INSERT INTO `account` VALUES ('10002', '13511112222', '111111111', null, '0', null, '2017-07-26 14:22:07', null);
INSERT INTO `account` VALUES ('10003', 'f5dcadb1f0ef84fcfaead8fa49419b8ab4f5d6e3', '123456', null, '1', null, '2017-07-26 14:53:46', null);
INSERT INTO `account` VALUES ('10004', '15066661111', '1111111111111', null, '0', null, '2017-07-26 14:54:29', null);
INSERT INTO `account` VALUES ('10005', '1232f63bf13d4b05d00693e58bd2b984', '123456', null, '1', null, '2017-07-26 15:31:21', null);
INSERT INTO `account` VALUES ('10006', '102cbeba47c962e914f27bd057f82231', '123456', null, '1', null, '2017-07-26 15:39:34', null);
INSERT INTO `account` VALUES ('10007', '13317937508', '123456', null, '0', '[1]', '2017-07-26 15:40:38', '2017-07-26 16:56:59');
INSERT INTO `account` VALUES ('10008', '9d6cae93e5873a9afdac3204590152c8', '123456', null, '1', null, '2017-07-26 15:48:28', null);
INSERT INTO `account` VALUES ('10009', '13011111111', '123456', null, '0', null, '2017-07-26 17:00:21', null);
INSERT INTO `account` VALUES ('10010', '13022222222', '123456', null, '0', null, '2017-07-26 17:27:28', null);
INSERT INTO `account` VALUES ('10011', '13033333333', '123456', null, '0', null, '2017-07-26 17:51:58', null);
INSERT INTO `account` VALUES ('10012', '13213132222', '11111111111', null, '0', null, '2017-07-26 19:41:23', null);
INSERT INTO `account` VALUES ('10013', '13122112211', '11111111111', null, '0', null, '2017-07-26 19:41:38', null);
INSERT INTO `account` VALUES ('10014', '16a3839d6d889cde3cb24bd7cb7372cc', '123456', null, '1', null, '2017-07-26 19:47:26', null);
INSERT INTO `account` VALUES ('10015', '13603083311', '123456', null, '0', null, '2017-07-26 20:21:38', null);
INSERT INTO `account` VALUES ('10016', '2851ef50778208039b149485c0a4357d', '123456', null, '1', null, '2017-07-26 20:24:45', null);
INSERT INTO `account` VALUES ('10017', '4bf998e2cd0e35dbab0b309cb379bc5a58a7557b', '123456', null, '1', null, '2017-07-26 23:19:33', null);
INSERT INTO `account` VALUES ('10018', '13317937508', '123456', null, '0', '[1]', '2017-07-27 00:19:50', '2017-07-28 20:45:26');
INSERT INTO `account` VALUES ('10019', '13500088808', '1111111', null, '0', null, '2017-07-27 00:22:30', null);
INSERT INTO `account` VALUES ('10020', '5818901905d9a94dd5e373d1a5700b5f', '123456', null, '1', null, '2017-07-27 01:04:23', null);
INSERT INTO `account` VALUES ('10021', '13603083312', '123456', null, '0', null, '2017-07-27 14:25:16', null);
INSERT INTO `account` VALUES ('10022', '13100001111', '1111111111', null, '0', null, '2017-07-27 16:46:53', null);
INSERT INTO `account` VALUES ('10023', '13011122212', '1111111111', null, '0', null, '2017-07-27 17:11:11', null);
INSERT INTO `account` VALUES ('10024', '0f9b8cf18cd02b94057ace1dd1582482', '123456', null, '1', null, '2017-07-28 07:51:14', null);
INSERT INTO `account` VALUES ('10025', 'D3B1273D-932D-4FC3-B919-5EDE773C9DB2', '123456', null, '1', null, '2017-07-28 14:50:33', null);
INSERT INTO `account` VALUES ('10026', 'CABEB87E-8EC3-4190-804A-BC3A80151D06', '123456', null, '1', null, '2017-07-28 15:23:49', null);
INSERT INTO `account` VALUES ('10027', '020A752F-9594-41D9-B2B3-97A7CD858021', '123456', null, '1', null, '2017-07-28 16:36:01', null);
INSERT INTO `account` VALUES ('10028', '13011112211', '11111111111', null, '0', null, '2017-07-28 21:09:59', null);
INSERT INTO `account` VALUES ('10029', 'cc98636fa2a01c54813122c31683ee7e', '123456', null, '1', null, '2017-07-31 09:57:31', null);
INSERT INTO `account` VALUES ('10030', '4d7a154f4b2dfc5f690c2f584863040f', '123456', null, '1', null, '2017-08-05 18:42:33', null);
INSERT INTO `account` VALUES ('10031', 'd95a9914c4145922fda24f9a755a25ff', '123456', null, '1', null, '2017-08-05 18:44:56', null);
INSERT INTO `account` VALUES ('10032', '13779945052', '123456', null, '0', null, '2017-08-16 10:24:53', null);
INSERT INTO `account` VALUES ('10033', '13810000001', '123456', null, '0', null, '2017-08-16 11:52:20', null);
INSERT INTO `account` VALUES ('10034', 'a7d044aefe1d8a6954cc1dd0816ebb96', '123456', null, '1', null, '2017-08-31 14:14:14', null);
INSERT INTO `account` VALUES ('10035', 'edb838a5b3e6f942bfc543be381c529c', '123456', null, '1', null, '2017-08-31 14:32:34', null);
INSERT INTO `account` VALUES ('10036', '18000000000', '123456', null, '0', null, '2017-10-03 10:05:55', null);
INSERT INTO `account` VALUES ('10037', '33dec5390e44cda2314f541ee27a1b9f5d9d8535', '123456', null, '1', null, '2017-10-14 10:43:41', null);
INSERT INTO `account` VALUES ('10038', '17511111111', '123456', null, '0', null, '2017-10-16 09:46:40', null);
INSERT INTO `account` VALUES ('10039', 'fbaccbcc96c97a0383acdb0b671686c6', '123456', null, '1', null, '2017-10-16 20:07:21', null);
INSERT INTO `account` VALUES ('10040', '04dc8a824272d7974ff37a971efbae70', '123456', null, '1', null, '2017-10-16 20:32:37', null);
INSERT INTO `account` VALUES ('10041', '17511111112', '111111', null, '0', null, '2017-10-17 11:40:00', null);
INSERT INTO `account` VALUES ('10042', 'ddccb1e218cd97fb926d153ffb36d91f24a6321b', '123456', null, '1', null, '2017-10-17 11:50:22', null);
INSERT INTO `account` VALUES ('10043', '999fb8d56d906abf50201efab637fe24', '123456', null, '1', null, '2017-10-18 14:50:25', null);

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
INSERT INTO `config_agent` VALUES ('sk', 'QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777', 'QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2', 'DGAMEASDWDSHEN-16TQASDEDE-W33TT', 'CN', 'zh', '172.19.209.147', '3', 'http://172.19.209.147:9800/code', 'http://172.19.209.147:9000/pay', 'http://172.19.209.147:8000', '0', '1');

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
  `dbBaseUrl` varchar(128) NOT NULL COMMENT '服务器上的mysql ip+port',
  `dbGameName` varchar(64) NOT NULL COMMENT '游戏数据库名',
  `dbBaseName` varchar(64) NOT NULL COMMENT '基础数据库名',
  `dbLogName` varchar(64) NOT NULL COMMENT '日志数据库名',
  `dbUsername` varchar(512) NOT NULL COMMENT '数据库用户名',
  `dbPassword` varchar(512) NOT NULL COMMENT '数据库用户密码',
  `assets` varchar(256) NOT NULL COMMENT '素材路径',
  `openServerDate` varchar(64) NOT NULL COMMENT '开服日期',
  `megerServerDate` varchar(64) DEFAULT NULL COMMENT '合服时间',
  `state` smallint(2) NOT NULL DEFAULT '1' COMMENT '状态（0停止使用 1正常使用）',
  `severState` smallint(2) NOT NULL DEFAULT '1' COMMENT '服务器状态(0.测试 1.流畅2：拥挤3.火爆  4.维护中 5.关闭)',
  `severType` smallint(2) NOT NULL DEFAULT '0' COMMENT '服务器类型(0.普通 1.新服 2.推荐)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器配置';

-- ----------------------------
-- Records of config_server
-- ----------------------------
INSERT INTO `config_server` VALUES ('1', '华夏征途-1服', 'sk', 'sk_0001', '106.15.180.133', '172.19.209.147', '8500', '9500', '172.19.209.147:3306/', 'sk_game_0001', 'sk_base_0001', 'sk_log_0001', 'BfaVOnYkT2Y33X/HUW3lZrdGlv8PzQQ4fa8P+stSuXVc2jwhfY8x5/Y/+9k6PVip/S6qUDetHmXO\r\nJjVqEQlHRjkESRTGHY+VUOzOMoFHobyP+jycVDPKTPE1XCTpUbCmjMmVcSSsfq4HMID1/waFlI2B\r\nyV9AelT5pnz1wHE7i+4=', 'BfaVOnYkT2Y33X/HUW3lZrdGlv8PzQQ4fa8P+stSuXVc2jwhfY8x5/Y/+9k6PVip/S6qUDetHmXO\r\nJjVqEQlHRjkESRTGHY+VUOzOMoFHobyP+jycVDPKTPE1XCTpUbCmjMmVcSSsfq4HMID1/waFlI2B\r\nyV9AelT5pnz1wHE7i+4=', '', '2017-07-24 10:59:59', null, '1', '3', '2');

-- ----------------------------
-- Table structure for log_pay
-- ----------------------------
DROP TABLE IF EXISTS `log_pay`;
CREATE TABLE `log_pay` (
  `logId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL COMMENT '用户账户',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `paySite` varchar(30) NOT NULL COMMENT '充值站点',
  `outOrderNo` varchar(255) DEFAULT NULL COMMENT '自己的订单号',
  `orderNo` varchar(255) DEFAULT NULL COMMENT '支付平台订单号',
  `money` int(11) NOT NULL COMMENT '金额',
  `payType` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付类型',
  `payItemId` int(11) unsigned NOT NULL COMMENT '购买的商品编号',
  `payUrl` text NOT NULL COMMENT '充值链接参数',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT ' 订单状态   0:支付唤起  1:支付关闭  2：支付成功 3：支付结束 ',
  `createTime` datetime NOT NULL COMMENT '充值时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`logId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付记录';

-- ----------------------------
-- Records of log_pay
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='权限表';

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of t_opt_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `NAME` varchar(64) NOT NULL COMMENT '角色名字',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员');
INSERT INTO `t_role` VALUES ('2', '超级GM');
INSERT INTO `t_role` VALUES ('3', 'GM');
INSERT INTO `t_role` VALUES ('4', '技术人员');

-- ----------------------------
-- Table structure for t_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_role_authority`;
CREATE TABLE `t_role_authority` (
  `ROLE_AUTHORITY_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色权限编号',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色编号',
  `AUTHORITY_ID` int(11) NOT NULL COMMENT '权限编号',
  PRIMARY KEY (`ROLE_AUTHORITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of t_role_authority
-- ----------------------------
INSERT INTO `t_role_authority` VALUES ('45', '4', '4');
INSERT INTO `t_role_authority` VALUES ('46', '1', '1');
INSERT INTO `t_role_authority` VALUES ('47', '1', '2');
INSERT INTO `t_role_authority` VALUES ('48', '1', '3');
INSERT INTO `t_role_authority` VALUES ('49', '1', '4');
INSERT INTO `t_role_authority` VALUES ('50', '1', '5');
INSERT INTO `t_role_authority` VALUES ('51', '1', '6');
INSERT INTO `t_role_authority` VALUES ('52', '1', '7');
INSERT INTO `t_role_authority` VALUES ('53', '1', '8');
INSERT INTO `t_role_authority` VALUES ('54', '1', '9');
INSERT INTO `t_role_authority` VALUES ('55', '1', '10');
INSERT INTO `t_role_authority` VALUES ('56', '1', '11');
INSERT INTO `t_role_authority` VALUES ('57', '1', '12');
INSERT INTO `t_role_authority` VALUES ('58', '1', '13');
INSERT INTO `t_role_authority` VALUES ('59', '1', '14');
INSERT INTO `t_role_authority` VALUES ('60', '3', '4');
INSERT INTO `t_role_authority` VALUES ('61', '3', '5');
INSERT INTO `t_role_authority` VALUES ('62', '3', '6');
INSERT INTO `t_role_authority` VALUES ('63', '3', '14');
INSERT INTO `t_role_authority` VALUES ('64', '3', '13');
INSERT INTO `t_role_authority` VALUES ('65', '2', '4');
INSERT INTO `t_role_authority` VALUES ('66', '2', '5');
INSERT INTO `t_role_authority` VALUES ('67', '2', '6');
INSERT INTO `t_role_authority` VALUES ('68', '2', '7');
INSERT INTO `t_role_authority` VALUES ('69', '2', '10');
INSERT INTO `t_role_authority` VALUES ('70', '2', '12');
INSERT INTO `t_role_authority` VALUES ('71', '2', '13');
INSERT INTO `t_role_authority` VALUES ('72', '1', '15');
INSERT INTO `t_role_authority` VALUES ('76', '2', '8');
INSERT INTO `t_role_authority` VALUES ('77', '3', '8');
INSERT INTO `t_role_authority` VALUES ('78', '4', '8');
INSERT INTO `t_role_authority` VALUES ('79', '3', '16');
INSERT INTO `t_role_authority` VALUES ('80', '2', '14');
INSERT INTO `t_role_authority` VALUES ('81', '1', '17');
INSERT INTO `t_role_authority` VALUES ('82', '2', '17');
INSERT INTO `t_role_authority` VALUES ('83', '3', '17');

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='数据分析账号表';

-- ----------------------------
-- Records of t_user_dataanalysis
-- ----------------------------
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'hy', '123456', '8090', '8090', '1', '1', '0', '2015-04-27 18:20:22');
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'qidian', 'qidian', '8090', '8090', '1', '1', '0', '2014-05-26 18:26:10');
INSERT INTO `t_user_dataanalysis` VALUES ('2', 'barsk', 'qdgame!@#', '8090', '8090', '2', '1', '0', '2014-03-10 15:10:27');
INSERT INTO `t_user_dataanalysis` VALUES ('3', 'just', 'qidian', '8090', '8090', '1', '1', '0', '2014-05-22 15:32:58');
INSERT INTO `t_user_dataanalysis` VALUES ('4', 'szwz_gm', 'szwz_gm', '8090', '8090', '1', '1', '0', '2014-05-27 18:09:03');
INSERT INTO `t_user_dataanalysis` VALUES ('5', 'hy_yy', 'a123456', '8090', '8090', '2', '1', '0', '2015-06-10 11:31:00');
INSERT INTO `t_user_dataanalysis` VALUES ('6', 'zhangmei', 'zhangmei888', '8090', '8090', '2', '1', '0', '2015-09-12 14:17:45');
INSERT INTO `t_user_dataanalysis` VALUES ('8', 'wanghui', 'nsgd8090wh', '8090', '8090', '2', '1', '0', '2015-10-24 16:35:15');
INSERT INTO `t_user_dataanalysis` VALUES ('9', 'tianwen', 'nsgd8090tw', '8090', '8090', '2', '1', '0', '2015-10-24 16:35:25');
INSERT INTO `t_user_dataanalysis` VALUES ('10', 'qinqian', 'qinqian', '8090', '8090', '1', '1', '0', '2015-11-06 18:23:00');
INSERT INTO `t_user_dataanalysis` VALUES ('11', '8787_admin', '8787_admin', '8787', '8787', '2', '1', '0', '2015-11-17 15:16:38');
INSERT INTO `t_user_dataanalysis` VALUES ('12', 'xd_admin', 'xd_admin', 'xd', 'xd', '2', '1', '0', '2015-11-17 16:08:02');
INSERT INTO `t_user_dataanalysis` VALUES ('13', 'jf_admin', 'jf_admin', 'jf', 'jf', '2', '1', '0', '2015-12-09 09:46:41');

-- ----------------------------
-- Table structure for t_user_gcc
-- ----------------------------
DROP TABLE IF EXISTS `t_user_gcc`;
CREATE TABLE `t_user_gcc` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `USER_NAME` varchar(32) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(32) NOT NULL COMMENT '密码',
  `AGENT` varchar(512) NOT NULL DEFAULT '广游' COMMENT '代理商',
  `SITE` varchar(32) NOT NULL DEFAULT 'qdgame' COMMENT '站点',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色编号',
  `STATE` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态(1.正常,2禁用)',
  `DELETE_FLAG` tinyint(2) NOT NULL DEFAULT '0' COMMENT '删除标记(0.未删 1.删除)',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`USER_ID`,`USER_NAME`),
  UNIQUE KEY `username` (`USER_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='后台账号表';

-- ----------------------------
-- Records of t_user_gcc
-- ----------------------------
INSERT INTO `t_user_gcc` VALUES ('1', 'qidian', '123456', 'sk', 'sk', '1', '1', '0', '2016-05-18 21:22:05');
INSERT INTO `t_user_gcc` VALUES ('16', 'sktb', '123456', 'sk', 'sk', '1', '1', '0', '2016-04-26 12:36:32');
INSERT INTO `t_user_gcc` VALUES ('17', 'wangbin', '123456', 'sk', 'sk', '2', '1', '0', '2016-05-09 11:41:12');
INSERT INTO `t_user_gcc` VALUES ('18', 'zhongshen1', '123456', 'sk', 'sk', '2', '1', '0', '2016-05-09 11:41:49');
