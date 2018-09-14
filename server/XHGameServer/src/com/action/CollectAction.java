package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.CollectProto.C_InterruptCollect;
import com.message.CollectProto.C_StartCollect;
import com.message.CollectProto.S_StartCollect;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.ICollectService;


public class CollectAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 采集开始
	 */
	
	public void startCollect(GameMessage gameMessage) throws Exception {
		ICollectService collectService = serviceCollection.getCollectService();
		long playerId =  gameMessage.getConnection().getPlayerId();	
		C_StartCollect param = C_StartCollect.parseFrom(gameMessage.getData());
		int playerCollectId = param.getPlayerCollectId();
		
		int state = collectService.startCollect(playerId, playerCollectId);	
		
		S_StartCollect.Builder builder = S_StartCollect.newBuilder();
		builder.setPlayerCollectId(playerCollectId);
		builder.setState(state);
		MessageObj msg = new MessageObj(MessageID.S_StartCollect_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 采集结束
	 */
	
	public void interruptCollect(GameMessage gameMessage)throws Exception{
		ICollectService collectService = serviceCollection.getCollectService();
		long playerId =  gameMessage.getConnection().getPlayerId();	
		C_InterruptCollect param = C_InterruptCollect.parseFrom(gameMessage.getData());
		int playerCollectId = param.getPlayerCollectId();
		collectService.InterruptCollect(playerId, playerCollectId);			
	}
}
