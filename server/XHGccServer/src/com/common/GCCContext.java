package com.common;

/**
 * @author ken
 * 2014-3-10
 * gcc上下文
 */
public class GCCContext {

	private static final ThreadLocal<ServiceCollection> threadServiceCollection = new ThreadLocal<ServiceCollection>();
	private static final ThreadLocal<ActionCollection> threadActionCollection = new ThreadLocal<ActionCollection>();
	
	private GCCContext() {}
	
	public static GCCContext getInstance() {
		return GameContextSingle.instance;
	}
	
	public ServiceCollection getServiceCollection() {
		ServiceCollection collection = threadServiceCollection.get();
		if (collection == null) {
			collection = new ServiceCollection();
			threadServiceCollection.set(collection);
		}
		return collection;
	}
	
	public ActionCollection getActionCollection() {
		ActionCollection collection = threadActionCollection.get();
		if (collection == null) {
			collection = new ActionCollection();
			threadActionCollection.set(collection);
		}
		return collection;
	}
	
	private static final class GameContextSingle {
		protected static final GCCContext instance = new GCCContext();
	}
}
