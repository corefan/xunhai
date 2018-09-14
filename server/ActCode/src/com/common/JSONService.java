package com.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON服务
 *
 */
public class JSONService {

	/**
	 * 将JSON列表转换为字符串
	 * @param jsonList
	 * @return
	 */
	public static String JSONListToString(List<JSONObject> jsonList) {
		if (jsonList==null) {
			return "[]";
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[");
		for (JSONObject json:jsonList) {
			stringBuffer.append(json.toString());
			stringBuffer.append(",");
		}
		if (stringBuffer.length()!=1) {
			stringBuffer.deleteCharAt(stringBuffer.length()-1);
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	/**
	 * 将JSON列表转换为字符串(支持num个字符)
	 * @param jsonList
	 * @return
	 */
	public static String JSONListToString(List<JSONObject> jsonList, int num) {
		if (jsonList==null) {
			return "[]";
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[");
		for (JSONObject json:jsonList) {
			if (stringBuffer.length() + json.toString().length() >= num)
				break;
			stringBuffer.append(json.toString());
			stringBuffer.append(",");
		}
		if (stringBuffer.length()!=1) {
			stringBuffer.deleteCharAt(stringBuffer.length()-1);
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	/**
	 * 将字符串转化为JSON列表
	 * @param jsonString 格式：[{"id":1,num:3},{"id":2,num:4}]
	 * @return
	 */
	public static List<JSONObject> stringToJSONList(String jsonListString) {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			if (jsonListString.length()>2) {
				String jsonListStringTmp = jsonListString.substring(0,jsonListString.length()-1);
				String[] jsonStrings = jsonListStringTmp.split("}");
				for (String jsonString : jsonStrings) {
					JSONObject jsonObject = new JSONObject(jsonString.substring(1)+"}");
					jsonList.add(jsonObject);
				}
			}
		} catch (JSONException e) {
			return new ArrayList<JSONObject>();
		}
		return jsonList;
	}

	/**
	 * JSONObject转换成HashMap
	 * */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> JSONObjectToMap(JSONObject object) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			Iterator<String> keys = object.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				map.put(key, object.get(key));
			}
		} catch (JSONException je) {
			return map;
		}

		return map;
	}

	/**
	 * 判断jsonObject里面是否存在key 没有放入0
	 * */
	public static int getInt(JSONObject jsonObject, String key) {

		try {
			if (jsonObject.has(key)) {
				return jsonObject.getInt(key);
			} else {
				jsonObject.put(key, 0);
			}
		} catch (JSONException je) {

		}

		return 0;
	}

}
