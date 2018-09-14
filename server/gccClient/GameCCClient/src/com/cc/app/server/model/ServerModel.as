package com.cc.app.server.model
{
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	public class ServerModel extends EventDispatcher
	{

		/**服务器列表*/
		public var serverList:ArrayCollection;
		
		private static var _instance:ServerModel;
		
		public static function get instance():ServerModel
		{
			if(_instance == null){
				_instance = new ServerModel();
			}
			return _instance;
		}
		
		public function ServerModel()
		{
		}
		
		
		
	}
}