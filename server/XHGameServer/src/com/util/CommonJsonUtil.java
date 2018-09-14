package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.common.JSONService;

/***
 * 把数据库一些str转换成通用的 类型
 * @author Administrator
 *
 */
public class CommonJsonUtil {
	
	/**此方法适用于  [{id:number,num:number}]**/
	public static Map<Integer,Integer> itmeCostParse(String str){
		Map<Integer,Integer> costMap = new HashMap<Integer, Integer>();
		
		try{
			List<JSONObject> jsonList = JSONService.stringToJSONList(str);
			
			for(JSONObject obj : jsonList){
				costMap.put(obj.getInt("id"), obj.getInt("num"));
			}
		}catch(Exception e){
			LogUtil.error("itmeCostParse error========>"+str,e);
		}
		
		return costMap;
	}
	
	/**[{"key":string,"value":number}]**/
	public static Map<String, Integer> keyValueParse(String str){
		Map<String,Integer> costMap = new HashMap<String, Integer>();
		
		try{
			List<JSONObject> jsonList = JSONService.stringToJSONList(str);
			
			for(JSONObject obj : jsonList){
				costMap.put(obj.getString("key"), obj.getInt("value"));
			}
		}catch(Exception e){
			LogUtil.error("keyValueParse error========>"+str,e);
		}
		
		return costMap;
	}
	
	
	
	public static List<Integer> str2List(String str){
		if(str == null || "".equals(str.trim())){
			return new ArrayList<Integer>();
		}
		
		String temp[] = str.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for(String s:temp){
			list.add(Integer.parseInt(s));
		}
		
		return list;
		
	}
	/***
	 * [num,num1,num2] ===>"num,num1,num2"
	 * @param list
	 * @return
	 */
	public static String list2Str(List<Integer> list){
		if(list == null || list.size() <= 0){
			return "";
		}
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		for(Integer integer : list){
			if(flag){
				sb.append(",");
			}
			sb.append(integer);
			flag = true;
		}
		
		return sb.toString();
	}
	
	
	
	public static Map<Integer,Integer> str2IntMap(String str){
		if(str == null || "".equals(str.trim())){
			return new HashMap<Integer, Integer>();
		}
		
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		String[] groupStr = str.split(";");
		for(String tmp : groupStr){
			String[] keyValues = tmp.split(",");
			map.put(Integer.parseInt(keyValues[0]), Integer.parseInt(keyValues[1]));
		}
		
		return map;
	}
	
	public static String IntMap2Str(Map<Integer,Integer> map){
		if(map == null || map.size() <= 0 ){
			return "";
		}
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		for(Entry<Integer,Integer> entry : map.entrySet()){
			if(flag){
				sb.append(";");
			}
			
			sb.append(entry.getKey()+","+entry.getValue());
			flag=true;
		}
		
		return sb.toString();
	}
	
	public static final String NUM_CUT = "[,]";
	public static final String ITEMS_CUT = "[|]";
	public static final String MULTI_CUT = "[;]";
	
	/** val,val,val ; val,val,val ;*/
	public static Map<Integer, Map<Integer, Integer>> convertString2ItemNumMap(String info){
		Map<Integer, Map<Integer, Integer>> result = new HashMap<Integer, Map<Integer,Integer>>();
		if(info==null || info.equals("")){
			return result;
		}
		String[] one = info.split(MULTI_CUT); 
		for(String oneInfo : one){
			String[] two = oneInfo.split(NUM_CUT);//val,val,val
			Map<Integer, Integer> data = new HashMap<Integer, Integer>();
			result.put(Integer.parseInt(two[0]), data);
			data.put(Integer.parseInt(two[1]), Integer.parseInt(two[2]));
		}
		return result;
	}
	
	
	/**key,val;key,val;key,val;*/
	public static Map<Integer, Integer> convertString2IMap(String info){
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		if(info==null || info.equals("")){
			return result;
		}
		String[] infos = info.replace(" ", "").split(MULTI_CUT);
		for(int i=0;i<infos.length;i++){
			String[] items = infos[i].split(NUM_CUT);
			result.put(Integer.parseInt(items[0]),Integer.parseInt(items[1]));
		}
		return result;
	}
	
	/**key,val;key,val;key,val;*/
	public static Map<String, Integer> convertString2SMap(String info){
		Map<String, Integer> result = new HashMap<String, Integer>();
		if(info==null || info.equals("")){
			return result;
		}
		String[] infos = info.replace(" ", "").split(MULTI_CUT);
		for(int i=0;i<infos.length;i++){
			String[] items = infos[i].split(NUM_CUT);
			result.put(items[0],Integer.parseInt(items[1]));
		}
		return result;
	}
	
	/** info;info;info */
	public static String[] convertStr(String info){
		if(info==null || info.equals(""))
			return new String[]{};
		String[] value = info.replace(" ", "").split(MULTI_CUT);
		String[] result = new String[value.length];
		for(int i=0;i<result.length;i++){
			result[i] = value[i];
		}
		return result;
	}
	
	/** info;info;info */
	public static int[] convertNum(String info){
		if(info==null || info.equals(""))
			return new int[]{};
		String[] value = info.replace(" ", "").split(MULTI_CUT);
		int[] result = new int[value.length];
		for(int i=0;i<result.length;i++){
			result[i] = Integer.parseInt(value[i]);
		}
		return result;
	}
	
	/** id,num | id,num;id,num; ....... */
	public static Map<Integer, Integer>[] convertItemNum(String info){
		String[] infos = info.replace(" ", "").split(MULTI_CUT);
		
		@SuppressWarnings("unchecked")
		Map<Integer, Integer>[] result = new Map[infos.length];
		for(int i=0;i<infos.length;i++){
			String items = infos[i];
			String[] itemInfo = items.split(ITEMS_CUT);
			Map<Integer, Integer> value = new HashMap<Integer, Integer>(itemInfo.length);
			for(String item : itemInfo){
				String[] ite = item.split(NUM_CUT);
				value.put(Integer.parseInt(ite[0]),Integer.parseInt(ite[1]));
			}
			result[i] = value;
		}
		return result;
	}
	
	public static void main(String args[]){
	}
}
