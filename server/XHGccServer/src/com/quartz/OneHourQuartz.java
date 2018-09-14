package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GCCContext;
import com.util.LogUtil;

public class OneHourQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		
		String jobName = job.getJobDetail().getKey().getName();

		if (jobName.equalsIgnoreCase("oneHourDetail1")) {
			try {
				GCCContext.getInstance().getServiceCollection().getBaseDataService().initData();
			} catch (Exception e) {
				LogUtil.error("",e);
			}
		}
	}

}
