package com.domain;

import java.io.Serializable;

/**
 * 位置
 * @author ken
 * @date 2016-12-28
 */
public class Position implements Serializable{
	private static final long serialVersionUID = -2407967025810208768L;
	
	private int x; 
	private int y;  //高度
	private int z;
	
	public Position() {
		
	}
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	
}
