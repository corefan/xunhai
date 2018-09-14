package com.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.common.GameException;

/**
 * 随机Service
 * @author TopTong
 *
 */
public class RandomService {

	private static Random random = new Random();

	/**
	 * 获得随机数字
	 * @param num
	 * @return
	 */
	public static int getRandomNum(int num) {
		return random.nextInt(num);
	}

	/**
	 * 获得随机数字
	 * @param minNum 最小值
	 * @param maxNum 最大值
	 * @return
	 */
	public static int getRandomNum(int minNum, int maxNum) {
		return random.nextInt(maxNum-minNum+1)+minNum;
	}

	/**
	 * 是否在范围内 如(5/100)则有5%几率返回值为true
	 * @param minPercent 分子
	 * @param maxPercent 分母
	 * @return 是否在范围内
	 */
	public static boolean isInTheLimits(Integer minPercent, Integer maxPercent) {
		if (random.nextInt(maxPercent)+1<=minPercent) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从给定的数组中随机生成指定数量的数组(不重复)
	 * @param params
	 * @param num 生成数量
	 * @return
	 */
	public static <T> List<T> getArrayFromArrayByNum(List<T> params, int num) {
		List<T> results = new ArrayList<T>(num);

		List<T> paramsCopy = new ArrayList<T>(params);
		int size = params.size();
		num = Math.min(size, num);
		int count = 0;
		while (count < num) {
			int result = random.nextInt(size);
			T t = paramsCopy.get(result);
			if (!results.contains(t)) {
				results.add(t);
				
				paramsCopy.remove(t);
				size --;
				count ++;
			}
		}

		return results;
	}
	
	/**
	 * 获取一个集合列表指定数目的子集 
	 * @param <T>	
	 * @param list 集合列表 
	 * @param subCount 子集数目 
	 * @return 子集
	 */
	public static <T> List<T> randomSubset(final List<T> list, final int subCount) {
		if (list == null || list.size() == 0 || list.size() < subCount) {
			throw new GameException("获取随机子集的参数不合逻辑！");
		}

		if (list.size() == subCount) {
			return list;
		}


		List<T> rs = new ArrayList<T>(subCount);

		for (int i = 0; i < subCount; i++) {
			rs.add(null);
		}

		Random random = new Random();

		List<T> copyList = new ArrayList<T>();

		copyList.addAll(list);

		for (int i = 0, odd = copyList.size() - 1; i < subCount; i++, odd--) {
			int ranindex = random.nextInt(odd);
			rs.set(i, copyList.get(ranindex));
			copyList.set(ranindex, list.get(odd));
		}

		return rs;
	} 

}
