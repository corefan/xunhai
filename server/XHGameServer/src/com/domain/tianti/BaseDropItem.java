/**
 * 
 */
package com.domain.tianti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.Reward;

/**
 * 竞技场掉落
 * @author jiangqin
 * @date 2017-11-14
 */
public class BaseDropItem implements Serializable{

	private static final long serialVersionUID = 2431710170522789921L;

	/** 掉落编号*/
	private	int	id;
	/** 地图ID*/
	private	int mapId;
	/** 掉落位置*/
	private String position;
	private List<Integer> posList;
	/** 刷新时间*/
	private	int  refreshTime;
	/** 掉落物品*/
	private String dropItem;
	private Map<Integer, List<Reward>> dropItemMap = new HashMap<Integer,List<Reward>>();
	
	public int getId() {
		return id;
	}
	public int getMapId() {
		return mapId;
	}
	public String getPosition() {
		return position;
	}
	public int getRefreshTime() {
		return refreshTime;
	}
	public String getDropItem() {
		return dropItem;
	}
	public Map<Integer, List<Reward>> getDropItemMap() {
		return dropItemMap;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}
	public void setDropItem(String dropItem) {
		this.dropItem = dropItem;
	}
	public void setDropItemMap(Map<Integer, List<Reward>> dropItemMap) {
		this.dropItemMap = dropItemMap;
	}
	public List<Integer> getPosList() {
		return posList;
	}
	public void setPosList(List<Integer> posList) {
		this.posList = posList;
	}
}
