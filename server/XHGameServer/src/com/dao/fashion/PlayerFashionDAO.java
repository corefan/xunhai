package com.dao.fashion;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.fashion.PlayerFashion;

/**
 * 玩家时装dao
 * @author ken
 * @date 2017-2-13
 */
public class PlayerFashionDAO extends GameSqlSessionTemplate {

	/**
	 * 创建玩家时装
	 * */
	public void createPlayerFashion(PlayerFashion playerFashion) {
		this.insert_noreturn(playerFashion.getInsertSql());
	}
	
	/**
	 * 获得玩家时装列表
	 * */
	public List<PlayerFashion> listPlayerFashions(long playerId) {
		String sql = "SELECT * FROM player_fashion  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerFashion.class);
	}
}
