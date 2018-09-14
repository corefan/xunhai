package com.cc.app.item
{
	import com.cc.app.main.MainModel;
	import com.cc.app.player.PlayerEvent;
	import com.cc.app.player.PlayerService;
	import com.cc.core.conf.Config;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.util.MessageUtil;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class PlayerListMediator extends Mediator
	{
		[Inject]
		public var playerList:PlayerList;
		
		public function PlayerListMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(PlayerEvent.SEARCH_PLAYER, onSearchPlayer);
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeContextListener(PlayerEvent.SEARCH_PLAYER, onSearchPlayer);
		}
		
		private function getService():HTTPService
		{
			var service:HTTPService = PlayerService.instance.playerService;
			service.url = "http://"+Config.host+":"+Config.port+"/player";
			return service;
		}
		
		/**
		 * 查询玩家
		 * */
		private function onSearchPlayer(e:PlayerEvent):void{
			var data:Object = e.data;
			data.optType = OptTypeConstant.PLAYER_2;
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			var token:AsyncToken = getService().send(data);
			token.addResponder(new mx.rpc.Responder(onSearchPlayerBack, MessageUtil.showFaultMessage));
		}
		
		/**
		 * 查询玩家返回
		 * */
		private function onSearchPlayerBack(event:ResultEvent):void{
			if (event.result) {
				var data:String = String(event.result);
				data = StringUtil.trim(data);
				var result:Object = JSON.parse(String(data));
				playerList.onSearchPlayer(result);
			}
		}
		
	}
}