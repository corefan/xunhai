package com.service;

/**
 * 排行榜
 * @author ken
 * @date 2017-5-8
 */
public interface IRankService {

	/**
	 * 初始缓存
	 */
	void initCache();
	
	/**
	 * 刷新排行榜
	 */
	void refreshRank();
	
	/**
	 * 获取排行榜列表
	 */
	void getRankList(long playerId, int type, int career, int start, int offset);
}
