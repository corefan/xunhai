package com.cc.app.login
{
	import com.cc.core.event.GlobalEvent;
	
	public class LoginEvent extends GlobalEvent
	{
		public static const LOGIN:String = "login";
		
		public function LoginEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
	}
}