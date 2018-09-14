package com.cc.core.util
{
	import mx.controls.DateField;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.formatters.DateFormatter;

		/**
		 * <p>Title:日期时间工具类 </p>
		 * <p>Description:提供时间日期的一些to string 或  string to 的操作</p>
		 * @author Maniac
		 */
		public class DateTimeUtil
		{
			
			public static const MILLISECOND:Number=1;
			public static const SECOND:Number=MILLISECOND * 1000;
			public static const MINUTE:Number=SECOND * 60;
			public static const HOUR:Number=MINUTE * 60;
			public static const DAY:Number=HOUR * 24;
			public static const WEEK:Number=DAY * 7;
			public static const CHINESE_DATE_FORMAT:String="YYYY-MM-DD";
			public static const CHINESE_DATETIME_FORMAT:String="YYYY-MM-DD";
			
			/**
			 * 字符串转换成日期类型（使用mx.controls.DateField类）
			 * @param dateString
			 * @param formatString
			 * @return 
			 */
			public static function stringToDate(dateString:String, formatString:String=CHINESE_DATE_FORMAT):Date
			{
				return DateField.stringToDate(dateString,formatString);
			}
			
			/**
			 * 日期转换成字符串类型（使用mx.controls.DateField类）
			 * @param date
			 * @param formatString
			 * @return 
			 */
			public static function dateToString(date:Date,formatString:String=CHINESE_DATE_FORMAT):String{
				return DateField.dateToString(date,formatString);
			}
			
			/**
			 * 格式化时间YYYY-MM-DD
			 * @param date
			 * @param formatString
			 * @return
			 */
			public static function formatDateTime(date:Date, formatString:String=CHINESE_DATE_FORMAT):String
			{
				
				//需要as3corelib.swc如果没有可以直接使用 ：return dateToString(data,formatString);
				var dateFormater:DateFormatter=new DateFormatter();
				dateFormater.formatString=formatString;
				return dateFormater.format(date);
			}
			
			/**
			 * 格式化时间YYYY-MM-DD HH:NN:SS
			 * @param date
			 * @param formatString
			 * @return
			 */
			public static function formatFullDateTime(date:Date, formatString:String=CHINESE_DATETIME_FORMAT):String
			{
				//需要as3corelib.swc如果没有可以直接使用 ：return dateToString(data,formatString);
				var dateFormater:DateFormatter=new DateFormatter();
				dateFormater.formatString=formatString;
				return dateFormater.format(date);
			}
			
			/**
			 * 为DataGridColumn提供时间格式化的labelFunction
			 *
			 * @param item
			 * @param column
			 * @return
			 *
			 */
			public static function formatDateForDataGridColumn(item:Object, column:DataGridColumn):String
			{
				
				return formatDateTime(item[column.dataField], CHINESE_DATE_FORMAT);
			}
			
			/**
			 * 为DataGridColumn提供时间格式化的labelFunction
			 *
			 * @param item
			 * @param column
			 * @return
			 *
			 */
			public static function formatDateTimeForDataGridColumn(item:Object, column:DataGridColumn):String
			{
				
				return formatDateTime(item[column.dataField], CHINESE_DATETIME_FORMAT);
			}
			
			/**
			 * 给制定时间添加天数或减少天数
			 * @param date
			 * @param addDay
			 * @return
			 *
			 */
			public static function addDaysByDateTime(date:Date, addDay:Number):Date
			{
				return new Date(date.getTime() + addDay * DAY);
			}
			
			/**
			 * 取下一天
			 */
			public static function  getNextDay(currentDate:Date):Date{
				return addDaysByDateTime(currentDate,1);
			}
			
			/**
			 * 取上一天
			 */
			public static function  getLastDay(currentDate:Date):Date{
				return addDaysByDateTime(currentDate,-1);
			}
			
			/**
			 * 取下一个月
			 */
			public static function getNextMonth(currentDate:Date):Date
			{
				var returnDate:Date=new Date(currentDate.getTime());
				returnDate.setMonth(returnDate.getMonth() + 1, returnDate.getDate());
				return returnDate;
			}
			
			/**
			 * 取上一个月
			 */
			public static function getLastMonth(currentDate:Date):Date
			{
				var returnDate:Date=new Date(currentDate.getTime());
				returnDate.setMonth(returnDate.getMonth() - 1, returnDate.getDate());
				return returnDate;
			}
			
			/**
			 * 取下一个年
			 */
			public static function getNextYear(currentDate:Date):Date
			{
				var returnDate:Date=new Date(currentDate.getTime());
				returnDate.setFullYear(returnDate.getFullYear() + 1);
				return returnDate;
			}
			
			/**
			 * 取上一个年
			 */
			public static function getLastYear(currentDate:Date):Date
			{
				var returnDate:Date=new Date(currentDate.getTime());
				returnDate.setFullYear(returnDate.getFullYear() - 1);
				return returnDate;
			}
			
			
			/**
			 * 取当月月底
			 */
			public static function getFristDayOfMonth(currentDate:Date):Date
			{
				currentDate.setMonth(currentDate.getMonth(), 1); //下个月的第一天，也就是下个月1号
				return currentDate;
			}
			
			/**
			 * 取当月月底
			 */
			public static function getLastDayOfMonth(currentDate:Date):Date
			{
				currentDate.setMonth(currentDate.getMonth() + 1, 1); //下个月的第一天，也就是下个月1号
				currentDate.setDate(currentDate.getDate() - 1); //下个月1号之前1天，也就是本月月底
				return currentDate;
			}
			
			
			/**
			 * 获取日期的中文表示方式：例如星期一\星期二之类的。(注意0表示星期天)
			 * @param currentDate
			 * @return
			 */
			public static function getChineseDay(currentDate:Date):String
			{
				switch (currentDate.getDay())
				{
					case 0:
						return "星期日";
						
					case 1:
						return "星期一";
						
					case 2:
						return "星期二";
						
					case 3:
						return "星期三";
						
					case 4:
						return "星期四";
						
					case 5:
						return "星期五";
						
					case 6:
						return "星期六";
						
					default:
						return "";
				}
			}
			
			/**
			 * 获取日期的中文表示方式：例如星期一\星期二之类的。(注意0表示星期天)
			 * @param currentDate
			 * @return
			 */
			public static function getEnglishDay(currentDate:Date):String
			{
				switch (currentDate.getDay())
				{
					case 0:
						return "Sunday";
						
					case 1:
						return "Monday";
						
					case 2:
						return "Tuesday";
						
					case 3:
						return "Wednesday";
						
					case 4:
						return "Thursday";
						
					case 5:
						return "Friday";
						
					case 6:
						return "Saturday";
						
					default:
						return "";
				}
			}
			
			
		}
}