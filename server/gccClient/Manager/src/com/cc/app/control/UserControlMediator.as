package com.cc.app.control
{
	import com.cc.app.control.model.UserModel;
	import com.cc.app.control.userview.UserControl;
	import com.cc.core.conf.Config;
	import com.cc.core.constant.OptTypeConstant;
	import com.cc.core.net.GameCCSocket;
	import com.cc.core.util.MessageUtil;
	
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * 用户管理
	 * @author Administrator
	 * 
	 */	
	public class UserControlMediator extends Mediator
	{
		[Inject]
		public var userControl:UserControl;
		
		public function UserControlMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			addContextListener(UserControlEvent.ADD_USER,onAddUserHandler);
			addContextListener(UserControlEvent.DEL_USER,onDelUserHandler);
			addContextListener(UserControlEvent.MODIFY_USER,onModifyUserHandler);
			addCMDListener();
			initView();
		}
		
		override public function onRemove():void
		{
			removeContextListener(UserControlEvent.ADD_USER,onAddUserHandler);
			removeContextListener(UserControlEvent.DEL_USER,onDelUserHandler);
			removeContextListener(UserControlEvent.MODIFY_USER,onModifyUserHandler);
			removeCMDListener();
			super.onRemove();
		}
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_1, onAddUserBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_2, onModifyUserBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_3, onDelUserBackHandler);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_4, onGetUserListBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.SYSTEM_8, onGetRoleListBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_1, onAddUserBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_2, onModifyUserBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_3, onDelUserBackHandler);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_4, onGetUserListBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.SYSTEM_8, onGetRoleListBack);
		}
		
		private function onGetRoleList(e:UserControlEvent = null):void
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
			}
		}
		
		private function onAddUserHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_1, param.toString());
		}
		
		/**
		 * 添加用户返回
		 */		
		private function onAddUserBackHandler(data:ByteArray, mid:int):void
		{
			//添加用户成功后刷新列表
			this.onGetUserList();
		}
		
		private function onModifyUserHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_2, param.toString());
		}
		
		/**
		 * 修改用户返回
		 */		
		private function onModifyUserBackHandler(data:ByteArray, mid:int):void
		{
			//修改用户成功后刷新列表
			MessageUtil.showFaultMessage("修改成功");
			this.onGetUserList();
		}
		
		private function onDelUserHandler(e:UserControlEvent):void
		{
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_3, param.toString());
		}
		
		/**
		 * 删除用户返回
		 */		
		private function onDelUserBackHandler(data:ByteArray, mid:int):void
		{
			this.onGetUserList();
		}
		
		private function initView():void
		{
			this.onGetUserList();
			//			this.onGetRoleList();
		}
		
		private function onGetUserList():void
		{
			GameCCSocket.instance.send(OptTypeConstant.SYSTEM_4);
		}
		
		private function onGetUserListBack(data:ByteArray, mid:int):void
		{
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				userControl.initData(JSON.parse(str));
			}
		}
		
	}
}