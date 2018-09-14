package com.quartz.hour;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.GameContext;
import com.common.LogoutCacheService;
import com.common.ServiceCollection;
import com.util.LogUtil;

/**
 * 每小时调度
 * @author ken
 * @date 2016-12-27
 */
public class OneHourQuartz implements Job {

	@Override
	public void execute(JobExecutionContext job)
			throws JobExecutionException {

	    ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();

		String jobName = job.getJobDetail().getKey().getName();
		if (jobName.equalsIgnoreCase("oneHourJobDetail_1")) {

//			try {
//				// 系统公告
//				serviceCollection.getSystemNoticeService().quartzSendSystemNotice_one(60*60);
//			} catch (Exception e) {
//				LogUtil.error("异常:",e);
//			}

			try {
				// 校验登出缓存(4小时)
				LogoutCacheService.checkExpirationCache(1);
			} catch (Exception e) {
				LogUtil.error("异常:",e);
			}			
		
			try {
				//天梯排行
				serviceCollection.getTiantiService().refreshRank();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}			
			
			try {
				// 计算购买基金人数			
				serviceCollection.getPlayerService().calBuyGrowthFundNum();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
			
			try {
				// 帮派每小时处理			
				serviceCollection.getGuildService().hourQuartz();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}
			
			try {
				//排行榜(耗时长， 放最后处理)
				serviceCollection.getRankService().refreshRank();
			} catch (Exception e) {
				LogUtil.error("异常：", e);
			}

		} 
	}

}
