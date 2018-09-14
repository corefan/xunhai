package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.CodeContext;
import com.service.ISynDataService;

/**
 * @author barsk
 * 2014-5-8
 * 5分钟调度	
 */
public class FiveMinQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		String jobName = job.getJobDetail().getKey().getName();

		if (jobName.equalsIgnoreCase("fiveMinDetail")) {
			ISynDataService synDataService = CodeContext.getInstance().getServiceCollection().getSynDataService();
			synDataService.update_fiveMinData();
		}
	}

}
