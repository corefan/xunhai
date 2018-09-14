package com.core.disruptor;

import com.domain.MessageObj;
import com.lmax.disruptor.WorkHandler;

public class DisruptorMsgHandler implements WorkHandler<MessageObj>  {

	private GameDataHandlerService dataHandlerService = new GameDataHandlerService();
	
	@Override
	public void onEvent(MessageObj msg) throws Exception {
		dataHandlerService.get().onDisruptorData(msg);
	}

}
