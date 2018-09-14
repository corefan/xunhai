package com.domain.monster;

import java.io.Serializable;

/**
 * 刷怪信息
 * @author ken
 * @date 2017-2-9
 */
public class RefreshMonsterInfo implements Serializable {

	private static final long serialVersionUID = 6320846119979520031L;

	/** 刷怪配置编号*/
	private int refMonsterId;
	/** 当前数量*/
	private int curNum;
	/** 刷怪时间戳*/
	private long refreshDate;
	
	/** 当前层数*/
	private int curLayerId;
	
	public int getRefMonsterId() {
		return refMonsterId;
	}
	public void setRefMonsterId(int refMonsterId) {
		this.refMonsterId = refMonsterId;
	}
	public int getCurNum() {
		return curNum;
	}
	public void setCurNum(int curNum) {
		this.curNum = curNum;
	}
	public long getRefreshDate() {
		return refreshDate;
	}
	public void setRefreshDate(long refreshDate) {
		this.refreshDate = refreshDate;
	}
	public int getCurLayerId() {
		return curLayerId;
	}
	public void setCurLayerId(int curLayerId) {
		this.curLayerId = curLayerId;
	}
	
}
