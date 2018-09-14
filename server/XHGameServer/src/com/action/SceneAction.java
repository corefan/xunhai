package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.PlayerConstant;
import com.core.GameMessage;
import com.domain.Position;
import com.domain.player.PlayerExt;
import com.message.SceneProto.C_CheckPuppets;
import com.message.SceneProto.C_EnterScene;
import com.message.SceneProto.C_SynPosition;
import com.message.SceneProto.C_Transfer;
import com.message.SceneProto.C_UpdatePosition;
import com.service.IPlayerService;
import com.service.ISceneService;
import com.util.PlayerUtil;

/**
 * 场景接口
 * @author ken
 * @date 2016-12-28
 */
public class SceneAction {
	
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 进入场景
	 */
	
	public void enterScene(GameMessage gameMessage) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		if(playerId == 0) return;
		
		C_EnterScene msg = C_EnterScene.parseFrom(gameMessage.getData());
		int mapId = msg.getMapId();
		int transferId = msg.getTransferId();
		boolean bLogin = true;
		if(transferId > 0){
			bLogin = false;	
		}
		
		sceneService.enterScene(playerId, mapId, transferId, bLogin, null, 0);
		
		if(bLogin){			
			// buff 检测
			serviceCollection.getBuffService().dealLogin(playerId);
		}
	}

	/**
	 * 同步位置状态
	 */
	
	public void synPosition(GameMessage gameMessage) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Long playerId = gameMessage.getConnection().getPlayerId();
		if(playerId == 0) return;
		
		C_SynPosition msg = C_SynPosition.parseFrom(gameMessage.getData());
		String guid = msg.getGuid();
		int state = msg.getState();
		int newX = msg.getPosition().getX();
		int newY = msg.getPosition().getY();
		int newZ = msg.getPosition().getZ();
		int direction = msg.getDirection();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		
		int fighterType = PlayerUtil.getType(guid);
		if(fighterType == PlayerConstant.PLAYER){
			sceneService.synPosition(playerId, state, newX, newY, newZ, direction, true);
		}else{
			sceneService.synPosition(playerExt.getSceneGuid(), guid, state, newX, newY, newZ, direction, true);
		}
			
	}

	/**
	 * 更新位置
	 */
	
	public void updatePosition(GameMessage gameMessage) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		if(playerId == 0) return;
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		C_UpdatePosition param = C_UpdatePosition.parseFrom(gameMessage.getData());
		String targetGuid = param.getGuid();
		Position positions = new Position(param.getPosition().getX(), param.getPosition().getY(), param.getPosition().getZ());
		int direction = param.getDirection();
		
		sceneService.updatePosition(playerExt.getSceneGuid(), targetGuid, positions, direction);
		
	}

	/**
	 * 获取周围所有元素
	 */
	
	public void getSceneElementList(GameMessage gameMessage) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		sceneService.getSceneElementList(playerId);
	}

	/**
	 * 检测残留单位
	 */
	
	public void checkPuppets(GameMessage gameMessage) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		Long playerId = gameMessage.getConnection().getPlayerId();
		if(playerId == 0) return;
		
		C_CheckPuppets param = C_CheckPuppets.parseFrom(gameMessage.getData());
		List<String> guids = param.getGuidsList();
		sceneService.checkPuppets(playerId, guids);
	}

	/**
	 * 同步怪物状态
	 */
	
	public void synMonsterState(GameMessage gameMessage){
		ISceneService sceneService = serviceCollection.getSceneService();
		Long playerId = gameMessage.getConnection().getPlayerId();
		sceneService.synMonsterState(playerId);
	}

	/**
	 * 传送
	 */
	
	public void transfer(GameMessage gameMessage) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		Long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Transfer param = C_Transfer.parseFrom(gameMessage.getData());
		int toMapId = param.getToMapId();
		Position toPosition = new Position(param.getToPosition().getX(), param.getToPosition().getY(), param.getToPosition().getZ());
		sceneService.transfer(playerId, toMapId, toPosition);
	}
}
