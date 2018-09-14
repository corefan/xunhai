package com.common;



/**
 * 缓存同步到数据库
 * @author ken
 *
 */
public class CacheSynDBService {
	
	private CacheSynDBService(){}
	
	private static class SingletonHolder {
		protected static final CacheSynDBService _instance = new CacheSynDBService();
	}
	
	public static CacheSynDBService getInstance() {
		return SingletonHolder._instance;
	}
	

}
