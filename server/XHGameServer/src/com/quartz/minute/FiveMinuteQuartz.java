package com.quartz.minute;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.service.ISynDataService;

/**
 *  5分钟调度
 * @author ken
 * @date 2016-12-27
 */
public class FiveMinuteQuartz implements Job {

	private Logger logger = Logger.getLogger(FiveMinuteQuartz.class);

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISynDataService synDataService = serviceCollection.getSynDataService();

		String jobName = job.getJobDetail().getKey().getName();
		if (jobName == null) return;

		if (jobName.equalsIgnoreCase("fiveMinJobDetail_1")) {

			try {
				// 从缓存中更新数据到数据库中
				synDataService.update_fiveOneData();
			} catch (Exception e1) {
				logger.error("异常：", e1);
			}

		} else if (jobName.equalsIgnoreCase("fiveMinJobDetail_2")) {
			try {
				// 批量更新
				synDataService.update_fiveTwoData();
			} catch (Exception e1) {
				logger.error("异常：", e1);
			}
		} else if (jobName.equalsIgnoreCase("fiveMinJobDetail_3")) {
			try {
				// 批量更新
				synDataService.update_fiveThreeData();
			} catch (Exception e1) {
				logger.error("异常：", e1);
			}
		} else if (jobName.equalsIgnoreCase("fiveMinJobDetail_4")) {
			try {
				// 5分钟日志
				serviceCollection.getLogService().createFiveOnlineLog();
			} catch (Exception e1) {
				logger.error("异常：", e1);
			}

		}
	}

}
