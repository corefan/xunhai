package com.service;

import java.util.List;
import java.util.Map;

import com.domain.Reward;
import com.domain.bag.PlayerBag;
import com.domain.reward.BaseReward;
import com.domain.reward.RewardRecord;

/**
 * 奖励实现
 * @author ken
 * @date 2017-2-16
 */
public interface IRewardService {
	
	/**
	 * 初始缓存数据
	 */
	void initBaseCache();

	/**
	 * 初始动态缓存
	 */
	void initCache();
	
	/**
	 * 根据ID获得 -> 综合奖励数据
	 * @param type@RewardConstant
	 * @param id 奖励ID
	 */
	BaseReward getBaseReward(int type, int rewardId);
	
	/**
	 * 综合奖励数据列表
	 */
	Map<Integer, BaseReward> listBaseRewards(int type);
	
	/**
	 * 获取奖励记录
	 */
	RewardRecord getRewardRecord(int rewardId);
	
	/**
	 * 创建奖励记录
	 */
	void createRewardRecord(RewardRecord model);
	
	/**
	 * 同步缓存to数据库
	 */
	void updateRewardRecord(RewardRecord model);
	
	/**
	 * 获取单个奖励 
	 * 返回(类型，静态id, 装备id, 数量)
	 */
	List<List<Object>> fetchRewardOne(long playerId, int type, int id, int num, int isBlind) throws Exception;
	
	/**
	 * 获取单个奖励 
	 * 返回(类型，静态id, 装备id, 数量)
	 */
	List<List<Object>> fetchRewardOne(long playerId, int type, int id, int num, int isBlind, int tigTag) throws Exception;
	
	/**
	 * 获取奖励(带背包验证)
	 * 返回list (类型，静态id, 装备id, 数量)
	 */
	List<List<Object>> fetchRewardList(long playerId, List<Reward> rewards) throws Exception;
	
	/**
	 * 获取奖励(带背包验证)
	 * 返回list (类型，静态id, 装备id, 数量)
	 */
	List<List<Object>> fetchRewardList(long playerId, List<Reward> rewards, int tigTag) throws Exception;
	
	/**
	 * 获取奖励(背包满发邮件)
	 */
	void fetchRewardList_nocheck(long playerId, List<Reward> rewards) throws Exception;
	
	/**
	 * 获取掉落装备
	 */
	void fetchDropEquipment(long playerId, long playerEquipmentId) throws Exception;
	
	/**
	 * 扣除单个道具
	 * @param isBinding 是否扣除绑定物品 ture:是 false:否
	 */
	void deductItem(long playerId, int itemId, int num, boolean isBinding)throws Exception;
	
	/**
	 * 扣除多个道具
	 */
	List<PlayerBag> deductItemList(long playerId, List<List<Integer>> items)throws Exception;

	/**
	 * 扣除指定背包道具包括装备
	 */
	void deductItemByPlayerBagId(long playerId, long playerBagId, int num)throws Exception;
	
	/**
	 * 检查背包是否够用  
	 */
	boolean checkBackNum(long playerId, List<Reward> rewards);
	
	
	/** 独立随机奖励*/
	List<Reward> independRandom(List<Reward> rewardList);
	
	/** 全局随机奖励*/
	Reward globalRandom(List<Reward> rewardList);
	
	/**
	 * 消耗物品判定
	 * @param expendList 需要消耗的物品
	 *  @param num 使用的物品数量
	 * @param flag 是否扣除物品标识
	 */
	void expendJudgment(long playerId, List<Reward> expendList, boolean flag, String costName) throws Exception;
}
