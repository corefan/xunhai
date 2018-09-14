package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.CacheSynDBService;

public class OneMinQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		String jobName = job.getJobDetail().getKey().getName();

		if (jobName.equalsIgnoreCase("oneMinDetail")) {
			CacheSynDBService.getAllAndClearLogCache_one_min_insert();
			
		}
	}
	
}
