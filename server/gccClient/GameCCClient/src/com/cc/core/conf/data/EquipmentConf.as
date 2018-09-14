package com.cc.core.conf.data
{
	public class EquipmentConf
	{
		public function EquipmentConf(data:Object=null)
		{
			if(data) parse(data);
		}
		
		private function parse(data:Object):void
		{
			// TODO Auto Generated method stub
			for (var s:String in data){
				var key:String = parseProperites(s);
				this[key] = data[s];
			}
		}
		
		private function parseProperites(s:String):String
		{
			// TODO Auto Generated method stub
			var pro:String = s.toLowerCase();
			var index:int = pro.indexOf("_");
			while(index != -1){
				var p:String = pro.charAt(index + 1);
				var regExp:RegExp = new RegExp("_" + p);
				pro = pro.replace(regExp, p.toUpperCase());
				index = pro.indexOf("_");
			}
			return pro;
		}
		
		///////////////////////////////////////////////////////////
		public var attack:int;
		public var description:String;
		public var intensifyLife:int;
		public var sellMoney:int;
		public var defence:int;
		public var intensifyDodge:int;
		public var quality:int;
		public var career:int;
		public var type:int;
		public var life:int;
		public var crit:int;
		public var level:int;
		public var dodge:int;
		public var intensifyCrit:int;
		public var equipmentId:int;
		public var addIntExp:int;
		public var intensifyAttack:int;
		public var intensifyDefence:int;
		public var intensifyFactor:int;
		public var name:String;
		public var style:int;
	}
}