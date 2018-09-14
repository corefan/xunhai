package com.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.common.Config;


/**
 * 2013-10-29
 * 线程池管理
 */
public class ThreadPoolService {

	/** 跨服消息处理线程 */
	private ThreadPoolExecutor crossExecutor;
	
	/** netty4线程 */
	private ThreadPoolExecutor netty4Executor;
	
	/** disruptor生产者线程 */
	private ThreadPoolExecutor disruptorProducerExecutor;
	/** disruptor对象池大小 */
	public static int DISRUPTOR_BUFF_SIZE;
	/** disruptor消费者线程数 */
	public static int DISRUPTOR_CONSUMER_THREAD_NUM;
	
	public ThreadPoolService() {
		
		
		int[] threadNum = getThreadNumByOpenDay(); 
		
//		// 跨服消息线程
//		if (Config.CROSS_SERVER_SWITCH == 1) {
//			crossExecutor = new ThreadPoolExecutor(threadNum[1], threadNum[1],
//					60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
//							"crossMsg-Thread", "crossThread", Thread.NORM_PRIORITY));
//			crossExecutor.prestartAllCoreThreads();
//		}
		
		netty4Executor = new ThreadPoolExecutor(threadNum[0], threadNum[0],
				60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
						"Netty-Msg-Thread", "netty4Msg", Thread.NORM_PRIORITY));
		netty4Executor.prestartAllCoreThreads();
		
		disruptorProducerExecutor = new ThreadPoolExecutor(threadNum[2], threadNum[2],
				60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory(
						"Netty-producer-Thread", "netty4Msg", Thread.NORM_PRIORITY));
		disruptorProducerExecutor.prestartAllCoreThreads();
		
		DISRUPTOR_BUFF_SIZE = 1024*16*threadNum[3];
		DISRUPTOR_CONSUMER_THREAD_NUM = threadNum[0];
	}
	
	
	
	/**
	 * 根据开服天数控制线程数量
	 * 消费者线程-跨服线程-生产者线程-buff估值-netty3线程
	 * */
	public int[] getThreadNumByOpenDay() {
		
		int severType = Config.SEVER_TYPE;
		int subDay = (int) ((System.currentTimeMillis() - Config.OPEN_SERVER_DATE.getTime())/(24*3600*1000));
		if (subDay <= 3) {
			// 开服不超过3天
			if (severType == 0) {
				return new int[]{16,6,2,8,20};
			} else if (severType == 1) {
				return new int[]{24,8,2,16,30};
			} else {
				return new int[]{32,10,3,32,40};
			}
		} else if (subDay <= 10) {
			// 开服不超过10天
			if (severType == 0) {
				return new int[]{12,4,1,4,16};
			} else if (severType == 1) {
				return new int[]{18,6,2,8,24};
			} else {
				return new int[]{24,8,2,16,32};
			}
		} else {
			if (severType == 0) {
				return new int[]{8,2,1,2,12};
			} else if (severType == 1) {
				return new int[]{12,4,1,4,18};
			} else {
				return new int[]{16,6,2,8,24};
			}
		}
		
	}
	
	public void executeNetty4Msg(Runnable r) {
		if (netty4Executor != null) {
			netty4Executor.execute(r);
		}
	}
	
	/**
	 * 执行跨服消息
	 * */
	public void executeCrossMsg(Runnable r) {
		if (crossExecutor != null) {
			crossExecutor.execute(r);
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
