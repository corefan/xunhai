/*
Navicat MySQL Data Transfer

Source Server         : 外网106.75.222.211
Source Server Version : 50639
Source Host           : 106.75.222.211:3306
Source Database       : yunyou_gcc

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-09-30 21:22:46
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
  `appId` int(11) NOT NULL DEFAULT '0' COMMENT '游戏标识',
  `serverListStr` varchar(255) DEFAULT NULL COMMENT '已创号的服务器列表',
  `realName` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `identity` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `createTime` datetime NOT NULL COMMENT '充值时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=1234134536206 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='账号表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1208542695425', '5b9f770c66bc4', '123456', null, '1', '1', '[1]', null, null, '2018-09-17 17:50:16', null);
INSERT INTO `account` VALUES ('1208542695426', '5b9f7f985ba38', '123456', null, '1', '1', '[1]', null, null, '2018-09-17 18:19:12', null);
INSERT INTO `account` VALUES ('1210963267585', '5ba09963c1627', '123456', null, '1', '0', '[1]', null, null, '2018-09-18 14:21:25', null);
INSERT INTO `account` VALUES ('1210963267586', '5ba9f3308904a', '123456', null, '1', '0', '[1]', null, null, '2018-09-25 16:34:57', null);
INSERT INTO `account` VALUES ('1210963267587', '5ba9f5b2c52f9', '123456', null, '1', '0', '[1]', null, null, '2018-09-25 16:45:41', null);
INSERT INTO `account` VALUES ('1210963267588', '5baa014388ad2', '123456', null, '1', '0', '[1]', null, null, '2018-09-25 17:35:00', null);
INSERT INTO `account` VALUES ('1234134536193', '5bab63a00e156', '123456', null, '1', '1', '[1]', null, null, '2018-09-26 18:46:57', null);
INSERT INTO `account` VALUES ('1234134536194', '5bac7bbb2d8bb', '123456', null, '1', '1', '[1]', null, null, '2018-09-27 14:42:05', null);
INSERT INTO `account` VALUES ('1234134536195', '5baca4edb0276', '123456', null, '1', '3', null, null, null, '2018-09-27 17:37:51', null);
INSERT INTO `account` VALUES ('1234134536196', '5baca611aec89', '123456', null, '1', '3', null, null, null, '2018-09-27 17:42:42', null);
INSERT INTO `account` VALUES ('1234134536197', '5baca71ae7974', '123456', null, '1', '3', '[1]', null, null, '2018-09-27 17:47:08', null);
INSERT INTO `account` VALUES ('1234134536198', '5bacaa96d2742', '123456', null, '1', '2', '[1]', null, null, '2018-09-27 18:01:59', null);
INSERT INTO `account` VALUES ('1234134536199', '5bacad699b890', '123456', null, '1', '3', null, null, null, '2018-09-27 18:14:02', null);
INSERT INTO `account` VALUES ('1234134536200', '5bacad753b9c1', '123456', null, '1', '2', null, null, null, '2018-09-27 18:14:14', null);
INSERT INTO `account` VALUES ('1234134536201', '5bacaf15c3af5', '123456', null, '1', '3', '[1]', null, null, '2018-09-27 18:25:38', null);
INSERT INTO `account` VALUES ('1234134536202', '5bacb0e0424d0', '123456', null, '1', '2', null, null, null, '2018-09-27 18:28:49', null);
INSERT INTO `account` VALUES ('1234134536203', '5badcb62b9cc9', '123456', null, '1', '2', '[1]', null, null, '2018-09-28 14:34:11', null);
INSERT INTO `account` VALUES ('1234134536204', '5bae09b1ec950', '123456', null, '1', '2', '[1]', null, null, '2018-09-28 19:00:03', null);
INSERT INTO `account` VALUES ('1234134536205', '5bae0b9c17302', '123456', null, '1', '3', '[1]', null, null, '2018-09-28 19:08:13', null);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='激活码表';

-- ----------------------------
-- Records of actcode
-- ----------------------------

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
INSERT INTO `config_agent` VALUES ('yunyou', 'QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777', 'QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2', 'DGAMEASDWDSHEN-16TQASDEDE-W33TT', 'CN', 'zh', '*', '4', 'http://10.25.219.23:9800/code', 'http://10.25.219.23:9002/pay', 'http://10.25.219.23:8002', '0', '1');

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
INSERT INTO `config_server` VALUES ('1', '1服', 'yunyou', 'yunyou_0001', '106.75.222.211', '10.25.219.23', '8502', '9502', '', '2018-09-01 10:00:00', null, '1', '3', '2', null, '0');

-- ----------------------------
-- Table structure for log_pay
-- ----------------------------
DROP TABLE IF EXISTS `log_pay`;
CREATE TABLE `log_pay` (
  `logId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL COMMENT '用户账户',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `platform` varchar(30) DEFAULT NULL COMMENT '支付渠道',
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
) ENGINE=InnoDB AUTO_INCREMENT=1267873981628425 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付记录';

-- ----------------------------
-- Records of log_pay
-- ----------------------------
INSERT INTO `log_pay` VALUES ('1197479916109825', '1210963267585', '18800772743171', null, 'yunyou_0001', '18800812359693@1210963267585|yunyou_0001|18800772743171|101|3|6|A3DFDCE42C1E62CFB222DDE7AFA6FFA9', '1000000445300403', '6', '3', '101', 'data=%7B%22uid%22%3A%222143414348%22%2C%22out_trade_no%22%3A%221000000445300403%22%2C%22game_trade_no%22%3A%2218800812359693%401210963267585%7Cyunyou_0001%7C18800772743171%7C101%7C3%7C6%7CA3DFDCE42C1E62CFB222DDE7AFA6FFA9%22%2C%22price%22%3A%226%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537260895%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%22863e7d6ba8a8ff45d909090c3febbc45%22%7D', '0', '2018-09-18 16:54:57', null);
INSERT INTO `log_pay` VALUES ('1197479916109826', '1210963267585', '18800772743171', null, 'yunyou_0001', '18800812359694@1210963267585|yunyou_0001|18800772743171|101|3|6|A3DFDCE42C1E62CFB222DDE7AFA6FFA9', '2018091816592710298975', '6', '2', '101', 'data=%7B%22uid%22%3A%222143414348%22%2C%22out_trade_no%22%3A%222018091816592710298975%22%2C%22game_trade_no%22%3A%2218800812359694%401210963267585%7Cyunyou_0001%7C18800772743171%7C101%7C3%7C6%7CA3DFDCE42C1E62CFB222DDE7AFA6FFA9%22%2C%22price%22%3A%226%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537261178%2C%22pay%22%3A%222%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%223b046be9f6c18bd6b9ecdc711bd977bf%22%7D', '0', '2018-09-18 16:59:38', null);
INSERT INTO `log_pay` VALUES ('1197479916109827', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359699@1210963267586|yunyou_0001|18800772743172|107|3|648|6B6DD301E6BF820C97053C0D96F18082', '1000000448643634', '648', '3', '107', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448643634%22%2C%22game_trade_no%22%3A%2218800812359699%401210963267586%7Cyunyou_0001%7C18800772743172%7C107%7C3%7C648%7C6B6DD301E6BF820C97053C0D96F18082%22%2C%22price%22%3A%22648%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864872%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%22f0c0fa2e205a7a2cda8b79c3fa0ee17d%22%7D', '0', '2018-09-25 16:41:13', null);
INSERT INTO `log_pay` VALUES ('1197479916109828', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359700@1210963267586|yunyou_0001|18800772743172|106|3|328|492CF5AD198761E0340434C244EF9174', '1000000448643695', '328', '3', '106', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448643695%22%2C%22game_trade_no%22%3A%2218800812359700%401210963267586%7Cyunyou_0001%7C18800772743172%7C106%7C3%7C328%7C492CF5AD198761E0340434C244EF9174%22%2C%22price%22%3A%22328%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864887%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%220640538df23f2e0284c4ee28d3210fbf%22%7D', '0', '2018-09-25 16:41:28', null);
INSERT INTO `log_pay` VALUES ('1197479916109829', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359701@1210963267586|yunyou_0001|18800772743172|105|3|238|1F6A047F174794569E385EA2C4CD34EE', '1000000448643769', '238', '3', '105', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448643769%22%2C%22game_trade_no%22%3A%2218800812359701%401210963267586%7Cyunyou_0001%7C18800772743172%7C105%7C3%7C238%7C1F6A047F174794569E385EA2C4CD34EE%22%2C%22price%22%3A%22238%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864897%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%22d865848a190e09e7bbff5936201c131b%22%7D', '0', '2018-09-25 16:41:37', null);
INSERT INTO `log_pay` VALUES ('1197479916109830', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359702@1210963267586|yunyou_0001|18800772743172|104|3|168|B38866DADB2C868F015093CDF6573D45', '1000000448643890', '168', '3', '104', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448643890%22%2C%22game_trade_no%22%3A%2218800812359702%401210963267586%7Cyunyou_0001%7C18800772743172%7C104%7C3%7C168%7CB38866DADB2C868F015093CDF6573D45%22%2C%22price%22%3A%22168%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864906%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%226191e5dbf4168f3d8eeb71433ba9cffa%22%7D', '0', '2018-09-25 16:41:46', null);
INSERT INTO `log_pay` VALUES ('1197479916109831', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359703@1210963267586|yunyou_0001|18800772743172|103|3|98|C248515DCAF68D53B805153C39F9383F', '1000000448643920', '98', '3', '103', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448643920%22%2C%22game_trade_no%22%3A%2218800812359703%401210963267586%7Cyunyou_0001%7C18800772743172%7C103%7C3%7C98%7CC248515DCAF68D53B805153C39F9383F%22%2C%22price%22%3A%2298%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864916%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%222de075e2d98b3db5d8d951dc1e9fca40%22%7D', '0', '2018-09-25 16:41:57', null);
INSERT INTO `log_pay` VALUES ('1197479916109832', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359704@1210963267586|yunyou_0001|18800772743172|102|3|30|4374C7968228AFFC226379DEF60E0F38', '1000000448643966', '30', '3', '102', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448643966%22%2C%22game_trade_no%22%3A%2218800812359704%401210963267586%7Cyunyou_0001%7C18800772743172%7C102%7C3%7C30%7C4374C7968228AFFC226379DEF60E0F38%22%2C%22price%22%3A%2230%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864925%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%228645e7f078a4d6aa3ef00e654d550eaa%22%7D', '0', '2018-09-25 16:42:06', null);
INSERT INTO `log_pay` VALUES ('1197479916109833', '1210963267586', '18800772743172', null, 'yunyou_0001', '18800812359706@1210963267586|yunyou_0001|18800772743172|101|3|6|22769F33DF0B6239EF678CCB7A98464B', '1000000448644373', '6', '3', '101', 'data=%7B%22uid%22%3A%222710930615%22%2C%22out_trade_no%22%3A%221000000448644373%22%2C%22game_trade_no%22%3A%2218800812359706%401210963267586%7Cyunyou_0001%7C18800772743172%7C101%7C3%7C6%7C22769F33DF0B6239EF678CCB7A98464B%22%2C%22price%22%3A%226%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1537864934%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%222578cb072ec7884cd69d2cf9f9b72cc2%22%7D', '0', '2018-09-25 16:42:14', null);
INSERT INTO `log_pay` VALUES ('1267873981628417', '1234134536194', '18826320773122', 'yunyou_0', 'yunyou_0001', '18826019012634', '1000000449885800', '648', '3', '107', 'data=%7B%22uid%22%3A%226638336607%22%2C%22out_trade_no%22%3A%221000000449885800%22%2C%22game_trade_no%22%3A%2218826019012634%401234134536194%7Cyunyou_0001%7C18826320773122%7C107%7C3%7C648%7C18826019012634%7C5DA212B546EEAACD99E0F67A405EBD35%22%2C%22price%22%3A%22648%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538033642%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%229c1b44a2b8b48cd2d6f5a0606f8ce22d%22%7D', '0', '2018-09-27 15:34:03', null);
INSERT INTO `log_pay` VALUES ('1267873981628418', '1234134536194', '18826320773122', 'yunyou_0', 'yunyou_0001', '18826019012635', '1000000449885897', '25', '3', '303', 'data=%7B%22uid%22%3A%226638336607%22%2C%22out_trade_no%22%3A%221000000449885897%22%2C%22game_trade_no%22%3A%2218826019012635%401234134536194%7Cyunyou_0001%7C18826320773122%7C303%7C3%7C25%7C18826019012635%7C3932AAB6DDCBFEF9CD30A23CDB2C3B9C%22%2C%22price%22%3A%2225%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538033660%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%2282df2789259e8acf22fd77d581e1bf04%22%7D', '0', '2018-09-27 15:34:20', null);
INSERT INTO `log_pay` VALUES ('1267873981628419', '1234134536204', '18826320773127', 'yunyou_0', 'yunyou_0001', '18826019012653', '1000000450671487', '25', '3', '303', 'data=%7B%22uid%22%3A%225475659844%22%2C%22out_trade_no%22%3A%221000000450671487%22%2C%22game_trade_no%22%3A%2218826019012653%401234134536204%7Cyunyou_0001%7C18826320773127%7C303%7C3%7C25%7C18826019012653%7C8DF02A4E26DA82FA37004CE94CB5E2C5%22%2C%22price%22%3A%2225%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538132585%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%228890dc34e188e5ed9e9dedcaef19894a%22%7D', '0', '2018-09-28 19:03:06', null);
INSERT INTO `log_pay` VALUES ('1267873981628420', '1234134536204', '18826320773127', 'yunyou_0', 'yunyou_0001', '18826019012654', '1000000450671540', '648', '3', '107', 'data=%7B%22uid%22%3A%225475659844%22%2C%22out_trade_no%22%3A%221000000450671540%22%2C%22game_trade_no%22%3A%2218826019012654%401234134536204%7Cyunyou_0001%7C18826320773127%7C107%7C3%7C648%7C18826019012654%7C15C8F78FF9DCE75B7E01E9479855BD7F%22%2C%22price%22%3A%22648%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538132595%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%22dc8cec427bc844b665ad30309a5e9153%22%7D', '0', '2018-09-28 19:03:16', null);
INSERT INTO `log_pay` VALUES ('1267873981628421', '1234134536205', '18826320773128', 'yunyou_0', 'yunyou_0001', '18826019012657', '1000000450674349', '25', '3', '303', 'data=%7B%22uid%22%3A%225889802229%22%2C%22out_trade_no%22%3A%221000000450674349%22%2C%22game_trade_no%22%3A%2218826019012657%401234134536205%7Cyunyou_0001%7C18826320773128%7C303%7C3%7C25%7C18826019012657%7CB2883B7651984B678D016CBEB6D28B00%22%2C%22price%22%3A%2225%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538133070%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%2221e9dbfb22acfe1a1e23ea5b46292403%22%7D', '0', '2018-09-28 19:11:11', null);
INSERT INTO `log_pay` VALUES ('1267873981628422', '1234134536205', '18826320773128', 'yunyou_0', 'yunyou_0001', '18826019012661', '1000000450674712', '648', '3', '107', 'data=%7B%22uid%22%3A%225889802229%22%2C%22out_trade_no%22%3A%221000000450674712%22%2C%22game_trade_no%22%3A%2218826019012661%401234134536205%7Cyunyou_0001%7C18826320773128%7C107%7C3%7C648%7C18826019012661%7C131F7571CE12260D9F5612FCAFF26FEE%22%2C%22price%22%3A%22648%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538133176%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%222e004da05f87432ce661ee9062b85fe6%22%7D', '0', '2018-09-28 19:12:56', null);
INSERT INTO `log_pay` VALUES ('1267873981628423', '1234134536205', '18826320773128', 'yunyou_0', 'yunyou_0001', '18826019012663', '1000000450674820', '328', '3', '106', 'data=%7B%22uid%22%3A%225889802229%22%2C%22out_trade_no%22%3A%221000000450674820%22%2C%22game_trade_no%22%3A%2218826019012663%401234134536205%7Cyunyou_0001%7C18826320773128%7C106%7C3%7C328%7C18826019012663%7CBF67A8A24E0304AA83C622005E21950D%22%2C%22price%22%3A%22328%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538133187%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%220c18dd23dc6ceb0c9312cde9a2e23a05%22%7D', '0', '2018-09-28 19:13:08', null);
INSERT INTO `log_pay` VALUES ('1267873981628424', '1234134536205', '18826320773128', 'yunyou_0', 'yunyou_0001', '18826019012664', '1000000450674837', '238', '3', '105', 'data=%7B%22uid%22%3A%225889802229%22%2C%22out_trade_no%22%3A%221000000450674837%22%2C%22game_trade_no%22%3A%2218826019012664%401234134536205%7Cyunyou_0001%7C18826320773128%7C105%7C3%7C238%7C18826019012664%7C1AB18EAF01A7E12F04A3C73E24132DE8%22%2C%22price%22%3A%22238%22%2C%22trade_status%22%3A%22TRADE_SUCCESS%22%2C%22time%22%3A1538133198%2C%22pay%22%3A%223%22%2C%22is_test%22%3A%221%22%2C%22sign%22%3A%22ecf2b3925a488d5906d19487007af785%22%7D', '0', '2018-09-28 19:13:19', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of t_opt_log
-- ----------------------------
INSERT INTO `t_opt_log` VALUES ('1', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:20:05');
INSERT INTO `t_opt_log` VALUES ('2', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:22:05');
INSERT INTO `t_opt_log` VALUES ('3', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-26 18:04:05');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='数据分析账号表';

-- ----------------------------
-- Records of t_user_dataanalysis
-- ----------------------------
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'qidian', 'ken123', 'yunyou', 'yunyou', '1', '1', '0', '2017-08-12 18:20:22');
INSERT INTO `t_user_dataanalysis` VALUES ('2', 'admin', 'sktb2017', 'yunyou', 'yunyou', '1', '1', '0', '2017-08-18 17:23:02');
INSERT INTO `t_user_dataanalysis` VALUES ('3', 'yunying', '123456', 'yunyou', 'yunyou', '1', '1', '0', '2018-09-26 18:12:39');

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
INSERT INTO `t_user_gcc` VALUES ('1', 'qidian', 'ken123', 'yunyou', 'yunyou', '1', '1', '0', '2016-05-18 21:22:05');
INSERT INTO `t_user_gcc` VALUES ('2', 'admin', '123456', 'yunyou', 'yunyou', '2', '1', '0', '2017-07-25 18:59:36');
INSERT INTO `t_user_gcc` VALUES ('3', 'yunying', '123456', 'yunyou', 'yunyou', '3', '1', '0', '2017-07-25 19:00:43');
INSERT INTO `t_user_gcc` VALUES ('4', 'jishu', '123456', 'yunyou', 'yunyou', '4', '1', '0', '2017-07-25 19:01:35');
INSERT INTO `t_user_gcc` VALUES ('5', 'yunwei', '123456', 'yunyou', 'yunyou', '5', '1', '0', '2017-07-25 19:01:57');
