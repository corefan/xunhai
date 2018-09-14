package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.service.IBuffService;

/**
 * buff
 * @author jiangqin
 * @date 2017-4-24
 */
 
public class BuffAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 调息buff添加
	 */
	
	public void AutoAddHPMP(GameMessage gameMessage) throws Exception {
		IBuffService buffService = serviceCollection.getBuffService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		buffService.autoAddHpMp(playerId);		
	}
	
	/**
	 * 中断调息buff
	 */
	
	public void BreakAddHPMP(GameMessage gameMessage) throws Exception {
		IBuffService buffService = serviceCollection.getBuffService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		buffService.breakAddHpMp(playerId);		
	}	
}
