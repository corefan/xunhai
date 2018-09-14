package com.action;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.SceneConstant;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.puppet.PlayerPuppet;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TiantiProto.C_GetRankPageList;
import com.message.TiantiProto.C_GetStageReward;
import com.message.TiantiProto.C_UseTiantiItem;
import com.message.TiantiProto.S_GetStageReward;
import com.message.TiantiProto.S_Match;
import com.scene.SceneModel;
import com.service.ISceneService;
import com.service.ITiantiService;

/**
 * 天梯接口
 * @author ken
 * @date 2017-4-14
 */
public class TiantiAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/** 天梯面板数据 */
	
	public void getTiantiPanelData(GameMessage gameMessage) throws Exception {
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		tiantiService.getTiantiPanelData(playerId);
	}

	/**
	 * 获取排行列表 分页
	 */
	
	public void getRankPageList(GameMessage gameMessage) throws Exception {
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetRankPageList param = C_GetRankPageList.parseFrom(gameMessage.getData());
		int start = param.getStart();
		int offset = param.getOffset();
		tiantiService.getRankPageList(playerId, start, offset);
	}

	/**
	 * pk3分钟时间到
	 */
	
	public void giveUp(GameMessage gameMessage){
		ISceneService sceneService = serviceCollection.getSceneService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		tiantiService.end(sceneModel, 0, playerId);
	}

	/** 
	 * 竞技场物品使用
	 */
	
	public void useTiantiItem(GameMessage gameMessage) throws Exception {
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_UseTiantiItem param = C_UseTiantiItem.parseFrom(gameMessage.getData());
		int itemId = param.getItemId();
		int num = param.getNum();
		tiantiService.useTiantiItem(playerId, itemId, num);
	}

	/**
	 * 领取段位奖励
	 */
	
	public void getStageReward(GameMessage gameMessage) throws Exception {
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetStageReward param = C_GetStageReward.parseFrom(gameMessage.getData());
		int stage = param.getStage();
		tiantiService.getStageReward(playerId, stage);
		
		S_GetStageReward.Builder builder = S_GetStageReward.newBuilder();
		builder.setStage(stage);
		MessageObj msg = new MessageObj(MessageID.S_GetStageReward_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 匹配
	 */
	
	public void match(GameMessage gameMessage) throws Exception {
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		tiantiService.match(playerId);
		
		S_Match.Builder builder = S_Match.newBuilder();
		builder.setState(0);
		MessageObj msg = new MessageObj(MessageID.S_Match_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 取消匹配
	 */
	
	public void calcelMatch(GameMessage gameMessage) throws Exception {
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		tiantiService.cancelMatch(playerId);
	}

}
