package com.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.microsoft.sqlserver.jdbc.SQLServerResultSetMetaData;
import com.util.GameSqlUtil;
import com.util.LogUtil;

public class SimpleJdbcTemplate extends JdbcTemplate {
	private final BasicRowProcessor convert = new BasicRowProcessor();
	private DataSource dataSource;

	public final static String EXCUTOR_UPDATE = "update";
	public final static String EXCUTOR_DELETE = "delete";

	/** 批量更新,删除大小 */
	private final static int BATCH_UPDATE_SIZE = 1000;

	/** 批量插入大小 */
	private final static int BATCH_INSERT_SIZE = 2000;

	public SimpleJdbcTemplate(DataSource datasource) {
		super(datasource);
		this.dataSource = datasource;
	}

	public <T> T queryForBean(String sql, final Class<T> beanType) {
		return query(sql, new ResultSetExtractor<T>() {

			@Override
			public T extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? convert.toBean(rs, beanType) : null;
			}
		});
	}

	public <T> T queryForBean(String sql, final Class<T> beanType, Object... args) {
		return query(sql, args, new ResultSetExtractor<T>() {

			@Override
			public T extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? convert.toBean(rs, beanType) : null;
			}
		});
	}

	public <T> List<T> queryForBeanList(String sql, final Class<T> beanType) {
		return query(sql, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return convert.toBean(rs, beanType);
			}
		});
	}

	public <T> List<T> queryForBeanList(String sql, final Class<T> beanType, Object... args) {
		return query(sql, args, new RowMapper<T>() {

			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				return convert.toBean(rs, beanType);
			}
		});
	}

	/**
	 * 创建，返回自增主键
	 * */
	public Long insert(String sql) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Long generatedKey = null;

		try {
			preparedStatement = dataSource.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
	 * 带参数执行函数
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object executeProc(final String proc, final List<Object> params) {
		return execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement statement = con.prepareCall(proc);
				if (params != null) {
					for (int i = 0; i < params.size(); i++) {
						statement.setObject(i + 1, params.get(i));
					}
				}
				return statement;
			}
		}, new CallableStatementCallback() {
			@Override
			public Object doInCallableStatement(CallableStatement stmt) throws SQLException, DataAccessException {
				stmt.execute();
				ResultSet rs = stmt.getResultSet();
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				if (rs == null) return rows;
				SQLServerResultSetMetaData rsMeta = (SQLServerResultSetMetaData) rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> row = new HashMap<String, Object>();
					for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
						String columName = rsMeta.getColumnLabel(i + 1);
						Object value = rs.getObject(i + 1);
						row.put(columName, value);
					}
					rows.add(row);
				}
				return rows;
			}
		});
	}
	
	/**
	 * 查询:有反射
	 * 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * */

	@SuppressWarnings("unchecked")
	public <T> T selectOne(String sql, Class<T> clazz) {
		T results = null;
		try {
			QueryRunner qr = new QueryRunner(dataSource);

			if (clazz == null) {
				results = (T) qr.query(sql, new MapHandler());
			} else {
				ResultSetHandler<T> rsh = new BeanHandler<T>(clazz);
				results = qr.query(sql, rsh);
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		}
		return results;
	}

	/**
	 * 查询:无反射
	 * 注：返回map
	 * */

	@SuppressWarnings("unchecked")
	public <T> T selectOne(String sql) {
		T results = null;
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<T> rsh = (ResultSetHandler<T>) new MapHandler();
			results = qr.query(sql, rsh);
		} catch (SQLException e) {
			LogUtil.error(e);
		}
		return results;
	}

	/**
	 * 查询:有反射
	 * 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * */
	public <T> List<T> selectList(String sql, Class<T> clazz) {
		List<T> results = null;
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			if (clazz == null) {
				results = qr.query(sql, new ColumnListHandler<T>());
			} else {
				ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
				results = qr.query(sql, rsh);
			}
		} catch (SQLException e) {
			LogUtil.error(e);
		}
		return results;
	}

	/**
	 * 查询:无反射
	 * 注:返回List<Map<String,Object>>
	 * */
	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(String sql) {
		List<T> results = null;
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			results = (List<T>) qr.query(sql, new MapListHandler());

		} catch (SQLException e) {
			LogUtil.error(e);
		}
		return results;
	}

	public void batchUpdate(Object obj1, Set<?> paramObjectList, int batchSize) {
		Statement statement = null;
		try {
			statement = dataSource.getConnection().createStatement();

			int n = 0;
			for (Iterator<?> iterator = paramObjectList.iterator(); iterator.hasNext();) {
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
		}
	}

	/**
	 * 批处理删除
	 * */
	public void batchDelete(Object obj1, Set<?> paramObjectList, int batchSize) {
		Statement statement = null;
		try {
			statement = dataSource.getConnection().createStatement();

			int n = 0;
			for (Iterator<?> iterator = paramObjectList.iterator(); iterator.hasNext();) {
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
	 * 批量插入
	 * */
	public void batchInsert(List<?> paramObjectList) {
		Statement statement = null;
		try {
			statement = dataSource.getConnection().createStatement();
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
		}
	}
}