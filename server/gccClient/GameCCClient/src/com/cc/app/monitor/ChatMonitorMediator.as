package com.cc.app.monitor
{
	import com.cc.app.monitor.event.ChatMonitorEvent;
	import com.cc.app.monitor.view.ChatMonitor;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/** 聊天监控 */
	public class ChatMonitorMediator extends Mediator
	{
		[Inject]
		public var chatMonitor:ChatMonitor;
		
		public function ChatMonitorMediator()
		{
			super();
		}
		
		override public function onRegister():void{
			super.onRegister();
			addViewListener(ChatMonitorEvent.REQ_CHAT_INFO, onReqChatInfo);
			addCMDListener();
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeViewListener(ChatMonitorEvent.REQ_CHAT_INFO, onReqChatInfo);
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.CHAT_MONITOR_1, onReqChatInfoBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.CHAT_MONITOR_1, onReqChatInfoBack);
		}
		
		/** 请求聊天信息 */
		private function onReqChatInfo(e:ChatMonitorEvent):void {
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.CHAT_MONITOR_1, param.toString());
		}
		
		/** 发送系统公告返回 */
		private function onReqChatInfoBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					chatMonitor.addChatInfo(result);
				}
			}
		}
	}
}