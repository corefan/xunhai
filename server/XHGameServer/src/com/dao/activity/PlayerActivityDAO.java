package com.dao.activity;

import com.db.GameSqlSessionTemplate;
import com.domain.activity.PlayerTomb;

/**
 * 玩家运营活动DAO
 * @author ken
 * @date 2017-10-25
 */
public class PlayerActivityDAO extends GameSqlSessionTemplate {

	/**
	 * 创建玩家陵墓
	 * */
	public void createPlayerTomb(PlayerTomb model) {
		this.insert_noreturn(model.getInsertSql());
		
	}
	
	/**
	 * 获取玩家陵墓数据
	 */
	public PlayerTomb getPlayerTomb(long playerId){
		String sql = "select * from player_tomb where playerId = "+playerId;
		return this.selectOne(sql, PlayerTomb.class);
	}
	
	/**
	 * 充值7天累计充值数据
	 */
	public void updateAllPlayerSevenPayData(){		
		String sql = "UPDATE player_wealth SET sevenPay = 0 WHERE sevenPay > 0";
		this.update(sql);	
		
		String sql1 = "update player_optional set spRewardIdStr = null";
		this.update(sql1);
	}
	

}
