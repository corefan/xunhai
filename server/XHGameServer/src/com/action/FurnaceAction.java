package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.furnace.PlayerFurnace;
import com.message.FurnaceProto.C_UpgradeFurnace;
import com.message.FurnaceProto.S_GetPlayerFurnaceList;
import com.message.FurnaceProto.S_UpgradeFurnace;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFurnaceService;
import com.service.IProtoBuilderService;

/**
 * 熔炉接口
 * @author ken
 * @date 2018年4月23日
 */
public class FurnaceAction {
	
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 获取熔炉列表
	 */
	
	public void getPlayerFurnaceList(GameMessage gameMessage) throws Exception {
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IFurnaceService furnaceService = serviceCollection.getFurnaceService();
		
		long playerId = gameMessage.getConnection().getPlayerId();	
		
		List<PlayerFurnace> lists = furnaceService.getPlayerFurnaceList(playerId);
		
		S_GetPlayerFurnaceList.Builder builder = S_GetPlayerFurnaceList.newBuilder();
		
		for(PlayerFurnace model : lists){
			builder.addFurnaceList(protoBuilderService.buildPlayerFurnaceMsg(model));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetPlayerFurnaceList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 升级熔炉
	 */
	
	public void upgradeFurnace(GameMessage gameMessage) throws Exception {
		IFurnaceService furnaceService = serviceCollection.getFurnaceService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		long playerId = gameMessage.getConnection().getPlayerId();	
		
		C_UpgradeFurnace param = C_UpgradeFurnace.parseFrom(gameMessage.getData());
		
		PlayerFurnace model = furnaceService.upgradeFurnace(playerId, param.getFurnaceId());
		
		S_UpgradeFurnace.Builder builder = S_UpgradeFurnace.newBuilder();
		builder.setPlayerFurnace(protoBuilderService.buildPlayerFurnaceMsg(model));
		MessageObj msg = new MessageObj(MessageID.S_UpgradeFurnace_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

}
