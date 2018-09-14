package com.quartz.minute;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.util.LogUtil;

/**
 * 10分钟调度
 * @author ken
 * @date 2016-12-27
 */
public class TenMinuteQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		String jobName = job.getJobDetail().getKey().getName();

		if ("tenMinJobDetail".equalsIgnoreCase(jobName)) {
			
			try {
				// 场景
				serviceCollection.getSceneService().tenMinuteQuarzt();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
			
//			try {
//				// 定时发送系统公告
//				serviceCollection.getSystemNoticeService().quartzSendSystemNotice_one(60*10);
//			} catch (Exception e) {
//				LogUtil.error("异常：", e);
//			}
			

			
		} 
	}

}
