package com.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domain.Reward;
import com.domain.bag.AddAttr;
import com.domain.chat.Notice;
import com.domain.collect.CollectReward;

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
	 * 分割Map<Long, Integer>
	 */
	public static Map<Long, Integer> getLongIntMap(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 2) {
			return null;
		}
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		Map<Long, Integer> map = new HashMap<Long,Integer>();
		try {
			for(String intList : strArr){
				String[] strArr2 = intList.split(",");
				map.put(Long.valueOf(strArr2[0]), Integer.valueOf(strArr2[1]));
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
	 * 转换成掉落信息
	 */
	public static List<Reward> getDropInfo(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 4) {
			return null;
		}
		
	
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		List<Reward> lists = new ArrayList<Reward>();
		try {
			for(String intList : strArr){
				String[] strArr2 = intList.split(",");
				Reward reward = new Reward();
				reward.setType(Integer.valueOf(strArr2[0].trim()));
				reward.setId(Integer.valueOf(strArr2[1].trim()));
				reward.setNum(Integer.valueOf(strArr2[2].trim()));
				reward.setRate(Integer.valueOf(strArr2[3].trim()));

				lists.add(reward);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lists;
	}
	
	/**
	 * 转换成奖励信息
	 */
	public static List<Reward> getRewardInfo(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 4) {
			return null;
		}	
	
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		List<Reward> lists = new ArrayList<Reward>();
		try {
			for(String intList : strArr){
				String[] strArr2 = intList.split(",");
				Reward reward = new Reward();
				reward.setType(Integer.valueOf(strArr2[0].trim()));
				reward.setId(Integer.valueOf(strArr2[1].trim()));
				reward.setNum(Integer.valueOf(strArr2[2].trim()));
				if(strArr2.length > 3){
					reward.setBlind(Integer.valueOf(strArr2[3].trim()));
				}				
				lists.add(reward);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lists;
	}	

	/**
	 * 转换采集奖励物品信息
	 */
	public static Map<Integer, List<CollectReward>> getCollectRewardInfo(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 4) {
			return null;
		}
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		List<CollectReward> list1 = new ArrayList<CollectReward>();
		List<CollectReward> list2 = new ArrayList<CollectReward>();
		List<CollectReward> list3 = new ArrayList<CollectReward>();
		Map<Integer, List<CollectReward>> map = new HashMap<Integer, List<CollectReward>>();
		try {
			for(String intList : strArr){
				String[] strArr2 = intList.split(",");
				
				CollectReward collectReward = new CollectReward();
				collectReward.setCareer(Integer.valueOf(strArr2[0]));
				collectReward.setId(Integer.valueOf(strArr2[1]));
				collectReward.setRate(Integer.valueOf(strArr2[2]));
				
				if(collectReward.getCareer() == 1){
					list1.add(collectReward);
				}else if(collectReward.getCareer() == 2){
					list2.add(collectReward);
				}else if(collectReward.getCareer() == 3){
					list3.add(collectReward);
				}				
			}
			
			map.put(1, list1);
			map.put(2, list2);
			map.put(3, list3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}

	/**
	 * 二维数组转String
	 */
    public static String getStringByIntIntList(int[][] list)
    {
    	if(list == null) return "";
    	
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.length; i++)
        {
        	int[] a = list[i];
        	if(a != null && a.length > 0){
        		sb.append(Arrays.toString(a));
        	}
        	
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * map转为string
     */
    public static String getStringByIntIntMap(Map<Integer, Integer> map){
    	
		if(map == null || map.isEmpty()) return "";
		
    	StringBuilder sb = new StringBuilder("[");
    	for(Map.Entry<Integer, Integer> entry : map.entrySet()){
    		sb.append("[");
    		sb.append(entry.getKey());
    		sb.append(",");
    		sb.append(entry.getValue());
    		sb.append("]");
    	}
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * map转为string
     */
    public static String getStringByLongIntMap(Map<Long, Integer> map){
    	
		if(map == null || map.isEmpty()) return "";
		
    	StringBuilder sb = new StringBuilder("[");
    	for(Map.Entry<Long, Integer> entry : map.entrySet()){
    		sb.append("[");
    		sb.append(entry.getKey());
    		sb.append(",");
    		sb.append(entry.getValue());
    		sb.append("]");
    	}
        sb.append("]");
        return sb.toString();
    }
    
    /**
	 * Notice数组转String ->{{PLAYER,10001,0,name},{SCENE,0,0,地图名}}
	 */
	public static String getStringByNoticeList(List<Notice> list)
    {
		if(list == null || list.isEmpty()) return "";
		
        StringBuilder sb = new StringBuilder("{");
        int i = 1;
        for (Notice notice : list)
        {
        	i++;
        	
        	sb.append("{");
        	sb.append(notice.getType().getNumber());
        	sb.append(",");
        	sb.append(notice.getParamInt());
        	sb.append(",");
        	sb.append(notice.getParamInt2());
        	sb.append(",");
        	sb.append("'" + notice.getParamStr() + "'");
        	sb.append("}");
        	
        	if(i <= list.size()){
        		sb.append(",");
        	}
        }
        sb.append("}");
        return sb.toString();
    }


    /**
	 * 转换成附加属性信息
	 */
	public static List<AddAttr> getAddAttrInfo(String string){
		if (string == null || "".equals(string.trim()) || "0".equals(string.trim()) || string.length() < 4) {
			return null;
		}
		string = string.substring(2, string.length() - 2);
		String[] strArr = string.split("\\]\\[");
		List<AddAttr> lists = new ArrayList<AddAttr>();
		try {
			for(String intList : strArr){
				String[] strArr2 = intList.split(",");
				
				AddAttr addAttr = new AddAttr();
				addAttr.setAttrId(Integer.valueOf(strArr2[0]));
				addAttr.setMinAttrValue(Integer.valueOf(strArr2[1]));
				addAttr.setMaxAttrValue(Integer.valueOf(strArr2[2]));				

				lists.add(addAttr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lists;
	}
	
	
	public static void main(String[] args) {
		String aa = "1003";
		Integer bb = 1003;
		
		System.out.println(aa.equals(bb.toString()));
		
	}
}
