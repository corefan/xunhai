package com.action;

import java.util.concurrent.BlockingQueue;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.enemy.PlayerEnemy;
import com.message.EnemyProto.C_DeleteEnemy;
import com.message.EnemyProto.C_TrackEnemy;
import com.message.EnemyProto.EnemyMsg;
import com.message.EnemyProto.S_DeleteEnemy;
import com.message.EnemyProto.S_SynEnemyList;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IEnemyService;
import com.service.IProtoBuilderService;

/**
 * 敌人接口
 * @author ken
 * @date 2018年7月11日
 */
public class EnemyAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/** 删除敌人*/
	
	public void deleteEnemy(GameMessage gameMessage) throws Exception {
		IEnemyService enemyService = serviceCollection.getEnemyService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_DeleteEnemy param = C_DeleteEnemy.parseFrom(gameMessage.getData());
		long enemyPlayerId = param.getEnemyPlayerId();
		
		enemyService.deleteEnemy(playerId, enemyPlayerId);
		S_DeleteEnemy.Builder builder = S_DeleteEnemy.newBuilder();
		builder.setEnemyPlayerId(enemyPlayerId);
		
		MessageObj msg = new MessageObj(MessageID.S_DeleteEnemy_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/** 获取敌人列表*/
	
	public void getEnemyList(GameMessage gameMessage) throws Exception {
		IEnemyService enemyService = serviceCollection.getEnemyService();		
		long playerId = gameMessage.getConnection().getPlayerId();		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();		
		BlockingQueue<PlayerEnemy> playerEnemyList = enemyService.getPlayerEnemyList(playerId);
		
		S_SynEnemyList.Builder builder = S_SynEnemyList.newBuilder();	
		for(PlayerEnemy playerEnemy : playerEnemyList){
			if(playerEnemy.getDeleteFlag() == 1) continue;
			EnemyMsg.Builder msg = protoBuilderService.buildEnemyMsg(playerEnemy);
			if(msg == null) continue;
			builder.addListEnemy(protoBuilderService.buildEnemyMsg(playerEnemy));
		}		 
		
		MessageObj msg = new MessageObj(MessageID.S_SynEnemyList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);		
	}

	/** 追踪敌人*/
	
	public void trackEnemy(GameMessage gameMessage) throws Exception {
		IEnemyService enemyService = serviceCollection.getEnemyService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_TrackEnemy param = C_TrackEnemy.parseFrom(gameMessage.getData());
		long enemyPlayerId = param.getEnemyPlayerId();		
		enemyService.trackEnemy(playerId, enemyPlayerId);		
	}
}
