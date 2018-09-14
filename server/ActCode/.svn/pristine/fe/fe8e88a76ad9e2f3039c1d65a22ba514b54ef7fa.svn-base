package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author barsk
 * 2014-5-8
 * 1小时调度	
 */
public class OneHourQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		String jobName = job.getJobDetail().getKey().getName();

		if (jobName.equalsIgnoreCase("oneHourDetail")) {
		}
	}

}
