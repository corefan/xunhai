package com.core.disruptor;

import java.util.concurrent.ExecutorService;

import com.core.ThreadPoolService;
import com.domain.MessageObj;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorServer {

	private RingBuffer<MessageObj> ringBuffer;
	
	private Disruptor<MessageObj> disruptor;
	
	/** 消费者线程 */
	private ExecutorService executor;
	
	private DisruptorServer() {}

	public static DisruptorServer getInstance() {
		return SingletonHolder.instance;
	}

	public void start() {

		//BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
		//SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
		//YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
		
		// 消费者线程数量
		executor = ThreadPoolService.getInstance().getNetty4Executor();
		
		disruptor = new Disruptor<MessageObj>(new EventFactory<MessageObj>() {
			@Override
			public MessageObj newInstance() {
				return new MessageObj();
			}
		}, ThreadPoolService.DISRUPTOR_BUFF_SIZE, executor, ProducerType.MULTI, new BlockingWaitStrategy());

		WorkHandler<MessageObj>[] workHandlers = new DisruptorMsgHandler[ThreadPoolService.DISRUPTOR_CONSUMER_THREAD_NUM];
		for (int i=0; i<=ThreadPoolService.DISRUPTOR_CONSUMER_THREAD_NUM-1; i++) {
			DisruptorMsgHandler handler = new DisruptorMsgHandler();
			workHandlers[i] = handler; 
		}
		
		disruptor.handleEventsWithWorkerPool(workHandlers);

		this.ringBuffer = disruptor.start();
	}

	private static final class SingletonHolder {
		private static final DisruptorServer instance = new DisruptorServer();
	}


	public RingBuffer<MessageObj> getRingBuffer() {
		return ringBuffer;
	}
}
