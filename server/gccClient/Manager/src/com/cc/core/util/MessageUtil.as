package com.cc.core.util
{
	import com.cc.core.view.Tip;
	
	import mx.controls.Alert;

	public class MessageUtil
	{
		public static function showFaultMessage(msg:String, isHtml:Boolean=false):void{
			if (msg != null && msg != "") {
				Tip.instance.setText(msg, isHtml);
				Alert.show(msg,"温馨提示");
			}
		}
	}
}