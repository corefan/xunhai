package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.InstanceProto.C_AgreeEnter;
import com.message.InstanceProto.C_EnterInstance;
import com.service.IInstanceService;

/**
 * 副本
 * @author ken
 * @date 2017-3-9
 */
public class InstanceAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 取开启的副本列表
	 */
	
	public void getOpenMapList(GameMessage gameMessage) throws Exception {
		IInstanceService instanceService = serviceCollection.getInstanceService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		instanceService.getOpenMapList(playerId);
	}

	/**
	 * 进入副本
	 */
	
	public void enterInstance(GameMessage gameMessage) throws Exception {
		IInstanceService instanceService = serviceCollection.getInstanceService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_EnterInstance param = C_EnterInstance.parseFrom(gameMessage.getData());
		int mapId = param.getMapId();
		
		instanceService.enterInstance(playerId, mapId);
	}

	/**
	 * 同意拒绝进入
	 * @state 1:同意  2：拒绝
	 */
	
	public void agreeEnter(GameMessage gameMessage) throws Exception {

		IInstanceService instanceService = serviceCollection.getInstanceService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AgreeEnter param = C_AgreeEnter.parseFrom(gameMessage.getData());
		int state =  param.getState();
		instanceService.agreeEnter(playerId, state);
	}

	/**
	 * 退出副本
	 */
	
	public void quitInstance(GameMessage gameMessage) throws Exception {
		IInstanceService instanceService = serviceCollection.getInstanceService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		instanceService.quitInstance(playerId);
	}

}
