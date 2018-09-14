package cn.ibrother.tools.data.framework;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kingsoft.commons.database.SqlCommand;

/**
 * @desc 数据访问辅助类
 * @author ken
 * @version 1.0
 * @since 2010-03-10
 */

public class BaseDataProvider extends SqlCommand {
	private String SQL_GET_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";

	public BaseDataProvider() {
		super();
	}

	public BaseDataProvider(String proxoolUrl) {
		super(proxoolUrl);
	}

	public int getCountBySql(String sql) throws SQLException {
		return getCountBySql(sql, new String[0]);
	}

	public int getCountBySql(String sql, String[] paramValues)
			throws SQLException {
		int count = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(sql);

			for (int i = 0; i < paramValues.length; i++) {
				ps.setObject(i + 1, paramValues[i]);
			}

			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
		return count;
	}

	public int getSumBySql(String sql) throws SQLException {
		return getCountBySql(sql);
	}

	public boolean isClosed() throws SQLException {
		try {
			return getConnection().isClosed();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		}
	}

	/**
	 * 通过一个存储数据库字段名和对应值的Map来执行新增数据到数据库的操作
	 * 
	 * @param parameterValues
	 *            存储数据库字段名和对应值的Map
	 * @throws SQLException
	 *             如果数据库执行错误
	 * @throws IllegalArgumentException
	 *             如果 parameterValues 中不包含键值 __TableName
	 */
	public long insertTable(Map<String, Object> parameterValues)
			throws SQLException {
		long result = -1;
		if (parameterValues.get("__TableName") == null) {
			throw new IllegalArgumentException("__TableName 没有被赋值.");
		}

		if (parameterValues.size() == 1) {
			return result;
		}

		String insertSql = getInsertSql(parameterValues);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(insertSql);

			Set<String> keys = parameterValues.keySet();
			int count = 0;
			for (String key : keys) {
				Object value = parameterValues.get(key);

				if (key.equals("__TableName") || key.equals("__Key")
						|| key.equals("__Value")) {
					continue;
				}

				if (value instanceof EmptyObject) {
					ps.setObject(count + 1, null);
				} else {
					ps.setObject(count + 1, value);
				}

				count++;
			}
			ps.execute();

			// 取最后更新的id值
			ps = getConnection().prepareStatement(SQL_GET_LAST_INSERT_ID);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getLong(1);
			}
			return result;
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 通过一个存储数据库字段名和对应值的Map来执行更新数据到数据库的操作
	 * 
	 * @param parameterValues
	 *            存储数据库字段名和对应值的Map
	 * @throws SQLException
	 *             如果数据库执行错误
	 * @throws IllegalArgumentException
	 *             如果 parameterValues 中不包含键值 __TableName, __Key, __Value
	 */
	public void updateTable(Map<String, Object> parameterValues)
			throws SQLException {
		if (parameterValues.get("__TableName") == null) {
			throw new IllegalArgumentException("__TableName 没有被赋值.");
		}

		if (parameterValues.get("__Key") == null) {
			throw new IllegalArgumentException("__Key 没有被赋值.");
		}

		if (parameterValues.get("__Value") == null) {
			throw new IllegalArgumentException("__Value 没有被赋值.");
		}

		if (parameterValues.size() == 3) {
			return;
		}

		String updateSql = getUpdateSql(parameterValues);

		PreparedStatement ps = null;

		try {
			ps = getConnection().prepareStatement(updateSql);

			Set<String> keys = parameterValues.keySet();
			int count = 0;
			for (String key : keys) {
				Object value = parameterValues.get(key);

				if (key.equals("__TableName") || key.equals("__Key")
						|| key.equals("__Value")) {
					continue;
				}

				if (value instanceof EmptyObject) {
					ps.setObject(count + 1, null);
				} else {
					ps.setObject(count + 1, value);
				}

				count++;
			}

			ps.setObject(count + 1, parameterValues.get("__Value"));

			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public int runSql(String sql) throws SQLException {
		return runSql(sql, new String[0]);
	}

	public int runSql(String sql, String[] paramValues) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql);
			for (int i = 0; i < paramValues.length; i++) {
				ps.setObject(i + 1, paramValues[i]);
			}
			return ps.executeUpdate();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public List<Map<String, Object>> getListBySql(String sql)
			throws SQLException {
		return getListBySql(sql, new String[0]);
	}

	public List<Map<String, Object>> getListBySql(String sql,
			String[] paramValues) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql);
			for (int i = 0; i < paramValues.length; i++) {
				ps.setObject(i + 1, paramValues[i]);
			}
			return getMapListByResultSet(ps.executeQuery());
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public List<Map<String, Object>> getListBySql(String sql,
			List<String> paramValues) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(sql);
			int i = 0;
			for (String value : paramValues) {
				ps.setObject(++i, value);
			}
			return getMapListByResultSet(ps.executeQuery());
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public int runProcedureForQuery(String storeProcedure) throws SQLException {
		return runProcedureForQuery(storeProcedure, new String[0]);
	}

	private String getCallSql(String storeProcedure, String[] parameters) {
		if (parameters.length == 0) {
			return String.format("{call %s()}", storeProcedure);
		}

		StringBuffer callSql = new StringBuffer();
		StringBuffer valuesSql = new StringBuffer();

		for (int i = 0; i < parameters.length; i++) {
			valuesSql.append(",?");
		}
		valuesSql.deleteCharAt(0); // 去除第一个逗号

		callSql.append(String.format("{call %s(%s)}", storeProcedure,
				valuesSql.toString()));

		return callSql.toString();
	}

	public int runProcedureForQuery(String storeProcedure, String[] parameters)
			throws SQLException {
		String callSql = getCallSql(storeProcedure, parameters);

		ResultSet rs = null;
		CallableStatement call = null;
		try {
			call = getConnection().prepareCall(callSql);

			for (int i = 0; i < parameters.length; i++) {
				call.setObject(i + 1, parameters[i]);
			}

			rs = call.executeQuery();
			return rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (call != null) {
				call.close();
			}
		}
	}

	public void runProcedureNonQuery(String storeProcedure) throws SQLException {
		runProcedureNonQuery(storeProcedure, new String[0]);
	}

	public void runProcedureNonQuery(String storeProcedure, String[] parameters)
			throws SQLException {
		String callSql = getCallSql(storeProcedure, parameters);
		CallableStatement call = null;
		try {
			call = getConnection().prepareCall(callSql);
			for (int i = 0; i < parameters.length; i++) {
				call.setObject(i + 1, parameters[i]);
			}
			call.execute();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (call != null) {
				call.close();
			}
		}
	}

	public Map<String, Object> getOneBySql(String sql, String[] parameters)
			throws SQLException {
		List<Map<String, Object>> items = getListBySql(sql, parameters);
		return items.size() > 0 ? items.get(0) : null;
	}

	public Map<String, Object> getOneBySql(String sql) throws SQLException {
		List<Map<String, Object>> items = getListBySql(sql);
		return items.size() > 0 ? items.get(0) : null;
	}

	public Map<String, Object> getOneBySql(String sql, List<String> parameters)
			throws SQLException {
		List<Map<String, Object>> items = getListBySql(sql, parameters);
		return items.size() > 0 ? items.get(0) : null;
	}

	public List<Map<String, Object>> getListByProcedure(String storeProcedure,
			String parameters[]) throws SQLException {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		String callSql = getCallSql(storeProcedure, parameters);

		ResultSet rs = null;
		CallableStatement call = null;
		try {
			call = getConnection().prepareCall(callSql);
			for (int i = 0; i < parameters.length; i++) {
				call.setObject(i + 1, parameters[i]);
			}
			rs = call.executeQuery();
			if (rs != null) {
				mapList = getMapListByResultSet(rs);
			}
			return mapList;
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (call != null) {
				call.close();
			}
		}
	}

	/**
	 * 通过表名和条件查询出记录的总条数
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param condition
	 *            查询条件
	 * @return 总记录条数
	 * @throws SQLException
	 *             使用例子: getCountByTable("t_info_content", "info_id>1");
	 */
	public int getCountByTable(String tableName, String condition)
			throws SQLException {
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM `")
				.append(tableName).append("` ");

		if (!SqlUtils.isNullOrEmpty(condition)) {
			sql.append(" WHERE ").append(condition);
		}

		return getCountBySql(sql.toString());
	}

	/**
	 * 通过表名来删除表内的所有记录
	 * 
	 * @param tableName
	 *            数据库表名
	 * @return 删除的记录条数
	 * @throws SQLException
	 *             使用例子: deleteByTable("t_info_content");
	 */
	public int deleteByTable(String tableName) throws SQLException {
		return deleteByTable(tableName, null);
	}

	/**
	 * 通过表名和条件来删除记录
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param condition
	 *            删除条件
	 * @return 删除的记录条数
	 * @throws SQLException
	 *             使用例子: deleteByTable("t_info_content", "info_id=1");
	 */
	public int deleteByTable(String tableName, String condition)
			throws SQLException {
		if (tableName == null || tableName.length() <= 0) {
			return -1;
		}
		StringBuffer sql = new StringBuffer("DELETE FROM `").append(tableName).append("` ");

		if (!SqlUtils.isNullOrEmpty(condition)) {
			sql.append(" WHERE ").append(condition);
		}

		return runSql(sql.toString());
	}

	public Map<String, Object> getOneByTable(String tableName, String condition)
			throws SQLException {
		return getOneByTable(tableName, condition, new String[0]);
	}

	/**
	 * 通过表名和条件查询出一个对象
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param condition
	 *            查询条件
	 * @return 用字段名和值Map表示的对象
	 * @throws SQLException
	 *             if execute sql failed 使用例子: getOneFromTable("t_info_content",
	 *             "info_id=1");
	 */
	public Map<String, Object> getOneByTable(String tableName,
			String condition, String[] paramValues) throws SQLException {
		List<Map<String, Object>> items = getListByTable(tableName, condition,
				null, paramValues);
		return items.size() > 0 ? items.get(0) : null;
	}

	/**
	 * 通过表名和条件查询出所有符合条件的对象List
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param condition
	 *            查询条件 使用例子: getListFromTable("t_info_content_multi_user",
	 *            "info_id>10");
	 */
	public List<Map<String, Object>> getListByTable(String tableName,
			String condition) throws SQLException {
		return getListByTable(tableName, condition, null, new String[0]);
	}

	/**
	 * 通过表名、条件查询和排序方式查询出所有符合条件的对象List
	 * 
	 * @param tableName
	 *            数据库表名
	 * @param condition
	 *            查询条件
	 * @param orderBy
	 *            排序条件 使用例子: getListFromTable("t_info_content", "info_id>10",
	 *            "info_id ASC")
	 */
	public List<Map<String, Object>> getListByTable(String tableName,
			String condition, String orderBy, String[] paramValues)
			throws SQLException {
		String sql = getSelectSql(tableName, condition, orderBy);
		return getListBySql(sql, paramValues);
	}

	private String getInsertSql(Map<String, Object> parameterValues) {
		// 添加``符号,以防止字段名为关键字的问题
		StringBuffer insertSql = new StringBuffer();

		StringBuffer fieldsSql = new StringBuffer();
		StringBuffer valuesSql = new StringBuffer();

		insertSql.append("INSERT INTO `");
		insertSql.append(parameterValues.get("__TableName")).append("` ");

		insertSql.append(" ");

		Set<String> keys = parameterValues.keySet();
		for (String key : keys) {
			if (key.equals("__TableName") || key.equals("__Key")
					|| key.equals("__Value")) {
				continue;
			}

			fieldsSql.append(String.format(",`%s`", key));
			valuesSql.append(",?");
		}

		fieldsSql.deleteCharAt(0); // 去除第一个逗号
		valuesSql.deleteCharAt(0); // 去除第一个逗号

		insertSql.append(String.format("(%s) VALUES (%s)",
				fieldsSql.toString(), valuesSql.toString()));

		return insertSql.toString();
	}

	private String getUpdateSql(Map<String, Object> parameterValues) {
		StringBuffer updateSql = new StringBuffer();

		updateSql.append("UPDATE `");
		updateSql.append(parameterValues.get("__TableName")).append("` ");

		updateSql.append(" SET ");

		Set<String> keys = parameterValues.keySet();
		for (String key : keys) {
			if (key.equals("__TableName") || key.equals("__Key")
					|| key.equals("__Value")) {
				continue;
			}

			updateSql.append(String.format("`%s`=?", key));

			updateSql.append(",");
		}

		updateSql.deleteCharAt(updateSql.length() - 1); // 删除最后一个逗号
		updateSql.append(String.format(" WHERE %s=?",
				parameterValues.get("__Key")));

		return updateSql.toString();
	}

	private List<Map<String, Object>> getMapListByResultSet(ResultSet rs)
			throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ResultSetMetaData metaData = null;
		try {
			metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> parameterValues = new Hashtable<String, Object>();
				parameterValues.put("__TableName", metaData.getTableName(1));
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					/*
					 * // 是null值也添加,交由外部进行处理 by oyxz. if (rs.getObject(i) !=
					 * null) { parameterValues.put(metaData.getColumnName(i),
					 * rs.getObject(i)); } else {
					 * parameterValues.put(metaData.getColumnName(i), new
					 * EmptyObject()); }
					 */
					if (rs.getObject(i) != null) {
						parameterValues.put(metaData.getColumnName(i),
								rs.getObject(i));
					}
				}
				list.add(parameterValues);
			}
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {

		}
		return list;
	}

	private String getSelectSql(String tableName, String condition,
			String orderBy) {
		StringBuffer sql = new StringBuffer("SELECT * FROM ").append(tableName);

		if (!SqlUtils.isNullOrEmpty(condition)) {
			sql.append(" WHERE ").append(condition);
		}

		if (!SqlUtils.isNullOrEmpty(orderBy)) {
			sql.append(" ORDER BY ").append(orderBy);
		}

		return sql.toString();
	}

	public void executeUpdateBatch(List<Map<String, Object>> updateListInfos)
			throws SQLException {
		if (updateListInfos == null || updateListInfos.size() == 0) {
			return;
		}
		String updateSql = getUpdateSql(updateListInfos.get(0));
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(updateSql);
			for (Map<String, Object> updateInfo : updateListInfos) {
				Set<String> keys = updateInfo.keySet();
				int count = 0;
				for (String key : keys) {
					if (key.equals("__TableName") || key.equals("__Key")
							|| key.equals("__Value")) {
						continue;
					}
					Object value = updateInfo.get(key);
					if (value instanceof EmptyObject) {
						ps.setObject(count + 1, null);
					} else {
						ps.setObject(count + 1, value);
					}
					count++;
				}
				ps.setObject(count + 1, updateInfo.get("__Value"));
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public void executeInsertBatch(List<Map<String, Object>> insertListInfos)
			throws SQLException {
		if (insertListInfos == null || insertListInfos.size() == 0) {
			return;
		}
		String insertSql = getInsertSql(insertListInfos.get(0));
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(insertSql);
			for (Map<String, Object> insertInfo : insertListInfos) {
				Set<String> keys = insertInfo.keySet();
				int count = 0;
				for (String key : keys) {
					if (key.equals("__TableName") || key.equals("__Key")
							|| key.equals("__Value")) {
						continue;
					}
					Object value = insertInfo.get(key);
					if (value instanceof EmptyObject) {
						ps.setObject(count + 1, null);
					} else {
						ps.setObject(count + 1, value);
					}
					count++;
				}
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public boolean isRecordExisted(String tableName, String key, String keyValue)
			throws SQLException {
		String condition = String.format("%s=? ", key);
		try {
			String[] values = { keyValue };
			Map<String, Object> rsMap = getOneByTable(tableName, condition,
					values);
			return rsMap != null;
		} catch (SQLException ex) {
			throw new SQLException(ex);
		}
	}

	public int getGeneratedKeys() throws SQLException {
		int autoIncKeyFromApi = -1;
		ResultSet rs;
		try {
			rs = prepStmt.getGeneratedKeys();
			if (rs.next()) {
				autoIncKeyFromApi = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new SQLException();
		}
		return autoIncKeyFromApi;
	}

	private String getDeleteSql(Map<String, Object> parameterValues) {
		StringBuffer deleteSql = new StringBuffer();
		deleteSql.append("DELETE FROM `");
		deleteSql.append(parameterValues.get("__TableName"));
		deleteSql.append("` ");
		deleteSql.append(String.format(" WHERE `%s`=? ",
				parameterValues.get("__Key")));
		return deleteSql.toString();
	}

	public void executeDeleteBatch(List<Map<String, Object>> deleteListInfos)
			throws SQLException {
		if (deleteListInfos == null || deleteListInfos.size() == 0) {
			return;
		}
		String deleteSql = getDeleteSql(deleteListInfos.get(0));
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(deleteSql);
			for (Map<String, Object> deleteInfo : deleteListInfos) {
				ps.setObject(1, deleteInfo.get("__Value"));

				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public List<String> getTables() throws SQLException {
		List<String> results = new ArrayList<String>();

		String sSql = " show tables ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(sSql);

			rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					results.add(rs.getString(1));
				}
			}
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		return results;
	}

	public List<String> getFieldnames(String tablename) throws SQLException {
		List<String> results = new ArrayList<String>();

		String sSql = String.format(" describe `%s` ", tablename);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(sSql);

			rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					results.add(rs.getString(1));
				}
			}
		} catch (SQLException ex) {
			throw new SQLException(ex);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		return results;
	}
}