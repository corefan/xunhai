/**
 * 
 */
package com.service;

import com.domain.puppet.PlayerPuppet;
import com.scene.SceneModel;


/**
 * @author jiangqin
 * @date 2017-8-15
 */
public interface IRebotService {
	
	/** 初始机器人基础数据*/
	void initBaseCache();
	
	/** 创建机器人*/
	void createRebot(long playerId, int num) throws Exception;
	
	/** 处理机器人AI*/
	void dealRebotAI(PlayerPuppet playerPuppet, SceneModel model);
}
