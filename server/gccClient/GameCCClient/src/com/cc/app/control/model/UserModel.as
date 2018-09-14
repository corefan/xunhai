package com.cc.app.control.model
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	public class UserModel extends EventDispatcher
	{
		/**权限列表*/
		public var competenceList:ArrayCollection;
		/**角色列表*/
		public var roleList:ArrayCollection;
		
		private static var _instance:UserModel;
		
		public static function get instance():UserModel
		{
			if(_instance == null){
				_instance = new UserModel();
			}
			return _instance;
		}
		
		public function UserModel()
		{
		}
		
		
		
	}
}