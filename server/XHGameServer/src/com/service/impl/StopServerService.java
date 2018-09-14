package com.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ChatConstant;
import com.core.GameServer;
import com.domain.MessageObj;
import com.domain.chat.Notice;
import com.message.ChatProto.ParamType;
import com.message.LoginProto.S_StopServer;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IChatService;
import com.service.IGameConfigCacheService;
import com.util.LogUtil;

/**
 * 停服Service接口实现
 * @author ken
 *
 */
public class StopServerService {

	public void handleStopServer(int stopMin,  int endStopMin) {
		
		try {
			//每1秒钟通知停服
			StopServerNoticeTask task = new StopServerNoticeTask();
			int second = stopMin * 60 + 1;
			if(second < 6){
				second = 6;
			}
			task.setLastTime(second);
			task.setEndStopMin(endStopMin);
			
			ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
			exec.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
		} catch (Exception e) {
			LogUtil.error("停服异常：", e);
		}
	}
	
	
	/**
	 * 推送停服倒计时
	 */
	private void sendStopServerNotice(int lastTime, int endStopMin){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IChatService chatService = serviceCollection.getChatService();		
		
		String timeStr = "";
		if(lastTime >= 600){
			//>=10分钟  每隔十分钟通知
			if(lastTime % 600 != 0) return;
			timeStr = lastTime / 600 * 10 + "分钟";
		}else if(lastTime >= 60 && lastTime <= 300){
			//五分钟内   每隔一分钟通知
			if(lastTime % 60 != 0) return;
			timeStr = lastTime / 60 + "分钟";
			
			if(lastTime == 300){
				//停服五分钟前设置维护结束时间
				if(endStopMin > 5){
					this.updateEndStopDate(endStopMin);	
				}
			}
		}else if(lastTime == 10){
			timeStr = lastTime +"秒";
		}else if(lastTime <= 5){
			timeStr = lastTime +"秒";
		}else{
			return;
		}
		
		System.out.println("服务器将在"+timeStr+"后停服维护！请做好下线准备");
		
		List<Notice> paramList = new ArrayList<Notice>();			
		Notice notice1 = new Notice(ParamType.PARAM, 0, 0, timeStr);
		paramList.add(notice1);			
		chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_19, paramList, gameSocketService.getOnLinePlayerIDList());
	}
	
	/**
	 * 设置维护结束时间
	 */
	private void updateEndStopDate(int endStopMin){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IGameConfigCacheService gameConfigCacheService = serviceCollection.getGameCfgCacheService();
		
		Date endDate = DateService.addDateByType(DateService.getCurrentUtilDate(), Calendar.MINUTE, endStopMin + 5);
		String endDateStr = DateService.dateFormat(endDate);
		
		gameConfigCacheService.updateEndStopDate(endDateStr);
	}
	
	/**
	 * 推送服务已经关闭提示
	 */
	private void sendServerStopedNotice(int endStopMin) {
		Date endDate = DateService.addDateByType(DateService.getCurrentUtilDate(), Calendar.MINUTE, endStopMin);
		
		S_StopServer.Builder builder = S_StopServer.newBuilder();
		
		builder.setEndStopTime(endDate.getTime());
		MessageObj msg = new MessageObj(MessageID.S_StopServer_VALUE, builder.build().toByteArray());
		GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();
		gameSocketService.sendDataToAll(msg);
	}
	
	
	/** 停服倒计时通知Task */
	class StopServerNoticeTask implements Runnable {
		private int lastTime = 301; //默认五分钟后停服
		private int endStopMin = 5; //维护时间
		public void run() {
			lastTime--;
			if(lastTime>0){
				sendStopServerNotice(lastTime, endStopMin);
			}else{
				sendServerStopedNotice(endStopMin);
				GameServer.getInstance().stopServer();
				System.exit(0);
			}
		}
		public void setLastTime(int lastTime) {
			this.lastTime = lastTime;
		}
		public void setEndStopMin(int endStopMin) {
			this.endStopMin = endStopMin;
		}
		
	}

}
