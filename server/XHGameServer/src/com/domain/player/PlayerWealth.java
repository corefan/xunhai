package com.domain.player;

import com.domain.GameEntity;

/**
 * 玩家财富
 * @author ken
 * @date 2016-12-27
 */
public class PlayerWealth extends GameEntity {

	private static final long serialVersionUID = 3651827425670322452L;
	
	/** 玩家编号 */
	private long playerId;	
	/** 玩家金币 */
	private int gold;
	/** 玩家宝玉 */
	private int stone;
	/** 充值钻石 */
	private int diamond;
	/** 累计充值金额 */
    private int totalPay;
    /** 累计消费金额 */
    private int totalSpend;
    /** 七天累计充值金额*/
    private int sevenPay;
    /** 玩家羽灵值*/
    private int wingValue;
    
	@Override
	public String getInsertSql() {
		return null;
	}
	
	/**
	 * 获得更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE player_wealth SET ");		
		sb.append(" gold = ");
		sb.append(gold);
		sb.append(",");
		sb.append(" stone = ");
		sb.append(stone);
		sb.append(",");
		sb.append(" diamond = ");
		sb.append(diamond);
		sb.append(",");
		sb.append(" totalPay = ");
		sb.append(totalPay);
		sb.append(",");
		sb.append(" totalSpend = ");
		sb.append(totalSpend);
		sb.append(",");
		sb.append(" sevenPay = ");
		sb.append(sevenPay);
		sb.append(",");
		sb.append(" wingValue = ");
		sb.append(wingValue);
		sb.append(" WHERE playerId = ");
		sb.append(playerId);		
		return sb.toString();
	}
	

	public int getSevenPay() {
		return sevenPay;
	}


	public void setSevenPay(int sevenPay) {
		this.sevenPay = sevenPay;
	}


	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		if (gold < 0) gold = 0;
		this.gold = gold;
	}

	public int getDiamond() {
		return diamond;
	}

	public void setDiamond(int diamond) {
		if (diamond < 0) diamond = 0;
		
		this.diamond = diamond;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		if (stone < 0) stone = 0;
		this.stone = stone;
	}

	public int getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(int totalPay) {
		this.totalPay = totalPay;
	}


	public int getTotalSpend() {
		return totalSpend;
	}

	public void setTotalSpend(int totalSpend) {
		this.totalSpend = totalSpend;
	}


	public int getWingValue() {
		return wingValue;
	}


	public void setWingValue(int wingValue) {	
		if (wingValue < 0) wingValue = 0;
		
		this.wingValue = wingValue;
	}

}
