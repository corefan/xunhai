package com.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import com.common.Config;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.util.LogUtil;

public class SqlSessionTemplate extends SimpleSqlSessionTemplate {

	private static BoneCP boneCP = null;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LogUtil.error(e);
		}
	}

	public SqlSessionTemplate() {
		
	}

	public static BoneCP getInstance() {
		if (boneCP == null) {
			try {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/bonecp-config.xml");
				// 初始化连接池
				BoneCPConfig boneConf = new BoneCPConfig(is,null);  
				
				boneConf.setJdbcUrl("jdbc:mysql://" + Config.DB_ACT_CODE);
				boneConf.setUsername(Config.DB_ACT_CODE_USER);
				boneConf.setPassword(Config.DB_ACT_CODE_PW);
				
				boneCP = new BoneCP(boneConf);
			} catch (Exception e) {
				LogUtil.error(e);
			} 
		}
		
		return boneCP;
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
