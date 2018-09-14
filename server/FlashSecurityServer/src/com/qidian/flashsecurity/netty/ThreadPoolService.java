package com.qidian.flashsecurity.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;
import org.jboss.netty.util.DefaultObjectSizeEstimator;


/**
 * @author barsk
 * 2013-10-29
 * 线程池管理
 */
public class ThreadPoolService {


	/** 消息处理线程 */
	private ExecutionHandler executionHandler;

	/** netty3线程 */
	private ThreadPoolExecutor netty3Executor;


	public ThreadPoolService() {

		netty3Executor = new MemoryAwareThreadPoolExecutor(32, 1024*128, 1024*128*32, 60, TimeUnit.MINUTES, 
				new MyObjectSizeEstimator(),
				new PriorityThreadFactory("FLASH-Msg-Thread", "netty3Msg", Thread.NORM_PRIORITY));
		netty3Executor.prestartAllCoreThreads();

		executionHandler = new ExecutionHandler(netty3Executor);

	}

	public class MyRunnable implements Runnable {

		private final byte[] data;

		public MyRunnable(byte[] data) {
			this.data = data;
		}

		public void run() {
			// Process 'data' ..
		}
	}

	public class MyObjectSizeEstimator extends DefaultObjectSizeEstimator {

		@Override
		public int estimateSize(Object o) {
			if (o instanceof MyRunnable) {
				return ((MyRunnable) o).data.length + 8;
			}
			return super.estimateSize(o);
		}
	}

	public ExecutionHandler getExecutionHandler() {
		return executionHandler;
	}

	public ExecutorService getNetty3Executor() {
		return netty3Executor;
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
