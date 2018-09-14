package com.core.disruptor;

import com.core.GameCCCommandService;
import com.domain.MessageObj;

/**
 * 2013-10-29
 * 游戏数据处理类
 */
public class GameDataHandler {

	private GameCCCommandService commandService = new GameCCCommandService();
	
	/**
	 * 数据处理
	 * */
	public void onDisruptorData(MessageObj gameMessage) {
		gameMessage.getConnection().getExceptionStr().setLength(0);
		commandService.executeCommand(gameMessage, gameMessage.getConnection());
	}

}
