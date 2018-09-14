package com.cc.app.control
{
	import com.cc.app.control.competenceview.CompetenceControl;
	import com.cc.app.control.model.UserModel;
	import com.cc.core.conf.Config;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.events.Event;
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * 权限管理
	 * @author Administrator
	 * 
	 */	
	public class CompetenceControlMediator extends Mediator
	{
		[Inject]
		public var competenceControl:CompetenceControl;
		
		public function CompetenceControlMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(UserControlEvent.ADD_COMPETENCE,onAddCompetence);
			addContextListener(UserControlEvent.MODIFY_COMPETENCE,onModifyCompetenceHandler);
			addContextListener(UserControlEvent.DEL_COMPETENCE,onDelCompetenceHandler);
			
			addCMDListener();
			
			initView();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_11, onAddCompetenceBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_12, onModifyCompetenceBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_13, onDelCompetenceBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_14, onGetQuxianListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_11, onAddCompetenceBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_12, onModifyCompetenceBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_13, onDelCompetenceBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_14, onGetQuxianListBack);
		}
		
		override public function onRemove():void
		{
			removeContextListener(UserControlEvent.ADD_COMPETENCE,onAddCompetence);
			removeContextListener(UserControlEvent.MODIFY_COMPETENCE,onModifyCompetenceHandler);
			removeContextListener(UserControlEvent.DEL_COMPETENCE,onDelCompetenceHandler);
			removeCMDListener();
			super.onRemove();
		}
		
		private function initView():void
		{
			this.onGetQuxianList();
			
		}
		
		private function onAddCompetence(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_11, param.toString());
		}
		
		private function onAddCompetenceBack(data:ByteArray, mid:int):void
		{
			this.onGetQuxianList();
		}
		
		private function onModifyCompetenceHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_12, param.toString());
		}
		
		/**
		 * 修改权限返回
		 */		
		private function onModifyCompetenceBackHandler(data:ByteArray, mid:int):void
		{
			//修改权限成功后刷新列表
			this.onGetQuxianList();
		}
		
		private function onDelCompetenceHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_13, param.toString());
		}
		
		/**
		 * 删除用户返回
		 */		
		private function onDelCompetenceBackHandler(data:ByteArray, mid:int):void
		{
			this.onGetQuxianList();
		}
		
		private function onGetQuxianList():void
		{
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_14);
		}
		
		private function onGetQuxianListBack(data:ByteArray, mid:int):void
		{
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				
				var obj:Object = JSON.parse(str);
				var arr:Array = JSON.parse(obj.dataList) as Array;
				
				var list:ArrayCollection = new ArrayCollection();
				for each(obj in arr){
					list.addItem(obj);
				}
				UserModel.instance.competenceList = list;
				competenceControl.initData();
			}
		}
		
	}
}