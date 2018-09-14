package
{
	import com.cc.app.control.CompetenceControlMediator;
	import com.cc.app.control.RoleControlMediator;
	import com.cc.app.control.UserControlMediator;
	import com.cc.app.control.UserLogMediator;
	import com.cc.app.control.competenceview.CompetenceControl;
	import com.cc.app.control.roleview.RoleControl;
	import com.cc.app.control.userlog.UserLog;
	import com.cc.app.control.userview.UserControl;
	import com.cc.app.dealData.DealData;
	import com.cc.app.dealData.DealDataMediator;
	import com.cc.app.item.AuditItemLogMediator;
	import com.cc.app.item.AuditItemLogView;
	import com.cc.app.item.AuditItemMediator;
	import com.cc.app.item.AuditItemView;
	import com.cc.app.item.Item;
	import com.cc.app.item.ItemMediator;
	import com.cc.app.login.Login;
	import com.cc.app.login.LoginMediator;
	import com.cc.app.monitor.ChatMonitorMediator;
	import com.cc.app.monitor.view.ChatMonitor;
	import com.cc.app.notice.NoticeMediator;
	import com.cc.app.notice.TimingNoticeMediator;
	import com.cc.app.notice.view.Notice;
	import com.cc.app.notice.view.TimingNotice;
	import com.cc.app.player.BanIPMediator;
	import com.cc.app.player.Player;
	import com.cc.app.player.PlayerMediator;
	import com.cc.app.player.SendEmail;
	import com.cc.app.player.SendEmailMediator;
	import com.cc.app.player.banip.BanIP;
	import com.cc.app.server.ServerControlMediator;
	import com.cc.app.server.serverview.ServerControl;
	
	import flash.display.DisplayObjectContainer;
	
	import org.robotlegs.mvcs.Context;
	
	public class GameCCClientContext extends Context
	{
		public function GameCCClientContext(contextView:DisplayObjectContainer=null, autoStartup:Boolean=true, contextName:String="")
		{
			super(contextView, autoStartup, contextName);
		}
		
		override public function startup():void{
		
			mediatorMap.mapView(ServerControl,ServerControlMediator);
			
			mediatorMap.mapView(Login, LoginMediator);
			
			mediatorMap.mapView(Notice, NoticeMediator);
			mediatorMap.mapView(TimingNotice, TimingNoticeMediator);
			
			mediatorMap.mapView(Player, PlayerMediator);
			mediatorMap.mapView(BanIP, BanIPMediator);
			
			mediatorMap.mapView(Item, ItemMediator);
			mediatorMap.mapView(AuditItemView, AuditItemMediator);
			mediatorMap.mapView(AuditItemLogView, AuditItemLogMediator);
			
			mediatorMap.mapView(DealData, DealDataMediator);
			

			mediatorMap.mapView(UserControl,UserControlMediator);
			mediatorMap.mapView(UserLog,UserLogMediator);
			mediatorMap.mapView(RoleControl,RoleControlMediator);
			mediatorMap.mapView(CompetenceControl,CompetenceControlMediator);
            
            mediatorMap.mapView(SendEmail, SendEmailMediator);
			
			mediatorMap.mapView(ChatMonitor, ChatMonitorMediator);
			
			super.startup();
		}
	}
}