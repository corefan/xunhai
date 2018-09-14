package com.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.CodeContext;
import com.service.ICodeService;

/**
 * @author barsk
 * 2014-4-23
 * 每日调度	
 */
public class DailyQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		
		String jobName = job.getJobDetail().getKey().getName();
		
		if (jobName.equalsIgnoreCase("dailyDetail")) {
			// 刷新激活码
			ICodeService codeService = CodeContext.getInstance().getServiceCollection().getCodeService();
			codeService.initActCodeCache();
		}
	}
}
