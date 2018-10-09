/*
Navicat MySQL Data Transfer

Source Server         : 外网106.75.222.211
Source Server Version : 50639
Source Host           : 106.75.222.211:3306
Source Database       : xh_gcc

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-09-30 23:31:33
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
) ENGINE=InnoDB AUTO_INCREMENT=1244877688836 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='账号表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('10000', 'c0f70255322758c736f0050624413292', '123456', null, '1', '0', '[1]', null, null, '2018-03-23 18:17:45', null);
INSERT INTO `account` VALUES ('10001', '0eb8f6781c854017ba3a994c4b26c34cc9c69ea5', '123456', null, '1', '0', '[1]', null, null, '2018-03-23 18:56:07', null);
INSERT INTO `account` VALUES ('10002', 'db22ef14f7de9a181fe9d28191155a3f', '123456', null, '1', '0', '[1]', null, null, '2018-03-23 19:08:20', null);
INSERT INTO `account` VALUES ('10003', '18680325320', '123456', null, '0', '0', '[1]', null, null, '2018-03-26 10:00:21', '2018-03-26 20:46:01');
INSERT INTO `account` VALUES ('10004', '13477771111', '123456', null, '0', '0', '[1]', null, null, '2018-03-26 21:24:36', '2018-05-17 20:17:23');
INSERT INTO `account` VALUES ('10005', '18290d3a32c225f2874f1b04d3f3b0ab', '123456', null, '1', '0', '[1]', null, null, '2018-03-26 21:30:36', null);
INSERT INTO `account` VALUES ('10006', '18811112222', '123456', null, '0', '0', '[1]', null, null, '2018-03-27 09:46:45', null);
INSERT INTO `account` VALUES ('10007', '18811113333', '123456', null, '0', '0', '[1]', null, null, '2018-03-27 09:49:00', null);
INSERT INTO `account` VALUES ('10008', '18811114444', '123456', null, '0', '0', '[1]', null, null, '2018-03-27 09:50:57', null);
INSERT INTO `account` VALUES ('10009', '13411112222', '123456', null, '0', '0', '[1]', null, null, '2018-03-27 09:55:13', null);
INSERT INTO `account` VALUES ('10010', 'aec97b5ba03481bcd4a02984955bff63b848c0f4', '123456', null, '1', '0', '[1]', null, null, '2018-04-10 20:33:54', null);
INSERT INTO `account` VALUES ('10011', '1cf74b02d8d3fb8a4ec6374af633b369ecb58f13', '123456', null, '1', '0', '[1]', null, null, '2018-04-11 16:34:37', null);
INSERT INTO `account` VALUES ('10012', '602208c2eac6e5ad45f650d1c57dcdad8a6f337f', '123456', null, '1', '0', '[1]', null, null, '2018-04-13 13:13:14', null);
INSERT INTO `account` VALUES ('10013', 'd2860eaeeb5a6644898c7defd973e294', '123456', null, '1', '0', '[1]', null, null, '2018-04-13 14:12:11', null);
INSERT INTO `account` VALUES ('10014', 'c096db37dbbc812ac2a2fcf32e95e22b', '123456', null, '1', '0', '[1]', null, null, '2018-04-13 14:23:48', null);
INSERT INTO `account` VALUES ('10015', 'ae58f577a51d0aaf242b539ec4d258bb131f872a', '123456', null, '1', '0', '[1]', null, null, '2018-04-21 17:27:04', null);
INSERT INTO `account` VALUES ('10016', '18811115555', '123456', null, '0', '0', '[1]', null, null, '2018-04-21 20:47:59', null);
INSERT INTO `account` VALUES ('10017', 'a08762a4af5d29ab968ae8e9fb377669f15641e5', '123456', null, '1', '0', null, null, null, '2018-05-06 20:03:43', null);
INSERT INTO `account` VALUES ('10018', 'e04aeead86c9db834de16e66f2e12ba3', '123456', null, '1', '0', '[1]', null, null, '2018-05-07 14:19:16', null);
INSERT INTO `account` VALUES ('10019', '196b0fcab5a3b3bb882ac4b0e22bbe62', '123456', null, '1', '0', '[1]', null, null, '2018-05-07 16:08:43', null);
INSERT INTO `account` VALUES ('10020', '13488881111', '123456', null, '0', '0', '[1]', null, null, '2018-05-08 10:42:30', null);
INSERT INTO `account` VALUES ('10021', '206d80030fd29bab51f2a2e6acadb2fa', '123456', null, '1', '0', '[1]', null, null, '2018-05-10 17:31:23', null);
INSERT INTO `account` VALUES ('10022', '18844444444', '123456', null, '0', '0', '[1]', null, null, '2018-05-10 18:00:38', null);
INSERT INTO `account` VALUES ('10023', '4005f3d74daeaa7117b5211d897124f1', '123456', null, '1', '0', '[1]', null, null, '2018-05-10 19:33:39', null);
INSERT INTO `account` VALUES ('10024', '1baebe563917dba54638e833a1f82211', '123456', null, '1', '0', '[1]', null, null, '2018-05-14 16:39:58', null);
INSERT INTO `account` VALUES ('10025', '06dfec0a26a84d01e1e3bdaeb070525d', '123456', null, '1', '0', '[1]', null, null, '2018-05-14 16:41:19', null);
INSERT INTO `account` VALUES ('10026', '4170ef53458a1b16a88d930c60bd7413', '123456', null, '1', '0', '[1]', null, null, '2018-05-14 18:18:34', null);
INSERT INTO `account` VALUES ('10027', '18600140500', '2212122', null, '0', '0', '[1]', null, null, '2018-05-15 18:10:59', null);
INSERT INTO `account` VALUES ('10028', 'f2d6825b068a9c8628bcf2346325c3c5', '123456', null, '1', '0', '[1]', null, null, '2018-05-15 18:17:32', null);
INSERT INTO `account` VALUES ('10029', '4e1d5221584c87ffc3d723225232ef6b', '123456', null, '1', '0', '[1]', null, null, '2018-05-15 18:17:48', null);
INSERT INTO `account` VALUES ('10030', 'a375d88b0da10a4df5cf7197b6ef8daa', '123456', null, '1', '0', '[1]', null, null, '2018-05-15 18:54:48', null);
INSERT INTO `account` VALUES ('10031', '6121334a29807ad053f4351f0062ed3f', '123456', null, '1', '0', '[1]', null, null, '2018-05-16 01:37:37', null);
INSERT INTO `account` VALUES ('10032', '72ce5f76c56acbb40db3de660d8f25d3', '123456', null, '1', '0', '[1]', null, null, '2018-05-16 18:10:31', null);
INSERT INTO `account` VALUES ('10033', '13499991111', '123456', null, '0', '0', '[1]', null, null, '2018-05-17 20:13:43', null);
INSERT INTO `account` VALUES ('10034', '745231393fe7ed568e8f0efc68749d20', '123456', null, '1', '0', '[1]', null, null, '2018-05-23 15:43:39', null);
INSERT INTO `account` VALUES ('10035', '979626115a09756e0e51e7114c294d89', '123456', null, '1', '0', '[1]', null, null, '2018-05-24 14:35:07', null);
INSERT INTO `account` VALUES ('10036', '7e86d2ab6d897a3b7f3687e42a0b31ad', '123456', null, '1', '0', '[1]', null, null, '2018-05-24 17:57:33', null);
INSERT INTO `account` VALUES ('10037', '21ef9440fb47ea63a616789e8cb67128', '123456', null, '1', '0', '[1]', null, null, '2018-05-25 10:44:16', null);
INSERT INTO `account` VALUES ('10038', '8a456207208c899065a84e184d7c60f6', '123456', null, '1', '0', '[1]', null, null, '2018-05-28 15:02:04', null);
INSERT INTO `account` VALUES ('10039', 'e7b3ce96d60bc1424d27f064442800126ba98024', '123456', null, '1', '0', '[1]', null, null, '2018-05-30 15:31:37', null);
INSERT INTO `account` VALUES ('10040', '9ec0352a2d02dd824b44793ddd696c75', '123456', null, '1', '0', '[1]', null, null, '2018-06-01 17:30:13', null);
INSERT INTO `account` VALUES ('10041', 'dadfd33b27cec46bcf00986c352ecba5', '123456', null, '1', '0', '[1]', null, null, '2018-06-01 18:23:45', null);
INSERT INTO `account` VALUES ('10042', '13111222220', '123456', null, '0', '0', '[1]', null, null, '2018-07-14 18:23:21', null);
INSERT INTO `account` VALUES ('10043', 'b5521ce6473e95024fbc5fa03e930acdb40d96f4', '123456', null, '1', '0', '[1]', null, null, '2018-07-14 20:26:31', null);
INSERT INTO `account` VALUES ('10044', '18088881111', '123456', null, '0', '0', '[1]', null, null, '2018-07-18 15:32:36', null);
INSERT INTO `account` VALUES ('10045', '18088882222', '123456', null, '0', '0', '[1]', null, null, '2018-07-18 15:36:26', null);
INSERT INTO `account` VALUES ('10046', '2eb8206cdfed5c8e7a7167da25971ab2', '123456', null, '1', '0', '[1]', null, null, '2018-07-18 15:39:05', null);
INSERT INTO `account` VALUES ('10047', '18088883333', '123456', null, '0', '0', '[1]', null, null, '2018-07-18 17:06:01', null);
INSERT INTO `account` VALUES ('10048', '13588881111', '123456', null, '0', '0', '[1]', null, null, '2018-07-18 19:08:20', null);
INSERT INTO `account` VALUES ('10049', '13588882222', '123456', null, '0', '0', '[1]', null, null, '2018-07-18 19:38:26', null);
INSERT INTO `account` VALUES ('10050', '13588883333', '123456', null, '0', '0', '[1]', null, null, '2018-07-19 09:47:04', null);
INSERT INTO `account` VALUES ('10051', '15916222528', '123456', null, '0', '0', '[1]', null, null, '2018-07-19 19:32:01', null);
INSERT INTO `account` VALUES ('10052', '13488882222', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 10:36:32', null);
INSERT INTO `account` VALUES ('10053', '13488883333', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 10:53:46', null);
INSERT INTO `account` VALUES ('10054', '13388881111', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 11:18:42', null);
INSERT INTO `account` VALUES ('10055', '13388882222', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 11:21:19', null);
INSERT INTO `account` VALUES ('10056', '13188881111', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 17:55:09', null);
INSERT INTO `account` VALUES ('10057', '13188882222', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 18:12:37', null);
INSERT INTO `account` VALUES ('10058', '13188883333', '123456', null, '0', '0', '[1]', null, null, '2018-07-23 18:23:30', null);
INSERT INTO `account` VALUES ('10059', '5ab5dc897db2b99b65a9aa6ca5ec11a9', '123456', null, '1', '0', '[1]', null, null, '2018-07-24 10:00:30', null);
INSERT INTO `account` VALUES ('10060', 'b0e0af78aa87b5c88a18564a3189fd96', '123456', null, '1', '0', '[1]', null, null, '2018-07-24 11:47:41', null);
INSERT INTO `account` VALUES ('10061', 'e4a89af22e4e9d510ac0d9c6911e2d5f', '123456', null, '1', '0', '[1]', null, null, '2018-07-24 15:15:48', null);
INSERT INTO `account` VALUES ('10062', '88fa6bf5aaaaa2b325176cdde2350539', '123456', null, '1', '0', '[1]', null, null, '2018-07-25 17:12:42', null);
INSERT INTO `account` VALUES ('10063', 'd51b303d09d1717a31ec91c75b2e51ea', '123456', null, '1', '0', '[1]', null, null, '2018-07-26 14:29:41', null);
INSERT INTO `account` VALUES ('10064', '9c829e456322b6fddb1bda90b3abfdce', '123456', null, '1', '0', '[1]', null, null, '2018-07-31 16:01:57', null);
INSERT INTO `account` VALUES ('10065', 'cf48645134b36eb9399a4242b77b3bf9', '123456', null, '1', '0', '[1]', null, null, '2018-07-31 16:43:33', null);
INSERT INTO `account` VALUES ('10066', '31dcfbfc095e0d240d1cdbd42a7c107d', '123456', null, '1', '0', '[1]', null, null, '2018-07-31 18:43:55', null);
INSERT INTO `account` VALUES ('10067', 'eefc1d12dd0b9f37174f4c601178272e', '123456', null, '1', '0', '[1]', null, null, '2018-08-02 12:17:11', null);
INSERT INTO `account` VALUES ('10068', '13114314167', 'bie199511230', null, '0', '0', '[1]', null, null, '2018-08-04 15:48:07', null);
INSERT INTO `account` VALUES ('10069', '630dc3ca70217624d9198183cdfc259e', '123456', null, '1', '0', '[1]', null, null, '2018-08-04 15:59:49', null);
INSERT INTO `account` VALUES ('10070', '67ac93b88a321a748f3d8e6e64deab61', '123456', null, '1', '0', '[1]', null, null, '2018-08-04 22:59:00', null);
INSERT INTO `account` VALUES ('10071', '2756a0213f2385a810501ec5f4722375', '123456', null, '1', '0', '[1]', null, null, '2018-08-06 16:35:18', null);
INSERT INTO `account` VALUES ('10072', 'dd1edc38c5b44098b1bb99ae77333de6', '123456', null, '1', '0', '[1]', null, null, '2018-08-06 18:17:39', null);
INSERT INTO `account` VALUES ('10073', '18601681721', '123456', null, '0', '0', '[1]', null, null, '2018-08-07 00:03:53', null);
INSERT INTO `account` VALUES ('10074', '7442b644624b998b4f4d699db9ce9d61', '123456', null, '1', '0', '[1]', null, null, '2018-08-07 17:12:33', null);
INSERT INTO `account` VALUES ('10075', 'a8ca28cf7f0e96c55756cbb00ee42273', '123456', null, '1', '0', '[1]', null, null, '2018-08-08 18:35:24', null);
INSERT INTO `account` VALUES ('10076', '3c127c1ae47730978d7d4679a55d8fa7', '123456', null, '1', '0', '[1]', null, null, '2018-08-10 14:10:04', null);
INSERT INTO `account` VALUES ('10077', '0c221114bd3537169cff69bbb377e031', '123456', null, '1', '0', '[1]', null, null, '2018-08-13 19:31:15', null);
INSERT INTO `account` VALUES ('10078', '73c3671db43b802522e045cd90f91f47', '123456', null, '1', '0', '[1]', null, null, '2018-08-15 21:43:47', null);
INSERT INTO `account` VALUES ('10079', '4cb56feac86f3957fd5d7dec4ae5fab1', '123456', null, '1', '0', '[1]', null, null, '2018-08-16 10:53:06', null);
INSERT INTO `account` VALUES ('1120842551297', 'e1e900cd773a0826e811345fc3a89557', '123456', null, '1', '0', null, null, null, '2018-08-17 18:23:39', null);
INSERT INTO `account` VALUES ('1120842551298', '766a4e4361627cb1917ccfa4841abb87', '123456', null, '1', '0', null, null, null, '2018-08-20 22:29:42', null);
INSERT INTO `account` VALUES ('1120842551299', 'c7fb04ca8fc6d3ddbb8902b16eba660c', '123456', null, '1', '0', null, null, null, '2018-08-23 11:11:59', null);
INSERT INTO `account` VALUES ('1120842551300', '310f5cdf26e8ade251d6c78ffc048fe8', '123456', null, '1', '0', null, null, null, '2018-08-23 14:41:20', null);
INSERT INTO `account` VALUES ('1120842551301', '8a431c3cdea68be42f2a887c31bbd27f', '123456', null, '1', '0', null, null, null, '2018-08-23 15:00:15', null);
INSERT INTO `account` VALUES ('1120842551302', '3634c98ebcc6d4b6b2a98e788aaa3479', '123456', null, '1', '0', null, null, null, '2018-08-23 15:07:23', null);
INSERT INTO `account` VALUES ('1120842551303', '18911650672', '123456789', null, '0', '0', null, null, null, '2018-08-23 15:08:33', null);
INSERT INTO `account` VALUES ('1120842551304', 'b89bf1bedfeab437011f305d612730f7', '123456', null, '1', '0', null, null, null, '2018-08-23 15:24:12', null);
INSERT INTO `account` VALUES ('1140270993409', '0de1639a318ca566577b41d4f87e10b1', '123456', null, '1', '0', null, null, null, '2018-08-24 15:05:28', null);
INSERT INTO `account` VALUES ('1140270993410', 'b6a4794526330cd32bd05c98747958e0', '123456', null, '1', '0', null, null, null, '2018-08-25 18:58:27', null);
INSERT INTO `account` VALUES ('1140270993411', '17679101034', '123456', null, '0', '0', null, null, null, '2018-08-27 15:31:58', null);
INSERT INTO `account` VALUES ('1140270993412', 'f165f1f25b444e34cfd1aae3a2fc7c10', '123456', null, '1', '0', '[1]', null, null, '2018-08-28 14:23:24', null);
INSERT INTO `account` VALUES ('1140270993413', 'c1c87ec04f87f00e7d5e2833dd649186', '123456', null, '1', '0', null, null, null, '2018-08-28 14:28:07', null);
INSERT INTO `account` VALUES ('1140270993414', 'ce23d9ce09b3098feaa859406b6a13bc', '123456', null, '1', '0', '[1]', null, null, '2018-08-28 16:00:21', null);
INSERT INTO `account` VALUES ('1140270993415', '1340A09E-1C6A-5C72-9E41-3E587E648623', '123456', null, '1', '0', '[1]', null, null, '2018-08-28 16:44:00', null);
INSERT INTO `account` VALUES ('1140270993416', '397d1159053345fddd093f4e470f8d3e', '123456', null, '1', '0', '[1]', null, null, '2018-08-28 19:21:29', null);
INSERT INTO `account` VALUES ('1140270993417', '3cc7ba1afd8fe7c7623a873172a4c0e7', '123456', null, '1', '0', '[1]', null, null, '2018-08-29 11:35:39', null);
INSERT INTO `account` VALUES ('1140270993418', 'aeae8594a2263eee81fa21741eaba096', '123456', null, '1', '0', '[1]', null, null, '2018-08-29 13:15:12', null);
INSERT INTO `account` VALUES ('1140270993419', 'b1ee55aa48f86b3aecd737d7397ea206', '123456', null, '1', '0', '[1]', null, null, '2018-08-29 14:12:10', null);
INSERT INTO `account` VALUES ('1140270993420', '0ed4994b2621d4812b92dbeca9a7fec5', '123456', null, '1', '0', '[1]', null, null, '2018-08-30 11:11:45', null);
INSERT INTO `account` VALUES ('1140270993421', 'ca8a6f3f0b13631d2325bb8c8cd5a5a2', '123456', null, '1', '0', '[1]', null, null, '2018-08-30 15:02:14', null);
INSERT INTO `account` VALUES ('1168908357633', '99e6de72b1deacb64df4d97837d8a272', '123456', null, '1', '0', '[1]', null, null, '2018-09-03 17:51:12', null);
INSERT INTO `account` VALUES ('1168908357634', '3b4bd47cd81e218dd2211416ce98756a', '123456', null, '1', '0', null, null, null, '2018-09-03 18:00:30', null);
INSERT INTO `account` VALUES ('1168908357635', 'e56092bbc19c76accc4d88f76eaedb35', '123456', null, '1', '0', null, null, null, '2018-09-03 18:16:19', null);
INSERT INTO `account` VALUES ('1168908357636', '4bc916342765fb758c262f5d86c23cc7', '123456', null, '1', '0', null, null, null, '2018-09-03 19:45:32', null);
INSERT INTO `account` VALUES ('1168908357637', '15757047016', '123456', null, '0', '0', null, null, null, '2018-09-03 19:46:24', null);
INSERT INTO `account` VALUES ('1168908357638', '27e68319ffc23d75550f4577754f4a34', '123456', null, '1', '0', null, null, null, '2018-09-03 19:51:14', null);
INSERT INTO `account` VALUES ('1168908357639', '13234354545', '123456', null, '0', '0', null, null, null, '2018-09-03 19:51:49', null);
INSERT INTO `account` VALUES ('1168908357640', '1211719d7ac30471f23284fc2479fab7', '123456', null, '1', '0', null, null, null, '2018-09-04 10:51:31', null);
INSERT INTO `account` VALUES ('1168908357641', 'abeaac167762dd1506186bb666c1f7ec', '123456', null, '1', '0', null, null, null, '2018-09-04 12:15:10', null);
INSERT INTO `account` VALUES ('1168908357642', '5ac57fdf2f042eff38e9e4bb498dcd80', '123456', null, '1', '0', null, null, null, '2018-09-04 13:28:38', null);
INSERT INTO `account` VALUES ('1168908357643', 'b6950fa461a0f50f61bd79b3c946dd79', '123456', null, '1', '0', null, null, null, '2018-09-04 15:47:04', null);
INSERT INTO `account` VALUES ('1168908357644', '457d711460284b3dacfd50d2293d09e6', '123456', null, '1', '0', null, null, null, '2018-09-04 16:10:32', null);
INSERT INTO `account` VALUES ('1168908357645', '0c89079ec80e18acbe3e9866d9304667', '123456', null, '1', '0', null, null, null, '2018-09-04 16:21:18', null);
INSERT INTO `account` VALUES ('1168908357646', 'fdfa559307da2045c6ea15f37235a6b7', '123456', null, '1', '0', null, null, null, '2018-09-04 16:41:04', null);
INSERT INTO `account` VALUES ('1168908357647', '13554953540', '4826213a', null, '0', '0', null, null, null, '2018-09-04 16:43:42', null);
INSERT INTO `account` VALUES ('1168908357648', 'fb463d74c4f23a42fa8c26366b58b8dd', '123456', null, '1', '0', null, null, null, '2018-09-04 17:18:32', null);
INSERT INTO `account` VALUES ('1168908357649', '388cbff94ef1b6d4c85115d02f284a6c', '123456', null, '1', '0', null, null, null, '2018-09-04 17:23:19', null);
INSERT INTO `account` VALUES ('1168908357650', '9dad0d1f746ff8780e65858592869fd5', '123456', null, '1', '0', null, null, null, '2018-09-04 17:26:18', null);
INSERT INTO `account` VALUES ('1173729349633', '6b8939eae87f86e8128257812a45b1cb', '123456', null, '1', '0', null, null, null, '2018-09-05 10:43:17', null);
INSERT INTO `account` VALUES ('1173729349634', '352d3d74196f4a9f48d60768d376948d', '123456', null, '1', '0', null, null, null, '2018-09-05 10:57:25', null);
INSERT INTO `account` VALUES ('1173729349635', '6215462c1e0ca2b7c57c427163aca6ce', '123456', null, '1', '0', null, null, null, '2018-09-05 11:15:05', null);
INSERT INTO `account` VALUES ('1173729349636', '18652458413', 'qq1111', null, '0', '0', '[1]', null, null, '2018-09-05 11:15:36', null);
INSERT INTO `account` VALUES ('1173729349637', '17610246032', 'a842399275', null, '0', '0', null, null, null, '2018-09-05 12:05:22', null);
INSERT INTO `account` VALUES ('1173729349638', '9f15e5470d1eaeca3f25692a0f3e71c2', '123456', null, '1', '0', null, null, null, '2018-09-05 12:11:24', null);
INSERT INTO `account` VALUES ('1173729349639', '861c3516a320dc45186a43d0c9a8d0a5', '123456', null, '1', '0', null, null, null, '2018-09-05 14:29:55', null);
INSERT INTO `account` VALUES ('1173729349640', 'cc3c0bb7b0d8d3a2adb194d8d67e5a1f', '123456', null, '1', '0', null, null, null, '2018-09-05 14:33:03', null);
INSERT INTO `account` VALUES ('1173729349641', '9be55d0bc2888e1f8c92c68506f984b6', '123456', null, '1', '0', null, null, null, '2018-09-05 15:01:40', null);
INSERT INTO `account` VALUES ('1173729349642', 'fffe08faf9f5f4a6f576315c06d2de34', '123456', null, '1', '0', null, null, null, '2018-09-05 15:08:15', null);
INSERT INTO `account` VALUES ('1173729349643', 'f453452b2e0de77bd8dc2edd6ca6efd1', '123456', null, '1', '0', null, null, null, '2018-09-05 15:22:21', null);
INSERT INTO `account` VALUES ('1173729349644', 'd307e384ec3e3e1c25614d5e26942b8f', '123456', null, '1', '0', null, null, null, '2018-09-05 15:23:00', null);
INSERT INTO `account` VALUES ('1173729349645', '58300e202b21dcbcf72fa56df606a137', '123456', null, '1', '0', null, null, null, '2018-09-05 15:25:24', null);
INSERT INTO `account` VALUES ('1173729349646', '81568a1b42663f6993030fed8f67fb50', '123456', null, '1', '0', null, null, null, '2018-09-05 15:29:32', null);
INSERT INTO `account` VALUES ('1173729349647', 'e7d961ef2c88bcd24cfe61146e0d1e3c', '123456', null, '1', '0', null, null, null, '2018-09-05 15:47:52', null);
INSERT INTO `account` VALUES ('1173729349648', 'e858fc8c2ccaf68d7e1e60814550e576', '123456', null, '1', '0', null, null, null, '2018-09-05 16:04:08', null);
INSERT INTO `account` VALUES ('1173729349649', '436185bbb039d359df510d307b5611c7', '123456', null, '1', '0', null, null, null, '2018-09-05 16:07:19', null);
INSERT INTO `account` VALUES ('1173729349650', 'bb250058d869cce9cf262f1673dddea8', '123456', null, '1', '0', null, null, null, '2018-09-05 16:09:10', null);
INSERT INTO `account` VALUES ('1173729349651', 'dfb95bf2496080f739632df0f810779f', '123456', null, '1', '0', null, null, null, '2018-09-05 16:14:13', null);
INSERT INTO `account` VALUES ('1173729349652', '52aee72318059fd83062489b91cce4b1', '123456', null, '1', '0', '[1]', null, null, '2018-09-05 16:21:44', null);
INSERT INTO `account` VALUES ('1173729349653', '13250535879', 'ly911004', null, '0', '0', null, null, null, '2018-09-05 16:24:42', null);
INSERT INTO `account` VALUES ('1173729349654', '13855558888', '135136137138139', null, '0', '0', null, null, null, '2018-09-05 16:33:47', null);
INSERT INTO `account` VALUES ('1173729349655', '13827258365', '123456', null, '0', '0', null, null, null, '2018-09-05 16:35:58', null);
INSERT INTO `account` VALUES ('1173729349656', '60615d6e62569e84a011960597f79c85', '123456', null, '1', '0', null, null, null, '2018-09-05 16:41:50', null);
INSERT INTO `account` VALUES ('1173729349657', '48d0a246860f82726e4446895cc9a374', '123456', null, '1', '0', null, null, null, '2018-09-05 16:56:11', null);
INSERT INTO `account` VALUES ('1173729349658', '15820247332', '123456', null, '0', '0', null, null, null, '2018-09-05 16:57:09', null);
INSERT INTO `account` VALUES ('1173729349659', 'ed52a3fa06bc5c4f06647c646ec7faec', '123456', null, '1', '0', null, null, null, '2018-09-05 17:36:00', null);
INSERT INTO `account` VALUES ('1173729349660', '35b6e17687a0f5f5a199718ca4decc65', '123456', null, '1', '0', null, null, null, '2018-09-05 17:40:12', null);
INSERT INTO `account` VALUES ('1173729349661', 'a768fe6ec1fe53a7f3d79097ac405438', '123456', null, '1', '0', '[1]', null, null, '2018-09-05 19:02:49', null);
INSERT INTO `account` VALUES ('1173729349662', '4688cfefc9afaf4cb4eb19ecc2c7dd7a', '123456', null, '1', '0', null, null, null, '2018-09-05 19:28:40', null);
INSERT INTO `account` VALUES ('1173729349663', '66cffead575ec398d4be9f606841eb94', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 11:18:17', null);
INSERT INTO `account` VALUES ('1173729349664', '905eba5061badc5c73fb835ae41bb259', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 11:55:01', null);
INSERT INTO `account` VALUES ('1173729349665', 'd36f91b871db51f4c71893a95a54bc5c', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 13:22:02', null);
INSERT INTO `account` VALUES ('1173729349666', '3ccd242f2a2e09810c9c284e60a75990', '123456', null, '1', '0', null, null, null, '2018-09-06 15:51:11', null);
INSERT INTO `account` VALUES ('1173729349667', '18696316507', '123456', null, '0', '0', '[1]', null, null, '2018-09-06 15:52:37', null);
INSERT INTO `account` VALUES ('1173729349668', 'd4fbbbed10a21884b071bd6f7f8c70ff', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 16:24:40', null);
INSERT INTO `account` VALUES ('1173729349669', '14fda4946f791bf7264b2735d72b4298', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 16:37:36', null);
INSERT INTO `account` VALUES ('1173729349670', '15313921389', '123456', null, '0', '0', '[1]', null, null, '2018-09-06 16:40:13', null);
INSERT INTO `account` VALUES ('1173729349671', '13422223333', '123456', null, '0', '0', '[1]', null, null, '2018-09-06 16:47:10', null);
INSERT INTO `account` VALUES ('1173729349672', '1c83d1b3442f17bea6df3735b8f2ad43', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 17:39:40', null);
INSERT INTO `account` VALUES ('1173729349673', '46e71516be8e38f363261e9102f3101c', '123456', null, '1', '0', '[1]', null, null, '2018-09-06 17:53:55', null);
INSERT INTO `account` VALUES ('1173729349674', '18320825121', '123123', null, '0', '0', '[1]', null, null, '2018-09-06 17:57:31', null);
INSERT INTO `account` VALUES ('1173729349675', '3c85982832229c16af67bb7e71d850fb', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 09:46:46', null);
INSERT INTO `account` VALUES ('1173729349676', '6628ccc328ed216e5b0376c50bfae987', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 10:17:47', null);
INSERT INTO `account` VALUES ('1173729349677', 'e87428bd224f91007967339d86a66bba', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 10:24:47', null);
INSERT INTO `account` VALUES ('1173729349678', '13088881111', '123456', null, '0', '0', '[1]', null, null, '2018-09-07 10:38:07', null);
INSERT INTO `account` VALUES ('1173729349679', 'f3b7d8cd5252b35f588fd4ae4d80067d', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 13:48:10', null);
INSERT INTO `account` VALUES ('1173729349680', '563ccab2ada19063d65e4c588b38acf8', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 14:16:16', null);
INSERT INTO `account` VALUES ('1173729349681', '13088882222', '123456', null, '0', '0', '[1]', null, null, '2018-09-07 14:17:00', null);
INSERT INTO `account` VALUES ('1173729349682', '83cb42feb6f764c3cfa09f73dfed91b4', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 14:23:50', null);
INSERT INTO `account` VALUES ('1173729349683', '13088883333', '123456', null, '0', '0', '[1]', null, null, '2018-09-07 14:35:40', null);
INSERT INTO `account` VALUES ('1173729349684', '3445f986fba1a224bcb203b9aef6b692', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 14:47:03', null);
INSERT INTO `account` VALUES ('1173729349685', '26baee1dcaea613af3f474007ddf94c9', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 16:21:17', null);
INSERT INTO `account` VALUES ('1173729349686', 'b308a722772a620e3771711dccd33815', '123456', null, '1', '0', '[1]', null, null, '2018-09-07 16:26:37', null);
INSERT INTO `account` VALUES ('1173729349687', 'f68f2ea94d47c49199d1fac3695493dd', '123456', null, '1', '0', '[1]', null, null, '2018-09-10 09:36:55', null);
INSERT INTO `account` VALUES ('1173729349688', '0dcb33f1e6a7cd403b1bb27dd54cc008', '123456', null, '1', '0', '[1]', null, null, '2018-09-10 09:46:51', null);
INSERT INTO `account` VALUES ('1173729349689', 'e28e88317a5498782b2c97207fc09a38', '123456', null, '1', '0', '[1]', null, null, '2018-09-10 10:31:27', null);
INSERT INTO `account` VALUES ('1173729349690', '86fbbb5c4ee89774463f4f107f75116d', '123456', null, '1', '0', '[1]', null, null, '2018-09-10 13:41:02', null);
INSERT INTO `account` VALUES ('1173729349691', 'f51652a5050cb441296b4eb46734499a', '123456', null, '1', '0', '[1]', null, null, '2018-09-10 23:17:23', null);
INSERT INTO `account` VALUES ('1173729349692', 'fe266bf28eec5fea6a3d252b98e832ed', '123456', null, '1', '0', '[1]', null, null, '2018-09-11 10:09:25', null);
INSERT INTO `account` VALUES ('1173729349693', '922c793e49c707a73f5f85005832077d', '123456', null, '1', '0', '[1]', null, null, '2018-09-11 11:34:55', null);
INSERT INTO `account` VALUES ('1173729349694', '709fc64db06e8f1b036c04bb5aed5c44', '123456', null, '1', '0', '[1]', null, null, '2018-09-11 15:10:17', null);
INSERT INTO `account` VALUES ('1173729349695', '3eace72a4ef9512a4bddfc3c3d8c60ab', '123456', null, '1', '0', '[1]', null, null, '2018-09-11 19:44:04', null);
INSERT INTO `account` VALUES ('1173729349696', '3717d7805730d3ea8fe0253f8e276c91', '123456', null, '1', '0', '[1]', null, null, '2018-09-12 17:12:14', null);
INSERT INTO `account` VALUES ('1173729349697', 'e54b8814ef19f376d44ebfb771aa2b0f', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 11:27:05', null);
INSERT INTO `account` VALUES ('1173729349698', '7c8ee96fa923bb1122b7333b301432e6', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 15:46:15', null);
INSERT INTO `account` VALUES ('1173729349699', '31f658ed18bc28ff166efaa3a2226e6e', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 16:02:41', null);
INSERT INTO `account` VALUES ('1173729349700', '0dc8fb9040d0abdce90e63ce139f191f', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 16:31:54', null);
INSERT INTO `account` VALUES ('1173729349701', '95d57e1912702c511653f7810ab4d348', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 17:03:44', null);
INSERT INTO `account` VALUES ('1173729349702', 'bf777cdf087e0401c16e8a0adbf74c76', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 17:13:15', null);
INSERT INTO `account` VALUES ('1173729349703', 'e0d15936fd8081d331cfa0198e7690c6', '123456', null, '1', '0', '[1]', null, null, '2018-09-13 18:21:02', null);
INSERT INTO `account` VALUES ('1173729349704', '903cf05d7643bb75ea90470bdeebeca5', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 11:36:14', null);
INSERT INTO `account` VALUES ('1173729349705', 'ee665a97a16d4aee2138f9539e79be8d', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 13:11:12', null);
INSERT INTO `account` VALUES ('1173729349706', '04688846b26b2d660b623edc2c9ed5b6', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 18:39:59', null);
INSERT INTO `account` VALUES ('1173729349707', '4f72600d045cc7c3c4623b9dfbf1c96a', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 18:50:08', null);
INSERT INTO `account` VALUES ('1173729349708', '19c55bfdedbdd024adae877c18f997eb', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 19:19:39', null);
INSERT INTO `account` VALUES ('1173729349709', '63ae9d3cc9b7e1f8317ebabe7d234b34', '123456', null, '1', '0', '[1]', null, null, '2018-09-14 19:30:35', null);
INSERT INTO `account` VALUES ('1173729349710', 'c6dd1fd7dc5d50a8486dafb5d8d38966', '123456', null, '1', '0', '[1]', null, null, '2018-09-15 09:52:47', null);
INSERT INTO `account` VALUES ('1173729349711', '38a78764e58deee4a46f058d22f0d794', '123456', null, '1', '0', '[1]', null, null, '2018-09-15 10:02:45', null);
INSERT INTO `account` VALUES ('1173729349712', 'bd42956295d79cab9ddd3a6a3e134aca', '123456', null, '1', '0', '[1]', null, null, '2018-09-17 14:49:48', null);
INSERT INTO `account` VALUES ('1173729349713', 'bd07336932e1dda400d2bebd45b0fb1a', '123456', null, '1', '0', '[1]', null, null, '2018-09-17 15:14:21', null);
INSERT INTO `account` VALUES ('1173729349714', '118b5ec9f626d55fddd2cb1dbb13c8ff', '123456', null, '1', '0', '[1]', null, null, '2018-09-17 18:25:47', null);
INSERT INTO `account` VALUES ('1173729349715', 'fb7bcefc09c45cf6baad156d209e9d85', '123456', null, '1', '0', '[1]', null, null, '2018-09-17 19:29:10', null);
INSERT INTO `account` VALUES ('1173729349716', '3775fc7de49d50f01bcecf16fa34e8ad', '123456', null, '1', '0', '[1]', null, null, '2018-09-17 19:42:41', null);
INSERT INTO `account` VALUES ('1173729349717', 'abaf8ce84a94f0ff5516a462da371481', '123456', null, '1', '0', '[1]', null, null, '2018-09-18 10:05:23', null);
INSERT INTO `account` VALUES ('1213768273921', 'b7ed66dd1c7e3508db86bf044752c715', '123456', null, '1', '0', '[1]', null, null, '2018-09-19 14:08:08', null);
INSERT INTO `account` VALUES ('1213768273922', 'db7651ad124423e2fbbc28cb8597b6c7', '123456', null, '1', '0', '[1]', null, null, '2018-09-19 14:25:36', null);
INSERT INTO `account` VALUES ('1213768273923', '372e684467cee6e69aea55499b98d20a77d1fae1', '123456', null, '1', '0', '[1]', null, null, '2018-09-19 21:03:32', null);
INSERT INTO `account` VALUES ('1213768273924', '29477168cdf7c689b695e3f6e86cbe73', '123456', null, '1', '0', '[1]', null, null, '2018-09-20 11:08:30', null);
INSERT INTO `account` VALUES ('1213768273925', '6ebb8046ed274595b90c5057c8ee740e', '123456', null, '1', '0', '[1]', null, null, '2018-09-20 11:18:59', null);
INSERT INTO `account` VALUES ('1213768273926', 'c7e5e1e2cc6ab303a601abec33e407ea', '123456', null, '1', '0', '[1]', null, null, '2018-09-20 11:20:09', null);
INSERT INTO `account` VALUES ('1213768273927', '7df813004eb875e6a282cb286622c054', '123456', null, '1', '0', '[1]', null, null, '2018-09-20 14:13:48', null);
INSERT INTO `account` VALUES ('1213768273928', '30ff7331f701effdb490482d3c98eed3', '123456', null, '1', '0', '[1]', null, null, '2018-09-20 14:18:24', null);
INSERT INTO `account` VALUES ('1217668124673', '17512013305', 'wanghui', null, '0', '0', null, null, null, '2018-09-20 23:11:42', null);
INSERT INTO `account` VALUES ('1217668124674', '8e1ff629fcfdaff07a71013e041bc550', '123456', null, '1', '0', '[1]', null, null, '2018-09-21 10:24:18', null);
INSERT INTO `account` VALUES ('1230703300609', '13307126940', 'qw1234', null, '0', '0', null, null, null, '2018-09-25 13:41:44', null);
INSERT INTO `account` VALUES ('1230703300610', '13597976611', '123456', null, '0', '0', null, null, null, '2018-09-26 10:13:03', null);
INSERT INTO `account` VALUES ('1230703300611', '13597976622', '123456', null, '0', '0', null, null, null, '2018-09-26 10:14:21', null);
INSERT INTO `account` VALUES ('1244877688833', '15392883540', 'qw.7852346', null, '0', '0', null, null, null, '2018-09-30 13:51:11', null);
INSERT INTO `account` VALUES ('1244877688834', '18664988376', '123456', null, '0', '0', '[1]', null, null, '2018-09-30 17:45:07', '2018-09-30 19:34:17');
INSERT INTO `account` VALUES ('1244877688835', '13155551111', '123456', null, '0', '0', '[1]', null, null, '2018-09-30 18:49:44', null);

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
INSERT INTO `config_agent` VALUES ('xh', 'QYQDGAMEDshEFWOKE7Y6GAEDE-WAN-0668-2625-7DGAMESZEFovDDe777', 'QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2', 'DGAMEASDWDSHEN-16TQASDEDE-W33TT', 'CN', 'zh', '10.25.219.23', '4', 'http://10.25.219.23:9800/code', 'http://10.25.219.23:9000/pay', 'http://10.25.219.23:8000', '0', '1');

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
INSERT INTO `config_server` VALUES ('1', '大唐诛仙-1服', 'xh', 'xh_0001', '106.75.222.211', '10.25.219.23', '8500', '9500', '', '2018-08-23 10:00:00', null, '1', '3', '2', null, '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_app_item
-- ----------------------------
INSERT INTO `t_app_item` VALUES ('1', '1', 'qidian', '1', 'xh_0001', 'null', '2018-09-06 17:07:30', '[{\"type\":3,\"itemNum\":99,\"itemId\":20033}]', 'testtesttesttest', 'qidian向所有玩家发送[{\"type\":3,\"itemNum\":99,\"itemId\":20033}]', 'testtesttesttesttest', '4', '发送成功', 'test', 'xh');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of t_opt_log
-- ----------------------------
INSERT INTO `t_opt_log` VALUES ('1', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-06 17:05:05');
INSERT INTO `t_opt_log` VALUES ('2', '1', 'qidian', '303', 'null', '全服发送物品', '{\"optType\":303,\"title\":\"test\",\"anex\":\"[{\"type\":3,\"itemNum\":99,\"itemId\":20033}]\",\"content\":\"testtesttesttest\",\"url\":\"http://10.25.219.23:9500/item\"}', '2018-09-06 17:08:05');
INSERT INTO `t_opt_log` VALUES ('3', '1', 'qidian', '2', 'null', 'qidian审核通过该申请物品', 'qidianxh_0001的所有玩家发送道具为', '2018-09-06 17:08:05');
INSERT INTO `t_opt_log` VALUES ('4', '1', 'qidian', '1', 'null', '用户登陆', 'qidian登陆了', '2018-09-06 19:07:05');

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
INSERT INTO `t_user_dataanalysis` VALUES ('1', 'qidian', 'ken123', 'xh', 'xh', '1', '1', '0', '2017-08-12 18:20:22');
INSERT INTO `t_user_dataanalysis` VALUES ('2', 'admin', 'sktb2017', 'xh', 'xh', '1', '1', '0', '2017-08-18 17:23:02');
INSERT INTO `t_user_dataanalysis` VALUES ('3', 'yunying', '123456', 'xh', 'xh', '1', '1', '0', '2018-09-26 18:12:39');

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
