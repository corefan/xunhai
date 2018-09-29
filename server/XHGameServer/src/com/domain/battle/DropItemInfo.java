package com.domain.battle;

import java.io.Serializable;
import java.util.List;

/**
 * 掉落物品信息
 * @author ken
 * @date 2017-1-13
 */
public class DropItemInfo implements Serializable {

	private static final long serialVersionUID = 818226582201494273L;

	private Object pickLock = new Object();
	
	/** 掉落唯一编号*/
	private Integer dropId;
	/** 掉落者*/
	private String targetGuid;
	/** 物品编号*/
	private int itemId;
	/** 掉落数量*/
	private int num;
	/** 是否绑定*/
	private int blind;
	/** 资源类型  @RewardTypeConstant */
	private int goodsType;
	/** 掉落位置*/
	private int x;
	private int y;
	private int z;
	
	/** 所处区域格*/
	private int gridId;
	/** 掉落时间*/
	private long dropTime;
	/** 状态 @BattleConstant*/
	private int state;
	/** 归属*/
	private List<Long> belongPlayerIds;
	/** 装备唯一编号*/
	private long playerEquipmentId;
	
	public Integer getDropId() {
		return dropId;
	}
	public void setDropId(Integer dropId) {
		this.dropId = dropId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public long getDropTime() {
		return dropTime;
	}
	public void setDropTime(long dropTime) {
		this.dropTime = dropTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public String getTargetGuid() {
		return targetGuid;
	}
	public void setTargetGuid(String targetGuid) {
		this.targetGuid = targetGuid;
	}
	public int getGridId() {
		return gridId;
	}
	public void setGridId(int gridId) {
		this.gridId = gridId;
	}
	public List<Long> getBelongPlayerIds() {
		return belongPlayerIds;
	}
	public void setBelongPlayerIds(List<Long> belongPlayerIds) {
		this.belongPlayerIds = belongPlayerIds;
	}
	public long getPlayerEquipmentId() {
		return playerEquipmentId;
	}
	public void setPlayerEquipmentId(long playerEquipmentId) {
		this.playerEquipmentId = playerEquipmentId;
	}
	public Object getPickLock() {
		return pickLock;
	}
	public void setPickLock(Object pickLock) {
		this.pickLock = pickLock;
	}
	public int getBlind() {
		return blind;
	}
	public void setBlind(int blind) {
		this.blind = blind;
	}
	
}
