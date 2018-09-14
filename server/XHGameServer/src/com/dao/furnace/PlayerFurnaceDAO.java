package com.dao.furnace;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.furnace.PlayerFurnace;

/**
 * 玩家熔炉DAO
 * @author ken
 * @date 2018年4月23日
 */
public class PlayerFurnaceDAO extends GameSqlSessionTemplate {

	/**
	 * 创建玩家熔炉
	 * */
	public void createPlayerFurnace(PlayerFurnace model) {
		this.insert_noreturn(model.getInsertSql());
	}
	
	/**
	 * 获得玩家熔炉列表
	 * */
	public List<PlayerFurnace> listPlayerFurnaces(long playerId) {
		String sql = "SELECT * FROM player_furnace  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerFurnace.class);
	}
}
