package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.EquipmentProto.C_Epigraph;
import com.message.EquipmentProto.S_Epigraph;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IEpigraphService;

/**
 * 铭文接口
 * @author ken
 * @date 2018年7月11日
 */
public class EpigraphAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 铭文
	 */
	
	public void epigraph(GameMessage gameMessage) throws Exception{
		//获取角色编号Id
		long playerID = gameMessage.getConnection().getPlayerId();		
		
		C_Epigraph param = C_Epigraph.parseFrom(gameMessage.getData());
		long playerEquipmentID = param.getPlayerEquipmentId();
		int holdId = param.getHoleId();
		long playerBagId = param.getPlayerBagId();
		
		IEpigraphService epigraphService = serviceCollection.getEpigraphService();		
		epigraphService.epigraph(playerID, playerEquipmentID, holdId, playerBagId);
		
		S_Epigraph.Builder builder = S_Epigraph.newBuilder();	
		builder.setHoleId(holdId);
		MessageObj msg = new MessageObj(MessageID.S_Epigraph_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
}
