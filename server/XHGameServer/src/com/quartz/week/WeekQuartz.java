package com.quartz.week;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * 周调度
 * @author ken
 * @date 2017-8-18
 */
public class WeekQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		String jobName = job.getJobDetail().getKey().getName();
		
		if ("weekDetail1".equalsIgnoreCase(jobName)) {
			
		} 
	}

}
