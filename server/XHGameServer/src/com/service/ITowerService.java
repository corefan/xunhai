package com.service;

import com.domain.tower.BaseTower;
import com.scene.SceneModel;

/**
 * 大荒塔
 * @author ken
 * @date 2017-3-24
 */
public interface ITowerService {

	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 取大荒塔配置
	 */
	BaseTower getBaseTowerById(int layerId);
	
	/**
	 * 进入大荒塔
	 */
	void enterTower(long playerId)throws Exception;
	
	/**
	 * 退出大荒塔
	 */
	void quitTower(long playerId)throws Exception;
	
	/**
	 * 重置大荒塔
	 */
	void resetTower(long playerId)throws Exception;
	
	/**
	 * 该波数打完处理
	 */
	void end(long playerId, int result, SceneModel sceneModel);
	
	/**
	 * 返回大荒塔buff编号
	 */
	int towerBuff(int curLayerId);
	
	/**
	 * 神境面板数据
	 */
	void getShenjingData(long playerId);
}
