package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.common.Config;
import com.util.LogUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 
 * @author ken
 * @date 2018年3月19日
 */
public class BaseSqlSessionTemplate extends SimpleSqlSessionTemplate {


	private static HikariDataSource ds = null;
	
	public BaseSqlSessionTemplate() {

	}

	private static HikariDataSource getInstance() {
		if (ds == null) {
			try {
				// 初始化连接池
				HikariConfig hikariConfig = new HikariConfig();
				hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
				hikariConfig.setPoolName("base");
				hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.DBBASE);
				hikariConfig.setUsername(Config.DBBASEUser);
				hikariConfig.setPassword(Config.DBBASEPW);
				hikariConfig.setMaximumPoolSize(10);
				hikariConfig.setMaxLifetime(1800000);
				if (Config.DBBASE == null) {
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
