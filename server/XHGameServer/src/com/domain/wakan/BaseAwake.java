package com.domain.wakan;

import java.util.List;

/**
 * 觉醒配置表
 * @author jiangqin
 * @date 2017-2-21
 */
public class BaseAwake {
	private int id; //觉醒配置编号
	private int needLevel; //需求等级
	private String attAwaken; //觉醒属性
	private List<List<Integer>> awakenPropertyList; //觉醒属性列表
	
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	public String getAttAwaken() {
		return attAwaken;
	}
	public void setAttAwaken(String attAwaken) {
		this.attAwaken = attAwaken;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<List<Integer>> getAwakenPropertyList() {
		return awakenPropertyList;
	}
	public void setAwakenPropertyList(List<List<Integer>> awakenPropertyList) {
		this.awakenPropertyList = awakenPropertyList;
	}
}
