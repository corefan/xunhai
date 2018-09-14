package com.quartz.second;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.service.ITiantiService;
import com.util.LogUtil;


/** 3秒钟调度*/
public class ThreeSecondQuartz implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String jobName = context.getJobDetail().getKey().getName();
		
		if (jobName.equalsIgnoreCase("threeSecondDetail")) {
			
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			try {
				ITiantiService tiantiService = serviceCollection.getTiantiService();
				tiantiService.systemMatch();
			} catch (Exception e) {
				LogUtil.error("天梯3秒匹配异常：", e);
			}
		} 
	}

}
