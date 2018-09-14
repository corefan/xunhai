package com.core.disruptor;

import com.core.GameMessage;
import com.lmax.disruptor.WorkHandler;

public class DisruptorMsgHandler implements WorkHandler<GameMessage>  {

	private GameDataHandlerService dataHandlerService = new GameDataHandlerService();
	
	@Override
	public void onEvent(GameMessage msg) throws Exception {
		dataHandlerService.get().onDisruptorData(msg);
	}

}
