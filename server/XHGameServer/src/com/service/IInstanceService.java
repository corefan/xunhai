package com.service;

import java.util.Map;

import com.domain.instance.BaseInstanceReward;
import com.domain.instance.PlayerInstance;
import com.domain.map.BaseMap;
import com.scene.SceneModel;

/**
 * 副本
 * @author ken
 * @date 2017-2-28
 */
public interface IInstanceService {
	
	/**
	 * 初始副本奖励数据
	 */
	void initBaseCache();	

	/**
	 * 玩家副本列表
	 */
	Map<Integer, PlayerInstance> listPlayerInstances(long playerId);
	
	/**
	 * 删除缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 取开启的副本列表
	 */
	void getOpenMapList(long playerId);
	
	/**
	 * 进入副本
	 */
	void enterInstance(long playerId, int mapId)throws Exception;
	
	/**
	 * 退出副本
	 */
	void quitInstance(long playerId)throws Exception;
	
	/**
	 * 同意拒绝进入
	 * @state 1:同意  2：拒绝
	 */
	void agreeEnter(long playerId, int state) throws Exception;
	
	/**
	 * 扣除副本次数
	 */
	void costCount(long playerId, BaseMap baseMap);
	
	/**
	 * 结束副本
	 */
	void end(SceneModel sceneModel, int result);
	
	/**
	 * 日结重置次数
	 */
	void quarztDaily();
	
	/**
	 * 取奖励信息
	 * @param rewardType:奖励类型
	 * @param lv:取奖励的所在的区间值
	 */
	BaseInstanceReward getBaseInstanceReward(int rewardType, int lv);
	
}
