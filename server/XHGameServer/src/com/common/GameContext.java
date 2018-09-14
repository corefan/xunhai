package com.common;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.util.LogUtil;

/**
 * 2013-10-30
 * 
 */
public class GameContext {

	private static final ThreadLocal<ServiceCollection> threadServiceCollection = new ThreadLocal<ServiceCollection>();  

	private static final ThreadLocal<ActionCollection> threadActionCollection = new ThreadLocal<ActionCollection>();  

	// 线程编号-集合
	private Map<Long, ServiceCollection> serviceMap = new ConcurrentHashMap<Long, ServiceCollection>();

	// 热更新的class
	private Map<String, Class<?>> serviceClassMap = new ConcurrentHashMap<String, Class<?>>();

	private GameContext() {}

	public static GameContext getInstance() {
		return GameContextSingle.instance;
	}

	public void refreshCollection(Class<?> cls, int type) throws Exception {

		if (type == 1) {
			for (Entry<Long, ServiceCollection> entry : serviceMap.entrySet()) {
				ServiceCollection sc = entry.getValue();
				sc.reLoadServiceClass(cls.newInstance());
			}
			serviceClassMap.put(cls.getSimpleName(), cls);
		}else if (type == 2) {
			
		}

	}

	/**
	 * service集合
	 */
	public ServiceCollection getServiceCollection() {
		ServiceCollection sc = threadServiceCollection.get();
		if (sc == null) {

			sc = new ServiceCollection();
			threadServiceCollection.set(sc);

			serviceMap.put(Thread.currentThread().getId(), sc);
			
			try {
				if (!serviceClassMap.isEmpty()) {
					for (Entry<String, Class<?>> entry : serviceClassMap.entrySet()) {
						sc.getHotServiceMap().put(entry.getKey(), entry.getValue().newInstance());
					}
				}
			} catch (Exception e) {
				LogUtil.error("异常: ",e);
			}
		} 

		return sc;
	}

	/**
	 * action集合
	 */
	public ActionCollection getActionCollection() {

		ActionCollection ac = threadActionCollection.get();

		if (ac == null) {
			ac = new ActionCollection();
			threadActionCollection.set(ac);
		} 

		return ac;
	}


	private static final class GameContextSingle {
		protected static final GameContext instance = new GameContext();
	}
}
