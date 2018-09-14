package com.cc.app.item
{
	import com.cc.app.item.event.ItemEvent;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class AuditItemMediator extends Mediator
	{
		[Inject]
		public var auditItem:AuditItemView;
		
		public function AuditItemMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			
			addContextListener(ItemEvent.AGREE_SEND_ITEM,onAgreeAudit);
			addContextListener(ItemEvent.REJECT_SEND_ITEM,onRejectAudit);
			addCMDListener();
			
			initView();
		}
		
		private function initView():void
		{
			this.getInfoList();
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			
			removeContextListener(ItemEvent.AGREE_SEND_ITEM,onAgreeAudit);
			removeContextListener(ItemEvent.REJECT_SEND_ITEM,onRejectAudit);
			
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_5, onAgreeAuditBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_6, onAuditListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_5, onAgreeAuditBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_6, onAuditListBack);
		}
		
		private function getInfoList():void {
			var data:Object = new Object();
			data.optType = OptTypeConstant.ITEM_6;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_6, param.toString());
		}
		
		private function onAuditListBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				var appItemList:Array = JSON.parse(String(result.appItemList)) as Array;
				auditItem.setAuditInfo(appItemList);
			}
		}
		
		/**
		 * 同意申请
		 * */
		private function onAgreeAudit(event:ItemEvent):void 
		{
			var data:Object = new Object();
			data.optType = OptTypeConstant.ITEM_5;
//			data.gameSite = MainModel.instance.currentGameSite;
			data.appItemID = event.data.appItemID;
			data.state = event.data.state;
			
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_5, param.toString());
		}
		
		/**
		 * 审核物品返回(同意，拒绝)
		 * */
		private function onAgreeAuditBack(data:ByteArray, mid:int):void 
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("操作成功！");
				}
			}
			this.getInfoList();
		}
		
		/**
		 * 拒绝申请
		 * */
		private function onRejectAudit(event:ItemEvent):void
		{
			var data:Object = new Object();
			data.optType = OptTypeConstant.ITEM_5;
//			data.gameSite = MainModel.instance.currentGameSite;
			data.appItemID = event.data.appItemID;
			data.state = event.data.state;
			
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_5, param.toString());
		}
		
	}
}