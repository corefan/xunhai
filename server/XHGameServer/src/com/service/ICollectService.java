package com.service;

import java.util.List;
import java.util.Map;

import com.domain.collect.BaseCollect;
import com.domain.collect.Collect;
import com.domain.puppet.PlayerPuppet;
import com.scene.SceneModel;

/**
 * 采集系统
 * @author jiangqin
 * @date 2017-2-16
 */
public interface ICollectService {
	
	/** 初始化配置表 */
	void initBaseCache();

	/** 根据采集ID找出采集基础信息 */
	BaseCollect getBaseCollectById(int collectId);

	/** 开始采集 */
	int startCollect(long playerId, int playerCollectId) throws Exception;	
	
	/** 结束采集 */
	void endCollect(SceneModel sceneModel, PlayerPuppet playerPuppet, Collect collect, BaseCollect baseCollect);

	/** 创建采集点信息 */
	Collect createCollect(SceneModel sceneModel, BaseCollect baseCollect);
	
	/** 根据类型获取采集信息 */
	List<BaseCollect> getBaseCollectByType(int type);
	
	/**	该线路所有普通采集 */
	Map<Integer, Boolean> getGeneralCollectCacheMap(int line);
	
	/** 调度刷新高级采集奖励 */
	void quartzRefreshSeniorCollectAward(SceneModel sceneModel, PlayerPuppet playerPuppet, Collect collect, BaseCollect baseCollect);
	
	/** 通知添加采集物 */
	void offerAddCollect(List<Collect> collectList, List<Long> playerIds);
	
	/** 通知移除采集物 */
	void offerRemoveCollect(List<Integer> collectIds, List<Long> playerIds);
	
	/** 玩家下线, 清理采集信息 */
	void clearCollect(PlayerPuppet playerPuppet);
	
	/** 根据玩家所在场景的格子位置，找出采集点*/
    Collect getCollect(int playerCollectId, PlayerPuppet playerPuppet,SceneModel sceneModel);

	/**  中断采集*/	
	void InterruptCollect(Long playerId, int playerCollectId);
}
