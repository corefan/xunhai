package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.cache.BaseCacheService;
import com.common.Config;
import com.common.RandomService;
import com.constant.CacheConstant;
import com.domain.map.BaseMap;

/**
 * 公共帮助类
 * */
public class CommonUtil {

	/**
	 * 初始化
	 * */
	public static void init() {
		initSensitiveWords();
	}
	
	/**
	 * 加载地图资源
	 */
	public static JSONObject initSceneInfo(int mapId){
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("config/scene/"+mapId+".json");
			if (url != null) {
				File file = new File(url.getFile().replace("%20", " "));
				if (file != null && file.exists()) {
					if (!".svn".equals(file.getName())) {
						FileInputStream fis = new FileInputStream(file);
						InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
						BufferedReader br = new BufferedReader(isr);
						String line = null;
						while ((line = br.readLine()) != null && !"".equals(line.trim())) {
							line.trim();
							break;
						}
						JSONObject json = new JSONObject(line);
						
						
						// 关闭流
						br.close();
						isr.close();
						fis.close();
						
						return json;
					}
				}
			}
			
		} catch (Exception e) {
			LogUtil.error(e);
		} 
		return null;
	}
	
	
	/**
	 * 以一个点为中心，radius为半径的园内随意一个点。
	 * 
	 */
	public static int[] getRandomPoint(BaseMap baseMap,
			int refX, int refZ, int radius) {

		if(radius < 0) radius = 0;
		
		int i = 100;
		while (i-- > 0) {
			double r = Math.random() * radius;
			double angle = Math.random() * Math.PI * 2;

			int x = refX + (int)(r * Math.sin(angle));
			
			if(x < 0) continue;
			
			int z = refZ + (int)(r * Math.cos(angle));
			
			if(z < 0) continue;
			
			boolean isBlock = baseMap.isBlock(x, z);
			if (!isBlock) {
				return new int[]{x, z};
			}
		}
		System.out.println("刷怪出阻挡了，mapId is "+baseMap.getMap_id()+" x="+refX+" z="+refZ);
		return new int[]{refX, refZ};
	}

	/**
	 * 以一个点为中心，radius为半径的圆周上随意一个点。
	 * 
	 */
	public static int[] getRandomPoint2(BaseMap baseMap,
			int refX, int refZ, int radius) {

		if(radius < 0) radius = 0;
		
		int i = 100;
		while (i-- > 0) {
			double angle = RandomService.getRandomNum(360);

			int x = refX + (int)(radius * Math.cos(angle * Math.PI / 180));
			
			
			if(x < 0) continue;
			
			int z = refZ + (int)(radius * Math.sin(angle * Math.PI / 180));
			
			if(z < 0) continue;
			
			boolean isBlock = baseMap.isBlock(x, z);
			if (!isBlock) {
				return new int[]{x, z};
			}
		}
		System.out.println("刷怪出阻挡了，mapId is "+baseMap.getMap_id()+" x="+refX+" z="+refZ);
		return new int[]{refX, refZ};
	}
	
	
	/**
	 * 返回在target 到 host 的直线上，以点host 为起点，以distance 为长度的 点
	 * 
	 * @param host
	 * @param target
	 * @param distance
	 * @return
	 */
	public static int[] buildEscapsePoint(int hostX, int hostZ, int targetX, int targetZ,
			double distance) {

        double px = hostX - targetX;
        double py = hostZ - targetZ;
        
		double dis = (px * px + py * py);
		double sinA = Math.pow((targetZ - hostZ), 2) / dis;
		double cosA = Math.pow((targetX - hostX), 2) / dis;

		double x = 0;
		double y = 0;
		double y1 = hostZ + Math.sqrt(sinA * distance * distance);
		double y2 = hostZ - Math.sqrt(sinA * distance * distance);

		double x1 = hostX + Math.sqrt(cosA * distance * distance);
		double x2 = hostX - Math.sqrt(cosA * distance * distance);

		if (targetX >= hostX) {
			if (x1 >= hostX) {
				x = x2;
			} else {
				x = x1;
			}
		} else {
			if (x1 >= hostX) {
				x = x1;
			} else {
				x = x2;
			}
		}

		if (targetZ > hostZ) {
			if (y1 > hostZ) {
				y = y2;
			} else {
				y = y1;
			}
		} else {
			if (y1 >= hostZ) {
				y = y1;
			} else {
				y = y2;
			}
		}
		return new int[]{(int)x, (int)y};
	}
	
	/**
	 * 初始化敏感字
	 */
	public static void initSensitiveWords() {
		
		BufferedReader br = null;
		
		try {
			File file = new File(Thread.currentThread().getContextClassLoader().getResource("config/other/dirty.txt").getFile().replace("%20", " "));

			List<String> nameList = new ArrayList<String>();
			if (file != null && file.exists()) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				StringBuilder sb = new StringBuilder(1 << 12);
				String line = null;
				while ((line = br.readLine()) != null && !"".equals(line.trim())) {
					sb.append(line);
				}
				
				String[] nameArr = sb.toString().split("\\|");
				for (String name : nameArr) {
					nameList.add(name);
				}
				
				BaseCacheService.putToBaseCache(CacheConstant.B_DIRTY_NAME_LIST, nameList);
				
				sb = null;
				line = null;
			}
		} catch (Exception e) {
			LogUtil.error("异常: ",e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LogUtil.error("异常: ",e);
				}
			}
		}
	}
	
	/**
	 * 验证长度
	 * */
	public static Integer checkLength(String inputValue) {
		
		int totalLen = 0;
		try {
			inputValue = new String(inputValue.getBytes(),"UTF-8");
			for (int i=0;i<inputValue.length();i++) {
				String s = null;
				if (i+1 == inputValue.length()) {
					s = inputValue.substring(i-1, i);
				} else {
					s = inputValue.substring(i, i+1);
				}
				// 中文
				if (s.getBytes().length >= 3) {
					totalLen += 2;
				} else {
					totalLen += s.getBytes().length;
				}
			}
		} catch (UnsupportedEncodingException e) {
			totalLen = 0;
		}
		
		return totalLen;
	}
	
	/**
	 * 替换掉特殊字符
	 * */
	public static String replaceInput(String inputValue) {
		
		inputValue = inputValue.replace(" ", "");
		inputValue = inputValue.replace("　", "");
		inputValue = inputValue.replace("	", "");
		inputValue = inputValue.replace(".", "");
		inputValue = inputValue.replace("'", "");
		inputValue = inputValue.replace("%", "");
		inputValue = inputValue.replace(",", "");
		inputValue = inputValue.replace(";", "");
		inputValue = inputValue.replace("&", "");
		inputValue = inputValue.replace("?", "");
		
		return inputValue;
	}
	
	/**
	 * 验证是否包含敏感字或特殊字符
	 * */
	public static boolean checkInput(String inputValue) {
		
		// 全角或半角空格
		if (inputValue.contains(" ") || inputValue.contains("　")) {
			return true;
		}
		
		// 特殊字符
		if (!"ru".equalsIgnoreCase(Config.LANGUAGE)) {
			if(inputValue.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*·*\\s*", "").length() != 0) {
				return true;
			}
		}
		
		return checkSensitive(inputValue);
	}
	
	/**
	 * 验证是否包含敏感字
	 * */
	@SuppressWarnings("unchecked")
	public static boolean checkSensitive(String inputValue) {
		
		List<String> nameList = (List<String>) BaseCacheService.getFromBaseCache(CacheConstant.B_DIRTY_NAME_LIST);
		if (nameList != null) {
			return nameList.contains(inputValue);
		}
		
		return false;
	}
	
}
