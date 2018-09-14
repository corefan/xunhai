package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ExceptionConstant;
import com.core.GameMessage;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.friend.PlayerApply;
import com.domain.player.Player;
import com.message.FriendProto.ApplyMsg;
import com.message.FriendProto.C_ApplyAddFriend;
import com.message.FriendProto.C_ApplyDeal;
import com.message.FriendProto.C_DeleteFriend;
import com.message.FriendProto.C_FriendList;
import com.message.FriendProto.C_SerachFriend;
import com.message.FriendProto.FriendMsg;
import com.message.FriendProto.S_ApplyAddFriend;
import com.message.FriendProto.S_ApplyDeal;
import com.message.FriendProto.S_ApplyMsgList;
import com.message.FriendProto.S_DeleteFriend;
import com.message.FriendProto.S_FriendList;
import com.message.FriendProto.S_SerachFriend;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFriendService;
import com.service.IProtoBuilderService;

/**
 * 好友接口
 * @author ken
 * @date 2018年7月11日
 */
public class FriendAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
	
	/**
	 * 获取好友列表
	 */
	
	public void applyFriend(GameMessage gameMessage) throws Exception {
		IFriendService friendService = serviceCollection.getFriendService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();
		
		//获取要添加的好友的编号
		C_ApplyAddFriend param = C_ApplyAddFriend.parseFrom(gameMessage.getData());
		long applyPlayerId = param.getApplyPlayerId();
		
		PlayerApply playerApply = friendService.addPlayerFriend(playerId, applyPlayerId);
		S_ApplyAddFriend.Builder builder = S_ApplyAddFriend.newBuilder();	
		ApplyMsg.Builder applyMsgMsg = protoBuilderService.builderApplyMsg(playerApply);
		builder.setApplyMsg(applyMsgMsg);		
		MessageObj msg = new MessageObj(MessageID.S_ApplyAddFriend_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(applyPlayerId,  msg);
	}	
	
	/**
	 * 获取好友列表
	 */
	
	public void getFriendList(GameMessage gameMessage) throws Exception {	
		IFriendService friendService = serviceCollection.getFriendService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		C_FriendList param = C_FriendList.parseFrom(gameMessage.getData());
		int type = param.getType();
		
		List<Long> listFriendId = friendService.getFriendList(playerId, type);
		S_FriendList.Builder builder = S_FriendList.newBuilder();		
		builder.setType(type);
		for(Long friendId : listFriendId){
			FriendMsg.Builder friendMsg = protoBuilderService.buildFriendMsg(friendId);
			if(friendMsg == null) continue;
			builder.addFriendList(friendMsg);
		}		
		MessageObj msg = new MessageObj(MessageID.S_FriendList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 删除好友
	 */
	
	public void deleteFriend(GameMessage gameMessage) throws Exception {
		IFriendService friendService = serviceCollection.getFriendService();
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_DeleteFriend param = C_DeleteFriend.parseFrom(gameMessage.getData());
		long deletePlayerId = param.getDeletePlayerId();
		friendService.deleteFriend(playerId, deletePlayerId);
		
		//发送消息给玩家
		S_DeleteFriend.Builder builder = S_DeleteFriend.newBuilder();
		builder.setDeletePlayerId(deletePlayerId);
		MessageObj msg = new MessageObj(MessageID.S_DeleteFriend_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
		
		//发送消息给被删除的玩家
		S_DeleteFriend.Builder builder1 = S_DeleteFriend.newBuilder();
		builder1.setDeletePlayerId(playerId);
		MessageObj msg1 = new MessageObj(MessageID.S_DeleteFriend_VALUE, builder1.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(deletePlayerId, msg1);
	}

	/**
	 * 获取取消息列表
	 */
	
	public void getApplyMsgList(GameMessage gameMessage) throws Exception {
		IFriendService friendService = serviceCollection.getFriendService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();
		S_ApplyMsgList.Builder builder = S_ApplyMsgList.newBuilder();
		
		List<PlayerApply> applyList = friendService.listPlayerApplys(playerId);
		
		for(PlayerApply playerApply : applyList){
			ApplyMsg.Builder applyMsgMsg = protoBuilderService.builderApplyMsg(playerApply);
			builder.addApplyMsgList(applyMsgMsg);			
		}		
		MessageObj msg = new MessageObj(MessageID.S_ApplyMsgList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);		
	}

	/**
	 * 搜索好友
	 */
	
	public void searchFriend(GameMessage gameMessage) throws Exception {
		IFriendService friendService = serviceCollection.getFriendService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		// 获取玩家的编号		
		C_SerachFriend param = C_SerachFriend.parseFrom(gameMessage.getData());
		String playerName = param.getPlayerName();
		
		S_SerachFriend.Builder builder = S_SerachFriend.newBuilder();
		Player player = friendService.searchFriend(playerName);
		
		if(player == null) throw new GameException(ExceptionConstant.PLAYER_1115);
		
		FriendMsg.Builder friendModelMsg = protoBuilderService.buildFriendMsg(player.getPlayerId());		
		builder.setFriendMsg(friendModelMsg);		
		MessageObj msg = new MessageObj(MessageID.S_SerachFriend_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 好友消息处理
	 */
	
	public void applyDeal(GameMessage gameMessage) throws Exception {		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IFriendService friendService = serviceCollection.getFriendService();		
		// 获取玩家的编号
		long playerId = gameMessage.getConnection().getPlayerId();		
		//获取要添加的好友的编号
		C_ApplyDeal param = C_ApplyDeal.parseFrom(gameMessage.getData());
		long applyPlayerId = param.getApplyPlayerId();
		int state = param.getState();	
		friendService.applyDeal(playerId, applyPlayerId, state);	
		if (state == 0){
			//消息发送给applyPlayerId
			FriendMsg.Builder playerFriendMsg = protoBuilderService.buildFriendMsg(playerId);
			if(playerFriendMsg == null) return;
			
			S_ApplyDeal.Builder builder = S_ApplyDeal.newBuilder();			
			builder.setFriendMsg(playerFriendMsg);				
			builder.setState(state);
			MessageObj msgToApply = new MessageObj(MessageID.S_ApplyDeal_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(applyPlayerId, msgToApply);			
			return ;
		}
		
		//消息发送给PlayerId
		FriendMsg.Builder applyFriendMsg = protoBuilderService.buildFriendMsg(applyPlayerId);
		if(applyFriendMsg != null){
			S_ApplyDeal.Builder builder1 = S_ApplyDeal.newBuilder();
			builder1.setFriendMsg(applyFriendMsg);	
			builder1.setState(state);
			MessageObj msg = new MessageObj(MessageID.S_ApplyDeal_VALUE, builder1.build().toByteArray());
			gameSocketService.sendData(gameMessage.getConnection(), msg);
		}
		
		//消息发送给applyPlayerId
		S_ApplyDeal.Builder builder2 = S_ApplyDeal.newBuilder();
		FriendMsg.Builder playerFriendMsg = protoBuilderService.buildFriendMsg(playerId);
		if(playerFriendMsg != null){
			builder2.setFriendMsg(playerFriendMsg);				
			builder2.setState(state);
			MessageObj msgToApply = new MessageObj(MessageID.S_ApplyDeal_VALUE, builder2.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(applyPlayerId, msgToApply);	
		}
		
	}

	/**
	 * 清空申请列表
	 */
	
	public void deleteAllApply(GameMessage gameMessage){
		IFriendService friendService = serviceCollection.getFriendService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		friendService.deleteAllApply(playerId);
		
	}

	/**
	 * 同意全部申请信息
	 */
	
	public void agreeAllApply(GameMessage gameMessage) throws Exception {
		IFriendService friendService = serviceCollection.getFriendService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		friendService.agreeAllApply(playerId);		
	}
}
