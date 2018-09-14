package com.quartz.minute;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.service.IVipService;
import com.util.LogUtil;

/**
 * 每分钟调度
 * @author ken
 * @date 2016-12-27
 */
public class OneMinuteQuartz implements Job {


	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IVipService  vipService = serviceCollection.getVipService();

		String jobName = context.getJobDetail().getKey().getName();

		if (jobName.equalsIgnoreCase("oneMinJobDetail_1")) {
			try {
				// 帮派
				serviceCollection.getGuildService().minuteQuartz();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
		} else if(jobName.equalsIgnoreCase("oneMinJobDetail_2")) {
			
			try{
				vipService.quartzPlayerVip();
			}catch(Exception e){
				LogUtil.error("异常", e);
			}
		}
	}

	
}
