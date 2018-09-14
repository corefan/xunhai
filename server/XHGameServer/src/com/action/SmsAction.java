package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.PlayerProto.C_BindPhone;
import com.message.PlayerProto.C_GetValidateCode;

/**
 * 手机验证码接口
 */
public class SmsAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();

	/** 获取手机验证码 */
	
	public void getValidateCode(GameMessage gameMessage) throws Exception {
		C_GetValidateCode param = C_GetValidateCode.parseFrom(gameMessage.getData());
		long telePhone = param.getTelePhone();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		serviceCollection.getSmsService().getValidateCode(playerId, telePhone);
	}

	/** 绑定手机号 */
	
	public void bindPhone(GameMessage gameMessage) throws Exception {
		long userId = gameMessage.getConnection().getUserId();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_BindPhone param = C_BindPhone.parseFrom(gameMessage.getData());
		long telePhone =  param.getTelePhone();
		int code = param.getCode();
		String bizId = param.getBizId();
		
		serviceCollection.getSmsService().bindPhone(userId, playerId, telePhone, code, bizId);
		
	}

	/** 获取账号绑定信息 */
	
	public void getBindInfo(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		
		serviceCollection.getSmsService().getBindInfo(playerId);
	}

	/** 获取账号绑定奖励 */
	
	public void getBindReward(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		
		serviceCollection.getSmsService().getBindReward(playerId);
	}
}
