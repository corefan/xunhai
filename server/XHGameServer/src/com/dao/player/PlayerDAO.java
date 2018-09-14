package com.dao.player;

import java.util.List;
import java.util.Map;

import com.db.GameSqlSessionTemplate;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.util.LogUtil;

/**
 * 玩家DAO
 * @author ken
 * @date 2016-12-23
 */
public class PlayerDAO extends GameSqlSessionTemplate {

	
	/**
	 * 根据账号获得玩家列表
	 */
	public List<Player> listPlayerByUserId(long userId) {

		String sql = "select * from player where userId = " + userId + " and deleteFlag = 0";

		return this.selectList(sql, Player.class);
	}

	/**
	 * 根据编号取玩家
	 */
	public Player getPlayerByPlayerId(long playerId){
		String sql = "select * from player where playerId = "+playerId+" and deleteFlag = 0";
		return this.selectOne(sql, Player.class);
	}
	
	/**
	 * 根据编号取玩家
	 */
	public Player getPlayerByPlayerName(String playerName){
		String sql = "select * from player where playerName = '"+playerName+"' and deleteFlag = 0";
		return this.selectOne(sql, Player.class);
	}
	
	/**
	 * 根据编号取玩家扩展信息
	 */
	public PlayerExt getPlayerExtById(long playerId){
		String sql = "select * from player_ext where playerId = "+playerId+" and deleteFlag = 0";
		return this.selectOne(sql, PlayerExt.class);
	}
	
	/**
	 * 根据编号取玩家属性信息
	 */
	public PlayerProperty getPlayerPropertyById(long playerId){
		String sql = "select * from player_property where playerId = "+playerId+" and deleteFlag = 0";
		return this.selectOne(sql, PlayerProperty.class);
	}
	
	/**
	 * 根据编号取玩家操作信息
	 */
	public PlayerOptional getPlayerOptionalById(long playerId){
		String sql = "select * from player_optional where playerId = "+playerId+" and deleteFlag = 0";
		return this.selectOne(sql, PlayerOptional.class);
	}
	
	/**
	 * 根据编号取玩家每日数据
	 */
	public PlayerDaily getPlayerDailyById(long playerId){
		String sql = "select * from player_daily where playerId = "+playerId+" and deleteFlag = 0";
		return this.selectOne(sql, PlayerDaily.class);
	}
	
	public PlayerWealth getPlayerWealthById(long playerId){
		String sql = "select * from player_wealth where playerId = "+playerId+" and deleteFlag = 0";
		return this.selectOne(sql, PlayerWealth.class);
	}
	
	/**
	 * 0点重置每日数据
	 */
	public void updateAllPlayerDailyData() {
		this.update(PlayerDaily.getUpdateAllSql());
		
		this.updateAllPlayerMonthCard();
	}
	
	
	/**
	 * 每天凌晨调度
	 */
	public void updateAllPlayerMonthCard(){
		//UPDATE player_daily SET monthCardVaildTime = 0 WHERE 	DATE_SUB(NOW(),INTERVAL 0 DAY) = from_unixtime(monthCardVaildTime/1000))
		
		// 当有效时间到今天截止时重置(TO_DAYS: 给定一个日期date，返回一个天数（自0年的天数）)
		String sql = "UPDATE player_daily SET monthCardVaildTime = 0 WHERE TO_DAYS(NOW()) = TO_DAYS(from_unixtime(monthCardVaildTime/1000))";
		this.update(sql);	
	}

	
	/**
	 * 每周一重置数据
	 */
	public void updateAllPlayerWeekData(){
		String sql = "update player_ext set weekTotalNum = 0";
		this.update(sql);
	}	
	
	/**
	 * 得到活跃的玩家扩展列表
	 * */
	public List<PlayerExt> getActivePlayerExtList(int day) {
		String activePlayerExtSql = "SELECT * FROM player_ext WHERE exitTime >= DATE_SUB(NOW(),INTERVAL "+day+" DAY) and deleteFlag = 0 ORDER BY exitTime DESC LIMIT 0,100";
		
		return this.selectList(activePlayerExtSql, PlayerExt.class);
	}
	
	/**
	 * 获取当前服创过角用户数， 总创角人数
	 */
	public int[] getCreateNum(String gameSite){
		String sql = "select count(distinct userId) pNum, count(playerId) createNum from player where site = '"+gameSite+"'";
		Map<String,Object> map = this.selectOne(sql);
		
		int[] arr = new int[2];
		arr[0] = Integer.parseInt(map.get("pNum").toString());
		arr[1] = Integer.parseInt(map.get("createNum").toString());
		return arr;
	}
	
	/**
	 * 各在线时长人数
	 */
	public Map<String,Object> getOnlineTimeNum(String gameSite, String date){
		String sql = "select (select count(playerId) from player_ext where TO_DAYS(loginTime) = TO_DAYS('"+date+"') and playerId in (select playerId from player where site = '"+gameSite+"')) loginNum, " +
				"count(case when everyOnlineTime >= 60 then 1 else null end ) num1, " +
				"count(case when everyOnlineTime >= 300 then 1 else null end ) num5, " +
				"count(case when everyOnlineTime >= 600 then 1 else null end ) num10, " +
				"count(case when everyOnlineTime >= 1200 then 1 else null end ) num20, " +
				"count(case when everyOnlineTime >= 1800 then 1 else null end ) num30, " +
				"count(case when everyOnlineTime >= 2400 then 1 else null end ) num40, " +
				"count(case when everyOnlineTime >= 3000 then 1 else null end ) num50, " +
				"count(case when everyOnlineTime >= 3600 then 1 else null end ) num60, " +
				"count(case when everyOnlineTime >= 18000 then 1 else null end ) h5, " +
				"count(case when everyOnlineTime >= 36000 then 1 else null end ) h10, " +
				"count(case when everyOnlineTime > 36000 then 1 else null end ) uph10 " +
				"from player_daily where playerId in (select playerId from player where site = '"+gameSite+"');";
		return this.selectOne(sql);
	}
	
	/**
	 * 删除角色
	 */
	public void deletePlayer(long playerId){
		try {
			String sql = "update player set deleteFlag = 1 where playerId = "+playerId;
			this.update(sql);
		} catch (Exception e) {
			LogUtil.error("删除player错误："+e);
		}
		
		try {
			String sql = "update player_ext set deleteFlag = 1 where playerId = "+playerId;
			this.update(sql);
		} catch (Exception e) {
			LogUtil.error("删除player_ext错误："+e);
		}
		
		try {
			String sql = "update player_property set deleteFlag = 1 where playerId = "+playerId;
			this.update(sql);
		} catch (Exception e) {
			LogUtil.error("删除player_property错误："+e);
		}
		
		try {
			String sql = "update player_wealth set deleteFlag = 1 where playerId = "+playerId;
			this.update(sql);
		} catch (Exception e) {
			LogUtil.error("删除player_wealth错误："+e);
		}
		
		try {
			String sql = "update player_daily set deleteFlag = 1 where playerId = "+playerId;
			this.update(sql);
		} catch (Exception e) {
			LogUtil.error("删除player_daily错误："+e);
		}
		
		try {
			String sql = "update player_optional set deleteFlag = 1 where playerId = "+playerId;
			this.update(sql);
		} catch (Exception e) {
			LogUtil.error("删除player_optional错误："+e);
		}
	}
	
	
	/****************************GM**************************/
	/**
	 * 根据账号获得玩家列表
	 */
	public List<Player> listPlayerByUserId_GM(long userId) {

		String sql = "select * from player where userId = " + userId;

		return this.selectList(sql, Player.class);
	}

	/**
	 * 根据编号取玩家
	 */
	public Player getPlayerByPlayerId_GM(long playerId){
		String sql = "select * from player where playerId = "+playerId;
		return this.selectOne(sql, Player.class);
	}
	
	/**
	 * 根据编号取玩家
	 */
	public Player getPlayerByPlayerName_GM(String playerName){
		String sql = "select * from player where playerName like '%"+playerName+"%' LIMIT 0,50";
		return this.selectOne(sql, Player.class);
	}
	
	/**
	 * 根据编号取玩家扩展信息
	 */
	public PlayerExt getPlayerExtById_GM(long playerId){
		String sql = "select * from player_ext where playerId = "+playerId;
		return this.selectOne(sql, PlayerExt.class);
	}
	
	/**
	 * 根据编号取玩家属性信息
	 */
	public PlayerProperty getPlayerPropertyById_GM(long playerId){
		String sql = "select * from player_property where playerId = "+playerId;
		return this.selectOne(sql, PlayerProperty.class);
	}
	
	public PlayerWealth getPlayerWealthById_GM(long playerId){
		String sql = "select * from player_wealth where playerId = "+playerId;
		return this.selectOne(sql, PlayerWealth.class);
	}	

	/** 找出所有离线超过两天的玩家(与活跃缓存对应)*/
	public List<PlayerExt> getOfflinePlayerExt(int day){
		String sql = "select * from player_ext where loginTime < exitTime and exitTime < DATE_SUB(NOW(),INTERVAL "+day+" DAY)";
		return this.selectList(sql, PlayerExt.class);
	}	
	
	/**
	 * 查找购买成长基金的玩家总人数
	 */
	public int getBuyGrowthFundNum() {		
		String sql = "select count(isBuyGrowthFund) total from player_optional where isBuyGrowthFund = 1";
		
		Map<String,Object> map = this.selectOne(sql);
		
		return Integer.parseInt(map.get("total").toString());
	}
}


