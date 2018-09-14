
package com.cc.core.util{
    import com.cc.core.manager.AlignType;
    import com.cc.core.manager.StageManager;
    import com.cc.core.manager.Styles;
    
    import flash.display.Bitmap;
    import flash.display.BitmapData;
    import flash.display.DisplayObject;
    import flash.display.DisplayObjectContainer;
    import flash.display.MovieClip;
    import flash.display.PixelSnapping;
    import flash.display.Shape;
    import flash.display.Sprite;
    import flash.filters.ColorMatrixFilter;
    import flash.geom.ColorTransform;
    import flash.geom.Matrix;
    import flash.geom.Point;
    import flash.geom.Rectangle;
    import flash.text.TextField;
    import flash.text.TextFormat;
    
    public class DisplayUtil {

        public static function underlineTextField(txt:TextField, _arg2:Boolean=true):void{
            var _local3:TextFormat = txt.defaultTextFormat;
            _local3.underline = _arg2;
            txt.defaultTextFormat = _local3;
            if (txt.text){
                txt.setTextFormat(_local3, 0, txt.text.length);
            };
        }
        public static function getTextField(_arg1:int, _arg2:int, _arg3:String="", _arg4:int=12, _arg5:String="Microsoft YaHei,微软雅黑", _arg6:String="left", _arg7:Boolean=false, _arg8:uint=0xFFFFFF):TextField{
            var _local9:TextField;
            _local9 = new TextField();
            var _local10:TextFormat = new TextFormat(Styles.fontName);
            _local10.size = _arg4;
            _local10.font = _arg5;
            _local10.color = _arg8;
            _local10.align = _arg6;
            _local9.defaultTextFormat = _local10;
            _local9.width = _arg1;
            _local9.height = _arg2;
            _local9.selectable = (_local9.mouseEnabled = (_local9.mouseWheelEnabled = false));
            _local9.autoSize = _arg6;
            _local9.wordWrap = (_local9.multiline = _arg7);
            _local9.text = _arg3;
            return (_local9);
        }
        public static function swapDisplayObject(_arg1:String, _arg2:DisplayObjectContainer, _arg3:DisplayObject):DisplayObject{
            var _local4:DisplayObject = _arg2.getChildByName(_arg1);
            if (_local4 == null){
                return (null);
            };
            var _local5:int = _arg2.getChildIndex(_local4);
            _arg3.x = _local4.x;
            _arg3.y = _local4.y;
            _arg3.width = _local4.width;
            _arg3.height = _local4.height;
            _arg2.addChildAt(_arg3, _local5);
            DisplayUtil.removeForParent(_local4);
            return (_arg3);
        }
        public static function grayDisplayObject(_arg1:DisplayObjectContainer, _arg2:Boolean=false):void{
            if (_arg2){
                _arg1.filters = [];
            } else {
                _arg1.filters = [new ColorMatrixFilter([1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0])];
            };
        }
        public static function stopAllMovieClip(doc:DisplayObjectContainer, _arg2:uint=0):void{
            var _local5:DisplayObjectContainer;
            var _local3:MovieClip = (doc as MovieClip);
            if (_local3 != null){
                if (_arg2 != 0){
                    _local3.gotoAndStop(_arg2);
                } else {
                    _local3.gotoAndStop(1);
                };
                _local3 = null;
            };
            var _local4:int = (doc.numChildren - 1);
            if (_local4 < 0){
                return;
            };
            var _local6:int = _local4;
            while (_local6 >= 0) {
                _local5 = (doc.getChildAt(_local6) as DisplayObjectContainer);
                if (_local5 != null){
                    stopAllMovieClip(_local5, _arg2);
                };
                _local6--;
            };
        }
        public static function playAllMovieClip(doc:DisplayObjectContainer):void{
            var _local4:DisplayObjectContainer;
            var _local2:MovieClip = (doc as MovieClip);
            if (_local2 != null){
                _local2.gotoAndPlay(1);
                _local2 = null;
            };
            var _local3:int = (doc.numChildren - 1);
            if (_local3 < 0){
                return;
            };
            var _local5:int = _local3;
            while (_local5 >= 0) {
                _local4 = (doc.getChildAt(_local5) as DisplayObjectContainer);
                if (_local4 != null){
                    playAllMovieClip(_local4);
                };
                _local5--;
            };
        }
        public static function removeAllChild(doc:DisplayObjectContainer):void{
            var _local2:DisplayObjectContainer;
            while (doc.numChildren > 0) {
                doc.removeChildAt(0);
            };
        }
        public static function playEndAndRemove(movie:MovieClip, fun:Function=null):void{
            var mc:MovieClip = movie;
            var removeCallBack:Function = fun;
            mc.gotoAndPlay(1);
            mc.addFrameScript((mc.totalFrames - 1), function ():void{
                mc.addFrameScript((mc.totalFrames - 1), null);
                mc.stop();
                DisplayUtil.removeForParent(mc);
                mc = null;
                if (removeCallBack != null){
                    removeCallBack.apply();
                };
                removeCallBack = null;
            });
        }
        public static function playEndAndStop(movie:MovieClip, fun:Function=null):void{
            var mc:* = movie;
            var callBack:Function = fun;
            mc.gotoAndPlay(1);
            mc.addFrameScript((mc.totalFrames - 1), function ():void{
                mc.addFrameScript((mc.totalFrames - 1), null);
                mc.stop();
                if (callBack != null){
                    callBack.apply();
                };
                callBack = null;
                mc = null;
            });
        }
        public static function removeForParent(dis:DisplayObject):void{
            if (dis == null){
                return;
            };
            if (dis.parent == null){
                return;
            };
            dis.parent.removeChild(dis);
        }
        public static function hasParent(dis:DisplayObject):Boolean{
            if (dis.parent == null){
                return (false);
            };
            return (dis.parent.contains(dis));
        }
        public static function localToLocal(dis1:DisplayObject, dis2:DisplayObject, p:Point=null):Point{
            if (p == null){
                p = new Point(0, 0);
            };
            p = dis1.localToGlobal(p);
            p = dis2.globalToLocal(p);
            return (p);
        }
        public static function align(dis:DisplayObject, rect:Rectangle=null, type:int=0, p:Point=null):void{
            if (dis == null){
                return;
            };
            if (rect == null){
                rect = new Rectangle(0, 0, StageManager.stageWidth, StageManager.stageHeight);
            };
            if (p){
                rect.offsetPoint(p);
            };
            var drect:Rectangle = dis.getRect(dis);
            var vw:int = (rect.width - dis.width);
            var vh:int = (rect.height - dis.height);
            switch (type){
                case AlignType.TOP_LEFT:
                    dis.x = rect.x;
                    dis.y = rect.y;
                    break;
                case AlignType.TOP_CENTER:
                    dis.x = ((rect.x + (vw / 2)) - drect.x);
                    dis.y = rect.y;
                    break;
                case AlignType.TOP_RIGHT:
                    dis.x = ((rect.x + vw) - drect.x);
                    dis.y = rect.y;
                    break;
                case AlignType.MIDDLE_LEFT:
                    dis.x = rect.x;
                    dis.y = ((rect.y + (vh / 2)) - drect.x);
                    break;
                case AlignType.MIDDLE_CENTER:
                    dis.x = ((rect.x + (vw / 2)) - drect.x);
                    dis.y = ((rect.y + (vh / 2)) - drect.y);
                    break;
                case AlignType.MIDDLE_RIGHT:
                    dis.x = ((rect.x + vw) - drect.x);
                    dis.y = ((rect.y + (vh / 2)) - drect.y);
                    break;
                case AlignType.BOTTOM_LEFT:
                    dis.x = rect.x;
                    dis.y = ((rect.y + vh) - drect.y);
                    break;
                case AlignType.BOTTOM_CENTER:
                    dis.x = ((rect.x + (vw / 2)) - drect.x);
                    dis.y = ((rect.y + vh) - drect.y);
                    break;
                case AlignType.BOTTOM_RIGHT:
                    dis.x = ((rect.x + vw) - drect.x);
                    dis.y = ((rect.y + vh) - drect.y);
                    break;
            };
        }
        public static function alignDisplay(dis:DisplayObject, rect:Rectangle, type:int=-1, _arg4:Point=null):void{
            if (type == -1){
                type = AlignType.MIDDLE_CENTER;
            };
            if (rect == null){
                rect = new Rectangle(0, 0, StageManager.stageWidth, StageManager.stageHeight);
            };
            if (_arg4){
                rect.offsetPoint(_arg4);
            };
            var drect:Rectangle = dis.getRect(dis);
            var vw:int = (rect.width - dis.width);
            var vh:int = (rect.height - dis.height);
            switch (type){
                case AlignType.TOP_LEFT:
                    dis.x = rect.x;
                    dis.y = rect.y;
                    break;
                case AlignType.TOP_CENTER:
                    dis.x = ((rect.x + (vw / 2)) - drect.x);
                    dis.y = rect.y;
                    break;
                case AlignType.TOP_RIGHT:
                    dis.x = ((rect.x + vw) - drect.x);
                    dis.y = rect.y;
                    break;
                case AlignType.MIDDLE_LEFT:
                    dis.x = rect.x;
                    dis.y = ((rect.y + (vh / 2)) - drect.x);
                    break;
                case AlignType.MIDDLE_CENTER:
                    dis.x = ((rect.x + (vw / 2)) - drect.x);
                    dis.y = ((rect.y + (vh / 2)) - drect.y);
                    break;
                case AlignType.MIDDLE_RIGHT:
                    dis.x = ((rect.x + vw) - drect.x);
                    dis.y = ((rect.y + (vh / 2)) - drect.y);
                    break;
                case AlignType.BOTTOM_LEFT:
                    dis.x = rect.x;
                    dis.y = ((rect.y + vh) - drect.y);
                    break;
                case AlignType.BOTTOM_CENTER:
                    dis.x = ((rect.x + (vw / 2)) - drect.x);
                    dis.y = ((rect.y + vh) - drect.y);
                    break;
                case AlignType.BOTTOM_RIGHT:
                    dis.x = ((rect.x + vw) - drect.x);
                    dis.y = ((rect.y + vh) - drect.y);
                    break;
            };
        }
        public static function alignPoint(dis:DisplayObject, rect:Rectangle, type:int=-1, _arg4:Point=null):Point{
            if (type == -1){
                type = AlignType.MIDDLE_CENTER;
            };
            if (rect == null){
                rect = new Rectangle(0, 0, StageManager.stageWidth, StageManager.stageHeight);
            };
            if (_arg4){
                rect.offsetPoint(_arg4);
            };
            var drect:Rectangle = dis.getRect(dis);
            var vw:int = (rect.width - dis.width);
            var vh:int = (rect.height - dis.height);
            var _local8:Point = new Point();
            switch (type){
                case AlignType.TOP_LEFT:
                    _local8.x = rect.x;
                    _local8.y = rect.y;
                    break;
                case AlignType.TOP_CENTER:
                    _local8.x = ((rect.x + (vw / 2)) - drect.x);
                    _local8.y = rect.y;
                    break;
                case AlignType.TOP_RIGHT:
                    _local8.x = ((rect.x + vw) - drect.x);
                    _local8.y = rect.y;
                    break;
                case AlignType.MIDDLE_LEFT:
                    _local8.x = rect.x;
                    _local8.y = ((rect.y + (vh / 2)) - drect.x);
                    break;
                case AlignType.MIDDLE_CENTER:
                    _local8.x = ((rect.x + (vw / 2)) - drect.x);
                    _local8.y = ((rect.y + (vh / 2)) - drect.y);
                    break;
                case AlignType.MIDDLE_RIGHT:
                    _local8.x = ((rect.x + vw) - drect.x);
                    _local8.y = ((rect.y + (vh / 2)) - drect.y);
                    break;
                case AlignType.BOTTOM_LEFT:
                    _local8.x = rect.x;
                    _local8.y = ((rect.y + vh) - drect.y);
                    break;
                case AlignType.BOTTOM_CENTER:
                    _local8.x = ((rect.x + (vw / 2)) - drect.x);
                    _local8.y = ((rect.y + vh) - drect.y);
                    break;
                case AlignType.BOTTOM_RIGHT:
                    _local8.x = ((rect.x + vw) - drect.x);
                    _local8.y = ((rect.y + vh) - drect.y);
                    break;
            };
            return (_local8);
        }
        public static function fillColor(dis:DisplayObject, _arg2:uint):void{
            var _local3:ColorTransform = new ColorTransform();
            _local3.color = _arg2;
            dis.transform.colorTransform = _local3;
        }
        public static function getColor(dis:DisplayObject, _arg2:uint=0, _arg3:uint=0, _arg4:Boolean=false):uint{
            var _local5:BitmapData = new BitmapData(dis.width, dis.height);
            _local5.draw(dis);
            var _local6:uint = (_arg4) ? _local5.getPixel32(int(_arg2), int(_arg3)) : _local5.getPixel(int(_arg2), int(_arg3));
            _local5.dispose();
            return (_local6);
        }
        public static function uniformScale(dis:DisplayObject, _arg2:Number):void{
            if (dis.width >= dis.height){
                dis.width = _arg2;
                dis.scaleY = dis.scaleX;
            } else {
                dis.height = _arg2;
                dis.scaleX = dis.scaleY;
            };
        }
        public static function copyDisplayAsBmp(dis:DisplayObject, _arg2:Boolean=true):Bitmap{
            var _local3:Number;
            var _local4:Number;
            _local4 = dis.scaleY;
            _local3 = dis.scaleX;
            var _local5:BitmapData = new BitmapData(dis.width, dis.height, true, 0);
            var _local6:Rectangle = dis.getRect(dis);
            var _local7:Matrix = new Matrix();
            if (_local3 < 0){
                dis.scaleX = -(dis.scaleX);
            };
            if (_local4 < 0){
                dis.scaleY = -(dis.scaleY);
            };
            _local7.createBox(dis.scaleX, dis.scaleY, 0, (-(_local6.x) * dis.scaleX), (-(_local6.y) * dis.scaleY));
            _local5.draw(dis, _local7);
            dis.scaleX = _local3;
            dis.scaleY = _local4;
            var _local8:Bitmap = new Bitmap(_local5, PixelSnapping.AUTO, _arg2);
            if (_local3 < 0){
                _local8.scaleX = -1;
            };
            if (_local4 < 0){
                _local8.scaleY = -1;
            };
            _local8.x = (_local6.x * dis.scaleX);
            _local8.y = (_local6.y * dis.scaleY);
            return (_local8);
        }
        public static function enableSprite(dis:Sprite, _arg2:Boolean):void{
            dis.mouseEnabled = (dis.mouseChildren = _arg2);
        }
        public static function getRect(_arg1:int, _arg2:int, _arg3:int, _arg4:Number=1):Shape{
            var _local5:Shape = new Shape();
            _local5.graphics.beginFill(_arg3, _arg4);
            _local5.graphics.drawRect(0, 0, _arg1, _arg2);
            _local5.graphics.endFill();
            return (_local5);
        }

    }
}//package com.qdgame.core.util 
