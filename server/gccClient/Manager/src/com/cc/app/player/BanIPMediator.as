package com.cc.app.player
{
	import com.cc.app.main.MainModel;
	import com.cc.app.player.banip.BanIP;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class BanIPMediator extends Mediator
	{
		[Inject]
		public var banIp:BanIP;
		
		public function BanIPMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(PlayerEvent.BAN_PLYAER_IP,onBanIP);
			addCMDListener();
			initView();
		}
		
		private function initView():void
		{
			this.onGetIpList();
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeContextListener(PlayerEvent.BAN_PLYAER_IP,onBanIP);
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.BAN_IP_OPERATE, onBanIPBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.BAN_IP_LIST, onGetIpListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.BAN_IP_OPERATE, onBanIPBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.BAN_IP_LIST, onGetIpListBack);
		}
		
		private function onGetIpList():void
		{
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = OptTypeConstant.BAN_IP_LIST;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.BAN_IP_LIST, param.toString());
		}
		
		private function onGetIpListBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				banIp.initData(result);
			}
		}
		
		/**
		 * 封停/解封IP
		 */
		private function onBanIP(e:PlayerEvent):void 
		{
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = OptTypeConstant.BAN_IP_OPERATE;
			data.ip = e.data.ip;
			data.ipState = e.data.ipState
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.BAN_IP_OPERATE, param.toString());
		}
		
		private function onBanIPBack(data:ByteArray, mid:int):void
		{
			this.onGetIpList();
		}
		
	}
}