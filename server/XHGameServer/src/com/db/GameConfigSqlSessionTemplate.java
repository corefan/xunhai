package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.common.Config;
import com.util.LogUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class GameConfigSqlSessionTemplate extends SimpleSqlSessionTemplate {
	private static HikariDataSource ds = null;
	
	private static  String dbUrl = null;
	private static String dbUsername = null;
	private static String dbPassword = null;
	
	@Override
	public Connection getConnection() {
		Connection conn = null;
			try {
				conn = getInstance().getConnection();
			} catch (SQLException e) {
				LogUtil.error(e);
				getInstance().resumePool();
			}
		
		return conn;
	}
	
	/*特殊函数不通用,请勿使用*/
	public static void initBoneCPSpecial(String url,String username,String password){
		dbUrl = url;
		dbUsername = username;
		dbPassword = password;
	}
	
	private static HikariDataSource getInstance() {
		if (ds == null) {
			try {
				// 初始化连接池
				HikariConfig hikariConfig = new HikariConfig();
				hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
				hikariConfig.setPoolName("config");
				hikariConfig.setJdbcUrl("jdbc:mysql://" + (dbUrl == null ? dbUrl = Config.DB_GCC : dbUrl));
				hikariConfig.setUsername(dbUsername == null ? Config.DB_CONFIG_USER : dbUsername);
				hikariConfig.setPassword(dbPassword == null ? Config.DB_CONFIG_PW : dbPassword);
				hikariConfig.setMaximumPoolSize(10);
				hikariConfig.setMaxLifetime(1800000);
				if (dbUrl == null) {
					ds = null;
					LogUtil.error("gcc db 初始化失败:");
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
	
	public void shutdown(){
		if (ds != null) {
			ds.close();
			ds = null;
		}
	}
	
	@Override
	public void freeConnection(Connection conn) {
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		}

	}

}
