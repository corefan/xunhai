package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.fashion.PlayerFashion;
import com.message.FashionProto.C_PutdownFashion;
import com.message.FashionProto.C_PutonFashion;
import com.message.FashionProto.S_PutdownFashion;
import com.message.FashionProto.S_PutonFashion;
import com.message.FashionProto.S_SynFashionList;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFashionService;
import com.service.IProtoBuilderService;

/**
 * 时装接口
 * @author ken
 * @date 2017-2-13
 */
public class FashionAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	

	/**
	 * 获取时装列表
	 */
	
	public void getFashionList(GameMessage gameMessage) throws Exception {
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IFashionService fashionService = serviceCollection.getFashionService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		List<PlayerFashion> list = fashionService.getFashionList(playerId);
		
		S_SynFashionList.Builder builder = S_SynFashionList.newBuilder();		
		for(PlayerFashion playerFashion : list){
			builder.addFashionList(protoBuilderService.buildPlayerFashionMsg(playerFashion));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SynFashionList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 装备时装
	 */
	
	public void putonFashion(GameMessage gameMessage) throws Exception {

		IFashionService fashionService = serviceCollection.getFashionService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_PutonFashion param = C_PutonFashion.parseFrom(gameMessage.getData());
		int fashionId = param.getFashionId();
		
		fashionService.putonFashion(playerId, fashionId);
		
		S_PutonFashion.Builder builder = S_PutonFashion.newBuilder();
		builder.setFashionId(fashionId);
		MessageObj msg = new MessageObj(MessageID.S_PutonFashion_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
		
	}

	/**
	 * 卸下时装
	 */
	
	public void putdownFashion(GameMessage gameMessage) throws Exception {
		IFashionService fashionService = serviceCollection.getFashionService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_PutdownFashion param = C_PutdownFashion.parseFrom(gameMessage.getData());
		int fashionId = param.getFashionId();
		
		fashionService.putdownFashion(playerId, fashionId);
		
		S_PutdownFashion.Builder builder = S_PutdownFashion.newBuilder();
		builder.setFashionId(fashionId);
		MessageObj msg = new MessageObj(MessageID.S_PutdownFashion_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}


}
