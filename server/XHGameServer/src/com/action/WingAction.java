package com.action;

import java.util.Map;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.player.PlayerWealth;
import com.domain.wing.PlayerWing;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.WingProto.C_Evolve;
import com.message.WingProto.C_PutdownWing;
import com.message.WingProto.C_PutonWing;
import com.message.WingProto.C_UnEvolve;
import com.message.WingProto.S_PutdownWing;
import com.message.WingProto.S_PutonWing;
import com.message.WingProto.S_SynWingList;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IWingService;

/** 
 * 羽翼
 * @author jiangqin
 * @date 2017-5-8
 */
public class WingAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/** 获取羽毛列表*/	
	
	public void getWingList(GameMessage gameMessage) throws Exception {
		IWingService wingService = serviceCollection.getWingService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		Map<Integer, PlayerWing> playerWingMap = wingService.getPlayerWingMap(playerId);
		S_SynWingList.Builder builder = S_SynWingList.newBuilder();
		for(Map.Entry<Integer, PlayerWing> entry : playerWingMap.entrySet()){
			PlayerWing playerWing = entry.getValue();
			builder.addListWing(protoBuilderService.buildWingMsg(playerWing));
		}
		
		PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
		builder.setTotalWingValue(playerWealth.getWingValue());
		
		MessageObj msg = new MessageObj(MessageID.S_SynWingList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);		
		
	}
	
	/** 穿戴羽翼*/
	
	public void putOnWing(GameMessage gameMessage) throws Exception {
		IWingService wingService = serviceCollection.getWingService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		C_PutonWing param = C_PutonWing.parseFrom(gameMessage.getData());
		int wingId = param.getWingId();		
		wingService.putOnWing(playerId, wingId);			
		
		S_PutonWing.Builder builder = S_PutonWing.newBuilder();			
		builder.setWingId(wingId);			
		MessageObj msg = new MessageObj(MessageID.S_PutonWing_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/** 卸载羽翼*/
	
	public void putDownWing(GameMessage gameMessage) throws Exception {	
		IWingService wingService = serviceCollection.getWingService();
		long playerId = gameMessage.getConnection().getPlayerId();	
		
		C_PutdownWing param = C_PutdownWing.parseFrom(gameMessage.getData());
		int wingId = param.getWingId();		
		wingService.putDownWing(playerId, wingId);	
		
		S_PutdownWing.Builder builder = S_PutdownWing.newBuilder();			
		builder.setWingId(wingId);		
		MessageObj msg = new MessageObj(MessageID.S_PutdownWing_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/** 羽化*/
	
	public void evolve(GameMessage gameMessage) throws Exception {		
		IWingService wingService = serviceCollection.getWingService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Evolve param = C_Evolve.parseFrom(gameMessage.getData());
		int wingId = param.getWingId();		
		int itemId = param.getItemId();
		int type = param.getType();
				
		wingService.evolve(playerId, type, wingId, itemId);		
	}
	
	/** 羽翼降解*/
	
	public void unEvolve(GameMessage gameMessage) throws Exception {
		IWingService wingService = serviceCollection.getWingService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_UnEvolve param = C_UnEvolve.parseFrom(gameMessage.getData());
		int wingId = param.getWingId();	
	
		wingService.unEvolve(playerId, wingId);		
	}	
}
