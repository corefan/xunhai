package com.cc.app.dealData
{
	import com.cc.core.event.GlobalEvent;
	
	import flash.events.Event;

	public class DealDataEvent extends GlobalEvent
	{
		/** 刷新基础缓存 */
		public static const REFRESH_BASE_CACHE:String = "com.cc.app.dealData:refreshBaseCache";
		/** 刷新配置缓存 */
		public static const REFRESH_CONFIG_CACHE:String = "com.cc.app.dealData:refreshConfigCache";
		/** 同步缓存 */
		public static const SYN_CACHE_DATA:String = "com.cc.app.dealData:synCacheData";
		/** 热更新class */
		public static const HOT_UPDATE_CLASS:String = "com.cc.app.dealData:hotupdateClass";
		
		/** 停服维护 */
		public static const STOP_SERVER:String = "com.cc.app.dealData:stopServer";
		
		public function DealDataEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
		
		override public function clone():Event{
			return new DealDataEvent(type, data);
		}
	}
}