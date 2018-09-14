package com.service;

import java.util.List;

import com.domain.monster.BaseAiDetermine;
import com.domain.monster.BaseMonster;
import com.domain.monster.BaseRefreshMonster;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.scene.SceneModel;

/**
 * 怪物管理
 * @author ken
 * @date 2017-1-7
 */
public interface IMonsterService {

	/**
	 * 初始基础表
	 */
	void initBaseCache();
	
	/**
	 * 取怪物配置
	 */
	BaseMonster getBaseMonster(int monsterId);
	
	/**
	 * 取刷怪配置
	 */
	BaseRefreshMonster getBaseRefreshMonster(int refMonsterId);
	
	/**
	 * 取ai判定表
	 */
	BaseAiDetermine getBaseAiDetermine(int aiId);
	
	/**
	 * 处理怪物AI
	 */
	void dealAI(MonsterPuppet monster, SceneModel sceneModel);
	
	/**
	 * 刷新区域怪物
	 */
	void refreshMonsters(SceneModel sceneModel, int refMonsterId, int curLayerId, boolean offer);
	
	/**
	 * 刷新区域怪物 （指定位置）
	 */
	void refreshMonsters(SceneModel sceneModel, int refMonsterId, int x, int y, int z, int curLayerId, boolean offer);
	
	/**
	 * 重置怪物数据
	 */
	void resetMonsterPuppet(SceneModel sceneModel, MonsterPuppet model);
	
	/**
	 * 把周边怪物推送给自己
	 */
	void synNearbyMonsterToSelf(long playerId, List<MonsterPuppet> monsterPuppets);
	
	/**
	 * 把怪物推送给周边玩家
	 */
	void synMonsterToNearby(MonsterPuppet monsterPuppet, List<Long> playerIds);
	
	/**
	 * 获取怪物经验
	 */
	void addPlayerExp(long playerId, int monsterId);	
	
	/**
	 * 初始召唤怪
	 */
	void initBeckonPuppet(SceneModel sceneModel, PlayerPuppet playerPuppet, int monsterId);
	
	/**
	 * 把周边召唤怪推送给自己
	 */
	void synNearbyBeckonToSelf(long playerId, List<BeckonPuppet> beckonPuppets);
	
	/**
	 * 把召唤怪物推送给周边玩家
	 */
	void synBeckonToNearby(BeckonPuppet beckonPuppet, List<Long> playerIds);
	
	/**
	 * 移除副本怪物
	 */
	void removeBeckonPuppet(PlayerPuppet p, SceneModel sceneModel);
	
	/**
	 * 加入怪物仇恨列表
	 */
	void addMonsterEnemy(int dmg, BasePuppet basePuppet, BasePuppet monster);
	
	/**
	 * 设置怪物位置
	 */
	void updatePosition(SceneModel sceneModel, MonsterPuppet monster, int newX, int newY, int newZ);
	
}
