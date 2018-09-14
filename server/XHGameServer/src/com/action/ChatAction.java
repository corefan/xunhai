package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ExceptionConstant;
import com.core.GameMessage;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.chat.Voice;
import com.google.protobuf.ByteString;
import com.message.ChatProto.C_Chat;
import com.message.ChatProto.C_GetVoice;
import com.message.ChatProto.C_PostVoice;
import com.message.ChatProto.S_GetVoice;
import com.message.ChatProto.S_PostVoice;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IChatService;

/**
 * 聊天系统
 * @author ken
 * @date 2018年7月11日
 */
public class ChatAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/** 聊天 */
	
	public void chat(GameMessage gameMessage) throws Exception {
		IChatService chatService = serviceCollection.getChatService();	
		
		long playerId =  gameMessage.getConnection().getPlayerId();	
		C_Chat parma = C_Chat.parseFrom(gameMessage.getData());		
	
		int type = parma.getType();
		long toPlayerId = parma.getToPlayerId();
		String content = parma.getContent();		
		String param = parma.getParam();
			
		chatService.chat(playerId, 0, content, type, toPlayerId, param);
	}

	/** 上传语音数据 */
	
	public void postVoice(GameMessage gameMessage) throws Exception {
		IChatService chatService = serviceCollection.getChatService();	
		
		C_PostVoice param = C_PostVoice.parseFrom(gameMessage.getData());
		String id = param.getId();
		ByteString voice = param.getVoice();
		
		if(voice == null || voice.isEmpty())  throw new GameException(ExceptionConstant.CHAT_2005);
		chatService.postVoice(id, voice);
		
		S_PostVoice.Builder builder = S_PostVoice.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_PostVoice_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取语音数据 */
	
	public void getVoice(GameMessage gameMessage) throws Exception {
		IChatService chatService = serviceCollection.getChatService();	
		
		C_GetVoice param = C_GetVoice.parseFrom(gameMessage.getData());
		String id = param.getId();
		
		Voice voice = chatService.getVoice(id);		
		if(voice == null)  throw new GameException(ExceptionConstant.CHAT_2008);
		
		S_GetVoice.Builder builder = S_GetVoice.newBuilder();
		builder.setVoice(voice.getVoice());
		MessageObj msg = new MessageObj(MessageID.S_GetVoice_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取离线消息 */
	
	public void getOfflineInfo(GameMessage gameMessage) throws Exception {
		IChatService chatService = serviceCollection.getChatService();	
		
		long playerId =  gameMessage.getConnection().getPlayerId();	
		
		chatService.getOfflineInfo(playerId);
	}
}

	

	
	
	
	
	

	
	


