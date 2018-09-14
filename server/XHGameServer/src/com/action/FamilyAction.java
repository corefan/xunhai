package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.FamilyProto.C_ChangeFamilyLeader;
import com.message.FamilyProto.C_ChangeFamilyNotice;
import com.message.FamilyProto.C_ChangeFamilyPlayerTitle;
import com.message.FamilyProto.C_ChangeFamilySortId;
import com.message.FamilyProto.C_CreateFamily;
import com.message.FamilyProto.C_FamilyFB;
import com.message.FamilyProto.C_InviteJoinFamily;
import com.message.FamilyProto.C_InviteMsgDeal;
import com.message.FamilyProto.C_KickFamilyPlayer;
import com.message.FamilyProto.S_FamilyFB;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFamilyService;

/**
 * 家族接口
 * @author jiangqin
 * @date 2017-4-6
 */
public class FamilyAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/** 获取家族信息*/
	
	public void getFamilyData(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		familyService.getFamilyData(playerId);		
	}

	/** 创建家族*/
	
	public void createFamily(GameMessage gameMessage) throws Exception {
		
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_CreateFamily param = C_CreateFamily.parseFrom(gameMessage.getData());
		String familyName = param.getFamilyName();			
		familyService.createFamily(playerId, familyName);
	}

	/** 解散家族*/
	
	public void disbandFamily(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		familyService.disbandFamily(playerId);		
	}

	/** 邀请加入家族*/
	
	public void inviteJoinFamily(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		C_InviteJoinFamily param = C_InviteJoinFamily.parseFrom(gameMessage.getData());
		long frinedId = param.getFriendId();
		familyService.inviteJoinFamily(playerId, frinedId);
	}

	/** 退出家族*/
	
	public void exitFamily(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		familyService.exitFamily(playerId);
		
	}

	/** 族长转让*/
	
	public void changeFamilyLeader(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeFamilyLeader param = C_ChangeFamilyLeader.parseFrom(gameMessage.getData());		
		long targetPlayerId = param.getNewLeaderPlayerId();		
		familyService.changeFamilyLeader(playerId, targetPlayerId);		
	}

	/** 调整家族成员排位*/
	
	public void changeFamilySortId(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeFamilySortId param = C_ChangeFamilySortId.parseFrom(gameMessage.getData());
		familyService.changeFamilySortId(playerId, param.getSortIdList());
	}
	
	/** 编辑家族公告*/
	
	public void changeFamilyNotice(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeFamilyNotice param = C_ChangeFamilyNotice.parseFrom(gameMessage.getData());
		String notice = param.getMsg();
		familyService.changeFamilyNotice(playerId, notice);
		
	}
	
	/** 踢出成员*/
	
	public void kickFamilyPlayer(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_KickFamilyPlayer param = C_KickFamilyPlayer.parseFrom(gameMessage.getData());
		long targetPlayerId = param.getKickPlayerId();
		familyService.kickFamilyPlayer(playerId, targetPlayerId);
	}
	
	/** 修改成员称谓*/
	
	public void changeFamilyPlayerTitle(GameMessage gameMessage)throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeFamilyPlayerTitle param = C_ChangeFamilyPlayerTitle.parseFrom(gameMessage.getData());
		long targetPlayerId = param.getChangePlayerId();
		String title = param.getTitle();
		familyService.changeFamilyPlayerTitle(playerId, targetPlayerId, title);	
		
	}
	
	/** 邀请信息处理*/
	
	public void inviteMsgDeal(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_InviteMsgDeal param = C_InviteMsgDeal.parseFrom(gameMessage.getData());	
		long playerFamilyId = param.getPlayerFamilyId();
		int state = param.getState();		
		familyService.inviteMsgDeal(playerId, playerFamilyId, state);
	}
	
	/**
	 * 家族副本
	 */
	public void familyFB(GameMessage gameMessage) throws Exception {
		IFamilyService familyService = serviceCollection.getFamilyService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_FamilyFB param = C_FamilyFB.parseFrom(gameMessage.getData());	
		int type = param.getType();
		
		familyService.familyFB(playerId, type);
		
		S_FamilyFB.Builder builder = S_FamilyFB.newBuilder();
		builder.setType(type);
		MessageObj msg = new MessageObj(MessageID.S_FamilyFB_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
}
