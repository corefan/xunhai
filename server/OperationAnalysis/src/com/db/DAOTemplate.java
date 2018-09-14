package com.db;

import javax.sql.DataSource;

import com.db.SimpleJdbcTemplate;

public abstract class DAOTemplate {
	/**
	 * 数据库操作模版
	 */
	protected SimpleJdbcTemplate jdbcTempldate;

	/**
	 * 设置数据源
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTempldate = new SimpleJdbcTemplate(dataSource);
	}
	
	protected String formatAgent(String agent) {
		return agent.split(",").toString();
	}
}
