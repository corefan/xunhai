package com.service;

import com.domain.sign.PlayerSign;

public interface ISignService {

	/** 初始奖励数据*/
	void initBaseSign();
	
	/** 调度删除缓存*/
	void deleteCache(long playerId);
	
	/** 签到*/
	void sign(long playerId) throws Exception;	
	
	/** 签到奖励领取*/
	void getConSignReward(long playerId, int signNum) throws Exception; 
	
	/** 日结处理 */	
	void quartzDaily();
	
	/** 月结处理 */	
	void quartzMonth();
	
	/** 获取玩家签到数据 */	
	PlayerSign getPlayerSign(long playerId);
}

