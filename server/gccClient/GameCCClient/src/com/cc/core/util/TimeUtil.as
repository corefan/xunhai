package com.cc.core.util
{
	import flash.utils.getTimer;

	public class TimeUtil
	{
		/**
		 *时间误差，精确到毫秒 
		 */		
		private static var tickOffset:int;
		
		
		/**
		 * 获取服务器时间，返回当前秒数 
		 * @return 
		 * 
		 */		
		public static function getServerSecond():int
		{
			return (getTimer()/1000)+tickOffset;
		}
		
		/**
		 * 获得服务器时间，返回Date
		 */
		public static function getSeverDate():Date{
			return (new Date(getServerSecond()*1000));
		}
		
		public static function syncServerTime(timeStamp:uint):void
		{
			tickOffset = timeStamp - (getTimer()/1000);
		}
		
		/**
		 * 根据时间返回字符串时间
		 * */
		public static function getTimeBySecond(second:int):String {
			
			var str:String = new String();
			
			if (second>=60) {
				str = int(second/60) + "分";
			} 
			str += (int(second%60)) + "秒";
			
			return str;
		}
		
		/**
		 * 根据时间返回字符串 00:00:00
		 */
		public static function formatTime(second:int):String{
			var hour:int = int(second/60/60)%24;
			var min:int = int(second/60)%60;
			var sec:int = int(second%60);
			var hourStr:String = hour < 10?("0"+hour):hour.toString();
			var minStr:String = min < 10?("0"+min):min.toString();
			var secStr:String = sec < 10?("0"+sec):sec.toString();
			
			return hourStr + ":" +minStr + ":" + secStr;
			//if(hour) return hourStr + ":" +minStr + ":" + secStr;
			//return minStr + ":" + secStr;
		}
		
		/**
		 * 根据时间返回字符串 00:00
		 */
		public static function formatTime2(second:int):String{
			var min:int = int(second/60)%60;
			var sec:int = int(second%60);
			var minStr:String = min < 10?("0"+min):min.toString();
			var secStr:String = sec < 10?("0"+sec):sec.toString();
			return (minStr + ":" + secStr);
		}
		
		/**
		 * 格式化数据网格列日期 MM-DD JJ:NN
		 */
		public static function formatColumnDate(tempDate:Date):String
		{
			var y:String = String(tempDate.fullYear);
			var m:String=((tempDate.getMonth()+1<10)?"0":"")+(tempDate.getMonth()+1);
			var day:String=(tempDate.getDate()<10)?"0":""+tempDate.getDate();
			var rect:String="";
			rect+=y+"-"+m+"-"+day+" ";
			rect+=(tempDate.getHours()<10)?"0":""+tempDate.getHours();
			rect+=":";
			rect+=(tempDate.getMinutes()<10)?("0"+tempDate.getMinutes()):""+tempDate.getMinutes();
			return rect; 
		}
		
		public static function formatColumnDate2(tempDate:Date):String
		{
			var y:String = String(tempDate.fullYear);
			var m:String=((tempDate.getMonth()+1<10)?"0":"")+(tempDate.getMonth()+1);
			var day:String=((tempDate.getDate()<10)?"0":"")+tempDate.getDate();
			var rect:String="";
			rect+=y+"/"+m+"/"+day+" ";
			return rect; 
		}
		
		/**
		 * XX年XX月XX日XX时
		 */
		public static function formatYMD(date:Date):String
		{
			var time:String = date.fullYear + "年"
				+ (date.month+1) +"月"
				+ date.date +"日";
//				+ date.hours +"时";
			return time; 
		}
		
		/**
		 * XX年XX月XX日XX时
		 */
		public static function formatRemain(second:int):String
		{
			var day:int = Math.floor(second/60/60/24);
			var hour:int = int(second/60/60)%24;
			var min:int = int(second/60)%60;
			var sec:int = int(second%60);
			return day + "天" + hour + "时" + min + "分" + sec + "秒";
		}
	}
}