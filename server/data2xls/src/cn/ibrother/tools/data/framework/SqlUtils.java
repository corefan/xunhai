package cn.ibrother.tools.data.framework;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @desc sql实用工具类
 * @author ken
 * @version 1.0
 * @since 2010-03-10
 */

public final class SqlUtils {

	private static List<String> getFieldNames(Map<String, Object> parameterValues) {
		List<String> fieldNames = new ArrayList<String>();
		
		for (String key : parameterValues.keySet()) {
			if(key.equals("__TableName") || key.equals("__Key") || key.equals("__Value")) {
				continue;
			}
				
			fieldNames.add(key);
		}
		
		return fieldNames;
	}
	
	/**
	 * 产生批量插入的Sql语句
	 */
	public static String generateBatchInsertSql(List<Map<String, Object>> insertItems) {
		StringBuffer sql = new StringBuffer();

		if (insertItems.size() == 0) {
			return "";
		}

		Map<String, Object> firstItem = insertItems.get(0);

		if (firstItem.get("__TableName") == null) {
			return "";
		}

		sql.append("INSERT INTO ");
		sql.append(firstItem.get("__TableName"));

		List<String> fields = getFieldNames(firstItem);

		if (fields.size() == 0) {
			return "";
		}

		sql.append(" (");
		for (int i = 0; i < fields.size(); i++) {
			sql.append(fields.get(i));
			if (i != fields.size() - 1) {
				sql.append(",");
			}
		}
		sql.append(" ) VALUES ");

		for (int i = 0; i < insertItems.size(); i++) {
			Map<String, Object> insertItem = (Map<String, Object>) insertItems.get(i);

			if (insertItem.size() == 2) {
				continue;
			}

			sql.append(" (");

			for (int j = 0; j < fields.size(); j++) {
				Object field = insertItem.get(fields.get(j));

				if (field == null) {
					sql.append("NULL");
				} else if (field.getClass().getName().equals("java.lang.Boolean")) {
					sql.append(field.toString());
				} else {
					sql.append("'" + field.toString() + "'");
				}

				if (j != fields.size() - 1) {
					sql.append(",");
				}
			}

			sql.append(" )");

			if (i != insertItems.size() - 1) {
				sql.append(",");
			}
		}

		sql.append(";");

		return sql.toString();
	}

	/**
	 * 从'源字段名和数值Map'中复制对象到'目标字段名和数值Map'
	 * @param sourceFieldValues 源字段名和数值Map
	 * @param targetFieldValues 目标字段名和数值Map
	 * @param fieldName 字段名
	 */
	public static void copyValueIfNotNull(Map<String, Object> sourceFieldValues, Map<String, Object> targetFieldValues, String fieldName) {
		Object sourceFieldValue = sourceFieldValues.get(fieldName);
		if(sourceFieldValue != null) {
			targetFieldValues.put(fieldName, sourceFieldValue);
		}
	}
	
	public static String getString(Map<String, Object> fieldValues, String fieldName, String defaultValue) {
		return new GenericGetter<String>().getObject(fieldValues, fieldName, defaultValue);
	}
	
	public static int getInt(Map<String, Object> fieldValues, String fieldName, int defaultValue) {
		return new GenericGetter<Integer>().getObject(fieldValues, fieldName, defaultValue);
	}
	
	public static long getLong(Map<String, Object> fieldValues, String fieldName, long defaultValue) {
		return new GenericGetter<Long>().getObject(fieldValues, fieldName, defaultValue);
	}
	
	public static Timestamp getTimestamp(Map<String, Object> fieldValues, String fieldName, Timestamp defaultValue) {
		return new GenericGetter<Timestamp>().getObject(fieldValues, fieldName, defaultValue);
	}
	
	public static Date getDate(Map<String, Object> fieldValues, String fieldName, Date defaultValue) {
		return new GenericGetter<Date>().getObject(fieldValues, fieldName, defaultValue);
	}
	
	public static Object getObject(Map<String, Object> fieldValues, String fieldName) {
		return new GenericGetter<Object>().getObject(fieldValues, fieldName, null);
	}
	
	public static BigDecimal getBigDecimal(Map<String, Object> fieldValues, String fieldName, BigDecimal defaultValue) {
		return new GenericGetter<BigDecimal>().getObject(fieldValues, fieldName, defaultValue);
	}
	
	private static class GenericGetter<T> {
		/**
		 * 从'字段名和数值Map'中获取指定字段名的数值的方法
		 * @param fieldValues 字段名和数值Map
		 * @param fieldName 字段名
		 * @param defaultValue 默认值
		 * @return fieldValues.get(fieldName) if fieldName exists in Map, otherwise defaultValue
		 */	
		@SuppressWarnings("unchecked")
		public T getObject(Map<String, Object> fieldValues, String fieldName, T defaultValue) {
			Object fieldValue = fieldValues.get(fieldName);
			return (fieldValue != null && !(fieldValue instanceof EmptyObject)) ? (T)fieldValue : defaultValue;
		}
	}
	
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.length() == 0;
	}	
}
