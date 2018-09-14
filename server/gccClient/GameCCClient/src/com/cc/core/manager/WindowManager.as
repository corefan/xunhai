package com.cc.core.manager
{
	import com.cc.core.conf.Config;
	import com.cc.core.util.AppUtil;
	import com.cc.core.util.DisplayUtil;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.getQualifiedClassName;
	
	import mx.core.IVisualElement;
	
	import spark.components.Group;
	
	public class WindowManager
	{
		private static var _instance:WindowManager;
		public static function get instance():WindowManager{
			if(_instance == null) _instance = new WindowManager();
			return _instance;
		}
		
		public var rect:Rectangle;
		
		public var windowMap:Object = new Object();
		
		private static var maskWindowList:Vector.<Group> = new Vector.<Group>();
		
		
		public var contentPanel:Group;
		
		public function setup():void{
		}
		
		
		
		/**
		 * 显示对话框(非模式窗口) 是否关闭其他对话框
		 * @win 显示对象
		 * @alone 是否关闭其他窗口
		 * @type 显示层级
		 * @align 对齐方式
		 * @pos 设定坐标
		 **/
		public function show(win:Group, closeAll:Boolean = true, type:* = LayerDef.SCENE, align:int = AlignType.MIDDLE_CENTER, pos:Point = null, mouseMove:Boolean = true, sort:Boolean = true):DisplayObject{
			var className:String = getQualifiedClassName(win);
			var container:Group;
			var isCacheWin:Boolean;
			if(type is String){
				isCacheWin = true;
				container = LayerManager.instance.getLayer(type);
			}else if(type is Group){
				container = type;
			}
			
			if(windowMap[className] != null && sort && isCacheWin){
				currentWindow = windowMap[className] as Group;
				if(currentWindow.stage){
					//如果是最前的窗口，则关闭
					if(container.getChildIndex(currentWindow) == container.numChildren-1){
						hide(currentWindow);
						return win;
					}
					//窗口提到最前
					container.setChildIndex(currentWindow, container.numChildren - 1);
					return win;
				}
			}
			if(closeAll) closeAllWindow();
			if(isCacheWin){
				windowMap[className] = win;
			}
			container.addElement(win);
			currentWindow = win;
			
			if(mouseMove) win.addEventListener(MouseEvent.MOUSE_DOWN, onSortWindowDeep, false, 0, true);
			
			rect = new Rectangle(Config.offsetX, Config.offsetY, Config.width, Config.height);
			DisplayUtil.align(win, rect, align, pos);
			
			return win;
		}
		private function onSortWindowDeep(e:MouseEvent):void{
			var win:Group = e.currentTarget as Group;
			var container:Group = win.parent as Group;
			//窗口提到最前
			container.setElementIndex(win, container.numChildren - 1);
//			container.setChildIndex(win, container.numChildren - 1);
			
			drag(win, e);
		}
		/** 检查是否在舞台 */
		public function checkInStage(winClass:Class):Boolean{
			var win:DisplayObject = AppUtil.getAppByClass(winClass);
			return win.stage != null;
		}
		
		public function hide(win:DisplayObject):void{ 
			if(win == null || win.stage == null) return;
			windowMap[getQualifiedClassName(win)] = null;
			delete windowMap[getQualifiedClassName(win)];
			
			if(win.hasEventListener(MouseEvent.MOUSE_DOWN)) win.removeEventListener(MouseEvent.MOUSE_DOWN, onSortWindowDeep);
			
			removeElementFromParent(win);
		}
		public function onResize(e:Event):void{
			
		}
		/**显示对话框(模式窗口) @param closeOther 是否关闭其他对话框*/
		public function showMask(win:Group, closeAll:Boolean = false, type:String = LayerDef.SCENE_UI, align:int = AlignType.MIDDLE_CENTER, pos:Point = null):DisplayObject{
			var c:Group;
			var container:Group = LayerManager.instance.getLayer(type);
			if(closeAll){
				closeAllWindow(true);
			}
			var aw:int = Config.width;
			var ah:int = Config.height;
			c = new Group();
			c.graphics.beginFill(0, 0.3);
			c.graphics.drawRect(0, 0, aw*5, ah*5);
			c.graphics.endFill();
			
			var className:String = getQualifiedClassName(win);
			c.addElement(win);
			maskWindowList.push(c);
			container.addElement(c);
			
			DisplayUtil.align(win, new Rectangle(Config.offsetX, Config.offsetY, Config.width, Config.height), align, pos);
			
			return win;
		}
		public function hideMask(win:DisplayObject):void{
			var c:Group = win.parent as Group;
			var index:int = maskWindowList.indexOf(c);
			if(index != -1){
				c.removeAllElements();
				removeElementFromParent(c);
			}
			maskWindowList.splice(index, 1);
		}
		private function removeElementFromParent(win:DisplayObject):void{
			var c:Group = win.parent as Group;
			if(c){
				c.removeElement(win as IVisualElement);
			}
		}
		/**关闭所有窗口*/
		public function closeAllWindow(all:Boolean = false):void{
			if(all){
				while(maskWindowList && maskWindowList.length > 0){
					var canvas:Group = maskWindowList.pop() as Group;
					DisplayUtil.removeAllChild(canvas);
					DisplayUtil.removeForParent(canvas);
				} 
			}
			for each(var win:Group in windowMap){
				if(win.stage){
					DisplayUtil.removeForParent(win);
				}
			}
		} 
		
		public function showContent(g:Group):void{
			contentPanel.removeAllElements();
			if(contentPanel == null) return;
			contentPanel.addElement(g); 
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		public var startDragPoint:Point;
		public var currentWindow:DisplayObject;
		public function drag(win:DisplayObject, event:MouseEvent):void{
			currentWindow = win;
			win.stage.addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
			win.stage.addEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
			startDragPoint = new Point(event.stageX - win.x, event.stageY - win.y);
		}
		private function onMouseMove(event:MouseEvent):void{
			var win:DisplayObject = currentWindow;
			if(!win) return;
			var vx:int = int(event.stageX - startDragPoint.x);
			var vy:int = int(event.stageY - startDragPoint.y);
			vx = vx < 0 ? 0 : vx; 
			vy = vy < 0 ? 0 : vy;
			//			vx = vx + win.width > Config.width ? Config.width - win.width : vx;
			//			vy = vy + win.height > Config.height ? Config.height - win.height : vy;
			win.x = vx;
			win.y = vy;
			var vw:int = Config.width - win.width;
			var vh:int = Config.height - win.height;
			
			vw = vw < 0 ? 0 : vw;
			vh = vh < 0 ? 0 : vh;
			
			win.x = vx;
			win.y = vy;
			event.updateAfterEvent();
		}
		private function onMouseUp(e:MouseEvent):void{
			currentWindow = null;
			var s:DisplayObject = e.currentTarget as DisplayObject;
			s.removeEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
			s.removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
		}
	}
}