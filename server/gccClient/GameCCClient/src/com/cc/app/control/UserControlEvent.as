package com.cc.app.control
{
	import com.cc.core.event.GlobalEvent;
	
	public class UserControlEvent extends GlobalEvent
	{
		/**查看用户日志*/
		public static const CHECK_USER_LOG:String = "checkUserLog";
		/**删除用户*/
		public static const DEL_USER:String = "deleteUser";
		/**添加用户*/
		public static const ADD_USER:String = "addUser";
		/**修改用户*/
		public static const MODIFY_USER:String = "modifyUser";
		
		/**删除角色*/
		public static const DEL_ROLE:String = "deleteRole";
		/**添加角色*/
		public static const ADD_ROLE:String = "addRole";
		/**修改角色*/
		public static const MODIFY_ROLE:String = "modifyRole";
		
		/**删除权限*/
		public static const DEL_COMPETENCE:String = "deleteCompetence";
		/**添加权限*/
		public static const ADD_COMPETENCE:String = "addCompetence";
		/**修改权限*/
		public static const MODIFY_COMPETENCE:String = "modifyCompetence";
		
		/**获取权限列表*/
		public static const GET_QUXIAN_LIST:String = "getQuxianList";
		
		public function UserControlEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
	}
}