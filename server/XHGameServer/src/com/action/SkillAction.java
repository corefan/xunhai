package com.action;

import java.util.List;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.skill.PlayerSkill;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SkillProto.C_AddSkillMastery;
import com.message.SkillProto.C_CreatePlayerSkill;
import com.message.SkillProto.C_UpgradePlayerSkill;
import com.message.SkillProto.S_CreatePlayerSkill;
import com.message.SkillProto.S_UpgradePlayerSkill;
import com.service.IProtoBuilderService;
import com.service.ISkillService;

/**
 * 技能升级
 * @author ken
 * @date 2017-2-7
 */
public class SkillAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 学习技能
	 */
	
	public void studyPlayerSkill(GameMessage gameMessage) throws Exception {
		
		ISkillService skillService = serviceCollection.getSkillService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_CreatePlayerSkill param = C_CreatePlayerSkill.parseFrom(gameMessage.getData());
		int skillId = param.getSkillId();
		
		PlayerSkill playerSkill = skillService.studyPlayerSkill(playerId, skillId, false);
		S_CreatePlayerSkill.Builder builder = S_CreatePlayerSkill.newBuilder();
		builder.setPlayerSkill(protoBuilderService.buildPlayerSkillMsg(playerSkill));
		MessageObj msg = new MessageObj(MessageID.S_CreatePlayerSkill_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 升级技能
	 */
	
	public void upgradePlayerSkill(GameMessage gameMessage) throws Exception {
		ISkillService skillService = serviceCollection.getSkillService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();

		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_UpgradePlayerSkill param = C_UpgradePlayerSkill.parseFrom(gameMessage.getData());
		int skillId = param.getSkillId();
		
		PlayerSkill playerSkill = skillService.upgradePlayerSkill(playerId, skillId);
		S_UpgradePlayerSkill.Builder builder = S_UpgradePlayerSkill.newBuilder();
		builder.setSkillId(skillId);
		builder.setNewPlayerSkill(protoBuilderService.buildPlayerSkillMsg(playerSkill));
		MessageObj msg = new MessageObj(MessageID.S_UpgradePlayerSkill_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	 /**
     * 使用物品添加技能熟练度
     */	
	
	public void addSkillMastery(GameMessage gameMessage) throws Exception {
		ISkillService skillService = serviceCollection.getSkillService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AddSkillMastery param = C_AddSkillMastery.parseFrom(gameMessage.getData());
		int skillId = param.getSkillId();
		int itemId = param.getItemId();		
		skillService.addSkillMastery(playerId, skillId, itemId);		
	}
	
	/**
	 * 获取玩家技能信息
	 */
	public void getPlayerSkills(GameMessage gameMessage) throws Exception {
		ISkillService skillService = serviceCollection.getSkillService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		List<PlayerSkill> lists = skillService.listPlayerSkills(playerId);
		
		skillService.synChangeListPlayerSkill(playerId, lists);
	}
}
