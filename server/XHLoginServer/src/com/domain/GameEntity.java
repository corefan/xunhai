package com.domain;

import java.io.Serializable;

/**
 * 同步入库的实体类
 * @author ken
 * @date 2018年4月24日
 */
public abstract class GameEntity implements Serializable {

	private static final long serialVersionUID = -7563503578546594971L;

	public abstract String getInsertSql();
	
	public abstract String getUpdateSql();
}
