package com.action;


import java.util.List;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.BagProto.C_AutoDecompose;
import com.message.BagProto.C_AutoRefine;
import com.message.BagProto.C_Compose;
import com.message.BagProto.C_Decompose;
import com.message.BagProto.C_Refine;
import com.message.BagProto.S_Compose;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFuseService;

/**
 * 合成与分解
 * @author ken
 * @date 2018年7月11日
 */
public class FuseAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();


	/**
	 * 合成
	 */
	
	public void compose(GameMessage gameMessage) throws Exception {
		IFuseService fuseService = serviceCollection.getFuseService();
		long playerId = gameMessage.getConnection().getPlayerId();
		C_Compose param = C_Compose.parseFrom(gameMessage.getData());
		int itemId = param.getItemId();
		
		fuseService.compose(playerId, itemId);
		
		S_Compose.Builder builder = S_Compose.newBuilder();		
		MessageObj msg = new MessageObj(MessageID.S_Compose_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 分解
	 */
	public void decompose(GameMessage gameMessage) throws Exception {
		IFuseService fuseService = serviceCollection.getFuseService();
		long playerId = gameMessage.getConnection().getPlayerId();
		C_Decompose param = C_Decompose.parseFrom(gameMessage.getData());
		List<Long> playerBagIdList = param.getPlayerBagIdList();
		
		fuseService.decompose(playerId, playerBagIdList);
	}

	/**
	 * 一键分解
	 */
	public void autoDecompose(GameMessage gameMessage) throws Exception {
		IFuseService fuseService = serviceCollection.getFuseService();
		long playerId = gameMessage.getConnection().getPlayerId();
		C_AutoDecompose param = C_AutoDecompose.parseFrom(gameMessage.getData());
		List<Integer> rareIdList = param.getRareIdList();		
		fuseService.autoDecompose(playerId, rareIdList);	
	
	}

	/**
	 * 提炼
	 */
	public void refine(GameMessage gameMessage) throws Exception {
		IFuseService fuseService = serviceCollection.getFuseService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Refine param = C_Refine.parseFrom(gameMessage.getData());
		List<Long> playerBagIdList = param.getPlayerBagIdList();
		
		fuseService.refine(playerId, playerBagIdList);
	}

	/**
	 * 一键提炼
	 */
	public void autoRefine(GameMessage gameMessage) throws Exception {
		IFuseService fuseService = serviceCollection.getFuseService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AutoRefine param = C_AutoRefine.parseFrom(gameMessage.getData());
		List<Integer> rareIdList = param.getRareIdList();		
		fuseService.autoRefine(playerId, rareIdList);	
	
	}
}
