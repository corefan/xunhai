package com.domain.tower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.domain.monster.BaseRefreshMonster;

/**
 * 大荒塔配置
 * @author ken
 * @date 2017-3-24
 */
public class BaseTower implements Serializable {

	private static final long serialVersionUID = 8014620729224179712L;

	/** 编号*/
	private int id;
	/** 地图编号*/
	private int mapId;
	/** 刷怪坐标*/
	private String refPoint;
	/** 波数*/
	private int num;
	/** 波数刷怪列表*/
	private String monList;
	private List<BaseRefreshMonster> refMonList = new ArrayList<BaseRefreshMonster>();
	/** 基础经验系数*/
	private int baseExp;
	/** 基础金币系数*/
	private int baseGold;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public String getRefPoint() {
		return refPoint;
	}
	public void setRefPoint(String refPoint) {
		this.refPoint = refPoint;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getMonList() {
		return monList;
	}
	public void setMonList(String monList) {
		this.monList = monList;
	}
	public int getBaseExp() {
		return baseExp;
	}
	public void setBaseExp(int baseExp) {
		this.baseExp = baseExp;
	}
	public int getBaseGold() {
		return baseGold;
	}
	public void setBaseGold(int baseGold) {
		this.baseGold = baseGold;
	}
	public List<BaseRefreshMonster> getRefMonList() {
		return refMonList;
	}
	public void setRefMonList(List<BaseRefreshMonster> refMonList) {
		this.refMonList = refMonList;
	}
	
}
