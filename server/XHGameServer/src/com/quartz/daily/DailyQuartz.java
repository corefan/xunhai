package com.quartz.daily;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.DateService;
import com.common.GameContext;
import com.common.LogoutCacheService;
import com.common.ServiceCollection;
import com.service.IActivityService;
import com.service.IMarketService;
import com.service.IPlayerService;
import com.service.ISignService;
import com.service.ITiantiService;
import com.util.LogUtil;

/**
 * 每日调度
 * @author ken
 *
 */
public class DailyQuartz implements Job {


	public DailyQuartz() {
	}

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {

		common(job.getJobDetail().getKey().getName());

	}

	private void common(String jobName) {
		if (jobName.equalsIgnoreCase("dailyOneDetail")) {
			dealDailyOneDetail();
		} else if (jobName.equalsIgnoreCase("dailyTwoDetail")) {
			dealDailyTwoDetail();
		} else if (jobName.equalsIgnoreCase("dailyThreeDetail")) {
			dealDailyThreeDetail();

		} else if (jobName.equalsIgnoreCase("dailyFourDetail")) {
			dealDailyFourDetail();
			
		}
		else if (jobName.equalsIgnoreCase("dailySevenDetail")) {
			// 清理数据
			dealDailySevenDetail();
		}
	}

	/**
	 * 日结后 (0点 1分 1-6秒)
	 * */
	private void dealDailyOneDetail() {

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();

		IPlayerService playerService = serviceCollection.getPlayerService();		
		ISignService signService = serviceCollection.getSignService();		
		IMarketService marketService = serviceCollection.getMarketService();
		IActivityService activityService = serviceCollection.getActivityService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		
		try {			
			activityService.quartzDaily();
		} catch (Exception e1) {
			LogUtil.error("异常：", e1);
		}
		
		try {
			// 更新玩家每日数据等
			playerService.handlePlayerInfoForDaily();
		} catch (Exception e1) {
			LogUtil.error("异常：", e1);
		}				
		
		try {
			//天梯检测日期
			tiantiService.checkDate();
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		
		try {
			//玩家签到数据(每日清签到状态)
			signService.quartzDaily();
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		

		try{
			// 玩家商城数据			
			marketService.quartzDaily();
		} catch (Exception e1) {
			LogUtil.error("异常：", e1);
		}		
	}

	/**
	 * 日结后(0点 1分 12-19秒)
	 * */
	private void dealDailyTwoDetail() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IActivityService activityService = serviceCollection.getActivityService();
		
		try{
			// 日结后清理玩家7天累计充值数据
			activityService.quartzDailyAfter();
		} catch (Exception e1) {
			LogUtil.error("异常：", e1);
		}		
		
		try{
			// 清理家族数据
			serviceCollection.getFamilyService().quartzDaily();
		}catch (Exception e) {
			LogUtil.error("异常：", e);
		}	
		
		
		//每周一
		this.dealWeekDetail();
		
		//每月1号
		this.dealMonthDetail();
		
		
		try{
			// 校验登出缓存(2天)
			LogoutCacheService.checkExpirationCache(3);
		} catch (Exception e1) {
			LogUtil.error("异常：", e1);
		}
	}
	
	/**
	 * 周结
	 */
	private void dealWeekDetail(){
		if(DateService.isFirstWeekDay()){
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			
			try {
				serviceCollection.getPlayerService().handlePlayerInfoForWeek();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
			
			
			try {
				// 每周一发放排位奖励
				serviceCollection.getTiantiService().weekQuartz();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
			
			try {
				// 帮派每周处理
				serviceCollection.getGuildService().weekQuartz();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
		}
	}

	
	/**
	 * 月结
	 */
	private void dealMonthDetail(){
		if(DateService.isFirstMonthDay()){
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			
			try {
				// 清签到数据
				serviceCollection.getSignService().quartzMonth();
			}catch (Exception e) {
				LogUtil.error("异常：", e);
			}
			
		}
	}
	
	
	/**
	 * 日结前夕
	 */
	private void dealDailyThreeDetail() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		Date curDate = DateService.getCurrentUtilDate();
		
		try {
			serviceCollection.getSynDataService().synCache_beforeClose();
		} catch (Exception e) {
			LogUtil.error("日结前同步缓存数据:",e);
		}
		
		try {
			serviceCollection.getLogService().createOnlineTimeLog(curDate);
		} catch (Exception e) {
			LogUtil.error("今日在线时长人数统计:",e);
		}
		
	}
	


	/**
	 * 凌晨三点内
	 * */
	private void dealDailyFourDetail() {

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		try{
			// 清理装备数据
			serviceCollection.getEquipmentService().quartzDeletePlayerEquipment();
		} catch (Exception e1) {
			LogUtil.error("异常：", e1);
		}
		
		try{
			// 清理好友数据
			serviceCollection.getFriendService().quartzDeleteFriends();
		}catch (Exception e) {
			LogUtil.error("异常：", e);
		}		
		
		
		try{
			// 清理仇敌数据
			serviceCollection.getEnemyService().quartzDeleteEnemys();
		}catch (Exception e) {
			LogUtil.error("异常：", e);
		}	
	}
	
	/**
	 * 凌晨五点内
	 * */
	private void dealDailySevenDetail() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		try{
			// 清理任务数据
			serviceCollection.getTaskService().quartzDeletePlayerTask();
		}catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		try {
			// 清理邮件数据
			serviceCollection.getMailService().quartzDeleteMailInbox();
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}

		try {
			// 帮派每日处理
			serviceCollection.getGuildService().dailyQuartz();
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
	}
	
}
