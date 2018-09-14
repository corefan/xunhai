package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.vip.PlayerVip;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.VipProto.C_GetVipActReward;
import com.message.VipProto.S_GetDailyReward;
import com.message.VipProto.S_GetPlayerVip;
import com.message.VipProto.S_GetVipActReward;
import com.service.IVipService;

/**
 * vip接口
 * @author ken
 * @date 2018年7月11日
 */
public class VipAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();

	/** 获取vip激活奖励*/
	
	public void GetVipActReward(GameMessage gameMessage) throws Exception {
		IVipService vipService = serviceCollection.getVipService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetVipActReward param = C_GetVipActReward.parseFrom(gameMessage.getData());
		int vipId = param.getVipId();
		
		vipService.GetVipActReward(playerId, vipId);
		
		S_GetVipActReward.Builder builder = S_GetVipActReward.newBuilder();
		builder.setVipId(vipId);		
		builder.setRewardState(2);
		
		MessageObj msg = new MessageObj(MessageID.S_GetVipActReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 领取每日奖励(vip双倍)*/
	
	public void GetDailyReward(GameMessage gameMessage) throws Exception {
		IVipService vipService = serviceCollection.getVipService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		vipService.GetDailyReward(playerId);
	
		S_GetDailyReward.Builder builder = S_GetDailyReward.newBuilder();
		builder.setState(1);
		
		MessageObj msg = new MessageObj(MessageID.S_GetDailyReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取玩家vip信息*/
	
	public void getPlayerVip(GameMessage gameMessage) throws Exception {
		IVipService vipService = serviceCollection.getVipService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		PlayerVip playerVip = vipService.getPlayerVip(playerId);
		S_GetPlayerVip.Builder builder = S_GetPlayerVip.newBuilder();
		if(playerVip != null){
			builder.setVipLevel(playerVip.getLevel());
			builder.setInvalidTime(playerVip.getVaildTime());
			
			builder.addRewardState(playerVip.getVipLv1State());
			builder.addRewardState(playerVip.getVipLv2State());
			builder.addRewardState(playerVip.getVipLv3State());
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetPlayerVip_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取玩家每日奖励领取状态*/
	
	public void getGetDailyRewardState(GameMessage gameMessage)	throws Exception {
		IVipService vipService = serviceCollection.getVipService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		vipService.getGetDailyRewardState(playerId);
	}

	/** 领取vip每日福利(月卡)*/
	
	public void GetVipWelfare(GameMessage gameMessage) throws Exception {
		IVipService vipService = serviceCollection.getVipService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		vipService.GetVipWelfare(playerId);
	}

	/** 获取vip每日福利领取状态*/
	
	public void GetVipWelfareState(GameMessage gameMessage){
		IVipService vipService = serviceCollection.getVipService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		vipService.GetVipWelfareState(playerId);		
	}

}
