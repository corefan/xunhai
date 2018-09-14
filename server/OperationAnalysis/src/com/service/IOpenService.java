package com.service;

import java.util.List;
import java.util.Map;

/**
 * @author ken
 * 2015-12-1
 * 开放数据	
 */
public interface IOpenService {

	/** 在线人数 */
	public int getCurrentOnlineNum(String agent);
	
	/** 在线人数 */
	public List<Map<String, Object>> getCurrentOnlineNum_agent(String agent);
	
	/** 在线人数 */
	public int getCurrentOnlineNum_site(String gameSite);
	
	/** 
	 * 执行dba函数
	 * combineReset 
	 *  */
	public String execDBAFunc_1(int gameId, String gameSite);
}
