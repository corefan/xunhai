package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.MailConstant;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.mail.MailInbox;
import com.message.MailProto.C_DeleteMail;
import com.message.MailProto.C_GetMailPageList;
import com.message.MailProto.C_ReadMail;
import com.message.MailProto.C_ReceiveAttachment;
import com.message.MailProto.S_DeleteMail;
import com.message.MailProto.S_GetMailPageList;
import com.message.MailProto.S_ReadMail;
import com.message.MailProto.S_ReceiveAttachment;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IMailService;
import com.service.IProtoBuilderService;

/**
 * 邮件接口
 * @author ken
 * @date 2017-2-14
 */
public class MailAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 获取邮件列表 分页
	 */
	
	public void getMailPageList(GameMessage gameMessage) throws Exception {
		IMailService mailService = serviceCollection.getMailService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetMailPageList param = C_GetMailPageList.parseFrom(gameMessage.getData());
		int start = param.getStart();
		int offset = param.getOffset();
		List<MailInbox> inboxPageList = mailService.getMailInboxPageListByplayerId(playerId, start, offset);

		S_GetMailPageList.Builder builder = S_GetMailPageList.newBuilder();
		if( mailService.getPlayerMailInboxByState(playerId, MailConstant.MAIL_STATE_NEW) != null){
			int newMailNum = mailService.getPlayerMailInboxByState(playerId, MailConstant.MAIL_STATE_NEW).size();
			builder.setNewMailNum(newMailNum);
		}	
		
		builder.setInboxMailNum(mailService.getPlayerMailInboxList(playerId).size());
		
		for(MailInbox mailInbox : inboxPageList){
			builder.addInboxPageList(protoBuilderService.buildMailInboxMsg(mailInbox));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetMailPageList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 读取收件箱邮件
	 */
	 
	public void readMail(GameMessage gameMessage) throws Exception {
		IMailService mailService = serviceCollection.getMailService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ReadMail param = C_ReadMail.parseFrom(gameMessage.getData());
		long mailInboxId = param.getMailInboxID();
		MailInbox mailInbox = mailService.readInboxMail(playerId, mailInboxId);

		S_ReadMail.Builder builder = S_ReadMail.newBuilder();
		builder.setMailInbox(protoBuilderService.buildMailInboxMsg(mailInbox));
		
		MessageObj msg = new MessageObj(MessageID.S_ReadMail_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
		
	}

	/**
	 * 领取收件附件
	 */
	
	public void receiveAttachment(GameMessage gameMessage) throws Exception {
		IMailService mailService = serviceCollection.getMailService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ReceiveAttachment param = C_ReceiveAttachment.parseFrom(gameMessage.getData());
		long mailInboxId = param.getMailInboxID();
		
		mailService.receiveAttachment(playerId, mailInboxId);
		
		S_ReceiveAttachment.Builder builder = S_ReceiveAttachment.newBuilder();
		builder.setMailInboxID(mailInboxId);
		
		MessageObj msg = new MessageObj(MessageID.S_ReceiveAttachment_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 根据邮件编号删除收件
	 */
	
	public void deleteMail(GameMessage gameMessage) throws Exception {
		IMailService mailService = serviceCollection.getMailService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_DeleteMail param = C_DeleteMail.parseFrom(gameMessage.getData());
		long mailInboxId = param.getMailInboxID();
		
		mailService.deleteMailInboxByID(playerId, mailInboxId);
		
		S_DeleteMail.Builder builder = S_DeleteMail.newBuilder();
		builder.setMailInboxID(mailInboxId);
		
		MessageObj msg = new MessageObj(MessageID.S_DeleteMail_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

}
