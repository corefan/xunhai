package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GCCContext;

/**
 * 两分钟任务
 * @author ken
 * @date 2018年8月9日
 */
public class OneMinQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		String jobName = job.getJobDetail().getKey().getName();

		if (jobName.equalsIgnoreCase("oneMinDetail")) {
			try {
				GCCContext.getInstance().getServiceCollection().getBaseDataService().initData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
