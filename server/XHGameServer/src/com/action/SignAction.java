package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.SignProto.C_GetConSignReward;
import com.service.ISignService;


/**
 * 签到
 * @author jiangqin
 * @date 2017-4-22
 */
 
public class SignAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	
	/** 签到 */
	
	public void sign(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		ISignService signService = serviceCollection.getSignService();
		
		signService.sign(playerId);	
	}

	/** 签到奖励领取*/
	
	public void getConSignReward(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		ISignService signService = serviceCollection.getSignService();
		C_GetConSignReward param = C_GetConSignReward.parseFrom(gameMessage.getData());
		int signNum = param.getSignNum();
		
		signService.getConSignReward(playerId, signNum);
	}

}
