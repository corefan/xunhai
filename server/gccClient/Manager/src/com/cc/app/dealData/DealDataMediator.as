package com.cc.app.dealData
{
	import com.cc.app.main.MainModel;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/** 处理数据 */
	public class DealDataMediator extends Mediator
	{
		[Inject]
		public var dealData:DealData;
		
		public function DealDataMediator()
		{
			super();
		}
		
		override public function onRegister():void{
			super.onRegister();
			addViewListener(DealDataEvent.REFRESH_BASE_CACHE, onRefreshBaseCache);
			addViewListener(DealDataEvent.REFRESH_CONFIG_CACHE, onRefreshConfigCache);
			addViewListener(DealDataEvent.SYN_CACHE_DATA, onSynCacheData);
			addViewListener(DealDataEvent.HOT_UPDATE_CLASS, onHotupdateClass);
			addViewListener(DealDataEvent.STOP_SERVER, stopServer);
			addCMDListener();
		}
		
		override public function onRemove():void
		{
			super.onRemove();
			removeViewListener(DealDataEvent.REFRESH_BASE_CACHE, onRefreshBaseCache);
			removeViewListener(DealDataEvent.REFRESH_CONFIG_CACHE, onRefreshConfigCache);
			removeViewListener(DealDataEvent.SYN_CACHE_DATA, onSynCacheData);
			removeViewListener(DealDataEvent.HOT_UPDATE_CLASS, onHotupdateClass);
			removeViewListener(DealDataEvent.STOP_SERVER, stopServer);
			removeCMDListener();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.DEAL_DATA_1, onRefreshBaseCacheBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.DEAL_DATA_2, onRefreshConfigCacheBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.DEAL_DATA_3, onSynCacheDataBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.DEAL_DATA_8, onHotupdateClassBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.DEAL_DATA_10, onStopServerBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.DEAL_DATA_1, onRefreshBaseCacheBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.DEAL_DATA_2, onRefreshConfigCacheBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.DEAL_DATA_3, onSynCacheDataBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.DEAL_DATA_8, onHotupdateClassBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.DEAL_DATA_10, onStopServerBack);
		}
		
		/** 热更新class */
		private function onHotupdateClass(e:DealDataEvent):void {
			var data:Object = new Object();
			data.optType = e.data;
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
			GameCCSocket.instance.send(OptTypeConstant.DEAL_DATA_8, param.toString());
			
		}
		
		/** 停服 */
		private function stopServer(e:DealDataEvent):void {
			var data:Object = new Object();
			data = e.data;
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
			GameCCSocket.instance.send(OptTypeConstant.DEAL_DATA_10, param.toString());
			
		}
		
		/** 热更新返回 */
		private function onHotupdateClassBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("热更新完成");
				}
			}
		}
		
		/** 停服返回 */
		private function onStopServerBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("停服完成");
				}
			}
		}
		
		/** 同步缓存数据 */
		private function onSynCacheData(e:DealDataEvent):void {
			var data:Object = new Object();
			data.optType = e.data;
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
			GameCCSocket.instance.send(OptTypeConstant.DEAL_DATA_3, param.toString());
			
		}
		
		/** 同步缓存数据返回 */
		private function onSynCacheDataBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("数据同步完成");
				}
			}
		}
		
		/** 刷新基础缓存 */
		private function onRefreshBaseCache(e:DealDataEvent):void {
			
			var data:Object = new Object();
			data.optType = e.data;
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
			GameCCSocket.instance.send(OptTypeConstant.DEAL_DATA_1, param.toString());
			
		}
		
		/** 刷新基础缓存返回 */
		private function onRefreshBaseCacheBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("刷新基础数据完成");
				}
			}
		}
		
		/** 刷新配置缓存 */
		private function onRefreshConfigCache(e:DealDataEvent):void {
			
			var data:Object = new Object();
			data.optType = e.data;
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
			GameCCSocket.instance.send(OptTypeConstant.DEAL_DATA_2, param.toString());
			
		}
		
		/** 刷新基础缓存返回 */
		private function onRefreshConfigCacheBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("刷新配置数据完成");
				}
			}
		}
		
	}
}