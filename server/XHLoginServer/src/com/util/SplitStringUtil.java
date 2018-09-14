package com.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.MD5Service;

/**
 * 字符串切割帮助类
 * 
 * @author ken 2015-7-18
 * 
 */
public class SplitStringUtil {

	
	/**
	 * 分割int[]
	 */
	public static List<Integer> getIntList(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim())) {
			return null;
		}
		List<Integer> lists = new ArrayList<Integer>();
		try {
			string = string.substring(1, string.length() - 1);
			if(string.equals("")){
				return null;
			}
			String[] strArr = string.split(",");
			for(String value : strArr){				
				lists.add(Integer.valueOf(value.trim()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lists;
	}	
	
	/**
	 * 分割int[][]
	 */
	public static List<List<Integer>> getIntIntList(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 2) {
			return null;
		}
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		List<List<Integer>> lists = new ArrayList<List<Integer>>();
		try {
			for(String intList : strArr){
				List<Integer> lists2 = new ArrayList<Integer>();
				String[] strArr2 = intList.split(",");
				for(String value : strArr2){
					lists2.add(Integer.valueOf(value));
				}
				lists.add(lists2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lists;
	}
	
	/**
	 * 分割Map<Integer, Integer>
	 */
	public static Map<Integer, Integer> getIntIntMap(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 2) {
			return null;
		}
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		Map<Integer, Integer> map = new HashMap<Integer,Integer>();
		try {
			for(String intList : strArr){
				String[] strArr2 = intList.split(",");
				map.put(Integer.valueOf(strArr2[0]), Integer.valueOf(strArr2[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}

	/**
	 * 转换坐标  float[] to int[]
	 */
	public static List<Integer> getPosition(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim())) {
			return null;
		}
		List<Integer> lists = new ArrayList<Integer>();
		try {
			string = string.substring(1, string.length() - 1);
			String[] strArr = string.split(",");			
			for(String value : strArr){
				lists.add((int)(Float.valueOf(value) * 100));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lists;
	}
	
	/**
	 * 二维数组转String
	 */
    public static String getStringByIntIntList(int[][] list)
    {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.length; i++)
        {
        	sb.append(Arrays.toString(list[i]));
        }
        sb.append("]");
        return sb.toString();
    }

	
	public static void main(String[] args) {
		try {
			String str = MD5Service.encryptToUpperString("aaa" + 6036 + 1000 + 100 + 1 + "QYQDGAME6ANOKEYGa668ddddSHEN-2535-7DGAME-GGWIWI-loWgTw7ET2");
			System.out.println(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
