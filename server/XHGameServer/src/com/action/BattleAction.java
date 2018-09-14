package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.core.GameMessage;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;
import com.message.BattleProto.C_ChangePkModel;
import com.message.BattleProto.C_Revive;
import com.message.BattleProto.C_SkillResult;
import com.message.BattleProto.C_SynSkill;
import com.util.PlayerUtil;

/**
 * 战斗接口
 * @author ken
 * @date 2017-1-12
 */
public class BattleAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();

	/**
	 * 技能同步
	 */
	
	public void synSkill(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_SynSkill param = C_SynSkill.parseFrom(gameMessage.getData());
		String guid = param.getGuid();
		int skillId = param.getSkillId();
		int type = param.getType();
		int direction = param.getDirection();
		
		int x = param.getTargetPoint().getX();
		int y = param.getTargetPoint().getY();
		int z = param.getTargetPoint().getZ();
		
		String targetId = param.getTargetId();
		
		PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		BasePuppet basePuppet = serviceCollection.getSceneService().getBasePuppet(playerPuppet.getSceneGuid(), guid, 0);
		
		if(basePuppet == null || basePuppet.getState() != BattleConstant.STATE_NORMAL) return;
		
		serviceCollection.getBattleService().synSkill(basePuppet, skillId, type, direction, x, y, z, targetId);
		
	}

	/**
	 * 技能结果
	 */
	
	public void skillResult(GameMessage gameMessage) throws Exception {
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_SkillResult param = C_SkillResult.parseFrom(gameMessage.getData());
		String guid = param.getGuid();
		int skillId = param.getSkillId();
		List<String> targerIds = param.getTargetIdsList();
		int accountModelId = param.getAccountModelId();
		int wigId = param.getWigId(); //地效唯一编号
		
		PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		int fighterType = PlayerUtil.getType(guid);
		
		serviceCollection.getBattleService().useSkill(playerPuppet.getSceneGuid(), guid, skillId, targerIds, accountModelId, wigId, fighterType);
	}

	/**
	 * 拾取掉落
	 */
	
	public void pickup(GameMessage gameMessage) throws Exception {

		serviceCollection.getBattleService().pickup(gameMessage);
		
	}

	/**
	 * 复活
	 */
	
	public void revive(GameMessage gameMessage) throws Exception {
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Revive param = C_Revive.parseFrom(gameMessage.getData());
		int type = param.getType();
		serviceCollection.getBattleService().revive(playerId, type);
	}

	/**
	 * 切换pk模式
	 */
	
	public void changePkModel(GameMessage gameMessage) throws Exception {
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangePkModel param = C_ChangePkModel.parseFrom(gameMessage.getData());
		int pkModel = param.getPkModel();
		
		serviceCollection.getBattleService().changePkModel(playerId, pkModel);
	}

}
