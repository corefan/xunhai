package com.dao.wakan;

import java.util.List;
import com.db.GameSqlSessionTemplate;
import com.domain.wakan.PlayerWakan;

public class PlayerWakanDao extends GameSqlSessionTemplate{
	/**
	 * 创建玩家注灵
	 * */
	public void createPlayerWakan(PlayerWakan playerWakan) {
		this.insert_noreturn(playerWakan.getInsertSql());
	}
	
	/**
	 * 获得玩家注灵
	 * */
	public List<PlayerWakan> listPlayerWakans(long playerId) {
		String sql = "SELECT * FROM player_wakan  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerWakan.class);
	}
}
