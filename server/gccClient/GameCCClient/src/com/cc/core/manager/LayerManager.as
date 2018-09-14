
package  com.cc.core.manager{
    import com.cc.core.conf.Config;
    
    import flash.display.DisplayObject;
    import flash.display.Sprite;
    
    import spark.components.Group;

    public class LayerManager {

        private static var _instance:LayerManager;

        private var _parent:Group;
		
		private var layerList:Array = new Array();

        public static function get instance():LayerManager{
            if (_instance == null){
                _instance = new (LayerManager)();
            };
            return (_instance);
        }

        public function setup(container:Group):void{
            this._parent = container;
            this.addLayer(LayerDef.SCENE, true);
            this.addLayer(LayerDef.SCENE_UI, true); 
			
			StageManager.regResize(onResize);
        }
		private function onResize():void{
			for each(var c:Group in layerList){
				c.width = StageManager.stageWidth;
				c.height = StageManager.stageHeight;
			}
			
		}
        public function showLayer(_arg1:String):void{
            var _local2:Sprite = this.getLayer(_arg1);
            if (_local2){
                _local2.x = 0;
            };
        }
        public function hideLayer(_arg1:String):void{
            var _local2:Sprite = this.getLayer(_arg1);
            if (_local2){
                _local2.x = 9999;
            };
        }
        public function addLayer(layerName:String, mouseEnabled:Boolean):void{
            var layer:Group;
            var d:DisplayObject = this._parent.getChildByName(layerName);
            if (d){
                return;
            };
			layer = new Group();
			layer.mouseEnabled = layer.mouseChildren = mouseEnabled;
			layer.name = layerName;
			
			layerList.push(layer);
            this._parent.addElement(layer);
        }
		public function addLayerByView(layer:Group, layerName:String, mouseEnable:Boolean = false):void{
			layer.name = layerName;
			layerList.push(layer);
		}
		
        public function getLayer(value:String):Group{
            if (!value){
                return (null);
            };
			for each(var g:Group in layerList){
				if(g.name == value){
					return g;
				}
			}
			return null;
        }
        public function addChild(dis:DisplayObject, _arg2:String):DisplayObject{
            var _local3:Sprite = this.getLayer(_arg2);
            if (_local3){
                _local3.addChild(dis);
            };
            return (dis);
        }
        public function addChildAt(dis:DisplayObject, _arg2:String, _arg3:uint=0):DisplayObject{
            var _local4:Sprite = this.getLayer(_arg2);
            if (_local4){
                _local4.addChildAt(dis, 0);
            };
            return (dis);
        }
        public function getChildByName(_arg1:String, _arg2:String=null):DisplayObject{
            var _local3:Sprite;
            var _local5:DisplayObject;
            if (_arg2){
                _local3 = this.getLayer(_arg2);
                if (_local3){
                    return (_local3.getChildByName(_arg1));
                };
                return (null);
            };
            var _local4:uint;
            while (_local4 < this._parent.numChildren) {
                _local3 = (this._parent.getChildAt(0) as Sprite);
                if (_local3){
                    _local5 = _local3.getChildByName(_arg1);
                    if (_local5){
                        return (_local5);
                    };
                };
                _local4++;
            };
            return (null);
        }
        public function removeChild(_arg1:DisplayObject):DisplayObject{
            if (!_arg1){
                return (_arg1);
            };
            if (_arg1.parent){
                _arg1.parent.removeChild(_arg1);
            };
            return (_arg1);
        }
        public function setCenter(_arg1:DisplayObject):void{
            if (!_arg1){
                return;
            };
            _arg1.x = int(((this._parent.stage.stageWidth - _arg1.width) / 2));
            _arg1.y = int(((this._parent.stage.stageHeight - _arg1.height) / 2));
        }

    }
}//package com.qdgame.core.manager 
