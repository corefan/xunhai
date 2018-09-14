package com.domain.team;

import java.io.Serializable;

/**
 * 组队配置表
 * @author ken
 * @date 2017-3-2
 */
public class BaseTeam implements Serializable {

	private static final long serialVersionUID = -608595742596995561L;

	/** 组队活动编号*/
	private int id;
	/** 名称*/
	private String targetName;
	/** 根目录*/
	private int targetGroup;
	/** 子目录*/
	private int targetType;
	/** 限制等级*/
	private int minimumLevel;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTargetGroup() {
		return targetGroup;
	}
	public void setTargetGroup(int targetGroup) {
		this.targetGroup = targetGroup;
	}
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public int getMinimumLevel() {
		return minimumLevel;
	}
	public void setMinimumLevel(int minimumLevel) {
		this.minimumLevel = minimumLevel;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
}
