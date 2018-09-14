package com.domain.trading;


import com.domain.GameEntity;

public class PlayerTradeBag extends GameEntity {
	
	private static final long serialVersionUID = -4625420662415239106L;	
	
	/** 玩家背包编号 */
	private long playerTradeBagId;
	/** 玩家编号 */
	private long playerId;	
	/** 物品类别 */
	private int goodsType;
	/** 物品编号 */
	private long itemId;
	/** 数量 */
	private int num;
	/** 是否绑定(1:是 0：否) */
	private int isBinding;
	/** 状态(0: 空置  1:背包) */
	private int state;	
	/** 售价*/
	private int price;	
	/** 交易过期时间*/
	private long overTime;

	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO player_trade_bag ");
		sql.append("(playerTradeBagId, playerId, goodsType, itemId, num, isBinding, state, price, overTime) VALUES");
		sql.append(" (");
		sql.append(playerTradeBagId);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(goodsType);
		sql.append(",");
		sql.append(itemId);
		sql.append(",");		
		sql.append(num);
		sql.append(",");
		sql.append(isBinding);
		sql.append(",");
		sql.append(state);
		sql.append(",");
		sql.append(price);
		sql.append(",");
		sql.append(overTime);
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("UPDATE player_trade_bag SET ");
		sql.append("playerId = ");
		sql.append(playerId);
		sql.append(",");
		sql.append("itemId = ");
		sql.append(itemId);
		sql.append(",");
		sql.append("goodsType = ");
		sql.append(goodsType);
		sql.append(",");		
		sql.append("num = ");
		sql.append(num);
		sql.append(",");
		sql.append("isBinding = ");
		sql.append(isBinding);
		sql.append(",");
		sql.append("state = ");
		sql.append(state);
		sql.append(",");
		sql.append("overTime = ");
		sql.append(overTime);
		sql.append(",");
		sql.append("price = ");
		sql.append(price);
		sql.append(" WHERE playerTradeBagId = ");
		sql.append(playerTradeBagId);
		
		return sql.toString();
	}

	public int getGoodsType() {
		return goodsType;
	}
	public int getNum() {
		return num;
	}
	public int getIsBinding() {
		return isBinding;
	}
	public int getState() {
		return state;
	}
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public void setIsBinding(int isBinding) {
		this.isBinding = isBinding;
	}
	public void setState(int state) {
		this.state = state;
	}

	public long getPlayerTradeBagId() {
		return playerTradeBagId;
	}

	public void setPlayerTradeBagId(long playerTradeBagId) {
		this.playerTradeBagId = playerTradeBagId;
	}

	public long getOverTime() {
		return overTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	 * 交易背包格子空置
	 */
	public void reset(){
		goodsType = 0;
		itemId = 0;
		num = 0;
		state = 0;		
		playerId = 0;	
		isBinding = 0;
		price = 0;
	    overTime =0;
	}
}
