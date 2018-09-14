package com.cc.app.player
{
	import com.cc.core.event.GlobalEvent;
	
	import flash.events.Event;
	
	public class PlayerEvent extends GlobalEvent
	{
		/** 查询玩家 */
		public static const SEARCH_PLAYER:String = "com.cc.app.player:searchPlayer";
		/** 封号 */
		public static const FENG_HAO:String = "com.cc.app.player:fenghao";
		/** 解封 */
		public static const JIE_FENG:String = "com.cc.app.player:jifeng";
		/** 删除缓存 */
		public static const DELETE_CACHE:String = "com.cc.app.player:deleteCache";
		
		/**背包物品查询 */
		public static const CHECK_BAGITEM:String = "checkBagItem";
		/** 消费记录查询*/
		public static const CHECK_CONSUME:String= "checkConsume";
		/** 登录日志查询 */
		public static const CHECK_LOGINLOG:String = "checkLoginLog";
		/** 充值记录查询 */
		public static const CHECK_PAY:String = "checkPay";
		/**封停IP*/
		public static const BAN_PLYAER_IP:String = "banPlayerIP";
		/**修改玩家角色*/
		public static const UPDATE_PLYAER_ROLE:String = "updatePlayerRole";
        /**禁言*/
        public static const JIN_YAN:String = "jinYan";
        /**解禁*/
        public static const JIE_JIN:String = "jieJin";
        /**发送邮件*/
        public static const SEND_EMAIL:String = "sendEmail";
        /**邮件查询玩家 */
        public static const EMAIL_SEARCH_PLAYER:String = "com.cc.app.player:emailSearchPlayer";
        /**邮件选择玩家 */
        public static const EMAIL_SELECT_PLAYER:String = "com.cc.app.player:emailSelectPlayer";
		
		/** 玩家相关日志查询 */
		public static const SEARCH_PLAYER_DATA_LOG:String = "search.playerDataLog";
		
		public var searchType:int;
		
		public function PlayerEvent(type:String, data:Object=null, searchOpt:int=0)
		{
			super(type, data);
			this.searchType = searchOpt;
		}
		
		override public function clone():Event{
			return new PlayerEvent(type, data);
		}
	}
}