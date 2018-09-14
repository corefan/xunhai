/**
 * 
 */
package com.domain.market;


import com.domain.GameEntity;


/**
 * 玩家商城信息
 * @author jiangqin
 * @date 2017-4-18
 */
public class PlayerMarket extends GameEntity {
	
	private static final long serialVersionUID = 875504371937758030L;
	
	/** 商城交易唯一编号*/
	private long id;
	/** 玩家编号*/
	private long  playerId;
	/** 商城物品编号*/
	private int  marketId;
	/** 当前购买次数*/
	private int curBuyNum;
	
	public String getInsertSql() {	
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_market ");
		sql.append("(id, playerId, marketId, curBuyNum) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");	
		sql.append(playerId);
		sql.append(",");		
		sql.append(marketId);	
		sql.append(",");		
		sql.append(curBuyNum);		
		sql.append(")");
		
		return sql.toString();
	}


	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();		
		sql.append("UPDATE player_market SET ");
		sql.append("curBuyNum = ");		
		sql.append(curBuyNum);	
		
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}
	
	/**
	 * 0点重置
	 */
	public static String getUpdateAllSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_market SET ");
		sql.append(" curBuyNum=0");
	
		return sql.toString();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	
	public int getMarketId() {
		return marketId;
	}		
	public int getCurBuyNum() {
		return curBuyNum;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}
	public void setCurBuyNum(int curBuyNum) {
		this.curBuyNum = curBuyNum;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
}
