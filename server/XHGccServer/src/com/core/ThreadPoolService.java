package com.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 2013-10-29
 * 线程池管理
 */
public class ThreadPoolService {
	
	/** netty4线程 */
	private ThreadPoolExecutor netty4Executor;
	
	/** disruptor生产者线程 */
	private ThreadPoolExecutor disruptorProducerExecutor;
	/** disruptor对象池大小 */
	public static int DISRUPTOR_BUFF_SIZE;
	/** disruptor消费者线程数 */
	public static int DISRUPTOR_CONSUMER_THREAD_NUM;
	
	public ThreadPoolService() {
		
		
		netty4Executor = new ThreadPoolExecutor(10, 10,
				60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
						"GCCNetty-Msg-Thread", "gccNetty4Msg", Thread.NORM_PRIORITY));
		netty4Executor.prestartAllCoreThreads();
		
		disruptorProducerExecutor = new ThreadPoolExecutor(10, 10,
				60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
						"GCCNetty-producer-Thread", "gccNetty4Msg", Thread.NORM_PRIORITY));
		disruptorProducerExecutor.prestartAllCoreThreads();
		
		DISRUPTOR_BUFF_SIZE = 1024*16*4;
		DISRUPTOR_CONSUMER_THREAD_NUM = 1;
	}
	
	
	
	
	public void executeNetty4Msg(Runnable r) {
		if (netty4Executor != null) {
			netty4Executor.execute(r);
		}
	}
	
	/**
	 * 执行生产者消息
	 * */
	public void executeProducerMsg(Runnable r) {
		if (disruptorProducerExecutor != null) {
			disruptorProducerExecutor.execute(r);
		}
	}

	public ExecutorService getNetty4Executor() {
		return netty4Executor;
	}
	
	private static class PriorityThreadFactory implements ThreadFactory {
		private int _prio;
		private String _name;
		private AtomicInteger _threadNumber = new AtomicInteger(1);
		private ThreadGroup _group;

		public PriorityThreadFactory(String name, String groupName, int prio) {
			_prio = prio;
			_name = name;
			_group = new ThreadGroup(groupName);
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(_group, r);
			t.setName(_name + "-" + _threadNumber.getAndIncrement());
			t.setPriority(_prio);
			t.setDaemon(true);
			return t;
		}

	}

	public static  ThreadPoolService getInstance() {
		return SingletonHolder._instance;
	}

	private static class SingletonHolder {
		protected static final ThreadPoolService _instance = new ThreadPoolService();
	}
}
