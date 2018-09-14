package com.core.jedis;

import com.util.LogUtil;

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
			RedisPoolService redisPoolService = RedisPoolService.getInstance();
			
			String IP = redisPoolService.getValue("redis.ip");
			int port = Integer.parseInt(redisPoolService.getValue("redis.port"));
			int maxTotal = Integer.parseInt(redisPoolService.getValue("redis.pool.maxTotal"));
			int maxIdle =Integer.parseInt(redisPoolService.getValue("redis.pool.maxIdle"));
			int maxWait =Integer.parseInt(redisPoolService.getValue("redis.pool.maxWait"));
			boolean testOnBorrow =Boolean.parseBoolean(redisPoolService.getValue("redis.pool.testOnBorrow"));
			boolean testOnReturn =Boolean.parseBoolean(redisPoolService.getValue("redis.pool.testOnReturn"));
			boolean testWhileIdle =Boolean.parseBoolean(redisPoolService.getValue("redis.pool.testWhileIdle"));
			String password = redisPoolService.getValue("redis.password");
			
			JedisPoolConfig  poolConfig = new JedisPoolConfig();
			poolConfig.setMaxIdle(maxIdle);
			poolConfig.setMaxTotal(maxTotal);
			poolConfig.setTestOnBorrow(testOnBorrow);
			poolConfig.setTestOnReturn(testOnReturn);
			poolConfig.setTestWhileIdle(testWhileIdle);
			poolConfig.setMaxWaitMillis(maxWait);
			jedisPool = new JedisPool(poolConfig, IP, port, maxWait, password);			
			//jedisPool = new JedisPool(poolConfig, IP, port, maxWait);			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/** 初始化redis*/
	public Jedis getResource(){
		try {
			if(jedisPool != null) return jedisPool.getResource();
			
		} catch (Exception e) {
			LogUtil.error("redis jedisPool异常", e);
		}
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
