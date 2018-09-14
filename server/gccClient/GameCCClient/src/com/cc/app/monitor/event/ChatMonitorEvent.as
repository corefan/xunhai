package com.cc.app.monitor.event
{
	import com.cc.core.event.GlobalEvent;
	
	import flash.events.Event;
	
	public class ChatMonitorEvent extends GlobalEvent
	{
		/** 请求聊天信息 */
		public static const REQ_CHAT_INFO:String = "com.cc.app.monitor:reqChatInof";
		
		public function ChatMonitorEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
		
		override public function clone():Event{
			return new ChatMonitorEvent(type, data);
		}
		
	}
}