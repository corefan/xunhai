package com.dao.market;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.market.PlayerMarket;

/**
 * 玩家商城数据
 * @author jiangqin
 * @date 2017-4-18
 */
public class PlayerMarketDAO extends GameSqlSessionTemplate{
	
	/**
	 * 创建玩家商城数据
	 * */
	public void createPlayerMarket(PlayerMarket PlayerMarket) {
		this.insert_noreturn(PlayerMarket.getInsertSql());		
	}
	
	/** 根据编号取商城数据*/	 
	public List<PlayerMarket> getPlayerMarketByPlayerId(long playerId){
		String sql = "select * from player_market where playerId = "+playerId;
		return this.selectList(sql, PlayerMarket.class);
	}
	
	/** 根据编号取商城数据*/	 
	public List<PlayerMarket> getPlayerMarketList(){
		String sql = "select * from player_market";
		return this.selectList(sql, PlayerMarket.class);
	}
	
	/**
	 * 0点重置商城数据
	 */
	public void updateAllPlayerMarketData() {
		this.update(PlayerMarket.getUpdateAllSql());
	}
}
