package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.service.ITowerService;

/**
 * 大荒塔
 * @author ken
 * @date 2017-3-24
 */
public class TowerAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 进入大荒塔
	 */
	
	public void enterTower(GameMessage gameMessage) throws Exception {

		ITowerService towerService = serviceCollection.getTowerService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		towerService.enterTower(playerId);
	}

	/**
	 * 退出大荒塔
	 */
	
	public void quitTower(GameMessage gameMessage) throws Exception {

		ITowerService towerService = serviceCollection.getTowerService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		towerService.quitTower(playerId);
	}

	/**
	 * 重置大荒塔
	 */
	
	public void resetTower(GameMessage gameMessage) throws Exception {

		ITowerService towerService = serviceCollection.getTowerService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		towerService.resetTower(playerId);
	}

	/**
	 * 神境面板数据
	 */
	
	public void getShenjingData(GameMessage gameMessage) throws Exception {
		ITowerService towerService = serviceCollection.getTowerService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		towerService.getShenjingData(playerId);
	}

}
