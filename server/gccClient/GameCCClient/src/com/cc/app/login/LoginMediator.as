package com.cc.app.login
{
	import com.cc.app.main.Main;
	import com.cc.app.main.MainModel;
	import com.cc.core.Globals;
	import com.cc.core.conf.Config;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.event.GlobalEvent;
	import com.cc.core.manager.AlignType;
	import com.cc.core.manager.LayerDef;
	import com.cc.core.manager.WindowManager;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.AppUtil;
	import com.cc.core.util.MessageUtil;
	
	import flash.geom.Point;
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class LoginMediator extends Mediator
	{
		[Inject]
		public var login:Login;
		
		public function LoginMediator()
		{
			super();
		}
		
		override public function onRegister():void{
			super.onRegister();
			
			addCMDListener();
			addViewListener(LoginEvent.LOGIN, onLogin);
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.USER_LOGIN, loginResultBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.USER_LOGIN, loginResultBack);
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeViewListener(LoginEvent.LOGIN, onLogin);
			removeCMDListener();
		}
		
		private function onLogin(e:GlobalEvent):void{
			
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.USER_LOGIN, param.toString());
			
		}
		
		private function loginResultBack(data:ByteArray, mid:int):void{
			
			var result:Object = JSON.parse(data.readUTFBytes(data.length));
			if (result.loginFlag == 0) {
				MessageUtil.showFaultMessage("账号或密码错误");
				return;
			}
			
			var roleID:int = result.roleID;
			MainModel.instance.userName = result.userName;
			MainModel.instance.roleID = roleID;
			
			var authorityIDList:Object = JSON.parse(String(result.authorityList));
			var list:Array = JSON.parse(authorityIDList.roleAuthorityList) as Array;
			MainModel.instance.authorityIDList = list;
			
			// 游戏站点
			var gameSiteList:Array = result.gameSiteList;
			var gameSiteAC:Array = new Array();
			for (var i:int=0;i<gameSiteList.length;i++) {
				var obj:Object = new Object();
				var arr:Array = gameSiteList[i].split("|");
				obj.gameSite = arr[0];
				obj.agent = arr[1];
				obj.siteName = arr[2];
				obj.openTime = arr[3]; // 开服时间(秒)
				gameSiteAC.push(obj);
			}
			
			MainModel.instance.gameSiteAC = gameSiteAC;
            MainModel.instance.agent = result.agent;
			
			login.close();
			WindowManager.instance.show(AppUtil.getAppByClass(Main), true, LayerDef.SCENE, AlignType.TOP_LEFT, new Point(0,0), false);
		}
		
	}
}