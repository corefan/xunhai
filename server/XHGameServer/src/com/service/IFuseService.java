package com.service;

import java.util.List;

/**
 * 合成 分解 提炼
 * @author ken
 * @date 2018年8月16日
 */
public interface IFuseService {
	
	/** 初始物品合成规则*/
	public void initBaseCache();

	/** 合成*/
	void compose(long playerId, int itemId) throws Exception;
	
	/** 分解*/
	void decompose(long playerId, List<Long> playerBagIds) throws Exception;
	
	/** 一键分解*/
	void autoDecompose(long playerId, List<Integer> rareIdList) throws Exception;
	
	
	/** 提炼*/
	void refine(long playerId, List<Long> playerBagIds) throws Exception;
	
	/** 一键提炼*/
	void autoRefine(long playerId, List<Integer> rareIdList) throws Exception;
}
