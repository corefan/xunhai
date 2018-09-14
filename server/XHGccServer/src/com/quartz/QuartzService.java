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

		// 聊天监控调度
		/*for (int i=1;i<=5;i++) {
			String detailName = "chatDetail"+i;
			String triggerName = "chatTrigger"+i;

			int second = (i-1)*5+1;

			JobDetail chatDetail = JobBuilder.newJob(ChatQuartz.class).withIdentity(detailName,"quartzGroup").build();
			CronTrigger chatTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, "quartzGroup").startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(second+"/15 * * * * ?")).build();
			scheduler.scheduleJob(chatDetail, chatTrigger);
		}*/

		// 1分钟调度
		JobDetail oneMinDetail = JobBuilder.newJob(OneMinQuartz.class).withIdentity("oneMinDetail", "quartzGroup").build();
		CronTrigger oneMinTrigger = TriggerBuilder.newTrigger().withIdentity("oneMinTrigger", "quartzGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule("5 0/1 * * * ?")).build();
		scheduler.scheduleJob(oneMinDetail, oneMinTrigger);

		// 1小时调度
		JobDetail oneHourDetail = JobBuilder.newJob(OneHourQuartz.class).withIdentity("oneHourDetail1", "quartzGroup").build();
		CronTrigger oneHourTrigger = TriggerBuilder.newTrigger().withIdentity("oneHourTrigger", "quartzGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule("5 13 0/1 * * ?")).build();
		scheduler.scheduleJob(oneHourDetail, oneHourTrigger);


		scheduler.start();
	}
}
