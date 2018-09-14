package com.cc.app.main
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	public class MainModel
	{
		private static var _instance:MainModel;
		
		/** 用户名字 */
		[Bindable]
		public var userName:String;
		/** 角色 */
		[Bindable]
		public var roleID:int;
		/** 权限列表 */
		[Bindable]
		public var authorityIDList:Array;
		
		/** 服务器站点列表 */
		[Bindable]
		public var gameSiteAC:Array;
		/** 当前服务器站点 */
		public var currentGameSite:String;
		/** 当前服务器名字 */
		public var currentSiteName:String;
		/** 平台列表 */
		public var agent:String;
		/** 当前平台*/
        public var currentAgent:String;
        /** 当前模式(1.单区 2.单个运营商 2.全部运营商)*/
        public var currentModel:int;
		/** 基础物品列表(道具,装备) */
		public var baseItemHash:Dictionary = new Dictionary();
		
		/** 发放物品种类数量 */
		public var itemIDNum:int;
		/** 发放物品列表(道具,装备) */
		public var sendItemList:Array = new Array();
		/** 发送属性列表 */
		public var sendProList:Array = new Array();
		/** 接收者名字 */
		public var receiveNames:String;
        /** 邮件接收者名字 */
        public var emailReceiveNames:String;
        /** 邮件接收者ID */
        public var emailReceiveIds:String;
		
		public function MainModel()
		{
			
		}
		
		public static function get instance():MainModel {
			if (_instance == null) {
				_instance = new MainModel();
			}
			return _instance;
		}
		
		/**
		 * 获取道具,装备基础信息
		 */		
		public function getItemConf(itemID:int):Object
		{
			return baseItemHash[itemID];
		}
	}
}