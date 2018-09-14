package com.cc.app.notice
{
	import com.cc.core.event.GlobalEvent;
	
	import flash.events.Event;

	public class NoticeEvent extends GlobalEvent
	{
		/** 发送即时系统公告 */
		public static const SEND_NOTICE:String = "com.cc.app.notice:sendNotice";
		/**删除系统公告*/
		public static const DEL_NOTICE:String = "delNotice";
		/**添加系统公告*/
		public static const ADD_NOTICE:String = "addNotice";
		public function NoticeEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
		
		override public function clone():Event{
			return new NoticeEvent(type, data);
		}
	}
}