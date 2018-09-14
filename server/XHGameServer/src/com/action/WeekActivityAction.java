package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.WeekactivityProto.C_EnterActivity;
import com.service.IWeekActivityService;

/**
 * 活动接口
 * @author ken
 * @date 2017-5-15
 */
public class WeekActivityAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 取活动类型列表
	 */
	
	public void getActivityList(GameMessage gameMessage) throws Exception {
		IWeekActivityService weekActivityService = serviceCollection.getWeekActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		weekActivityService.getActivityList(playerId);
	}

	/**
	 * 进入活动
	 */
	
	public void enterActivity(GameMessage gameMessage) throws Exception {
		IWeekActivityService weekActivityService = serviceCollection.getWeekActivityService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_EnterActivity param = C_EnterActivity.parseFrom(gameMessage.getData());
		int activityId = param.getId();
		weekActivityService.enterActivity(playerId, activityId);
	}

}
