package com.cc.core.event
{
	import flash.events.Event;
	
	public class GlobalEvent extends Event
	{
		public var data:Object;
		public function GlobalEvent(type:String, data:Object = null)
		{
			super(type, false, false);
			this.data = data;
		}
		
		override public function clone():Event{
			return new GlobalEvent(type, data);
		}
	}
}