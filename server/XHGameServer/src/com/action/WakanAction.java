package com.action;

import java.util.List;
import java.util.Map;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.wakan.PlayerWakan;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.WakanProto.C_TakeWakan;
import com.message.WakanProto.S_WakanList;
import com.message.WakanProto.WakanMsg;
import com.service.IProtoBuilderService;
import com.service.IWakanService;

/**
 * 注灵系统
 * @author jiangqin
 * @date 2017-2-22
 */
public class WakanAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 获取玩家装备位注灵信息
	 */
	
	public void wakanList(GameMessage gameMessage){
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IWakanService wakanService = serviceCollection.getWakanService();
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		Map<Integer, PlayerWakan>  playerWakanMap = wakanService.getPlayerWakanMap(playerId);
		
		S_WakanList.Builder builder = S_WakanList.newBuilder();
		
		for(Map.Entry<Integer, PlayerWakan> entry : playerWakanMap.entrySet()){
			PlayerWakan model = entry.getValue();
			WakanMsg.Builder wakanMsg = protoBuilderService.buildWakanMsg(model);	
			builder.addWakanList(wakanMsg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_WakanList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);		
	}

	/**
	 * 注灵  
	 */
	
	public void takeWakan(GameMessage gameMessage) throws Exception {
		IWakanService wakanService = serviceCollection.getWakanService();
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		C_TakeWakan parm = C_TakeWakan.parseFrom(gameMessage.getData());
		int posId = parm.getPosId();
		List<Integer> itemList = parm.getListItemsList();
		wakanService.takeWakan(playerId, posId, itemList);		
	}

}
