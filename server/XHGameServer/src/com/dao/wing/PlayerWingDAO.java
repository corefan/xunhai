package com.dao.wing;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.wing.PlayerWing;

public class PlayerWingDAO extends GameSqlSessionTemplate{
	/**
	 * 创建玩家注灵
	 */
	public void createPlayerWing(PlayerWing playerWing) {
		this.insert_noreturn(playerWing.getInsertSql());
	}
	
	/**
	 * 获得玩家注灵
	 */
	public List<PlayerWing> listPlayerWing(long playerId) {
		String sql = "SELECT * FROM player_wing  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerWing.class);
	}
}
