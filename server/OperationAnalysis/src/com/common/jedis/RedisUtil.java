package com.common.jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.LogUtil;



/**
 * redis工具类
 * @author ken
 * @date 2018年3月22日
 */
public class RedisUtil {
	
	/**
	 * 根据key，获取存储的字符串
	 * 
	 * @author 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		String value = null;
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return value;
	}
	
	/**
	 * 根据key，获取存储的字符串 （反序列化）
	 * 
	 * @author 
	 * @param <T>
	 * @param key
	 * @return
	 */
	public static <T> T getValue(String key, T t) {
		String value = null;
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			value = jedis.get(key);
			
			return deserializelist(value, new TypeToken<T>(){});
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return null;
	}
	
	/**
	 * 把一个list降排序，并返回排序好的结果
	 * @author 
	 * @param key
	 * @return
	 * 2015-3-25 下午1:26:19
	 */
	public static List<String> getValueAfterDESCSort(String key) {
		List<String> value = null;
		Jedis jedis = RedisServer.getInstance().getResource();
		
		try {
			SortingParams p = new SortingParams();
			value = jedis.sort(key, p.alpha().desc());
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return value;
	}
	/**
	 * 在原来的Value上的后面追加新的值
	 * 
	 * @author 
	 * @param key
	 * @param value
	 * @param expire
	 *            此信息在Redis中呆的时间，-1为不设置
	 */
	public static  void appendValue(String key, String value, int expire) {
		Jedis jedis = RedisServer.getInstance().getResource();

		try {
			jedis.append(key, value);
			if (expire != -1) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}

	/**
	 * 删除Redis中的一个字符键
	 * 
	 * @author 
	 * @param key
	 */
	public static void deleteKey(String key) {
		
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.del(key);
		} catch (Exception e) {
			// 释放资源
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}

	/**
	 * 删除redis中hash的字段值
	 * 
	 * @author 
	 * @param key
	 * @param fields
	 */
	public static void deleteMapKey(String key, String... fields) {
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.hdel(key, fields);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}

	/**
	 * 判断某个键是否存在。
	 * 
	 * @author 
	 * @param key
	 * @param pool
	 * @return
	 */
	public static boolean isExistKey(String key) {
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			
			return jedis.exists(key);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return false;
	}
	/**
	 * 判断某个map中的字段键是否存在。
	 * 
	 * @author
	 * @param key
	 * @param pool
	 * @return
	 */
	public static boolean isExistKeyOfMap(String key, String field) {
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			
			return jedis.hexists(key, field);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return false;
	}

	/**
	 * 把对象存到RediS的hash中。
	 * 
	 * @author 
	 * @param map 	字段名——字段值
	 * @return 1 成功，0 失败
	 */
	public static void saveToMap(String key, String field, String value, int expire) {
		
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.hsetnx(key, field, value);
			if (expire != -1) {
				jedis.expire(key, expire);
			}

		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}

	}
	
	public static void saveToMap(String key, Map<String, String> map, int expire) {
		if(map == null){
			return ;
		}
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.hmset(key, map);
			if (expire != -1) {
				jedis.expire(key, expire);
			}

		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}

	}

	/**
	 * 获取redis中map的字段值
	 * 
	 * @author 
	 * @param key
	 * @param field
	 * @return
	 */
	public static String getValueOfMap(String key, String field) {
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.hget(key, field);
			
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}

		return null;
	}

	/**
	 * 存储key - value 信息到redis之中。
	 * 
	 * @author
	 * @param key
	 * @param value
	 * @param 在redis中呆的时间
	 *            ，-1是不设置
	 */
	public static void setValue(String key, String value, int expire) {
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.set(key, value);
			
			if(expire != -1){
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}

		
	}
	
	/**
	 * 存储key - value 信息到redis之中。  （序列化）
	 * 
	 * @author
	 * @param key
	 * @param t 对象
	 * @param 在redis中呆的时间
	 *            ，-1是不设置
	 */
	public static <T> void  setValue(String key, T t, int expire){
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.set(key, serialize(t));
			
			if(expire != -1){
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}
	

	/**
	 * 向一个redis的list列表中从左开始添加值
	 * 
	 * @author 
	 * @param key
	 * @param value
	 */
	public static void addListValueOfL(String key, String value, int expire) {
		Jedis jedis =RedisServer.getInstance().getResource();
		try {		
			jedis.lpush(key, value);
			if (expire != -1) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}
	
	/**
	 * 向一个redis的set列表中添加值
	 * 
	 * @author 
	 * @param key
	 * @param value
	 */
	public static void addListValueOfS(String key, int expire, String ...values) {
		Jedis jedis =RedisServer.getInstance().getResource();
		try {		
			jedis.sadd(key, values);
			if (expire != -1) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}
	
	/**
	 * 向一个redis的set列表中获取值
	 * 
	 * @author 
	 * @param key
	 * @param value
	 */
	public static Set<String> getListValueOfS(String key) {
		Jedis jedis =RedisServer.getInstance().getResource();
		try {		
			return jedis.smembers(key);
			
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		
		return null;
	}
	
	/**
	 * 一次向redis的list中添加多个值
	 *@author 
	 *2015-1-9上午10:43:03	
	 * @param key
	 * @param values
	 * @param expire
	 */
	public static void addSomeListValueOfL(String key,String[] values,int expire){
		Jedis jedis =RedisServer.getInstance().getResource();
		try {
			jedis.lpush(key, values);
			if (expire != -1) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}

	/**
	 * 从redis的map中获取多个值
	 * 
	 * @author
	 * @param key
	 * @param field
	 * @return
	 */
	public static List<String> getValueOfMap(String key, String... field) {
		Jedis jedis = RedisServer.getInstance().getResource();
		
		List<String> list = new ArrayList<String>();
		try {
			list = jedis.hmget(key, field);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return list;
	}

	/**
	 * 获取redis中list的值
	 * 
	 * @author 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getValueOfList(String key, int start, int end) {
		Jedis jedis = RedisServer.getInstance().getResource();
		
		List<String> list = new ArrayList<String>();
		try {
			
			list = jedis.lrange(key, start, end);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return list;
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。<br/>
	 * count 的值可以是以下几种：<br/>
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 <br/>
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。<br/>
	 * count = 0 : 移除表中所有与 value相等的值。<br/>
	 * @author 
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public static long removeValueOfList(String key, int count, String value) {
		Jedis jedis = RedisServer.getInstance().getResource();
		
		long result = 0;
		try {

			result = jedis.lrem(key, count, value);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return result;
		
	}

	/*
	 * public String getStringByLua(String script){ Jedis jedis = null;
	 * JedisPool pool = null; String value = ""; try{ pool =
	 * this.getLocalJedisPool(); jedis = pool.getResource();
	 * 
	 * }catch (Exception e) { pool.returnBrokenResource(jedis);
	 * CommonLog.ERR("获取本地Redis内容错误：" + e.getMessage() + "\n" + e.getClass());
	 * e.printStackTrace(); } finally { if (jedis != null) {
	 * pool.returnResource(jedis); } } return value; }
	 */
	/**
	 * 原子更新map中，值类型为数字的值
	 * 
	 * @author 
	 * @param key
	 * @param field
	 * @param addValue
	 *            变化的数值，正为加，负为减
	 * @param expire
	 *            此条信息在Redis中呆的时间，-1为不设置
	 * @return
	 */
	public static long addValueOfMap(String key, String field, int addValue, int expire) {
		Jedis jedis = RedisServer.getInstance().getResource();
		long value = 0;
		try {
			
			value = jedis.hincrBy(key, field, addValue);
			if(expire != -1){
				jedis.expire(key, expire);
			}
			
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return value;
	}

	/**
	 * 设置redis中map的值
	 * 
	 * @author 
	 * @param key
	 * @param field
	 * @param value
	 * @param expire
	 *            在redis中呆的时间，以秒为单位,-1表示不设置
	 */
	public static void setValueOfMap(String key, String field, String value, int expire) {
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			
			jedis.hset(key, field, value);
			if (expire != -1) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
	}
	/**
	 * 获取redis中hash中的所有内容，map中的key是field,
	 * @author
	 * @param key
	 * @return
	 * 2015-3-10 下午6:38:50
	 */
	public static Map<String, String> getAllValueOfMap(String key){
		Jedis jedis = RedisServer.getInstance().getResource();
		
		try {
			
			return jedis.hgetAll(key);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return null;
	}
	/**
	 * 原子增加一个数的值
	 * @author 
	 * @param key
	 * @return
	 * 2015-3-23 下午6:56:07
	 */
	public static int incrValue(String key){
		Jedis jedis = RedisServer.getInstance().getResource();
		
		long result =0;
		try {
			
			result = jedis.incr(key);
			return (int)result;
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		return 0;
	}
	/**
	 * 设计一个key的有效时间
	 * 
	 * @param key
	 * @param seconds
	 * 2015-3-24 下午6:38:55
	 */
	public static void setExpire(String key,int seconds){
		Jedis jedis = RedisServer.getInstance().getResource();
		try {
			jedis.expire(key, seconds);
		} catch (Exception e) {
			LogUtil.error("redis异常", e);
		} finally {
			if (jedis != null) {
				RedisServer.getInstance().returnResource(jedis);
			}
		}
		
	}

	/**
	 * 序列化对象成String 
	 */
	public static <T> String serialize(T obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	/**
	 * String反序列化成对象
	 */
	public static <T> T deserializelist(String json, TypeToken<T> typeToken) {
		Gson gson = new Gson();
	
		return gson.fromJson(json, typeToken.getType());
	}
}

