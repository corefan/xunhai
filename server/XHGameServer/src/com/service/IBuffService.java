package com.service;

import java.util.List;

import com.domain.buff.BaseBuff;
import com.domain.buff.Buff;
import com.domain.player.PlayerExt;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;

/**
 * buff管理
 * @author ken
 * @date 2017-4-1
 */
public interface IBuffService {

	/**
	 * 初始基础数据
	 */
	void initBaseCache();
	
	/**
	 * 添加buff
	 */
	List<Buff> addBuff(BasePuppet fighter, BasePuppet target, List<Integer> buffIdList);
	
	/**
	 * 处理buff结算
	 */
	void dealBuff(BasePuppet basePuppet);
	
	/**
	 * 死亡buff处理
	 */
	void dealDead(BasePuppet target);
	
	/**
	 * 下线buff处理
	 */
	void dealExit(PlayerExt playerExt,PlayerPuppet playerPuppet);
	
	/**
	 * 上线buff处理
	 */
	void dealLogin(long playerId);
	
	/**
	 * 取buff基础信息
	 */
	BaseBuff getBaseBuff(int buffId);
	
	/**
	 * 添加调息buff
	 */
	void autoAddHpMp(long playerId);
	
	/**
	 * 中断调息buff
	 */
	void breakAddHpMp(long playerId);
	
	
	/**
	 * 添加buff(达到某条件时触发)
	 */
	void addBuffById(long playerId, int buffId);
	
	/**
	 * 添加buff(某条件不满足时删除)
	 */
	void removeBuffById(BasePuppet basePuppet, int buffId);
	
}
