package com.domain.map;

import java.io.Serializable;

/**
 * 幻境开启与结束
 * @author ken
 * @date 2018年7月9日
 */
public class Huanjing implements Serializable {

	private static final long serialVersionUID = -1626387291499955697L;

	/** 状态   1：开启  0：结束*/
	private int state;
	
	/** 结束时间搓*/
	private long endTime;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}
