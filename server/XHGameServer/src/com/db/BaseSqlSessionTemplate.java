package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.common.Config;
import com.util.LogUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 2013-11-7
 * 
 */
public class BaseSqlSessionTemplate extends SimpleSqlSessionTemplate {


	private static HikariDataSource ds = null;

	public BaseSqlSessionTemplate() {

	}

	public static HikariDataSource getInstance() {
		if (ds == null) {
			try {
				// 初始化连接池
				HikariConfig hikariConfig = new HikariConfig();
				hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
				hikariConfig.setPoolName("base");
				hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.DB_BASE);
				hikariConfig.setUsername(Config.DB_CONFIG_USER);
				hikariConfig.setPassword(Config.DB_CONFIG_PW);
				hikariConfig.setMaximumPoolSize(1);
				hikariConfig.setMaxLifetime(1800000);
				if (Config.DB_BASE == null) {
					ds = null;
					LogUtil.error("base db 初始化失败:");
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
		    LogUtil.error("异常:", e);
			getInstance().resumePool();
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
			    LogUtil.error("异常:", e);
			}
		}
	}

}
