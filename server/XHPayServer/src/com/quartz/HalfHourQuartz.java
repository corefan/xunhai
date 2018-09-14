package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GCCContext;

public class HalfHourQuartz implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		GCCContext.getInstance().getServiceCollection().getBaseDataService().initData();
	}

}
