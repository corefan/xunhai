package com.quartz.hour;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.LogoutCacheService;

/**
 * 6小时调度
 * @author ken
 * @date 2016-12-27
 */
public class SixHourQuartz implements Job {

	private Logger logger = Logger.getLogger(SixHourQuartz.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	
		try {
			//校验登出缓存(24小时)
			LogoutCacheService.checkExpirationCache(2);
			
		} catch (Exception e) {
			logger.error("异常：", e);
		}
	}
}
