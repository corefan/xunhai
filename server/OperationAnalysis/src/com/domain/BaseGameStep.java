package com.domain;

import java.io.Serializable;

/**
 * 基础任务表
 * @author ken
 * @date 2018年3月29日
 */
public class BaseGameStep implements Serializable {

	private static final long serialVersionUID = 746092399959434537L;

	private int id;
	
	private String taskName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}
