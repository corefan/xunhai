package com.service.impl;

import org.json.JSONObject;

import com.common.Config;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ExceptionConstant;
import com.constant.HttpConstant;
import com.constant.RewardConstant;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.player.Player;
import com.domain.player.PlayerOptional;
import com.domain.reward.BaseReward;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.PlayerProto.S_BindPhone;
import com.message.PlayerProto.S_GetBindInfo;
import com.message.PlayerProto.S_GetValidateCode;
import com.service.IPlayerService;
import com.service.IRewardService;
import com.service.ISmsService;
import com.util.HttpUtil;
import com.util.PhoneFormatCheckUtil;

/**
 * 手机绑定
 * @author jiangqin
 * @date 2017-7-21
 */
public class SmsService implements ISmsService{

	@Override
	public void getValidateCode(long playerId, long telePhone) throws Exception {
		if(playerId < 1 || telePhone < 1) throw new GameException(ExceptionConstant.ERROR_10000);	
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		if(!PhoneFormatCheckUtil.isChinaPhoneLegal(telePhone + "")){
			throw new GameException(ExceptionConstant.BIND_3501);
		}	
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("telePhone", telePhone);			
		
		//TODO 
		String result = HttpUtil.httpsRequest(Config.ACCOUNT_URL + HttpConstant.SEND_SMS, "POST", jsonObject.toString());
//		String result = null;
		if(result == null) throw new GameException(ExceptionConstant.ERROR_2);
		
		JSONObject resultJson = new JSONObject(result);		
		int state = resultJson.getInt("result");
		if(state != 0){
			if(state == 1){
				throw new GameException(ExceptionConstant.CREATE_1100);
			}else if(state == 2){
				throw new GameException(ExceptionConstant.BIND_3505);
			}else if(state == 3){
				throw new GameException(ExceptionConstant.BIND_3504);
			}
			return;
		}
		
		String bizId = resultJson.getString("bizId");
		if(bizId == null) throw new GameException(ExceptionConstant.BIND_3504);
		
		S_GetValidateCode.Builder builder = S_GetValidateCode.newBuilder();
		builder.setTelePhone(telePhone);
		builder.setBizId(bizId);		
		MessageObj msg = new MessageObj(MessageID.S_GetValidateCode_VALUE, builder.build().toByteArray());	
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void bindPhone(long userId, long playerId, long telePhone, int code, String bizId) throws Exception {
		if(userId < 1 || playerId < 1 ||telePhone < 1 || code < 1 || bizId == null){
			throw new GameException(ExceptionConstant.ERROR_10000);
		}
		
		if(!PhoneFormatCheckUtil.isChinaPhoneLegal(telePhone + "")){
			throw new GameException(ExceptionConstant.BIND_3501);
		}
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId", userId);		
		jsonObject.put("telePhone", telePhone);	
		jsonObject.put("code", code);			
		jsonObject.put("bizId", bizId);				
		
		//TODO 
		String result = HttpUtil.httpsRequest(Config.ACCOUNT_URL + HttpConstant.BIND_PHONE, "POST", jsonObject.toString());
//		String result = null;
		if(result == null) throw new GameException(ExceptionConstant.ERROR_2);
		
		JSONObject resultJson = new JSONObject(result);
		int state = resultJson.getInt("result");
		if(state != 0){
			if(state == 1){
				throw new GameException(ExceptionConstant.CREATE_1100);
			}else if(state == 3){
				throw new GameException(ExceptionConstant.BIND_3500);
			}
			return;
		}
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		Player player = playerService.getPlayerByID(playerId);
		player.setTelePhone(telePhone);
		playerService.updatePlayer(player);
		
		S_BindPhone.Builder builder = S_BindPhone.newBuilder();
		builder.setTelePhone(player.getTelePhone());
		MessageObj msg = new MessageObj(MessageID.S_BindPhone_VALUE, builder.build().toByteArray());	
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);	
	}

	@Override
	public void getBindInfo(long playerId){
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		Player player = playerService.getPlayerByID(playerId);	
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		
		S_GetBindInfo.Builder builder = S_GetBindInfo.newBuilder();
		builder.setTelePhone(player.getTelePhone());
		builder.setRewardState(playerOptional.getBindRewardState());
		MessageObj msg = new MessageObj(MessageID.S_GetBindInfo_VALUE, builder.build().toByteArray());	
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void getBindReward(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);
		Player player = playerService.getPlayerByID(playerId);
		
		if(player.getTelePhone() < 1) throw new GameException(ExceptionConstant.BIND_3503);
		if(playerOptional.getBindRewardState() == 1) throw new GameException(ExceptionConstant.BIND_3502);
		
		BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_BIND_PHONE, 201);
		rewardService.fetchRewardList(playerId, baseReward.getRewardList());
		
		playerOptional.setBindRewardState(1);
		playerService.updatePlayerOptional(playerOptional);
		
		S_GetBindInfo.Builder builder = S_GetBindInfo.newBuilder();
		builder.setTelePhone(player.getTelePhone());
		builder.setRewardState(playerOptional.getBindRewardState());
		MessageObj msg = new MessageObj(MessageID.S_GetBindInfo_VALUE, builder.build().toByteArray());	
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}
}
