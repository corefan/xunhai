package com.service;

import java.util.List;

import com.domain.skill.BaseSkill;
import com.domain.skill.DamagModel;
import com.domain.skill.PlayerSkill;

/**
 * 技能系统
 * @author ken
 * @date 2017-1-19
 */
public interface ISkillService {

	/** 初始配置表 */
	void initBaseCache();
	
	/**
	 * 清缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 取技能结算模块配置
	 */
	DamagModel getDamagModel(int skillModelId);
	
	/**
	 * 取技能配置
	 */
	BaseSkill getBaseSkill(int skillId);
	
	/**
	 * 玩家技能列表
	 */
	List<PlayerSkill> listPlayerSkills(long playerId);
	
	/**
	 * 取某个技能
	 */
	PlayerSkill getPlayerSkill(long playerId, int skillId);
	
	/**
	 * 学习技能
	 */
	PlayerSkill studyPlayerSkill(long playerId, int skillId, boolean initFlag)throws Exception;
	
	/**
	 * 升级技能
	 */
	PlayerSkill upgradePlayerSkill(long playerId, int skillId)throws Exception;
	
	/**
	 * 增加技能熟练度
	 */
	void addMastery(long playerId, int skillId);
	
	/**
	 * 测试开发玩家所有技能
	 */
	void testOpenAllPlayerSkills(long playerId)throws Exception;

	/**
	 * 更新玩家技能数据
	 */
	void updatePlayerSkill(PlayerSkill playerSkill);
	
	/**
	 * 根据技能类型ID, 找出玩家技能
	 */
     PlayerSkill getPlayerSkillByIndex(long playerId, int skillIndex);  
     
     /**
      * 同步改变的技能信息
 	  */	
     void synChangeListPlayerSkill(long playerId, List<PlayerSkill> changeListPlayerSkill);
     
     /**
      * 使用物品添加技能熟练度
      */	 	
     void addSkillMastery(long playerId, int skillId, int itemId) throws Exception;
}
