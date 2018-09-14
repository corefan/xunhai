package com.cc.app.item
{
	import com.cc.app.main.MainModel;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class AuditItemLogMediator extends Mediator
	{
		[Inject]
		public var auditLogItem:AuditItemLogView;
		
		public function AuditItemLogMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			
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
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_9, onGetApplyLogBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_9, onGetApplyLogBack);
		}
		
		private function getInfoList():void {
			var data:Object = new Object();
			data.agent = MainModel.instance.currentAgent;
			data.optType = OptTypeConstant.ITEM_9;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_9, param.toString());
		}
		
		private function onGetApplyLogBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				var appItemList:Array = JSON.parse(String(result.appItemList)) as Array;
				auditLogItem.setAuditInfo(appItemList);
			}
		}
		
	}
}