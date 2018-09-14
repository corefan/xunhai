package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class FiveMinQuartz implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String jobName = context.getJobDetail().getKey().getName();
		
		if ("fiveMinDetail".equals(jobName)) {

		}
	}

}
