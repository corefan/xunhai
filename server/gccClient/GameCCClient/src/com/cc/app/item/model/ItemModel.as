package com.cc.app.item.model
{
	public class ItemModel
	{
		private var _items:Array;
		
		private static var _instance:ItemModel;
		
		public static function get instance():ItemModel
		{
			if(_instance == null){
				_instance = new ItemModel();
			}
			return _instance;
		}
		
		public function ItemModel()
		{
		}
		
		public function set itemConfList(value:Array):void
		{
			_items = value;
		}
		
		public function get itemConfList():Array
		{
			return _items;
		}
	}
}