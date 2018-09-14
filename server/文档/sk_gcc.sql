/*
Navicat MySQL Data Transfer

Source Server         : 外网正式服101.132.109.250
Source Server Version : 50173
Source Host           : 101.132.109.250:3306
Source Database       : sk_gcc

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-12-19 20:38:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(64) NOT NULL COMMENT '账号',
  `passWord` varchar(64) NOT NULL DEFAULT '123456' COMMENT '密码',
  `telephone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `tourist` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否游客  1：是 ',
  `serverListStr` varchar(255) DEFAULT NULL COMMENT '已创号的服务器列表',
  `createTime` datetime NOT NULL COMMENT '充值时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=10866 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='账号表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('10000', 'a8d56326d3a0a70033c943d901162c18488f2492', '123456', null, '1', null, '2017-09-15 11:11:50', null);
INSERT INTO `account` VALUES ('10001', '4d5a9e6aba39443c117e4985a074a753068b83fc', '123456', null, '1', null, '2017-09-15 11:16:11', null);
INSERT INTO `account` VALUES ('10002', 'd16f3429cc4725d13af62501c7076344', '123456', null, '1', null, '2017-09-15 12:56:48', null);
INSERT INTO `account` VALUES ('10003', '5818901905d9a94dd5e373d1a5700b5f', '123456', null, '1', null, '2017-09-15 13:16:52', null);
INSERT INTO `account` VALUES ('10004', '13603083313', '123456', null, '0', null, '2017-09-15 14:47:24', null);
INSERT INTO `account` VALUES ('10005', '16a3839d6d889cde3cb24bd7cb7372cc', '123456', null, '1', null, '2017-09-15 16:16:04', null);
INSERT INTO `account` VALUES ('10006', '13144024634', '123456', null, '0', null, '2017-09-16 09:14:41', null);
INSERT INTO `account` VALUES ('10007', '13812345678', '123456', null, '0', '[1]', '2017-09-16 09:15:26', null);
INSERT INTO `account` VALUES ('10008', '18006077533', '123456', '18006077533', '0', null, '2017-09-16 09:33:48', null);
INSERT INTO `account` VALUES ('10009', '18213511637', 'duanran', null, '0', null, '2017-09-16 09:36:15', null);
INSERT INTO `account` VALUES ('10010', '15959489715', '3344327', null, '0', null, '2017-09-16 09:36:18', null);
INSERT INTO `account` VALUES ('10011', '13159262516', '123456789', '13159262516', '0', '[1]', '2017-09-16 09:38:32', null);
INSERT INTO `account` VALUES ('10012', '15060526220', '123456', null, '0', null, '2017-09-16 09:38:43', null);
INSERT INTO `account` VALUES ('10013', '13417092230', '123456', null, '0', null, '2017-09-16 09:57:45', null);
INSERT INTO `account` VALUES ('10014', '13812345677', '123456', null, '0', null, '2017-09-16 14:04:55', null);
INSERT INTO `account` VALUES ('10015', '13850404259', '3344327', null, '0', null, '2017-09-16 14:10:54', null);
INSERT INTO `account` VALUES ('10016', '13779923303', '123456', null, '0', '[1]', '2017-09-16 15:26:36', null);
INSERT INTO `account` VALUES ('10017', '13800005200', 'mmmm1234', null, '0', null, '2017-09-18 10:19:42', null);
INSERT INTO `account` VALUES ('10018', '15019232538', '123456', null, '0', null, '2017-09-18 10:28:31', null);
INSERT INTO `account` VALUES ('10019', '18906968967', 'mmmm1234', '18906968967', '0', null, '2017-09-18 10:29:42', null);
INSERT INTO `account` VALUES ('10020', '13954366775', '123456', null, '0', null, '2017-09-18 10:51:18', null);
INSERT INTO `account` VALUES ('10021', '18CF62D9-B9B7-4BA1-8BCF-3FDF4BC67500', '123456', null, '1', null, '2017-09-18 14:19:30', null);
INSERT INTO `account` VALUES ('10022', '15012451813', '123456', null, '0', null, '2017-09-18 14:54:35', null);
INSERT INTO `account` VALUES ('10023', 'd95a9914c4145922fda24f9a755a25ff', '123456', null, '1', null, '2017-09-19 14:40:18', null);
INSERT INTO `account` VALUES ('10024', '18926460201', '123456789', null, '0', null, '2017-09-20 09:08:21', null);
INSERT INTO `account` VALUES ('10025', '9EE65BD7-DB58-5E70-BDB6-035FB97FBB15', '123456', null, '1', '[1]', '2017-09-23 09:44:03', null);
INSERT INTO `account` VALUES ('10026', '75547edf5776a0ba4ba1d547498efac4', '123456', null, '1', '[1]', '2017-09-23 16:44:34', null);
INSERT INTO `account` VALUES ('10027', '18396298356', '337032', null, '0', '[1]', '2017-09-24 23:07:46', null);
INSERT INTO `account` VALUES ('10028', '15060000000', '123456', null, '0', '[1]', '2017-09-26 09:09:13', null);
INSERT INTO `account` VALUES ('10029', '13800000103', '3344327', null, '0', '[1]', '2017-09-26 09:10:08', null);
INSERT INTO `account` VALUES ('10030', '15967565111', '123456', null, '0', '[1]', '2017-09-26 09:12:47', null);
INSERT INTO `account` VALUES ('10031', '13333337596', '123456', null, '0', '[1]', '2017-09-26 09:16:28', null);
INSERT INTO `account` VALUES ('10032', '13159232191', '123456', null, '0', '[1]', '2017-09-26 09:17:28', null);
INSERT INTO `account` VALUES ('10033', '13333333759', '123456', null, '0', '[1]', '2017-09-26 09:18:06', null);
INSERT INTO `account` VALUES ('10034', '65ca0a34d0a4077c99637e5de2800aae', '123456', null, '1', '[1]', '2017-09-27 14:58:21', null);
INSERT INTO `account` VALUES ('10035', '6b7577e55afcd15c6dda9798c5bfdcf7', '123456', null, '1', '[1]', '2017-09-27 17:31:27', null);
INSERT INTO `account` VALUES ('10036', '18856745961', '123456', null, '0', '[1]', '2017-09-28 09:50:24', null);
INSERT INTO `account` VALUES ('10037', 'ccb03956f9f9197f4fa347d54a6dd4b1', '123456', null, '1', '[1]', '2017-09-28 12:55:42', null);
INSERT INTO `account` VALUES ('10038', '13568745924', '123456', null, '0', '[1]', '2017-09-28 15:10:13', null);
INSERT INTO `account` VALUES ('10039', '9e0d3c04cdb4917d161acf74e875fce6', '123456', null, '1', '[1]', '2017-09-28 16:06:36', null);
INSERT INTO `account` VALUES ('10040', 'ca62d14d684aab23557fd2c53ae865d2', '123456', null, '1', '[1]', '2017-09-28 19:25:46', null);
INSERT INTO `account` VALUES ('10041', '703e95424428b6dd0740c60c0fc09352', '123456', null, '1', null, '2017-09-28 20:15:29', null);
INSERT INTO `account` VALUES ('10042', 'cef3596ca433404ada3416704ea44a48', '123456', null, '1', '[1]', '2017-09-28 21:29:42', null);
INSERT INTO `account` VALUES ('10043', '4b9846001bafc040610a98f9063d196d', '123456', null, '1', '[1]', '2017-09-29 11:24:17', null);
INSERT INTO `account` VALUES ('10044', '4cb2ac7b02e600839c09c2cc2d0ba3bc', '123456', null, '1', '[1]', '2017-09-29 22:14:06', null);
INSERT INTO `account` VALUES ('10045', 'd9b14d8408f338448dcd5949d52342c6', '123456', null, '1', '[1]', '2017-10-02 11:56:36', null);
INSERT INTO `account` VALUES ('10046', '0de09d8905a87afb1a2f5882685b1b37', '123456', null, '1', '[1]', '2017-10-02 15:56:33', null);
INSERT INTO `account` VALUES ('10047', 'c4182f354ce9823882aee899b80bb531', '123456', null, '1', '[1]', '2017-10-03 22:32:50', null);
INSERT INTO `account` VALUES ('10048', 'ff46999a319707bce768879406502c86', '123456', null, '1', '[1]', '2017-10-04 11:25:02', null);
INSERT INTO `account` VALUES ('10049', 'f8a46da46793bdbac8b3c9a45990960c', '123456', null, '1', '[1]', '2017-10-05 15:51:10', null);
INSERT INTO `account` VALUES ('10050', '5eb4fe52b8d3a78379782addfdc84211', '123456', null, '1', '[1]', '2017-10-06 18:04:09', null);
INSERT INTO `account` VALUES ('10051', 'f55ae58f468a7ef8d8196402f8cac536', '123456', null, '1', '[1]', '2017-10-08 13:30:59', null);
INSERT INTO `account` VALUES ('10052', '84deb8ae6a92ba8019b59028bcf1fde5', '123456', null, '1', '[1]', '2017-10-08 13:48:40', null);
INSERT INTO `account` VALUES ('10053', 'a79e7cf0ea75b8789b11a3bcd5673f99', '123456', null, '1', '[1]', '2017-10-09 15:28:02', null);
INSERT INTO `account` VALUES ('10054', '45592e70db5b0e98091dc624295d907a', '123456', null, '1', '[1]', '2017-10-09 16:14:57', null);
INSERT INTO `account` VALUES ('10055', 'aba30dbf3433ab1476d483b217c4a783', '123456', null, '1', '[1]', '2017-10-09 18:17:02', null);
INSERT INTO `account` VALUES ('10056', '15368524586', '123456', null, '0', '[1]', '2017-10-09 21:13:00', null);
INSERT INTO `account` VALUES ('10057', '15235866845', '123456', null, '0', '[1]', '2017-10-09 21:47:17', null);
INSERT INTO `account` VALUES ('10058', '15019232539', '123456', null, '0', '[1]', '2017-10-11 11:35:24', null);
INSERT INTO `account` VALUES ('10059', '90fa20c28678e41db852ba926f22336f', '123456', null, '1', '[1]', '2017-10-11 15:53:48', null);
INSERT INTO `account` VALUES ('10060', '15011111111', '3324041', null, '0', '[1]', '2017-10-11 16:20:51', null);
INSERT INTO `account` VALUES ('10061', '15000000000', '3324041', null, '0', '[1]', '2017-10-11 16:47:44', null);
INSERT INTO `account` VALUES ('10062', '15022222222', '3324041', null, '0', '[1]', '2017-10-11 17:04:57', null);
INSERT INTO `account` VALUES ('10063', '15044444444', '3324041', null, '0', '[1]', '2017-10-11 17:12:54', null);
INSERT INTO `account` VALUES ('10064', '15055555555', '3324041', null, '0', '[1]', '2017-10-11 17:15:36', null);
INSERT INTO `account` VALUES ('10065', '15066666666', '3324041', null, '0', '[1]', '2017-10-11 17:17:48', null);
INSERT INTO `account` VALUES ('10066', '15033333333', '3324041', null, '0', '[1]', '2017-10-11 17:18:33', null);
INSERT INTO `account` VALUES ('10067', '15077777777', '3324041', null, '0', '[1]', '2017-10-11 17:19:55', null);
INSERT INTO `account` VALUES ('10068', '15088888888', '3324041', null, '0', '[1]', '2017-10-11 17:22:11', null);
INSERT INTO `account` VALUES ('10069', '15099999999', '3324041', null, '0', '[1]', '2017-10-11 17:24:24', null);
INSERT INTO `account` VALUES ('10070', '18059836543', '3324041', null, '0', '[1]', '2017-10-11 18:11:05', null);
INSERT INTO `account` VALUES ('10071', '13466559944', '123456', null, '0', '[1]', '2017-10-12 10:21:14', null);
INSERT INTO `account` VALUES ('10072', '15100000000', '3324041', null, '0', '[1]', '2017-10-12 14:03:56', null);
INSERT INTO `account` VALUES ('10073', '15111111111', '3324041', null, '0', '[1]', '2017-10-12 14:06:33', null);
INSERT INTO `account` VALUES ('10074', '15122222222', '3324041', null, '0', '[1]', '2017-10-12 14:08:46', null);
INSERT INTO `account` VALUES ('10075', '15133333333', '3324041', null, '0', '[1]', '2017-10-12 14:10:44', null);
INSERT INTO `account` VALUES ('10076', '15144444444', '3324041', null, '0', '[1]', '2017-10-12 14:12:48', null);
INSERT INTO `account` VALUES ('10077', '15155555555', '3324041', null, '0', '[1]', '2017-10-12 14:14:49', null);
INSERT INTO `account` VALUES ('10078', '15166666666', '3324041', null, '0', '[1]', '2017-10-12 14:16:53', null);
INSERT INTO `account` VALUES ('10079', '15177777777', '3324041', null, '0', '[1]', '2017-10-12 14:19:09', null);
INSERT INTO `account` VALUES ('10080', '15188888888', '3324041', null, '0', '[1]', '2017-10-12 14:22:04', null);
INSERT INTO `account` VALUES ('10081', '15199999999', '3324041', null, '0', '[1]', '2017-10-12 14:24:11', null);
INSERT INTO `account` VALUES ('10082', '17805980363', '123456', null, '0', '[1]', '2017-10-12 16:41:58', null);
INSERT INTO `account` VALUES ('10083', '57055cbfd4bd91202386fddbbf0b2fe2', '123456', null, '1', '[1]', '2017-10-12 17:03:44', null);
INSERT INTO `account` VALUES ('10084', '8d57a90e404df4bd7aa74ca98e481861', '123456', null, '1', '[1]', '2017-10-12 20:36:30', null);
INSERT INTO `account` VALUES ('10085', '13812345682', '123456', null, '0', '[1]', '2017-10-13 08:52:00', null);
INSERT INTO `account` VALUES ('10086', '13546546546', '123456', null, '0', '[1]', '2017-10-13 08:58:07', null);
INSERT INTO `account` VALUES ('10087', '13012345678', '123456', null, '0', '[1]', '2017-10-13 09:01:52', null);
INSERT INTO `account` VALUES ('10088', '18213511638', '123456', null, '0', '[1]', '2017-10-13 09:03:47', null);
INSERT INTO `account` VALUES ('10089', '13159262511', '123456', null, '0', '[1]', '2017-10-13 09:05:44', null);
INSERT INTO `account` VALUES ('10090', '13500000011', '123456', null, '0', '[1]', '2017-10-13 09:05:55', null);
INSERT INTO `account` VALUES ('10091', '13801234567', '123456', null, '0', '[1]', '2017-10-13 09:08:34', null);
INSERT INTO `account` VALUES ('10092', '18001234567', '123456', null, '0', '[1]', '2017-10-13 09:17:25', null);
INSERT INTO `account` VALUES ('10093', '13340000056', '123456', null, '0', '[1]', '2017-10-13 09:23:34', null);
INSERT INTO `account` VALUES ('10094', '18927524235', 'jackal09ai', null, '0', '[1]', '2017-10-13 09:31:32', null);
INSERT INTO `account` VALUES ('10095', '18927521111', '870609', null, '0', '[1]', '2017-10-13 11:18:59', null);
INSERT INTO `account` VALUES ('10096', '93425a7c9c323eca18f59ae0d3207376', '123456', null, '1', '[1]', '2017-10-13 14:41:59', null);
INSERT INTO `account` VALUES ('10097', '014df6c5b53c96403dfdae075dd3c231', '123456', null, '1', '[1]', '2017-10-14 16:19:56', null);
INSERT INTO `account` VALUES ('10098', '11fc15e0d2da4d7529ecc72b06a46f04', '123456', null, '1', '[1]', '2017-10-16 10:46:36', null);
INSERT INTO `account` VALUES ('10099', 'f1694dca9ad94c155c6b084deefafa68', '123456', null, '1', '[1]', '2017-10-16 15:55:36', null);
INSERT INTO `account` VALUES ('10100', '41e13e21b8ea21f39ee30a8f87d261f8', '123456', null, '1', '[1]', '2017-10-16 16:29:58', null);
INSERT INTO `account` VALUES ('10101', '58d524c6e1f6dd8612f45cfba56798f0', '123456', null, '1', '[1]', '2017-10-16 16:33:10', null);
INSERT INTO `account` VALUES ('10102', '3b5ba4cd76dc7001e3e1244e42c5d3ef', '123456', null, '1', '[1]', '2017-10-16 16:37:35', null);
INSERT INTO `account` VALUES ('10103', 'a8d0d85130e913f105a545de93d8ebe2', '123456', null, '1', '[1]', '2017-10-17 13:20:10', null);
INSERT INTO `account` VALUES ('10104', '18810572752', 'zhao19870911', null, '0', '[1]', '2017-10-17 16:21:55', null);
INSERT INTO `account` VALUES ('10105', '0ee8d5b4975eb4c1c0535f5a629ddd51', '123456', null, '1', '[1]', '2017-10-17 16:39:24', null);
INSERT INTO `account` VALUES ('10106', 'c8109c6c01dc30af14e1a9b098ed9da0', '123456', null, '1', '[1]', '2017-10-17 21:15:07', null);
INSERT INTO `account` VALUES ('10107', '1147a4710009511f2eff67b1a3a1258e', '123456', null, '1', '[1]', '2017-10-17 23:01:18', null);
INSERT INTO `account` VALUES ('10108', '5dcbecd7c03e70a5a0aa49c845ef484e', '123456', null, '1', null, '2017-10-17 23:04:12', null);
INSERT INTO `account` VALUES ('10109', '17863643166', '123456', '17863643166', '0', '[1]', '2017-10-17 23:04:56', null);
INSERT INTO `account` VALUES ('10110', '18691642330', 'yulin&caidie', null, '0', '[1]', '2017-10-18 00:55:09', null);
INSERT INTO `account` VALUES ('10111', '13106046785', '98137563QW', null, '0', '[1]', '2017-10-18 07:39:05', null);
INSERT INTO `account` VALUES ('10112', '7a2e905c000124257eeb730db664e5d7', '123456', null, '1', '[1]', '2017-10-18 09:58:40', null);
INSERT INTO `account` VALUES ('10113', 'c4fe912a86971f53080cb018d9528f21', '123456', null, '1', '[1]', '2017-10-18 10:21:26', null);
INSERT INTO `account` VALUES ('10114', 'f5a1cf527e2077a43d7f76f36d8f05f3', '123456', null, '1', '[1]', '2017-10-18 10:51:56', null);
INSERT INTO `account` VALUES ('10115', '18002361737', '58983060', '18002361737', '0', '[1]', '2017-10-18 10:57:42', null);
INSERT INTO `account` VALUES ('10116', '892ee8946a31530df74be0069f32cc84', '123456', null, '1', '[1]', '2017-10-18 11:08:20', null);
INSERT INTO `account` VALUES ('10117', 'f941ecd016d6353c340db29da8338275', '123456', null, '1', '[1]', '2017-10-18 11:25:33', null);
INSERT INTO `account` VALUES ('10118', '13958904180', 'sr19881220', '13958904180', '0', '[1]', '2017-10-18 11:38:24', null);
INSERT INTO `account` VALUES ('10119', '7f1bfdbfd089f4f3abd59da907863768', '123456', null, '1', '[1]', '2017-10-18 14:04:52', null);
INSERT INTO `account` VALUES ('10120', '4a8feac87a21e60d21550fffc255e7b2', '123456', null, '1', '[1]', '2017-10-18 14:25:38', null);
INSERT INTO `account` VALUES ('10121', 'e17b8fb1e1813b4d88c08565c16e3391', '123456', null, '1', '[1]', '2017-10-18 14:34:24', null);
INSERT INTO `account` VALUES ('10122', '13721848956', '1314520', null, '0', '[1]', '2017-10-18 17:22:45', null);
INSERT INTO `account` VALUES ('10123', '15501111183', 'qwerty', null, '0', '[1]', '2017-10-18 17:25:33', null);
INSERT INTO `account` VALUES ('10124', '15277303997', '15277303997', null, '0', '[1]', '2017-10-18 17:39:37', null);
INSERT INTO `account` VALUES ('10125', '15541228181', '5219674264.A', null, '0', '[1]', '2017-10-18 18:04:27', null);
INSERT INTO `account` VALUES ('10126', '42c75509aab152e9f98270991542fef9', '123456', null, '1', '[1]', '2017-10-18 18:50:18', null);
INSERT INTO `account` VALUES ('10127', '8b52a5217a11185511173367a5b0b9dd', '123456', null, '1', '[1]', '2017-10-18 22:08:43', null);
INSERT INTO `account` VALUES ('10128', 'aadc73d8ca7bfea0514ff84e0c69c6a8', '123456', null, '1', '[1]', '2017-10-18 22:56:02', null);
INSERT INTO `account` VALUES ('10129', '43b4470f0b036adbe5b5bda7cb145e65', '123456', null, '1', '[1]', '2017-10-19 17:43:03', null);
INSERT INTO `account` VALUES ('10130', '13958905624', 'sr19881220', null, '0', '[1]', '2017-10-20 10:44:03', null);
INSERT INTO `account` VALUES ('10131', '41fad79ba4a3986e3a7da0ef70cae614', '123456', null, '1', '[1]', '2017-10-21 18:00:43', null);
INSERT INTO `account` VALUES ('10132', '18682681113', '1q2w3e4r5t', null, '0', '[1]', '2017-10-21 18:05:33', null);
INSERT INTO `account` VALUES ('10133', '18777837303', '334461', null, '0', '[1]', '2017-10-22 09:33:20', null);
INSERT INTO `account` VALUES ('10134', '15933816763', 'li19871124', '15933816763', '0', '[1]', '2017-10-23 14:31:23', null);
INSERT INTO `account` VALUES ('10135', '13213370267', '123456', null, '0', '[1]', '2017-10-23 20:51:05', null);
INSERT INTO `account` VALUES ('10136', '04dc8a824272d7974ff37a971efbae70', '123456', null, '1', '[1]', '2017-10-23 21:48:18', null);
INSERT INTO `account` VALUES ('10137', '102cbeba47c962e914f27bd057f82231', '123456', null, '1', '[1]', '2017-10-23 21:51:57', null);
INSERT INTO `account` VALUES ('10138', '1232f63bf13d4b05d00693e58bd2b984', '123456', null, '1', '[1]', '2017-10-23 21:54:33', null);
INSERT INTO `account` VALUES ('10139', '15122161831', '6821495', null, '0', '[1]', '2017-10-24 02:25:46', null);
INSERT INTO `account` VALUES ('10140', '13820000361', '123456', null, '0', '[1]', '2017-10-24 10:30:57', null);
INSERT INTO `account` VALUES ('10141', '13820000362', '123456', null, '0', '[1]', '2017-10-24 10:32:36', null);
INSERT INTO `account` VALUES ('10142', 'b64da38ee41230501186f48befac5023', '123456', null, '1', '[1]', '2017-10-24 10:34:15', null);
INSERT INTO `account` VALUES ('10143', '13820000363', '123456', null, '0', '[1]', '2017-10-24 10:34:19', null);
INSERT INTO `account` VALUES ('10144', '13820000364', '123456', null, '0', '[1]', '2017-10-24 10:35:06', null);
INSERT INTO `account` VALUES ('10145', '13820000365', '123456', null, '0', '[1]', '2017-10-24 10:35:54', null);
INSERT INTO `account` VALUES ('10146', '13820000366', '123456', null, '0', '[1]', '2017-10-24 10:36:37', null);
INSERT INTO `account` VALUES ('10147', '13820000367', '123456', null, '0', '[1]', '2017-10-24 10:37:36', null);
INSERT INTO `account` VALUES ('10148', '13820000368', '123456', null, '0', '[1]', '2017-10-24 10:38:20', null);
INSERT INTO `account` VALUES ('10149', '13820000369', '123456', null, '0', '[1]', '2017-10-24 10:39:03', null);
INSERT INTO `account` VALUES ('10150', '13820000370', '123456', null, '0', '[1]', '2017-10-24 10:39:48', null);
INSERT INTO `account` VALUES ('10151', '13820000371', '123456', null, '0', '[1]', '2017-10-24 10:40:33', null);
INSERT INTO `account` VALUES ('10152', '13820000372', '123456', null, '0', '[1]', '2017-10-24 10:41:19', null);
INSERT INTO `account` VALUES ('10153', '13820000373', '123456', null, '0', '[1]', '2017-10-24 10:42:10', null);
INSERT INTO `account` VALUES ('10154', '13820000374', '123456', null, '0', '[1]', '2017-10-24 10:43:31', null);
INSERT INTO `account` VALUES ('10155', '13820000375', '123456', null, '0', '[1]', '2017-10-24 10:44:15', null);
INSERT INTO `account` VALUES ('10156', '13820000376', '123456', null, '0', '[1]', '2017-10-24 10:45:16', null);
INSERT INTO `account` VALUES ('10157', '13820000377', '123456', null, '0', '[1]', '2017-10-24 10:46:24', null);
INSERT INTO `account` VALUES ('10158', '13820000378', '123456', null, '0', '[1]', '2017-10-24 10:47:13', null);
INSERT INTO `account` VALUES ('10159', '13820000379', '123456', null, '0', '[1]', '2017-10-24 10:47:59', null);
INSERT INTO `account` VALUES ('10160', '13820000380', '123456', null, '0', '[1]', '2017-10-24 10:48:46', null);
INSERT INTO `account` VALUES ('10161', '13820000381', '123456', null, '0', '[1]', '2017-10-24 10:49:30', null);
INSERT INTO `account` VALUES ('10162', '13820000382', '123456', null, '0', '[1]', '2017-10-24 10:50:12', null);
INSERT INTO `account` VALUES ('10163', '13820000383', '123456', null, '0', '[1]', '2017-10-24 10:50:56', null);
INSERT INTO `account` VALUES ('10164', '13820000384', '123456', null, '0', '[1]', '2017-10-24 10:51:41', null);
INSERT INTO `account` VALUES ('10165', '13820000385', '123456', null, '0', '[1]', '2017-10-24 10:52:26', null);
INSERT INTO `account` VALUES ('10166', '13820000386', '123456', null, '0', '[1]', '2017-10-24 10:53:21', null);
INSERT INTO `account` VALUES ('10167', '13820000387', '123456', null, '0', '[1]', '2017-10-24 10:54:20', null);
INSERT INTO `account` VALUES ('10168', '13820000388', '123456', null, '0', '[1]', '2017-10-24 10:55:04', null);
INSERT INTO `account` VALUES ('10169', '13820000389', '123456', null, '0', '[1]', '2017-10-24 10:55:58', null);
INSERT INTO `account` VALUES ('10170', '13820000390', '123456', null, '0', '[1]', '2017-10-24 10:57:07', null);
INSERT INTO `account` VALUES ('10171', '13820000391', '123456', null, '0', '[1]', '2017-10-24 10:57:49', null);
INSERT INTO `account` VALUES ('10172', '13820000392', '123456', null, '0', '[1]', '2017-10-24 10:58:36', null);
INSERT INTO `account` VALUES ('10173', '13820000393', '123456', null, '0', '[1]', '2017-10-24 10:59:22', null);
INSERT INTO `account` VALUES ('10174', '13820000394', '123456', null, '0', '[1]', '2017-10-24 11:00:05', null);
INSERT INTO `account` VALUES ('10175', '13820000395', '123456', null, '0', '[1]', '2017-10-24 11:00:49', null);
INSERT INTO `account` VALUES ('10176', '13820000396', '123456', null, '0', '[1]', '2017-10-24 11:01:35', null);
INSERT INTO `account` VALUES ('10177', '13820000397', '123456', null, '0', '[1]', '2017-10-24 11:02:20', null);
INSERT INTO `account` VALUES ('10178', '13820000398', '123456', null, '0', '[1]', '2017-10-24 11:03:16', null);
INSERT INTO `account` VALUES ('10179', '13820000399', '123456', null, '0', '[1]', '2017-10-24 11:04:23', null);
INSERT INTO `account` VALUES ('10180', '13820000400', '123456', null, '0', '[1]', '2017-10-24 11:05:14', null);
INSERT INTO `account` VALUES ('10181', '13820000316', '123456', null, '0', '[1]', '2017-10-24 11:06:04', null);
INSERT INTO `account` VALUES ('10182', '13820000401', '123456', null, '0', '[1]', '2017-10-24 11:06:07', null);
INSERT INTO `account` VALUES ('10183', '13820000136', '123456', null, '0', '[1]', '2017-10-24 11:06:10', null);
INSERT INTO `account` VALUES ('10184', '13820000402', '123456', null, '0', '[1]', '2017-10-24 11:06:52', null);
INSERT INTO `account` VALUES ('10185', '13820000226', '123456', null, '0', '[1]', '2017-10-24 11:07:30', null);
INSERT INTO `account` VALUES ('10186', '13820000317', '123456', null, '0', '[1]', '2017-10-24 11:07:33', null);
INSERT INTO `account` VALUES ('10187', '13820000137', '123456', null, '0', '[1]', '2017-10-24 11:07:38', null);
INSERT INTO `account` VALUES ('10188', '13820000403', '123456', null, '0', '[1]', '2017-10-24 11:07:40', null);
INSERT INTO `account` VALUES ('10189', '13820000404', '123456', null, '0', '[1]', '2017-10-24 11:08:24', null);
INSERT INTO `account` VALUES ('10190', '13820000318', '123456', null, '0', '[1]', '2017-10-24 11:08:26', null);
INSERT INTO `account` VALUES ('10191', '13820000227', '123456', null, '0', '[1]', '2017-10-24 11:08:26', null);
INSERT INTO `account` VALUES ('10192', '13820000138', '123456', null, '0', '[1]', '2017-10-24 11:08:36', null);
INSERT INTO `account` VALUES ('10193', '13820000405', '123456', null, '0', '[1]', '2017-10-24 11:09:10', null);
INSERT INTO `account` VALUES ('10194', '13820000319', '123456', null, '0', '[1]', '2017-10-24 11:09:16', null);
INSERT INTO `account` VALUES ('10195', '13820000228', '123456', null, '0', '[1]', '2017-10-24 11:09:24', null);
INSERT INTO `account` VALUES ('10196', '13820000139', '123456', null, '0', '[1]', '2017-10-24 11:09:32', null);
INSERT INTO `account` VALUES ('10197', '13820000406', '123456', null, '0', '[1]', '2017-10-24 11:09:57', null);
INSERT INTO `account` VALUES ('10198', '13820000320', '123456', null, '0', '[1]', '2017-10-24 11:10:13', null);
INSERT INTO `account` VALUES ('10199', '13820000140', '123456', null, '0', '[1]', '2017-10-24 11:10:25', null);
INSERT INTO `account` VALUES ('10200', '13820000229', '123456', null, '0', '[1]', '2017-10-24 11:10:35', null);
INSERT INTO `account` VALUES ('10201', '13820000407', '123456', null, '0', '[1]', '2017-10-24 11:10:49', null);
INSERT INTO `account` VALUES ('10202', '13820000321', '123456', null, '0', '[1]', '2017-10-24 11:11:06', null);
INSERT INTO `account` VALUES ('10203', '13820000046', '123456', null, '0', '[1]', '2017-10-24 11:11:32', null);
INSERT INTO `account` VALUES ('10204', '13820000181', '123456', null, '0', '[1]', '2017-10-24 11:11:45', null);
INSERT INTO `account` VALUES ('10205', '13820000408', '123456', null, '0', '[1]', '2017-10-24 11:12:00', null);
INSERT INTO `account` VALUES ('10206', '13820000047', '123456', null, '0', '[1]', '2017-10-24 11:12:02', null);
INSERT INTO `account` VALUES ('10207', '13820000322', '123456', null, '0', '[1]', '2017-10-24 11:12:07', null);
INSERT INTO `account` VALUES ('10208', '13820000048', '123456', null, '0', '[1]', '2017-10-24 11:12:39', null);
INSERT INTO `account` VALUES ('10209', '13820000409', '123456', null, '0', '[1]', '2017-10-24 11:12:47', null);
INSERT INTO `account` VALUES ('10210', '13820000182', '123456', null, '0', '[1]', '2017-10-24 11:12:58', null);
INSERT INTO `account` VALUES ('10211', '13820000323', '123456', null, '0', '[1]', '2017-10-24 11:13:00', null);
INSERT INTO `account` VALUES ('10212', '13820000049', '123456', null, '0', '[1]', '2017-10-24 11:13:23', null);
INSERT INTO `account` VALUES ('10213', '13820000183', '123456', null, '0', '[1]', '2017-10-24 11:13:48', null);
INSERT INTO `account` VALUES ('10214', '13820000050', '123456', null, '0', '[1]', '2017-10-24 11:13:50', null);
INSERT INTO `account` VALUES ('10215', '13820000324', '123456', null, '0', '[1]', '2017-10-24 11:13:53', null);
INSERT INTO `account` VALUES ('10216', '13820000410', '123456', null, '0', '[1]', '2017-10-24 11:13:54', null);
INSERT INTO `account` VALUES ('10217', '13820000051', '123456', null, '0', '[1]', '2017-10-24 11:14:17', null);
INSERT INTO `account` VALUES ('10218', '13820000411', '123456', null, '0', '[1]', '2017-10-24 11:14:38', null);
INSERT INTO `account` VALUES ('10219', '13820000052', '123456', null, '0', '[1]', '2017-10-24 11:14:43', null);
INSERT INTO `account` VALUES ('10220', '13820000230', '123456', null, '0', '[1]', '2017-10-24 11:14:44', null);
INSERT INTO `account` VALUES ('10221', '13820000053', '123456', null, '0', '[1]', '2017-10-24 11:15:11', null);
INSERT INTO `account` VALUES ('10222', '13820000412', '123456', null, '0', '[1]', '2017-10-24 11:15:23', null);
INSERT INTO `account` VALUES ('10223', '13820000325', '123456', null, '0', '[1]', '2017-10-24 11:15:26', null);
INSERT INTO `account` VALUES ('10224', '13820000231', '123456', null, '0', '[1]', '2017-10-24 11:15:31', null);
INSERT INTO `account` VALUES ('10225', '13820000054', '123456', null, '0', '[1]', '2017-10-24 11:15:38', null);
INSERT INTO `account` VALUES ('10226', '13820000184', '123456', null, '0', '[1]', '2017-10-24 11:15:40', null);
INSERT INTO `account` VALUES ('10227', '13820000232', '123456', null, '0', '[1]', '2017-10-24 11:16:02', null);
INSERT INTO `account` VALUES ('10228', '13820000055', '123456', null, '0', '[1]', '2017-10-24 11:16:04', null);
INSERT INTO `account` VALUES ('10229', '13820000185', '123456', null, '0', '[1]', '2017-10-24 11:16:13', null);
INSERT INTO `account` VALUES ('10230', '13820000413', '123456', null, '0', '[1]', '2017-10-24 11:16:14', null);
INSERT INTO `account` VALUES ('10231', '13820000326', '123456', null, '0', '[1]', '2017-10-24 11:16:18', null);
INSERT INTO `account` VALUES ('10232', '13820000233', '123456', null, '0', '[1]', '2017-10-24 11:16:26', null);
INSERT INTO `account` VALUES ('10233', '13820000056', '123456', null, '0', '[1]', '2017-10-24 11:16:30', null);
INSERT INTO `account` VALUES ('10234', '13820000057', '123456', null, '0', '[1]', '2017-10-24 11:16:56', null);
INSERT INTO `account` VALUES ('10235', '13820000234', '123456', null, '0', '[1]', '2017-10-24 11:16:59', null);
INSERT INTO `account` VALUES ('10236', '13820000414', '123456', null, '0', '[1]', '2017-10-24 11:17:00', null);
INSERT INTO `account` VALUES ('10237', '13820000327', '123456', null, '0', '[1]', '2017-10-24 11:17:19', null);
INSERT INTO `account` VALUES ('10238', '13820000235', '123456', null, '0', '[1]', '2017-10-24 11:17:20', null);
INSERT INTO `account` VALUES ('10239', '13820000058', '123456', null, '0', '[1]', '2017-10-24 11:17:30', null);
INSERT INTO `account` VALUES ('10240', '13820000186', '123456', null, '0', '[1]', '2017-10-24 11:17:46', null);
INSERT INTO `account` VALUES ('10241', '13820000059', '123456', null, '0', '[1]', '2017-10-24 11:18:00', null);
INSERT INTO `account` VALUES ('10242', '13820000328', '123456', null, '0', '[1]', '2017-10-24 11:18:15', null);
INSERT INTO `account` VALUES ('10243', '13820000415', '123456', null, '0', '[1]', '2017-10-24 11:18:15', null);
INSERT INTO `account` VALUES ('10244', '13820000187', '123456', null, '0', '[1]', '2017-10-24 11:18:20', null);
INSERT INTO `account` VALUES ('10245', '13820000060', '123456', null, '0', '[1]', '2017-10-24 11:18:27', null);
INSERT INTO `account` VALUES ('10246', '13820000236', '123456', null, '0', '[1]', '2017-10-24 11:18:44', null);
INSERT INTO `account` VALUES ('10247', '13820000061', '123456', null, '0', '[1]', '2017-10-24 11:18:54', null);
INSERT INTO `account` VALUES ('10248', '13820000141', '123456', null, '0', '[1]', '2017-10-24 11:18:59', null);
INSERT INTO `account` VALUES ('10249', '13820000416', '123456', null, '0', '[1]', '2017-10-24 11:19:00', null);
INSERT INTO `account` VALUES ('10250', '13820000237', '123456', null, '0', '[1]', '2017-10-24 11:19:02', null);
INSERT INTO `account` VALUES ('10251', '13820000188', '123456', null, '0', '[1]', '2017-10-24 11:19:06', null);
INSERT INTO `account` VALUES ('10252', '13820000329', '123456', null, '0', '[1]', '2017-10-24 11:19:12', null);
INSERT INTO `account` VALUES ('10253', '13820000062', '123456', null, '0', '[1]', '2017-10-24 11:19:23', null);
INSERT INTO `account` VALUES ('10254', '13820000238', '123456', null, '0', '[1]', '2017-10-24 11:19:36', null);
INSERT INTO `account` VALUES ('10255', '13820000189', '123456', null, '0', '[1]', '2017-10-24 11:19:36', null);
INSERT INTO `account` VALUES ('10256', '13820000142', '123456', null, '0', '[1]', '2017-10-24 11:19:45', null);
INSERT INTO `account` VALUES ('10257', '13820000063', '123456', null, '0', '[1]', '2017-10-24 11:19:50', null);
INSERT INTO `account` VALUES ('10258', '13820000417', '123456', null, '0', '[1]', '2017-10-24 11:19:53', null);
INSERT INTO `account` VALUES ('10259', '13820000239', '123456', null, '0', '[1]', '2017-10-24 11:19:57', null);
INSERT INTO `account` VALUES ('10260', '13820000064', '123456', null, '0', '[1]', '2017-10-24 11:20:17', null);
INSERT INTO `account` VALUES ('10261', '13820000330', '123456', null, '0', '[1]', '2017-10-24 11:20:18', null);
INSERT INTO `account` VALUES ('10262', '13820000143', '123456', null, '0', '[1]', '2017-10-24 11:20:34', null);
INSERT INTO `account` VALUES ('10263', '13820000418', '123456', null, '0', '[1]', '2017-10-24 11:20:37', null);
INSERT INTO `account` VALUES ('10264', '13820000065', '123456', null, '0', '[1]', '2017-10-24 11:20:42', null);
INSERT INTO `account` VALUES ('10265', '13820000190', '123456', null, '0', '[1]', '2017-10-24 11:20:48', null);
INSERT INTO `account` VALUES ('10266', '13820000066', '123456', null, '0', '[1]', '2017-10-24 11:21:11', null);
INSERT INTO `account` VALUES ('10267', '13820000191', '123456', null, '0', '[1]', '2017-10-24 11:21:20', null);
INSERT INTO `account` VALUES ('10268', '13820000144', '123456', null, '0', '[1]', '2017-10-24 11:21:32', null);
INSERT INTO `account` VALUES ('10269', '13820000067', '123456', null, '0', '[1]', '2017-10-24 11:21:39', null);
INSERT INTO `account` VALUES ('10270', '13820000331', '123456', null, '0', '[1]', '2017-10-24 11:21:44', null);
INSERT INTO `account` VALUES ('10271', '13820000419', '123456', null, '0', '[1]', '2017-10-24 11:21:49', null);
INSERT INTO `account` VALUES ('10272', '13820000192', '123456', null, '0', '[1]', '2017-10-24 11:21:50', null);
INSERT INTO `account` VALUES ('10273', '13820000068', '123456', null, '0', '[1]', '2017-10-24 11:22:05', null);
INSERT INTO `account` VALUES ('10274', '13820000145', '123456', null, '0', '[1]', '2017-10-24 11:22:20', null);
INSERT INTO `account` VALUES ('10275', '13820000193', '123456', null, '0', '[1]', '2017-10-24 11:22:20', null);
INSERT INTO `account` VALUES ('10276', '13820000069', '123456', null, '0', '[1]', '2017-10-24 11:22:30', null);
INSERT INTO `account` VALUES ('10277', '13820000332', '123456', null, '0', '[1]', '2017-10-24 11:22:38', null);
INSERT INTO `account` VALUES ('10278', '13820000420', '123456', null, '0', '[1]', '2017-10-24 11:22:41', null);
INSERT INTO `account` VALUES ('10279', '13820000070', '123456', null, '0', '[1]', '2017-10-24 11:22:56', null);
INSERT INTO `account` VALUES ('10280', '13820000194', '123456', null, '0', '[1]', '2017-10-24 11:22:58', null);
INSERT INTO `account` VALUES ('10281', '13820000146', '123456', null, '0', '[1]', '2017-10-24 11:23:06', null);
INSERT INTO `account` VALUES ('10282', '13820000071', '123456', null, '0', '[1]', '2017-10-24 11:23:21', null);
INSERT INTO `account` VALUES ('10283', '13820000195', '123456', null, '0', '[1]', '2017-10-24 11:23:29', null);
INSERT INTO `account` VALUES ('10284', '13820000333', '123456', null, '0', '[1]', '2017-10-24 11:23:30', null);
INSERT INTO `account` VALUES ('10285', '13820000421', '123456', null, '0', '[1]', '2017-10-24 11:23:32', null);
INSERT INTO `account` VALUES ('10286', '13820000072', '123456', null, '0', '[1]', '2017-10-24 11:23:49', null);
INSERT INTO `account` VALUES ('10287', '13820000196', '123456', null, '0', '[1]', '2017-10-24 11:24:03', null);
INSERT INTO `account` VALUES ('10288', '13820000147', '123456', null, '0', '[1]', '2017-10-24 11:24:06', null);
INSERT INTO `account` VALUES ('10289', '13820000073', '123456', null, '0', '[1]', '2017-10-24 11:24:16', null);
INSERT INTO `account` VALUES ('10290', '13820000334', '123456', null, '0', '[1]', '2017-10-24 11:24:23', null);
INSERT INTO `account` VALUES ('10291', '13820000197', '123456', null, '0', '[1]', '2017-10-24 11:24:37', null);
INSERT INTO `account` VALUES ('10292', '13820000074', '123456', null, '0', '[1]', '2017-10-24 11:24:42', null);
INSERT INTO `account` VALUES ('10293', '13820000241', '123456', null, '0', '[1]', '2017-10-24 11:24:42', null);
INSERT INTO `account` VALUES ('10294', '13820000148', '123456', null, '0', '[1]', '2017-10-24 11:24:51', null);
INSERT INTO `account` VALUES ('10295', '13820000240', '123456', null, '0', '[1]', '2017-10-24 11:24:56', null);
INSERT INTO `account` VALUES ('10296', '13820000422', '123456', null, '0', '[1]', '2017-10-24 11:25:00', null);
INSERT INTO `account` VALUES ('10297', '13820000075', '123456', null, '0', '[1]', '2017-10-24 11:25:07', null);
INSERT INTO `account` VALUES ('10298', '13820000335', '123456', null, '0', '[1]', '2017-10-24 11:25:18', null);
INSERT INTO `account` VALUES ('10299', '13820000076', '123456', null, '0', '[1]', '2017-10-24 11:25:32', null);
INSERT INTO `account` VALUES ('10300', '13820000149', '123456', null, '0', '[1]', '2017-10-24 11:25:37', null);
INSERT INTO `account` VALUES ('10301', '13820000423', '123456', null, '0', '[1]', '2017-10-24 11:25:45', null);
INSERT INTO `account` VALUES ('10302', '13820000077', '123456', null, '0', '[1]', '2017-10-24 11:25:58', null);
INSERT INTO `account` VALUES ('10303', '13820000242', '123456', null, '0', '[1]', '2017-10-24 11:26:02', null);
INSERT INTO `account` VALUES ('10304', '13820000336', '123456', null, '0', '[1]', '2017-10-24 11:26:21', null);
INSERT INTO `account` VALUES ('10305', '13820000243', '123456', null, '0', '[1]', '2017-10-24 11:26:21', null);
INSERT INTO `account` VALUES ('10306', '13820000078', '123456', null, '0', '[1]', '2017-10-24 11:26:27', null);
INSERT INTO `account` VALUES ('10307', '13820000424', '123456', null, '0', '[1]', '2017-10-24 11:26:28', null);
INSERT INTO `account` VALUES ('10308', '13820000150', '123456', null, '0', '[1]', '2017-10-24 11:26:42', null);
INSERT INTO `account` VALUES ('10309', '13820000079', '123456', null, '0', '[1]', '2017-10-24 11:26:53', null);
INSERT INTO `account` VALUES ('10310', '13820000198', '123456', null, '0', '[1]', '2017-10-24 11:26:55', null);
INSERT INTO `account` VALUES ('10311', '13820000244', '123456', null, '0', '[1]', '2017-10-24 11:26:55', null);
INSERT INTO `account` VALUES ('10312', '13820000337', '123456', null, '0', '[1]', '2017-10-24 11:27:12', null);
INSERT INTO `account` VALUES ('10313', '13820000425', '123456', null, '0', '[1]', '2017-10-24 11:27:12', null);
INSERT INTO `account` VALUES ('10314', '13820000080', '123456', null, '0', '[1]', '2017-10-24 11:27:18', null);
INSERT INTO `account` VALUES ('10315', '13820000151', '123456', null, '0', '[1]', '2017-10-24 11:27:27', null);
INSERT INTO `account` VALUES ('10316', '13820000199', '123456', null, '0', '[1]', '2017-10-24 11:27:27', null);
INSERT INTO `account` VALUES ('10317', '13820000081', '123456', null, '0', '[1]', '2017-10-24 11:27:44', null);
INSERT INTO `account` VALUES ('10318', '13820000245', '123456', null, '0', '[1]', '2017-10-24 11:27:54', null);
INSERT INTO `account` VALUES ('10319', '13820000426', '123456', null, '0', '[1]', '2017-10-24 11:27:59', null);
INSERT INTO `account` VALUES ('10320', '13820000338', '123456', null, '0', '[1]', '2017-10-24 11:28:02', null);
INSERT INTO `account` VALUES ('10321', '13820000082', '123456', null, '0', '[1]', '2017-10-24 11:28:10', null);
INSERT INTO `account` VALUES ('10322', '13820000152', '123456', null, '0', '[1]', '2017-10-24 11:28:12', null);
INSERT INTO `account` VALUES ('10323', '13820000246', '123456', null, '0', '[1]', '2017-10-24 11:28:16', null);
INSERT INTO `account` VALUES ('10324', '13820000200', '123456', null, '0', '[1]', '2017-10-24 11:28:35', null);
INSERT INTO `account` VALUES ('10325', '13820000083', '123456', null, '0', '[1]', '2017-10-24 11:28:36', null);
INSERT INTO `account` VALUES ('10326', '13820000339', '123456', null, '0', '[1]', '2017-10-24 11:28:52', null);
INSERT INTO `account` VALUES ('10327', '13820000247', '123456', null, '0', '[1]', '2017-10-24 11:28:53', null);
INSERT INTO `account` VALUES ('10328', '13820000427', '123456', null, '0', '[1]', '2017-10-24 11:28:53', null);
INSERT INTO `account` VALUES ('10329', '13820000153', '123456', null, '0', '[1]', '2017-10-24 11:29:00', null);
INSERT INTO `account` VALUES ('10330', '13820000084', '123456', null, '0', '[1]', '2017-10-24 11:29:10', null);
INSERT INTO `account` VALUES ('10331', '13820000248', '123456', null, '0', '[1]', '2017-10-24 11:29:13', null);
INSERT INTO `account` VALUES ('10332', '13820000201', '123456', null, '0', '[1]', '2017-10-24 11:29:28', null);
INSERT INTO `account` VALUES ('10333', '13820000085', '123456', null, '0', '[1]', '2017-10-24 11:29:35', null);
INSERT INTO `account` VALUES ('10334', '13820000428', '123456', null, '0', '[1]', '2017-10-24 11:29:36', null);
INSERT INTO `account` VALUES ('10335', '13820000249', '123456', null, '0', '[1]', '2017-10-24 11:29:42', null);
INSERT INTO `account` VALUES ('10336', '13820000340', '123456', null, '0', '[1]', '2017-10-24 11:29:49', null);
INSERT INTO `account` VALUES ('10337', '13820000154', '123456', null, '0', '[1]', '2017-10-24 11:29:49', null);
INSERT INTO `account` VALUES ('10338', '13820000250', '123456', null, '0', '[1]', '2017-10-24 11:30:09', null);
INSERT INTO `account` VALUES ('10339', '13820000429', '123456', null, '0', '[1]', '2017-10-24 11:30:23', null);
INSERT INTO `account` VALUES ('10340', '13820000251', '123456', null, '0', '[1]', '2017-10-24 11:30:32', null);
INSERT INTO `account` VALUES ('10341', '13820000155', '123456', null, '0', '[1]', '2017-10-24 11:30:33', null);
INSERT INTO `account` VALUES ('10342', '13820000341', '123456', null, '0', '[1]', '2017-10-24 11:30:38', null);
INSERT INTO `account` VALUES ('10343', '13820000252', '123456', null, '0', '[1]', '2017-10-24 11:30:57', null);
INSERT INTO `account` VALUES ('10344', '13820000202', '123456', null, '0', '[1]', '2017-10-24 11:31:07', null);
INSERT INTO `account` VALUES ('10345', '13820000086', '123456', null, '0', '[1]', '2017-10-24 11:31:10', null);
INSERT INTO `account` VALUES ('10346', '13820000430', '123456', null, '0', '[1]', '2017-10-24 11:31:11', null);
INSERT INTO `account` VALUES ('10347', '13820000253', '123456', null, '0', '[1]', '2017-10-24 11:31:21', null);
INSERT INTO `account` VALUES ('10348', '13820000342', '123456', null, '0', '[1]', '2017-10-24 11:31:28', null);
INSERT INTO `account` VALUES ('10349', '13820000156', '123456', null, '0', '[1]', '2017-10-24 11:31:45', null);
INSERT INTO `account` VALUES ('10350', '13820000254', '123456', null, '0', '[1]', '2017-10-24 11:31:45', null);
INSERT INTO `account` VALUES ('10351', '13820000087', '123456', null, '0', '[1]', '2017-10-24 11:31:46', null);
INSERT INTO `account` VALUES ('10352', '13820000088', '123456', null, '0', '[1]', '2017-10-24 11:32:15', null);
INSERT INTO `account` VALUES ('10353', '13820000255', '123456', null, '0', '[1]', '2017-10-24 11:32:17', null);
INSERT INTO `account` VALUES ('10354', '13820000343', '123456', null, '0', '[1]', '2017-10-24 11:32:20', null);
INSERT INTO `account` VALUES ('10355', '13820000256', '123456', null, '0', '[1]', '2017-10-24 11:32:38', null);
INSERT INTO `account` VALUES ('10356', '13820000157', '123456', null, '0', '[1]', '2017-10-24 11:32:42', null);
INSERT INTO `account` VALUES ('10357', '13820000089', '123456', null, '0', '[1]', '2017-10-24 11:32:43', null);
INSERT INTO `account` VALUES ('10358', '13820000257', '123456', null, '0', '[1]', '2017-10-24 11:33:05', null);
INSERT INTO `account` VALUES ('10359', '13820000090', '123456', null, '0', '[1]', '2017-10-24 11:33:10', null);
INSERT INTO `account` VALUES ('10360', '13820000344', '123456', null, '0', '[1]', '2017-10-24 11:33:16', null);
INSERT INTO `account` VALUES ('10361', '13820000258', '123456', null, '0', '[1]', '2017-10-24 11:33:30', null);
INSERT INTO `account` VALUES ('10362', '13820000158', '123456', null, '0', '[1]', '2017-10-24 11:33:32', null);
INSERT INTO `account` VALUES ('10363', '13820000259', '123456', null, '0', '[1]', '2017-10-24 11:33:56', null);
INSERT INTO `account` VALUES ('10364', '13820000345', '123456', null, '0', '[1]', '2017-10-24 11:34:05', null);
INSERT INTO `account` VALUES ('10365', '13820000260', '123456', null, '0', '[1]', '2017-10-24 11:34:25', null);
INSERT INTO `account` VALUES ('10366', '13820000159', '123456', null, '0', '[1]', '2017-10-24 11:34:28', null);
INSERT INTO `account` VALUES ('10367', '13820000261', '123456', null, '0', '[1]', '2017-10-24 11:34:48', null);
INSERT INTO `account` VALUES ('10368', '13820000346', '123456', null, '0', '[1]', '2017-10-24 11:35:05', null);
INSERT INTO `account` VALUES ('10369', '13820000203', '123456', null, '0', '[1]', '2017-10-24 11:35:10', null);
INSERT INTO `account` VALUES ('10370', '13820000262', '123456', null, '0', '[1]', '2017-10-24 11:35:23', null);
INSERT INTO `account` VALUES ('10371', '13820000160', '123456', null, '0', '[1]', '2017-10-24 11:35:27', null);
INSERT INTO `account` VALUES ('10372', '13820000204', '123456', null, '0', '[1]', '2017-10-24 11:35:40', null);
INSERT INTO `account` VALUES ('10373', '13820000263', '123456', null, '0', '[1]', '2017-10-24 11:35:48', null);
INSERT INTO `account` VALUES ('10374', '13820000161', '123456', null, '0', '[1]', '2017-10-24 11:36:13', null);
INSERT INTO `account` VALUES ('10375', '13820000205', '123456', null, '0', '[1]', '2017-10-24 11:36:22', null);
INSERT INTO `account` VALUES ('10376', '13820000347', '123456', null, '0', '[1]', '2017-10-24 11:36:31', null);
INSERT INTO `account` VALUES ('10377', '13820000264', '123456', null, '0', '[1]', '2017-10-24 11:36:40', null);
INSERT INTO `account` VALUES ('10378', '13820000206', '123456', null, '0', '[1]', '2017-10-24 11:36:52', null);
INSERT INTO `account` VALUES ('10379', '13820000162', '123456', null, '0', '[1]', '2017-10-24 11:37:01', null);
INSERT INTO `account` VALUES ('10380', '13820000265', '123456', null, '0', '[1]', '2017-10-24 11:37:04', null);
INSERT INTO `account` VALUES ('10381', '13820000348', '123456', null, '0', '[1]', '2017-10-24 11:37:21', null);
INSERT INTO `account` VALUES ('10382', '13820000207', '123456', null, '0', '[1]', '2017-10-24 11:37:22', null);
INSERT INTO `account` VALUES ('10383', '13820000266', '123456', null, '0', '[1]', '2017-10-24 11:37:32', null);
INSERT INTO `account` VALUES ('10384', '13820000267', '123456', null, '0', '[1]', '2017-10-24 11:37:55', null);
INSERT INTO `account` VALUES ('10385', '18962855236', 'qq498933956', null, '0', '[1]', '2017-10-24 11:37:56', null);
INSERT INTO `account` VALUES ('10386', '13820000163', '123456', null, '0', '[1]', '2017-10-24 11:37:56', null);
INSERT INTO `account` VALUES ('10387', '13820000208', '123456', null, '0', '[1]', '2017-10-24 11:37:58', null);
INSERT INTO `account` VALUES ('10388', '13820000349', '123456', null, '0', '[1]', '2017-10-24 11:38:10', null);
INSERT INTO `account` VALUES ('10389', '13820000268', '123456', null, '0', '[1]', '2017-10-24 11:38:21', null);
INSERT INTO `account` VALUES ('10390', '13820000164', '123456', null, '0', '[1]', '2017-10-24 11:38:41', null);
INSERT INTO `account` VALUES ('10391', '13820000269', '123456', null, '0', '[1]', '2017-10-24 11:38:45', null);
INSERT INTO `account` VALUES ('10392', '13820000209', '123456', null, '0', '[1]', '2017-10-24 11:38:48', null);
INSERT INTO `account` VALUES ('10393', '13820000270', '123456', null, '0', '[1]', '2017-10-24 11:39:09', null);
INSERT INTO `account` VALUES ('10394', '13820000350', '123456', null, '0', '[1]', '2017-10-24 11:39:10', null);
INSERT INTO `account` VALUES ('10395', '13820000210', '123456', null, '0', '[1]', '2017-10-24 11:39:25', null);
INSERT INTO `account` VALUES ('10396', '13820000165', '123456', null, '0', '[1]', '2017-10-24 11:39:36', null);
INSERT INTO `account` VALUES ('10397', '13820000351', '123456', null, '0', '[1]', '2017-10-24 11:40:01', null);
INSERT INTO `account` VALUES ('10398', '13820000211', '123456', null, '0', '[1]', '2017-10-24 11:40:09', null);
INSERT INTO `account` VALUES ('10399', '13820000166', '123456', null, '0', '[1]', '2017-10-24 11:40:26', null);
INSERT INTO `account` VALUES ('10400', '13820000212', '123456', null, '0', '[1]', '2017-10-24 11:40:43', null);
INSERT INTO `account` VALUES ('10401', '13820000352', '123456', null, '0', '[1]', '2017-10-24 11:40:57', null);
INSERT INTO `account` VALUES ('10402', '13820000167', '123456', null, '0', '[1]', '2017-10-24 11:41:11', null);
INSERT INTO `account` VALUES ('10403', '13820000213', '123456', null, '0', '[1]', '2017-10-24 11:41:17', null);
INSERT INTO `account` VALUES ('10404', '13820000214', '123456', null, '0', '[1]', '2017-10-24 11:41:47', null);
INSERT INTO `account` VALUES ('10405', '13820000353', '123456', null, '0', '[1]', '2017-10-24 11:41:56', null);
INSERT INTO `account` VALUES ('10406', '13820000168', '123456', null, '0', '[1]', '2017-10-24 11:41:59', null);
INSERT INTO `account` VALUES ('10407', '13820000215', '123456', null, '0', '[1]', '2017-10-24 11:42:15', null);
INSERT INTO `account` VALUES ('10408', '13820000216', '123456', null, '0', '[1]', '2017-10-24 11:42:42', null);
INSERT INTO `account` VALUES ('10409', '13820000169', '123456', null, '0', '[1]', '2017-10-24 11:42:46', null);
INSERT INTO `account` VALUES ('10410', '13820000354', '123456', null, '0', '[1]', '2017-10-24 11:43:11', null);
INSERT INTO `account` VALUES ('10411', '13820000217', '123456', null, '0', '[1]', '2017-10-24 11:43:12', null);
INSERT INTO `account` VALUES ('10412', '13820000170', '123456', null, '0', '[1]', '2017-10-24 11:43:41', null);
INSERT INTO `account` VALUES ('10413', '13820000218', '123456', null, '0', '[1]', '2017-10-24 11:43:46', null);
INSERT INTO `account` VALUES ('10414', '13820000355', '123456', null, '0', '[1]', '2017-10-24 11:44:17', null);
INSERT INTO `account` VALUES ('10415', '13820000219', '123456', null, '0', '[1]', '2017-10-24 11:44:24', null);
INSERT INTO `account` VALUES ('10416', '13820000221', '123456', null, '0', '[1]', '2017-10-24 11:44:56', null);
INSERT INTO `account` VALUES ('10417', '13820000171', '123456', null, '0', '[1]', '2017-10-24 11:45:00', null);
INSERT INTO `account` VALUES ('10418', '13820000356', '123456', null, '0', '[1]', '2017-10-24 11:45:07', null);
INSERT INTO `account` VALUES ('10419', '13820000220', '123456', null, '0', '[1]', '2017-10-24 11:45:37', null);
INSERT INTO `account` VALUES ('10420', '13820000172', '123456', null, '0', '[1]', '2017-10-24 11:45:42', null);
INSERT INTO `account` VALUES ('10421', '13820000357', '123456', null, '0', '[1]', '2017-10-24 11:45:54', null);
INSERT INTO `account` VALUES ('10422', '13820000222', '123456', null, '0', '[1]', '2017-10-24 11:46:11', null);
INSERT INTO `account` VALUES ('10423', '13820000173', '123456', null, '0', '[1]', '2017-10-24 11:46:26', null);
INSERT INTO `account` VALUES ('10424', '13820000358', '123456', null, '0', '[1]', '2017-10-24 11:46:42', null);
INSERT INTO `account` VALUES ('10425', '13820000223', '123456', null, '0', '[1]', '2017-10-24 11:46:42', null);
INSERT INTO `account` VALUES ('10426', '13820000224', '123456', null, '0', '[1]', '2017-10-24 11:47:12', null);
INSERT INTO `account` VALUES ('10427', '13820000174', '123456', null, '0', '[1]', '2017-10-24 11:47:15', null);
INSERT INTO `account` VALUES ('10428', '13820000359', '123456', null, '0', '[1]', '2017-10-24 11:47:43', null);
INSERT INTO `account` VALUES ('10429', '13820000225', '123456', null, '0', '[1]', '2017-10-24 11:47:46', null);
INSERT INTO `account` VALUES ('10430', '13820000175', '123456', null, '0', '[1]', '2017-10-24 11:47:59', null);
INSERT INTO `account` VALUES ('10431', '13820000091', '123456', null, '0', '[1]', '2017-10-24 11:48:56', null);
INSERT INTO `account` VALUES ('10432', '13820000360', '123456', null, '0', '[1]', '2017-10-24 11:49:11', null);
INSERT INTO `account` VALUES ('10433', '13820000177', '123456', null, '0', '[1]', '2017-10-24 11:49:37', null);
INSERT INTO `account` VALUES ('10434', '13820000092', '123456', null, '0', '[1]', '2017-10-24 11:49:56', null);
INSERT INTO `account` VALUES ('10435', '13820000176', '123456', null, '0', '[1]', '2017-10-24 11:50:21', null);
INSERT INTO `account` VALUES ('10436', '13820000093', '123456', null, '0', '[1]', '2017-10-24 11:50:31', null);
INSERT INTO `account` VALUES ('10437', '13820000094', '123456', null, '0', '[1]', '2017-10-24 11:51:04', null);
INSERT INTO `account` VALUES ('10438', '13820000178', '123456', null, '0', '[1]', '2017-10-24 11:51:08', null);
INSERT INTO `account` VALUES ('10439', '13820000179', '123456', null, '0', '[1]', '2017-10-24 11:51:54', null);
INSERT INTO `account` VALUES ('10440', '13820000095', '123456', null, '0', '[1]', '2017-10-24 11:51:55', null);
INSERT INTO `account` VALUES ('10441', '13820000096', '123456', null, '0', '[1]', '2017-10-24 11:52:34', null);
INSERT INTO `account` VALUES ('10442', '13820000180', '123456', null, '0', '[1]', '2017-10-24 11:52:39', null);
INSERT INTO `account` VALUES ('10443', '13820000097', '123456', null, '0', '[1]', '2017-10-24 11:53:21', null);
INSERT INTO `account` VALUES ('10444', '13820000098', '123456', null, '0', '[1]', '2017-10-24 11:55:00', null);
INSERT INTO `account` VALUES ('10445', '13820000099', '123456', null, '0', '[1]', '2017-10-24 11:57:01', null);
INSERT INTO `account` VALUES ('10446', '13820000100', '123456', null, '0', '[1]', '2017-10-24 11:57:42', null);
INSERT INTO `account` VALUES ('10447', '13820000101', '123456', null, '0', '[1]', '2017-10-24 11:58:54', null);
INSERT INTO `account` VALUES ('10448', '13820000102', '123456', null, '0', '[1]', '2017-10-24 11:59:33', null);
INSERT INTO `account` VALUES ('10449', '13820000103', '123456', null, '0', '[1]', '2017-10-24 12:00:07', null);
INSERT INTO `account` VALUES ('10450', '13820000104', '123456', null, '0', '[1]', '2017-10-24 12:00:38', null);
INSERT INTO `account` VALUES ('10451', '13820000105', '123456', null, '0', '[1]', '2017-10-24 13:31:49', null);
INSERT INTO `account` VALUES ('10452', '13820000106', '123456', null, '0', '[1]', '2017-10-24 13:32:31', null);
INSERT INTO `account` VALUES ('10453', '15168332957', '1362683957', null, '0', '[1]', '2017-10-24 13:32:34', null);
INSERT INTO `account` VALUES ('10454', '13820000107', '123456', null, '0', '[1]', '2017-10-24 13:35:44', null);
INSERT INTO `account` VALUES ('10455', '13820000108', '123456', null, '0', '[1]', '2017-10-24 13:36:17', null);
INSERT INTO `account` VALUES ('10456', '13820000109', '123456', null, '0', '[1]', '2017-10-24 13:36:53', null);
INSERT INTO `account` VALUES ('10457', '13820000110', '123456', null, '0', '[1]', '2017-10-24 13:37:33', null);
INSERT INTO `account` VALUES ('10458', '13820000111', '123456', null, '0', '[1]', '2017-10-24 13:38:03', null);
INSERT INTO `account` VALUES ('10459', '13820000112', '123456', null, '0', '[1]', '2017-10-24 13:38:55', null);
INSERT INTO `account` VALUES ('10460', '13820000114', '123456', null, '0', '[1]', '2017-10-24 13:40:30', null);
INSERT INTO `account` VALUES ('10461', '13820000115', '123456', null, '0', '[1]', '2017-10-24 13:41:05', null);
INSERT INTO `account` VALUES ('10462', '13820000116', '123456', null, '0', '[1]', '2017-10-24 13:41:48', null);
INSERT INTO `account` VALUES ('10463', '13820000117', '123456', null, '0', '[1]', '2017-10-24 13:42:19', null);
INSERT INTO `account` VALUES ('10464', '13820000119', '123456', null, '0', '[1]', '2017-10-24 13:44:05', null);
INSERT INTO `account` VALUES ('10465', '13820000120', '123456', null, '0', '[1]', '2017-10-24 13:44:52', null);
INSERT INTO `account` VALUES ('10466', '13820000121', '123456', null, '0', '[1]', '2017-10-24 13:45:22', null);
INSERT INTO `account` VALUES ('10467', '13820000122', '123456', null, '0', '[1]', '2017-10-24 13:46:08', null);
INSERT INTO `account` VALUES ('10468', '13820000123', '123456', null, '0', '[1]', '2017-10-24 13:46:40', null);
INSERT INTO `account` VALUES ('10469', '13820000124', '123456', null, '0', '[1]', '2017-10-24 13:47:13', null);
INSERT INTO `account` VALUES ('10470', '13820000125', '123456', null, '0', '[1]', '2017-10-24 13:47:50', null);
INSERT INTO `account` VALUES ('10471', '13820000126', '123456', null, '0', '[1]', '2017-10-24 13:50:52', null);
INSERT INTO `account` VALUES ('10472', '13820000127', '123456', null, '0', '[1]', '2017-10-24 13:51:42', null);
INSERT INTO `account` VALUES ('10473', '13820000128', '123456', null, '0', '[1]', '2017-10-24 13:52:27', null);
INSERT INTO `account` VALUES ('10474', '13820000130', '123456', null, '0', '[1]', '2017-10-24 13:55:02', null);
INSERT INTO `account` VALUES ('10475', '13820000132', '123456', null, '0', '[1]', '2017-10-24 13:56:39', null);
INSERT INTO `account` VALUES ('10476', '13820000133', '123456', null, '0', '[1]', '2017-10-24 13:58:48', null);
INSERT INTO `account` VALUES ('10477', '13820000134', '123456', null, '0', '[1]', '2017-10-24 13:59:42', null);
INSERT INTO `account` VALUES ('10478', '18628801886', 'lzlno1', null, '0', '[1]', '2017-10-24 13:59:54', null);
INSERT INTO `account` VALUES ('10479', '13820000135', '123456', null, '0', '[1]', '2017-10-24 14:00:21', null);
INSERT INTO `account` VALUES ('10480', '13820000113', '123456', null, '0', '[1]', '2017-10-24 14:09:02', null);
INSERT INTO `account` VALUES ('10481', '13820000118', '123456', null, '0', '[1]', '2017-10-24 14:11:48', null);
INSERT INTO `account` VALUES ('10482', '13820000001', '123456', null, '0', '[1]', '2017-10-24 14:18:03', null);
INSERT INTO `account` VALUES ('10483', '13820000129', '123456', null, '0', '[1]', '2017-10-24 14:18:25', null);
INSERT INTO `account` VALUES ('10484', '13820000002', '123456', null, '0', '[1]', '2017-10-24 14:18:39', null);
INSERT INTO `account` VALUES ('10485', '13820000003', '123456', null, '0', '[1]', '2017-10-24 14:19:47', null);
INSERT INTO `account` VALUES ('10486', '13820000004', '123456', null, '0', '[1]', '2017-10-24 14:20:23', null);
INSERT INTO `account` VALUES ('10487', '13820000005', '123456', null, '0', '[1]', '2017-10-24 14:20:58', null);
INSERT INTO `account` VALUES ('10488', '13820000006', '123456', null, '0', '[1]', '2017-10-24 14:21:30', null);
INSERT INTO `account` VALUES ('10489', '13820000131', '123456', null, '0', '[1]', '2017-10-24 14:21:38', null);
INSERT INTO `account` VALUES ('10490', '13820000007', '123456', null, '0', '[1]', '2017-10-24 14:21:53', null);
INSERT INTO `account` VALUES ('10491', '13820000008', '123456', null, '0', '[1]', '2017-10-24 14:22:38', null);
INSERT INTO `account` VALUES ('10492', '13820000009', '123456', null, '0', '[1]', '2017-10-24 14:23:06', null);
INSERT INTO `account` VALUES ('10493', '13820000010', '123456', null, '0', '[1]', '2017-10-24 14:23:30', null);
INSERT INTO `account` VALUES ('10494', '13820000011', '123456', null, '0', '[1]', '2017-10-24 14:23:56', null);
INSERT INTO `account` VALUES ('10495', '13820000012', '123456', null, '0', '[1]', '2017-10-24 14:24:18', null);
INSERT INTO `account` VALUES ('10496', '13820000013', '123456', null, '0', '[1]', '2017-10-24 14:24:40', null);
INSERT INTO `account` VALUES ('10497', '13820000014', '123456', null, '0', '[1]', '2017-10-24 14:25:08', null);
INSERT INTO `account` VALUES ('10498', '13820000015', '123456', null, '0', '[1]', '2017-10-24 14:25:29', null);
INSERT INTO `account` VALUES ('10499', '13820000016', '123456', null, '0', '[1]', '2017-10-24 14:25:51', null);
INSERT INTO `account` VALUES ('10500', '13820000017', '123456', null, '0', '[1]', '2017-10-24 14:26:13', null);
INSERT INTO `account` VALUES ('10501', '13820000018', '123456', null, '0', '[1]', '2017-10-24 14:26:33', null);
INSERT INTO `account` VALUES ('10502', '13820000019', '123456', null, '0', '[1]', '2017-10-24 14:26:57', null);
INSERT INTO `account` VALUES ('10503', '13820000020', '123456', null, '0', '[1]', '2017-10-24 14:27:19', null);
INSERT INTO `account` VALUES ('10504', '13820000021', '123456', null, '0', '[1]', '2017-10-24 14:27:40', null);
INSERT INTO `account` VALUES ('10505', '13820000022', '123456', null, '0', '[1]', '2017-10-24 14:28:03', null);
INSERT INTO `account` VALUES ('10506', '13820000023', '123456', null, '0', '[1]', '2017-10-24 14:28:31', null);
INSERT INTO `account` VALUES ('10507', '13820000024', '123456', null, '0', '[1]', '2017-10-24 14:29:00', null);
INSERT INTO `account` VALUES ('10508', '13820000025', '123456', null, '0', '[1]', '2017-10-24 14:29:57', null);
INSERT INTO `account` VALUES ('10509', '13820000026', '123456', null, '0', '[1]', '2017-10-24 14:30:34', null);
INSERT INTO `account` VALUES ('10510', '13820000027', '123456', null, '0', '[1]', '2017-10-24 14:31:59', null);
INSERT INTO `account` VALUES ('10511', '13820000028', '123456', null, '0', '[1]', '2017-10-24 14:32:58', null);
INSERT INTO `account` VALUES ('10512', '13820000029', '123456', null, '0', '[1]', '2017-10-24 14:33:25', null);
INSERT INTO `account` VALUES ('10513', '13820000030', '123456', null, '0', '[1]', '2017-10-24 14:33:49', null);
INSERT INTO `account` VALUES ('10514', '13820000031', '123456', null, '0', '[1]', '2017-10-24 14:34:13', null);
INSERT INTO `account` VALUES ('10515', '13820000032', '123456', null, '0', '[1]', '2017-10-24 14:34:38', null);
INSERT INTO `account` VALUES ('10516', '13820000033', '123456', null, '0', '[1]', '2017-10-24 14:35:02', null);
INSERT INTO `account` VALUES ('10517', '13820000034', '123456', null, '0', '[1]', '2017-10-24 14:35:26', null);
INSERT INTO `account` VALUES ('10518', '13820000035', '123456', null, '0', '[1]', '2017-10-24 14:36:09', null);
INSERT INTO `account` VALUES ('10519', '13820000036', '123456', null, '0', '[1]', '2017-10-24 14:36:33', null);
INSERT INTO `account` VALUES ('10520', '13820000037', '123456', null, '0', '[1]', '2017-10-24 14:37:00', null);
INSERT INTO `account` VALUES ('10521', '13820000038', '123456', null, '0', '[1]', '2017-10-24 14:37:24', null);
INSERT INTO `account` VALUES ('10522', '13820000039', '123456', null, '0', '[1]', '2017-10-24 14:37:47', null);
INSERT INTO `account` VALUES ('10523', '13820000040', '123456', null, '0', '[1]', '2017-10-24 14:38:34', null);
INSERT INTO `account` VALUES ('10524', '13820000041', '123456', null, '0', '[1]', '2017-10-24 14:38:57', null);
INSERT INTO `account` VALUES ('10525', '13820000042', '123456', null, '0', '[1]', '2017-10-24 14:39:21', null);
INSERT INTO `account` VALUES ('10526', '13820000043', '123456', null, '0', '[1]', '2017-10-24 14:39:44', null);
INSERT INTO `account` VALUES ('10527', '13820000044', '123456', null, '0', '[1]', '2017-10-24 14:40:06', null);
INSERT INTO `account` VALUES ('10528', '13820000045', '123456', null, '0', '[1]', '2017-10-24 14:40:30', null);
INSERT INTO `account` VALUES ('10529', '13820000271', '123456', null, '0', '[1]', '2017-10-24 14:53:49', null);
INSERT INTO `account` VALUES ('10530', '13820000272', '123456', null, '0', '[1]', '2017-10-24 14:54:39', null);
INSERT INTO `account` VALUES ('10531', '13820000273', '123456', null, '0', '[1]', '2017-10-24 14:55:08', null);
INSERT INTO `account` VALUES ('10532', '13820000274', '123456', null, '0', '[1]', '2017-10-24 14:55:59', null);
INSERT INTO `account` VALUES ('10533', '13820000275', '123456', null, '0', '[1]', '2017-10-24 14:57:09', null);
INSERT INTO `account` VALUES ('10534', '13820000276', '123456', null, '0', '[1]', '2017-10-24 14:58:04', null);
INSERT INTO `account` VALUES ('10535', '13820000277', '123456', null, '0', '[1]', '2017-10-24 14:59:49', null);
INSERT INTO `account` VALUES ('10536', '13820000278', '123456', null, '0', '[1]', '2017-10-24 15:01:17', null);
INSERT INTO `account` VALUES ('10537', '13820000279', '123456', null, '0', '[1]', '2017-10-24 15:02:10', null);
INSERT INTO `account` VALUES ('10538', '13820000280', '123456', null, '0', '[1]', '2017-10-24 15:03:15', null);
INSERT INTO `account` VALUES ('10539', '13820000281', '123456', null, '0', '[1]', '2017-10-24 15:03:51', null);
INSERT INTO `account` VALUES ('10540', '13820000282', '123456', null, '0', '[1]', '2017-10-24 15:04:23', null);
INSERT INTO `account` VALUES ('10541', '13820000283', '123456', null, '0', '[1]', '2017-10-24 15:04:53', null);
INSERT INTO `account` VALUES ('10542', '13820000284', '123456', null, '0', '[1]', '2017-10-24 15:05:54', null);
INSERT INTO `account` VALUES ('10543', '13820000285', '123456', null, '0', '[1]', '2017-10-24 15:06:34', null);
INSERT INTO `account` VALUES ('10544', '13820000286', '123456', null, '0', '[1]', '2017-10-24 15:07:05', null);
INSERT INTO `account` VALUES ('10545', '13820000287', '123456', null, '0', '[1]', '2017-10-24 15:07:36', null);
INSERT INTO `account` VALUES ('10546', '13820000288', '123456', null, '0', '[1]', '2017-10-24 15:08:04', null);
INSERT INTO `account` VALUES ('10547', '13820000289', '123456', null, '0', '[1]', '2017-10-24 15:08:32', null);
INSERT INTO `account` VALUES ('10548', '13820000290', '123456', null, '0', '[1]', '2017-10-24 15:09:11', null);
INSERT INTO `account` VALUES ('10549', '13820000291', '123456', null, '0', '[1]', '2017-10-24 15:10:08', null);
INSERT INTO `account` VALUES ('10550', '13820000292', '123456', null, '0', '[1]', '2017-10-24 15:11:03', null);
INSERT INTO `account` VALUES ('10551', '13820000293', '123456', null, '0', '[1]', '2017-10-24 15:11:39', null);
INSERT INTO `account` VALUES ('10552', '13820000294', '123456', null, '0', '[1]', '2017-10-24 15:12:08', null);
INSERT INTO `account` VALUES ('10553', '13820000295', '123456', null, '0', '[1]', '2017-10-24 15:12:49', null);
INSERT INTO `account` VALUES ('10554', '13820000296', '123456', null, '0', '[1]', '2017-10-24 15:14:05', null);
INSERT INTO `account` VALUES ('10555', '13820000297', '123456', null, '0', '[1]', '2017-10-24 15:15:13', null);
INSERT INTO `account` VALUES ('10556', '13820000298', '123456', null, '0', '[1]', '2017-10-24 15:15:57', null);
INSERT INTO `account` VALUES ('10557', '13820000299', '123456', null, '0', '[1]', '2017-10-24 15:16:45', null);
INSERT INTO `account` VALUES ('10558', '13820000300', '123456', null, '0', '[1]', '2017-10-24 15:19:58', null);
INSERT INTO `account` VALUES ('10559', '13820000301', '123456', null, '0', '[1]', '2017-10-24 15:21:01', null);
INSERT INTO `account` VALUES ('10560', '13820000302', '123456', null, '0', '[1]', '2017-10-24 15:21:39', null);
INSERT INTO `account` VALUES ('10561', '13820000303', '123456', null, '0', '[1]', '2017-10-24 15:22:58', null);
INSERT INTO `account` VALUES ('10562', '13820000304', '123456', null, '0', '[1]', '2017-10-24 15:24:04', null);
INSERT INTO `account` VALUES ('10563', '13820000305', '123456', null, '0', '[1]', '2017-10-24 15:25:04', null);
INSERT INTO `account` VALUES ('10564', '13820000306', '123456', null, '0', '[1]', '2017-10-24 15:25:50', null);
INSERT INTO `account` VALUES ('10565', '13820000307', '123456', null, '0', '[1]', '2017-10-24 15:26:20', null);
INSERT INTO `account` VALUES ('10566', '13820000308', '123456', null, '0', '[1]', '2017-10-24 15:27:17', null);
INSERT INTO `account` VALUES ('10567', '13820000309', '123456', null, '0', '[1]', '2017-10-24 15:28:22', null);
INSERT INTO `account` VALUES ('10568', '13820000310', '123456', null, '0', '[1]', '2017-10-24 15:29:07', null);
INSERT INTO `account` VALUES ('10569', '13820000311', '123456', null, '0', '[1]', '2017-10-24 15:29:39', null);
INSERT INTO `account` VALUES ('10570', '13820000312', '123456', null, '0', '[1]', '2017-10-24 15:30:22', null);
INSERT INTO `account` VALUES ('10571', '13820000313', '123456', null, '0', '[1]', '2017-10-24 15:30:57', null);
INSERT INTO `account` VALUES ('10572', '13820000314', '123456', null, '0', '[1]', '2017-10-24 15:31:29', null);
INSERT INTO `account` VALUES ('10573', '13820000315', '123456', null, '0', '[1]', '2017-10-24 15:32:09', null);
INSERT INTO `account` VALUES ('10574', 'fafd1e67ecc07c22482741a38a8ac86d', '123456', null, '1', '[1]', '2017-10-24 15:38:32', null);
INSERT INTO `account` VALUES ('10575', '451938a8f9894734e191ea40f1e2ce34', '123456', null, '1', '[1]', '2017-10-24 16:45:21', null);
INSERT INTO `account` VALUES ('10576', '13455668899', '123456', null, '0', '[1]', '2017-10-24 21:02:57', null);
INSERT INTO `account` VALUES ('10577', '0cb197daef34f2a912abf4a10d6c3795', '123456', null, '1', '[1]', '2017-10-25 09:31:43', null);
INSERT INTO `account` VALUES ('10578', '15811213652', 'gll92117', null, '0', '[1]', '2017-10-25 10:39:29', null);
INSERT INTO `account` VALUES ('10579', '13100221122', '11111111', null, '0', '[1]', '2017-10-25 10:46:58', null);
INSERT INTO `account` VALUES ('10580', '50a68caae88e11a09fe7d487b4d756aa', '123456', null, '1', '[1]', '2017-10-25 11:15:27', null);
INSERT INTO `account` VALUES ('10581', '35dbb0099df6b784c35687069c1fca9b', '123456', null, '1', '[1]', '2017-10-25 14:15:44', null);
INSERT INTO `account` VALUES ('10582', '3c358939a60bd0f4865e93cc3449fe67', '123456', null, '1', '[1]', '2017-10-25 18:17:09', null);
INSERT INTO `account` VALUES ('10583', '4d56ae4cf0f6f6eae88ea9ea5299d6ed', '123456', null, '1', '[1]', '2017-10-26 13:57:41', null);
INSERT INTO `account` VALUES ('10584', '13371880050', '123456', null, '0', '[1]', '2017-10-26 14:12:43', null);
INSERT INTO `account` VALUES ('10585', 'a56364dff3e3328123ad16326b18cf49', '123456', null, '1', '[1]', '2017-10-26 14:54:59', null);
INSERT INTO `account` VALUES ('10586', '13601885658', '123456', null, '0', '[1]', '2017-10-26 17:01:45', null);
INSERT INTO `account` VALUES ('10587', '13521482617', 'Cherry0617', null, '0', '[1]', '2017-10-26 17:23:07', null);
INSERT INTO `account` VALUES ('10588', 'cd9e459ea708a948d5c2f5a6ca8838cf', '123456', null, '1', '[1]', '2017-10-26 23:54:10', null);
INSERT INTO `account` VALUES ('10589', '8a3301458c1e1b662e8852e7c1e82f40', '123456', null, '1', '[1]', '2017-10-27 09:35:04', null);
INSERT INTO `account` VALUES ('10590', '13812345670', '123456', null, '0', '[1]', '2017-10-27 14:40:47', null);
INSERT INTO `account` VALUES ('10591', '7c04f19be728592f54d75bdc6debba45', '123456', null, '1', '[1]', '2017-10-27 14:41:07', null);
INSERT INTO `account` VALUES ('10592', '13812345660', '123456', null, '0', '[1]', '2017-10-27 14:43:01', null);
INSERT INTO `account` VALUES ('10593', '13812345661', '123456', null, '0', '[1]', '2017-10-27 14:43:51', null);
INSERT INTO `account` VALUES ('10594', '13812345662', '123456', null, '0', '[1]', '2017-10-27 14:44:54', null);
INSERT INTO `account` VALUES ('10595', '13812345663', '123456', null, '0', '[1]', '2017-10-27 14:45:45', null);
INSERT INTO `account` VALUES ('10596', '13812345664', '123456', null, '0', '[1]', '2017-10-27 14:46:44', null);
INSERT INTO `account` VALUES ('10597', '13812345665', '123456', null, '0', '[1]', '2017-10-27 14:47:40', null);
INSERT INTO `account` VALUES ('10598', '13812345666', '123456', null, '0', '[1]', '2017-10-27 14:48:31', null);
INSERT INTO `account` VALUES ('10599', '13812345667', '123456', null, '0', '[1]', '2017-10-27 14:49:28', null);
INSERT INTO `account` VALUES ('10600', '13812345668', '123456', null, '0', '[1]', '2017-10-27 14:50:49', null);
INSERT INTO `account` VALUES ('10601', '13812345669', '123456', null, '0', '[1]', '2017-10-27 14:51:41', null);
INSERT INTO `account` VALUES ('10602', '13812345671', '123456', null, '0', '[1]', '2017-10-27 14:52:32', null);
INSERT INTO `account` VALUES ('10603', '13812345672', '123456', null, '0', '[1]', '2017-10-27 14:53:28', null);
INSERT INTO `account` VALUES ('10604', '13812345673', '123456', null, '0', '[1]', '2017-10-27 14:54:27', null);
INSERT INTO `account` VALUES ('10605', '13812345674', '123456', null, '0', '[1]', '2017-10-27 14:55:29', null);
INSERT INTO `account` VALUES ('10606', '13812345675', '123456', null, '0', '[1]', '2017-10-27 14:56:19', null);
INSERT INTO `account` VALUES ('10607', '13812345676', '123456', null, '0', '[1]', '2017-10-27 14:57:10', null);
INSERT INTO `account` VALUES ('10608', '13812345679', '123456', null, '0', '[1]', '2017-10-27 14:59:19', null);
INSERT INTO `account` VALUES ('10609', '13812345680', '123456', null, '0', '[1]', '2017-10-27 15:00:11', null);
INSERT INTO `account` VALUES ('10610', '13812345681', '123456', null, '0', '[1]', '2017-10-27 15:01:08', null);
INSERT INTO `account` VALUES ('10611', '13812345683', '123456', null, '0', '[1]', '2017-10-27 15:02:40', null);
INSERT INTO `account` VALUES ('10612', '13812345684', '123456', null, '0', '[1]', '2017-10-27 15:03:32', null);
INSERT INTO `account` VALUES ('10613', '13812345685', '123456', null, '0', '[1]', '2017-10-27 15:04:25', null);
INSERT INTO `account` VALUES ('10614', '13812345686', '123456', null, '0', '[1]', '2017-10-27 15:05:18', null);
INSERT INTO `account` VALUES ('10615', '13812345687', '123456', null, '0', '[1]', '2017-10-27 15:06:12', null);
INSERT INTO `account` VALUES ('10616', '13812345688', '123456', null, '0', '[1]', '2017-10-27 15:07:02', null);
INSERT INTO `account` VALUES ('10617', '13812345689', '123456', null, '0', '[1]', '2017-10-27 15:07:54', null);
INSERT INTO `account` VALUES ('10618', '13812345690', '123456', null, '0', '[1]', '2017-10-27 15:08:44', null);
INSERT INTO `account` VALUES ('10619', '13812345691', '123456', null, '0', '[1]', '2017-10-27 15:09:32', null);
INSERT INTO `account` VALUES ('10620', '13812345692', '123456', null, '0', '[1]', '2017-10-27 15:10:22', null);
INSERT INTO `account` VALUES ('10621', '13812345693', '123456', null, '0', '[1]', '2017-10-27 15:11:12', null);
INSERT INTO `account` VALUES ('10622', '13812345694', '123456', null, '0', '[1]', '2017-10-27 15:12:18', null);
INSERT INTO `account` VALUES ('10623', '13812345695', '123456', null, '0', '[1]', '2017-10-27 15:13:20', null);
INSERT INTO `account` VALUES ('10624', '13812345696', '123456', null, '0', '[1]', '2017-10-27 15:14:09', null);
INSERT INTO `account` VALUES ('10625', '15020000001', '123456', null, '0', '[1]', '2017-10-28 09:24:22', null);
INSERT INTO `account` VALUES ('10626', '15020000002', '123456', null, '0', '[1]', '2017-10-28 10:16:27', null);
INSERT INTO `account` VALUES ('10627', '15212345678', '123456', null, '0', '[1]', '2017-10-28 10:31:41', null);
INSERT INTO `account` VALUES ('10628', '13812345789', '123456', null, '0', '[1]', '2017-10-28 10:33:56', null);
INSERT INTO `account` VALUES ('10629', '15912345690', '123456', null, '0', '[1]', '2017-10-28 10:34:03', null);
INSERT INTO `account` VALUES ('10630', '15012345674', '123456', null, '0', '[1]', '2017-10-28 10:36:56', null);
INSERT INTO `account` VALUES ('10631', '15060526222', '123456', null, '0', '[1]', '2017-10-28 10:41:26', null);
INSERT INTO `account` VALUES ('10632', '13112345670', '123456', null, '0', '[1]', '2017-10-28 10:49:08', null);
INSERT INTO `account` VALUES ('10633', '13112345671', '123456', null, '0', '[1]', '2017-10-28 10:51:39', null);
INSERT INTO `account` VALUES ('10634', '13592804569', '123456', null, '0', '[1]', '2017-10-28 10:52:12', null);
INSERT INTO `account` VALUES ('10635', '13112345672', '123456', null, '0', '[1]', '2017-10-28 11:03:35', null);
INSERT INTO `account` VALUES ('10636', '13046254954', '8365610', null, '0', '[1]', '2017-10-29 21:27:14', null);
INSERT INTO `account` VALUES ('10637', '18770106015', 'wanbiaohui199', null, '0', '[1]', '2017-11-02 10:56:53', null);
INSERT INTO `account` VALUES ('10638', '13958903324', 'sr19881220', null, '0', '[1]', '2017-11-03 13:04:49', null);
INSERT INTO `account` VALUES ('10639', '18576740797', '123456', null, '0', '[1]', '2017-11-04 00:48:40', null);
INSERT INTO `account` VALUES ('10640', '13456143555', 'kaiser90', '13456143555', '0', '[1]', '2017-11-08 22:50:33', null);
INSERT INTO `account` VALUES ('10641', '5d835ac910f54c607f2f22fd8b466fa1', '123456', null, '1', '[1]', '2017-11-10 11:52:42', null);
INSERT INTO `account` VALUES ('10642', '00efe2814d7798b3ba979a79d0abfe01', '123456', null, '1', null, '2017-11-15 07:25:32', null);
INSERT INTO `account` VALUES ('10643', '13763262792', 'qq199253', null, '0', null, '2017-11-15 22:01:38', null);
INSERT INTO `account` VALUES ('10644', '5dfb4017d8e70e8abd8cae57d7294823', '123456', null, '1', null, '2017-11-19 15:27:33', null);
INSERT INTO `account` VALUES ('10645', '58404a60f10cf84760148b3f51dbf55a', '123456', null, '1', '[1]', '2017-11-20 10:44:48', null);
INSERT INTO `account` VALUES ('10646', '13312341234', '123456', null, '0', null, '2017-11-20 10:46:20', null);
INSERT INTO `account` VALUES ('10647', '471aef96b0e2f348fc7a8836c232057e', '123456', null, '1', null, '2017-11-20 16:05:27', null);
INSERT INTO `account` VALUES ('10648', '0c60e58c117646b07aa2a20ecf930f1f', '123456', null, '1', null, '2017-11-21 10:31:29', null);
INSERT INTO `account` VALUES ('10649', '18098044688', 'q198410', null, '0', null, '2017-11-22 00:19:26', null);
INSERT INTO `account` VALUES ('10650', '18547709833', 'w198410', null, '0', null, '2017-11-22 00:24:55', null);
INSERT INTO `account` VALUES ('10651', '13033662233', '11111111', null, '0', '[1]', '2017-11-22 21:17:11', null);
INSERT INTO `account` VALUES ('10652', '730fba6aea6da4b99540638fe77a4582', '123456', null, '1', '[1]', '2017-11-23 10:02:03', null);
INSERT INTO `account` VALUES ('10653', '18000000000', '3324041', null, '0', '[1]', '2017-11-23 10:13:42', null);
INSERT INTO `account` VALUES ('10654', 'a86cf182d2b0b9faf1e52638fedaf71d', '123456', null, '1', '[1]', '2017-11-23 10:49:13', null);
INSERT INTO `account` VALUES ('10655', '18000000001', '3324041', null, '0', '[1]', '2017-11-23 15:13:41', null);
INSERT INTO `account` VALUES ('10656', '13560084441', '870609', null, '0', '[1]', '2017-11-24 08:19:58', null);
INSERT INTO `account` VALUES ('10657', '3d22a07c03d73b5289d8b45537d832d7', '123456', null, '1', '[1]', '2017-11-24 10:57:09', null);
INSERT INTO `account` VALUES ('10658', '18000000002', '3324041', null, '0', '[1]', '2017-11-24 11:32:44', null);
INSERT INTO `account` VALUES ('10659', '56e285f393faa20075047c10bda10f14', '123456', null, '1', '[1]', '2017-11-24 12:46:12', null);
INSERT INTO `account` VALUES ('10660', '13266891004', '15814347137', null, '0', '[1]', '2017-11-25 06:35:46', null);
INSERT INTO `account` VALUES ('10661', '10abc7c3c047bd0f44334e46f220756d', '123456', null, '1', '[1]', '2017-11-25 15:02:27', null);
INSERT INTO `account` VALUES ('10662', '18317882769', 'w197282', null, '0', '[1]', '2017-11-30 23:53:41', null);
INSERT INTO `account` VALUES ('10663', 'd3ded648aa1d978c941485c2ea1a5b7c', '123456', null, '1', '[1]', '2017-12-01 14:03:04', null);
INSERT INTO `account` VALUES ('10664', '4bf998e2cd0e35dbab0b309cb379bc5a58a7557b', '123456', null, '1', '[1]', '2017-12-01 16:23:41', null);
INSERT INTO `account` VALUES ('10665', '33dec5390e44cda2314f541ee27a1b9f5d9d8535', '123456', null, '1', '[1]', '2017-12-01 16:29:00', null);
INSERT INTO `account` VALUES ('10666', '796fe8d8fc5016a3affb6ad5eb5bbab5', '123456', null, '1', '[1]', '2017-12-02 20:37:22', null);
INSERT INTO `account` VALUES ('10667', '5b75b6df1394d6b7dc4746a836c70b6c', '123456', null, '1', '[1]', '2017-12-04 15:22:48', null);
INSERT INTO `account` VALUES ('10668', 'efe6891cc1b7ffd39ba3e92664966316', '123456', null, '1', '[1]', '2017-12-06 11:05:53', null);
INSERT INTO `account` VALUES ('10669', '0e87d7832030468d422875bb00320ad5', '123456', null, '1', '[1]', '2017-12-11 15:17:38', null);
INSERT INTO `account` VALUES ('10670', '18710288243', 'gaohui', null, '0', '[1]', '2017-12-13 21:21:33', null);
INSERT INTO `account` VALUES ('10671', '90a7f1fc4adebc50b8d277912219d57c', '123456', null, '1', '[1]', '2017-12-14 21:38:42', null);
INSERT INTO `account` VALUES ('10672', '15985827370', '123456', null, '0', '[1]', '2017-12-15 10:01:25', null);
INSERT INTO `account` VALUES ('10673', 'F01179C0-AD3A-4356-8754-8C6AAFD61E2A', '123456', null, '1', '[1]', '2017-12-15 10:24:01', null);
INSERT INTO `account` VALUES ('10674', '13605050563', '123456', null, '0', '[1]', '2017-12-15 10:47:31', null);
INSERT INTO `account` VALUES ('10675', '13899999999', '123456', null, '0', '[1]', '2017-12-15 10:49:11', null);
INSERT INTO `account` VALUES ('10676', '17112345678', '123456', null, '0', '[1]', '2017-12-15 10:56:45', null);
INSERT INTO `account` VALUES ('10677', '15211111111', '123456', null, '0', '[1]', '2017-12-15 10:57:49', null);
INSERT INTO `account` VALUES ('10678', '15023333333', '123456', null, '0', '[1]', '2017-12-15 11:32:23', null);
INSERT INTO `account` VALUES ('10679', '13866666666', '123456', null, '0', '[1]', '2017-12-15 11:33:06', null);
INSERT INTO `account` VALUES ('10680', '15967565110', '123456', null, '0', '[1]', '2017-12-15 11:40:05', null);
INSERT INTO `account` VALUES ('10681', 'dcd202d3901e692ceb64c152531751f3', '123456', null, '1', '[1]', '2017-12-15 13:24:15', null);
INSERT INTO `account` VALUES ('10682', '999fb8d56d906abf50201efab637fe24', '123456', null, '1', '[1]', '2017-12-15 16:10:48', null);
INSERT INTO `account` VALUES ('10683', '15023333334', '123456', null, '0', '[1]', '2017-12-15 16:28:53', null);
INSERT INTO `account` VALUES ('10684', '7df9f9448e94ea66a23e7d7aa42745fd', '123456', null, '1', '[1]', '2017-12-15 16:56:21', null);
INSERT INTO `account` VALUES ('10685', '26992f6e19085aea81712fecf152f911', '123456', null, '1', '[1]', '2017-12-15 17:11:20', null);
INSERT INTO `account` VALUES ('10686', 'd828e141ca3fb587641a4e293a714efe', '123456', null, '1', '[1]', '2017-12-15 17:28:37', null);
INSERT INTO `account` VALUES ('10687', '13800138000', '114477225588', null, '0', '[1]', '2017-12-15 17:29:42', null);
INSERT INTO `account` VALUES ('10688', '13800138001', '123456', null, '0', '[1]', '2017-12-16 14:39:44', null);
INSERT INTO `account` VALUES ('10689', '9a9eec3e6054fc65de8cefb34fd79663', '123456', null, '1', null, '2017-12-16 20:57:13', null);
INSERT INTO `account` VALUES ('10690', '9B868D4D-E59A-46A4-8DDC-229280D63122', '123456', null, '1', '[1]', '2017-12-17 01:11:30', null);
INSERT INTO `account` VALUES ('10691', '13652871442', '1314520', null, '0', '[1]', '2017-12-18 09:18:04', null);
INSERT INTO `account` VALUES ('10692', '13302239617', '121231234', null, '0', '[1]', '2017-12-18 10:35:39', null);
INSERT INTO `account` VALUES ('10693', 'F41AAD2A-7C65-47B1-BD74-E702FC4231D2', '123456', null, '1', '[1]', '2017-12-18 13:17:13', null);
INSERT INTO `account` VALUES ('10694', '89FD50F9-7BC1-4FA5-A466-5A110164D267', '123456', null, '1', '[1]', '2017-12-18 13:42:26', null);
INSERT INTO `account` VALUES ('10695', '991BE7CD-8CAB-46FE-8774-3716E7786E4B', '123456', null, '1', '[1]', '2017-12-18 14:01:38', null);
INSERT INTO `account` VALUES ('10696', '18959818709', '123456', null, '0', '[1]', '2017-12-18 14:02:16', null);
INSERT INTO `account` VALUES ('10697', '19172B8E-2E8A-4BF4-B333-F23836DFBADF', '123456', null, '1', '[1]', '2017-12-18 14:19:12', null);
INSERT INTO `account` VALUES ('10698', '13592804587', '123456', null, '0', '[1]', '2017-12-18 14:20:33', null);
INSERT INTO `account` VALUES ('10699', 'ADF75592-692C-4280-B741-F80596496E86', '123456', null, '1', '[1]', '2017-12-18 14:22:17', null);
INSERT INTO `account` VALUES ('10700', '15311111111', '123456', null, '0', '[1]', '2017-12-18 14:23:47', null);
INSERT INTO `account` VALUES ('10701', '5101DE87-8193-43DB-AD92-7F3294A8289D', '123456', null, '1', '[1]', '2017-12-18 14:25:45', null);
INSERT INTO `account` VALUES ('10702', 'B0C3916D-6C41-4325-AD1D-821653B1407A', '123456', null, '1', '[1]', '2017-12-18 14:26:23', null);
INSERT INTO `account` VALUES ('10703', '18922922800', '990429', null, '0', '[1]', '2017-12-18 14:27:35', null);
INSERT INTO `account` VALUES ('10704', '18605963939', 'xyb1221', '18605963939', '0', '[1]', '2017-12-18 14:28:30', null);
INSERT INTO `account` VALUES ('10705', '18983197997', 'liao2003', null, '0', '[1]', '2017-12-18 14:38:28', null);
INSERT INTO `account` VALUES ('10706', '14789557275', '123456', null, '0', '[1]', '2017-12-18 14:44:37', null);
INSERT INTO `account` VALUES ('10707', '18030300216', 'Abc159753456', null, '0', '[1]', '2017-12-18 14:46:40', null);
INSERT INTO `account` VALUES ('10708', '8DD52E27-0BBB-451A-8D47-1A56647A4AEE', '123456', null, '1', '[1]', '2017-12-18 14:47:15', null);
INSERT INTO `account` VALUES ('10709', '13333333333', '123456', null, '0', '[1]', '2017-12-18 14:48:12', null);
INSERT INTO `account` VALUES ('10710', '13534150073', 'a123456', null, '0', '[1]', '2017-12-18 14:54:33', null);
INSERT INTO `account` VALUES ('10711', 'F0B6B086-422C-490C-AB05-53E15FCD1364', '123456', null, '1', '[1]', '2017-12-18 15:02:33', null);
INSERT INTO `account` VALUES ('10712', '3F2F4F45-8B4E-4387-B510-F7DB2D8BE3EF', '123456', null, '1', '[1]', '2017-12-18 15:17:44', null);
INSERT INTO `account` VALUES ('10713', 'C8D2431A-DA2F-4496-B8B1-C395F589B882', '123456', null, '1', '[1]', '2017-12-18 15:24:54', null);
INSERT INTO `account` VALUES ('10714', '56131916-DB66-47C0-895E-DD824ADFBDF3', '123456', null, '1', '[1]', '2017-12-18 15:25:58', null);
INSERT INTO `account` VALUES ('10715', 'E0B040B5-7555-4A5C-8BAF-00D5E8DEC02C', '123456', null, '1', '[1]', '2017-12-18 15:30:03', null);
INSERT INTO `account` VALUES ('10716', '13914397885', 'zyz86565907', '13914397885', '0', '[1]', '2017-12-18 15:33:25', null);
INSERT INTO `account` VALUES ('10717', '15860759533', '564335', null, '0', '[1]', '2017-12-18 15:39:57', null);
INSERT INTO `account` VALUES ('10718', '4D7C2F4D-6ED3-4F0B-9AF9-F0887D93FD6B', '123456', null, '1', '[1]', '2017-12-18 15:58:10', null);
INSERT INTO `account` VALUES ('10719', '13200221155', '111111111', null, '0', '[1]', '2017-12-18 15:58:33', null);
INSERT INTO `account` VALUES ('10720', '18605959577', '123789', '18605959577', '0', '[1]', '2017-12-18 16:01:22', '2017-12-18 16:59:58');
INSERT INTO `account` VALUES ('10721', '15527093932', 'a66201187', null, '0', '[1]', '2017-12-18 16:06:58', null);
INSERT INTO `account` VALUES ('10722', '13911001100', '1111111', null, '0', '[1]', '2017-12-18 16:08:30', null);
INSERT INTO `account` VALUES ('10723', '7EECAD64-62F9-44C7-AD24-8B27476CD3BA', '123456', null, '1', '[1]', '2017-12-18 16:08:39', null);
INSERT INTO `account` VALUES ('10724', '1281BD49-9635-46D7-AB23-1C5AEC4391DF', '123456', null, '1', '[1]', '2017-12-18 16:09:49', null);
INSERT INTO `account` VALUES ('10725', '253EF682-3E7A-475B-A68D-2B7B913C696A', '123456', null, '1', null, '2017-12-18 16:31:39', null);
INSERT INTO `account` VALUES ('10726', '13908216604', 'a19871024', null, '0', '[1]', '2017-12-18 16:32:30', null);
INSERT INTO `account` VALUES ('10727', 'AD96148B-2823-461C-A0FE-0759D90E1C1C', '123456', null, '1', '[1]', '2017-12-18 16:37:55', null);
INSERT INTO `account` VALUES ('10728', 'D81918BD-D2F8-4121-9D08-E25C3BD51A2C', '123456', null, '1', '[1]', '2017-12-18 16:40:15', null);
INSERT INTO `account` VALUES ('10729', '2E13641D-018A-4AD6-AAAD-DCFDA9599D97', '123456', null, '1', null, '2017-12-18 16:41:40', null);
INSERT INTO `account` VALUES ('10730', '15312666665', '5901993', null, '0', '[1]', '2017-12-18 16:42:36', null);
INSERT INTO `account` VALUES ('10731', '4C310BF0-EFB5-42D5-B5B8-121871B968C8', '123456', null, '1', '[1]', '2017-12-18 16:45:44', null);
INSERT INTO `account` VALUES ('10732', '13687518001', 'a67694856', null, '0', '[1]', '2017-12-18 16:48:44', null);
INSERT INTO `account` VALUES ('10733', '15019232365', 'shan1314', null, '0', '[1]', '2017-12-18 16:55:38', null);
INSERT INTO `account` VALUES ('10734', '15256822022', '1987225', null, '0', '[1]', '2017-12-18 16:57:40', null);
INSERT INTO `account` VALUES ('10735', 'B171D17E-44EF-4A7C-9A68-8871B81EC5B9', '123456', null, '1', '[1]', '2017-12-18 16:59:41', null);
INSERT INTO `account` VALUES ('10736', '13798473106', 'ddddxxxx', null, '0', '[1]', '2017-12-18 17:09:15', null);
INSERT INTO `account` VALUES ('10737', '594E847D-4B90-49EE-88DB-066BAB9C1DFA', '123456', null, '1', '[1]', '2017-12-18 17:09:35', null);
INSERT INTO `account` VALUES ('10738', '13838875770', 'wn7758521', null, '0', '[1]', '2017-12-18 17:11:58', null);
INSERT INTO `account` VALUES ('10739', '13777573332', '85459105', null, '0', '[1]', '2017-12-18 17:15:29', null);
INSERT INTO `account` VALUES ('10740', '18931562551', 'woaini', null, '0', '[1]', '2017-12-18 17:19:46', null);
INSERT INTO `account` VALUES ('10741', '18591262093', 'guohao1993', null, '0', '[1]', '2017-12-18 17:24:08', null);
INSERT INTO `account` VALUES ('10742', 'F2D552E4-8BB1-4449-8E83-606B8DA925DD', '123456', null, '1', '[1]', '2017-12-18 17:26:52', null);
INSERT INTO `account` VALUES ('10743', '15957660758', '484848', null, '0', '[1]', '2017-12-18 17:31:15', null);
INSERT INTO `account` VALUES ('10744', 'F9A4365E-648E-4410-B703-F7235DBB825A', '123456', null, '1', null, '2017-12-18 17:32:55', null);
INSERT INTO `account` VALUES ('10745', '18643119613', '19880613', null, '0', '[1]', '2017-12-18 17:33:40', null);
INSERT INTO `account` VALUES ('10746', 'F484C1CD-9060-458A-AD6C-54AB39A151C8', '123456', null, '1', '[1]', '2017-12-18 17:39:21', null);
INSERT INTO `account` VALUES ('10747', '4DD736E6-82B0-4F32-9675-483E8BFB83AF', '123456', null, '1', '[1]', '2017-12-18 17:44:43', null);
INSERT INTO `account` VALUES ('10748', '13331215975', 'tanghao975', null, '0', '[1]', '2017-12-18 17:46:24', null);
INSERT INTO `account` VALUES ('10749', 'F3B0C98E-E63B-4EB2-A278-DBA7180D900B', '123456', null, '1', '[1]', '2017-12-18 17:57:26', null);
INSERT INTO `account` VALUES ('10750', '00455D2D-AB78-454D-9337-6A5EE01D0110', '123456', null, '1', '[1]', '2017-12-18 17:59:41', null);
INSERT INTO `account` VALUES ('10751', '13777767780', '1232468', null, '0', '[1]', '2017-12-18 18:10:37', null);
INSERT INTO `account` VALUES ('10752', '78D62857-EA47-4F00-9997-3405FA3D18B1', '123456', null, '1', '[1]', '2017-12-18 18:16:57', null);
INSERT INTO `account` VALUES ('10753', '15605063022', '123456', null, '0', '[1]', '2017-12-18 18:19:29', null);
INSERT INTO `account` VALUES ('10754', '18221091232', '13221212', null, '0', '[1]', '2017-12-18 18:20:39', null);
INSERT INTO `account` VALUES ('10755', '15589376955', 'wz550228', null, '0', '[1]', '2017-12-18 18:28:09', null);
INSERT INTO `account` VALUES ('10756', '410EA97B-95C1-49D2-879E-B8F1781C6EA5', '123456', null, '1', '[1]', '2017-12-18 18:32:00', null);
INSERT INTO `account` VALUES ('10757', 'F1CC7235-68C2-4554-946C-FF9FF2635D75', '123456', null, '1', '[1]', '2017-12-18 18:32:11', null);
INSERT INTO `account` VALUES ('10758', 'B1DC4EF4-1CE3-43E5-8066-367FDB05A2FB', '123456', null, '1', '[1]', '2017-12-18 18:39:46', null);
INSERT INTO `account` VALUES ('10759', 'F5983173-C371-4E42-A661-F23DD913454E', '123456', null, '1', '[1]', '2017-12-18 18:43:59', null);
INSERT INTO `account` VALUES ('10760', '15583583267', '7205400', null, '0', '[1]', '2017-12-18 18:50:40', null);
INSERT INTO `account` VALUES ('10761', 'B892F0CC-3D72-4C14-B6D8-9604DE6E58CD', '123456', null, '1', '[1]', '2017-12-18 18:53:31', null);
INSERT INTO `account` VALUES ('10762', '13556975604', 'qqqqqq', null, '0', '[1]', '2017-12-18 19:01:37', null);
INSERT INTO `account` VALUES ('10763', '0CFB27DE-B1FB-40F6-8202-369BA7471650', '123456', null, '1', '[1]', '2017-12-18 19:16:19', null);
INSERT INTO `account` VALUES ('10764', '13632538082', 'zz5201314', null, '0', '[1]', '2017-12-18 19:23:23', null);
INSERT INTO `account` VALUES ('10765', 'ACE8115F-5128-4E64-A58B-40F8EF7651B8', '123456', null, '1', '[1]', '2017-12-18 19:40:53', null);
INSERT INTO `account` VALUES ('10766', 'DEB5FB3A-578D-433C-B61D-5C48127CD108', '123456', null, '1', '[1]', '2017-12-18 19:45:12', null);
INSERT INTO `account` VALUES ('10767', 'D3688BB1-325A-40A1-B633-BACB3CE212C9', '123456', null, '1', '[1]', '2017-12-18 20:00:16', null);
INSERT INTO `account` VALUES ('10768', '0432D10B-C384-4832-ADD3-6ED23DA382D6', '123456', null, '1', '[1]', '2017-12-18 20:02:43', null);
INSERT INTO `account` VALUES ('10769', '17625111892', 'geng199157qq', null, '0', '[1]', '2017-12-18 20:04:27', null);
INSERT INTO `account` VALUES ('10770', '2CB559C4-5558-4DD3-AB79-8C18C93ED5D6', '123456', null, '1', '[1]', '2017-12-18 20:15:05', null);
INSERT INTO `account` VALUES ('10771', '18825752370', '170520qqpp', null, '0', '[1]', '2017-12-18 20:17:25', null);
INSERT INTO `account` VALUES ('10772', 'CF24E20D-2EBB-4EB7-B658-91D696544ED9', '123456', '18264038444', '1', '[1]', '2017-12-18 20:21:27', null);
INSERT INTO `account` VALUES ('10773', '13178255613', '101388', null, '0', '[1]', '2017-12-18 20:22:24', null);
INSERT INTO `account` VALUES ('10774', '4AA546B1-1F7E-44B9-9791-85E05AB6194A', '123456', null, '1', '[1]', '2017-12-18 20:27:56', null);
INSERT INTO `account` VALUES ('10775', 'F5D06541-F428-402D-A983-43503B398A81', '123456', null, '1', '[1]', '2017-12-18 20:28:48', null);
INSERT INTO `account` VALUES ('10776', '13594000378', '1234567890', null, '0', '[1]', '2017-12-18 20:34:15', null);
INSERT INTO `account` VALUES ('10777', '915FD060-0AE5-422E-82C3-36EFB15F3881', '123456', null, '1', '[1]', '2017-12-18 20:37:01', null);
INSERT INTO `account` VALUES ('10778', '18642771133', '123123', null, '0', '[1]', '2017-12-18 20:51:54', null);
INSERT INTO `account` VALUES ('10779', '514D8267-C842-41E3-93B9-6C08A9EF36E2', '123456', null, '1', '[1]', '2017-12-18 20:53:36', null);
INSERT INTO `account` VALUES ('10780', '18904085338', 'yang123456', null, '0', '[1]', '2017-12-18 20:59:32', null);
INSERT INTO `account` VALUES ('10781', '15651812627', '84385703', null, '0', '[1]', '2017-12-18 21:05:31', null);
INSERT INTO `account` VALUES ('10782', '17663902303', '962464', null, '0', '[1]', '2017-12-18 21:06:18', null);
INSERT INTO `account` VALUES ('10783', '3F653E5C-B072-42D8-91C5-6712FD9794EF', '123456', null, '1', '[1]', '2017-12-18 21:07:14', null);
INSERT INTO `account` VALUES ('10784', '18607036772', '123456', null, '0', '[1]', '2017-12-18 21:07:42', null);
INSERT INTO `account` VALUES ('10785', '15059682022', 'q3378925', null, '0', '[1]', '2017-12-18 21:09:33', null);
INSERT INTO `account` VALUES ('10786', '13777118992', '14ck57b36', null, '0', '[1]', '2017-12-18 21:10:06', null);
INSERT INTO `account` VALUES ('10787', 'C722A19F-BD79-449D-930C-75015F065A54', '123456', null, '1', '[1]', '2017-12-18 21:12:11', null);
INSERT INTO `account` VALUES ('10788', '5378C172-2642-4EEF-8003-368C82DC060A', '123456', null, '1', '[1]', '2017-12-18 21:13:21', null);
INSERT INTO `account` VALUES ('10789', '13812187600', '123456', '13812187600', '0', '[1]', '2017-12-18 21:13:33', null);
INSERT INTO `account` VALUES ('10790', '75C78B28-CFF3-430D-B995-8A07DC1FCD53', '123456', null, '1', '[1]', '2017-12-18 21:21:31', null);
INSERT INTO `account` VALUES ('10791', '15990073408', 'fgdaijxq', null, '0', '[1]', '2017-12-18 21:29:05', null);
INSERT INTO `account` VALUES ('10792', 'CF6DE8AA-F3DA-4E7D-9A35-ABAFE3652D1A', '123456', null, '1', '[1]', '2017-12-18 21:29:11', null);
INSERT INTO `account` VALUES ('10793', 'C50F329A-0256-4E71-B0CC-37BEB583AC67', '123456', '13779923303', '1', '[1]', '2017-12-18 21:39:25', null);
INSERT INTO `account` VALUES ('10794', '760AD8E6-D73C-4DDF-998C-EE619E5DFA54', '123456', null, '1', '[1]', '2017-12-18 21:44:15', null);
INSERT INTO `account` VALUES ('10795', '49E922F3-2944-4A4C-A202-937259E94338', '123456', null, '1', '[1]', '2017-12-18 21:45:39', null);
INSERT INTO `account` VALUES ('10796', '18678420421', '962464', null, '0', null, '2017-12-18 21:56:03', null);
INSERT INTO `account` VALUES ('10797', '422A2A45-E43C-4CDB-8971-815C924C6465', '123456', null, '1', '[1]', '2017-12-18 22:02:38', null);
INSERT INTO `account` VALUES ('10798', '6fef8fb6d31c81d76454d2e0fd1f4baa', '123456', null, '1', '[1]', '2017-12-18 22:08:08', null);
INSERT INTO `account` VALUES ('10799', 'FE957BFC-47D3-4558-8BBA-E0BDD8FA8638', '123456', null, '1', '[1]', '2017-12-18 22:09:07', null);
INSERT INTO `account` VALUES ('10800', '2AD497EB-E715-4F1E-93D8-B978D194C310', '123456', null, '1', '[1]', '2017-12-18 22:18:46', null);
INSERT INTO `account` VALUES ('10801', '15679929521', 'xiao16883', null, '0', '[1]', '2017-12-18 22:24:57', null);
INSERT INTO `account` VALUES ('10802', '18020018378', 'wfe7220010', null, '0', '[1]', '2017-12-18 22:32:03', null);
INSERT INTO `account` VALUES ('10803', 'AF8451B3-92A7-4427-87C5-191FBB637D18', '123456', null, '1', '[1]', '2017-12-18 22:34:01', null);
INSERT INTO `account` VALUES ('10804', '18225413278', 'xsl19820817', null, '0', '[1]', '2017-12-18 22:39:50', null);
INSERT INTO `account` VALUES ('10805', '39D07BAC-1253-45DB-8A91-41C134A34FC1', '123456', null, '1', '[1]', '2017-12-18 22:50:09', null);
INSERT INTO `account` VALUES ('10806', '13758122716', '109054010', null, '0', '[1]', '2017-12-18 23:00:38', null);
INSERT INTO `account` VALUES ('10807', 'FBF801A6-BA28-488E-8A9B-4176EE31B04B', '123456', null, '1', '[1]', '2017-12-18 23:03:39', null);
INSERT INTO `account` VALUES ('10808', '13812650530', 'xbdw226', null, '0', '[1]', '2017-12-18 23:04:54', null);
INSERT INTO `account` VALUES ('10809', '15959619521', 'AS12852245shen', '15959619521', '0', '[1]', '2017-12-18 23:05:45', null);
INSERT INTO `account` VALUES ('10810', 'B7FBBE87-7F84-4C9A-9C79-2599CCF3398D', '123456', null, '1', '[1]', '2017-12-18 23:16:11', null);
INSERT INTO `account` VALUES ('10811', '18581021081', '5211314', null, '0', '[1]', '2017-12-18 23:22:38', null);
INSERT INTO `account` VALUES ('10812', 'E2249D29-BF49-48C7-8B25-6526DBC88A03', '123456', null, '1', '[1]', '2017-12-18 23:27:46', null);
INSERT INTO `account` VALUES ('10813', '56E87F6B-4E3B-48CA-A095-06770162ABFF', '123456', null, '1', '[1]', '2017-12-18 23:28:41', null);
INSERT INTO `account` VALUES ('10814', 'B0E39053-EC51-4376-9026-36B1F18F5FC5', '123456', null, '1', '[1]', '2017-12-18 23:35:02', null);
INSERT INTO `account` VALUES ('10815', '18629291155', '87967801', null, '0', '[1]', '2017-12-18 23:37:18', null);
INSERT INTO `account` VALUES ('10816', '13934004820', '2130128', '13934004820', '0', '[1]', '2017-12-18 23:46:46', null);
INSERT INTO `account` VALUES ('10817', '15124663438', '5218464', null, '0', '[1]', '2017-12-19 00:02:57', null);
INSERT INTO `account` VALUES ('10818', 'ACFAB08B-3EDE-4C8A-8C07-1181BC168B55', '123456', null, '1', '[1]', '2017-12-19 00:19:00', null);
INSERT INTO `account` VALUES ('10819', '5E7EABAF-1FA4-49DB-98BF-965739D10095', '123456', null, '1', '[1]', '2017-12-19 00:25:11', null);
INSERT INTO `account` VALUES ('10820', '15210052349', '187054769', null, '0', '[1]', '2017-12-19 00:25:38', null);
INSERT INTO `account` VALUES ('10821', '13656782683', '6153006', '13656782683', '0', '[1]', '2017-12-19 00:32:16', null);
INSERT INTO `account` VALUES ('10822', '15847683799', 'ding285830862', null, '0', '[1]', '2017-12-19 00:36:44', null);
INSERT INTO `account` VALUES ('10823', '17743143330', 'lt19870619', null, '0', '[1]', '2017-12-19 00:41:02', null);
INSERT INTO `account` VALUES ('10824', '89BEDB77-DA0D-4531-985F-F1C3DB713929', '123456', null, '1', null, '2017-12-19 01:00:07', null);
INSERT INTO `account` VALUES ('10825', '15253249968', 'gaoyu123.+', null, '0', '[1]', '2017-12-19 01:08:53', null);
INSERT INTO `account` VALUES ('10826', '15884144508', 'liuyi1998414**', '15884144508', '0', '[1]', '2017-12-19 01:35:55', null);
INSERT INTO `account` VALUES ('10827', '60233C39-1E5C-4DCC-9FD7-134898D3EDA2', '123456', null, '1', '[1]', '2017-12-19 03:27:11', null);
INSERT INTO `account` VALUES ('10828', '1B30CA8D-2B4B-41FA-A09E-ADEDC53DA04B', '123456', null, '1', '[1]', '2017-12-19 03:45:58', null);
INSERT INTO `account` VALUES ('10829', '18001585053', '574821', null, '0', '[1]', '2017-12-19 03:55:21', null);
INSERT INTO `account` VALUES ('10830', '7A8A89B8-E4DC-4272-A3F8-1D0020971035', '123456', null, '1', '[1]', '2017-12-19 05:09:31', null);
INSERT INTO `account` VALUES ('10831', 'C83C66EA-8377-4287-9896-0E3C92E3F092', '123456', null, '1', '[1]', '2017-12-19 06:47:26', null);
INSERT INTO `account` VALUES ('10832', '6FAC81A0-366D-4F06-84DC-153DB74E7E3E', '123456', null, '1', '[1]', '2017-12-19 07:40:28', null);
INSERT INTO `account` VALUES ('10833', '66CEE9F6-4D33-4A71-BD08-DBAB5112B1DD', '123456', null, '1', '[1]', '2017-12-19 08:27:56', null);
INSERT INTO `account` VALUES ('10834', '15717065605', 'kangkang', null, '0', '[1]', '2017-12-19 08:51:43', null);
INSERT INTO `account` VALUES ('10835', 'A1630C9C-7509-4701-9EBA-A103CF0ECB22', '123456', null, '1', null, '2017-12-19 09:01:56', null);
INSERT INTO `account` VALUES ('10836', 'DFA179B7-FDE6-4CA2-9E0A-F9A4A8117E52', '123456', null, '1', '[1]', '2017-12-19 09:12:23', null);
INSERT INTO `account` VALUES ('10837', '18879215587', 'A123123', null, '0', '[1]', '2017-12-19 09:30:01', null);
INSERT INTO `account` VALUES ('10838', '13736969392', '123123', '13736969392', '0', '[1]', '2017-12-19 09:43:51', null);
INSERT INTO `account` VALUES ('10839', '105C8E71-AFF8-4981-BFB3-3847B383B5A2', '123456', null, '1', '[1]', '2017-12-19 09:46:30', null);
INSERT INTO `account` VALUES ('10840', '15190281737', 'wangshunws', null, '0', '[1]', '2017-12-19 09:47:29', null);
INSERT INTO `account` VALUES ('10841', '13640636305', 'mu19870810', null, '0', '[1]', '2017-12-19 09:49:40', null);
INSERT INTO `account` VALUES ('10842', '13613456789', '123456', null, '0', '[1]', '2017-12-19 09:54:58', null);
INSERT INTO `account` VALUES ('10843', '13800000000', '123456', null, '0', '[1]', '2017-12-19 10:03:33', null);
INSERT INTO `account` VALUES ('10844', '889036BA-1327-4371-B4C6-FA4AF38D3D62', '123456', null, '1', '[1]', '2017-12-19 10:22:57', null);
INSERT INTO `account` VALUES ('10845', '18343392036', 'jinyong', null, '0', '[1]', '2017-12-19 10:45:03', null);
INSERT INTO `account` VALUES ('10846', '399ABAC0-3E4A-4E17-BB71-659EDA144E55', '123456', null, '1', '[1]', '2017-12-19 11:03:28', null);
INSERT INTO `account` VALUES ('10847', '1D0E6D8D-B469-4D25-A435-35513F0A8A6A', '123456', null, '1', '[1]', '2017-12-19 11:07:00', null);
INSERT INTO `account` VALUES ('10848', 'C07AFDD5-EFB1-4A02-88B1-4181FE3EED41', '123456', null, '1', '[1]', '2017-12-19 11:19:51', null);
INSERT INTO `account` VALUES ('10849', 'EB439BA4-5B63-492D-A8D6-CCDA7AB34765', '123456', null, '1', null, '2017-12-19 11:22:45', null);
INSERT INTO `account` VALUES ('10850', '17755411118', 'sz4851628', null, '0', '[1]', '2017-12-19 11:23:19', null);
INSERT INTO `account` VALUES ('10851', '01D91159-D19C-4B10-82B5-13587A69477D', '123456', null, '1', '[1]', '2017-12-19 11:31:40', null);
INSERT INTO `account` VALUES ('10852', 'AC3C01C0-4301-4E88-87DA-A9E6CE6B527A', '123456', null, '1', '[1]', '2017-12-19 12:10:25', null);
INSERT INTO `account` VALUES ('10853', '452C503D-3E2F-4740-BFFF-D6BB7562DCDC', '123456', null, '1', '[1]', '2017-12-19 13:19:08', null);
INSERT INTO `account` VALUES ('10854', '15815876080', 'qq102030', null, '0', '[1]', '2017-12-19 13:26:18', null);
INSERT INTO `account` VALUES ('10855', '2D5EA60D-F228-4B79-9F85-7A7B9FD9E4D7', '123456', null, '1', '[1]', '2017-12-19 13:37:21', null);
INSERT INTO `account` VALUES ('10856', 'C0011C37-87D9-4900-BA3F-E875D0F59933', '123456', null, '1', '[1]', '2017-12-19 13:47:22', null);
INSERT INTO `account` VALUES ('10857', 'B85F0A49-4897-4882-B151-1A06CAC5A6A1', '123456', null, '1', '[1]', '2017-12-19 14:24:26', null);
INSERT INTO `account` VALUES ('10858', 'a6c203f6d4f0bbae15e0de579dc7acc7', '123456', null, '1', '[1]', '2017-12-19 14:37:21', null);
INSERT INTO `account` VALUES ('10859', '5a284754a4295f6e3e377050db82290a', '123456', null, '1', '[1]', '2017-12-19 15:04:18', null);
INSERT INTO `account` VALUES ('10860', '993eedadb04f0a7cba700301c8d7bdcb', '123456', null, '1', null, '2017-12-19 15:51:38', null);
INSERT INTO `account` VALUES ('10861', '60EBB541-F2AF-4F18-91E7-A7BFB7B87E3A', '123456', null, '1', '[1]', '2017-12-19 16:50:42', null);
INSERT INTO `account` VALUES ('10862', '57AD85C3-8DE3-4193-9890-9E3B94BA000F', '123456', null, '1', '[1]', '2017-12-19 17:00:15', null);
INSERT INTO `account` VALUES ('10863', '2DB6F741-B0BA-4EFA-AB1A-EB9305E7A2C5', '123456', null, '1', null, '2017-12-19 17:26:03', null);
INSERT INTO `account` VALUES ('10864', '86DBDABA-7A64-4491-909E-4BC51B6166A5', '123456', null, '1', '[1]', '2017-12-19 18:37:36', null);
INSERT INTO `account` VALUES ('10865', '9DAE9CCA-E8DB-4D09-92D6-884180F5D5D3', '123456', null, '1', '[1]', '2017-12-19 20:36:53', null);

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
INSERT INTO `config_agent` VALUES ('sk', 'QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777', 'QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2', 'DGAMEASDWDSHEN-16TQASDEDE-W33TT', 'CN', 'zh', '172.19.209.149', '3', 'http://172.19.209.149:9800/code', 'http://172.19.209.149:9000/pay', 'http://172.19.209.149:8000', '0', '1');

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
INSERT INTO `config_server` VALUES ('1', '华夏征途-1服', 'sk', 'sk_0001', '101.132.109.250', '172.19.209.149', '8500', '9500', '', '2017-12-15 10:00:00', null, '1', '3', '2', null, '0');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付记录';

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_app_item
-- ----------------------------
INSERT INTO `t_app_item` VALUES ('6', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:38:50', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '测试', 'qidian向萨菲;发送物品', '测试', '4', '发送成功', '测试', 'sk');
INSERT INTO `t_app_item` VALUES ('7', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:39:06', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '测试', 'qidian向萨菲;发送物品', '测试', '4', '发送成功', '测试', 'sk');
INSERT INTO `t_app_item` VALUES ('8', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:39:17', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '测试', 'qidian向萨菲;发送物品', '测试', '4', '发送成功', '测试', 'sk');
INSERT INTO `t_app_item` VALUES ('9', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:39:30', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '测试', 'qidian向萨菲;发送物品', '测试', '4', '发送成功', '测试', 'sk');
INSERT INTO `t_app_item` VALUES ('10', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:51:29', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('11', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:51:46', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('12', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:52:04', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('13', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:52:16', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('14', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:52:28', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('15', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:52:36', '[{\"itemId\":20033,\"type\":3,\"itemNum\":99}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('16', '1', 'qidian', '0', 'sk_0001', '萨菲;', '2017-09-18 16:53:20', '[{\"itemId\":20033,\"type\":3,\"itemNum\":396}]', '补偿', 'qidian向萨菲;发送物品', '补偿', '4', '发送成功', '补偿', 'sk');
INSERT INTO `t_app_item` VALUES ('17', '2', 'admin', '0', 'sk_0001', '亢飞;', '2017-10-24 11:59:57', '[{\"itemNum\":1,\"type\":1,\"itemId\":1140103}]', '测试', 'admin向亢飞;发送物品', '测试', '4', '发送成功', '测试', 'sk');
INSERT INTO `t_app_item` VALUES ('18', '2', 'admin', '0', 'sk_0001', '亢飞;', '2017-10-24 12:02:17', '[{\"itemNum\":1,\"type\":3,\"itemId\":25003}]', '测', 'admin向亢飞;发送物品', '测试', '4', '发送成功', '测试', 'sk');

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
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of t_opt_log
-- ----------------------------
INSERT INTO `t_opt_log` VALUES ('58', '2', 'admin', '1', '119.137.55.122', '用户登陆', 'admin登陆了', '2017-09-18 16:24:05');
INSERT INTO `t_opt_log` VALUES ('59', '1', 'qidian', '1', '119.137.55.122', '用户登陆', 'qidian登陆了', '2017-09-18 16:37:05');
INSERT INTO `t_opt_log` VALUES ('60', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:39:05');
INSERT INTO `t_opt_log` VALUES ('61', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:39:05');
INSERT INTO `t_opt_log` VALUES ('62', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:40:05');
INSERT INTO `t_opt_log` VALUES ('63', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:40:05');
INSERT INTO `t_opt_log` VALUES ('64', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:40:05');
INSERT INTO `t_opt_log` VALUES ('65', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:40:05');
INSERT INTO `t_opt_log` VALUES ('66', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:40:05');
INSERT INTO `t_opt_log` VALUES ('67', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:40:05');
INSERT INTO `t_opt_log` VALUES ('68', '1', 'qidian', '1', '119.137.55.122', '用户登陆', 'qidian登陆了', '2017-09-18 16:51:05');
INSERT INTO `t_opt_log` VALUES ('69', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:52:05');
INSERT INTO `t_opt_log` VALUES ('70', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:52:05');
INSERT INTO `t_opt_log` VALUES ('71', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:52:05');
INSERT INTO `t_opt_log` VALUES ('72', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:52:05');
INSERT INTO `t_opt_log` VALUES ('73', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('74', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('75', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('76', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('77', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('78', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('79', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*99;', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('80', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:53:05');
INSERT INTO `t_opt_log` VALUES ('81', '1', 'qidian', '302', '119.137.55.122', 'qidian向萨菲;发送物品', '大袋元宝*396;', '2017-09-18 16:54:05');
INSERT INTO `t_opt_log` VALUES ('82', '1', 'qidian', '2', '119.137.55.122', 'qidian审核通过该申请物品', 'qidiansk_0001向指定玩家【萨菲,】发送物品', '2017-09-18 16:54:05');
INSERT INTO `t_opt_log` VALUES ('83', '1', 'qidian', '1', '119.137.55.122', '用户登陆', 'qidian登陆了', '2017-09-19 10:52:05');
INSERT INTO `t_opt_log` VALUES ('84', '1', 'qidian', '810', '119.137.55.122', '停服维护', '{\"time\":1505789531582,\"endStopMin\":3,\"stopMin\":5,\"key\":\"D44DD8758DC11D6BAD115487C567FD07\"}', '2017-09-19 10:53:05');
INSERT INTO `t_opt_log` VALUES ('85', '1', 'qidian', '1', '119.137.54.52', '用户登陆', 'qidian登陆了', '2017-09-20 11:19:05');
INSERT INTO `t_opt_log` VALUES ('86', '1', 'qidian', '810', '119.137.54.52', '停服维护', '{\"time\":1505877558403,\"endStopMin\":5,\"stopMin\":5,\"key\":\"6B6102D1279BD8ADD740710FD1DE28BE\"}', '2017-09-20 11:20:05');
INSERT INTO `t_opt_log` VALUES ('87', '1', 'qidian', '1', '119.137.54.52', '用户登陆', 'qidian登陆了', '2017-09-21 16:51:05');
INSERT INTO `t_opt_log` VALUES ('88', '1', 'qidian', '810', '119.137.54.52', '停服维护', '{\"time\":1505983851083,\"endStopMin\":2,\"stopMin\":2,\"key\":\"C3374A3338314F117413F4CFD279C510\"}', '2017-09-21 16:51:05');
INSERT INTO `t_opt_log` VALUES ('89', '1', 'qidian', '1', '119.137.54.87', '用户登陆', 'qidian登陆了', '2017-09-22 09:49:05');
INSERT INTO `t_opt_log` VALUES ('90', '1', 'qidian', '810', '119.137.54.87', '停服维护', '{\"time\":1506044954644,\"endStopMin\":2,\"stopMin\":5,\"key\":\"492D22B8BFC06235EAB2CCF86A6182C3\"}', '2017-09-22 09:50:05');
INSERT INTO `t_opt_log` VALUES ('91', '1', 'qidian', '1', '119.137.54.87', '用户登陆', 'qidian登陆了', '2017-09-23 16:33:05');
INSERT INTO `t_opt_log` VALUES ('92', '1', 'qidian', '810', '119.137.54.87', '停服维护', '{\"time\":1506155565539,\"endStopMin\":2,\"stopMin\":5,\"key\":\"BAA1ABC37B65C7F4734665C1D4D0835F\"}', '2017-09-23 16:33:05');
INSERT INTO `t_opt_log` VALUES ('93', '1', 'qidian', '1', '119.137.54.18', '用户登陆', 'qidian登陆了', '2017-10-11 11:32:05');
INSERT INTO `t_opt_log` VALUES ('94', '1', 'qidian', '810', '119.137.54.18', '停服维护', '{\"time\":1507692695253,\"endStopMin\":1,\"stopMin\":5,\"key\":\"F941771B3A677CFB7E40EE2BA11F90AA\"}', '2017-10-11 11:32:05');
INSERT INTO `t_opt_log` VALUES ('95', '5', 'yunwei', '1', '119.137.54.85', '用户登陆', 'yunwei登陆了', '2017-10-23 21:36:05');
INSERT INTO `t_opt_log` VALUES ('96', '2', 'admin', '1', '119.137.54.85', '用户登陆', 'admin登陆了', '2017-10-23 21:37:05');
INSERT INTO `t_opt_log` VALUES ('97', '1', 'qidian', '1', '119.137.54.85', '用户登陆', 'qidian登陆了', '2017-10-23 21:38:05');
INSERT INTO `t_opt_log` VALUES ('98', '1', 'qidian', '810', '119.137.54.85', '停服维护', '{\"time\":1508765889794,\"endStopMin\":5,\"stopMin\":5,\"key\":\"A09736B707B908D5CC5F687FE4EDA024\"}', '2017-10-23 21:39:05');
INSERT INTO `t_opt_log` VALUES ('99', '2', 'admin', '1', '119.137.54.85', '用户登陆', 'admin登陆了', '2017-10-24 11:59:05');
INSERT INTO `t_opt_log` VALUES ('100', '2', 'admin', '302', '119.137.54.85', 'admin向亢飞;发送物品', '初心护头·传说*1;', '2017-10-24 12:01:05');
INSERT INTO `t_opt_log` VALUES ('101', '2', 'admin', '2', '119.137.54.85', 'admin审核通过该申请物品', 'adminsk_0001向指定玩家【亢飞,】发送物品', '2017-10-24 12:01:05');
INSERT INTO `t_opt_log` VALUES ('102', '2', 'admin', '302', '119.137.54.85', 'admin向亢飞;发送物品', '三倍经验药水(大)*1;', '2017-10-24 12:03:05');
INSERT INTO `t_opt_log` VALUES ('103', '2', 'admin', '2', '119.137.54.85', 'admin审核通过该申请物品', 'adminsk_0001向指定玩家【亢飞,】发送物品', '2017-10-24 12:03:05');
INSERT INTO `t_opt_log` VALUES ('104', '2', 'admin', '1', '119.137.52.164', '用户登陆', 'admin登陆了', '2017-10-28 15:32:05');
INSERT INTO `t_opt_log` VALUES ('105', '1', 'qidian', '1', '119.137.54.229', '用户登陆', 'qidian登陆了', '2017-11-11 16:28:05');
INSERT INTO `t_opt_log` VALUES ('106', '1', 'qidian', '810', '119.137.54.229', '停服维护', '{\"time\":1510388857353,\"endStopMin\":5,\"stopMin\":5,\"key\":\"3EDB0A1D8936F229DB75C6E78E73A6BD\"}', '2017-11-11 16:28:05');
INSERT INTO `t_opt_log` VALUES ('107', '1', 'qidian', '1', '119.137.55.253', '用户登陆', 'qidian登陆了', '2017-11-22 21:02:05');
INSERT INTO `t_opt_log` VALUES ('108', '1', 'qidian', '810', '119.137.55.253', '停服维护', '{\"time\":1511355696883,\"endStopMin\":5,\"stopMin\":5,\"key\":\"93CD28E0702D49573E05E87F1BC59E0C\"}', '2017-11-22 21:02:05');
INSERT INTO `t_opt_log` VALUES ('109', '1', 'qidian', '1', '119.137.55.253', '用户登陆', 'qidian登陆了', '2017-11-22 22:04:05');
INSERT INTO `t_opt_log` VALUES ('110', '1', 'qidian', '810', '119.137.55.253', '停服维护', '{\"time\":1511359454660,\"endStopMin\":2,\"stopMin\":2,\"key\":\"B5C3A04B31F3E51BFD8337C5BAF4B584\"}', '2017-11-22 22:05:05');
INSERT INTO `t_opt_log` VALUES ('111', '1', 'qidian', '1', '119.137.53.213', '用户登陆', 'qidian登陆了', '2017-12-11 21:25:05');
INSERT INTO `t_opt_log` VALUES ('112', '1', 'qidian', '810', '119.137.53.213', '停服维护', '{\"time\":1512998706719,\"endStopMin\":1,\"stopMin\":1,\"key\":\"673099F7D5F651DB2D2289A731E1686E\"}', '2017-12-11 21:26:05');
INSERT INTO `t_opt_log` VALUES ('113', '1', 'qidian', '1', '119.137.55.36', '用户登陆', 'qidian登陆了', '2017-12-14 20:54:05');
INSERT INTO `t_opt_log` VALUES ('114', '1', 'qidian', '810', '119.137.55.36', '停服维护', '{\"time\":1513256041595,\"endStopMin\":1,\"stopMin\":1,\"key\":\"3C80FE8163DE8366F56A7DFB6FF78503\"}', '2017-12-14 20:54:05');
INSERT INTO `t_opt_log` VALUES ('115', '5', 'yunwei', '1', '119.137.55.36', '用户登陆', 'yunwei登陆了', '2017-12-15 15:58:05');
INSERT INTO `t_opt_log` VALUES ('116', '1', 'qidian', '1', '119.137.55.36', '用户登陆', 'qidian登陆了', '2017-12-15 15:58:05');
INSERT INTO `t_opt_log` VALUES ('117', '1', 'qidian', '810', '119.137.55.36', '停服维护', '{\"time\":1513324724872,\"endStopMin\":2,\"stopMin\":2,\"key\":\"D5019DDCDA4B89648C019F7743A3AD14\"}', '2017-12-15 15:59:05');
INSERT INTO `t_opt_log` VALUES ('118', '5', 'yunwei', '1', '119.137.53.244', '用户登陆', 'yunwei登陆了', '2017-12-18 14:33:05');

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
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'qidian', 'ken123', 'sk', 'sk', '1', '1', '0', '2017-08-12 18:20:22');
INSERT INTO `t_user_dataanalysis` VALUES ('2', 'admin', 'sktb2017', 'sk', 'sk', '1', '1', '0', '2017-08-18 17:23:02');

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
INSERT INTO `t_user_gcc` VALUES ('1', 'qidian', 'ken123', 'sk', 'sk', '1', '1', '0', '2016-05-18 21:22:05');
INSERT INTO `t_user_gcc` VALUES ('2', 'admin', '123456', 'sk', 'sk', '2', '1', '0', '2017-07-25 18:59:36');
INSERT INTO `t_user_gcc` VALUES ('3', 'yunying', '123456', 'sk', 'sk', '3', '1', '0', '2017-07-25 19:00:43');
INSERT INTO `t_user_gcc` VALUES ('4', 'jishu', '123456', 'sk', 'sk', '4', '1', '0', '2017-07-25 19:01:35');
INSERT INTO `t_user_gcc` VALUES ('5', 'yunwei', '123456', 'sk', 'sk', '5', '1', '0', '2017-07-25 19:01:57');
