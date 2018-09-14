package com.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


/**
 * @author ken
 * 2014-4-23
 * 调度	
 */
public class QuartzService {

	private static Scheduler scheduler = null;

	public static void start() throws Exception {

		SchedulerFactory factory = new StdSchedulerFactory();

		scheduler = factory.getScheduler();

		// 两分钟调度
		JobDetail oneMinDetail = JobBuilder.newJob(OneMinQuartz.class).withIdentity("oneMinDetail", "quartzGroup").build();
		CronTrigger oneMinTrigger = TriggerBuilder.newTrigger().withIdentity("oneMinTrigger", "quartzGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule("5 0/2 * * * ?")).build();
		scheduler.scheduleJob(oneMinDetail, oneMinTrigger);

//		// 5分钟调度
//		JobDetail fiveMinDetail = JobBuilder.newJob(FiveMinQuartz.class).withIdentity("fiveMinDetail", "quartzGroup").build();
//		CronTrigger fiveMinTrigger = TriggerBuilder.newTrigger().withIdentity("fiveMinTrigger", "quartzGroup")
//				.withSchedule(CronScheduleBuilder.cronSchedule("13 3/5 * * * ?")).build();
//		scheduler.scheduleJob(fiveMinDetail, fiveMinTrigger);

//		// 10分钟调度
//		JobDetail tenMinDetail = JobBuilder.newJob(TenMinQuartz.class).withIdentity("tenMinDetail", "quartzGroup").build();
//		CronTrigger tenMinTrigger = TriggerBuilder.newTrigger().withIdentity("tenMinTrigger", "quartzGroup")
//				.withSchedule(CronScheduleBuilder.cronSchedule("26 2/10 * * * ?")).build();
//		scheduler.scheduleJob(tenMinDetail, tenMinTrigger);

		// 半小时调度
		/*JobDetail halfHourDetail = JobBuilder.newJob(HalfHourQuartz.class).withIdentity("halfHourDetail", "quartzGroup").build();
		CronTrigger halfHourTrigger = TriggerBuilder.newTrigger().withIdentity("halfHourTrigger", "quartzGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule("5 26/30 * * * ?")).build();
		scheduler.scheduleJob(halfHourDetail, halfHourTrigger);*/

		scheduler.start();
	}
	
	public static void stop() throws Exception { 
		scheduler.shutdown(true);
	}
}
