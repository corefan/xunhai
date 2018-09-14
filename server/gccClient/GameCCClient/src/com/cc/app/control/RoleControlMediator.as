package com.cc.app.control
{
	import com.cc.app.control.model.UserModel;
	import com.cc.app.control.roleview.RoleControl;
	import com.cc.core.conf.Config;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * 角色管理
	 * @author Administrator
	 * 
	 */	
	public class RoleControlMediator extends Mediator
	{
		[Inject]
		public var roleControl:RoleControl;
		
		public function RoleControlMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(UserControlEvent.ADD_ROLE,onAddRole);
			addContextListener(UserControlEvent.MODIFY_ROLE,onModifyRoleHandler);
			addContextListener(UserControlEvent.DEL_ROLE,onDelRoleHandler);
			addCMDListener();
			initView();
		}
		
		override public function onRemove():void
		{
			removeContextListener(UserControlEvent.ADD_ROLE,onAddRole);
			removeContextListener(UserControlEvent.MODIFY_ROLE,onModifyRoleHandler);
			removeContextListener(UserControlEvent.DEL_ROLE,onDelRoleHandler);
			removeCMDListener();
			super.onRemove();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_5, onAddRoleBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_6, onModifyRoleBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_7, onDelRoleBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_8, onGetRoleListBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_14, onGetQuxianListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_5, onAddRoleBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_6, onModifyRoleBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_7, onDelRoleBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_8, onGetRoleListBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_14, onGetQuxianListBack);
		}
		
		private function initView():void
		{
			this.onGetRoleList();
		}
		
		private function onAddRole(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_5, param.toString());
		}
		
		private function onAddRoleBack(data:ByteArray, mid:int):void
		{
			this.onGetRoleList();
		}
		
		private function onModifyRoleHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_6, param.toString());
		}
		
		/**
		 * 修改角色返回
		 */		
		private function onModifyRoleBackHandler(data:ByteArray, mid:int):void
		{
			//修改角色成功后刷新列表
			this.onGetRoleList();
		}
		
		private function onDelRoleHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_7, param.toString());
		}
		
		/**
		 * 删除用户返回
		 */		
		private function onDelRoleBackHandler(data:ByteArray, mid:int):void
		{
			this.onGetRoleList();
		}
		
		private function onGetRoleList():void
		{
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_8);
		}
		
		/**
		 * 获取角色列表返回
		 */		
		private function onGetRoleListBack(data:ByteArray, mid:int):void
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
				UserModel.instance.roleList = list;
				
				roleControl.initData();
			}
			
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
			}
		}
		
	}
}