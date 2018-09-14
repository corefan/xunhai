package com.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.common.Config;
import com.constant.QuartzCronTypeConstant;
import com.quartz.daily.DailyQuartz;
import com.quartz.hour.OneHourQuartz;
import com.quartz.hour.SixHourQuartz;
import com.quartz.minute.FiveMinuteQuartz;
import com.quartz.minute.OneMinuteQuartz;
import com.quartz.minute.TenMinuteQuartz;
import com.quartz.second.ThreeSecondQuartz;
import com.quartz.week.WeekQuartz;

/**
 * 调度器
 * @author ken
 * @date 2016-12-27
 */
public class QuartzService {

	private static Scheduler scheduler = null;

	public static void start() throws Exception {

		SchedulerFactory factory = new StdSchedulerFactory();
		scheduler = factory.getScheduler();
		
		secondQuartz();
		
		minuteQuartz();
		
		hourQuartz();
		
		dailyQuartz();
		
		//weekQuartz();
		
		activityQuartz();
		
		otherQuartz();
		
		robotQuartz();
		
		scheduler.start();
	}

	public static void stop() throws Exception { 
		scheduler.shutdown(true);
	}

	/**
	 * 秒调度
	 */
	private static void secondQuartz() throws Exception{
		
		JobDetail threeSecondJobDetail = JobBuilder.newJob(ThreeSecondQuartz.class).withIdentity("threeSecondDetail","jobGroup").build();
		CronTrigger threeSecondTrigger = TriggerBuilder.newTrigger().withIdentity("threeSecondTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.THREE_SEC))).build();
		scheduler.scheduleJob(threeSecondJobDetail, threeSecondTrigger);
		
		//十秒钟调度(1)
//		JobDetail tenSecondJobDetail = JobBuilder.newJob(TenSecondQuartz.class).withIdentity("tenSecondDetail","jobGroup").build();
//		CronTrigger tenSecondTrigger = TriggerBuilder.newTrigger().withIdentity("tenSecondTrigger", "triggerGroup")
//				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.TEN_SEC))).build();
//		scheduler.scheduleJob(tenSecondJobDetail, tenSecondTrigger);
	}
	
	/**
	 * 分钟调度
	 */
	private static void minuteQuartz() throws Exception{
		
		// 1分钟调度(1)
		JobDetail oneMinJobDetail_1 = JobBuilder.newJob(OneMinuteQuartz.class).withIdentity("oneMinJobDetail_1","jobGroup").build();
		CronTrigger oneMinTrigger_1 = TriggerBuilder.newTrigger().withIdentity("oneMinTrigger_1", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.ONE_MIN_ONE))).build();
		scheduler.scheduleJob(oneMinJobDetail_1, oneMinTrigger_1);
		// 1分钟调度(2)
		JobDetail oneMinJobDetail_2 = JobBuilder.newJob(OneMinuteQuartz.class).withIdentity("oneMinJobDetail_2","jobGroup").build();
		CronTrigger oneMinTrigger_2 = TriggerBuilder.newTrigger().withIdentity("oneMinTrigger_2", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.ONE_MIN_TWO))).build();
		scheduler.scheduleJob(oneMinJobDetail_2, oneMinTrigger_2);
		
		
		
		// 5分钟调度(1)
		JobDetail fiveMinJobDetail_1 = JobBuilder.newJob(FiveMinuteQuartz.class).withIdentity("fiveMinJobDetail_1","jobGroup").build();
		CronTrigger fiveMinTrigger_1 = TriggerBuilder.newTrigger().withIdentity("fiveMinTrigger_1", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.FIVE_MIN_ONE))).build();
		scheduler.scheduleJob(fiveMinJobDetail_1, fiveMinTrigger_1);

		// 5分钟调度(2)
		JobDetail fiveMinJobDetail_2 = JobBuilder.newJob(FiveMinuteQuartz.class).withIdentity("fiveMinJobDetail_2","jobGroup").build();
		CronTrigger fiveMinTrigger_2 = TriggerBuilder.newTrigger().withIdentity("fiveMinTrigger_2", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.FIVE_MIN_TWO))).build();
		scheduler.scheduleJob(fiveMinJobDetail_2, fiveMinTrigger_2);

		// 5分钟调度(3)
		JobDetail fiveMinJobDetail_3 = JobBuilder.newJob(FiveMinuteQuartz.class).withIdentity("fiveMinJobDetail_3","jobGroup").build();
		CronTrigger fiveMinTrigger_3 = TriggerBuilder.newTrigger().withIdentity("fiveMinTrigger_3", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.FIVE_MIN_THREE))).build();
		scheduler.scheduleJob(fiveMinJobDetail_3, fiveMinTrigger_3);

		// 5分钟调度(4)
		JobDetail fiveMinJobDetail_4 = JobBuilder.newJob(FiveMinuteQuartz.class).withIdentity("fiveMinJobDetail_4","jobGroup").build();
		CronTrigger fiveMinTrigger_4 = TriggerBuilder.newTrigger().withIdentity("fiveMinTrigger_4", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.FIVE_MIN_FOUR))).build();
		scheduler.scheduleJob(fiveMinJobDetail_4, fiveMinTrigger_4);
		
		
		// 10分钟调度
		JobDetail tenMinJobDetail = JobBuilder.newJob(TenMinuteQuartz.class).withIdentity("tenMinJobDetail","jobGroup").build();
		CronTrigger tenMinTrigger = TriggerBuilder.newTrigger().withIdentity("tenMinTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.TEN_MIN))).build();
		scheduler.scheduleJob(tenMinJobDetail, tenMinTrigger);

	}
	/**
	 * 小时调度
	 */
	private static void hourQuartz() throws Exception{
		
		// 每小时调度
		JobDetail oneHourJobDetail_1 = JobBuilder.newJob(OneHourQuartz.class).withIdentity("oneHourJobDetail_1","jobGroup").build();
		CronTrigger oneHourTrigger_1 = TriggerBuilder.newTrigger().withIdentity("oneHourTrigger_1", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.ONE_HOUR_ONE))).build();
		scheduler.scheduleJob(oneHourJobDetail_1, oneHourTrigger_1);
		
		// 6小时调度
		JobDetail sixHourJobDetail = JobBuilder.newJob(SixHourQuartz.class).withIdentity("sixHourJobDetail","jobGroup").build();
		CronTrigger sixHourTrigger = TriggerBuilder.newTrigger().withIdentity("sixHourTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.SIX_HOUR))).build();
		scheduler.scheduleJob(sixHourJobDetail, sixHourTrigger);
	}
	/**
	 * 日调度
	 */
	private static void dailyQuartz() throws Exception{
		// 每日调度
		JobDetail dailyOneDetail = JobBuilder.newJob(DailyQuartz.class).withIdentity("dailyOneDetail", "jobGroup").build();
		CronTrigger dailyOneTrigger = TriggerBuilder.newTrigger().withIdentity("dailyOneTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.DAY_ONE))).build();
		scheduler.scheduleJob(dailyOneDetail, dailyOneTrigger);

		// 每日调度
		JobDetail dailyTwoDetail = JobBuilder.newJob(DailyQuartz.class).withIdentity("dailyTwoDetail", "jobGroup").build();
		CronTrigger dailyTwoTrigger = TriggerBuilder.newTrigger().withIdentity("dailyTwoTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.DAY_TWO))).build();
		scheduler.scheduleJob(dailyTwoDetail, dailyTwoTrigger);

		// 每日调度
		JobDetail dailyFourDetail = JobBuilder.newJob(DailyQuartz.class).withIdentity("dailyFourDetail", "jobGroup").build();
		CronTrigger dailyFourTrigger = TriggerBuilder.newTrigger().withIdentity("dailyFourTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.DAY_FOUR))).build();
		scheduler.scheduleJob(dailyFourDetail, dailyFourTrigger);

		// 每日调度-清理数据库数据1
		JobDetail dailyThreeDetail = JobBuilder.newJob(DailyQuartz.class).withIdentity("dailyThreeDetail", "jobGroup").build();
		CronTrigger dailyThreeTrigger = TriggerBuilder.newTrigger().withIdentity("dailyThreeTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.DAY_THREE))).build();
		scheduler.scheduleJob(dailyThreeDetail, dailyThreeTrigger);

		// 每日调度-清理数据库数据2
		JobDetail dailySevenDetail = JobBuilder.newJob(DailyQuartz.class).withIdentity("dailySevenDetail", "jobGroup").build();
		CronTrigger dailySevenTrigger = TriggerBuilder.newTrigger().withIdentity("dailySevenTrigger", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.DAY_SEVEN))).build();
		scheduler.scheduleJob(dailySevenDetail, dailySevenTrigger);
	}
	
	/**
	 * 周调度
	 */
	private static void weekQuartz() throws Exception{
		
		JobDetail jobDetail1 = JobBuilder.newJob(WeekQuartz.class).withIdentity("weekDetail1", "jobGroup").build();
		CronTrigger jobTrigger1 = TriggerBuilder.newTrigger().withIdentity("weekDetail1", "triggerGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(QuartzUtil.getCronBySiteAndType(Config.GAME_SITE, QuartzCronTypeConstant.WEEK_ONE))).build();
		scheduler.scheduleJob(jobDetail1, jobTrigger1);

	}
	
	/**
	 * 活动调度
	 */
	private static void activityQuartz() throws Exception{
		
		
	}
	
	/**
	 * 其他调度
	 */
	private static void otherQuartz() throws Exception{
		
	}
	
	/**
	 * 机器人调度
	 */
	private static void robotQuartz() throws Exception {
		
	}
}
