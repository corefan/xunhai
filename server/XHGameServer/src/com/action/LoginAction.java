package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.Connection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.message.LoginProto.C_CreatePlayer;
import com.message.LoginProto.C_DeletePlayer;
import com.message.LoginProto.C_EnterGame;
import com.message.LoginProto.S_DeletePlayer;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.ILoginService;

/**
 * 登录游戏
 * @author ken
 * @date 2016-12-22
 */
public class LoginAction {
	
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/** 登录游戏*/
	
	public void login(GameMessage gameMessage) throws Exception {
		ILoginService loginService = serviceCollection.getLoginService();
	
		loginService.login(gameMessage);
	}

	/** 创建角色*/
	
	public void createPlayer(GameMessage gameMessage) throws Exception {
		ILoginService loginService = serviceCollection.getLoginService();
		
		long userId = gameMessage.getConnection().getUserId();
		int serverNo= gameMessage.getConnection().getServerNo();
		
		if(userId == 0 || serverNo == 0) return;
		
		C_CreatePlayer msg = C_CreatePlayer.parseFrom(gameMessage.getData());
		
		loginService.createPlayer(userId, serverNo, msg.getCareer(), msg.getPlayerName(), msg.getTelePhone());
	}

	/** 进入游戏*/
	
	public void enterGame(GameMessage gameMessage) throws Exception {

		ILoginService loginService = serviceCollection.getLoginService();
		
		C_EnterGame msg = C_EnterGame.parseFrom(gameMessage.getData());
		loginService.enterGame(msg.getPlayerId(), msg.getTelePhone());
	}

	/** 进入游戏完成后 获取数据*/
	
	public void enterComplete(GameMessage gameMessage) throws Exception {
		ILoginService loginService = serviceCollection.getLoginService();
		
		loginService.enterComplete(gameMessage.getConnection());
	}

	/** 下线或断线   此时保留场景数据20s*/
	
	public void logout(Connection connection) throws Exception {
		ILoginService loginService = serviceCollection.getLoginService();
		loginService.logout(connection);
	}

	/**
	 * 删除角色
	 */
	
	public void deletePlayer(GameMessage gameMessage) throws Exception {
		C_DeletePlayer param = C_DeletePlayer.parseFrom(gameMessage.getData());
		long playerId = param.getPlayerId();
		serviceCollection.getPlayerService().deletePlayer(playerId);
		
		S_DeletePlayer.Builder builder = S_DeletePlayer.newBuilder();
		builder.setPlayerId(playerId);
		serviceCollection.getGameSocketService().sendData(
				gameMessage.getConnection(),
				new MessageObj(MessageID.S_DeletePlayer_VALUE, builder.build()
						.toByteArray()));
	}

	/** 断线重连*/
	
	public void loginAgain(GameMessage gameMessage) throws Exception {

		ILoginService loginService = serviceCollection.getLoginService();
		
		loginService.loginAgain(gameMessage);
	}

}
