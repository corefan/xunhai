package com.service;

import java.util.List;

import com.domain.Position;
import com.domain.map.BaseMap;
import com.domain.map.Transfer;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;
import com.scene.SceneModel;

/**
 * 场景管理器
 * @author ken
 * @date 2016-12-28
 */
public interface ISceneService {

	/**
	 * 初始配置缓存
	 */
	void initBaseCache();
	
	/**
	 * 初始缓存
	 */
	void initCache();
	
	/**
	 * 取地图配置数据
	 */
	BaseMap getBaseMap(int mapId);
	
	/**
	 * 传送门
	 */
	Transfer getTransfer(int transferId);
	
	/**
	 * 取场景缓存
	 */
	SceneModel getSceneModel(String sceneGuid);
	
	/**
	 * 进入场景
	 * @param param 扩展参数 
	 */
	void enterScene(long playerId, int mapId, int transferId, boolean bLogin, String sceneGuid, int param) throws Exception;
	
	/**
	 * 传送
	 */
	void transfer(long playerId, int mapId, Position toPosition) throws Exception;
	
	/**
	 * 退出场景
	 */
	void quitScene(Long playerId) throws Exception;
	
	/**
	 * 获取周围所有元素
	 */
	void getSceneElementList(Long playerId);
	
	/**
	 * 同步位置状态(玩家跑动)
	 */
	void synPosition(Long playerId, int newState, int newX, int newY, int newZ, int newDiretion, boolean move);
	
	/**
	 * 同步位置状态(非玩家的跑动)
	 */
	void synPosition(String sceneGuid, String targetGuid, int newState, int newX, int newY, int newZ, int newDiretion, boolean move);
	
	/**
	 * 更新位置
	 */
	void updatePosition(String sceneGuid, String targetGuid, Position newPosition, int newDiretion);
	
	/**
	 * 检测残留单位
	 */
	void checkPuppets(Long playerId, List<String> guids);
	
	/**
	 * 取场景玩家编号列表
	 */
	List<Long> getScenePlayerIds(SceneModel sceneModel);
	
	/**
	 * 取区域玩家编号列表
	 */
	List<Long> getNearbyPlayerIds(BasePuppet basePuppet);
	
	/**
	 * 根据区域点取玩家编号列表
	 */
	List<Long> getNearbyPlayerIdsByGridId(SceneModel sceneModel, int gridId);
	
	/**
	 * 获取场景单位
	 */
	BasePuppet getBasePuppet(String sceneGuid, String guid, int fighterType);
	
	/**
	 * 取场景玩家数据
	 */
	PlayerPuppet getPlayerPuppet(long playerId);
	
	/**
	 * 删除场景玩家缓存
	 */
	void deletePlayerPuppet(long playerId);
	
	/**
	 * 通知周边玩家移除自己
	 */
	void removeSelfToNearby(String guid, List<Long> nearbyPlayerIds);
	
	/**
	 * 移除周边单位列表
	 */
	void removeNearbyToSelf(long playerId, List<String> guids);
	
	/**
	 * 重置
	 */
	void resetPuppet(BasePuppet basePuppet);
	
	/**
	 * 摧毁副本
	 */
	void destroy(SceneModel sceneModel);	
	
	/**
	 * 同步怪物状态
	 */
	void synMonsterState(Long playerId);
	
	/**
	 * 十分钟任务
	 */
	void tenMinuteQuarzt();
}
