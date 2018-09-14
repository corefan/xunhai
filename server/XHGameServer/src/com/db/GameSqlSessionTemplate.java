package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.common.Config;
import com.util.LogUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class GameSqlSessionTemplate extends SimpleSqlSessionTemplate {

	private static HikariDataSource ds = null;

	public GameSqlSessionTemplate() {
		
	}
	
	public static HikariDataSource getInstance() {
		if (ds == null) {
			try {
				// 初始化连接池
				HikariConfig hikariConfig = new HikariConfig();
				hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
				hikariConfig.setPoolName("game");
				hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.DB_GAME);
				hikariConfig.setUsername(Config.DB_USER);
				hikariConfig.setPassword(Config.DB_PW);
				hikariConfig.setMaximumPoolSize(10);
				hikariConfig.setMaxLifetime(1800000);
				if (Config.DB_GAME == null) {
					ds = null;
					LogUtil.error("game db 初始化失败:");
					System.exit(-1);
				}
				ds = new HikariDataSource(hikariConfig);
			} catch (Exception e) {
				ds = null;
			    LogUtil.error("异常:", e);
			} 
		}
		
		return ds;
	}
	/**
	 * 获得连接
	 * */
	@Override
	public Connection getConnection() {
		
		Connection conn = null;
		try {
			conn = getInstance().getConnection();
		} catch (SQLException e) {
			LogUtil.error(e);
		}
		
		return conn;
	}
	
	/**
	 * 释放连接
	 * */
	@Override
	public void freeConnection(Connection conn) {
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LogUtil.error(e);
			}
		}
	}

}
