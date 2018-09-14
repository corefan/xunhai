package com.cc.app.item.event
{
	import com.cc.core.event.GlobalEvent;
	
	import flash.events.Event;
	
	/**
	 * 物品事件
	 * */
	public class ItemEvent extends GlobalEvent
	{
		/** 翻页 */
		public static const FANYE:String = "com.cc.app.item:fanye";
		/** 取消选中物品 */
		public static const UN_SELECT_ITEM:String = "com.cc.app.item:unSelectItem";
		/** 物品发放 */
		public static const SEND_ITEM:String = "com.cc.app.item:sendItem";
		/** 物品发放 */
		public static const SEND_ITEM_TO_ALL:String = "com.cc.app.item:sendItemToAll";
		
		public static const SELECT_PLAYER:String = "selectPlayer";
		
		/**待发送物品列表*/
		public static const SEND_ITEM_LIST:String = "sendItemList";
		/**保存待发送物品模板*/
		public static const SAVE_SEND_ITEMINFO:String = "saveSendItemInfo";
		
		/**同意发送物品*/
		public static const AGREE_SEND_ITEM:String = "agreeSendItem";
		/**拒绝发送物品*/
		public static const REJECT_SEND_ITEM:String = "rejectSendItem";
		/**查询物品根据名称*/
		public static const CHECK_ITEM_BYNAME:String = "checkItemByName";
		
		public function ItemEvent(type:String, data:Object=null)
		{
			super(type, data);
		}
		
		override public function clone():Event{
			return new ItemEvent(type, data);
		}
	}
}