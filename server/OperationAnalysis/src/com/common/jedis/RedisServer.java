package com.common.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis启动类
 * @author ken
 * @date 2018年3月22日
 */
public class RedisServer {
	
	/** key： redisServerID, VALUE:jedisPool*/
	public static JedisPool jedisPool = null;
	
	public void startServer(){
		try {
			
			JedisPoolConfig  poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(5);
			poolConfig.setMaxTotal(500);
			poolConfig.setTestOnBorrow(true);
			poolConfig.setTestOnReturn(true);
			poolConfig.setTestWhileIdle(true);
			poolConfig.setMaxWaitMillis(3000);
			jedisPool = new JedisPool(poolConfig, "47.106.208.235", 10001, 3000, "Ken@2018");			
			//jedisPool = new JedisPool(poolConfig, IP, port, maxWait);			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/** 初始化redis*/
	public Jedis getResource(){
		if(jedisPool != null) return jedisPool.getResource();
		return null;
	}
	
	/** 释放redisPool*/
	public void returnResource(Jedis jedis){
		if(jedisPool != null){
			jedis.close();
		}
	}
	
	public void shotdown(){
		if(jedisPool != null){
			jedisPool.destroy();
		}
	}
	
	public static RedisServer getInstance(){
		return SingletonHolder.instance;
	}
	
	private static final class SingletonHolder {
		private static final RedisServer instance = new RedisServer();
	} 
}
