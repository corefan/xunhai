package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;


import com.util.GameSqlUtil;
import com.util.LogUtil;

@SuppressWarnings("unchecked")
public abstract class SimpleSqlSessionTemplate {
	
	public final static String EXCUTOR_UPDATE = "update";
	public final static String EXCUTOR_DELETE = "delete";
	
	/** 批量更新,删除大小 */
	private final static int BATCH_UPDATE_SIZE = 1000;
	/** 批量插入大小 */
	private final static int BATCH_INSERT_SIZE = 2000;

	public abstract Connection getConnection();
	
	public abstract void freeConnection(Connection conn);

	/**
	 * 创建，返回自增主键
	 * */
	public Long insert(String sql) {

		Connection conn = getConnection();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Long generatedKey = null;

		try {

			preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				generatedKey = (Long) resultSet.getObject(1);
			}

		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				preparedStatement.close();
			} catch (SQLException e) {
				LogUtil.error(e);
			}
			freeConnection(conn);
		}

		return generatedKey;
	}
	
	/**
	 * 创建-有事务
	 * */
	public Long insert(String sql, Connection conn) {

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Long generatedKey = null;

		try {

			preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet.next()) {
				generatedKey = (Long) resultSet.getObject(1);
			}

		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				preparedStatement.close();
			} catch (SQLException e) {
				LogUtil.error(e);
			}
		}

		return generatedKey;
	}
	
	/**
	 * 删除
	 * */
	public void delete(String sql) {
		update(sql);
	}
	
	/**
	 * 更新
	 * */
	public Integer update(String sql) {

		Connection conn = getConnection();
		
		Integer result = null;
		try {
			QueryRunner qr = new QueryRunner();
			result = qr.update(conn, sql);
			
		} catch (SQLException e) {
			result = null;
			LogUtil.error(e);
		} finally {
			freeConnection(conn);
		}
		return result;
	}
	
	public void batchUpdate(Object obj1, Set<?> paramObjectList, int batchSize) {

		Connection conn = getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();

			int n = 0;
			for (Iterator<?> iterator = paramObjectList.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				String batchSql = GameSqlUtil.beanToUpdateSql(object);
				if (batchSql != null) {
					statement.addBatch(batchSql);
				}
				
				n++;
				if (n % batchSize == 0 || n == paramObjectList.size()) {
					statement.executeBatch();
				}
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				LogUtil.error(e);
			}
			freeConnection(conn);
		}
	}
	
	/**
	 * 批处理删除
	 * */
	public void batchDelete(Object obj1, Set<?> paramObjectList, int batchSize) {
		
		Connection conn = getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();

			int n = 0;
			for (Iterator<?> iterator = paramObjectList.iterator(); iterator.hasNext();) {
				//Object object = iterator.next();
				//String batchSql = GameSqlUtil.beanToDeleteSql(object);
				String batchSql = "xxx";
				if (batchSql != null) {
					statement.addBatch(batchSql);
				}
				
				n++;
				if (n % batchSize == 0 || n == paramObjectList.size()) {
					statement.executeBatch();
				}
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				LogUtil.error(e);
			}
			freeConnection(conn);
		}
	}
	
	/**
	 * 批量处理
	 * */
	public void batchExcute(Set<?> paramObjectList, String executor) {
		if (EXCUTOR_DELETE.equals(executor)) {
			if (paramObjectList.iterator().hasNext()) {
				Object obj = paramObjectList.iterator().next();
				this.batchDelete(obj, paramObjectList, BATCH_UPDATE_SIZE);
			}
		} else {
			if (paramObjectList.iterator().hasNext()) {
				Object obj = paramObjectList.iterator().next();
				this.batchUpdate(obj, paramObjectList, BATCH_UPDATE_SIZE);
			}
		}
	}
	
	/**
	 * 日志库-批量插入
	 * */
	public void batchInsert_log(List<?> paramObjectList) {
	
		Connection conn = getConnection();
		try {

			int totalTimes = paramObjectList.size() / BATCH_INSERT_SIZE;
			if (totalTimes > 0) {
				for (int i = 0; i < totalTimes; i++) {
					//StringBuilder startSql = GameSqlUtil.getBatchInsertSql(paramObjectList, BATCH_INSERT_SIZE * i, BATCH_INSERT_SIZE * (i + 1));
					String startSql = "xxx";
					PreparedStatement ps = conn.prepareStatement(startSql.toString());
					ps.execute();
				}
			}
			
			if (paramObjectList.size() > BATCH_INSERT_SIZE * totalTimes) {
				String startSql = "xxx";
				//StringBuilder startSql = GameSqlUtil.getBatchInsertSql(paramObjectList, BATCH_INSERT_SIZE * totalTimes, paramObjectList.size());
				PreparedStatement ps = conn.prepareStatement(startSql.toString());
				ps.execute();
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			freeConnection(conn);
		}
	}
	
	/**
	 * 批量插入
	 * */
	public void batchInsert(List<?> paramObjectList) {
		
		Connection conn = getConnection();
		Statement statement = null;
		try {
			statement = conn.createStatement();

			int n = 0;

			for (Iterator<?> iterator = paramObjectList.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				String insertSql = GameSqlUtil.beanToInsertSql(object);
				if (insertSql != null) {
					statement.addBatch(insertSql);
				}

				n++;
				if (n % BATCH_INSERT_SIZE == 0 || n == paramObjectList.size()) {
					statement.executeBatch();
				}
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				LogUtil.error(e);
			}
			freeConnection(conn);
		}
	}
	
	/**
	 * 查询:有反射
	 * 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * */
	 
	public <T> T selectOne(String sql, Class<T> clazz) {
		
		Connection conn = getConnection();
		T results = null;
		try {
			
			QueryRunner qr = new QueryRunner();
			
			if (clazz == null) {
				results = (T) qr.query(conn, sql, new MapHandler());
			} else {
				ResultSetHandler<T> rsh = new BeanHandler<T>(clazz);
				results = qr.query(conn, sql, rsh);
			}
			
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			freeConnection(conn);
		}
		
		return results;
	}
	
	/**
	 * 查询:无反射
	 * 注：返回map
	 * */
	 
	public <T> T selectOne(String sql) {
		
		Connection conn = getConnection();
		T results = null;
		try {
			QueryRunner qr = new QueryRunner();
			ResultSetHandler<T> rsh = (ResultSetHandler<T>) new MapHandler();
			results = qr.query(conn, sql, rsh);
			
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			freeConnection(conn);
		}
		
		return results;
	}
	
	/**
	 * 查询:有反射
	 * 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * */
	public <T> List<T> selectList(String sql, Class<T> clazz) {
		
		Connection conn = getConnection();
		List<T> results = null;
		try {
			
			QueryRunner qr = new QueryRunner();
			if (clazz == null) {
				results = qr.query(conn, sql, new ColumnListHandler<T>());
			} else {
				ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
				results = qr.query(conn, sql, rsh);
			}
			
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			freeConnection(conn);
		}
		
		return results;
	}
	
	/**
	 * 查询:无反射
	 * 注:返回List<Map<String,Object>>
	 * */
	 
	public <T> List<T> selectList(String sql) {
		
		Connection conn = getConnection();
		List<T> results = null;
		
		try {
			QueryRunner qr = new QueryRunner();
			results = (List<T>) qr.query(conn, sql, new MapListHandler());
			
		} catch (SQLException e) {
			LogUtil.error(e);
		} finally {
			freeConnection(conn);
		}
		
		return results;
	}
	
}
