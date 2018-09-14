package com.domain.map;

import java.io.Serializable;

/**
 * 地图布置怪物
 * @author ken
 * @date 2017-1-10
 */
public class MonsterInfo implements Serializable {

	private static final long serialVersionUID = 5236864941736916596L;

	/** 怪物编号*/
	private int refreshMonsterId;
	/** 出生坐标*/
	private int x;
	/** 出生坐标*/
	private int y;
	/** 出生坐标*/
	private int z;
	
	private int direction;
	public MonsterInfo() {
		
	}
	
	public MonsterInfo(int refreshMonsterId, int x, int y, int z, int direction) {
		this.refreshMonsterId = refreshMonsterId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}
	

	public int getRefreshMonsterId() {
		return refreshMonsterId;
	}

	public void setRefreshMonsterId(int refreshMonsterId) {
		this.refreshMonsterId = refreshMonsterId;
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
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
