package com.service;

import com.core.Connection;
import com.core.GameMessage;

/**
 * 登录系统
 * @author ken
 * @date 2016-12-22
 */
public interface ILoginService {

	
	/** 登录游戏*/
	void login(GameMessage gameMessage) throws Exception;
	
	/** 创建角色*/
	void createPlayer(long userId, int serverNo, int career, String playerName, long telePhone) throws Exception;
	
	/** 进入游戏*/
	void enterGame(long playerId, long telePhone) throws Exception;
	
	/** 进入游戏完成后 获取数据*/
	void enterComplete(Connection connection) throws Exception;
	
	/** 下线或断线   此时保留场景数据20s*/
	void logout(Connection connection) throws Exception;
	
	/** 真正退出了游戏。  删除场景数据*/
	void exitGame(long playerId);
	
	/** 断线重连*/
	void loginAgain(GameMessage gameMessage) throws Exception;
}
