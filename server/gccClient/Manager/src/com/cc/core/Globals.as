package com.cc.core
{
	import flash.display.DisplayObject;
	
	import mx.managers.CursorManager;
	
	import org.robotlegs.core.IContext;

	public class Globals
	{
		private static var _instance:Globals;
		public static function get instance():Globals{
			if(_instance == null) _instance = new Globals();
			return _instance;
		}
		public function Globals()
		{
		}
		public var context:IContext;
		
		public var maskCanvas:DisplayObject;
		
		public function set setBusy(flag:Boolean):void{
			if(flag){
				maskCanvas.visible = true;
				CursorManager.setBusyCursor();
			} else {
				maskCanvas.visible = false;
				CursorManager.removeAllCursors();
			}
		}
	}
}