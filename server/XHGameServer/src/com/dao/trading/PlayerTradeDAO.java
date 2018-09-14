package com.dao.trading;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.trading.PlayerTradeBag;

public class PlayerTradeDAO extends GameSqlSessionTemplate{		
	
	/**
	 * 重置物品交易次数
	 */
	public void quartzDaily(){
		String sql = "update player_trade_item set buyItemNum = 0, sellItemNum = 0";
		this.update(sql);
	}
	
	
	/**
	 * 创建玩家交易背包
	 * */
	public void createPlayerTradeBag(PlayerTradeBag playerTradeBag) {		
		this.insert_noreturn(playerTradeBag.getInsertSql());
	}	
	
	/**
	 * 取玩家交易物品列表
	 */
	public List<PlayerTradeBag> listPlayerTradeBag(long playerId){
		String sql = "select * from player_trade_bag WHERE playerId="+ playerId;
		return this.selectList(sql, PlayerTradeBag.class);
	}
	
	/**
	 * 取玩家交易物品列表
	 */
	public List<PlayerTradeBag> listPlayerTradeBag(){
		String sql = "select * from player_trade_bag";
		return this.selectList(sql, PlayerTradeBag.class);
	}
	
	/**
	 * 调度清理已删除的任务
	 * */
	public void quartzDeleteTradeBag(long playerId) {
		this.delete("DELETE FROM player_trade_bag WHERE playerId="+ playerId);
	}
}
