package com.domain.bag;

import com.domain.GameEntity;

/**
 * 玩家背包
 * @author ken
 * @date 2017-1-4
 */
public class PlayerBag extends GameEntity {

	private static final long serialVersionUID = 1713162840873066017L;
	
	/** 玩家背包编号 */
	private long playerBagId;
	/** 玩家编号 */
	private long playerId;
	/** 物品索引 */
	private Integer itemIndex;	
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
	
	
	/** 临时数量变化(0： 无 1：变化) */
	private int numChanged;
	/** 临时排序物品编号 */
	private int sortItemId;

	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO player_bag ");
		sql.append("(playerBagId, playerId, goodsType, itemId, itemIndex, num, isBinding, state) VALUES");
		sql.append(" (");
		sql.append(playerBagId);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(goodsType);
		sql.append(",");
		sql.append(itemId);
		sql.append(",");
		sql.append(itemIndex);
		sql.append(",");
		sql.append(num);
		sql.append(",");
		sql.append(isBinding);
		sql.append(",");
		sql.append(state);
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 11);
		
		sql.append("UPDATE player_bag SET ");
		sql.append("itemId = ");
		sql.append(itemId);
		sql.append(",");
		sql.append("goodsType = ");
		sql.append(goodsType);
		sql.append(",");
		sql.append("itemIndex = ");
		sql.append(itemIndex);
		sql.append(",");
		sql.append("num = ");
		sql.append(num);
		sql.append(",");
		sql.append("isBinding = ");
		sql.append(isBinding);
		sql.append(",");
		sql.append("state = ");
		sql.append(state);
		sql.append(" WHERE playerBagId = ");
		sql.append(playerBagId);
		
		return sql.toString();
	}

	/**
	 * 背包格子空置
	 */
	public void reset(){
		goodsType = 0;
		itemId = 0;
		num = 0;
		state = 0;
		isBinding = 0;
	}
	

	public long getPlayerBagId() {
		return playerBagId;
	}

	public void setPlayerBagId(long playerBagId) {
		this.playerBagId = playerBagId;
	}

	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public Integer getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(Integer itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getIsBinding() {
		return isBinding;
	}

	public void setIsBinding(int isBinding) {
		this.isBinding = isBinding;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getNumChanged() {
		return numChanged;
	}

	public void setNumChanged(int numChanged) {
		this.numChanged = numChanged;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getSortItemId() {
		return sortItemId;
	}

	public void setSortItemId(int sortItemId) {
		this.sortItemId = sortItemId;
	}

}
