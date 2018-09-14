package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GCCContext;

public class FiveMinQuartz implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String jobName = context.getJobDetail().getKey().getName();
		
		if ("fiveMinDetail".equals(jobName)) {
//			try {
//				GCCContext.getInstance().getServiceCollection().getLogService().dealSavePayLog();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}

}
