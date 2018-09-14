package com.common;

/**
 * @author barsk
 */
public class CodeContext {

	private static final ThreadLocal<ServiceCollection> threadServiceCollection = new ThreadLocal<ServiceCollection>();
	
	private CodeContext() {}
	
	public static CodeContext getInstance() {
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
	
	private static final class GameContextSingle {
		protected static final CodeContext instance = new CodeContext();
	}
}
