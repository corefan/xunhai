package com.service;

import com.domain.activity.BaseChargeActivity;
import com.domain.activity.PlayerTomb;

/**
 * 运营活动系统
 * @author ken
 * @date 2017-10-25
 */
public interface IActivityService {

	/** 初始数据*/
	void initBaseActivity();	

	/** 调度删除缓存*/
	void deleteCache(long playerId);
	
	/** 领取在线奖励*/
	void getOnlineReward(long playerId, int rewardId) throws Exception;	
	
	/** 在线奖励领取列表*/
	void getRewardList(long playerId);	
	
	/** 获取充值活动相关数据*/
	void getPayActData(long playerId);
	
	/** 领取首冲奖励*/
	void getFristPayReward(long playerId, int rewardId) throws Exception;
	
	/** 获领取累计充值奖励*/
	void getTotalRrechargeReward(long playerId, int rewardId) throws Exception;
	
	/** 获领取累计充值奖励*/
	void getTotalSpendReward(long playerId, int rewardId) throws Exception;
	
	/** 领取每日累计充值奖励*/
	void getDailyRrechargeReward(long playerId, int rewardId) throws Exception;
	
	/** 购买成长基金奖励*/
	void buyGrowthFund(long playerId) throws Exception;
	
	/** 领取成长基金奖励*/
	void getGrowthFund(long playerId, int rewardId) throws Exception;
	
	/** 领取全民福利奖励*/
	void getNationalWelfare(long playerId, int rewardId) throws Exception;	
	
	/** 获取转盘抽奖相关数据*/
	void getTurntableData(long playerId);
	
	/** 转盘抽奖*/
	void turntableDraw(long playerId, int type) throws Exception;	
	
	/** 获取转盘抽奖榜单信息*/
	public void getTurnRecList(long playerId,  int start, int offset) throws Exception;
	
	/** 获取玩家陵墓数据 */
	PlayerTomb getPlayerTomb(long playerId);
	
	/** 获取陵墓面板数据 */	
	void getTombData(long playerId) throws Exception;	
	
	/** 抽取陵墓*/
	void tomb(long playerId, int tombIndex) throws Exception;
	
	/** 更换陵墓*/
	void changeTomb(long playerId) throws Exception;
	
	/** 购买神器*/
	void buyArtifact(long playerId);
	
	/** 领取开服七天乐奖励*/
	void getOpenServerReward(long playerId, int rewardId) throws Exception;	
	/** 获取开服七天乐数据*/
	void getOpenServerData(long playerId);	
	
	/** 领取累计七天充值奖励*/
	void getSevenPayReward(long playerId, BaseChargeActivity baseChargeActivity);	
	/** 获取累计七天充值数据*/
	void getSevenPayData(long playerId) throws Exception;	
	
	/** 在七天累计充值活动时间内的基础数据*/
	BaseChargeActivity getBaseChargeActivity();
	
	/** 获取购买神器数据*/
	void buyArtifactData(long playerId);
	
	/** 获取首充数据*/
	void getFristPayData(long playerId);
	
	/** 领取冲级奖励*/
	public int getLevelAward(long playerId, int rewardId) throws Exception;
	
	/** 领取战力奖励*/
	public int getBattleValueAward(long playerId, int rewardId) throws Exception;
	
	/** 获取冲级奖励数据*/
	public void getLevelAwardData(long playerId);
	
	/** 获取战力奖励数据*/
	public void getBVAwardData(long playerId);
	
	/**
	 * 使用激活码
	 */
	void useActCode(long playerId, String code) throws Exception;
	
	/** 获取实名认证信息*/
	void getIdentCheckInfo(long playerId);
	
	/** 实名认证*/
	int identityCheck(long playerId, String identity, String realName) throws Exception;
	
	/** 领取实名认证奖励*/
	void getIdCheckAward(long playerId) throws Exception;	
	
	/** 每天0点调度*/
	void quartzDaily();	
	
	/** 日结后调度*/
	void quartzDailyAfter();	
}



