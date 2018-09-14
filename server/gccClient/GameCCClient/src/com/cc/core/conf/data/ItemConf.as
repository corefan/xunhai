package  com.cc.core.conf.data
{
	public class ItemConf
	{
		public function ItemConf(data:Object = null)
		{
			if(data) parse(data);
		}
		public function parse(data:Object):void{
			for (var s:String in data){
				var key:String = parseProperites(s);
				this[key] = data[s];
			}
		}
		private function parseProperites(s:String):String{
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
		///////////////////////////////////////////////////////////////////////////////
		public var sellPrice:int;
		public var description:String;
		public var name:String;
		public var category:int;
		public var sellType:int;
		public var isOverlap:int;
		public var itemId:int;
		public var needLevel:int;
		public var type:int;
		public var useType:int;
		public var diamondPrice:int;
		public var makeItemId:int;
		public var quality:int;
		public var effectValue:int;
		public var bindDiamPrice:int;
	}
}