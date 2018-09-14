package com.common;

/**
 * @author ken
 * 2014-3-10
 * gcc上下文
 */
public class GCCContext {

	private static final ThreadLocal<ServiceCollection> threadServiceCollection = new ThreadLocal<ServiceCollection>();
	private static GCCContext instance;
	
	private GCCContext() {}
	
	public static GCCContext getInstance() {
		if (instance == null) {	
			instance = new GCCContext();
		}
		return instance;
	}
	
	public ServiceCollection getServiceCollection() {
		ServiceCollection collection = threadServiceCollection.get();
		if (collection == null) {
			collection = new ServiceCollection();
			threadServiceCollection.set(collection);
		}
		return collection;
	}
}
