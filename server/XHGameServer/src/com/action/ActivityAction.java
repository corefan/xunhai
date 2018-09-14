package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.ActivityProto.C_GetBattleValueAward;
import com.message.ActivityProto.C_GetDailyRrechargeReward;
import com.message.ActivityProto.C_GetFristPayReward;
import com.message.ActivityProto.C_GetGiftAward;
import com.message.ActivityProto.C_GetGrowthFund;
import com.message.ActivityProto.C_GetLevelAward;
import com.message.ActivityProto.C_GetNationalWelfare;
import com.message.ActivityProto.C_GetOpenServerReward;
import com.message.ActivityProto.C_GetReward;
import com.message.ActivityProto.C_GetTotalRrechargeReward;
import com.message.ActivityProto.C_GetTotalSpendReward;
import com.message.ActivityProto.C_GetTurnRecList;
import com.message.ActivityProto.C_IdentityCheck;
import com.message.ActivityProto.C_Tomb;
import com.message.ActivityProto.C_TurntableDraw;
import com.message.ActivityProto.S_GetDailyRrechargeReward;
import com.message.ActivityProto.S_GetFristPayReward;
import com.message.ActivityProto.S_GetGrowthFund;
import com.message.ActivityProto.S_GetIdCheckAward;
import com.message.ActivityProto.S_GetLevelAward;
import com.message.ActivityProto.S_GetNationalWelfare;
import com.message.ActivityProto.S_GetReward;
import com.message.ActivityProto.S_GetTotalRrechargeReward;
import com.message.ActivityProto.S_GetTotalSpendReward;
import com.message.ActivityProto.S_IdentityCheck;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IActivityService;
import com.service.IProtoBuilderService;

/**
 * 活动接口
 * @author jiangqin
 * @date 2017-5-4
 */
 
public class ActivityAction  {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/** 获取在线奖励*/
	
	public void getOnlineReward(GameMessage gameMessage) throws Exception {
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IActivityService activityService = serviceCollection.getActivityService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_GetReward param = C_GetReward.parseFrom(gameMessage.getData());
		int rewardId = param.getId();
		
		activityService.getOnlineReward(playerId, rewardId);
		
		S_GetReward.Builder builder = S_GetReward.newBuilder();
		builder.setReward(protoBuilderService.buildRewardMsg(rewardId, 2));
		MessageObj msg = new MessageObj(MessageID.S_GetReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取奖励列表*/
	
	public void getRewardList(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		activityService.getRewardList(playerId);		
	}

	/** 获取充值活动相关数据*/
	
	public void getPayActData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getPayActData(playerId);		
	}

	/** 领取首冲奖励*/
	
	public void getFristPayReward(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetFristPayReward param = C_GetFristPayReward.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		activityService.getFristPayReward(playerId, id);
		
		S_GetFristPayReward.Builder builder = S_GetFristPayReward.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_GetFristPayReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获领取累计充值奖励*/
	
	public void getTotalRrechargeReward(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetTotalRrechargeReward param = C_GetTotalRrechargeReward.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		activityService.getTotalRrechargeReward(playerId, id);	
		
		S_GetTotalRrechargeReward.Builder builder = S_GetTotalRrechargeReward.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_GetTotalRrechargeReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 领取每日累计充值奖励*/
	
	public void getDailyRrechargeReward(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetDailyRrechargeReward param = C_GetDailyRrechargeReward.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		activityService.getDailyRrechargeReward(playerId, id);
		
		S_GetDailyRrechargeReward.Builder builder = S_GetDailyRrechargeReward.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_GetDailyRrechargeReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 领取成长基金奖励*/
	
	public void getGrowthFund(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetGrowthFund param = C_GetGrowthFund.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		activityService.getGrowthFund(playerId, id);
		
		S_GetGrowthFund.Builder builder = S_GetGrowthFund.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_GetGrowthFund_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 领取全民福利奖励*/
	
	public void getNationalWelfare(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetNationalWelfare param = C_GetNationalWelfare.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		activityService.getNationalWelfare(playerId, id);
		
		S_GetNationalWelfare.Builder builder = S_GetNationalWelfare.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_GetNationalWelfare_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取转盘抽奖相关数据*/
	
	public void getTurntableData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		activityService.getTurntableData(playerId);
	}

	/** 转盘抽奖*/
	
	public void turntableDraw(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_TurntableDraw param = C_TurntableDraw.parseFrom(gameMessage.getData());
		int type = param.getType();
		activityService.turntableDraw(playerId, type);
	}

	/** 获取陵墓面板数据 */
	
	public void getTombData(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		activityService.getTombData(playerId);
	}

	/** 抽取陵墓*/
	
	public void tomb(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Tomb param = C_Tomb.parseFrom(gameMessage.getData());
		activityService.tomb(playerId, param.getTombIndex());
	}

	/** 更换陵墓*/
	
	public void changeTomb(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		activityService.changeTomb(playerId);
	}

	/** 获取转盘抽奖榜单信息*/
	
	public void getTurnRecList(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetTurnRecList param = C_GetTurnRecList.parseFrom(gameMessage.getData());
		int start = param.getStart();
		int offset = param.getOffset(); 
		
		activityService.getTurnRecList(playerId, start, offset);		
	}

	/** 获领取累计消费奖励*/
	
	public void getTotalSpendReward(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetTotalSpendReward param = C_GetTotalSpendReward.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		activityService.getTotalSpendReward(playerId, id);
		
		S_GetTotalSpendReward.Builder builder = S_GetTotalSpendReward.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_GetTotalSpendReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 购买成长基金奖励*/
	
	public void buyGrowthFund(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.buyGrowthFund(playerId);
	}

	/** 领取开服七天乐奖励*/
	
	public void getOpenServerReward(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetOpenServerReward param = C_GetOpenServerReward.parseFrom(gameMessage.getData());
		int rewardId = param.getId();
		
		activityService.getOpenServerReward(playerId, rewardId);
	}

	/** 获取开服七天乐数据*/
	
	public void getOpenServerData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getOpenServerData(playerId);
	}

	/** 获取累计七天充值数据*/
	
	public void getSevenPayData(GameMessage gameMessage) throws Exception {
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getSevenPayData(playerId);		
	}

	/** 获取购买神器数据*/
	
	public void buyArtifactData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.buyArtifactData(playerId);			
	}

	/** 获取首充数据*/
	
	public void getFristPayData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getFristPayData(playerId);
	}	
	
	/** 领取冲级奖励*/
	public void getLevelAward(GameMessage gameMessage)throws Exception{
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetLevelAward param = C_GetLevelAward.parseFrom(gameMessage.getData());
		int rewardId = param.getId();
		
		int num = activityService.getLevelAward(playerId, rewardId);
		
		S_GetLevelAward.Builder builder = S_GetLevelAward.newBuilder();				
		builder.setId(rewardId);
		builder.setNum(num);
		MessageObj msg = new MessageObj(MessageID.S_GetLevelAward_VALUE, builder.build().toByteArray());
 		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 领取战力奖励*/
	public void getBattleValueAward(GameMessage gameMessage) throws Exception{
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetBattleValueAward param = C_GetBattleValueAward.parseFrom(gameMessage.getData());
		int rewardId = param.getId();
		
		int num = activityService.getBattleValueAward(playerId, rewardId);
		
		S_GetLevelAward.Builder builder = S_GetLevelAward.newBuilder();				
		builder.setId(rewardId);
		builder.setNum(num);
		MessageObj msg = new MessageObj(MessageID.S_GetBattleValueAward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取冲级奖励数据*/
	public void getLevelAwardData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getLevelAwardData(playerId);
	}		

	/** 获取战力奖励数据*/
	public void getBVAwardData(GameMessage gameMessage){
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getBVAwardData(playerId);
	}
	
	/**
	 * 使用激活码
	 */
	public void useActCode(GameMessage gameMessage)throws Exception{ 
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetGiftAward param = C_GetGiftAward.parseFrom(gameMessage.getData());
	
		activityService.useActCode(playerId, param.getGiftCode());
	}
	
	/** 获取实名认证信息*/
	public void getIdentCheckInfo(GameMessage gameMessage)throws Exception{ 
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getIdentCheckInfo(playerId);
	}
	
	/** 实名认证*/
	public void identityCheck(GameMessage gameMessage)throws Exception{ 
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		C_IdentityCheck param = C_IdentityCheck.parseFrom(gameMessage.getData());
		
		int state = activityService.identityCheck(playerId, param.getIdentity(), param.getRealName());
		
		S_IdentityCheck.Builder builder = S_IdentityCheck.newBuilder();
		builder.setIcState(state);
		MessageObj msg = new MessageObj(MessageID.S_IdentityCheck_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/** 领取实名认证奖励*/
	public void getIdCheckAward(GameMessage gameMessage)throws Exception{  
		IActivityService activityService = serviceCollection.getActivityService();	
		long playerId = gameMessage.getConnection().getPlayerId();
		
		activityService.getIdCheckAward(playerId);
		
		S_GetIdCheckAward.Builder builder = S_GetIdCheckAward.newBuilder();
		builder.setRewardState(1);
		MessageObj msg = new MessageObj(MessageID.S_GetIdCheckAward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
}
