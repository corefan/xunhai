package com.service;

import java.util.List;
import java.util.Map;

import com.core.GameMessage;
import com.domain.battle.DropItemInfo;
import com.domain.map.BaseMap;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;
import com.scene.SceneModel;


/**
 * 战斗处理
 * @author ken
 * @date 2017-1-10
 */
public interface IBattleService {

	/**
	 * 释放技能
	 * @param mapId 地图编号
	 * @param line  当前线路
	 * @param guid  攻击者
	 * @param skillId 技能编号
	 * @param type  技能类型
	 * @param direction  释放方向
	 * @param x y z 释放位置
	 * @param targetId 指向目标
	 */
	void synSkill(BasePuppet basePuppet, int skillId, int type, int direction, int x, int y, int z, String targetId)throws Exception;
	
	/**
	 * 技能受击
	 * @param mapId 地图编号
	 * @param line  当前线路
	 * @param guid  攻击者
	 * @param skillId 技能编号
	 * @param targetIds 指向目标列表
	 * @param fighterType 攻击者类型 @PlayerConstant
	 */
	void useSkill(String sceneGuid, String guid, int skillId, List<String> targerIds, int accountModelId, int wigId, int fighterType)throws Exception;
	
	/**
	 * 攻击
	 * @param basePuppet 攻击者
     * @param listTargets 受击者列表
     * @param listDmgs   受击者对应伤害
	 */
	void attack(BasePuppet basePuppet, List<BasePuppet> listTargets, List<Integer> listDmgs);
	
	/**
	 * 处理pk值
	 */
	void dealPkValue(PlayerPuppet playerPuppet);
	
	/**
	 * 处理高级采集
	 */
	void dealCollect(PlayerPuppet playerPuppet, SceneModel sceneModel, int gridId);
	
	/**
	 * 处理buff
	 */
	void dealBuff(BasePuppet basePuppet);
	
	/**
	 * 处理副本
	 */
	void dealFB(PlayerPuppet playerPuppet, SceneModel sceneModel);
	
	/**
	 * 推送掉落
	 */
	void offerDropItems(List<DropItemInfo> dropItems, List<Long> playerIds);
	
	/**
	 * 移除过期掉落
	 */
	void removeDropItems(List<Integer> dropIds, List<Long> playerIds);
	
	/**
	 * 拾取掉落
	 */
	void pickup(GameMessage gameMessage)throws Exception;
	
	/**
	 * 复活
	 * @type 1.免费 2.道具复活 3.钻石复活
	 */
	void revive(long playerId, int type)throws Exception;
	
	/**
	 * 切换pk模式
	 */
	void changePkModel(long playerId, int pkModel) throws Exception;
	
	/**
	 * 创建掉落
	 * dropItems :返回的掉落信息
	 * targetGuid 掉落者
	 * x y z  掉落的原点
	 * belongPlayerIds 归属   null为无归属
	 * index 位置下标
	 * offset 离原点的偏移距离
	 * randomType 0：九宫格类型    1：园内随机点
	 */
	int createDrop(Map<Integer, List<DropItemInfo>> dropItems, String targetGuid,
			int x, int y, int z, int goodsType, int itemId, int num, long playerEquipmentId, 
			List<Long> belongPlayerIds, int index, BaseMap baseMap, SceneModel sceneModel,
			int offset, int randomType);
}
