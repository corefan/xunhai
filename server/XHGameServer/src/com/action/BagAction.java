package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;
import com.message.BagProto.C_PutdownDrug;
import com.message.BagProto.C_PutonDrug;
import com.message.BagProto.C_SellItem;
import com.message.BagProto.C_UseItem;
import com.message.BagProto.S_PutdownDrug;
import com.message.BagProto.S_PutonDrug;
import com.message.BagProto.S_SynBagItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBagService;
import com.service.IProtoBuilderService;

/**
 * 背包接口
 * @author ken
 * @date 2017-1-4
 */
public class BagAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 整理背包
	 */
	
	public void tidyBag(GameMessage gameMessage) throws Exception {
		IBagService bagService = serviceCollection.getBagService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		bagService.tidyBag(playerId);

	}

	/**
	 * 出售背包物品
	 */
	
	public void sellItem(GameMessage gameMessage) throws Exception {
		IBagService bagService = serviceCollection.getBagService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_SellItem param = C_SellItem.parseFrom(gameMessage.getData());
		long playerBagId = param.getPlayerBagId();
		bagService.sellItem(playerId, playerBagId);
		
	}

	/**
	 * 使用背包物品
	 */
	
	public void useItem(GameMessage gameMessage) throws Exception {
		IBagService bagService = serviceCollection.getBagService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_UseItem param = C_UseItem.parseFrom(gameMessage.getData());
		long playerBagId = param.getPlayerBagId();
		int num = param.getNum();
		
		PlayerBag playerBag = bagService.useItem(playerId, playerBagId, num);
		if(playerBag == null) return;
		
		S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
		builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
		MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 装备药品栏
	 */
	
	public void putonDrug(GameMessage gameMessage) throws Exception {
		IBagService bagService = serviceCollection.getBagService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_PutonDrug param = C_PutonDrug.parseFrom(gameMessage.getData());
		int type = param.getType();
		int itemId = param.getItemId();
		
		PlayerDrug playerDrug = bagService.putonDrug(playerId, type, itemId);
		
		S_PutonDrug.Builder builder = S_PutonDrug.newBuilder();
		builder.setType(type);
		builder.setDrugLumn(protoBuilderService.buildDrugLumnMsg(playerDrug));
		MessageObj msg = new MessageObj(MessageID.S_PutonDrug_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 卸下药品
	 */
	
	public void putdownDrug(GameMessage gameMessage) throws Exception {
		IBagService bagService = serviceCollection.getBagService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_PutdownDrug param = C_PutdownDrug.parseFrom(gameMessage.getData());
		int type = param.getType();
		int itemId = param.getItemId();
		PlayerDrug playerDrug = bagService.putdownDrug(playerId, type, itemId);
		
		S_PutdownDrug.Builder builder = S_PutdownDrug.newBuilder();
		builder.setType(type);
		builder.setDrugLumn(protoBuilderService.buildDrugLumnMsg(playerDrug));
		MessageObj msg = new MessageObj(MessageID.S_PutdownDrug_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

}
