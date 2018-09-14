package com.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间Service
 * @author ken
 * 
 */
public class DateService {

	/**
	 * 获得当前Util时间
	 * 
	 * @return
	 */
	public static Date getCurrentUtilDate() {
		return new Date();
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 * @return
	 */
	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	/**
	 * 返回分钟
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}
	
	/**
	 * 返回小时
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 返回天
	 * */
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * 获取一天的开始时间(yyyy-MM-dd 00:00:00)
	 * @param time
	 * @return
	 */
	public static Date getDayOfStartTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取一天的结束时间(yyyy-MM-dd 23:59:59)
	 * @param time
	 * @return
	 */
	public static Date getDayOfEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取一个小时的开始时间(yyyy-MM-dd hh:00:00)
	 * @param time
	 * @return
	 */
	public static Date getHourOfStartTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取一个小时的结束时间(yyyy-MM-dd hh:59:59)
	 * @param time
	 * @return
	 */
	public static Date getHourOfEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 根据类型修改时间(加时间)
	 * @param date
	 * @param type 秒，分，小时，天等等
	 * @param time 
	 * @return
	 */
	public static Date addDateByType(Date date, int type, int time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, time);
		
		return calendar.getTime();
	}
	
	/**
	 * 根据类型修改时间(减时间)
	 * @param date
	 * @param type 秒，分，小时，天等等
	 * @param time 
	 * @return
	 */
	public static Date subDateByType(Date date, int type, int time) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		long dateTime = calendar.getTimeInMillis();
		
		long mul = 1;
		
		switch (type) {
		case Calendar.DAY_OF_MONTH:
			mul = mul * 24 * 60 * 60 * 1000;
			break;
		case Calendar.HOUR:
		case Calendar.HOUR_OF_DAY:
			mul = mul * 60 * 60 * 1000;
			break;
		case Calendar.MINUTE:
			mul = mul * 60 * 1000;
			break;
		case Calendar.SECOND:
			mul = mul * 1000;
			break;
		}
		
		calendar.setTimeInMillis(dateTime - mul * time);
		
		return calendar.getTime();
	}
	
	/**
	 * 是否是当天
	 * @param date
	 * @return
	 */
	public static boolean isCurrentDay(Date date) {
		boolean result = false;
		
		Calendar today = Calendar.getInstance();
		
		Calendar dateDay = Calendar.getInstance();
		dateDay.setTime(date);
		
		if (today.get(Calendar.YEAR) == dateDay.get(Calendar.YEAR)
				&& today.get(Calendar.MONTH) == dateDay.get(Calendar.MONTH)
				&& today.get(Calendar.DAY_OF_MONTH) == dateDay.get(Calendar.DAY_OF_MONTH)) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 当前时间是否在开始时间和结束时间之间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isInTime(Date startTime, Date endTime) {
		boolean result = false;

		if (System.currentTimeMillis() > startTime.getTime()
				&& System.currentTimeMillis() < endTime.getTime()) {
			result = true;
		}

		return result;
	}
	
	/**
	 *	指定的时间是否在开始时间和结束时间之内
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isInTime(Date date, Date startTime, Date endTime) {
		boolean result = false;

		if (date.getTime() > startTime.getTime()
				&& date.getTime() < endTime.getTime()) {
			result = true;
		}

		return result;
	}
	
	/**
	 * 当前时间是否在开始时间和结束时间之间
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isInTime(long startTime, long endTime) {
		boolean result = false;

		if (System.currentTimeMillis() > startTime
				&& System.currentTimeMillis() < endTime) {
			result = true;
		}

		return result;
	}
	
	/**
	 * 验证时间是否在这周
	 * @param getGiftbagTime
	 * @return
	 */
	public static boolean checkIsInWeek(Date getGiftbagTime) {
		boolean result = false;
		
		if (getGiftbagTime != null) {
			
			Calendar calendar = Calendar.getInstance();
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			
			// 本周第一天
			Calendar fistDay = Calendar.getInstance();
			fistDay.add(Calendar.DAY_OF_MONTH, 2 - week);
			fistDay.set(Calendar.HOUR_OF_DAY, 0);
			fistDay.set(Calendar.MINUTE, 0);
			fistDay.set(Calendar.SECOND, 0);
			
			// 本周最后一天
			Calendar lastDay = Calendar.getInstance();
			lastDay.add(Calendar.DAY_OF_MONTH, 8 - week);
			lastDay.set(Calendar.HOUR_OF_DAY, 23);
			lastDay.set(Calendar.MINUTE, 59);
			lastDay.set(Calendar.SECOND, 59);
			
			if (getGiftbagTime.getTime() > fistDay.getTime().getTime() && getGiftbagTime.getTime() < lastDay.getTime().getTime()) {
				result = true;
			}
		}
		
		return result;
	}
	
	/**
	 * 是否每周第一天
	 * @return
	 */
	public static boolean isFirstWeekDay() {
		boolean result = false;
		
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 设置calendar时间
	 */
	public static Calendar setCalendarTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
	
	/**
	 * 获取时间字符串(00:00:00)
	 */
	public static String getTimeStr(int hour, int minute) {
		String str = hour + ":";
		if (minute > 9) {
			str = str + minute + ":00";
		} else {
			str = str + "0" + minute + ":00";
		}
		return str;
	}
	
	/**
	 * 指定字符类时间转Date
	 * @param timeStr
	 * @return
	 */
	public static Date getDateByString(String timeStr) {
		if(timeStr == null || timeStr.trim().equals(""))
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return dateFormat.parse(timeStr);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	/** 时间格式化 */
	public static String dateFormat(Date date) {
		if(date == null) return "";
		
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/** 时间格式化 */
	public static String dateFormat_ymd(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}
