package com.quartz;

import com.common.RandomService;
import com.constant.QuartzCronTypeConstant;
import com.util.LogUtil;

/**
 * 调度帮助类
 *
 */
public class QuartzUtil {

	
	/**
	 * 根据游戏site和调度类型得到执行表达式
	 * */
	public static String getCronBySiteAndType(String siteName, Integer cronType) {
		
		return getDefaultValueByCronType(siteName, cronType);
	}
	
	/**
	 * 根据调度类型得到默认值
	 * */
	public static String getDefaultValueByCronType(String gameSite, Integer cronType) {
		
		String defaultCron = null;
		
		switch (cronType) {
		case QuartzCronTypeConstant.THREE_SEC:
			return getCronExpByGameSite_threeSecond(gameSite);
		case QuartzCronTypeConstant.TEN_SEC:
			return getCronExpByGameSite_tenSecond(gameSite);
			
		case QuartzCronTypeConstant.ONE_MIN_ONE:
			return getCronExpByGameSite_oneMin(gameSite);
		case QuartzCronTypeConstant.ONE_MIN_TWO:
			return getCronExpByGameSite_twoMin(gameSite);
		case QuartzCronTypeConstant.FIVE_MIN_ONE:
			return getCronExpByGameSite_fiveOne(gameSite);
		case QuartzCronTypeConstant.FIVE_MIN_TWO:
			return getCronExpByGameSite_fiveTwo(gameSite);
		case QuartzCronTypeConstant.FIVE_MIN_THREE:
			return getCronExpByGameSite_fiveThree(gameSite);
		case QuartzCronTypeConstant.FIVE_MIN_FOUR:
			return getCronExpByGameSite_fiveFour(gameSite);
		case QuartzCronTypeConstant.TEN_MIN:
			return getCronExpByGameSite_tenMin(gameSite);
			
		case QuartzCronTypeConstant.ONE_HOUR_ONE:
			return getCronExpByGameSite_oneHour_1(gameSite);
		case QuartzCronTypeConstant.SIX_HOUR:
			return getCronExpByGameSite_sixHour(gameSite);
			
		case QuartzCronTypeConstant.DAY_ONE:
			return getCronExpByGameSite_dailyOne(gameSite);
		case QuartzCronTypeConstant.DAY_TWO:
			return getCronExpByGameSite_dailyTwo(gameSite);
		case QuartzCronTypeConstant.DAY_THREE:
			return getCronExpByGameSite_dailyThree(gameSite);
		case QuartzCronTypeConstant.DAY_FOUR:
			return getCronExpByGameSite_dailyFour(gameSite);
		case QuartzCronTypeConstant.DAY_SEVEN:
			return getCronExpByGameSite_dailySeven(gameSite);
			
		case QuartzCronTypeConstant.WEEK_ONE:
			return getCronExpByGameSite_weekOne(gameSite);
		}
		
		return defaultCron;
	}
	
	
	/**
	 * 根据游戏区号得到10秒钟调度执行时间
	 * */
	private static String getCronExpByGameSite_tenSecond(String gameSite) {
		
		String cronExpression = "5/10 * * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 0:
					cronExpression = "0/10 * * * * ?";
					break;
				case 1:
					cronExpression = "2/10 * * * * ?";
					break;
				case 2:
					cronExpression = "4/10 * * * * ?";
					break;
				case 3:
					cronExpression = "6/10 * * * * ?";
					break;
				case 4:
					cronExpression = "8/10 * * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到3秒钟调度执行时间
	 * */
	private static String getCronExpByGameSite_threeSecond(String gameSite) {
		
		String cronExpression = "0/3 * * * * ?";
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到1分钟调度执行时间
	 * */
	private static String getCronExpByGameSite_oneMin(String gameSite) {
		
		String cronExpression = "1 0/1 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "1 0/1 * * * ?";
					break;
				case 2:
					cronExpression = "2 0/1 * * * ?";
					break;
				case 3:
					cronExpression = "3 0/1 * * * ?";
					break;
				case 4:
					cronExpression = "4 0/1 * * * ?";
					break;
				case 0:
					cronExpression = "0 0/1 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	private static String getCronExpByGameSite_twoMin(String gameSite){
		String cronExpression = "14 0/1 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "14 0/1 * * * ?";
					break;
				case 2:
					cronExpression = "24 0/1 * * * ?";
					break;
				case 3:
					cronExpression = "34 0/1 * * * ?";
					break;
				case 4:
					cronExpression = "44 0/1 * * * ?";
					break;
				case 0:
					cronExpression = "54 0/1 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	/**
	 * 根据游戏区号得到5分钟调度执行时间
	 * */
	private static String getCronExpByGameSite_fiveOne(String gameSite) {
		
		String cronExpression = "14 4/5 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "9 4/5 * * * ?";
					break;
				case 2:
					cronExpression = "12 4/5 * * * ?";
					break;
				case 3:
					cronExpression = "15 4/5 * * * ?";
					break;
				case 4:
					cronExpression = "18 4/5 * * * ?";
					break;
				case 0:
					cronExpression = "21 4/5 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到5分钟调度执行时间
	 * */
	private static String getCronExpByGameSite_fiveTwo(String gameSite) {
		
		String cronExpression = "11 2/5 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "11 2/5 * * * ?";
					break;
				case 2:
					cronExpression = "21 2/5 * * * ?";
					break;
				case 3:
					cronExpression = "31 2/5 * * * ?";
					break;
				case 4:
					cronExpression = "41 2/5 * * * ?";
					break;
				case 0:
					cronExpression = "51 2/5 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到5分钟调度执行时间
	 * */
	private static String getCronExpByGameSite_fiveThree(String gameSite) {
		
		String cronExpression = "3 3/5 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "3 3/5 * * * ?";
					break;
				case 2:
					cronExpression = "13 3/5 * * * ?";
					break;
				case 3:
					cronExpression = "23 3/5 * * * ?";
					break;
				case 4:
					cronExpression = "33 3/5 * * * ?";
					break;
				case 0:
					cronExpression = "43 3/5 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到5分钟调度执行时间
	 * */
	private static String getCronExpByGameSite_fiveFour(String gameSite) {
		
		String cronExpression = "15 0/5 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "20 0/5 * * * ?";
					break;
				case 2:
					cronExpression = "25 0/5 * * * ?";
					break;
				case 3:
					cronExpression = "35 0/5 * * * ?";
					break;
				case 4:
					cronExpression = "28 0/5 * * * ?";
					break;
				case 0:
					cronExpression = "33 0/5 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到10分钟调度执行时间
	 * */
	private static String getCronExpByGameSite_tenMin(String gameSite) {
		
		String cronExpression = "38 0/10 * * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "28 0/10 * * * ?";
					break;
				case 2:
					cronExpression = "33 0/10 * * * ?";
					break;
				case 3:
					cronExpression = "38 0/10 * * * ?";
					break;
				case 4:
					cronExpression = "43 0/10 * * * ?";
					break;
				case 0:
					cronExpression = "48 0/10 * * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到1小时调度执行时间
	 * */
	private static String getCronExpByGameSite_oneHour_1(String gameSite) {
		
		String cronExpression = "22 59 0/1 * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "15 59 0/1 * * ?";
					break;
				case 2:
					cronExpression = "18 59 0/1 * * ?";
					break;
				case 3:
					cronExpression = "21 59 0/1 * * ?";
					break;
				case 4:
					cronExpression = "24 59 0/1 * * ?";
					break;
				case 0:
					cronExpression = "27 59 0/1 * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到6小时调度执行时间
	 * */
	private static String getCronExpByGameSite_sixHour(String gameSite) {
		
		String cronExpression = "2 10 0/6 * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "1 3 0/6 * * ?";
					break;
				case 2:
					cronExpression = "2 6 0/6 * * ?";
					break;
				case 3:
					cronExpression = "3 9 0/6 * * ?";
					break;
				case 4:
					cronExpression = "4 12 0/6 * * ?";
					break;
				case 0:
					cronExpression = "5 15 0/6 * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到每日调度执行时间
	 * dailyOne
	 * */
	private static String getCronExpByGameSite_dailyOne(String gameSite) {
		
		String cronExpression = "3 1 0 * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "2 1 0 * * ?";
					break;
				case 2:
					cronExpression = "3 1 0 * * ?";
					break;
				case 3:
					cronExpression = "4 1 0 * * ?";
					break;
				case 4:
					cronExpression = "5 1 0 * * ?";
					break;
				case 0:
					cronExpression = "6 1 0 * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到每日调度执行时间
	 * */
	private static String getCronExpByGameSite_dailyTwo(String gameSite) {
		
		String cronExpression = "12 1 0 * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "11 1 0 * * ?";
					break;
				case 2:
					cronExpression = "13 1 0 * * ?";
					break;
				case 3:
					cronExpression = "15 1 0 * * ?";
					break;
				case 4:
					cronExpression = "17 1 0 * * ?";
					break;
				case 0:
					cronExpression = "19 1 0 * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
	}
	
	/**
	 * 根据游戏区号得到每日调度执行时间
	 * */
	private static String getCronExpByGameSite_dailyThree(String gameSite) {
		
		String cronExpression = "10 58 23 * * ?";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 5;
				switch (index) {
				case 1:
					cronExpression = "1 58 23 * * ?";
					break;
				case 2:
					cronExpression = "3 58 23 * * ?";
					break;
				case 3:
					cronExpression = "5 58 23 * * ?";
					break;
				case 4:
					cronExpression = "7 58 23 * * ?";
					break;
				case 0:
					cronExpression = "9 58 23 * * ?";
					break;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;
		

	}
	
	/**
	 * 根据游戏区号得到每日调度执行时间
	 * */
	private static String getCronExpByGameSite_dailyFour(String gameSite) {
		int min = RandomService.getRandomNum(5, 48);
		int second = RandomService.getRandomNum(9, 50);
		
		String cronExpression = ""+second+" "+min+" 3 * * ?";
		
		return cronExpression;
	}
	
	
	/**
	 * 根据游戏区号得到每日调度执行时间
	 * */
	private static String getCronExpByGameSite_dailySeven(String gameSite) {
		
		int min = RandomService.getRandomNum(5, 48);
		int second = RandomService.getRandomNum(9, 50);
		
		String cronExpression = ""+second+" "+min+" 5 * * ?";
		
		return cronExpression;
	}

	
	/**
	 * 根据游戏区号得到清理日志库数据调度执行时间
	 * */
	private static String getCronExpByGameSite_weekOne(String gameSite) {
		
		String cronExpression = "0 0 4 ? * MON";
		
		if (gameSite != null && !"".equals(gameSite.trim())) {
			try {
				String num = gameSite.substring(gameSite.length() - 1);
				Integer index = Integer.parseInt(num) % 8;
				if(index > 0){
					cronExpression = "0 "+ RandomService.getRandomNum(1, 10) +" 4 ? * "+index;
				}
			} catch (NumberFormatException e) {
			    LogUtil.error("异常:",e);
			}
		}
		
		return cronExpression;

	}
	
}
