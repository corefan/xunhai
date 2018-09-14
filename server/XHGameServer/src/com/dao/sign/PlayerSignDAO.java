package com.dao.sign;

import com.db.GameSqlSessionTemplate;
import com.domain.sign.PlayerSign;

public class PlayerSignDAO extends GameSqlSessionTemplate{
	
	/**
	 * 创建玩家签到
	 * */
	public void createPlayerSign(PlayerSign playerSign) {
		this.insert_noreturn(playerSign.getInsertSql());
	}
	
	/**
	 * 取玩家签到信息
	 */
	public PlayerSign getPlayerSign(long playerId){
		String playerSignSql = "select * from player_sign where playerId = "+playerId;
		return this.selectOne(playerSignSql, PlayerSign.class);
	}
	
	public void quartzDaliyPlayerSign(){
		String sql = "UPDATE player_sign SET conSignDay = 0, conSignRewardStr = null where state = 0 ";
		this.update(sql);
		
		String sql2 = "UPDATE player_sign SET state = 0 WHERE state > 0";
		this.update(sql2);
	}
	
	
	public void quartzMonthPlayerSign(){
		String sql = "UPDATE player_sign SET signNum = 0, reSignNum = 0, state = 0, conSignDay = 0, conSignRewardStr = null";
		this.update(sql);
	}
}
