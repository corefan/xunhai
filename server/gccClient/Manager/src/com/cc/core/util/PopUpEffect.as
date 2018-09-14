package com.cc.core.util
{
	import flash.display.DisplayObject;
	
	import mx.core.IFlexDisplayObject;
	import mx.effects.Blur;
	import mx.events.TweenEvent;
	import mx.managers.PopUpManager;

	/**
	 * 窗口弹出效果
	 * @author Maniac
	 * 
	 */	
	public class PopUpEffect
	{
		public function PopUpEffect()
		{
			
		}
		
		public static function Show(control:IFlexDisplayObject, parent:DisplayObject, modal:Boolean=true):void
		{
			             var mShowEffect:Blur=new Blur();
			             mShowEffect.blurXFrom=255;
			             mShowEffect.blurYFrom=255;
			             mShowEffect.blurXTo=0;
			             mShowEffect.blurYTo=0;
			             mShowEffect.target= control;
			             mShowEffect.duration=300;
//			var mShowEffect:Zoom=new Zoom();
//			mShowEffect.zoomWidthTo=1;
//			mShowEffect.zoomWidthTo=1;
//			mShowEffect.zoomHeightFrom=0;
//			mShowEffect.zoomWidthFrom=0;
//			mShowEffect.target=control;
//			mShowEffect.duration=300;
			PopUpManager.addPopUp(control,parent,modal);
			PopUpManager.centerPopUp(control);
			mShowEffect.play();
			
		}
		
		public static function Hide(control:IFlexDisplayObject):void
		{
			var mHideEffect:Blur=new Blur();
			mHideEffect.blurXFrom=0;
			mHideEffect.blurYFrom=0;
			mHideEffect.blurXTo=255;
			mHideEffect.blurYTo=255;
			mHideEffect.addEventListener(TweenEvent.TWEEN_END, function():void
			{
				PopUpManager.removePopUp(control);
			});
			mHideEffect.duration=200;
			mHideEffect.target=control;
			mHideEffect.play();
		}
	}
}