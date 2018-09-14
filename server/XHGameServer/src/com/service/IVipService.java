package com.service;

import com.domain.player.PlayerExt;
import com.domain.vip.BaseVipPrivilege;
import com.domain.vip.PlayerVip;

/**
 * vip系统
 * @author ken
 * @date 2018年7月11日
 */
public interface IVipService {
	
	void initBaseCache();
	
	/** 激活vip*/
	void ActiviteVip(long playerId, int vipId);
	
	/** 获取vip激活奖励*/
	void GetVipActReward(long playerId, int vipId) throws Exception;	
	
	/** 获取玩家vip信息*/
	PlayerVip getPlayerVip(long playerId);
	
	/** 领取每日奖励(vip双倍)*/
	void GetDailyReward(long playerId) throws Exception;
	
	/** 获取玩家每日奖励领取状态*/
	void getGetDailyRewardState(long playerId);
	
	/** 根据特权ID找出特权配置*/
	BaseVipPrivilege getBaseVipPrivilege(int vipPrivilegeId);	
	
	/** 领取vip每日福利(月卡)*/
	void GetVipWelfare(long playerId) throws Exception;
	
	/** 获取vip每日福利领取状态*/
	public void GetVipWelfareState(long playerId);
	
	/** 玩家vip过期检测*/
	void quartzPlayerVip();
	
	/** 玩家vip缓存清理*/
	void deleteCache(long playerId);
	
	/** 获取vip特权值 */
	int getVipPrivilegeValue(long playerId, int vipPrivilegeId);
	
	/** 登陆处理*/
	void dealLogin(PlayerExt playerExt);	
}
