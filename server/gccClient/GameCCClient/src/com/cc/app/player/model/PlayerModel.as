package com.cc.app.player.model
{
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	public class PlayerModel extends EventDispatcher
	{
		public var port:String;
		public var host:String;
//		public var site:String;
		public var assets:String;
		public var assets1:String;
		
		private static var _instance:PlayerModel;
		
		public static function get instance():PlayerModel
		{
			if(_instance == null){
				_instance = new PlayerModel();
			}
			return _instance;
		}
		
		public function PlayerModel()
		{
		}
		
		public static function getTypeName(type:int):String
		{
			//1:玩家 2.GM 3.引导员 4.内部账号 5.机器人
			var name:String = "玩家";
			switch(type){
				case 1:
					name = "玩家";
					break;
				case 2:
					name = "GM";
					break;
				case 3:
					name = "引导员";
					break;
				case 4:
					name = "内部账号";
					break;
				case 5:
					name = "机器人";
					break;
			}
			return name;
		}
		
		
		
	}
}