package com.dao.bag;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;

/**
 * 背包dao
 * @author ken
 * @date 2017-1-4
 */
public class PlayerBagDAO extends GameSqlSessionTemplate {

	/**
	 * 创建背包物品
	 * */
	public void createPlayerBag(PlayerBag playerBag) {
		this.insert_noreturn(playerBag.getInsertSql());
		
	}
	
	/**
	 * 获得玩家背包物品列表
	 * */
	public List<PlayerBag> getPlayerBagListByPlayerID(long playerId) {
		String sql = "SELECT * FROM player_bag  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerBag.class);
	}
	
	/**
	 * 创建药品栏
	 */
	public void createPlayerDrug(PlayerDrug playerDrug){
		this.insert_noreturn(playerDrug.getInsertSql());		
	}
	
	/**
	 * 取玩家药品栏数据
	 */
	public List<PlayerDrug> listPlayerDrugs(long playerId){
		String sql = "SELECT * FROM player_drug  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerDrug.class);
	}
	
}
