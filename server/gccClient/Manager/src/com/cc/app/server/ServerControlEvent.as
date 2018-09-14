package com.cc.app.server
{
	import com.cc.core.event.GlobalEvent;
	
	public class ServerControlEvent extends GlobalEvent
	{
		/**查看用户日志*/
		public static const CHECK_SERVER_LOG:String = "checkServerLog";
		/**删除用户*/
		public static const DEL_SERVER:String = "deleteServer";
		/**添加用户*/
		public static const ADD_SERVER:String = "addServer";
		/**修改用户*/
		public static const MODIFY_SERVER:String = "modifyServer";
		
		
		public function ServerControlEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
	}
}