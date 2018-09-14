package com.domain.map;

import java.io.Serializable;

/**
 * 传送点
 * @author ken
 * @date 2017-2-6
 */
public class Transfer implements Serializable {

	private static final long serialVersionUID = 9164345824667833868L;

	/** 传送点编号*/
	private int id;
	/** 目标地图*/
	private int toMapId;
	/** 目标位置*/
	private int toX;
	private int toY;
	private int toZ;
	
	
	public Transfer() {
	
	}
	public Transfer(int id, int toMapId, int toX, int toY, int toZ) {
		this.id = id;
		this.toMapId = toMapId;
		this.toX = toX;
		this.toY = toY;
		this.toZ = toZ;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getToMapId() {
		return toMapId;
	}
	public void setToMapId(int toMapId) {
		this.toMapId = toMapId;
	}
	public int getToX() {
		return toX;
	}
	public void setToX(int toX) {
		this.toX = toX;
	}
	public int getToY() {
		return toY;
	}
	public void setToY(int toY) {
		this.toY = toY;
	}
	public int getToZ() {
		return toZ;
	}
	public void setToZ(int toZ) {
		this.toZ = toZ;
	}
}
