package com.cc.app.server
{
	import com.cc.app.server.serverview.ServerControl;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * 服务器管理
	 * @author Administrator
	 * 
	 */	
	public class ServerControlMediator extends Mediator
	{
		[Inject]
		public var serverControl:ServerControl;
		
		public function ServerControlMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(ServerControlEvent.ADD_SERVER,onAddServerHandler);
			addContextListener(ServerControlEvent.DEL_SERVER,onDelServerHandler);
			addContextListener(ServerControlEvent.MODIFY_SERVER,onModifyServerHandler);
			addCMDListener();
			initView();
		}
		
		override public function onRemove():void
		{
			removeContextListener(ServerControlEvent.ADD_SERVER,onAddServerHandler);
			removeContextListener(ServerControlEvent.DEL_SERVER,onDelServerHandler);
			removeContextListener(ServerControlEvent.MODIFY_SERVER,onModifyServerHandler);
			removeCMDListener();
			super.onRemove();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SERVER_1, onAddServerBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SERVER_2, onModifyServerBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SERVER_3, onDelServerBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SERVER_4, onGetServerListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SERVER_1, onAddServerBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SERVER_2, onModifyServerBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SERVER_3, onDelServerBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SERVER_4, onGetServerListBack);
		}
		
		
		private function onAddServerHandler(e:ServerControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SERVER_1, param.toString());
		}
		
		/**
		 * 添加服务器返回
		 */		
		private function onAddServerBackHandler(data:ByteArray, mid:int):void
		{
			//添加服务器成功后刷新列表
			MessageUtil.showFaultMessage("添加成功");
			this.onGetServerList();
		}
		
		private function onModifyServerHandler(e:ServerControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SERVER_2, param.toString());
		}
		
		/**
		 * 修改服务器返回
		 */		
		private function onModifyServerBackHandler(data:ByteArray, mid:int):void
		{
			//修改服务器成功后刷新列表
			MessageUtil.showFaultMessage("修改成功");
			this.onGetServerList();
		}
		
		private function onDelServerHandler(e:ServerControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SERVER_3, param.toString());
		}
		
		/**
		 * 删除服务器返回
		 */		
		private function onDelServerBackHandler(data:ByteArray, mid:int):void
		{
			MessageUtil.showFaultMessage("删除成功");
			this.onGetServerList();
		}
		
		private function initView():void
		{
			this.onGetServerList();
			//			this.onGetRoleList();
		}
		
		private function onGetServerList():void
		{
			GameCCSocket.instance.send(OptTypeConstant.SERVER_4);
		}
		
		private function onGetServerListBack(data:ByteArray, mid:int):void
		{
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				serverControl.initData(JSON.parse(str));
			}
		}
		
	}
}