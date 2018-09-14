
package  com.cc.core.manager{
    import com.cc.core.conf.Config;
    
    import flash.display.MovieClip;
    import flash.display.Stage;
    import flash.display.StageAlign;
    import flash.display.StageScaleMode;
    import flash.events.Event;
    import flash.events.SampleDataEvent;
    import flash.geom.Rectangle;
    import flash.media.Sound;
    import flash.media.SoundChannel;
    
    import __AS3__.vec.Vector;

    public class StageManager {

        public static var stage:Stage;
        public static var fps:int;
        public static var stageWidth:int;
        public static var stageHeight:int;
        public static var stageRect:Rectangle;
        private static var _resizeFuncVec:Vector.<Function> = new Vector.<Function>();
;
        private static var _sound:Sound;
        private static var _soundChannel:SoundChannel;

        public static function setup(s:Stage):void{
            stage = s;
			stageWidth = s.stageWidth;
			stageHeight = s.stageHeight;
            stage.scaleMode = StageScaleMode.NO_SCALE;
            stage.align = StageAlign.TOP_LEFT;
            stage.addEventListener(Event.ACTIVATE, onActivate);
            stage.addEventListener(Event.DEACTIVATE, onDeactivate);
            stage.addEventListener(Event.RESIZE, onResize);
            stageRect = new Rectangle();
            update();
		
        }
        private static function onActivate(_arg1:Event):void{
            try {
                if (_sound != null){
                    _sound.removeEventListener(SampleDataEvent.SAMPLE_DATA, onSampleDataHandler);
                    _soundChannel.stop();
                    _soundChannel = null;
                };
            } catch(e:Error) {
            };
        }
        private static function onDeactivate(_arg1:Event):void{
            try {
                if (_sound == null){
                    _sound = new Sound();
                };
                _sound.addEventListener(SampleDataEvent.SAMPLE_DATA, onSampleDataHandler);
                _soundChannel = _sound.play();
            } catch(e:Error) {
            };
        }
        private static function onSampleDataHandler(_arg1:SampleDataEvent):void{
            _arg1.data.position = (_arg1.data.length = (0x1000 * 4));
        }
        public static function regResize(fun:Function):void{
            if (_resizeFuncVec.indexOf(fun) == -1){
                _resizeFuncVec.push(fun);
            };
        }
        public static function removeResize(fun:Function):void{
            var index:int = _resizeFuncVec.indexOf(fun);
            if (index != -1){
                _resizeFuncVec.splice(index, 1);
            };
        }
        private static function onResize(e:Event):void{
            var fun:Function;
            update();
            for each (fun in _resizeFuncVec) {
                fun();
            };
        }
        private static function update():void{ 
            stageWidth = stage.stageWidth;
            stageHeight = stage.stageHeight;
            fps = stage.frameRate;
            stageRect.x = 0;
            stageRect.y = 0;
            stageRect.width = stageWidth;
            stageRect.height = stageHeight;
			
			Config.width = stageWidth;
			Config.height = stageHeight;
        }

    }
}//package com.qdgame.core.manager 
