package com.service;

import com.domain.tianti.BaseDropItem;
import com.domain.tianti.BaseTiantiScore;
import com.domain.tianti.PlayerTianti;
import com.scene.SceneModel;

/**
 * 天梯系统
 * @author ken
 * @date 2017-4-14
 */
public interface ITiantiService {

	/**
	 * 初始基础配置
	 */
	void initBaseCache();
	
	/**
	 * 初始动态数据
	 */
	void initCache();
	
	/**
	 * 清理缓存
	 */
	void deleteCache(long playerId);
	
	/**
	 * 获取玩家天梯记录
	 */
	PlayerTianti getPlayerTianti(long playerId);
	
	/**
	 * 更新玩家天梯
	 */
	void updatePlayerTianti(PlayerTianti playerTianti);
	
	/**
	 * 天梯面板数据
	 */
	void getTiantiPanelData(long playerId);
	
	/**
	 * 获取排行列表 分页
	 */
	void getRankPageList(long playerId, int start, int offset);
	
	/**
	 * 匹配
	 */
	void match(long playerId) throws Exception;
	
	/**
	 * 取消匹配
	 */
	void cancelMatch(long playerId) throws Exception;
	
	/** 
	 * 竞技场物品使用
	 */
	void useTiantiItem(long playerId, int itemId, int num) throws Exception;	
	
	/** 系统匹配*/
	void systemMatch();
	
	/**
	 * pk3分钟时间到
	 */
	void end(SceneModel sceneModel, long winnerId, long failId);
	
	/**
	 * 1小时刷新排行榜
	 */
	void refreshRank();
	
	/**
	 * 检查日期
	 */
	void checkDate();
	
	/**
	 * 发放排位奖励(每周一)
	 */
	void weekQuartz();
	
	/**
	 * 处理登录
	 */
	void doLogin(long playerId);
	
	/**
	 * 删除记录
	 */
	void deletePlayerTian(long playerId);
	
	/**
	 * 领取段位奖励
	 */
	void getStageReward(long playerId, int stage) throws Exception ;

	/**
	 * 积分取段位信息
	 */
	BaseTiantiScore getBaseTiantiScore(int score);
	
	/**
	 * 玩家是否在竞技场匹配列表中
	 */
	boolean isPKMacth(long playerId);
	
	/**
	 * 刷新掉落
	 */
	void refreshDropItem(BaseDropItem baseDropItem, SceneModel sceneModel);	
	
	/**
	 * 根据竞技场存在时间, 给场景内玩家添加对应的pkBuff
	 */
	void addPKbuff(SceneModel sceneModel, int lifeTime);
	
	/** 处理下线*/
	void dealExit(long playerId);
}
