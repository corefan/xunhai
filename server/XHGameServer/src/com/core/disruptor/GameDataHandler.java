package com.core.disruptor;

import com.core.GameCommandService;
import com.core.GameMessage;

/**
 * 2013-10-29
 * 游戏数据处理类
 */
public class GameDataHandler {

	private GameCommandService commandService = new GameCommandService();

	/**
	 * 数据处理
	 * */
	public void onDisruptorData(GameMessage gameMessage) {
		commandService.executeDisruptorCommand(gameMessage);
	}

}
