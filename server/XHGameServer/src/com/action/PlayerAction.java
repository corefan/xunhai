package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.player.PlayerOptional;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.PlayerProto.C_Pay;
import com.message.PlayerProto.C_QuickTips;
import com.message.PlayerProto.C_SetIsAcceptApply;
import com.message.PlayerProto.C_SetIsAcceptChat;
import com.message.PlayerProto.S_GetPlayerOptional;
import com.message.PlayerProto.S_SetIsAcceptApply;
import com.message.PlayerProto.S_SetIsAcceptChat;
import com.message.SceneProto.C_ShowPlayer;
import com.service.IChatService;
import com.service.IFriendService;
import com.service.IPlayerService;

/**
 * 玩家接口
 * @author ken
 * @date 2017-6-20
 */
public class PlayerAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
	
	/**
	 * 角色展示信息
	 */
	
	public void getShowPlayer(GameMessage gameMessage) throws Exception {
		C_ShowPlayer param = C_ShowPlayer.parseFrom(gameMessage.getData());
		long playerId = gameMessage.getConnection().getPlayerId();
		
		long showPlayerId = param.getPlayerId();
		serviceCollection.getPlayerService().getShowPlayer(playerId, showPlayerId);		
	}

	/**
	 * 获取支付信息
	 */
	
	public void getPayInfo(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Pay param = C_Pay.parseFrom(gameMessage.getData());
		int payItemId = param.getPayItemId();
		int payType = param.getPayType();
		
		serviceCollection.getPayService().getPayInfo(playerId, payItemId, payType);
	}

	/**
	 * 获取已首冲的列表
	 */
	
	public void getFristPayIdList(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();		
		serviceCollection.getPayService().getFristPayIdList(playerId);
	}

	/**
	 * 玩家信息提示框
	 */
	
	public void quickTips(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();	
		
		C_QuickTips param = C_QuickTips.parseFrom(gameMessage.getData());
		
		long tipPlayerId = param.getPlayerId();
		serviceCollection.getPlayerService().quickTips(playerId, tipPlayerId);
	}

	/** 设置是否接受陌生人信息 */
	
	public void setIsAcceptChat(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();	
		IChatService chatService = serviceCollection.getChatService();
		
		C_SetIsAcceptChat param = C_SetIsAcceptChat.parseFrom(gameMessage.getData());
		int state = param.getState();
		
		chatService.setIsAcceptChat(playerId, state);
		
		S_SetIsAcceptChat.Builder builder = S_SetIsAcceptChat.newBuilder();
		builder.setState(state);
		MessageObj msg = new MessageObj(MessageID.S_SetIsAcceptChat_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 设置是否接受好友申请
	 */
	
	public void setIsAcceptApply(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();	
		IFriendService friendService = serviceCollection.getFriendService();
		
		C_SetIsAcceptApply param = C_SetIsAcceptApply.parseFrom(gameMessage.getData());
		int state = param.getState();
		
		friendService.setIsAcceptApply(playerId, state);
		
		S_SetIsAcceptApply.Builder builder = S_SetIsAcceptApply.newBuilder();
		builder.setState(state);
		MessageObj msg = new MessageObj(MessageID.S_SetIsAcceptApply_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
		
	}

	/**
	 * 根据玩家编号取操作信息
	 */	
	
	public void getPlayerOptional(GameMessage gameMessage) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		long playerId = gameMessage.getConnection().getPlayerId();			
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		
		S_GetPlayerOptional.Builder builder = S_GetPlayerOptional.newBuilder();
		builder.setIsAcceptApply(playerOptional.getIsAcceptApply());
		builder.setIsAcceptChat(playerOptional.getIsAcceptChat());
		
		MessageObj msg = new MessageObj(MessageID.S_GetPlayerOptional_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
		
	}
}
