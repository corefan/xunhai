
package com.cc.core.conf {
    
    import com.cc.core.conf.data.EquipmentConf;
    import com.cc.core.conf.data.ItemConf;
    
    import flash.utils.ByteArray;
    import flash.utils.Dictionary;
    
    import mx.collections.ArrayCollection;
    
    public class ConfigManager {

		/** 道具物品缓存 */ 
		public var itemHash:Dictionary;
		/** 装备物品缓存 */
		public var equipmentHash:Dictionary;
		
        private static var _instance:ConfigManager;

        public function ConfigManager(){
        }
        public static function get instance():ConfigManager{
            if (_instance == null){
                _instance = new (ConfigManager)();
            };
            return (_instance);
        }
		
		public function setup(data:ByteArray):void{
			RegisterData.instance.setup();
			
			data.uncompress();
			data.position = 0;
			var map:Object = data.readObject();
			itemHash = map["item"];
			equipmentHash = map["equipment"];
		}
		
		/**
		 * 获取道具基础信息
		 */		
		public function getItemConf(itemID:int):ItemConf
		{
			return itemHash[itemID];
		}
		
		/**
		 * 获取装备基础信息
		 */		
		public function getEquipmentConf(equipmentID:int):EquipmentConf
		{
			return equipmentHash[equipmentID];
		}
		
		/**
		 * 得到道具列表
		 * */
		public function getItemList():ArrayCollection {
			var list:ArrayCollection = new ArrayCollection();
			for each (var item:ItemConf in itemHash) {
				list.addItem(item);
			}
			return list;
		}
		
		/**
		 * 得到装备列表
		 * */
		public function getEquipmentList():ArrayCollection {
			var list:ArrayCollection = new ArrayCollection();
			for each (var item:EquipmentConf in equipmentHash) {
				list.addItem(item);
			}
			return list;
		}
		
    }
}//package com.qdgame.core.conf 

class ContentBytesInfo {

    public var name:String;
    public var len:uint;

    public function ContentBytesInfo(){
    }
}
