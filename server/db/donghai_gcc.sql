/*
Navicat MySQL Data Transfer

Source Server         : 外网106.75.222.211
Source Server Version : 50639
Source Host           : 106.75.222.211:3306
Source Database       : donghai_gcc

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-09-30 21:15:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户唯一编号',
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
) ENGINE=InnoDB AUTO_INCREMENT=1238913617927 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='账号表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('439', 'Tggjibfcd', '123456', null, '1', '0', null, null, null, '2018-09-08 18:42:39', null);
INSERT INTO `account` VALUES ('440', 'Ytrertg', '123456', null, '1', '0', null, null, null, '2018-09-08 23:41:53', null);
INSERT INTO `account` VALUES ('442', 'Tfvhyrg', '123456', null, '1', '0', null, null, null, '2018-09-09 12:57:55', null);
INSERT INTO `account` VALUES ('534', 'Grtthrf', '123456', null, '1', '0', null, null, null, '2018-09-12 12:09:30', null);
INSERT INTO `account` VALUES ('549', 'asdfghjk', '123456', null, '1', '0', '[1]', null, null, '2018-09-12 17:09:07', null);
INSERT INTO `account` VALUES ('557', 'Rtftyhii', '123456', null, '1', '0', null, null, null, '2018-09-13 16:00:26', null);
INSERT INTO `account` VALUES ('564', 'Hdhcbchbfb', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 18:36:04', null);
INSERT INTO `account` VALUES ('571', 'xiaoeyu111', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 20:18:57', null);
INSERT INTO `account` VALUES ('583', 'Pokill', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 11:45:36', null);
INSERT INTO `account` VALUES ('620', 'Hdhddh', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 19:19:40', null);
INSERT INTO `account` VALUES ('633', 'Eedjdjjdjn', '123456', null, '1', '0', '[1]', null, null, '2018-09-17 17:46:10', null);
INSERT INTO `account` VALUES ('1238913617921', 'Fgguhh', '123456', null, '1', '149', '[1]', null, null, '2018-09-28 11:17:43', null);
INSERT INTO `account` VALUES ('1238913617922', 'Ethhfg', '123456', null, '1', '149', '[1]', null, null, '2018-09-28 11:37:57', null);
INSERT INTO `account` VALUES ('1238913617923', 'Erfrrt', '123456', null, '1', '149', null, null, null, '2018-09-28 12:02:00', null);
INSERT INTO `account` VALUES ('1238913617924', 'Rtrdrft', '123456', null, '1', '149', '[1]', null, null, '2018-09-28 12:05:52', null);
INSERT INTO `account` VALUES ('1238913617925', 'Difjjgfjjd', '123456', null, '1', '149', '[1]', null, null, '2018-09-28 22:35:01', null);
INSERT INTO `account` VALUES ('1238913617926', 'Flyflyerson', '123456', null, '1', '149', '[1]', null, null, '2018-09-30 03:29:09', null);

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
INSERT INTO `config_agent` VALUES ('donghai', 'QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777', 'QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2', 'DGAMEASDWDSHEN-16TQASDEDE-W33TT', 'CN', 'zh', '*', '4', 'http://10.25.219.23:9800/code', 'http://10.25.219.23:9000/pay', 'http://10.25.219.23:8001', '0', '1');

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
INSERT INTO `config_server` VALUES ('1', '1服', 'donghai', 'donghai_0001', '106.75.222.211', '10.25.219.23', '8501', '9501', '', '2018-08-31 10:00:00', null, '1', '3', '2', null, '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=1144689282875404 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付记录';

-- ----------------------------
-- Records of log_pay
-- ----------------------------
INSERT INTO `log_pay` VALUES ('247530859036673', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280882', '201809282236301168382818189', '6', '3', '101', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282236301168382818189\",\"totalFee\":\"600\",\"randStr\":\"ecd95218fb40a2c4f57b1e1f84163116\",\"sign\":\"a4f4f99903b6c9107628358c63f56bd5\",\"endtime\":\"1538145580\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|101|3|6|18831100280882|16A1BEB39A8DDA2CAEAC764CD7651BAD\",\"cpOrderId\":\"18831100280882\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:39:41', null);
INSERT INTO `log_pay` VALUES ('247530859036674', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280883', '201809282239571536895279404', '30', '3', '102', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282239571536895279404\",\"totalFee\":\"3000\",\"randStr\":\"acbd701dcdc44942e2255bc1d0306e8e\",\"sign\":\"e1c6c12d61dd2a4c3621e1e799c2687a\",\"endtime\":\"1538145618\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|102|3|30|18831100280883|ED049CE62BE9BF020E9159A5C1ADA3B7\",\"cpOrderId\":\"18831100280883\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:40:19', null);
INSERT INTO `log_pay` VALUES ('247530859036675', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280884', '201809282240201714072132167', '98', '3', '103', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282240201714072132167\",\"totalFee\":\"9800\",\"randStr\":\"f72d9729a24eb929dcddf77ba2f6a258\",\"sign\":\"3be3f079458a2229323b902a7723394c\",\"endtime\":\"1538145636\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|103|3|98|18831100280884|DE67AD288290085260656C44591EC614\",\"cpOrderId\":\"18831100280884\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:40:36', null);
INSERT INTO `log_pay` VALUES ('247530859036676', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280885', '201809282240411442874198434', '168', '3', '104', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282240411442874198434\",\"totalFee\":\"16800\",\"randStr\":\"5a17e758ba2643f9a840f75bd0ccd402\",\"sign\":\"fcd81e948e43b8a50dc3ceef40ab6fa9\",\"endtime\":\"1538145656\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|104|3|168|18831100280885|304EF85D61A3B64FFCCC34DCD1A54682\",\"cpOrderId\":\"18831100280885\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:40:57', null);
INSERT INTO `log_pay` VALUES ('247530859036677', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280886', '201809282240591920840882421', '238', '3', '105', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282240591920840882421\",\"totalFee\":\"23800\",\"randStr\":\"ba134fc97e45cfe4b07248bafa43e291\",\"sign\":\"afba20fea52143c7fa01641a56358ff8\",\"endtime\":\"1538145678\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|105|3|238|18831100280886|EEF9EB74778EE0A84F1E607F63A535CA\",\"cpOrderId\":\"18831100280886\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:41:19', null);
INSERT INTO `log_pay` VALUES ('247530859036678', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280887', '201809282241231506240724946', '328', '3', '106', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282241231506240724946\",\"totalFee\":\"32800\",\"randStr\":\"89c4633dab8d54baf405c8d241019361\",\"sign\":\"954274d7dcd57c06c94280dfee0abf7a\",\"endtime\":\"1538145695\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|106|3|328|18831100280887|AE7FDFE7CC3316F0BB014C6A31F7F041\",\"cpOrderId\":\"18831100280887\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:41:36', null);
INSERT INTO `log_pay` VALUES ('247530859036679', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280888', '201809282241371989085226664', '648', '3', '107', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282241371989085226664\",\"totalFee\":\"64800\",\"randStr\":\"1b473260eaf2510b2b3a7da672bf2bf0\",\"sign\":\"bfa14df11d977da0b4049602a9fd0e33\",\"endtime\":\"1538145751\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|107|3|648|18831100280888|E2F40EAD341581745EB3605CB9A570F5\",\"cpOrderId\":\"18831100280888\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:42:46', null);
INSERT INTO `log_pay` VALUES ('247530859036680', '1238913617925', '18831099789316', 'donghai_0', 'donghai_0001', '18831100280889', '201809282242161292475623486', '648', '3', '107', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809282242161292475623486\",\"totalFee\":\"64800\",\"randStr\":\"8599ff51e5d912e96f568b718197b99d\",\"sign\":\"bf4c9eea45248039b79103c855c5f4a6\",\"endtime\":\"1538145766\",\"orderStatus\":\"1\",\"customInfo\":\"1238913617925|donghai_0001|18831099789316|107|3|648|18831100280889|CDA8B2BFFDD6A7950356A7C5AD9C2935\",\"cpOrderId\":\"18831100280889\",\"userId\":\"793\",\"platform\":\"1\"}', '0', '2018-09-28 22:42:46', null);
INSERT INTO `log_pay` VALUES ('1144689282875393', '549', '18789373378561', null, 'donghai_0001', '18789374263297', '201809131735041922206571520', '6', '3', '1_101', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809131735041922206571520\",\"totalFee\":\"600\",\"randStr\":\"4bd65f44d4d796391174859633f1f166\",\"sign\":\"bf42f84a5ace9a877da445e39273f3cd\",\"endtime\":\"1536831358\",\"orderStatus\":\"1\",\"customInfo\":\"549|donghai_0001|18789373378561|101|3|6|125DEEAD34F75A04D9AC336AFAA8D7E4\",\"cpOrderId\":\"18789374263297\",\"userId\":\"549\",\"platform\":\"1\"}', '0', '2018-09-13 17:35:59', null);
INSERT INTO `log_pay` VALUES ('1144689282875394', '549', '18789373378561', null, 'donghai_0001', '18789374263298', '201809131736111552116771362', '168', '3', '1_104', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809131736111552116771362\",\"totalFee\":\"16800\",\"randStr\":\"1b43e71705c47197dd3a35f4f0e61a93\",\"sign\":\"b8391c393a20411de84ed47109673e90\",\"endtime\":\"1536831378\",\"orderStatus\":\"1\",\"customInfo\":\"549|donghai_0001|18789373378561|104|3|168|39477CEB3EF259A0F520172D4201ACBB\",\"cpOrderId\":\"18789374263298\",\"userId\":\"549\",\"platform\":\"1\"}', '0', '2018-09-13 17:36:19', null);
INSERT INTO `log_pay` VALUES ('1144689282875395', '549', '18789373378561', null, 'donghai_0001', '18789374263300', '201809141146441789397493745', '6', '3', '1_101', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809141146441789397493745\",\"totalFee\":\"600\",\"randStr\":\"3d768fb18825d050327a09b2aa8af69d\",\"sign\":\"2cc32bedef965716cdfa766c55213392\",\"endtime\":\"1536896825\",\"orderStatus\":\"1\",\"customInfo\":\"549|donghai_0001|18789373378561|101|3|6|125DEEAD34F75A04D9AC336AFAA8D7E4\",\"cpOrderId\":\"18789374263300\",\"userId\":\"549\",\"platform\":\"1\"}', '0', '2018-09-14 11:47:06', null);
INSERT INTO `log_pay` VALUES ('1144689282875396', '633', '18789373378566', null, 'donghai_0001', '18789374263303', '201809171828301674141444341', '6', '3', '1_101', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171828301674141444341\",\"totalFee\":\"600\",\"randStr\":\"631b198fd6a7c0f2a8352a707fe778ff\",\"sign\":\"ed842d3116280923a1bb10fc16db0ea4\",\"endtime\":\"1537180129\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|101|3|6|9E74E95FDEF1E14D14A289CD833A02EE\",\"cpOrderId\":\"18789374263303\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:28:49', null);
INSERT INTO `log_pay` VALUES ('1144689282875397', '633', '18789373378566', null, 'donghai_0001', '18789374263304', '201809171829011234978872745', '30', '3', '1_102', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171829011234978872745\",\"totalFee\":\"3000\",\"randStr\":\"714111f5a17b4f64996fa18835b50301\",\"sign\":\"3e32eadaf8c018886640dce536e4a56f\",\"endtime\":\"1537180154\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|102|3|30|B6A4BD8D0F2F593E9D99C79F5827CEE8\",\"cpOrderId\":\"18789374263304\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:29:15', null);
INSERT INTO `log_pay` VALUES ('1144689282875398', '633', '18789373378566', null, 'donghai_0001', '18789374263305', '201809171829171763347908868', '98', '3', '1_103', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171829171763347908868\",\"totalFee\":\"9800\",\"randStr\":\"254aaaea19e56089c6915e2de0d515f4\",\"sign\":\"f748bd88475737e2b70477f00018b51b\",\"endtime\":\"1537180171\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|103|3|98|7A02537A8F6ECEC570312D735618F5B8\",\"cpOrderId\":\"18789374263305\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:29:32', null);
INSERT INTO `log_pay` VALUES ('1144689282875399', '633', '18789373378566', null, 'donghai_0001', '18789374263306', '201809171829341075120715588', '168', '3', '1_104', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171829341075120715588\",\"totalFee\":\"16800\",\"randStr\":\"24e0e197aeace404697aee0413efeb85\",\"sign\":\"810ff4d95b1bd259dce4970495ba33de\",\"endtime\":\"1537180181\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|104|3|168|F139588489D11976559288CB4435ABDA\",\"cpOrderId\":\"18789374263306\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:29:42', null);
INSERT INTO `log_pay` VALUES ('1144689282875400', '633', '18789373378566', null, 'donghai_0001', '18789374263307', '201809171829481180589377891', '238', '3', '1_105', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171829481180589377891\",\"totalFee\":\"23800\",\"randStr\":\"13fd1b625588a2b7ae8b76ecd4717297\",\"sign\":\"74b83f46cb13f5a37d9ea1366b98d237\",\"endtime\":\"1537180198\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|105|3|238|C7C4D153DB37AC2983A52E8218083766\",\"cpOrderId\":\"18789374263307\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:29:58', null);
INSERT INTO `log_pay` VALUES ('1144689282875401', '633', '18789373378566', null, 'donghai_0001', '18789374263308', '201809171830021348424911633', '328', '3', '1_106', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171830021348424911633\",\"totalFee\":\"32800\",\"randStr\":\"a4a35c1b86f59602139cf81f0de09675\",\"sign\":\"9d04c43e958b1ed75be5c233391a2241\",\"endtime\":\"1537180209\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|106|3|328|20D8E281936BB9D3937A7286E100FDD0\",\"cpOrderId\":\"18789374263308\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:30:10', null);
INSERT INTO `log_pay` VALUES ('1144689282875402', '633', '18789373378566', null, 'donghai_0001', '18789374263309', '201809171830121254523854865', '648', '3', '1_107', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171830121254523854865\",\"totalFee\":\"64800\",\"randStr\":\"a6ac60e5360a701d79a76770f66c5f32\",\"sign\":\"654a8d4be7827be1822371c2813bfa76\",\"endtime\":\"1537180223\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|107|3|648|E9150E32E1C6A649C7A78153E0F96E9E\",\"cpOrderId\":\"18789374263309\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:30:23', null);
INSERT INTO `log_pay` VALUES ('1144689282875403', '633', '18789373378566', null, 'donghai_0001', '18789374263310', '201809171851141042635346571', '6', '3', '1_101', '{\"gameId\":\"86\",\"subGameId\":\"149\",\"orderId\":\"201809171851141042635346571\",\"totalFee\":\"600\",\"randStr\":\"1be6312902aa09b0872da4f944872fb5\",\"sign\":\"11e94178ed0abcb1e79e820fa0977a27\",\"endtime\":\"1537181489\",\"orderStatus\":\"1\",\"customInfo\":\"633|donghai_0001|18789373378566|101|3|6|9E74E95FDEF1E14D14A289CD833A02EE\",\"cpOrderId\":\"18789374263310\",\"userId\":\"633\",\"platform\":\"1\"}', '0', '2018-09-17 18:51:31', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of t_opt_log
-- ----------------------------
INSERT INTO `t_opt_log` VALUES ('1', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 15:45:05');
INSERT INTO `t_opt_log` VALUES ('2', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:24:05');
INSERT INTO `t_opt_log` VALUES ('3', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:31:05');
INSERT INTO `t_opt_log` VALUES ('4', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:32:05');
INSERT INTO `t_opt_log` VALUES ('5', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:33:05');
INSERT INTO `t_opt_log` VALUES ('6', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:36:05');
INSERT INTO `t_opt_log` VALUES ('7', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:42:05');
INSERT INTO `t_opt_log` VALUES ('8', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:44:05');
INSERT INTO `t_opt_log` VALUES ('9', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:45:05');
INSERT INTO `t_opt_log` VALUES ('10', '1', 'qidian', '1001', 'null', 'qidian封了192.168.1.200', 'qidian封了192.168.1.200', '2018-09-04 16:46:05');
INSERT INTO `t_opt_log` VALUES ('11', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-04 16:51:05');
INSERT INTO `t_opt_log` VALUES ('12', '1', 'qidian', '1001', 'null', 'qidian封了192.168.1.200', 'qidian封了192.168.1.200', '2018-09-04 16:51:05');
INSERT INTO `t_opt_log` VALUES ('13', '1', 'qidian', '1001', 'null', 'qidian解除了192.168.1.200', 'qidian解除了192.168.1.200', '2018-09-04 16:51:05');
INSERT INTO `t_opt_log` VALUES ('14', '3', 'yunying', '1', 'null', '用户登陆', 'yunying登陆了', '2018-09-04 16:54:05');
INSERT INTO `t_opt_log` VALUES ('15', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-06 17:37:05');
INSERT INTO `t_opt_log` VALUES ('16', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-26 18:04:05');

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
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'qidian', 'ken123', 'donghai', 'donghai', '1', '1', '0', '2017-08-12 18:20:22');
INSERT INTO `t_user_dataanalysis` VALUES ('2', 'admin', 'xh@2018', 'donghai', 'donghai', '1', '1', '0', '2017-08-18 17:23:02');
INSERT INTO `t_user_dataanalysis` VALUES ('3', 'yunying', '123456', 'donghai', 'donghai', '1', '1', '0', '2018-09-04 16:51:54');

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
INSERT INTO `t_user_gcc` VALUES ('1', 'qidian', 'ken123', 'donghai', 'donghai', '1', '1', '0', '2016-05-18 21:22:05');
INSERT INTO `t_user_gcc` VALUES ('2', 'admin', '123456', 'donghai', 'donghai', '2', '1', '0', '2017-07-25 18:59:36');
INSERT INTO `t_user_gcc` VALUES ('3', 'yunying', '123456', 'donghai', 'donghai', '3', '1', '0', '2017-07-25 19:00:43');
INSERT INTO `t_user_gcc` VALUES ('4', 'jishu', '123456', 'donghai', 'donghai', '4', '1', '0', '2017-07-25 19:01:35');
INSERT INTO `t_user_gcc` VALUES ('5', 'yunwei', '123456', 'donghai', 'donghai', '5', '1', '0', '2017-07-25 19:01:57');
