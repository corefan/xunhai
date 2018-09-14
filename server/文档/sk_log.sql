/*
Navicat MySQL Data Transfer

Source Server         : 外网正式服101.132.109.250
Source Server Version : 50173
Source Host           : 101.132.109.250:3306
Source Database       : sk_log

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-12-19 20:38:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_cost_log
-- ----------------------------
DROP TABLE IF EXISTS `t_cost_log`;
CREATE TABLE `t_cost_log` (
  `logId` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '账号编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `playerName` varchar(64) NOT NULL COMMENT '角色名称',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '资源类型',
  `costName` varchar(64) NOT NULL COMMENT '名称',
  `value` int(11) NOT NULL COMMENT '值',
  `createTime` datetime NOT NULL COMMENT '时间',
  `agent` varchar(30) NOT NULL COMMENT '运营商',
  `gameSite` varchar(30) NOT NULL COMMENT '站点',
  PRIMARY KEY (`logId`),
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `gameSite` (`gameSite`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='元宝消耗表';

-- ----------------------------
-- Table structure for t_five_online_log
-- ----------------------------
DROP TABLE IF EXISTS `t_five_online_log`;
CREATE TABLE `t_five_online_log` (
  `logId` bigint(20) NOT NULL COMMENT '日志编号',
  `num` int(6) NOT NULL DEFAULT '0' COMMENT '在线数量',
  `regNum` int(11) NOT NULL DEFAULT '0' COMMENT '当前注册数',
  `pNum` int(11) NOT NULL DEFAULT '0' COMMENT '当前创角用户数',
  `createNum` int(11) NOT NULL DEFAULT '0' COMMENT '当前总创角数',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `agent` varchar(30) NOT NULL COMMENT '运营商',
  `gameSite` varchar(30) NOT NULL COMMENT '站点',
  PRIMARY KEY (`logId`),
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `gameSite` (`gameSite`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_game_step_log
-- ----------------------------
DROP TABLE IF EXISTS `t_game_step_log`;
CREATE TABLE `t_game_step_log` (
  `logId` bigint(20) NOT NULL,
  `playerId` bigint(20) NOT NULL COMMENT '玩家ID',
  `taskId` int(11) NOT NULL COMMENT '主线任务编号',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `agent` varchar(30) NOT NULL COMMENT '平台',
  `game_site` varchar(30) NOT NULL COMMENT '站点',
  PRIMARY KEY (`logId`),
  KEY `taskId` (`taskId`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `game_site` (`game_site`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏节点';

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `logId` bigint(20) NOT NULL COMMENT '日志编号',
  `userId` int(11) NOT NULL COMMENT '账号编号',
  `createTime` datetime NOT NULL COMMENT '登录时间',
  `playerNum` tinyint(2) NOT NULL DEFAULT '0' COMMENT '角色数量',
  `agent` varchar(30) NOT NULL COMMENT '平台',
  `game_site` varchar(30) NOT NULL COMMENT '站点',
  PRIMARY KEY (`logId`),
  KEY `userId` (`userId`) USING BTREE,
  KEY `playerNum` (`playerNum`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `game_site` (`game_site`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录日志';

-- ----------------------------
-- Table structure for t_market_log
-- ----------------------------
DROP TABLE IF EXISTS `t_market_log`;
CREATE TABLE `t_market_log` (
  `logId` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '账号编号',
  `playerId` bigint(20) NOT NULL COMMENT '玩家编号',
  `playerName` varchar(64) NOT NULL COMMENT '角色名称',
  `itemId` int(11) NOT NULL DEFAULT '0' COMMENT '物品编号',
  `itemName` varchar(64) NOT NULL COMMENT '物品名称',
  `price` int(11) NOT NULL COMMENT '花费元宝（数量*单价）',
  `num` int(11) NOT NULL COMMENT '购买数量',
  `createTime` datetime NOT NULL COMMENT '时间',
  `agent` varchar(30) NOT NULL COMMENT '运营商',
  `gameSite` varchar(30) NOT NULL DEFAULT '' COMMENT '站点',
  PRIMARY KEY (`logId`),
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `gameSite` (`gameSite`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商城日志表';

-- ----------------------------
-- Table structure for t_onlinetime_log
-- ----------------------------
DROP TABLE IF EXISTS `t_onlinetime_log`;
CREATE TABLE `t_onlinetime_log` (
  `logId` bigint(20) NOT NULL,
  `loginNum` int(11) NOT NULL DEFAULT '0' COMMENT '登录人数',
  `num1` int(11) NOT NULL DEFAULT '0' COMMENT '1分钟人数',
  `num5` int(11) NOT NULL DEFAULT '0' COMMENT '5分钟人数',
  `num10` int(11) NOT NULL DEFAULT '0' COMMENT '10分钟人数',
  `num20` int(11) NOT NULL DEFAULT '0' COMMENT '20分钟人数',
  `num30` int(11) NOT NULL DEFAULT '0' COMMENT '30分钟人数',
  `num40` int(11) NOT NULL DEFAULT '0' COMMENT '40分钟人数',
  `num50` int(11) NOT NULL DEFAULT '0' COMMENT '50分钟人数',
  `num60` int(11) NOT NULL DEFAULT '0' COMMENT '60分钟人数',
  `h5` int(11) NOT NULL DEFAULT '0' COMMENT '5小时人数',
  `h10` int(11) NOT NULL DEFAULT '0' COMMENT '10小时人数',
  `uph10` int(11) NOT NULL DEFAULT '0' COMMENT '10小时以上人数',
  `createTime` datetime NOT NULL COMMENT '时间',
  `agent` varchar(30) NOT NULL COMMENT '运营商',
  `gameSite` varchar(30) NOT NULL COMMENT '站点',
  PRIMARY KEY (`logId`),
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `gameSite` (`gameSite`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日在线时长记录';

-- ----------------------------
-- Table structure for t_register_log
-- ----------------------------
DROP TABLE IF EXISTS `t_register_log`;
CREATE TABLE `t_register_log` (
  `logId` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '账号编号',
  `createTime` datetime NOT NULL COMMENT '时间',
  `agent` varchar(30) NOT NULL COMMENT '运营商',
  `gameSite` varchar(30) NOT NULL COMMENT '站点',
  PRIMARY KEY (`logId`),
  UNIQUE KEY `userId-gameSite` (`userId`,`gameSite`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `gameSite` (`gameSite`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='注册记录';

-- ----------------------------
-- Procedure structure for sp_act_instance_log_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_act_instance_log_count`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_act_instance_log_count`(dbgamename VARCHAR(30))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE sqlstr  VARCHAR(200) ;
  
  SET v_dtime = DATE_ADD(CURDATE(),INTERVAL -1 DAY ) ;

  SET sqlstr = CONCAT(' DELETE FROM ',dbgamename,'.t_act_instance_log_count where create_date=''',v_dtime,''' ') ;
  
  CALL sp_execsql(sqlstr);

  SET sqlstr = CONCAT(' INSERT INTO  ',dbgamename,'.t_act_instance_log_count SELECT PLAYER_ID, INSTANCE_ID, TIMES, EXT_TIMES,''',v_dtime,''' from ',dbgamename,'.t_act_instance_log ') ;
  
  CALL sp_execsql(sqlstr);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_activeplayer_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_activeplayer_count`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_activeplayer_count`(IN dblogname VARCHAR(30),IN time1 datetime,IN time2 datetime,IN gamesite VARCHAR(12))
BEGIN

  DECLARE SQL1 VARCHAR(300) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_PLAYER_DIARY_ACTIVE_LOG
                              where  date(CREATE_TIME) BETWEEN ''',time1,''' and ''',time2,'''
                                and  GAME_SITE=''',gamesite,'''  limit 0,100');
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_daily_diamond_inventory
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_daily_diamond_inventory`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_daily_diamond_inventory`(IN dblogname VARCHAR(30), IN time1 datetime ,IN time2 datetime, IN gamesite VARCHAR(12) )
BEGIN
  DECLARE SQL1 VARCHAR(200) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DIAMOND_STOCK_LOG  WHERE date(DIAMOND_STOCK_CREATE_TIME) BETWEEN ''',time1,'''  and ''',time2,''' and GAME_SITE=''',gamesite,'''  ');
  CALL sp_execsql(SQL1) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_day_onlinetime
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_day_onlinetime`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_day_onlinetime`(IN dblogname VARCHAR(30),IN time1 datetime ,IN time2 datetime ,IN gate_size VARCHAR(12))
BEGIN
  
  DECLARE SQl1 VARCHAR(500) ;
  SET SQL1=CONCAT('SELECT * FROM ',dblogname,'.T_DA_ONLINE_TIME_2  WHERE date(CREATE_TIME) BETWEEN  ''',time1,''' AND ''',time2,''' AND GAME_SITE=''',gate_size,'''') ;
  CALL sp_execsql(SQl1);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_day_register_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_day_register_log`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_day_register_log`(dblogname VARCHAR(30),dbgamename VARCHAR(30), dtime CHAR(10))
BEGIN
  DECLARE sqlstr   VARCHAR(300) ;
  DECLARE v_dtime  datetime ;
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE
     SET v_dtime=dtime ;
  END IF ;  
  
  SET sqlstr=CONCAT(' SELECT COUNT(*) INTO @numcount FROM db_analysis.t_day_register_log ') ;
  CALL sp_execsql(sqlstr);

  SET sqlstr=CONCAT('SELECT game_site,agent INTO @gamesite,@agent FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr); 

  IF @numcount=0 THEN
    SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_day_register_log(REGISTER_DATE, register_num, AGENT, GAME_SITE) SELECT date(CREATE_TIME),COUNT(PLAYER_ID),@agent, @gamesite FROM ',dbgamename,'.t_player   GROUP BY date(CREATE_TIME) ') ;
    CALL sp_execsql(sqlstr);
  ELSE 
    SET sqlstr=concat(' DELETE FROM  db_analysis.t_day_register_log WHERE  DATE(REGISTER_DATE)=DATE(''',v_dtime,''') and game_site=@gamesite ') ;
    
    CALL sp_execsql(sqlstr) ;
    SET sqlstr=concat(' INSERT INTO  db_analysis.t_day_register_log(REGISTER_DATE, register_num,AGENT, GAME_SITE) SELECT date(CREATE_TIME),COUNT(PLAYER_ID),@agent,@gamesite FROM ',dbgamename,'.t_player WHERE DATE(CREATE_TIME)=DATE(''',v_dtime,''')    ') ;
    CALL sp_execsql(sqlstr);
    
  END IF ;        
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_diamond_consumption_distribution
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_diamond_consumption_distribution`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_diamond_consumption_distribution`(IN dblogname VARCHAR(30), IN time1 datetime ,IN time2 datetime, IN gamesite VARCHAR(12) )
BEGIN

  DECLARE SQL1 VARCHAR(300) ;
  SET SQL1 = CONCAT(' SELECT COST_TYPE,NAME,SUM(VALUE) NUM FROM ',dblogname,'.T_DA_DIAMOND_COST  WHERE date(STATISTIC_TIME) BETWEEN ''',time1,'''  and ''',time2,''' and GAME_SITE=''',gamesite,''' GROUP by COST_TYPE ');
  CALL sp_execsql(SQL1) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_firstpay_analysis
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_firstpay_analysis`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_firstpay_analysis`(IN dblogname VARCHAR(30), IN time1 datetime ,IN time2 datetime, IN gamesite VARCHAR(12) )
BEGIN

  DECLARE SQL1 VARCHAR(300) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DA_PAY_1  WHERE date(STATISTIC_TIME) BETWEEN ''',time1,'''  and ''',time2,''' and GAME_SITE=''',gamesite,''' ');
  CALL sp_execsql(SQL1) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_firstpay_class_analysis
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_firstpay_class_analysis`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_firstpay_class_analysis`(IN dblogname VARCHAR(30),  IN gamesite VARCHAR(12) )
BEGIN

  DECLARE SQL1 VARCHAR(300) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DA_PAY_3  WHERE  GAME_SITE=''',gamesite,''' ');
  CALL sp_execsql(SQL1) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_five_online
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_five_online`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_five_online`(IN dtime datetime )
BEGIN 

  DECLARE sqlstr VARCHAR(600);
  SET sqlstr=CONCAT(' SELECT num ,DATE_FORMAT(create_time,''%Y-%m-%d %H:%i'') create_time from db_analysis.T_FIVE_ONLINE_LOG  
       WHERE date(CREATE_TIME)=''',dtime,''''   );
  
  CALL sp_execsql(sqlstr) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_five_recharge
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_five_recharge`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_five_recharge`(IN dblogname VARCHAR(30), IN time VARCHAR(19),IN game_site VARCHAR(12))
BEGIN
  DECLARE SQL1 VARCHAR(300) ;
  DECLARE nowtime VARCHAR(19) ;
  IF time='' THEN
     SET nowtime=DATE(NOW()) ;
  ELSE 
     SET nowtime=DATE(time) ;
  END IF ; 
  SET SQL1=CONCAT('select FIVE_PAY_NUM, CREATE_TIME from ',dblogname,'.t_five_pay_log  where date(create_time)=''',nowtime,'''   AND GAME_SITE = ''',game_site,'''');
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_five_register
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_five_register`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_five_register`( IN time VARCHAR(19))
BEGIN

  DECLARE SQL1 VARCHAR(300) ;
  DECLARE nowdate VARCHAR(19) ;  
  
  IF  time='' THEN
    SET nowdate=date(now()) ;
  ELSE 
    SET nowdate=time ;
  END IF ; 

  SET SQL1=CONCAT('SELECT   NUM, date_format(create_time,''%Y-%m-%d %H:%i'') CREATE_TIME FROM ','db_analysis','.t_five_register_log WHERE DATE(create_time)=''',nowdate,'''  ') ;
  
  CALL sp_execsql(SQL1); 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_gamesite_analysis
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_gamesite_analysis`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_gamesite_analysis`(IN dblogname VARCHAR(30),IN gamesite VARCHAR(12))
BEGIN

  DECLARE SQL1 VARCHAR(100) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DA_GAME_STEP where  GAME_SITE=''',gamesite,'''  ORDER BY GAME_STEP ASC ');
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_mall_sell_diamond_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_mall_sell_diamond_count`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_mall_sell_diamond_count`(IN dblogname VARCHAR(30), IN time1 datetime ,IN time2 datetime, IN gamesite VARCHAR(12) )
BEGIN

  DECLARE SQL1 VARCHAR(300) ;
  SET SQL1 = CONCAT(' SELECT ITEM_ID,NAME,SUM(NUM) NUM,SUM(PRICE) PRICE FROM ',dblogname,'.T_DA_SHOP_SELL  WHERE date(STATISTIC_TIME) BETWEEN ''',time1,'''  and ''',time2,''' and GAME_SITE=''',gamesite,''' GROUP by ITEM_ID ');
  CALL sp_execsql(SQL1) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_player_landing_site
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_player_landing_site`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_player_landing_site`(IN dblogname VARCHAR(30),IN gamesite VARCHAR(12))
BEGIN

  DECLARE SQL1 VARCHAR(100) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DA_LOGIN_DAY where  GAME_SITE=''',gamesite,'''');
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_player_lost_onlinetime
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_player_lost_onlinetime`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_player_lost_onlinetime`(IN dblogname VARCHAR(30),IN gamesite VARCHAR(12))
BEGIN

  DECLARE SQL1 VARCHAR(100) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DA_LOSE_2 where  GAME_SITE=''',gamesite,'''');
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_player_lost_task
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_player_lost_task`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_player_lost_task`(IN dblogname VARCHAR(30),IN gamesite VARCHAR(12))
BEGIN

  DECLARE SQL1 VARCHAR(100) ;
  SET SQL1 = CONCAT(' SELECT * FROM ',dblogname,'.T_DA_LOSE_1 where  GAME_SITE=''',gamesite,'''  limit 50');
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_RegAnaly
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_RegAnaly`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_RegAnaly`(IN dblogname VARCHAR(30),IN time1 datetime ,IN time2 datetime ,IN gate_size VARCHAR(12))
BEGIN
  
  DECLARE SQL1 VARCHAR(500) ;
  SET SQL1=CONCAT('SELECT date(register_time) as registerDate, count(player_id) as num from ',dblogname,'.t_register_log  
                           WHERE date(register_time) BETWEEN ''',time1,''' AND ''',time2,''' 
                             AND GAME_SITE=''',gate_size,''' GROUP BY date(register_time)  ') ;
  CALL sp_execsql(SQL1) ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_an_retained
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_an_retained`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_an_retained`(IN dblogname VARCHAR(30),IN time1 datetime ,IN time2 datetime ,IN gate_size VARCHAR(12))
BEGIN
  
  DECLARE SQL1 VARCHAR(500) ;
  SET SQL1=CONCAT(' SELECT * FROM ',dblogname,'.T_DA_RETAIN 
                            WHERE STATISTIC_TIME BETWEEN ''',time1,''' AND ''',time2,'''  
                               AND game_site=''',gate_size,'''      ') ; 
  CALL sp_execsql(SQL1); 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_carbon_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_carbon_count`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_carbon_count`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime CHAR(10))
BEGIN
  
  DECLARE v_num INT ;
  DECLARE v_dtime datetime ; 
  DECLARE sqlstr VARCHAR(8000) ;
  IF dtime='' THEN
     SET v_dtime = CURDATE() ;
  ELSE 
     SET v_dtime = dtime ;
  END IF ;
  
  CALL sp_login_num_level(dblogname, dbgamename, v_dtime,35 , @out ) ;
  SET v_num = @out ;
  
  SET sqlstr = CONCAT(' SELECT 
       SUM(CASE WHEN TIMES=1 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM1,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=0 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM2,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=1 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM3,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=2 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM4,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=3 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM5, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=4 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM6,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=5 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM7,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=6 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM8,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=7 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM9,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=8 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM10,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=9 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM11,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=10 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM12,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=11 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM13,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=12 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM14,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=13 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM15,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=14 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM16,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=15 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM17,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=16 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM18,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=17 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM19,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=18 AND INSTANCE_ID=201011 THEN 1 ELSE 0 END) AS EXPNUM20,   
       SUM(CASE WHEN TIMES=1 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM1,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=0 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM2,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=1 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM3,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=2 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM4, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=3 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM5, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=4 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM6, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=5 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM7, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=6 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM8, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=7 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM9, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=8 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM10, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=9 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM11, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=10 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM12, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=11 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM13, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=12 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM14, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=13 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM15, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=14 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM16, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=15 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM17, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=16 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM18, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=17 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM19, 
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=18 AND INSTANCE_ID=201021 THEN 1 ELSE 0 END) AS MONNUM20,  
       SUM(CASE WHEN TIMES=1 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM1,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=0 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM2,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=1 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM3,
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=2 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM4,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=3 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM5,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=4 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM6,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=5 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM7,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=6 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM8,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=7 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM9,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=8 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM10,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=9 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM11,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=10 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM12,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=11 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM13,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=12 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM14,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=13 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM15,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=14 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM16,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=15 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM17,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=16 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM18,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=17 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM19,  
       SUM(CASE WHEN TIMES=2 AND EXT_TIMES=18 AND INSTANCE_ID=201031 THEN 1 ELSE 0 END) AS MAGNUM20
       INTO  @A20818, @A30818, @A40818, @A50818, @A60818, @A70818, @A80818, @A90818, @A100818, @A110818, @A120818, @A130818, @A140818, @A150818, @A160818, @A170818, @A180818, @A190818, @A200818, @A210818,  
             @B20818, @B30818, @B40818, @B50818, @B60818, @B70818, @B80818, @B90818, @B100818, @B110818, @B120818, @B130818, @B140818, @B150818, @B160818, @B170818, @B180818, @B190818, @B200818, @B210818, 
             @C20818, @C30818, @C40818, @C50818, @C60818, @C70818, @C80818, @C90818, @C100818, @C110818, @C120818, @C130818, @C140818, @C150818, @C160818, @C170818, @C180818, @C190818, @C200818, @C210818 
  FROM ',dbgamename,'.t_act_instance_log ') ;
  CALL sp_execsql(sqlstr) ;
  
  
  SET @A10818:= v_num-@A20818-@A30818-@A40818-@A50818-@A60818-@A70818-@A80818-@A90818-@A100818-@A110818-@A120818-@A130818-@A140818-@A150818-@A160818-@A170818-@A180818-@A190818-@A200818-@A210818 ;
  
  IF @A10818<0 THEN
     SET @A10818=0 ;
     SET v_num=@A20818+@A30818+@A40818+@A50818+@A60818+@A70818+@A80818+@A90818+@A100818+@A110818+@A120818+@A130818+@A140818+@A150818+@A160818+@A170818+@A180818+@A190818+@A200818+@A210818 ;
  END IF ;
  
  SET @B10818:= v_num-@B20818-@B30818-@B40818-@B50818-@B60818-@B70818-@B80818-@B90818-@B100818-@B110818-@B120818-@B130818-@B140818-@B150818-@B160818-@B170818-@B180818-@B190818-@B200818-@B210818 ;
  IF @B10818<0 THEN
     SET @B10818=0 ;
     SET v_num=@B20818+@B30818+@B40818+@B50818+@B60818+@B70818+@B80818+@B90818+@B100818+@B110818+@B120818+@B130818+@B140818+@B150818+@B160818+@B170818+@B180818+@B190818+@B200818+@B210818 ;
  END IF ;
  
  SET @C10818:= v_num-@C20818-@C30818-@C40818-@C50818-@C60818-@C70818-@C80818-@C90818-@C100818-@C110818-@C120818-@C130818-@C140818-@C150818-@C160818-@C170818-@C180818-@C190818-@C200818-@C210818 ;
  IF @C10818<0 THEN
     SET @C10818=0 ;
     SET v_num=@C20818+@C30818+@C40818+@C50818+@C60818+@C70818+@C80818+@C90818+@C100818+@C110818+@C120818+@C130818+@C140818+@C150818+@C160818+@C170818+@C180818+@C190818+@C200818+@C210818 ;
  END IF ;

  SET sqlstr = CONCAT(' SELECT GAME_SITE,AGENT INTO @gamesite0818,@agent0818 from ',dblogname,'.t_gamesite_agent ') ;
   
  CALL sp_execsql(sqlstr) ;
  DELETE FROM db_analysis.t_carbon_count WHERE create_date=v_dtime AND game_site=@gamesite0818 ;
  INSERT INTO db_analysis.t_carbon_count SELECT  v_num, @A10818, @A20818, @A30818, @A40818, @A50818, @A60818, @A70818, @A80818, @A90818, @A100818, @A110818, @A120818, @A130818, @A140818, @A150818, @A160818, @A170818, @A180818, @A190818, @A200818, @A210818,
                                                        @B10818, @B20818, @B30818, @B40818, @B50818, @B60818, @B70818, @B80818, @B90818, @B100818, @B110818, @B120818, @B130818, @B140818, @B150818, @B160818, @B170818, @B180818, @B190818, @B200818, @B210818,
                                                        @C10818, @C20818, @C30818, @C40818, @C50818, @C60818, @C70818, @C80818, @C90818, @C100818, @C110818, @C120818, @C130818, @C140818, @C150818, @C160818, @C170818, @C180818, @C190818, @C200818, @C210818,v_dtime,@gamesite0818,@agent0818 ;
 END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_alchemy
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_alchemy`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_alchemy`(dblogname VARCHAR(30),dbgamename VARCHAR(30),dtime CHAR(10))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num SMALLINT ;
  DECLARE sqlstr VARCHAR(800) ;
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE 
     SET v_dtime=dtime ;
  END IF ;
  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,17,@out) ;
  SET num = @out ;
  
  SET sqlstr = CONCAT(' SELECT  SUM(CASE WHEN ALCHEMY_TIMES>=1 AND ALCHEMY_TIMES<=5 THEN 1 ELSE 0 END ),
        SUM(CASE WHEN ALCHEMY_TIMES>=6 AND ALCHEMY_TIMES<=10 THEN 1 ELSE 0 END ),
        SUM(CASE WHEN ALCHEMY_TIMES>=11 AND ALCHEMY_TIMES<=15 THEN 1 ELSE 0 END ),
        SUM(CASE WHEN ALCHEMY_TIMES>=16 AND ALCHEMY_TIMES<=20 THEN 1 ELSE 0 END ),
        SUM(CASE WHEN ALCHEMY_TIMES>=21 AND ALCHEMY_TIMES<=25 THEN 1 ELSE 0 END ),
        SUM(CASE WHEN ALCHEMY_TIMES>=26 AND ALCHEMY_TIMES<=30 THEN 1 ELSE 0 END ),
        SUM(CASE WHEN ALCHEMY_TIMES>=31 AND ALCHEMY_TIMES<=35 THEN 1 ELSE 0 END ) 
        INTO @C10819, @C20819, @C30819, @C40819, @C50819, @C60819, @C70819 
     FROM ',dbgamename,'.t_player_alchemy  ') ;
  CALL sp_execsql(sqlstr) ;
  
  SET @C00819 := num-@C10819-@C20819-@C30819-@C40819-@C50819-@C60819-@C70819 ;
  IF @C00819<0 THEN
     SET @C00819=0 ;
     SET num=@C10819+@C20819+@C30819+@C40819+@C50819+@C60819+@C70819 ;
  END IF ;
  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite0819, @agent0819 from ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr) ; 
  
  DELETE FROM db_analysis.t_cn_alchemy  WHERE game_site=@gamesite0819 AND create_date=v_dtime ;
  INSERT INTO db_analysis.t_cn_alchemy SELECT @C00819, @C10819, @C20819, @C30819, @C40819, @C50819, @C60819, @C70819 ,num,v_dtime,@gamesite0819, @agent0819 ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_arena_num
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_arena_num`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_arena_num`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime CHAR(10) )
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num SMALLINT ; 
  DECLARE sqlstr VARCHAR(900) ;
  IF dtime='' THEN
     SET v_dtime = CURDATE() ;
  ELSE 
     SET v_dtime = dtime ;
  END IF ;
  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,20,@out) ;
  SET num=@out ;
  
  SET sqlstr = CONCAT(' SELECT SUM(CASE WHEN ARENA_NUM>=1 AND ARENA_NUM<=5 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=6 AND ARENA_NUM<=10 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=11 AND ARENA_NUM<=15 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=16 AND ARENA_NUM<=20 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=21 AND ARENA_NUM<=25 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=26 AND ARENA_NUM<=30 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=31 AND ARENA_NUM<=35 THEN 1 ELSE 0 END) ,
       SUM(CASE WHEN ARENA_NUM>=36 AND ARENA_NUM<=40 THEN 1 ELSE 0 END)  
       INTO @num10819, @num20819, @num30819,  @num40819,  @num50819,  @num60819,  @num70819, @num80819   
        FROM ',dbgamename,'.t_player_daily_active_count where create_date=''',v_dtime,'''  ') ;
  
  CALL sp_execsql(sqlstr) ;
  SET @num00819:=num-@num10819-@num20819-@num30819-@num40819-@num50819-@num60819-@num70819-@num80819  ;
  IF @num00819<0 THEN
     SET @num00819=0 ;
     SET num=@num10819+@num20819+@num30819+@num40819+@num50819+@num60819+@num70819+@num80819  ;
  END IF ;
  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite0819, @agent0819 from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr) ; 
   
  DELETE FROM db_analysis.t_cn_arena_num WHERE create_date=v_dtime AND game_site=@gamesite0819 ;
  INSERT INTO db_analysis.t_cn_arena_num SELECT @num00819,@num10819, @num20819, @num30819,  @num40819,  @num50819,  @num60819,  @num70819, @num80819,num,v_dtime, @gamesite0819, @agent0819 ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_daily_task
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_daily_task`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_daily_task`(dblogname VARCHAR(30),dbgamename VARCHAR(30),dtime CHAR(10))
BEGIN
  
    DECLARE v_dtime datetime ;
  DECLARE num SMALLINT ;
  DECLARE sqlstr VARCHAR(800) ;
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE 
     SET v_dtime=dtime ;
  END IF ;
  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,28,@out) ;
  SET num = @out ;
  
  SET sqlstr = CONCAT(' SELECT  SUM(CASE WHEN DAILY_TASK_FINISH_NUM>=1 AND DAILY_TASK_FINISH_NUM<=5 THEN 1 ELSE 0 END) ,
        SUM(CASE WHEN DAILY_TASK_FINISH_NUM>=6 AND DAILY_TASK_FINISH_NUM<=10 THEN 1 ELSE 0 END) ,
        SUM(CASE WHEN DAILY_TASK_FINISH_NUM>=11 AND DAILY_TASK_FINISH_NUM<=15 THEN 1 ELSE 0 END) ,
        SUM(CASE WHEN DAILY_TASK_FINISH_NUM>=16 AND DAILY_TASK_FINISH_NUM<=20 THEN 1 ELSE 0 END) 
        INTO @D10819, @D20819, @D30819, @D40819 
        FROM ',dbgamename,'.t_player_daily_data_count  
        WHERE CREATE_DATE=''',v_dtime,'''') ;
  CALL sp_execsql(sqlstr) ;
  
  SET @D00819 := num-@D10819-@D20819-@D30819-@D40819 ;
  
  IF @D00819<0 THEN
     SET @D00819=0 ;
     SET num=@D10819+@D20819+@D30819+@D40819 ;
  END IF ;
  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite0819, @agent0819 from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr) ; 
  
  DELETE FROM db_analysis.t_cn_daily_task  WHERE game_site=@gamesite0819 AND create_date=v_dtime ;
  INSERT INTO db_analysis.t_cn_daily_task  SELECT @D00819, @D10819, @D20819, @D30819, @D40819  ,num,v_dtime,@gamesite0819, @agent0819 ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_energy_using_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_energy_using_count`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_energy_using_count`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime CHAR(10))
BEGIN
  
  
  DECLARE v_dtime datetime ;
  DECLARE sqlstr  VARCHAR(1000) ;
  IF dtime='' THEN
     SET v_dtime = CURDATE() ; 
  ELSE 
     SET v_dtime = dtime ;
  END IF ;
  
  CALL sp_login_num(dblogname, dbgamename, v_dtime, @out) ;
  SET @count0818:=@out ;

  SET sqlstr=CONCAT(' SELECT game_site,agent into @gamesite,@agent from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr); 

  
  SET sqlstr = CONCAT(' SELECT SUM(a.A1) A1,SUM(a.A2) A2,SUM(a.A3) A3,SUM(a.A4) A4,SUM(a.A5) A5,SUM(a.A6) A6, game_site ,agent FROM (
                        SELECT  CASE WHEN SUM(COST_ENERGY)>=1 AND SUM(COST_ENERGY)<=50 THEN 1 ELSE 0 END  AS A1,
        CASE WHEN SUM(COST_ENERGY)>=51 AND SUM(COST_ENERGY)<=100 THEN 1 ELSE 0 END AS A2,
        CASE WHEN SUM(COST_ENERGY)>=101 AND SUM(COST_ENERGY)<=200 THEN 1 ELSE 0 END AS A3,
        CASE WHEN SUM(COST_ENERGY)>=201 AND SUM(COST_ENERGY)<=300 THEN 1 ELSE 0 END AS A4,
        CASE WHEN SUM(COST_ENERGY)>=301 AND SUM(COST_ENERGY)<=500 THEN 1 ELSE 0 END AS A5,
        CASE WHEN SUM(COST_ENERGY)>=501  THEN 1 ELSE 0 END AS A6, @gamesite game_site,@agent agent
    FROM ',dblogname,'.t_energy_use_log
    WHERE DATE(CREATE_TIME)=''',v_dtime,''' 
    GROUP BY PLAYER_ID,DATE(CREATE_TIME)  )a  INTO @A10818, @A20818, @A30818, @A40818, @A50818, @A60818, @gamesite0818, @agent0818') ;
  
  CALL sp_execsql(sqlstr); 
  
  SET sqlstr = CONCAT(' DELETE FROM db_analysis.t_cn_energy_using_count where game_site=''',@gamesite0818,''' AND create_date=''',v_dtime,'''') ;
  CALL sp_execsql(sqlstr);
 
  SET @A00818 :=  @count0818-@A10818-@A20818-@A30818-@A40818-@A50818-@A60818 ;
  SET @A10818 :=  @A10818+@A00818 ;
  INSERT INTO db_analysis.t_cn_energy_using_count SELECT  @A10818, @A20818, @A30818, @A40818, @A50818, @A60818 ,@count0818, v_dtime,@gamesite0818, @agent0818 ;
   
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_guild_task
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_guild_task`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_guild_task`(dblogname VARCHAR(30),dbgamename VARCHAR(30),dtime CHAR(10))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num SMALLINT ;
  DECLARE sqlstr VARCHAR(800) ;
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE 
     SET v_dtime=dtime ;
  END IF ;
  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,41,@out) ;
  SET num = @out ;
  
  SET sqlstr = CONCAT(' SELECT  SUM(CASE WHEN GUILD_TASK_FINISH_NUM>=1 AND GUILD_TASK_FINISH_NUM<=5 THEN 1 ELSE 0 END) ,
        SUM(CASE WHEN GUILD_TASK_FINISH_NUM>=6 AND GUILD_TASK_FINISH_NUM<=10 THEN 1 ELSE 0 END) ,
        SUM(CASE WHEN GUILD_TASK_FINISH_NUM>=11 AND GUILD_TASK_FINISH_NUM<=15 THEN 1 ELSE 0 END) ,
        SUM(CASE WHEN GUILD_TASK_FINISH_NUM>=16 AND GUILD_TASK_FINISH_NUM<=20 THEN 1 ELSE 0 END) 
        INTO @D10819, @D20819, @D30819, @D40819 
        FROM ',dbgamename,'.t_player_daily_data_count WHERE CREATE_DATE=''',v_dtime,'''  ') ;
  CALL sp_execsql(sqlstr) ;
  
  SET @D00819 := num-@D10819-@D20819-@D30819-@D40819 ;
  IF @D00819<0 THEN
     SET @D00819=0 ;
     SET  num=@D10819+@D20819+@D30819+@D40819 ;
  END IF ;
  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite0819, @agent0819 from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr) ; 
  
  DELETE FROM db_analysis.t_cn_guild_task  WHERE game_site=@gamesite0819 AND create_date=v_dtime ;
  INSERT INTO db_analysis.t_cn_guild_task  SELECT @D00819, @D10819, @D20819, @D30819, @D40819  ,num,v_dtime,@gamesite0819, @agent0819 ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_king_heg
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_king_heg`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_cn_king_heg`(dblogname VARCHAR(30),dbgamename VARCHAR(37),dtime CHAR(10))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num     SMALLINT ;
  DECLARE sqlstr  VARCHAR(800) ; 
  IF dtime='' THEN
     SET v_dtime = CURDATE() ;
  ELSE 
     SET v_dtime = dtime ;
  END IF ; 

  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,37,@out) ;
  SET num = @out ;
  
  SET sqlstr = CONCAT(' SELECT SUM(CASE WHEN ARENA_ACTIVITY_NUM>=1 AND ARENA_ACTIVITY_NUM<=5 THEN 1 ELSE 0 END) , 
       SUM(CASE WHEN ARENA_ACTIVITY_NUM>=6 AND ARENA_ACTIVITY_NUM<=10 THEN 1 ELSE 0 END) , 
       SUM(CASE WHEN ARENA_ACTIVITY_NUM>=11 AND ARENA_ACTIVITY_NUM<=15 THEN 1 ELSE 0 END) , 
       SUM(CASE WHEN ARENA_ACTIVITY_NUM>=16 AND ARENA_ACTIVITY_NUM<=20 THEN 1 ELSE 0 END) , 
       SUM(CASE WHEN ARENA_ACTIVITY_NUM>=21 AND ARENA_ACTIVITY_NUM<=25 THEN 1 ELSE 0 END) , 
       SUM(CASE WHEN ARENA_ACTIVITY_NUM>=26 AND ARENA_ACTIVITY_NUM<=30 THEN 1 ELSE 0 END) 
       INTO @B10819, @B20819, @B30819, @B40819, @B50819, @B60819
       FROM  ',dbgamename,'.t_player_daily_data_count where create_date=''',v_dtime,'''  ') ;
  
  CALL sp_execsql(sqlstr);
 
  SET @B00819 := num-@B10819-@B20819-@B30819-@B40819-@B50819-@B60819 ;
  IF @B00819<0 THEN
     SET @B00819=0 ;
     SET num=@B10819+@B20819+@B30819+@B40819+@B50819+@B60819 ;
  END IF ;
  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite081902, @agent081902 from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr) ; 
  
  DELETE FROM db_analysis.t_cn_king_heg WHERE create_date=v_dtime AND game_site=@gamesite081902 ;
  INSERT INTO db_analysis.t_cn_king_heg SELECT  @B00819, @B10819, @B20819, @B30819, @B40819, @B50819, @B60819, num, v_dtime, @gamesite081902, @agent081902 ;
   
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_monster
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_monster`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_monster`(dblogname VARCHAR(30),dbgamename VARCHAR(30),dtime CHAR(10))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num SMALLINT ;
  DECLARE sqlstr VARCHAR(800) ;
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE 
     SET v_dtime=dtime ;
  END IF ;
  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,22,@out) ;
  SET num = @out ;
  
  
  
  SET sqlstr = CONCAT(' SELECT SUM(CASE WHEN NUM=1 THEN 1 ELSE 0 END) A1, 
       SUM(CASE WHEN NUM=2 THEN 1 ELSE 0 END) A2, 
       SUM(CASE WHEN NUM=3 THEN 1 ELSE 0 END) A3, 
       SUM(CASE WHEN NUM=4 THEN 1 ELSE 0 END) A4, 
       SUM(CASE WHEN NUM=5 THEN 1 ELSE 0 END) A5, 
       SUM(CASE WHEN NUM=6 THEN 1 ELSE 0 END) A6, 
       SUM(CASE WHEN NUM=7 THEN 1 ELSE 0 END) A7, 
       SUM(CASE WHEN NUM=8 THEN 1 ELSE 0 END) A8, 
       SUM(CASE WHEN NUM=9 THEN 1 ELSE 0 END) A9,
       SUM(CASE WHEN NUM=10 THEN 1 ELSE 0 END) A10  
       INTO @A10820 ,@A20820 ,@A30820 ,@A40820 ,@A50820 ,@A60820 ,@A70820 ,@A80820 ,@A90820 ,@A100820 
FROM (
SELECT PLAYER_ID,COUNT(*) AS NUM
FROM ',dbgamename,'.t_boss_monster_log 
  WHERE STATE IN(1,3) GROUP BY PLAYER_ID  ) b ') ;
  CALL sp_execsql(sqlstr) ;

  
  SET @A00820 := num-@A10820-@A20820-@A30820-@A40820-@A50820-@A60820-@A70820-@A80820-@A90820-@A100820 ;   
  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite0819, @agent0819 from ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr) ; 
  
  DELETE FROM db_analysis.t_cn_monster WHERE game_site=@gamesite0819 AND create_date=v_dtime ;
  INSERT INTO db_analysis.t_cn_monster SELECT @A00820,@A10820 ,@A20820 ,@A30820 ,@A40820 ,@A50820 ,@A60820 ,@A70820 ,@A80820 ,@A90820 ,@A100820 ,num,v_dtime,@gamesite0819, @agent0819 ;
  
  update db_analysis.t_cn_monster set A0=0 ,NUM=A1+A2+A3+A4+A5+A6+A7+A8+A9+A10 where A0<0 ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_monster_lev
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_monster_lev`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_monster_lev`(dblogname VARCHAR(30), dbgamename VARCHAR(30))
BEGIN 
  
  DECLARE v_dtime datetime ;
  DECLARE sqlstr VARCHAR(3000) ;
  SET v_dtime = CURDATE() ;

  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite0820, @agent0820 from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr) ; 

  DELETE FROM db_analysis.t_cn_monster_lev WHERE CREATE_DATE=v_dtime AND GAME_SITE=@gamesite0820 ;  
  SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_cn_monster_lev 
SELECT  SUM(CASE WHEN BOSS_INS_MONSTER_ID=1 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=2 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=3 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=4 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=5 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=6 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=7 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=8 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=9 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=10 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=11 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=12 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=13 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=14 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=15 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=16 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=17 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=18 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=19 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=20 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=21 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=22 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=23 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=24 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=25 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=26 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=27 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=28 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=29 THEN 1 ELSE 0 END), 
      SUM(CASE WHEN BOSS_INS_MONSTER_ID=30 THEN 1 ELSE 0 END),
      ''',v_dtime,''', @gamesite0820, @agent0820
FROM  ',dbgamename,'.t_boss_monster_log WHERE STATE IN(1,3);') ;  
  CALL sp_execsql(sqlstr);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_mult_task
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_mult_task`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_cn_mult_task`(dblogname VARCHAR(30),dbgamename VARCHAR(30),dtime CHAR(10))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num     SMALLINT ;
  DECLARE sqlstr  VARCHAR(800) ; 
  IF dtime='' THEN
     SET v_dtime = CURDATE() ;
  ELSE 
     SET v_dtime = dtime ;
  END IF ; 

  CALL sp_login_num_level(dblogname,dbgamename,v_dtime,34,@out) ;
  SET num = @out ;
  
  SET sqlstr = CONCAT(' SELECT SUM(CASE WHEN MULTI_INS_TIMES=1 AND BUY_MULTI_INS_TIMES=0 THEN 1 ELSE 0 END ) ,
       SUM(CASE WHEN MULTI_INS_TIMES=2 AND BUY_MULTI_INS_TIMES=0 THEN 1 ELSE 0 END ) , 
       SUM(CASE WHEN MULTI_INS_TIMES=3 AND BUY_MULTI_INS_TIMES=0 THEN 1 ELSE 0 END ) ,
       SUM(CASE WHEN MULTI_INS_TIMES=3 AND BUY_MULTI_INS_TIMES=1 THEN 1 ELSE 0 END ) ,
       SUM(CASE WHEN MULTI_INS_TIMES=3 AND BUY_MULTI_INS_TIMES=2 THEN 1 ELSE 0 END ) ,
       SUM(CASE WHEN MULTI_INS_TIMES=3 AND BUY_MULTI_INS_TIMES=3 THEN 1 ELSE 0 END ) 
       INTO @B10819, @B20819, @B30819, @B40819, @B50819, @B60819 
       FROM ',dbgamename,'.t_player_daily_data_count where create_date=''',v_dtime,'''') ;
  
  CALL sp_execsql(sqlstr); 
  SET @B00819 := num-@B10819-@B20819-@B30819-@B40819-@B50819-@B60819 ;
  
  IF @B00819<0 THEN
     SET @B00819=0 ;
     SET num=@B10819+@B20819+@B30819+@B40819+@B50819+@B60819 ;
  END IF ; 

  SET sqlstr = CONCAT(' SELECT GAME_SITE, AGENT INTO @gamesite081902, @agent081902 from ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr) ; 
  
  DELETE FROM db_analysis.t_cn_mult_task  WHERE create_date=v_dtime AND game_site=@gamesite081902 ;
  INSERT INTO db_analysis.t_cn_mult_task  SELECT  @B00819, @B10819, @B20819, @B30819, @B40819, @B50819, @B60819, num, v_dtime, @gamesite081902, @agent081902 ;
   
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_cn_pay_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cn_pay_log`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_cn_pay_log`(dblogname VARCHAR(30),dbgamename VARCHAR(30))
BEGIN
  
  DECLARE sqlstr VARCHAR(500) ;
  SET sqlstr=CONCAT(' SELECT GAME_SITE INTO @gamesite0916 FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr);
  DELETE FROM db_analysis.t_pay_log where pay_site=@gamesite0916 ;
  SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_pay_log SELECT * FROM ',dbgamename,'.t_pay_log ') ;
  CALL sp_execsql(sqlstr); 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_consume_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_consume_count`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_consume_count`(dblogname VARCHAR(30), dbgamename VARCHAR(30) ,dbbasename VARCHAR(30), dtime CHAR(10) )
BEGIN

  DECLARE v_dtime  datetime ; 
  DECLARE sqlstr VARCHAR(800) ;

  IF dtime='' THEN  
    SET v_dtime = CURDATE() ;
  ELSE 
    SET v_dtime=dtime ;
  END IF ;
 
  set sqlstr=CONCAT(' select GAME_SITE ,agent into @gamesite0716,@agent0730 from ',dblogname,'.t_gamesite_agent');
  call sp_execsql(sqlstr);
   
  DELETE FROM db_analysis.t_consume_count WHERE GAME_SITE=@gamesite0716 AND CREATE_DATE=v_dtime ;
  
  SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_consume_count(CREATE_DATE, ITEM_ID, `NAME`, PNUM, TOTAL, DIAMOND, GAME_SITE ,AGENT) 
     SELECT DATE(a.CREATE_TIME) CREATE_TIME , a.ITEM_ID ,b.NAME ,COUNT(DISTINCT(a.PLAYER_ID)) PNUM ,SUM(NUM) TOTAL,SUM(PRICE) DIAMOND , @gamesite0716, @agent0730
              FROM  ',dblogname,'.t_item_buy_log  a
        INNER JOIN  ',dbbasename,'.t_base_item    b
                ON  b.ITEM_ID=a.ITEM_ID   
        INNER JOIN  ',dbgamename,'.t_player c
                ON  c.player_id=a.player_id
               AND  c.type=1
             WHERE DATE(a.CREATE_TIME) = ''',v_dtime,''' 
               AND a.TYPE = 2                       
          GROUP BY a.ITEM_ID   ') ;   
  
  CALL sp_execsql(sqlstr) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_all
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_all`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_all`(IN dblogname VARCHAR(20),IN dbgamename VARCHAR(20), dtime CHAR(10))
    COMMENT '总览'
BEGIN
  
  DECLARE v_dtime datetime ; 
  DECLARE SQL1  VARCHAR(600) ;   
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE 
     SET v_dtime=dtime ;
  END IF ;

  SET SQL1=CONCAT('SELECT @registerNum:=COUNT(PLAYER_ID)  from ' ,dbgamename , '.t_player where DATE(create_time) = ''',v_dtime,''' ' );
  CALL sp_execsql(SQL1) ;

  SET SQL1=CONCAT('SELECT @totalRegNum:=COUNT(PLAYER_ID)  from ',dbgamename,'.t_player;' ) ;
  CALL sp_execsql(SQL1) ;

  SET SQL1=CONCAT('SELECT @payNum:=sum(money)  from ', dbgamename,'.t_pay_log where DATE(CREATE_time) =  ''',v_dtime,''' ' ) ;
  CALL sp_execsql(SQL1) ;

  SET SQL1=CONCAT('SELECT @totalPayNum:=sum(money) from ',dbgamename,'.t_pay_log;') ;
  CALL sp_execsql(SQL1) ;

  SET SQL1=CONCAT('SELECT @totalPayPlayerNum:=count(DISTINCT(PLAYER_ID))  from ',dbgamename,'.t_pay_log ');
  CALL sp_execsql(SQL1) ;
 
  SET SQL1=CONCAT(' SELECT game_site INTO @game_site from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(SQL1) ;
 
  DELETE FROM db_analysis.t_da_all WHERE game_site=@game_site ;

	SET SQL1=CONCAT('INSERT INTO db_analysis.t_da_all(REGISTER_NUM, TOTAL_REG_NUM, PAY_NUM, TOTAL_PAY_NUM, TOTAL_PAY_PLAYER_NUM,GAME_SITE) 
	select @registerNum ,  @totalRegNum,
	case when @payNum is null then 0 else @payNum end,
	case when @totalPayNum is null then 0 else @totalPayNum end, @totalPayPlayerNum ,@game_site ' ) ; 
  CALL sp_execsql(SQL1);
 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_all_bak
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_all_bak`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_da_all_bak`(IN dblogname VARCHAR(20),IN dbgamename VARCHAR(20))
    COMMENT '总览'
BEGIN
	
	
	
	
	
  DECLARE SQL1  VARCHAR(120) ;   
  
  SET SQL1=CONCAT('SELECT @registerNum:=COUNT(PLAYER_ID)  from ' ,dblogname , '.t_register_log where DATE(register_time) = date(NOW()) ;' );
  CALL sp_execsql(SQL1) ;
  

  SET SQL1=CONCAT('SELECT @totalRegNum:=COUNT(PLAYER_ID)  from ',dblogname,'.t_register_log;' ) ;
  CALL sp_execsql(SQL1) ;
  

  SET SQL1=CONCAT('SELECT @payNum:=sum(money)  from ', dbgamename,'.t_pay_log where DATE(CREATE_time) = date(NOW());') ;
  CALL sp_execsql(SQL1) ;
  

  SET SQL1=CONCAT('SELECT @totalPayNum:=sum(money) from ',dbgamename,'.t_pay_log;') ;
  CALL sp_execsql(SQL1) ;
 	

  SET SQL1=CONCAT('SELECT @totalPayPlayerNum:=count(DISTINCT(PLAYER_ID))  from ',dbgamename,'.t_pay_log;');
  CALL sp_execsql(SQL1) ;
 	

  SET	SQL1=CONCAT('TRUNCATE ' ,dblogname ,'.t_da_all ; ') ;
  CALL sp_execsql(SQL1) ;
  

	SET SQL1=CONCAT('INSERT INTO' ,dblogname ,'.t_da_all(@REGISTER_NUM, @TOTAL_REG_NUM, @PAY_NUM, @TOTAL_PAY_NUM, @TOTAL_PAY_PLAYER_NUM)  ') ;
	select  @registerNum ,  @totalRegNum,
	case when @payNum IS NULL then 0 else @payNum end,
	case when @totalPayNum IS NULL then 0 else @totalPayNum end, @totalPayPlayerNum;      
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_all_bak_1
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_all_bak_1`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_da_all_bak_1`(in dbname VARCHAR(20))
BEGIN     
  DECLARE tbname VARCHAR(40) ;
  
  SET tbname = CONCAT(dbname,'.t_register_log');  
  
  SET @SQL1='SELECT  @registerNum:=COUNT(PLAYER_ID)   from tbname where DATE(register_time) = date(NOW())' ;
  PREPARE stmt1 FROM @SQL1 ;
  EXECUTE  stmt1  ;
  DEALLOCATE PREPARE  stmt1 ;
  select @SQL1,tbname ,@registerNum ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_diamond_cost
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_diamond_cost`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_diamond_cost`(IN dblogname VARCHAR(30),dbgamename VARCHAR(30), dtime datetime )
    COMMENT '钻石消耗'
BEGIN
  
  DECLARE SQL1 VARCHAR(700) ; 

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;

  DELETE FROM db_analysis.t_da_diamond_cost WHERE game_site=@GAME_SITE  AND STATISTIC_TIME=dtime ;
  SET SQL1=CONCAT(' insert into db_analysis.t_da_diamond_cost(STATISTIC_TIME,COST_TYPE,name,value,game_site,AGENT)
                    select date(a.create_time) time, in_out_type, type_name, sum(value) price,@GAME_SITE,@AGENT  from ',dblogname,'.t_diamond_log a 
                           inner join ',dbgamename,'.t_player b
                                   on  b.player_id=a.player_id
                                  and  b.type=1
               where value < 0 AND a.TYPE=1 and date(a.create_time)=''',dtime,''' group by  date(a.create_time), in_out_type ') ;
  
  CALL sp_execsql(SQL1) ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_diamond_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_diamond_log`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_diamond_log`(IN dblogname VARCHAR(30), IN dbgamename VARCHAR(30) ,dtime CHAR(10))
BEGIN 
  
  DECLARE SQL1 VARCHAR(1000) ;

  IF dtime='' THEN 
     SET @time=CURDATE() ;
  ELSE 
     SET @time:=dtime ; 
  END IF ;  

  DROP TEMPORARY TABLE IF EXISTS tmp_id ;
  CREATE TEMPORARY TABLE tmp_id 
  ( player_id INT 
  ) ENGINE=memory  ;

  DROP TEMPORARY TABLE IF EXISTS tmp_dist_id ;
  CREATE TEMPORARY TABLE tmp_dist_id 
  ( player_id INT
  ) ENGINE=memory ;
  
  SET SQL1=CONCAT(' INSERT INTO tmp_id SELECT player_id from ( 
             SELECT PLAYER_ID,COUNT(LOGIN_TIME) SUM FROM (
                  SELECT a.PLAYER_ID, b.LOGIN_TIME FROM ',dbgamename,'.t_player  a 
                           INNER JOIN (SELECT player_id,DATE(LOGIN_TIME) LOGIN_TIME FROM ',dblogname,'.t_login_log WHERE  DATEDIFF(@time,DATE(LOGIN_TIME))<=30 ) b 
                              ON b.player_id=a.PLAYER_ID
                            WHERE a.type=1
                        GROUP BY PLAYER_ID,b.LOGIN_TIME ) c  
                          GROUP BY c.player_id HAVING sum>=3 ) aa ') ;
  CALL sp_execsql(SQL1) ;
  
  SET SQL1=CONCAT(' INSERT INTO tmp_id select player_id from (SELECT PLAYER_ID FROM db_analysis.t_da_online_time_1 WHERE CREATE_TIME=@time AND ONLINE_TIME>=10 )aa') ;
  CALL sp_execsql(SQL1) ;
  
  INSERT INTO tmp_dist_id SELECT  DISTINCT player_id FROM tmp_id ;
  
  SELECT COUNT(1) INTO @SUM0904 FROM tmp_dist_id ; 
  
  SET SQL1=CONCAT(' SELECT @totaldiamond:=SUM(a.diamond)  FROM ',dbgamename,'.t_player_wealth a inner join tmp_dist_id b on b.player_id=a.player_id   ');
  CALL sp_execsql(SQL1) ;
  
  SET SQL1=CONCAT(' SELECT @daybuydiamond:=SUM(a.diamond)  FROM ',dbgamename,'.t_pay_log a inner join tmp_dist_id b on b.player_id=a.player_id  WHERE date(a.create_time)=@time  ');
  CALL sp_execsql(SQL1) ;
 
  SET SQL1=CONCAT(' SELECT @dayusediamond:=abs(SUM(a.VALUE))  FROM ',dblogname,'.t_diamond_log a INNER JOIN tmp_dist_id b on b.player_id=a.player_id WHERE date(a.create_time)=@time AND a.`VALUE`<=0 AND a.TYPE=1 ');
  CALL sp_execsql(SQL1) ; 

  IF (@daybuydiamond IS NULL ) THEN
      SET  @daybuydiamond := 0 ;
  END IF ;
  IF (@dayusediamond IS NULL ) THEN
      SET  @dayusediamond := 0 ;
  END IF ;
 
   
   SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
   CALL sp_execsql(SQL1) ;

   delete from db_analysis.t_diamond_stock_log where DIAMOND_STOCK_CREATE_TIME=CONCAT(DATE(@time),' 23:59:59') AND GAME_SITE=@GAME_SITE  ;

   SET SQL1=CONCAT(' INSERT INTO db_analysis.t_diamond_stock_log(ACTIVE_PLAYER,DIAMOND_STOCK_TOTAL_NUM,DIAMOND_STOCK_USE_NUM,DIAMOND_STOCK_BUY_NUM,DIAMOND_STOCK_CREATE_TIME,GAME_SITE,AGENT) VALUES (@SUM0904,@totaldiamond,@dayusediamond,@daybuydiamond,CONCAT(DATE(@time),'' 23:59:59''),@GAME_SITE,@AGENT) ') ;
   CALL sp_execsql(SQL1);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_diary_active_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_diary_active_log`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_diary_active_log`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime datetime )
BEGIN
  
  DECLARE sqlstr VARCHAR(250) DEFAULT ' ' ; 
  
  SET sqlstr=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr) ;

  DELETE FROM db_analysis.t_player_diary_active_log WHERE game_site=@GAME_SITE AND create_time=CONCAT(date(dtime),' 23:59:59') ;

  SET sqlstr=CONCAT(' SELECT  @total_reg:=COUNT(player_id)   FROM ',dbgamename,'.t_player WHERE create_time<=''',dtime,'''');  
  CALL sp_execsql(sqlstr);

  SET sqlstr=CONCAT(' SELECT  @day_login:=COUNT(DISTINCT(player_id))   FROM ',dblogname,'.t_login_log  WHERE DATE(login_time)=''',dtime,'''  ');  
  CALL sp_execsql(sqlstr);

  SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_player_diary_active_log(diary_active_num,register_total_num,create_time,GAME_SITE,AGENT) SELECT @day_login,@total_reg,CONCAT(DATE(''',date(dtime),'''),'' 23:59:59''),@GAME_SITE,@AGENT '); 
  
  CALL sp_execsql(sqlstr);   
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_five_online_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_five_online_log`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_five_online_log`(dblogname VARCHAR(30))
BEGIN
  
  DECLARE sqlstr VARCHAR(600) ;
  SET sqlstr=CONCAT(' SELECT game_site,agent into @gamesite,@agent from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr);
  
  
  SELECT CASE WHEN MAX(LOG_ID) IS NULL THEN 'Y' ELSE MAX(LOG_ID) END INTO @max_log_id FROM db_analysis.t_five_online_log WHERE GAME_SITE=@gamesite ;
  IF  @max_log_id='Y' THEN 
      SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_five_online_log(log_id,num,create_time,agent,game_site,create_date)
          SELECT log_id,num,create_time,@agent,@gamesite,create_date FROM ',dblogname,'.t_five_online_log ') ;
      CALL sp_execsql(sqlstr) ;
  ELSE 
      SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_five_online_log(log_id,num,create_time,agent,game_site,create_date)
          SELECT log_id,num,create_time,@agent,@gamesite,create_date FROM ',dblogname,'.t_five_online_log WHERE LOG_ID>@max_log_id ') ;
      CALL sp_execsql(sqlstr);
  END IF ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_five_pay_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_five_pay_log`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_five_pay_log`(dblogname VARCHAR(30))
BEGIN
  
  DECLARE sqlstr VARCHAR(600) ;
  SET sqlstr=CONCAT(' SELECT game_site,agent into @gamesite,@agent from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr);
  
  SELECT CASE WHEN MAX(FIVE_PAY_ID) IS NULL THEN 'Y' ELSE MAX(FIVE_PAY_ID) END INTO @ALL_FIVE_PAY_ID  FROM db_analysis.t_five_pay_log  WHERE game_site=@gamesite ;
  IF @ALL_FIVE_PAY_ID='Y' THEN
      SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_five_pay_log(five_pay_id,five_pay_num,create_time,game_site,create_date,agent)
          SELECT five_pay_id,five_pay_num,create_time,@gamesite,create_date,@agent FROM ',dblogname,'.t_five_pay_log ') ;
      CALL sp_execsql(sqlstr) ;
  ELSE 
      SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_five_pay_log(five_pay_id,five_pay_num,create_time,game_site,create_date,agent)
          SELECT five_pay_id,five_pay_num,create_time,@gamesite,create_date,@agent FROM ',dblogname,'.t_five_pay_log WHERE FIVE_PAY_ID> @ALL_FIVE_PAY_ID ') ;
      CALL sp_execsql(sqlstr) ;
  END IF ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_five_register_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_five_register_log`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_five_register_log`(dblogname VARCHAR(30))
BEGIN
  
  DECLARE sqlstr VARCHAR(600) ;
  SET sqlstr=CONCAT(' SELECT game_site,agent into @gamesite,@agent from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr);
  SELECT CASE WHEN MAX(LOG_ID) IS NULL THEN 'Y' ELSE MAX(LOG_ID) END INTO @max_log_id FROM db_analysis.t_five_register_log WHERE GAME_SITE=@gamesite ;
  IF  @max_log_id='Y' THEN 
      SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_five_register_log(log_id,num,create_time,agent,game_site,create_date)
          SELECT log_id,num,create_time,@agent,@gamesite,create_date FROM ',dblogname,'.t_five_register_log ') ;
      CALL sp_execsql(sqlstr) ;
  ELSE 
      SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_five_register_log(log_id,num,create_time,agent,game_site,create_date)
          SELECT log_id,num,create_time,@agent,@gamesite,create_date FROM ',dblogname,'.t_five_register_log WHERE LOG_ID>@max_log_id ') ;
      CALL sp_execsql(sqlstr) ;
  END IF ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_login_day
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_login_day`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_login_day`(IN dblogname VARCHAR(30))
    COMMENT '玩家登陆天数分析'
BEGIN
  
  DECLARE SQL1 VARCHAR(600) ;
  DECLARE SQL2 VARCHAR(8000) ;
  drop TEMPORARY TABLE if EXISTS da_temp_login_1;
  create TEMPORARY TABLE da_temp_login_1 (
	    player_id int,
	    login_days int
  )ENGINE=memory  ;

  SET SQL1=CONCAT(' INSERT into da_temp_login_1(player_id, login_days)  select a.player_id, count(a.player_id) num from 
                   (select player_id, date(login_time) time from ',dblogname,'.t_login_log  group by player_id, date(login_time)) a 
                    group by a.player_id; ') ;
  
  CALL sp_execsql(SQL1);

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;
  
  DELETE FROM db_analysis.t_da_login_day WHERE game_site=@GAME_SITE ;

  SET SQL2= CONCAT(' insert into db_analysis.t_da_login_day(num1,num2,num3,num4,num5,num6,num7,game_site,agent) 
       SELECT SUM(CASE WHEN login_days = 1 THEN 1 ELSE 0 END ),
              SUM(CASE WHEN login_days > 1 and login_days <= 3 THEN 1 ELSE 0 END ),
              SUM(CASE WHEN login_days > 3 and login_days <= 7 THEN 1 ELSE 0 END ),  
              SUM(CASE WHEN login_days > 7 and login_days <= 14 THEN 1 ELSE 0 END ),  
              SUM(CASE WHEN login_days > 14 and login_days <= 30 THEN 1 ELSE 0 END ),  
              SUM(CASE WHEN login_days > 30 and login_days <= 60 THEN 1 ELSE 0 END ),  
              SUM(CASE WHEN login_days > 60 THEN 1 ELSE 0 END ) ,@GAME_SITE,@AGENT from da_temp_login_1  ') ;
  
  CALL sp_execsql(SQL2) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_lose_1
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_lose_1`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_lose_1`(IN dblogname VARCHAR(30),IN dbgamename VARCHAR(30),IN dbbasename VARCHAR(30))
    COMMENT '玩家流失任务节点'
BEGIN
  
  DECLARE SQL1 VARCHAR(2000) ;
  drop TEMPORARY TABLE if EXISTS tem_lose_player;
  create TEMPORARY TABLE tem_lose_player (
    player_id int,
    task_id INT
  )ENGINE=memory ;

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;

  DELETE FROM db_analysis.t_da_lose_1 WHERE game_site=@GAME_SITE ; 

  SET SQL1 = concat(' insert into tem_lose_player  select g.player_id, g.task_id from  
                     (select b.player_id from (select player_id, max(logout_time) lastTime from ',dblogname,'.t_login_log group by player_id) b ) b 
                     LEFT JOIN ',dbgamename,'.t_player_task g  on g.PLAYER_ID = b.player_id and g.TYPE = 1; ');
  CALL sp_execsql(SQL1) ;  
  
  SET SQL1=CONCAT(' insert into db_analysis.t_da_lose_1(task_id,num,game_site,agent) select task_id, count(player_id) num,@GAME_SITE,@AGENT from  tem_lose_player  where task_id is not null group by task_id; ') ;
  CALL sp_execsql(SQL1)  ;

  SET SQL1=CONCAT('update db_analysis.t_da_lose_1 lose,',dbbasename,'.t_base_game_task gt set lose.task_name = gt.`NAME`,lose.task_level = gt.`LEVEL` where lose.task_id=gt.GAME_TASK_ID AND game_site=@GAME_SITE ') ;
  
  CALL sp_execsql(SQL1);
 
  DELETE FROM db_analysis.t_da_game_step WHERE game_site=@GAME_SITE ; 
  SET SQL1=CONCAT(' insert into db_analysis.t_da_game_step(game_step,num,game_site,agent) SELECT  a.STEP_ID,CASE WHEN b.num IS NOT NULL THEN b.num ELSE 0 END  num,@GAME_SITE,@AGENT   FROM ',dbbasename,'.t_base_game_step  a 
   LEFT  JOIN (select game_step, count(player_Id) num  from ',dbgamename,'.t_player_ext where game_step !=0 group by game_step ) b
           ON b.game_step=a.STEP_ID  ; ') ;
  CALL sp_execsql(SQL1);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_lose_2
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_lose_2`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_lose_2`(IN dblogname VARCHAR(30))
    COMMENT '玩家流失时间'
BEGIN
  
  DECLARE SQL1 VARCHAR(300) ;
  DECLARE SQL2 VARCHAR(1000) ;
  drop TEMPORARY TABLE if EXISTS tem_lose_2;
  drop TEMPORARY TABLE if EXISTS tem_lose_3;

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;

  DELETE FROM db_analysis.t_da_lose_2 WHERE game_site=@GAME_SITE;
  create TEMPORARY TABLE tem_lose_2 (
    player_id int ,
    KEY plid (player_id)
  )ENGINE=memory ;

  create TEMPORARY TABLE tem_lose_3 (
    player_id int,
    online_time int
  )ENGINE=memory;
 
  SET SQL1=CONCAT(' insert into tem_lose_2 select b.player_id from (select player_id, max(logout_time) lastTime from ',dblogname,'.t_login_log group by player_id) b where now() > DATE_ADD(b.lastTime,INTERVAL 1 day); ') ;
  
  CALL sp_execsql(SQL1) ;

  SET SQL1=CONCAT(' insert into tem_lose_3 select a.player_id,b.time from tem_lose_2 a INNER JOIN  (select player_id,sum(online_time/60) time from ',dblogname,'.t_login_log b group by player_id) b on a.player_id=b.player_id; ') ;
  
  CALL sp_execsql(SQL1) ;

  SET SQL2=CONCAT(' insert into db_analysis.t_da_lose_2(num1,num2,num3,num4,num5,num6,num7,num8,num9,game_site,agent)
    select  SUM(CASE WHEN online_time < 5 THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 5 and online_time < 10  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 10 and online_time < 30  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 30 and online_time < 60  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 60 and online_time < 3*60  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 3*60 and online_time < 24*60  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 24*60 and online_time < 24*3*60  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 24*3*60 and online_time < 24*7*60  THEN 1 ELSE 0 END),
	    	SUM(CASE WHEN online_time >= 24*7*60 THEN 1 ELSE 0 END),@GAME_SITE,@AGENT from tem_lose_3  ') ;
  CALL sp_execsql(SQL2) ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_online_time
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_online_time`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_online_time`(IN dblogname VARCHAR(30),dtime datetime )
    COMMENT '在线分析'
BEGIN
  
  DECLARE SQL1 VARCHAR(500) ; 
  SET @v_begin_date=dtime ;

  SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;    

  DELETE FROM db_analysis.t_da_online_time_1 WHERE game_site=@GAME_SITE AND create_time=@v_begin_date ;

  SET SQL1=CONCAT(' insert into db_analysis.t_da_online_time_1 (player_Id,player_name,online_time,create_time,game_site)
                    select player_Id, player_name, sum(online_time)/60,date(login_time), @GAME_SITE from ',dblogname,'.t_login_log WHERE DATE(LOGIN_TIME)=@v_begin_date group by player_id; ') ; 
  CALL sp_execsql(SQL1) ;
	
  DELETE FROM db_analysis.t_da_online_time_2 WHERE game_site=@GAME_SITE AND CREATE_TIME=@v_begin_date  ; 

	insert into db_analysis.t_da_online_time_2(create_time,num1,num2,num3,num4,num5,num6,num7,num8,game_site,agent )
        select @v_begin_date, SUM(CASE WHEN online_time < 1 THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 1 and online_time < 5  THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 5 and online_time < 10  THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 10 and online_time < 30 THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 30 and online_time < 60 THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 60 and online_time < 120 THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 120 and online_time < 180 THEN 1 ELSE 0 END ),
           SUM(CASE WHEN  online_time >= 180 THEN 1 ELSE 0 END ),@GAME_SITE,@AGENT 
           FROM  db_analysis.t_da_online_time_1
       WHERE create_time=@v_begin_date AND game_site=@GAME_SITE ;

  update db_analysis.t_da_online_time_2 set TOTAL_NUM = num1+num2+num3+num4+num5+num6+num7+num8 where game_site=@GAME_SITE AND create_time=@v_begin_date ;
  SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_pay_1
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_pay_1`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_pay_1`(IN dblogname VARCHAR(30), IN dbgamename VARCHAR(30), dtime datetime)
    COMMENT '玩家进入游戏当天付费率'
BEGIN
   
   DECLARE SQL1 VARCHAR(5000) ;
   DECLARE end_date DATE ; 
   SET end_date := dtime  ;
   SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
   
   SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE_ALL,@AGENT FROM ',dblogname,'.t_gamesite_agent_mult  ') ;
   CALL sp_execsql(SQL1) ;

   SET @num:=LENGTH(@GAME_SITE_ALL)-LENGTH(REPLACE(@GAME_SITE_ALL,',',''))+1 ;   
   SET @n:=1 ;
   WHILE @n<=@num DO
       SET @GAME_SITE:=SUBSTRING_INDEX(SUBSTRING_INDEX(@GAME_SITE_ALL,',',@n),',',-1) ;
       
       SET SQL1=CONCAT(' SELECT @u_begin_date:=date(MIN(CREATE_TIME)) FROM ',dbgamename,'.t_player WHERE site=@GAME_SITE  ') ;
       CALL sp_execsql(SQL1) ;

       DELETE FROM db_analysis.t_da_pay_1 WHERE game_site=@GAME_SITE  ;

       WHILE @u_begin_date<=end_date DO
           SET SQL1=CONCAT(' SELECT COUNT(player_id) INTO @regnum0827 FROM ',dbgamename,'.t_player where date(create_time)=@u_begin_date AND TYPE=1 AND site=@GAME_SITE ') ;
           CALL  sp_execsql(SQL1) ;
    
           SET SQL1=CONCAT(' insert into db_analysis.t_da_pay_1(statistic_time,register_num,pay_num1,pay_num2,pay_num3,pay_num7,game_site,agent ) 
                   select @u_begin_date,@regnum0827 ,   sum(case when b1.player_id is null then 0 else 1 end) paynum1,
				    	    		sum(case when b2.player_id is null then 0 else 1 end) paynum2, 
				        			sum(case when b3.player_id is null then 0 else 1 end) paynum3,
				    	    		sum(case when b4.player_id is null then 0 else 1 end) paynum7 ,@GAME_SITE,@AGENT
              from  (select player_id  from ',dbgamename,'.t_player where date(create_time) =@u_begin_date AND site=@GAME_SITE) a 
                    left JOIN (select distinct player_id from ',dbgamename,'.t_pay_log where date(CREATE_TIME)=@u_begin_date and pay_site=@GAME_SITE) b1 on a.player_id = b1.player_id  
	    			    		left JOIN	(select distinct player_id from ',dbgamename,'.t_pay_log where date(CREATE_TIME)=date_add(@u_begin_date,interval 1 day) and pay_site=@GAME_SITE) b2 on a.player_id = b2.player_id 
	    			    		left JOIN	(select distinct player_id from ',dbgamename,'.t_pay_log where date(CREATE_TIME)=date_add(@u_begin_date,interval 2 day) and pay_site=@GAME_SITE) b3 on a.player_id = b3.player_id 
	    				    	left JOIN	(select distinct player_id from ',dbgamename,'.t_pay_log where date(CREATE_TIME)=date_add(@u_begin_date,interval 6 day) and pay_site=@GAME_SITE) b4 on a.player_id = b4.player_id;  ') ;
	    	      CALL sp_execsql(SQL1) ;
		        	
              SET @u_begin_date=DATE_ADD(@u_begin_date,INTERVAL 1 DAY) ;
       END WHILE ; 
       UPDATE db_analysis.t_da_pay_1 set register_num = 0,PAY_NUM1=0,PAY_NUM2=0,PAY_NUM3=0,PAY_NUM7=0 where register_num =0 AND game_site=@GAME_SITE   ; 
       SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
       SET @n=@n+1 ;
   END WHILE ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_pay_2
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_pay_2`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_pay_2`(IN dblogname VARCHAR(30),IN dbgamename varchar(30))
    COMMENT '玩家首次付费'
BEGIN
  
  DECLARE SQL1 VARCHAR(2000) ;  
  SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED; 
  
  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;
   
  DELETE FROM db_analysis.t_da_pay_2 WHERE game_site=@GAME_SITE ;
  
  SET SQL1=CONCAT(' insert into db_analysis.t_da_pay_2(player_id,player_level,register_time,pay_time,game_site)
	 select a.player_id,a.player_level,case when b.register_time is null then CURDATE() else b.REGISTER_TIME END ,a.pay_time,@GAME_SITE from 	
  ( select player_id, player_level,min(create_time) as pay_time from ',dbgamename,'.t_pay_log group by player_id) a 
	LEFT JOIN ',dblogname,'.t_register_log b on a.player_Id = b.PLAYER_ID;   ') ;
  CALL sp_execsql(SQL1);
  

  SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;   
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_pay_3
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_pay_3`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_pay_3`(IN dblogname VARCHAR(30))
    COMMENT '玩家首次付费等级分布'
BEGIN
  
  DECLARE SQL1 VARCHAR(2000) ;
  SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;	

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;
  
  DELETE FROM db_analysis.t_da_pay_3 WHERE game_site=@GAME_SITE ;
   
  SET SQL1=CONCAT('insert into db_analysis.t_da_pay_3(pay_num1,pay_num2,pay_num3,pay_num4,pay_num5,pay_num6,pay_num7,pay_num8,pay_num9,pay_num10,game_site,agent)  
    SELECT SUM( CASE WHEN player_level < 20 THEN 1 ELSE 0 END ) ,
         SUM( CASE WHEN player_level BETWEEN 20 and 30  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 31 and 35  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 36 and 40  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 41 and 43  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 44 and 46  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 47 and 50  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 51 and 55  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level BETWEEN 56 and 60  THEN 1 ELSE 0 END ),
         SUM( CASE WHEN player_level > 60  THEN 1 ELSE 0 END ),@GAME_SITE,@AGENT
         FROM  db_analysis.t_da_pay_2  WHERE game_site=@GAME_SITE  ');
  CALL sp_execsql(SQL1) ;
  
 
  SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_pay_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_pay_log`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_pay_log`(dblogname VARCHAR(30), dbgamename VARCHAR(30))
BEGIN
  
  DECLARE sqlstr VARCHAR(300) ;
  SET sqlstr=CONCAT(' SELECT game_site INTO @gamesite FROM ',dblogname,'.t_gamesite_agent_mult ') ;
  CALL sp_execsql(sqlstr);
   
  SET @gamesite := CONCAT('"',@gamesite,'"') ;
  SET @gamesite := REPLACE(@gamesite,',','","') ;
  SET sqlstr=CONCAT(' SELECT CASE WHEN MAX(PAY_LOG_ID) IS NULL THEN ''Y'' ELSE MAX(PAY_LOG_ID) END INTO @PAY_LOG_ID FROM db_analysis.t_pay_log WHERE PAY_SITE IN(',@gamesite,')   ');
  CALL sp_execsql(sqlstr) ; 
  
  
  IF @PAY_LOG_ID='Y' THEN 
     SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_pay_log SELECT * FROM ',dbgamename,'.t_pay_log ') ;
     CALL sp_execsql(sqlstr) ;
     
  ELSE 
     SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_pay_log SELECT * FROM ',dbgamename,'.t_pay_log WHERE PAY_LOG_ID > @PAY_LOG_ID    ') ;
     CALL sp_execsql(sqlstr) ;
     
  END IF ; 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_retain
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_retain`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_retain`(IN dblogname VARCHAR(30) , IN dbgamename VARCHAR(30))
    COMMENT '1-30日留存分析'
BEGIN
  
  DECLARE v_end_date DATE;
  DECLARE v_agent VARCHAR(30) ;
  DECLARE SQL1 VARCHAR(5000);


  SET v_end_date = curdate();
  SET SQL1=CONCAT(' SELECT   @v_begin_date:=DATE(MIN(CREATE_TIME)) FROM ',dbgamename,'.t_player; ') ;
  CALL sp_execsql(SQL1);

  SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;

	SET SQL1=CONCAT(' DELETE FROM  db_analysis.t_da_retain  WHERE game_site=@GAME_SITE ') ;
  CALL sp_execsql(SQL1) ;

  WHILE @v_begin_date < v_end_date DO
      SET SQL1 = CONCAT(' SELECT COUNT(player_id)   INTO  @REGNUM0826 FROM ',dbgamename,'.t_player where DATE(CREATE_TIME)=@v_begin_date  AND TYPE=1  ' ) ;
      CALL sp_execsql(SQL1) ;
      
      
      SET SQL1 =CONCAT(' insert into db_analysis.t_da_retain(STATISTIC_TIME,REGISTER_NUM,LOGIN_NUM1,LOGIN_NUM2,LOGIN_NUM3,LOGIN_NUM4,LOGIN_NUM5,LOGIN_NUM6,LOGIN_NUM7,LOGIN_NUM14,LOGIN_NUM30,GAME_SITE,AGENT ) 
       SELECT * FROM (SELECT @v_begin_date, @REGNUM0826 ,
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=1 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A2 ,     
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=2 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A3 ,     
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=3 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A4 ,     
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=4 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A5 ,     
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=5 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A6,       
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=6 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A7,      
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=7 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0  END ) as A8,       
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=13 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0 END ) as A14,     
       SUM( CASE WHEN datediff(DATE(b.LOGIN_TIME),DATE(a.CREATE_TIME))=29 AND DATE(CREATE_TIME)=@v_begin_date THEN 1 ELSE 0 END ) as A30,@GAME_SITE,@AGENT   
       FROM ',dbgamename,'.t_player    a 
       INNER JOIN (SELECT player_id,LOGIN_TIME FROM ',dblogname,'.t_login_log  GROUP BY player_id,date(LOGIN_TIME) )  b
           ON b.PLAYER_ID = a.PLAYER_ID  ) d ') ;
      
			CALL sp_execsql(SQL1);
        
      SET @v_begin_date = DATE_ADD(@v_begin_date,INTERVAL 1 DAY);
   END WHILE;
   SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_da_shop_sell
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_da_shop_sell`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_da_shop_sell`(IN dblogname VARCHAR(30),IN dbgamename VARCHAR(30),  dtime VARCHAR(30) )
BEGIN
  
  DECLARE SQL1 VARCHAR(1000) ; 

  SET SQL1=CONCAT(' SELECT GAME_SITE,AGENT INTO @GAME_SITE,@AGENT FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(SQL1) ;
  DELETE FROM db_analysis.t_da_shop_sell WHERE game_site=@GAME_SITE AND STATISTIC_TIME=dtime ;

  SET SQL1=CONCAT('INSERT into db_analysis.t_da_shop_sell(statistic_time,item_id,num,price,game_site,agent) 
         select date(a.CREATE_TIME),item_id,sum(num), sum(price),@GAME_SITE,@AGENT from ',dblogname,'.t_item_buy_log  a 
            inner join ',dbgamename,'.t_player  b
                    ON b.player_id=a.player_id
                   AND b.type=1 
           where a.type=2 and date(a.CREATE_TIME)=''',dtime,''' group by  item_id;  ') ;
  CALL sp_execsql(SQL1) ;

  SET SQL1=CONCAT(' update db_analysis.t_da_shop_sell s, db_base_2181_0001.t_base_item i set s.`NAME` = i.`NAME` where s.ITEM_ID = i.ITEM_ID and  game_site=@GAME_SITE and STATISTIC_TIME=''',dtime,''' ') ;
  CALL sp_execsql(SQL1) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_daily_active_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_daily_active_count`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_daily_active_count`(dblogname VARCHAR(30), dbgamename VARCHAR(30),p_dtime CHAR(20))
BEGIN
  
  DECLARE dtime datetime ;
  DECLARE sqlstr VARCHAR(800) ;
   
  IF p_dtime='' THEN
     SET dtime = CURDATE() ;
  ELSE
     SET dtime = p_dtime ;
  END IF ;
  
  SET sqlstr = CONCAT(' SELECT SUM(CASE WHEN ACTIVE_VALUE>0 AND ACTIVE_VALUE<=19 THEN 1 ELSE 0 END ),
       SUM(CASE WHEN ACTIVE_VALUE>=20 AND ACTIVE_VALUE<=49 THEN 1 ELSE 0 END ), 
       SUM(CASE WHEN ACTIVE_VALUE>=50 AND ACTIVE_VALUE<=89 THEN 1 ELSE 0 END ),
       SUM(CASE WHEN ACTIVE_VALUE>=90 AND ACTIVE_VALUE<=139 THEN 1 ELSE 0 END ),
       SUM(CASE WHEN ACTIVE_VALUE>=140 AND ACTIVE_VALUE<=199 THEN 1 ELSE 0 END ),
       SUM(CASE WHEN ACTIVE_VALUE>=200 THEN 1 ELSE 0 END ) INTO @A10721 ,@A20721 ,@A30721 ,@A40721 ,@A50721 ,@A60721 
       FROM ',dbgamename,'.t_player_daily_active_count where CREATE_DATE=''',dtime,'''  ') ;
  CALL sp_execsql(sqlstr);
   
  SET sqlstr = CONCAT('SELECT game_site, AGENT INTO @gamesite0721, @agent0721 FROM  ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr);
 
  SET @lognum0721 := @A10721 + @A20721 + @A30721 + @A40721 + @A50721 + @A60721 ;
  DELETE FROM db_analysis.t_daily_active_count WHERE create_date = dtime AND game_site = @gamesite0721 ;
  INSERT INTO db_analysis.t_daily_active_count(LOGINNUM , A1, A2, A3, A4, A5, A6, create_date, game_site, agent) 
     VALUES(@lognum0721, @A10721, @A20721, @A30721, @A40721, @A50721, @A60721, dtime, @gamesite0721, @agent0721 ) ;  
 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_daily_active_sum
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_daily_active_sum`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_daily_active_sum`(dblogname VARCHAR(30), dbgamename VARCHAR(30), p_dtime char(20))
BEGIN
  
  DECLARE dtime  datetime ;
  DECLARE sqlstr VARCHAR(2000) ;
  IF p_dtime IS NULL THEN
     SET dtime=CURDATE() ;
  ELSE
     SET dtime=p_dtime ;
  END IF ; 
  SET sqlstr = CONCAT(' SELECT COUNT(DISTINCT PLAYER_ID) INTO @lognum0726  FROM ',dblogname,'.t_login_log WHERE DATE(LOGIN_TIME)= ''',dtime, '''' );
  CALL sp_execsql(sqlstr);

  SET sqlstr = CONCAT('SELECT game_site, AGENT INTO @gamesite0726, @agent0726 FROM  ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr);

  DELETE FROM db_analysis.t_daily_active_sum WHERE GAME_SITE=@gamesite0726 AND CREATE_DATE=dtime ;
  SET sqlstr=CONCAT(' INSERT  INTO  DB_ANALYSIS.t_daily_active_sum 
       SELECT ''',dtime,''' as CREATE_DATE ,
       @lognum0726 as LOGIN_NUM    ,
       SUM(CASE WHEN ONLINE_TIME>0 THEN 1 ELSE 0 END) as ONLINE_TIME ,
       SUM(LOGIN_VIP)  as LOGIN_VIP ,
       SUM(MULTI_INS_NUM) as MULTI_INS_NUM ,
       SUM(DAY_INS_EXP) as  DAY_INS_EXP ,  
       SUM(DAY_INS_MONEY) as DAY_INS_MONEY ,
       SUM(DAY_INS_MC) as DAY_INS_MC ,
       SUM(END_TOWER_NUM) as END_TOWER_NUM ,
       SUM(CASE WHEN BOSS_CHALLENGE_NUM>0 THEN 1 ELSE 0 END)  as  BOSS_CHALLENGE_NUM ,
       SUM(CASE WHEN ARENA_ACTIVITY_NUM>0 THEN 1 ELSE 0 END)  as  ARENA_ACTIVITY_NUM ,
       SUM(CASE WHEN ARENA_NUM>0 THEN 1 ELSE 0 END )  as  ARENA_NUM ,
       SUM(CASE WHEN CHAOS_BATTLE_NUM>0 THEN 1 ELSE 0 END ) as  CHAOS_BATTLE_NUM ,
       SUM(CASE WHEN EQUIPMENT_INT>0 THEN 1 ELSE 0 END ) as  EQUIPMENT_INT ,
       SUM(CASE WHEN EQUIPMENT_PRO>0 THEN 1 ELSE 0 END ) as  EQUIPMENT_PRO ,
       SUM(CASE WHEN EQUIPMENT_DIS>0 THEN 1 ELSE 0 END ) as  EQUIPMENT_DIS ,
       SUM(CASE WHEN UPGRADE_SKILL>0 THEN 1 ELSE 0 END ) as  UPGRADE_SKILL ,
       SUM(CASE WHEN UPGRADE_TALENT>0 THEN 1 ELSE 0 END ) as   UPGRADE_TALENT ,
       SUM(CASE WHEN GODDESS_LEVEL>0 THEN 1 ELSE 0 END ) as GODDESS_LEVEL ,
       SUM(CASE WHEN GODDESS_STAR>0 THEN 1 ELSE 0 END ) as GODDESS_STAR ,
       SUM(CASE WHEN UPGRADE_GEM>0 THEN 1 ELSE 0 END )  as UPGRADE_GEM ,
       SUM(CASE WHEN SPEND_DIAMOND>0 THEN 1 ELSE 0 END ) as SPEND_DIAMOND ,
       SUM(CASE WHEN GUILD_DONATE>0 THEN 1 ELSE 0 END ) as GUILD_DONATE ,
       SUM(CASE WHEN USE_FRUIT>0 THEN 1 ELSE 0 END ) as USE_FRUIT ,
       SUM(CASE WHEN OPEN_FET>0 THEN 1 ELSE 0 END ) as OPEN_FET ,
       @gamesite0726  as GAME_SITE,
       @agent0726     as AGENT 
    FROM    ',dbgamename,'.t_player_daily_active_count where create_date=''',dtime,'''  ') ;
  
  CALL sp_execsql(sqlstr);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_diamond_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_diamond_log`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_diamond_log`()
BEGIN
  DECLARE totaldiamond BIGINT ;
  DECLARE daybuydiamond INT   ;
  DECLARE dayusediamond INT   ;
  DECLARE time       datetime ; 
  SET time=CURDATE() ; 
  SELECT SUM(diamond) INTO totaldiamond FROM db_game_2181_9999.t_player_wealth ;
  SELECT SUM(diamond) INTO daybuydiamond FROM db_game_2181_9999.t_pay_log WHERE create_time>=time AND CREATE_TIME<date_add(time,INTERVAL 1 DAY) ;
  SELECT abs(SUM(VALUE)) INTO dayusediamond FROM db_log_2181_9999.t_diamond_log WHERE create_time>=time AND CREATE_TIME<date_add(time,INTERVAL 1 DAY) AND `VALUE`<=0;

  IF (daybuydiamond IS NULL ) THEN
      SET  daybuydiamond = 0 ;
  END IF ;
  IF (dayusediamond IS NULL ) THEN
      SET  dayusediamond = 0 ;
  END IF ;

   INSERT INTO db_log_2181_9999.t_diamond_stock_log(DIAMOND_STOCK_TOTAL_NUM,DIAMOND_STOCK_USE_NUM,DIAMOND_STOCK_BUY_NUM,DIAMOND_STOCK_CREATE_TIME) VALUES (totaldiamond,dayusediamond,daybuydiamond,CONCAT(DATE(time),' 23:59:59'))  ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_diary_active_log
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_diary_active_log`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_diary_active_log`()
BEGIN
  DECLARE total_reg  INT  ;
  DECLARE day_login  INT  ;
  DECLARE dtime  datetime ;
  DECLARE ddtime datetime ;

  SET dtime  = CURDATE() ;
  SET ddtime = date_add(dtime, INTERVAL 1 DAY) ;

  SELECT  COUNT(REGISTER_LOG_ID)     INTO  total_reg FROM db_log_2181_9999.t_register_log ; 
  SELECT  COUNT(DISTINCT(player_id)) INTO  day_login FROM db_log_2181_9999.t_login_log  WHERE login_time>=dtime AND login_time< ddtime; 
  
  DELETE FROM db_log_2181_9999.t_player_diary_active_log WHERE DATE(create_time)=DATE(dtime) ;
  INSERT INTO db_log_2181_9999.t_player_diary_active_log(diary_active_num,register_total_num,create_time) VALUES (day_login,total_reg,CONCAT(DATE(dtime),' 23:59:59')) ; 
  
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_execsql
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_execsql`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_execsql`(sqlstr text)
BEGIN
     SET @sql = sqlstr;
     PREPARE  stmt  FROM  @sql;
     EXECUTE  stmt;
     DEALLOCATE  PREPARE  stmt;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_five_count_time
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_five_count_time`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_five_count_time`(IN dblogname VARCHAR(30), IN dbgamename VARCHAR(30))
BEGIN
  
  DECLARE ftime datetime ;
  DECLARE stime datetime ;
  DECLARE sqlstr text    ;  
  SET ftime='2014-08-25 21:55:00' ;
  SET stime=DATE_ADD(ftime,INTERVAL -5 MINUTE )   ;  
WHILE  stime>='2014-08-25 10:00:00'   DO
  SELECT stime ;
  
  SET sqlstr=CONCAT(' SELECT game_site into @GUID0705 FROM ',dblogname,'.t_gamesite_agent  ') ;
  CALL sp_execsql(sqlstr) ;
  
  SET sqlstr=CONCAT(' SELECT COUNT(DISTINCT(a.PLAYER_ID)) INTO @regnum0705 FROM ',dblogname,'.t_register_log  a
    INNER JOIN ',dbgamename,'.t_player  b
            ON  a.PLAYER_ID=b.PLAYER_ID 
           AND  b.type=1 
     WHERE a.REGISTER_TIME BETWEEN ''',stime,''' AND ''',ftime,'''' ) ;    
  CALL sp_execsql(sqlstr) ; 
  
  SET sqlstr=CONCAT(' SELECT COUNT(DISTINCT(a.PLAYER_ID)) INTO @loginnum0705 FROM ',dblogname,'.t_login_log  a
    INNER JOIN ',dbgamename,'.t_player  b
            ON  a.PLAYER_ID=b.PLAYER_ID 
           AND  b.type=1 
     WHERE a.login_time >=''',stime,''' AND  a.login_time <= ''',ftime,'''
       or ( a.login_time <= ''',stime,''' AND a.logout_time > ''',stime,'''  )' ) ;   
  CALL sp_execsql(sqlstr);

  SET sqlstr=CONCAT(' select count(distinct(a.player_id)) into @rechargenum0705 from ',dbgamename,'.t_pay_log a 
    INNER JOIN ',dbgamename,'.t_player b
             on a.player_ID=b.player_id 
            AND b.type=1 
     where a.create_time between ''',stime,''' and  ''',ftime,'''' ) ;
  CALL sp_execsql(sqlstr);
  DELETE FROM db_analysis.t_five_min_count WHERE  create_time=ftime ;
  INSERT INTO db_analysis.t_five_min_count(create_time, regnum, rechargenum, logonnum, game_site) VALUES(ftime, @regnum0705, @rechargenum0705, @loginnum0705, @GUID0705  ) ;
  SET ftime=  stime ;
  SET stime=DATE_ADD(ftime,INTERVAL -5 MINUTE )   ;  
END WHILE ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_game_plate
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_game_plate`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_game_plate`(dblogname VARCHAR(30), dbgamename VARCHAR(30) )
BEGIN  
  
  DECLARE sqlstr VARCHAR(500) DEFAULT ' ' ;
  DECLARE dtime  datetime ;
  set dtime = CURRENT_DATE() ; 
  
  SET sqlstr=CONCAT(' SELECT SITE INTO @site0707 FROM ',dbgamename,'.t_player LIMIT 1  ') ;
  CALL sp_execsql(sqlstr);
  

  SET sqlstr=CONCAT(' SELECT COUNT(*) INTO @cnum0707 FROM ',dbgamename,'.t_player  WHERE type=1  ');
  CALL sp_execsql(sqlstr); 
  

  SET sqlstr=CONCAT(' SELECT COUNT(DISTINCT(player_id)),SUM(MONEY) INTO @cnum20140702, @needmoney20140702  FROM  ',dbgamename,'.t_pay_log  ') ;  
  CALL sp_execsql(sqlstr); 
  IF @needmoney20140702 IS NULL THEN
     SET @needmoney20140702= 0 ;
  END IF ;
  
  IF  @cnum20140702 = 0 THEN
    SET @CARPU:=0 ;
  ELSE 
    SET @CARPU := @needmoney20140702/@cnum20140702  ;
  END IF ;
  
  
  IF @cnum0707 = 0 THEN
    SET @NARPU := 0 ;
  ELSE
    SET @NARPU := @needmoney20140702/@cnum0707 ;
  END IF ;
  
 
  SET sqlstr=concat(' SELECT count(DISTINCT(a.PLAYER_ID))/@cnum0707*100  INTO @1dretain0707 FROM ',dblogname,'.t_login_log  a 
   INNER JOIN ',dbgamename,'.t_player    b
           ON b.PLAYER_ID=a.PLAYER_ID
          AND date(a.LOGIN_TIME)=DATE_ADD(date(b.CREATE_TIME),INTERVAL 1 DAY)  ') ;
  
  CALL sp_execsql(sqlstr); 
  
 
  SET sqlstr=concat(' SELECT count(DISTINCT(a.PLAYER_ID))/@cnum0707*100 INTO @7dretain0707 FROM ',dblogname,'.t_login_log  a 
   INNER JOIN ',dbgamename,'.t_player    b
           ON b.PLAYER_ID=a.PLAYER_ID
          AND date(a.LOGIN_TIME)=DATE_ADD(date(b.CREATE_TIME),INTERVAL 6 DAY) ') ;     
  CALL sp_execsql(sqlstr) ;
  
    SET sqlstr=concat(' SELECT  count(DISTINCT(a.PLAYER_ID))/@cnum0707*100   INTO @14dretain0707 FROM ',dblogname,'.t_login_log  a 
   INNER JOIN ',dbgamename,'.t_player    b
           ON b.PLAYER_ID=a.PLAYER_ID
          AND date(a.LOGIN_TIME)=DATE_ADD(date(b.CREATE_TIME),INTERVAL 13 DAY) ') ; 
  CALL sp_execsql(sqlstr) ;
  
    SET sqlstr=concat(' SELECT  count(DISTINCT(a.PLAYER_ID))/@cnum0707*100   INTO @30dretain0707 FROM ',dblogname,'.t_login_log  a 
   INNER JOIN ',dbgamename,'.t_player    b
           ON b.PLAYER_ID=a.PLAYER_ID
          AND date(a.LOGIN_TIME)=DATE_ADD(date(b.CREATE_TIME),INTERVAL 29 DAY) ') ;  
  CALL sp_execsql(sqlstr) ;   
  
  SET sqlstr=CONCAT(' select agent into @agent0707 from ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr);
 
  
  DELETE FROM db_analysis.t_game_plate_count WHERE   game_site=@site0707 ;
  INSERT INTO db_analysis.t_game_plate_count(regnum, recharge, rechmoney, carpu, narpu, type1, type7, type14, type30,  create_time, game_site ,agent) 
      VALUES(@cnum0707, @cnum20140702, @needmoney20140702, @CARPU, @NARPU, @1dretain0707, @7dretain0707, @14dretain0707, @30dretain0707, dtime, @site0707, @agent0707) ;   
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_get_godbattle
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_get_godbattle`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_get_godbattle`(IN dbgamename VARCHAR(30) ,IN dbbasename VARCHAR(30))
BEGIN 
-- CALL sp_get_godbattle('db_game_2181_9999','db_base_2181_9999')
  DECLARE sqlstr VARCHAR(8000) ;
  DECLARE beilv1  FLOAT DEFAULT 2    ;   -- 定义星级为0升到1的总倍率  
  DECLARE beilv2  FLOAT DEFAULT 5  ;   -- 定义星级为0升到2的总倍率  
  DECLARE beilv3  FLOAT DEFAULT 8 ;   -- 定义星级为0升到3的总倍率    
  DECLARE beilv4  FLOAT DEFAULT 12  ;   -- 定义星级为0升到4的总倍率   
  DECLARE beilv5  FLOAT DEFAULT 16  ;   -- 定义星级为0升到5的总倍率   
  DECLARE beilv6  FLOAT DEFAULT 21    ;   -- 定义星级为0升到6的总倍率    
  DECLARE beilv7  FLOAT DEFAULT 26  ;   -- 定义星级为0升到7的总倍率   
  DECLARE beilv8  FLOAT DEFAULT 32  ;   -- 定义星级为0升到8的总倍率          
  DECLARE beilv9  FLOAT DEFAULT 38  ;   -- 定义星级为0升到9的总倍率   
  DECLARE beilv10  FLOAT DEFAULT 45  ;   -- 定义星级为0升到10的总倍率    
  DECLARE beilv11 FLOAT DEFAULT  52  ;   -- 定义星级为0升到11的总倍率  
  DECLARE beilv12 FLOAT DEFAULT  60  ;   -- 定义星级为0升到12的总倍率        
  DECLARE beilv13 FLOAT DEFAULT  68 ;   -- 定义星级为0升到13的总倍率        
  DECLARE beilv14 FLOAT DEFAULT  77 ;   -- 定义星级为0升到14的总倍率   
  DECLARE beilv15 FLOAT DEFAULT  86 ;   -- 定义星级为0升到15的总倍率  
  DECLARE beilv16 FLOAT DEFAULT  96 ;   -- 定义星级为0升到16的总倍率  
  DECLARE beilv17 FLOAT DEFAULT  106 ;   -- 定义星级为0升到17的总倍率      
  DECLARE beilv18 FLOAT DEFAULT  117 ;   -- 定义星级为0升到18的总倍率         
  DECLARE beilv19 FLOAT DEFAULT  128 ;   -- 定义星级为0升到19的总倍率  
  DECLARE beilv20 FLOAT DEFAULT  140 ;   -- 定义星级为0升到20的总倍率  
  DECLARE beilv21 FLOAT DEFAULT  152 ;   -- 定义星级为0升到21的总倍率  
  DECLARE beilv22 FLOAT DEFAULT  165 ;   -- 定义星级为0升到22的总倍率      
  DECLARE beilv23 FLOAT DEFAULT  178 ;   -- 定义星级为0升到23的总倍率        
  DECLARE beilv24 FLOAT DEFAULT  192 ;   -- 定义星级为0升到24的总倍率   
  DECLARE beilv25 FLOAT DEFAULT  206 ;   -- 定义星级为0升到25的总倍率  
  SET sqlstr=CONCAT('SELECT CASE WHEN goddess_id=1 THEN @HP1:=HP  
            WHEN goddess_id=2 THEN @HP2:=HP 
            WHEN goddess_id=3 THEN @HP3:=HP 
            WHEN goddess_id=4 THEN @HP4:=HP  
            WHEN goddess_id=5 THEN @HP5:=HP 
            WHEN goddess_id=6 THEN @HP6:=HP 
            WHEN goddess_id=7 THEN @HP7:=HP 
            WHEN goddess_id=8 THEN @HP8:=HP   
            WHEN goddess_id=30 THEN @HP30:=HP END 
     FROM   ',dbbasename,'.t_base_goddess ' ) ;
  CALL sp_execsql(sqlstr) ;     
  SET sqlstr=CONCAT('SELECT CASE WHEN goddess_id=1 THEN @ATK1:=ATK 
            WHEN goddess_id=2 THEN @ATK2:=ATK 
            WHEN goddess_id=3 THEN @ATK3:=ATK 
            WHEN goddess_id=4 THEN @ATK4:=ATK  
            WHEN goddess_id=5 THEN @ATK5:=ATK 
            WHEN goddess_id=6 THEN @ATK6:=ATK
            WHEN goddess_id=7 THEN @ATK7:=ATK 
            WHEN goddess_id=8 THEN @ATK8:=ATK   
            WHEN goddess_id=30 THEN @ATK30:=ATK END 
     FROM   ',dbbasename,'.t_base_goddess  ') ;
  CALL sp_execsql(sqlstr) ;
  SET sqlstr=CONCAT('SELECT CASE WHEN goddess_id=1 THEN @DEF1:=DEF 
            WHEN goddess_id=2 THEN @DEF2:=DEF  
            WHEN goddess_id=3 THEN @DEF3:=DEF
            WHEN goddess_id=4 THEN @DEF4:=DEF   
            WHEN goddess_id=5 THEN @DEF5:=DEF 
            WHEN goddess_id=6 THEN @DEF6:=DEF 
            WHEN goddess_id=7 THEN @DEF7:=DEF 
            WHEN goddess_id=8 THEN @DEF8:=DEF 
            WHEN goddess_id=30 THEN @DEF30:=DEF END 
     FROM   ',dbbasename,'.t_base_goddess ') ;
  CALL sp_execsql(sqlstr);
  SET sqlstr=CONCAT('SELECT CASE WHEN goddess_id=1 THEN @ADDHP1:=HP_ADD
            WHEN goddess_id=2 THEN @ADDHP2:=HP_ADD
            WHEN goddess_id=3 THEN @ADDHP3:=HP_ADD
            WHEN goddess_id=4 THEN @ADDHP4:=HP_ADD  
            WHEN goddess_id=5 THEN @ADDHP5:=HP_ADD 
            WHEN goddess_id=6 THEN @ADDHP6:=HP_ADD 
            WHEN goddess_id=7 THEN @ADDHP7:=HP_ADD 
            WHEN goddess_id=8 THEN @ADDHP8:=HP_ADD  
            WHEN goddess_id=30 THEN @ADDHP30:=HP_ADD END 
     FROM   ',dbbasename,'.t_base_goddess ') ;
  CALL sp_execsql(sqlstr);
  SET sqlstr=CONCAT('SELECT CASE WHEN goddess_id=1 THEN @ADDATK1:=ATK_ADD 
            WHEN goddess_id=2 THEN @ADDATK2:=ATK_ADD  
            WHEN goddess_id=3 THEN @ADDATK3:=ATK_ADD 
            WHEN goddess_id=4 THEN @ADDATK4:=ATK_ADD 
            WHEN goddess_id=5 THEN @ADDATK5:=ATK_ADD
            WHEN goddess_id=6 THEN @ADDATK6:=ATK_ADD 
            WHEN goddess_id=7 THEN @ADDATK7:=ATK_ADD 
            WHEN goddess_id=8 THEN @ADDATK8:=ATK_ADD 
            WHEN goddess_id=30 THEN @ADDATK30:=ATK_ADD  END 
     FROM   ',dbbasename,'.t_base_goddess ');
  CALL sp_execsql(sqlstr) ;
  SET sqlstr=CONCAT('SELECT CASE WHEN goddess_id=1 THEN @ADDDEF1:=DEF_ADD
            WHEN goddess_id=2 THEN @ADDDEF2:=DEF_ADD  
            WHEN goddess_id=3 THEN @ADDDEF3:=DEF_ADD
            WHEN goddess_id=4 THEN @ADDDEF4:=DEF_ADD  
            WHEN goddess_id=5 THEN @ADDDEF5:=DEF_ADD
            WHEN goddess_id=6 THEN @ADDDEF6:=DEF_ADD 
            WHEN goddess_id=7 THEN @ADDDEF7:=DEF_ADD
            WHEN goddess_id=8 THEN @ADDDEF8:=DEF_ADD  
            WHEN goddess_id=30 THEN @ADDDEF30:=DEF_ADD END 
     FROM   ',dbbasename,'.t_base_goddess ');
  CALL sp_execsql(sqlstr);

  SET sqlstr=CONCAT('UPDATE ',dbgamename,'.t_player_goddess   
    SET battle_value=((CASE WHEN goddess_id=1 THEN  @ATK1  WHEN goddess_id=2 THEN  @ATK2 
                        WHEN goddess_id=3 THEN  @ATK3  WHEN goddess_id=4 THEN  @ATK4 
                        WHEN goddess_id=5 THEN  @ATK5  WHEN goddess_id=6 THEN  @ATK6 
                        WHEN goddess_id=7 THEN  @ATK7  WHEN goddess_id=8 THEN  @ATK8 
                        WHEN goddess_id=30 THEN @ATK30 END)+(LEVEL-1)* ( CASE WHEN goddess_id=1 THEN  @ADDATK1
                        WHEN goddess_id=2 THEN  @ADDATK2  WHEN goddess_id=3 THEN  @ADDATK3
                        WHEN goddess_id=4 THEN  @ADDATK4  WHEN goddess_id=5 THEN  @ADDATK5 
                        WHEN goddess_id=6 THEN  @ADDATK6  WHEN goddess_id=7 THEN  @ADDATK7 
                        WHEN goddess_id=8 THEN  @ADDATK8  WHEN goddess_id=30 THEN @ADDATK30 END) + (CASE WHEN star=0 THEN 0 WHEN star=1 THEN ',beilv1,' 
                        WHEN star=2 THEN ',beilv2,' WHEN star=3 THEN ',beilv3,' WHEN star=4 THEN ',beilv4,' WHEN star=5 THEN ',beilv5,'
                        WHEN star=6 THEN ',beilv6,' WHEN star=7 THEN ',beilv7,' WHEN star=8 THEN ',beilv8,' WHEN star=9 THEN ',beilv9,'
                        WHEN star=10 THEN ',beilv10,' WHEN star=11 THEN ',beilv11,' WHEN star=12 THEN ',beilv12,' WHEN star=13 THEN ',beilv13,'
                        WHEN star=14 THEN ',beilv14,' WHEN star=15 THEN ',beilv15,' WHEN star=16 THEN ',beilv16,' WHEN star=17 THEN ',beilv17,'
                        WHEN star=18 THEN ',beilv18,' WHEN star=19 THEN ',beilv19,' WHEN star=20 THEN ',beilv20,' WHEN star=21 THEN ',beilv21,' 
                        WHEN star=22 THEN ',beilv22,' WHEN star=23 THEN ',beilv23,' WHEN star=24 THEN ',beilv24,' WHEN star=25 THEN ',beilv25,' END)*( CASE WHEN goddess_id=1 THEN  @ADDATK1
                        WHEN goddess_id=2 THEN  @ADDATK2  WHEN goddess_id=3 THEN  @ADDATK3
                        WHEN goddess_id=4 THEN  @ADDATK4  WHEN goddess_id=5 THEN  @ADDATK5 
                        WHEN goddess_id=6 THEN  @ADDATK6  WHEN goddess_id=7 THEN  @ADDATK7 
                        WHEN goddess_id=8 THEN  @ADDATK8  WHEN goddess_id=30 THEN @ADDATK30 END))     -- 攻击加成
                       +((CASE WHEN goddess_id=1 THEN  @DEF1  WHEN goddess_id=2 THEN  @DEF2 
                        WHEN goddess_id=3 THEN  @DEF3  WHEN goddess_id=4 THEN  @DEF4 
                        WHEN goddess_id=5 THEN  @DEF5  WHEN goddess_id=6 THEN  @DEF6 
                        WHEN goddess_id=7 THEN  @DEF7  WHEN goddess_id=8 THEN  @DEF8 
                        WHEN goddess_id=30 THEN @DEF30 END)+(LEVEL-1)* ( CASE WHEN goddess_id=1 THEN  @ADDDEF1
                        WHEN goddess_id=2 THEN  @ADDDEF2  WHEN goddess_id=3 THEN  @ADDDEF3 
                        WHEN goddess_id=4 THEN  @ADDDEF4  WHEN goddess_id=5 THEN  @ADDDEF5 
                        WHEN goddess_id=6 THEN  @ADDDEF6  WHEN goddess_id=7 THEN  @ADDDEF7 
                        WHEN goddess_id=8 THEN  @ADDDEF8  WHEN goddess_id=30 THEN @ADDDEF30 END) + (CASE WHEN star=0 THEN 0 WHEN star=1 THEN ',beilv1,' 
                        WHEN star=2 THEN ',beilv2,' WHEN star=3 THEN ',beilv3,' WHEN star=4 THEN ',beilv4,' WHEN star=5 THEN ',beilv5,'
                        WHEN star=6 THEN ',beilv6,' WHEN star=7 THEN ',beilv7,' WHEN star=8 THEN ',beilv8,' WHEN star=9 THEN ',beilv9,'
                        WHEN star=10 THEN ',beilv10,' WHEN star=11 THEN ',beilv11,' WHEN star=12 THEN ',beilv12,' WHEN star=13 THEN ',beilv13,'
                        WHEN star=14 THEN ',beilv14,' WHEN star=15 THEN ',beilv15,' WHEN star=16 THEN ',beilv16,' WHEN star=17 THEN ',beilv17,'
                        WHEN star=18 THEN ',beilv18,' WHEN star=19 THEN ',beilv19,' WHEN star=20 THEN ',beilv20,' WHEN star=21 THEN ',beilv21,' 
                        WHEN star=22 THEN ',beilv22,' WHEN star=23 THEN ',beilv23,' WHEN star=24 THEN ',beilv24,' WHEN star=25 THEN ',beilv25,' END)* ( CASE WHEN goddess_id=1 THEN  @ADDDEF1
                        WHEN goddess_id=2 THEN  @ADDDEF2  WHEN goddess_id=3 THEN  @ADDDEF3 
                        WHEN goddess_id=4 THEN  @ADDDEF4  WHEN goddess_id=5 THEN  @ADDDEF5 
                        WHEN goddess_id=6 THEN  @ADDDEF6  WHEN goddess_id=7 THEN  @ADDDEF7 
                        WHEN goddess_id=8 THEN  @ADDDEF8  WHEN goddess_id=30 THEN @ADDDEF30 END))*2   -- 防御加成
                        +((CASE WHEN goddess_id=1 THEN  @HP1  WHEN goddess_id=2 THEN  @HP2 
                        WHEN goddess_id=3 THEN  @HP3  WHEN goddess_id=4 THEN  @HP4 
                        WHEN goddess_id=5 THEN  @HP5  WHEN goddess_id=6 THEN  @HP6 
                        WHEN goddess_id=7 THEN  @HP7  WHEN goddess_id=8 THEN  @HP8 
                        WHEN goddess_id=30 THEN @HP30 END)+(LEVEL-1)* ( CASE WHEN goddess_id=1 THEN @ADDHP1
                        WHEN goddess_id=2 THEN @ADDHP2 WHEN goddess_id=3 THEN @ADDHP3 
                        WHEN goddess_id=4 THEN @ADDHP4 WHEN goddess_id=5 THEN @ADDHP5
                        WHEN goddess_id=6 THEN @ADDHP6 WHEN goddess_id=7 THEN @ADDHP7 
                        WHEN goddess_id=8 THEN @ADDHP8 WHEN goddess_id=30 THEN @ADDHP30 END) + (CASE WHEN star=0 THEN 0 WHEN star=1 THEN ',beilv1,' 
                        WHEN star=2 THEN ',beilv2,' WHEN star=3 THEN ',beilv3,' WHEN star=4 THEN ',beilv4,' WHEN star=5 THEN ',beilv5,'
                        WHEN star=6 THEN ',beilv6,' WHEN star=7 THEN ',beilv7,' WHEN star=8 THEN ',beilv8,' WHEN star=9 THEN ',beilv9,'
                        WHEN star=10 THEN ',beilv10,' WHEN star=11 THEN ',beilv11,' WHEN star=12 THEN ',beilv12,' WHEN star=13 THEN ',beilv13,'
                        WHEN star=14 THEN ',beilv14,' WHEN star=15 THEN ',beilv15,' WHEN star=16 THEN ',beilv16,' WHEN star=17 THEN ',beilv17,'
                        WHEN star=18 THEN ',beilv18,' WHEN star=19 THEN ',beilv19,' WHEN star=20 THEN ',beilv20,' WHEN star=21 THEN ',beilv21,' 
                        WHEN star=22 THEN ',beilv22,' WHEN star=23 THEN ',beilv23,' WHEN star=24 THEN ',beilv24,' WHEN star=25 THEN ',beilv25,' END)* ( CASE WHEN goddess_id=1 THEN @ADDHP1
                        WHEN goddess_id=2 THEN @ADDHP2 WHEN goddess_id=3 THEN @ADDHP3 
                        WHEN goddess_id=4 THEN @ADDHP4 WHEN goddess_id=5 THEN @ADDHP5
                        WHEN goddess_id=6 THEN @ADDHP6 WHEN goddess_id=7 THEN @ADDHP7 
                        WHEN goddess_id=8 THEN @ADDHP8 WHEN goddess_id=30 THEN @ADDHP30 END))/10  ; -- 生命加成') ;
  CALL sp_execsql(sqlstr) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_index_trend_plate
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_index_trend_plate`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_index_trend_plate`(dblogname VARCHAR(30), dbgamename VARCHAR(30),p_dtime CHAR(20))
BEGIN
  
  DECLARE sqlstr VARCHAR(600) ;
  DECLARE dtime  datetime ; 
  IF p_dtime='' THEN 
     SET dtime=CURRENT_DATE() ;
  ELSE
     SET dtime= p_dtime ;
  END IF ; 
  SET sqlstr=CONCAT(' SELECT game_site ,agent INTO @game_site0708, @agent0708 FROM ',dblogname,'.t_gamesite_agent  ');
  CALL sp_execsql(sqlstr); 
  
  SET sqlstr=CONCAT(' SELECT @money0707:=(CASE WHEN SUM(MONEY) IS NULL THEN 0 ELSE SUM(MONEY)  END ) , @plidcount0707:=(COUNT(DISTINCT(PLAYER_ID)) )  FROM ',dbgamename,'.t_pay_log  WHERE date(CREATE_TIME)=''',dtime,'''  ');
  CALL sp_execsql(sqlstr);
  

  SET sqlstr=CONCAT(' SELECT @ologinnum0708:=COUNT(DISTINCT(PLAYER_ID)) FROM ',dblogname,'.t_login_log 
          WHERE DATE(LOGIN_TIME)=''',dtime,''' 
            AND PLAYER_ID NOT IN (SELECT  PLAYER_ID FROM ',dbgamename,'.t_player WHERE DATE(CREATE_TIME)=''',dtime,''' and type=1 )   ');
  CALL sp_execsql(sqlstr) ;

  SET sqlstr=CONCAT(' SELECT @nloginnum0708:=COUNT(PLAYER_ID) FROM ',dbgamename,'.t_player WHERE DATE(CREATE_TIME)=''',dtime,'''  and type=1  ');
  CALL sp_execsql(sqlstr);
  SET @loginnum0707:= @ologinnum0708 + @nloginnum0708 ;

  
  SET sqlstr=CONCAT(' SELECT  NUM   INTO @v_loginpeak   FROM ',dblogname,'.t_five_online_log   WHERE date(create_time)=''',dtime,'''  ORDER BY NUM  DESC LIMIT 1  ');
  CALL sp_execsql(sqlstr);

  IF @v_loginpeak IS NULL THEN
     SET @v_loginpeak:=0 ;
  END IF ;

  
  SET sqlstr=CONCAT('SELECT SUM(NUM)/COUNT(*) into @v_loginave FROM ',dblogname,'.t_five_online_log WHERE create_date=''',dtime,''' ') ;
  CALL sp_execsql(sqlstr);

  IF @v_loginave IS NULL THEN
     SET @v_loginave:=0 ;
  END IF ;
  
  SET sqlstr=CONCAT(' SELECT @paycountnum0708:=COUNT(DISTINCT(player_id)) ,@paymoney0708:=(CASE WHEN SUM(MONEY) IS NULL THEN 0 ELSE SUM(MONEY) END)  FROM ',dbgamename,'.t_pay_log WHERE  DATE(CREATE_TIME)=''',dtime,'''  ');
  CALL sp_execsql(sqlstr) ;
 
   
  IF @loginnum0707=0 THEN 
     SET @payrate0708='0' ;
  ELSE 
     SET @payrate0708:= left(@paycountnum0708/@loginnum0707 *100,10) ;
  END IF ;
  SELECT @payrate0708 ; 
  IF @paycountnum0708=0 THEN
     SET @payarpu0708=0 ;
  ELSE
     SET @payarpu0708:=  @paymoney0708/@paycountnum0708  ;
  END IF ;
 
  
  SET sqlstr=CONCAT(' SELECT @newpaymoney0708:=( CASE WHEN SUM(a.MONEY) IS NULL THEN 0 ELSE SUM(a.MONEY) END ), 
         @newcount0708:=COUNT(DISTINCT b.PLAYER_ID)  FROM ',dbgamename,'.t_pay_log  a 
   INNER JOIN ',dbgamename,'.t_player   b
           ON b.PLAYER_ID=a.PLAYER_ID
          AND date(b.CREATE_TIME)=''',dtime,''' 
         where date(a.CREATE_TIME)=''',dtime,''' '); 
  CALL sp_execsql(sqlstr);

  
  SET sqlstr=CONCAT(' SELECT @countpid0708:=COUNT(player_id) FROM ',dbgamename,'.t_player WHERE DATE(CREATE_TIME)=''',dtime,''' AND TYPE=1  ') ; 
  CALL sp_execsql(sqlstr) ;    
  
  IF @countpid0708=0 THEN 
     SET @newpayrate0708='0' ;
  ELSE 
     SET @newpayrate0708:= @newcount0708/@countpid0708*100 ;
  END IF ;
  
  IF  @newcount0708=0 THEN 
     SET @newpayARPU0708=0 ;
  ELSE 
     SET @newpayARPU0708:=@newpaymoney0708/@newcount0708 ;
  END IF ;

  
  SET @oldpaysum0708:=@money0707-@newpaymoney0708 ;
  SET @oldpaycount0708:=@plidcount0707-@newcount0708 ;
  
  SET @oldloginnum0708:=@loginnum0707-@countpid0708 ;

         
  SET @oldpaycountnum0708:=@paycountnum0708 - @countpid0708 ;
         
  SET @oldpaymoney0708   :=@paymoney0708 - @newpaymoney0708 ;
  
  IF @countpid0708=0 THEN 
     SET @oldpayARPU0708=0 ;
  ELSE 
     SET @oldpayARPU0708    :=@oldpaymoney0708/@countpid0708  ;
  END IF ; 

  
  IF @oldloginnum0708=0 THEN
     SET @oldpayrate0708:=0 ;
  ELSE 
     SET @oldpayrate0708:=@countpid0708/@oldloginnum0708 ;
  END IF ;
  
  
  DELETE FROM db_analysis.t_index_trend_plate WHERE create_time=dtime AND game_site=@game_site0708  ;
  INSERT INTO db_analysis.t_index_trend_plate(money, paynum, loginnum, onlinepeak, onlineave, payrate, payarpu, nmoney, npaynum, nloginnum, npayrate, npayarpu, omoney, opaynum, ologinnum, opayrate, opayarpu, create_time, game_site, agent)
         VALUES (@money0707, @plidcount0707 , @loginnum0707, @v_loginpeak, @v_loginave, left(@payrate0708,10) ,left(@payarpu0708,10) ,@newpaymoney0708 , @newcount0708 , @countpid0708, left(@newpayrate0708,10), left(@newpayARPU0708,10), @oldpaysum0708, @oldpaycount0708, @oldloginnum0708, left(@oldpayrate0708,10), left(@oldpayARPU0708,10), dtime, @game_site0708,@agent0708) ;
 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_login_num
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_login_num`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_login_num`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime datetime, OUT p_out INT )
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE num     INT ;
  DECLARE sqlstr VARCHAR(300) ; 
  SET v_dtime=dtime ;
 
  
  
  SET sqlstr = CONCAT(' SELECT COUNT(DISTINCT a.PLAYER_ID) INTO @outnum0818  FROM ',dblogname,'.t_login_log  a 
 INNER JOIN ',dbgamename,'.t_player b
         ON b.PLAYER_ID = a.PLAYER_ID 
        AND DATE(b.create_time)=DATE(a.LOGIN_TIME)
  WHERE DATE(LOGIN_TIME)=''',v_dtime,''' ') ;
  CALL sp_execsql(sqlstr) ;
  
  SET sqlstr = CONCAT(' SELECT COUNT(PLAYER_ID) INTO @reg0818 FROM ',dbgamename,'.t_player WHERE DATE(create_time)=''',v_dtime,''' ') ;
  CALL sp_execsql(sqlstr) ;
  
  SET sqlstr = CONCAT(' SELECT COUNT(DISTINCT a.PLAYER_ID) INTO @num0818  FROM ',dblogname,'.t_login_log  a  WHERE DATE(LOGIN_TIME)=''',v_dtime,''' ' ) ;
  CALL sp_execsql(sqlstr) ;
  SET  num = @num0818+@reg0818-@outnum0818 ;
  SELECT num INTO p_out ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_login_num_level
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_login_num_level`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_login_num_level`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime datetime, lev TINYINT UNSIGNED, OUT p_out INT)
BEGIN 
  
  DECLARE sqlstr VARCHAR(400) ;
  DECLARE num INT ;
  SET sqlstr = CONCAT(' SELECT COUNT(DISTINCT a.PLAYER_ID) INTO @plnum FROM  ',dblogname,'.t_login_log  a 
    INNER JOIN ',dbgamename,'.t_player_property b
            ON b.PLAYER_ID=a.PLAYER_ID 
           AND b.`LEVEL`>=',lev,'
    INNER JOIN ',dbgamename,'.t_player c
            ON c.player_id=a.player_id
         WHERE DATE(a.LOGIN_TIME)=''',dtime,'''') ;
  CALL sp_execsql(sqlstr) ;
  SELECT @plnum INTO p_out ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_login_user_content
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_login_user_content`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_login_user_content`(dblogname VARCHAR(30), dbgamename VARCHAR(30), dtime CHAR(10))
BEGIN
  
  DECLARE v_dtime datetime ;
  DECLARE sqlstr VARCHAR(1800) ;
  IF dtime='' THEN
     SET v_dtime=CURDATE() ;
  ELSE
     SET v_dtime=dtime ;
  END IF ; 
  
  SET sqlstr = CONCAT(' SELECT COUNT(player_id) INTO @REGISTER_NUM0731 FROM ',dbgamename,'.t_player WHERE DATE(CREATE_TIME)=''',v_dtime,''' AND type=1  ; ') ;
  CALL sp_execsql(sqlstr);

  
  SET sqlstr = CONCAT('  SELECT COUNT(DISTINCT a.player_id) INTO  @LOGIN_NUM0731  FROM ',dblogname,'.t_login_log a where DATE(LOGIN_TIME)=''',v_dtime,'''  ') ;
  CALL sp_execsql(sqlstr) ;
  
  
  SET sqlstr = concat(' SELECT COUNT(DISTINCT a.player_id)  INTO @NEWLOG0828  FROM ',dblogname,'.t_login_log a 
    INNER JOIN ',dbgamename,'.t_player b
      ON b.player_id=a.PLAYER_ID 
     AND  DATE(CREATE_TIME)=''',v_dtime,''' 
    where DATE(LOGIN_TIME)=''',v_dtime,''' ') ;
  CALL sp_execsql(sqlstr) ;  

     

  
  SET  @OLD_USER0731:=  @LOGIN_NUM0731-@NEWLOG0828 ;

  
  SET sqlstr=CONCAT(' SELECT    SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 7 DAY )  AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 14 DAY) THEN 1 ELSE 0 END)  REG_TWO_WEEK ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 14 DAY ) AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 30 DAY) THEN 1 ELSE 0 END)  REG_ONE_MON  ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 30 DAY ) AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 60 DAY) THEN 1 ELSE 0 END)  REG_TWO_MON  ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 60 DAY ) AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 90 DAY) THEN 1 ELSE 0 END)  REG_THR_MON  ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 90 DAY ) AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 120 DAY) THEN 1 ELSE 0 END)  REG_FOUR_MON  ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 120 DAY ) AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 150 DAY) THEN 1 ELSE 0 END)  REG_FIVE_MON ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 150 DAY ) AND b.LOGIN_TIME<=DATE_ADD(a.CREATE_TIME,INTERVAL 180 DAY) THEN 1 ELSE 0 END)  REG_SIX_MON  ,
       SUM(CASE WHEN   b.LOGIN_TIME>DATE_ADD(a.CREATE_TIME,INTERVAL 180 DAY ) THEN 1 ELSE 0 END)  REG_MORE_MON    
       INTO @REG_TWO_WEEK0731, @REG_ONE_MON0731, @REG_TWO_MON0731, @REG_THR_MON0731, @REG_FOUR_MON0731, @REG_FIVE_MON0731, @REG_SIX_MON0731, @REG_MORE_MON0731
   FROM ',dbgamename,'.t_player  a 
        INNER JOIN (  
            SELECT DISTINCT PLAYER_ID ,DATE(LOGIN_TIME) LOGIN_TIME FROM ',dblogname,'.t_login_log    
             where   DATE(LOGIN_TIME)=''',v_dtime,''' 
            )b  ON b.PLAYER_ID=a.PLAYER_ID  ' ) ; 
   
   CALL sp_execsql(sqlstr)  ;
   
   
   IF  @LOGIN_NUM0731=0 THEN
       SET  @REG_ONE_WEEK0731:=0 ;
       SET  @REG_TWO_WEEK0731:=0 ;    
       SET  @REG_ONE_MON0731:=0 ; 
       SET  @REG_TWO_MON0731:=0 ; 
       SET  @REG_THR_MON0731:=0 ; 
       SET  @REG_FOUR_MON0731:=0 ; 
       SET  @REG_FIVE_MON0731:=0 ; 
       SET  @REG_SIX_MON0731:=0 ; 
       SET  @REG_MORE_MON0731:=0 ; 
   END IF ;
   
   SET @REG_ONE_WEEK0731 = @LOGIN_NUM0731 - @REG_TWO_WEEK0731 - @REG_ONE_MON0731 - @REG_TWO_MON0731 - @REG_THR_MON0731 - @REG_FOUR_MON0731 - @REG_FIVE_MON0731 - @REG_SIX_MON0731 - @REG_MORE_MON0731 ;

   SET  sqlstr=CONCAT('  SELECT GAME_SITE,agent INTO @gamesite0731,@agent0731 FROM ',dblogname,'.t_gamesite_agent ') ;
   CALL sp_execsql(sqlstr) ; 
   
   DELETE FROM db_analysis.t_login_user_content WHERE CREATE_DATE=v_dtime AND GAME_SITE=@gamesite0731 ;
   INSERT INTO db_analysis.t_login_user_content(LOGIN_NUM, REGISTER_NUM, OLD_USER, REG_ONE_WEEK, REG_TWO_WEEK, REG_ONE_MON, REG_TWO_MON, REG_THR_MON, REG_FOUR_MON, REG_FIVE_MON, REG_SIX_MON, CREATE_DATE, GAME_SITE, AGENT) 
        VALUES(@LOGIN_NUM0731, @REGISTER_NUM0731, @OLD_USER0731, @REG_ONE_WEEK0731, @REG_TWO_WEEK0731, @REG_ONE_MON0731, @REG_TWO_MON0731, @REG_THR_MON0731, @REG_FOUR_MON0731, @REG_FIVE_MON0731, @REG_SIX_MON0731, v_dtime, @gamesite0731, @agent0731) ;  
 
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_manage_big_customer
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_manage_big_customer`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_manage_big_customer`(dblogname VARCHAR(30),dbgamename VARCHAR(30))
BEGIN
  
  DECLARE sqlstr VARCHAR(3000) ;

  DROP TABLE IF EXISTS tmp_player_id ;
  CREATE TABLE tmp_player_id
  ( player_id INT   
   ) ENGINE=memory ;
  
  SET sqlstr=CONCAT(' INSERT INTO  tmp_player_id(player_id)  SELECT PLAYER_ID FROM (
      SELECT DISTINCT player_id,SUM(MONEY)  SUMMONEY   FROM ',dbgamename,'.t_pay_log   GROUP BY PLAYER_ID 
     HAVING SUMMONEY>=1000 )b') ;
  CALL sp_execsql(sqlstr);

  SET sqlstr = CONCAT('SELECT GAME_SITE,agent INTO @gamesite0902,@agent  FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr);  
  
  DELETE FROM db_analysis.t_manage_big_customer  where GAME_SITE=@gamesite0902 ;

  CALL sp_execsql(sqlstr);
  SET sqlstr=CONCAT(' INSERT INTO db_analysis.t_manage_big_customer 
  SELECT b.USER_NAME,a.PLAYER_ID , b.`NAME`,@agent,@agent,a.PAY_SITE,SUM(a.MONEY)  SUMMONEY ,e.SUMCOST ,IFNULL(f.WEEKMONEY,0), IFNULL(g.FOURWEEKMON,0),DATEDIFF(CURDATE(),DATE(EXIT_TIME)) LEVDAY,i.RECLEVDAY
         FROM ',dbgamename,'.t_pay_log a 
   INNER JOIN ',dbgamename,'.t_player b
           ON b.PLAYER_ID=a.PLAYER_ID 
   INNER JOIN ',dblogname,'.t_gamesite_agent  c
           ON c.GAME_SITE=a.PAY_SITE 
   INNER JOIN tmp_player_id d
           ON d.player_id=a.PLAYER_ID
   INNER JOIN (SELECT PLAYER_ID,SUM(PRICE)/10 SUMCOST FROM ',dblogname,'.t_item_buy_log   WHERE TYPE=2  GROUP BY PLAYER_ID   ) e 
           ON e.PLAYER_ID=a.PLAYER_ID
   LEFT JOIN (SELECT PLAYER_ID,SUM(MONEY) WEEKMONEY FROM ',dbgamename,'.t_pay_log WHERE DATEDIFF(CURDATE(),DATE(CREATE_TIME))<=7   GROUP BY player_id  ) f
           ON f.PLAYER_ID=a.PLAYER_ID
   LEFT JOIN (SELECT PLAYER_ID,SUM(MONEY) FOURWEEKMON FROM ',dbgamename,'.t_pay_log WHERE DATEDIFF(CURDATE(),DATE(CREATE_TIME))<=28  GROUP BY player_id  ) g
           ON g.PLAYER_ID=a.PLAYER_ID
   INNER JOIN ',dbgamename,'.t_player_ext h
           ON h.PLAYER_ID=a.PLAYER_ID
   INNER JOIN (SELECT PLAYER_ID, DATEDIFF(CURDATE(),DATE(max(CREATE_TIME))) reclevday FROM ',dbgamename,'.t_pay_log  GROUP BY PLAYER_ID  ) i
           ON i.player_id=a.player_id
   GROUP BY PLAYER_ID  
   ORDER BY SUMMONEY DESC ;') ;
  CALL sp_execsql(sqlstr);
  
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_online_time_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_online_time_count`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `sp_online_time_count`(dblogname VARCHAR(30), dbgamename VARCHAR(30), ddate CHAR(10))
BEGIN 
  
  DECLARE sqlstr VARCHAR(4000) ;
  DECLARE dtime  datetime ;

  IF ddate='' THEN 
     SET dtime=CURDATE() ; 
  ELSE 
     SET dtime=ddate ;
  END IF ;

  SET sqlstr=CONCAT(' TRUNCATE TABLE db_analysis.t_da_online_time_1  ');
  CALL sp_execsql(sqlstr) ;


  
  SET sqlstr=CONCAT( ' INSERT INTO db_analysis.t_da_online_time_1(player_id, player_name, online_time, CREATE_time, game_site) 
                      SELECT a.PLAYER_ID ,`NAME` , ceil(sum(logout_time-login_time)/60), DATE(LOGIN_TIME),b.site FROM ',dblogname,'.t_login_log  a 
                       INNER JOIN ',dbgamename,'.t_player  b
                               ON a.PLAYER_ID=b.PLAYER_ID
                              AND type=1 
                      GROUP BY a.PLAYER_ID ,DATE(LOGIN_TIME) 
                      ORDER BY a.PLAYER_ID ,DATE(LOGIN_TIME);
                        ') ;
  CALL sp_execsql(sqlstr) ;
  SET sqlstr=CONCAT(' select agent,game_site INTO @agent0711,@gamesite0711  from ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr);   

  SET sqlstr=CONCAT(' SELECT SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<1 THEN 1 ELSE 0 END ) AS a,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<2 and  ONLINE_TIME>=1  THEN 1 ELSE 0 END ) AS b,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<3 and  ONLINE_TIME>=2 THEN 1 ELSE 0 END ) AS c,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<4 and  ONLINE_TIME>=3  THEN 1 ELSE 0 END ) AS d,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<5 and  ONLINE_TIME>=4  THEN 1 ELSE 0 END ) AS e,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<6 and  ONLINE_TIME>=5 THEN 1 ELSE 0 END ) AS f,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<7 and  ONLINE_TIME>=6  THEN 1 ELSE 0 END ) AS g,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<8 and  ONLINE_TIME>=7  THEN 1 ELSE 0 END ) AS h,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<9 and  ONLINE_TIME>=8  THEN 1 ELSE 0 END ) AS i,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<10 and  ONLINE_TIME>=9  THEN 1 ELSE 0 END ) AS j,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<20 and  ONLINE_TIME>=10  THEN 1 ELSE 0 END ) AS k,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<30 and  ONLINE_TIME>=20  THEN 1 ELSE 0 END ) AS l,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<40 and  ONLINE_TIME>=30  THEN 1 ELSE 0 END ) AS m,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<50 and  ONLINE_TIME>=40  THEN 1 ELSE 0 END ) AS n,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<60 and  ONLINE_TIME>=50  THEN 1 ELSE 0 END ) AS o,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<120 and  ONLINE_TIME>=60  THEN 1 ELSE 0 END ) AS p,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<180 and  ONLINE_TIME>=120  THEN 1 ELSE 0 END ) AS q,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<240 and  ONLINE_TIME>=180  THEN 1 ELSE 0 END ) AS r,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<300 and  ONLINE_TIME>=240  THEN 1 ELSE 0 END ) AS s,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<360 and  ONLINE_TIME>=300  THEN 1 ELSE 0 END ) AS t,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<420 and  ONLINE_TIME>=360  THEN 1 ELSE 0 END ) AS u,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<480 and  ONLINE_TIME>=420  THEN 1 ELSE 0 END ) AS v,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<540 and  ONLINE_TIME>=480  THEN 1 ELSE 0 END ) AS w,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME<600 and  ONLINE_TIME>=540  THEN 1 ELSE 0 END ) AS x,
       SUM(CASE WHEN CREATE_TIME=''',dtime,''' AND ONLINE_TIME>=600  THEN 1 ELSE 0 END ) AS y  INTO @a10711 ,@a20711,
       @a30711 ,@a40711 ,@a50711 ,@a60711 ,@a70711 ,@a80711 ,@a90711 ,@a100711 ,@a110711 ,@a120711 ,@a130711 ,@a140711 ,
       @a150711 ,@a160711 ,@a170711 ,@a180711 ,@a190711 ,@a200711 ,@a210711 ,@a220711 ,@a230711 ,@a240711 ,@a250711 
  FROM db_analysis.t_da_online_time_1 ');
  CALL sp_execsql(sqlstr) ;


  DELETE FROM db_analysis.t_online_time_count WHERE create_time=dtime AND game_site= @gamesite0711 ;
  INSERT INTO db_analysis.t_online_time_count(num1, num2, num3, num4, num5, num6, num7, num8, num9, num10, num11, num12, num13, num14, num15, num16, num17, num18, num19, num20, num21, num22, num23, num24, num25, create_time, game_site, agent) 
    VALUE (@a10711, @a20711, @a30711, @a40711, @a50711, @a60711, @a70711, @a80711, @a90711, @a100711, @a110711, @a120711, @a130711, @a140711, @a150711, @a160711, @a170711, @a180711, @a190711, @a200711, @a210711, @a220711, @a230711, @a240711, @a250711, dtime, @gamesite0711, @agent0711)   ;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_temple_of_damned
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_temple_of_damned`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_temple_of_damned`(dblogname VARCHAR(30),dbgamename VARCHAR(30),p_dtime datetime)
BEGIN
  
  DECLARE dtime datetime ; 
  DECLARE sqlstr VARCHAR(1100) ; 
  SET dtime=p_dtime ;
  CALL  sp_login_num_level(dblogname,dbgamename,p_dtime,35,@out) ;
  SET @num0905:=@out ;
  SET sqlstr=CONCAT(' SELECT game_site,agent INTO @gamesite0905,@agent0905 FROM ',dblogname,'.t_gamesite_agent ') ;
  CALL sp_execsql(sqlstr);
  
  DELETE FROM  db_analysis.t_temple_of_damned WHERE game_site=@gamesite0905 AND CREATE_DATE=dtime ;
  SET sqlstr=CONCAT('INSERT INTO db_analysis.t_temple_of_damned
   SELECT @num0905,@num0905-A10-A30-A60-A90-A120-A180-A240-A300-A380, A10,A30,A60,A90,A120,A180,A240,A300,A380,''',dtime,''',@gamesite0905,@agent0905  FROM (
     SELECT SUM(CASE WHEN DO_TIME/60<10 THEN 1 ELSE 0 END) A10 ,
       SUM(CASE WHEN DO_TIME/60>=10 AND DO_TIME/60<30 THEN 1 ELSE 0 END) A30,
       SUM(CASE WHEN DO_TIME/60>=30 AND DO_TIME/60<60 THEN 1 ELSE 0 END) A60,
       SUM(CASE WHEN DO_TIME/60>=30 AND DO_TIME/60<90 THEN 1 ELSE 0 END) A90,
       SUM(CASE WHEN DO_TIME/60>=90 AND DO_TIME/60<120 THEN 1 ELSE 0 END) A120,
       SUM(CASE WHEN DO_TIME/60>=120 AND DO_TIME/60<180 THEN 1 ELSE 0 END)A180,
       SUM(CASE WHEN DO_TIME/60>=180 AND DO_TIME/60<240 THEN 1 ELSE 0 END) A240,
       SUM(CASE WHEN DO_TIME/60>=240 AND DO_TIME/60<300 THEN 1 ELSE 0 END) A300,
       SUM(CASE WHEN DO_TIME/60>=300 THEN 1 ELSE 0 END) A380 ,
CREATE_TIME FROM ',dblogname,'.t_player_map_time_log 
WHERE DATE(CREATE_TIME)=''',dtime,''' AND MAP_TYPE=1 ) aa') ;
  
  CALL sp_execsql(sqlstr) ;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for sp_user_info
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_user_info`;
DELIMITER ;;
CREATE DEFINER=`gs`@`%` PROCEDURE `sp_user_info`(dblogname VARCHAR(30), dbgamename VARCHAR(30))
BEGIN
  
  DECLARE ddate datetime ;
  DECLARE sqlstr VARCHAR(2000) ;
  SET ddate=CURDATE() ;
  SET sqlstr=CONCAT(' SELECT game_site,agent INTO @gamesite1013,@agent1013 FROM ',dblogname,'.t_gamesite_agent') ;
  CALL sp_execsql(sqlstr) ;
  DELETE FROM db_analysis.t_da_user_info WHERE game_site=@gamesite1013 ;
  SET sqlstr=CONCAT('INSERT INTO db_analysis.t_da_user_info SELECT a.player_id,a.NAME,CASE WHEN a.career=0 THEN ''新手'' WHEN a.CAREER=1 THEN ''剑士'' WHEN a.CAREER=2 THEN ''法师'' WHEN a.CAREER=3 THEN ''枪手'' END CAREER,
       b.LEVEL,b.BATTLE_VALUE,c.VIP,CASE WHEN e.RANK IS NULL THEN 0 ELSE e.RANK END RANK ,CASE WHEN d.POSITION IS NULL THEN ''未加入公会'' WHEN d.POSITION=1 THEN ''会长大人'' WHEN d.POSITION=2 THEN ''副会长理事'' WHEN d.POSITION=3 THEN ''普通会员'' END POSITION ,
       f.DIAMOND,f.BIND_DIAMOND,g.maxlogin-DATE(a.create_time)+1 REGDATE,CASE WHEN h.LOGMUM IS NULL THEN 0 ELSE h.LOGMUM end LOGMUM,CASE WHEN i.LOGTIME is null THEN 0 ELSE i.LOGTIME END LOGTIME ,''tel'',@gamesite1013,@agent1013
  from ',dbgamename,'.t_player a 
  INNER JOIN db_analysis.t_manage_big_customer  z
          ON z.PLAYER_ID=a.PLAYER_ID
         AND z.GAME_SITE=a.SITE 
  LEFT JOIN ',dbgamename,'.t_player_property  b 
          ON b.PLAYER_ID=a.PLAYER_ID 
  LEFT JOIN ',dbgamename,'.t_player_vip c
          ON c.PLAYER_ID=a.PLAYER_ID
  LEFT JOIN ',dbgamename,'.t_guild_player d
          ON d.PLAYER_ID=a.PLAYER_ID
  LEFT JOIN ',dbgamename,'.t_guild e
          ON e.guild_id=d.guild_id
  LEFT JOIN ',dbgamename,'.t_player_wealth f
          ON f.PLAYER_ID=a.PLAYER_ID
  LEFT JOIN (SELECT player_id,MAX(date(login_time)) maxlogin FROM ',dblogname,'.t_login_log GROUP BY PLAYER_ID )  g
          ON g.player_id=a.player_id 
  LEFT JOIN (SELECT player_id,COUNT(DISTINCT DATE(LOGIN_TIME)) logmum FROM ',dblogname,'.t_login_log WHERE DATE(LOGIN_TIME)>=DATE_ADD(''',ddate,''',INTERVAL -7 DAY)
               GROUP BY PLAYER_ID  ) h
          ON h.PLAYER_ID=a.PLAYER_ID 
  LEFT JOIN (SELECT PLAYER_ID,SUM(ONLINE_TIME)/7  logtime FROM db_analysis.t_da_online_time_1 
                WHERE  DATE(CREATE_TIME)>=DATE_ADD(''',ddate,''',INTERVAL -7 DAY)  GROUP BY player_id ) i 
          ON i.player_id=a.player_id  ') ;
  CALL sp_execsql(sqlstr) ; 
  
END
;;
DELIMITER ;
