package com.cc.app.notice
{
	import com.cc.app.main.MainModel;
	import com.cc.app.notice.view.TimingNotice;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * 定时公告
	 */	
	public class TimingNoticeMediator extends Mediator
	{
		[Inject]
		public var timingNotice:TimingNotice;
		
		public function TimingNoticeMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(NoticeEvent.ADD_NOTICE,onAddNotice);
			addContextListener(NoticeEvent.DEL_NOTICE,onDelNotice);
			addCMDListener();
			initView();
		}
		
		private function initView():void
		{
			this.onGetNoticeList();
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeContextListener(NoticeEvent.ADD_NOTICE,onAddNotice);
			removeContextListener(NoticeEvent.DEL_NOTICE,onDelNotice);
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.NOTICE_2, onAddNoticeBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.NOTICE_3, onDelNoticeBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.NOTICE_4, onGetNoticeListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.NOTICE_2, onAddNoticeBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.NOTICE_3, onDelNoticeBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.NOTICE_4, onGetNoticeListBack);
		}
		
		private function onGetNoticeList():void
		{
			var data:Object = new Object();
			data.optType = OptTypeConstant.NOTICE_4;
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.NOTICE_4, param.toString());
		}
		
		private function onGetNoticeListBack(data:ByteArray, mid:int):void
		{
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				timingNotice.initData(result);
			}
		}
		
		private function onAddNotice(e:NoticeEvent):void
		{
			var data:Object = new Object();
			data.optType = OptTypeConstant.NOTICE_2;
			data.content = e.data.content;
			data.frequency = e.data.frequency;
			data.startTime = e.data.startTime;
			data.endTime = e.data.endTime;
            
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
			GameCCSocket.instance.send(OptTypeConstant.NOTICE_2, param.toString());
		}
		
		/**
		 * 添加公告返回
		 */		
		private function onAddNoticeBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("添加公告成功！");
				}
			}
			this.onGetNoticeList();
		}
		
		private function onDelNotice(e:NoticeEvent):void
		{
			var data:Object = new Object();
			data.optType = OptTypeConstant.NOTICE_3;
			data.systemNoticeId = e.data.systemNoticeId;
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
			GameCCSocket.instance.send(OptTypeConstant.NOTICE_3, param.toString());
		}
		
		/**
		 * 删除公告返回
		 */		
		private function onDelNoticeBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("删除公告成功！");
				}
			}
			this.onGetNoticeList();
		}
		
	}
}