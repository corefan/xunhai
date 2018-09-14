package com.domain.guild;

import com.domain.GameEntity;

/**
 * 优惠购买记录
 * @author ken
 * @date 2018年8月2日
 */
public class GuildBuy extends GameEntity {

	private static final long serialVersionUID = 5318092683312349323L;

	/** 物品编号*/
	private int itemId;
	/** 今日被购数量*/
	private int buyNum;
	
	@Override
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO guild_buy ");
		sql.append("(itemId, buyNum) VALUES");
		sql.append(" (");
		sql.append(itemId);
		sql.append(",");
		sql.append(buyNum);
		sql.append(")");		
		return sql.toString();
	}

	@Override
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE guild_buy SET ");
		sql.append("buyNum = ");
		sql.append(buyNum);
		sql.append(" WHERE itemId = ");
		sql.append(itemId);
		
		return sql.toString();
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

}
