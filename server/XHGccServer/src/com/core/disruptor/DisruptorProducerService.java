package com.core.disruptor;

import com.core.Connection;
import com.core.ThreadPoolService;
import com.domain.MessageObj;
import com.lmax.disruptor.RingBuffer;

public class DisruptorProducerService {

	public void onData(MessageObj msg, Connection con) {
		ThreadPoolService.getInstance().executeProducerMsg(new MsgJob(msg.getMsgID(), msg.getData(), con));
	}

	private final class MsgJob implements Runnable {

		/** 消息编号 */
		private int msgID;
		/** 消息数据 */
		private byte[] data;
		/** 连接信息 */
		private Connection con;
		
		public MsgJob(int msgID, byte[] data, Connection con) {
			super();
			this.msgID = msgID;
			this.data = data;
			this.con = con;
		}

		@Override
		public void run() {

			/**
			 * Disruptor 的事件发布过程是一个两阶段提交的过程：
　　				第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
　　				第二步：获取对应的事件对象，将数据写入事件对象；
　　				第三步：将事件提交到 RingBuffer;
				事件只有在提交之后才会通知 EventProcessor 进行处理；
			 * */
			RingBuffer<MessageObj> ringBuffer = DisruptorServer.getInstance().getRingBuffer();
			long sequence = ringBuffer.next();
			try {
				
				MessageObj gameMessage = ringBuffer.get(sequence);
				gameMessage.setMsgID(msgID);
				gameMessage.setData(data);
				gameMessage.setConnection(con);
				
			} finally {
				/**
				 * 最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；
				 * 如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer
				 * */
				ringBuffer.publish(sequence);
				//ringBuffer.notifyAll();
			}

		}
	}
}
