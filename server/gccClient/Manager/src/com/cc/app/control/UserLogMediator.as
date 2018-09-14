package com.cc.app.control
{
	import com.cc.app.control.userlog.UserLog;
	import com.cc.core.conf.Config;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * 用户日志查询
	 * @author Administrator
	 * 
	 */	
	public class UserLogMediator extends Mediator
	{
		[Inject]
		public var userLog:UserLog;
		
		public function UserLogMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(UserControlEvent.CHECK_USER_LOG,onCheckUserLog);
			addCMDListener();
//			initView();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.OPT_LOG_1, onCheckAllUserLogBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.OPT_LOG_2, onCheckUserLogBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.OPT_LOG_1, onCheckAllUserLogBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.OPT_LOG_2, onCheckUserLogBack);
		}
		
		private function initView():void
		{
			this.onCheckAllUserLog();
		}
		
		private function onCheckUserLog(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.OPT_LOG_2, param.toString());
		}
		
		/**
		 * 查询单个用户操作返回
		 */		
		private function onCheckUserLogBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				
				userLog.initData(JSON.parse(str));
			}
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeContextListener(UserControlEvent.CHECK_USER_LOG,onCheckUserLog);
		}
		
		private function onCheckAllUserLog():void
		{
			GameCCSocket.instance.send(OptTypeConstant.OPT_LOG_1);
		}
		
		private function onCheckAllUserLogBack(data:ByteArray, mid:int):void
		{
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				
				userLog.initData(JSON.parse(str));
			}else{
				MessageUtil.showFaultMessage("没有查询到数据");
			}
		}
		
	}
}