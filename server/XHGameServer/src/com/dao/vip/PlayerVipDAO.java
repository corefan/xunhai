package com.dao.vip;

import com.db.GameSqlSessionTemplate;
import com.domain.vip.PlayerVip;

/**
 * 玩家vip数据
 * @author jiangqin
 * @date 2017-6-15
 */
public class PlayerVipDAO  extends GameSqlSessionTemplate{
	
	/** 创建仇敌信息*/
	public void createPlayerVip(PlayerVip playerVip) {
		String sql = playerVip.getInsertSql();		
		this.insert_noreturn(sql);
	}	
	
	/** 取玩家vip数据*/
	public PlayerVip getPlayerVip(long playerId){
		String sql = "SELECT * FROM player_vip" + "  WHERE playerId="+playerId;

		return this.selectOne(sql, PlayerVip.class);
	}
	
	
	
}
