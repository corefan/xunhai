package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.ExceptionConstant;
import com.core.GameMessage;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.PlayerEquipment;
import com.message.EquipmentProto.C_ComposeEquip;
import com.message.EquipmentProto.C_InheritEquip;
import com.message.EquipmentProto.C_PutDownEquipment;
import com.message.EquipmentProto.C_PutOnEquipment;
import com.message.EquipmentProto.C_ShowEquipment;
import com.message.EquipmentProto.C_StrongEquip;
import com.message.EquipmentProto.S_ComposeEquip;
import com.message.EquipmentProto.S_InheritEquip;
import com.message.EquipmentProto.S_ShowEquipment;
import com.message.EquipmentProto.S_StrongEquip;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IEquipmentService;

/**
 * 装备接口
 * @author ken
 * @date 2017-1-5
 */
public class EquipmentAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 穿装备
	 */
	
	public void putOnEquipment(GameMessage gameMessage) throws Exception {
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_PutOnEquipment param =  C_PutOnEquipment.parseFrom(gameMessage.getData());
		long onPlayerEquipmentId = param.getPlayerEquipmentId();	
		
		equipmentService.putOnEquipment(playerId, onPlayerEquipmentId);
		
	}

	/**
	 * 脱装备
	 */
	
	public void putDownEquipment(GameMessage gameMessage) throws Exception {
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_PutDownEquipment param = C_PutDownEquipment.parseFrom(gameMessage.getData());
		long playerEquipmentId = param.getPlayerEquipmentId();
		
		equipmentService.putDownEquipment(playerId, playerEquipmentId);
		
	}

	/**
	 * 装备信息展示
	 */
	
	public void showEquipment(GameMessage gameMessage) throws Exception {
		
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		long playerId = gameMessage.getConnection().getPlayerId();
		C_ShowEquipment param = C_ShowEquipment.parseFrom(gameMessage.getData());
		
		long showPlayerId = param.getShowPlayerId();
		long showPlayerEquipmentId = param.getPlayerEquipmentId();
		
		if(playerId <= 0 || showPlayerId <= 0 || showPlayerEquipmentId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		PlayerEquipment showEquipment = equipmentService.getPlayerEquipmentByID(showPlayerId, showPlayerEquipmentId);
		if(showEquipment == null){
			throw new GameException(ExceptionConstant.EQUIP_1400);
		}
		
		S_ShowEquipment.Builder builder = S_ShowEquipment.newBuilder();
		builder.setShowEquipment(serviceCollection.getProtoBuilderService().buildPlayerEquipmentMsg(showEquipment));			
		MessageObj msg = new MessageObj(MessageID.S_ShowEquipment_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 强化装备
	 */
	public void strongEquip(GameMessage gameMessage) throws Exception {
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		long playerId = gameMessage.getConnection().getPlayerId();

		C_StrongEquip param = C_StrongEquip.parseFrom(gameMessage.getData());
		long playerEquipId = param.getPlayerEquipId();
		int luckItem = param.getLuckItem();
		
		int result = equipmentService.strongEquip(playerId, playerEquipId, luckItem);
		
		S_StrongEquip.Builder builder = S_StrongEquip.newBuilder();
		builder.setResult(result);
		MessageObj msg = new MessageObj(MessageID.S_StrongEquip_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 合成装备
	 */
	public void composeEquip(GameMessage gameMessage) throws Exception {
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ComposeEquip param = C_ComposeEquip.parseFrom(gameMessage.getData());
		List<Long> playerEquipIds = param.getPlayerEquipIdsList();
		
		equipmentService.composeEquip(playerId, playerEquipIds);
		
		S_ComposeEquip.Builder builder = S_ComposeEquip.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_ComposeEquip_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 传承装备
	 */
	public void inheritEquip(GameMessage gameMessage) throws Exception {
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_InheritEquip param = C_InheritEquip.parseFrom(gameMessage.getData());
		long playerEquipId = param.getPlayerEquipId();
		long targetEquipId = param.getTargetEquipId();
		
		equipmentService.inheritEquip(playerId, playerEquipId, targetEquipId);
		
		S_InheritEquip.Builder builder = S_InheritEquip.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_InheritEquip_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendData(gameMessage.getConnection(), msg);
	}
}
