package com.cc.core.conf
{
	
	
	import com.cc.core.conf.data.EquipmentConf;
	import com.cc.core.conf.data.ItemConf;
	
	import flash.net.registerClassAlias;
	
	
	public class RegisterData
	{
		private static var _instance:RegisterData;
		public static function get instance():RegisterData{
			if (_instance == null){
				_instance = new (RegisterData)();
			};
			return (_instance);
		}
		public function RegisterData()
		{
		}
		public function setup():void{
			registerClassAlias("data.item", ItemConf);
			registerClassAlias("data.equipment", EquipmentConf);
		}
	}
}