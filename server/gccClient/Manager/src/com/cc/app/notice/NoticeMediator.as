package com.cc.app.notice
{
	import com.cc.app.main.MainModel;
	import com.cc.app.notice.view.Notice;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/** 系统公告*/
	public class NoticeMediator extends Mediator
	{
		[Inject]
		public var notice:Notice;
		
		public function NoticeMediator()
		{
			super();
		}
		
		override public function onRegister():void{
			super.onRegister();
			addViewListener(NoticeEvent.SEND_NOTICE, onSendNotice);
			addCMDListener();
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeViewListener(NoticeEvent.SEND_NOTICE, onSendNotice);
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.NOTICE_1, onSendNoticeBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.NOTICE_1, onSendNoticeBack);
		}
		
		/** 发送系统公告 */
		private function onSendNotice(e:NoticeEvent):void {
			var data:Object = new Object();
			data.optType = e.data.optType;
			data.content = e.data.content;
			if (MainModel.instance.currentModel == 3) {
				data.agent = MainModel.instance.agent;
				data.gameSite = "";
			} else if (MainModel.instance.currentModel == 2) {
				data.agent = MainModel.instance.currentAgent;
				data.gameSite = "";
			} else {
				if(MainModel.instance.currentGameSite == ""){
					MessageUtil.showFaultMessage("请选择服务器");
					return;
				}
                data.agent = MainModel.instance.currentAgent;
                data.gameSite = MainModel.instance.currentGameSite;
            }
			
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.NOTICE_1, param.toString());
			
		}
		
		/** 发送系统公告返回 */
		private function onSendNoticeBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("发送成功！");
				}
			}
		}
		
	}
}