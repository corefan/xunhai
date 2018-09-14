package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ConfigConstant;
import com.constant.PlayerConstant;
import com.constant.ProdefineConstant;
import com.constant.RewardTypeConstant;
import com.domain.Position;
import com.domain.Reward;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;
import com.domain.bag.PlayerEquipment;
import com.domain.battle.DropItemInfo;
import com.domain.battle.SkillEffect;
import com.domain.battle.WigSkillInfo;
import com.domain.buff.Buff;
import com.domain.collect.Collect;
import com.domain.enemy.PlayerEnemy;
import com.domain.epigraph.PlayerWeaponEffect;
import com.domain.family.Family;
import com.domain.family.PlayerFamily;
import com.domain.fashion.PlayerFashion;
import com.domain.friend.PlayerApply;
import com.domain.furnace.PlayerFurnace;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.mail.MailInbox;
import com.domain.market.PlayerMarket;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.rank.BattleValueRank;
import com.domain.rank.EquipRank;
import com.domain.rank.GoldRank;
import com.domain.sign.PlayerSign;
import com.domain.skill.PlayerSkill;
import com.domain.task.PlayerTask;
import com.domain.trading.PlayerTradeBag;
import com.domain.wakan.PlayerWakan;
import com.domain.wing.PlayerWing;
import com.message.ActivityProto.RewardDataMsg;
import com.message.ActivityProto.RewardMsg;
import com.message.ActivityProto.TurnRecMsg;
import com.message.BagProto.DrugLumnMsg;
import com.message.BagProto.PlayerBagMsg;
import com.message.BattleProto.SkillEffectMsg;
import com.message.BuffProto.BuffMsg;
import com.message.EnemyProto.EnemyMsg;
import com.message.EquipmentProto.PlayerEquipmentMsg;
import com.message.EquipmentProto.PlayerWeaponEffectMsg;
import com.message.FamilyProto.FamilyPlayerMsg;
import com.message.FashionProto.PlayerFashionMsg;
import com.message.FriendProto.ApplyMsg;
import com.message.FriendProto.FriendMsg;
import com.message.FurnaceProto.PlayerFurnaceMsg;
import com.message.GuildProto.ApplyPlayerMsg;
import com.message.GuildProto.GuildMsg;
import com.message.GuildProto.GuildPlayerMsg;
import com.message.GuildProto.GuildWarMsg;
import com.message.MailProto.MailInboxMsg;
import com.message.MarketProto.MarketItemMsg;
import com.message.PlayerProto.PlayerCommonMsg;
import com.message.PlayerProto.PlayerMsg;
import com.message.PlayerProto.SynPlayerPropertyMsg;
import com.message.RankProto.BattleValueRankMsg;
import com.message.RankProto.EquipRankMsg;
import com.message.RankProto.GoldRankMsg;
import com.message.SceneProto.BeckonPuppetMsg;
import com.message.SceneProto.CollectItemInfoMsg;
import com.message.SceneProto.DropItemInfoMsg;
import com.message.SceneProto.MonsterPuppetMsg;
import com.message.SceneProto.MonsterStateMsg;
import com.message.SceneProto.PlayerPuppetMsg;
import com.message.SceneProto.Vector3Msg;
import com.message.SceneProto.WigSkillInfoMsg;
import com.message.SignProto.SignMsg;
import com.message.SkillProto.PlayerSkillMsg;
import com.message.TaskProto.PlayerTaskMsg;
import com.message.TiantiProto.EndPKPlayerMsg;
import com.message.TiantiProto.LoadPkPlayerMsg;
import com.message.TiantiProto.PKRewardMsg;
import com.message.TradingProto.PlayerTradeBagMsg;
import com.message.TradingProto.PlayerTradeEquipmentMsg;
import com.message.WakanProto.WakanMsg;
import com.message.WakanProto.WakanMsg.Builder;
import com.message.WingProto.WingMsg;
import com.service.IFamilyService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.util.PlayerUtil;

/**
 * proto管理器
 * @author ken
 * @date 2016-12-30
 */
public class ProtoBuilderService implements IProtoBuilderService {

	@Override
	public PlayerMsg.Builder buildPlayerMsg(Player player) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerProperty playerProperty =  playerService.getPlayerPropertyById(player.getPlayerId());
		if(playerProperty == null) return null;
		
		PlayerExt playerExt = playerService.getPlayerExtById(player.getPlayerId());
		
		PlayerMsg.Builder msg = PlayerMsg.newBuilder();
		msg.setCareer(player.getCareer());
		msg.setLevel(playerProperty.getLevel());
		msg.setPlayerId(player.getPlayerId());
		msg.setPlayerName(player.getPlayerName());
		msg.setWeaponStyle(playerExt.getWeaponStyle());
		msg.setDressStyle(playerExt.getDressStyle());
		msg.setWingStyle(playerExt.getWingStyle());
		msg.setCreateTime(player.getCreateTime().getTime());
		if(playerExt.getLoginTime() != null){
			msg.setLoginTime(playerExt.getLoginTime().getTime());
		}
		
		return msg;
	}

	@Override
	public PlayerCommonMsg.Builder buildPlayerCommonMsg(Player player,
			PlayerProperty playerProperty, PlayerExt playerExt, PlayerWealth playerWealth, PlayerDaily playerDaily) {
		PlayerCommonMsg.Builder commonMsg = PlayerCommonMsg.newBuilder();
		commonMsg.setSeverNo(player.getServerNo());
		commonMsg.setGuid(PlayerUtil.getGuid(PlayerConstant.PLAYER, player.getPlayerId()));
		commonMsg.setGold(playerWealth.getGold());
		commonMsg.setDiamond(playerWealth.getDiamond());
		commonMsg.setStone(playerWealth.getStone());
		
		List<SynPlayerPropertyMsg.Builder> proMsgs = buildSynPlayerPropertyMsgList(playerProperty, playerExt);
		for(SynPlayerPropertyMsg.Builder msg : proMsgs){
			commonMsg.addPlayerPropertyMsg(msg);
		}
		
		SynPlayerPropertyMsg.Builder msg61 = SynPlayerPropertyMsg.newBuilder();
		msg61.setPropertyId(ProdefineConstant.WEEK_TASK_NUM);
		msg61.setPropertyValue(playerExt.getWeekTaskNum());
		commonMsg.addPlayerPropertyMsg(msg61);	
		
		SynPlayerPropertyMsg.Builder msg64 = SynPlayerPropertyMsg.newBuilder();
		msg64.setPropertyId(ProdefineConstant.DAILY_TASK_NUM);
		msg64.setPropertyValue(playerDaily.getDailyTaskNum());
		commonMsg.addPlayerPropertyMsg(msg64);	
		
		return commonMsg;
	}	

	@Override
	public PlayerPuppetMsg.Builder buildPlayerPuppetMsg(PlayerPuppet model) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(model.getEid());
		if(playerExt == null) return null;
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(model.getEid());
		if(playerProperty == null) return null;
		
		PlayerPuppetMsg.Builder msg = PlayerPuppetMsg.newBuilder();
		msg.setGuid(model.getGuid());
		msg.setEid(model.getEid());
		msg.setName(model.getName());
		msg.setType(model.getType());
		msg.setLevel(model.getLevel());
		msg.setDressStyle(model.getDressStyle());
		msg.setWingStyle(model.getWingStyle());
		msg.setWeaponStyle(model.getWeaponStyle());
		msg.setCareer(model.getCareer());
		msg.setPosition(this.buildVector3Msg(model.getX(), model.getY(), model.getZ()));
		msg.setDirection(model.getDirection());
		msg.setMoveSpeed(model.getMoveSpeed());
		msg.setHp(model.getHp());
		msg.setMp(model.getMp());
		msg.setHpMax(model.getHpMax());
		msg.setMpMax(model.getMpMax());
		msg.setState(model.getPuppetState().ordinal());
		msg.setPkModel(model.getPkMode());
		msg.setPkValue(model.getPkVlaue());
		msg.setNameColor(model.getNameColor());
		
		msg.setTeamId(playerExt.getTeamId());
		msg.setStage(model.getStage());
		msg.setBattleValue(playerProperty.getBattleValue());
				
		for(Map.Entry<Integer, Buff> entry : model.getBuffMap().entrySet()){
			msg.addBuffList(this.buildBuffMsg(entry.getValue()));
		}
		
		int CREATE_FAMILY_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
		if(playerProperty.getLevel() >= CREATE_FAMILY_LEVEL){
			PlayerFamily playerFamily = serviceCollection.getFamilyService().getPlayerFamily(model.getEid());
			if(playerFamily != null && playerFamily.getPlayerFamilyId() > 0){
				Family family = serviceCollection.getFamilyService().getFamily(playerFamily.getPlayerFamilyId());
				if(family != null){
					msg.setFamilyName(family.getFamilyName());
					msg.setFamilySortId(playerFamily.getFamilySortId());	
				}
			}
		}
		
		int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
		if(playerProperty.getLevel() >= GUILD_NEED_LEVEL){
			PlayerGuild playerGuild = serviceCollection.getGuildService().getPlayerGuild(model.getEid());
			if(playerGuild != null && playerGuild.getGuildId() > 0){
				Guild guild = serviceCollection.getGuildService().getGuildById(playerGuild.getGuildId());
				if(guild != null){
					msg.setGuildId(guild.getGuildId());
					msg.setGuildName(guild.getGuildName());
				}
			
			}
		}
		
		return msg;
	}
	
	@Override
	public MonsterPuppetMsg.Builder buildMonsterPuppetMsg(MonsterPuppet model) {

		MonsterPuppetMsg.Builder msg = MonsterPuppetMsg.newBuilder();
		msg.setGuid(model.getGuid());
		msg.setEid((int)model.getEid());
		msg.setName(model.getName());
		msg.setType(model.getType());
		msg.setLevel(model.getLevel());
		msg.setDressStyle(model.getDressStyle());
		msg.setPosition(this.buildVector3Msg(model.getX(), model.getY(), model.getZ()));
		msg.setDirection(model.getDirection());
		msg.setMoveSpeed(model.getMoveSpeed());
		msg.setHp(model.getHp());
		msg.setMp(model.getMp());
		msg.setHpMax(model.getHpMax());
		msg.setMpMax(model.getMpMax());
		msg.setState(model.getPuppetState().ordinal());
		
		for(Map.Entry<Integer, Buff> entry : model.getBuffMap().entrySet()){
			msg.addBuffList(this.buildBuffMsg(entry.getValue()));
		}
		return msg;
	}

	@Override
	public BeckonPuppetMsg.Builder buildBeckonPuppetMsg(BeckonPuppet model) {
		BeckonPuppetMsg.Builder msg = BeckonPuppetMsg.newBuilder();
		msg.setOwnerGuid(model.getOwnerGuid());
		msg.setGuid(model.getGuid());
		msg.setEid((int)model.getEid());
		msg.setName(model.getName());
		msg.setType(model.getType());
		msg.setLevel(model.getLevel());
		msg.setDressStyle(model.getDressStyle());
		msg.setPosition(this.buildVector3Msg(model.getX(), model.getY(), model.getZ()));
		msg.setDirection(model.getDirection());
		msg.setMoveSpeed(model.getMoveSpeed());
		msg.setHp(model.getHp());
		msg.setMp(model.getMp());
		msg.setHpMax(model.getHpMax());
		msg.setMpMax(model.getMpMax());
		msg.setState(model.getPuppetState().ordinal());
		msg.setPkModel(model.getPkMode());
		
		for(Map.Entry<Integer, Buff> entry : model.getBuffMap().entrySet()){
			msg.addBuffList(this.buildBuffMsg(entry.getValue()));
		}
		return msg;
	}	
	
	/**
	 * 位置信息
	 */
	@Override
	public Vector3Msg.Builder buildVector3Msg(Position model){
		Vector3Msg.Builder msg = Vector3Msg.newBuilder();
		if(model == null) return msg;
		
		msg.setX(model.getX());
		msg.setY(model.getY());
		msg.setZ(model.getZ());
		return msg;
	}
	
	/**
	 * 位置信息
	 */
	@Override
	public Vector3Msg.Builder buildVector3Msg(int x, int y, int z){
		Vector3Msg.Builder msg = Vector3Msg.newBuilder();
		
		msg.setX(x);
		msg.setY(y);
		msg.setZ(z);
		return msg;
	}

	@Override
	public List<SynPlayerPropertyMsg.Builder> buildSynPlayerPropertyMsgList(PlayerProperty playerProperty, PlayerExt playerExt) {
		List<SynPlayerPropertyMsg.Builder> lists = new ArrayList<SynPlayerPropertyMsg.Builder>();
		
		SynPlayerPropertyMsg.Builder msg30 = SynPlayerPropertyMsg.newBuilder();
		msg30.setPropertyId(ProdefineConstant.STRENGTH);
		msg30.setPropertyValue(playerProperty.getStrength());
		lists.add(msg30);
		
		SynPlayerPropertyMsg.Builder msg31 = SynPlayerPropertyMsg.newBuilder();
		msg31.setPropertyId(ProdefineConstant.INTELLIGENCE);
		msg31.setPropertyValue(playerProperty.getIntelligence());
		lists.add(msg31);
		
		SynPlayerPropertyMsg.Builder msg32 = SynPlayerPropertyMsg.newBuilder();
		msg32.setPropertyId(ProdefineConstant.ENDURANCE);
		msg32.setPropertyValue(playerProperty.getEndurance());
		lists.add(msg32);
		
		SynPlayerPropertyMsg.Builder msg33 = SynPlayerPropertyMsg.newBuilder();
		msg33.setPropertyId(ProdefineConstant.SPIRIT);
		msg33.setPropertyValue(playerProperty.getSpirit());
		lists.add(msg33);
		
		SynPlayerPropertyMsg.Builder msg34 = SynPlayerPropertyMsg.newBuilder();
		msg34.setPropertyId(ProdefineConstant.LUCKY);
		msg34.setPropertyValue(playerProperty.getLucky());
		lists.add(msg34);
		
		SynPlayerPropertyMsg.Builder msg = SynPlayerPropertyMsg.newBuilder();
		msg.setPropertyId(ProdefineConstant.HP_MAX_PANEL);
		msg.setPropertyValue(playerProperty.getHpMax());
		lists.add(msg);
		
		SynPlayerPropertyMsg.Builder msg1 = SynPlayerPropertyMsg.newBuilder();
		msg1.setPropertyId(ProdefineConstant.MP_MAX_PANEL);
		msg1.setPropertyValue(playerProperty.getMpMax());
		lists.add(msg1);
		
		SynPlayerPropertyMsg.Builder msg2 = SynPlayerPropertyMsg.newBuilder();
		msg2.setPropertyId(ProdefineConstant.P_ATTACK_PANEL);
		msg2.setPropertyValue(playerProperty.getP_attack());
		lists.add(msg2);
		
		SynPlayerPropertyMsg.Builder msg3 = SynPlayerPropertyMsg.newBuilder();
		msg3.setPropertyId(ProdefineConstant.M_ATTACK_PANEL);
		msg3.setPropertyValue(playerProperty.getM_attack());
		lists.add(msg3);
		
		SynPlayerPropertyMsg.Builder msg4 = SynPlayerPropertyMsg.newBuilder();
		msg4.setPropertyId(ProdefineConstant.P_DAMAGE_PANEL);
		msg4.setPropertyValue(playerProperty.getP_damage());
		lists.add(msg4);
		
		SynPlayerPropertyMsg.Builder msg5 = SynPlayerPropertyMsg.newBuilder();
		msg5.setPropertyId(ProdefineConstant.M_DAMAGE_PANEL);
		msg5.setPropertyValue(playerProperty.getM_damage());
		lists.add(msg5);
		
		SynPlayerPropertyMsg.Builder msg6 = SynPlayerPropertyMsg.newBuilder();
		msg6.setPropertyId(ProdefineConstant.CRIT_PANEL);
		msg6.setPropertyValue(playerProperty.getCrit());
		lists.add(msg6);
		
		SynPlayerPropertyMsg.Builder msg20 = SynPlayerPropertyMsg.newBuilder();
		msg20.setPropertyId(ProdefineConstant.TOUGH_PANEL);
		msg20.setPropertyValue(playerProperty.getTough());
		lists.add(msg20);
		
		SynPlayerPropertyMsg.Builder msg21 = SynPlayerPropertyMsg.newBuilder();
		msg21.setPropertyId(ProdefineConstant.DMG_DEEP_PER_PANEL);
		msg21.setPropertyValue(playerProperty.getDmgDeepPer());
		lists.add(msg21);
		
		SynPlayerPropertyMsg.Builder msg22 = SynPlayerPropertyMsg.newBuilder();
		msg22.setPropertyId(ProdefineConstant.DMG_REDUCT_PER_PANEL);
		msg22.setPropertyValue(playerProperty.getDmgReductPer());
		lists.add(msg22);
		
		SynPlayerPropertyMsg.Builder msg23 = SynPlayerPropertyMsg.newBuilder();
		msg23.setPropertyId(ProdefineConstant.DMG_CRIT_PER_PANEL);
		msg23.setPropertyValue(playerProperty.getDmgCritPer());
		lists.add(msg23);
		
		
		SynPlayerPropertyMsg.Builder msg7 = SynPlayerPropertyMsg.newBuilder();
		msg7.setPropertyId(ProdefineConstant.MOVE_SPEED_PANEL);
		msg7.setPropertyValue(playerProperty.getMoveSpeed());
		lists.add(msg7);
		
		SynPlayerPropertyMsg.Builder msg8 = SynPlayerPropertyMsg.newBuilder();
		msg8.setPropertyId(ProdefineConstant.BATTLE_VALUE);
		msg8.setPropertyValue(playerProperty.getBattleValue());
		lists.add(msg8);
		
		SynPlayerPropertyMsg.Builder msg9 = SynPlayerPropertyMsg.newBuilder();
		msg9.setPropertyId(ProdefineConstant.LEVEL);
		msg9.setPropertyValue(playerProperty.getLevel());
		lists.add(msg9);
		
		SynPlayerPropertyMsg.Builder msg10 = SynPlayerPropertyMsg.newBuilder();
		msg10.setPropertyId(ProdefineConstant.EXP);
		msg10.setPropertyValue(playerProperty.getExp());
		lists.add(msg10);
		
		SynPlayerPropertyMsg.Builder msg58 = SynPlayerPropertyMsg.newBuilder();
		msg58.setPropertyId(ProdefineConstant.NAME_COLOR);
		msg58.setPropertyValue(playerExt.getNameColor());
		lists.add(msg58);
		
		return lists;
	}

	@Override
	public PlayerBagMsg.Builder buildPlayerBagMsg(PlayerBag playerBag) {
		PlayerBagMsg.Builder msg = PlayerBagMsg.newBuilder();
		msg.setPlayerBagId(playerBag.getPlayerBagId());
		msg.setGoodsType(playerBag.getGoodsType());
		msg.setItemId(playerBag.getItemId());
		msg.setItemIndex(playerBag.getItemIndex());
		msg.setNum(playerBag.getNum());
		msg.setIsBinding(playerBag.getIsBinding());
		msg.setState(playerBag.getState());
		return msg;
	}

	@Override
	public PlayerEquipmentMsg.Builder buildPlayerEquipmentMsg(PlayerEquipment playerEquipment){	
		PlayerEquipmentMsg.Builder msg = PlayerEquipmentMsg.newBuilder();
		msg.setPlayerEquipmentId(playerEquipment.getPlayerEquipmentId());
		msg.setEquipmentId(playerEquipment.getEquipmentId());
		msg.setEquipType(playerEquipment.getEquipType());
		msg.setState(playerEquipment.getState());	
		msg.setHoleNum(playerEquipment.getHoleNum());			
		msg.setScore(playerEquipment.getScore());
		msg.setIsBinding(playerEquipment.getIsBinding());
		for(Map.Entry<Integer, Integer> entry : playerEquipment.getAddAttrMap().entrySet()){
			SynPlayerPropertyMsg.Builder msginfo = SynPlayerPropertyMsg.newBuilder();
			msginfo.setPropertyId(entry.getKey());
			msginfo.setPropertyValue(entry.getValue());
			msg.addAddPropertyMsg(msginfo);
		}
		
		msg.setStrongLv(playerEquipment.getStrongLv());
		return msg;
	}

	@Override
	public SkillEffectMsg.Builder buildSkillEffectMsg(SkillEffect model) {
		SkillEffectMsg.Builder msg = SkillEffectMsg.newBuilder();
		msg.setTargetId(model.getTargetId());
		msg.setDmg(model.getDmg());
		msg.setHp(model.getHp());
		msg.setFightResult(model.getFightResult());
		return msg;
	}

	@Override
	public DropItemInfoMsg.Builder buildDropItemInfoMsg(DropItemInfo model) {
		DropItemInfoMsg.Builder msg = DropItemInfoMsg.newBuilder();
		
		if(model.getGoodsType() != RewardTypeConstant.BOX){
			msg.setTargetGuid(model.getTargetGuid());
			msg.setItemId(model.getItemId());
			msg.setNum(model.getNum());
		}
		
		msg.setDropId(model.getDropId());	
		msg.setGoodsType(model.getGoodsType());
		msg.setDropPosition(this.buildVector3Msg(model.getX(), model.getY(), model.getZ()));
		return msg;
	}

	@Override
	public WigSkillInfoMsg.Builder buildWigSkillInfoMsg(WigSkillInfo model) {
		WigSkillInfoMsg.Builder msg = WigSkillInfoMsg.newBuilder();
		msg.setGuid(model.getGuid());
		msg.setSkillId(model.getSkillId());
		msg.setTargetPoint(this.buildVector3Msg(model.getX(), model.getY(), model.getZ()));
		msg.setLeftTime((int)(model.getEndTime() - System.currentTimeMillis()));
		msg.setWigId(model.getWigId());
		return msg;
	}

	@Override
	public DrugLumnMsg.Builder buildDrugLumnMsg(PlayerDrug model) {
		DrugLumnMsg.Builder msg =  DrugLumnMsg.newBuilder();
		msg.setItemIndex(model.getItemIndex());
		msg.setItemId(model.getItemId());
		return msg;
	}

	@Override
	public PlayerSkillMsg.Builder buildPlayerSkillMsg(PlayerSkill playerSkill) {
		PlayerSkillMsg.Builder msg = PlayerSkillMsg.newBuilder();		
		msg.setSkillId(playerSkill.getSkillId());
		msg.setMastery(playerSkill.getMastery());
		msg.setMwSkillId(playerSkill.getMwSkillId());
		return msg;
	}

	@Override
	public PlayerFashionMsg.Builder buildPlayerFashionMsg(PlayerFashion playerFashion) {
		
		PlayerFashionMsg.Builder msg = PlayerFashionMsg.newBuilder();		
		msg.setFashionId(playerFashion.getFashionId());
		msg.setDressFlag(playerFashion.getDressFlag());
		return msg;
	}

	@Override
	public MailInboxMsg.Builder buildMailInboxMsg(MailInbox mailInbox) {
		MailInboxMsg.Builder msg = MailInboxMsg.newBuilder();
		
		msg.setMailInboxID(mailInbox.getMailInboxID());
		msg.setSenderName(mailInbox.getSenderName());
		msg.setReceiverID(mailInbox.getReceiverID());
		msg.setTheme(mailInbox.getTheme());
		msg.setContent(mailInbox.getContent());
		msg.setHaveAttachment(mailInbox.getHaveAttachment());
		msg.setHaveReceiveAttachment(mailInbox.getHaveReceiveAttachment());
		if (mailInbox.getAttachment() != null)
			msg.setAttachment(mailInbox.getAttachment());
		msg.setState(mailInbox.getState());
		msg.setReceiveTime(mailInbox.getReceiveTime().getTime());
		msg.setRemainDays(mailInbox.getRemainDays());

		return msg;
	}

	@Override
	public FriendMsg.Builder buildFriendMsg(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		
		Player player = playerService.getPlayerByID(playerId);
		if(player == null) return null;
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		if(playerProperty == null) return null;
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		if(playerExt == null) return null;
		
		FriendMsg.Builder msg = FriendMsg.newBuilder();
		msg.setPlayerId(player.getPlayerId());
		msg.setPlayerName(player.getPlayerName());
		msg.setLevel(playerProperty.getLevel());
		if(!gameSocketService.checkOnLine(playerId)){
			msg.setExitTime(playerExt.getExitTime().getTime());
		}		
		msg.setCareer(player.getCareer());		
	
		int CREATE_FAMILY_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
		if(playerProperty.getLevel() >= CREATE_FAMILY_LEVEL){
			PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);
			if(playerFamily != null && playerFamily.getPlayerFamilyId() > 0){
				msg.setPlayerFamilyId(playerFamily.getPlayerFamilyId());
				Family family = familyService.getFamily(playerFamily.getPlayerFamilyId());
				if(family == null) return msg;
				msg.setFamilyName(family.getFamilyName());
			}
		}
		
		return msg;
	}

	@Override
	public ApplyMsg.Builder builderApplyMsg(PlayerApply playerApply) {
		ApplyMsg.Builder msg = ApplyMsg.newBuilder();
		msg.setPlayerId(playerApply.getApplyPlayerId());		
		msg.setTime(playerApply.getApplyTime());
		msg.setPlayerName(playerApply.getApplyPlayerName());
		msg.setCareer(playerApply.getApplyPlayerCareer());
		msg.setLevel(playerApply.getApplyPlayerLevel());
		return msg;
	}

	@Override
	public Builder buildWakanMsg(PlayerWakan playerWakan) {
		WakanMsg.Builder msg = WakanMsg.newBuilder();
		msg.setPosId(playerWakan.getPosId());
		msg.setWakanLevel(playerWakan.getWakanLevel());
		msg.setWakanValue(playerWakan.getWakanValue());		
		return msg;
	}
	@Override
	public PlayerTaskMsg.Builder buildPlayerTaskMsg(PlayerTask model) {
		PlayerTaskMsg.Builder msg = PlayerTaskMsg.newBuilder();
		msg.setTaskId(model.getTaskId());
		msg.setType(model.getType());
		msg.setCurrentNum(model.getCurrentNum());
		msg.setTaskState(model.getTaskState());
		return msg;
	}
	
	@Override
	public PlayerWeaponEffectMsg.Builder buildPlayerWeaponEffectMsg(PlayerWeaponEffect model) {
		PlayerWeaponEffectMsg.Builder msg = PlayerWeaponEffectMsg.newBuilder();
		msg.setType(model.getType());
		msg.setHoleId(model.getHoldId());
		msg.setEffectId(model.getEffectId());
		msg.setBaseId(model.getBaseId());
		msg.setProValue(model.getProValue());
		return msg;
	}

	@Override
	public CollectItemInfoMsg.Builder buildCollectMsg(Collect model) {
		CollectItemInfoMsg.Builder msg = CollectItemInfoMsg.newBuilder();
		msg.setPlayerCollectId(model.getPlayerCollectId());
		msg.setCollectId(model.getCollectId());
		return msg;
	}

	@Override
	public BuffMsg.Builder buildBuffMsg(Buff buff) {
		BuffMsg.Builder msg = BuffMsg.newBuilder();
		msg.setId(buff.getId());
		if(buff.getFighter() != null){
			msg.setAttackGuid(buff.getFighter().getGuid());
		}else{
			msg.setAttackGuid(buff.getTargetGuid());	
		}
		msg.setTargetGuid(buff.getTargetGuid());
		msg.setBuffId(buff.getBuffId());
		msg.setType(buff.getType());
		msg.setEndTime(buff.getEndTime());
		msg.setHpShow(buff.getHpShow());
		return msg;
	}

	@Override
	public FamilyPlayerMsg.Builder buildFamilyPlayerMsg(PlayerFamily model) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		Player player = playerService.getPlayerByID(model.getPlayerId());
		if(player == null) return null;
		
		PlayerExt playerExt = playerService.getPlayerExtById(model.getPlayerId());
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(model.getPlayerId());
		
		FamilyPlayerMsg.Builder msg = FamilyPlayerMsg.newBuilder();
		msg.setPlayerId(model.getPlayerId());
		msg.setFamilyPosId(model.getFamilyPosId());
		msg.setFamilySortId(model.getFamilySortId());
		msg.setFamilyTitle(model.getFamilyTitle());		
		msg.setPlayerName(player.getPlayerName());		
		msg.setLevel(playerProperty.getLevel());
		msg.setCareer(player.getCareer());				
		
		msg.setDressStyle(playerExt.getDressStyle());
		msg.setWeaponStyle(playerExt.getWeaponStyle());
		msg.setExitTime(playerExt.getExitTime().getTime());
		msg.setWingStyle(playerExt.getWingStyle());
		
		boolean online = serviceCollection.getGameSocketService().checkOnLine(model.getPlayerId());
		msg.setOnline(online ? 1 : 0);
		if(online){
			PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(model.getPlayerId());
			if(playerPuppet != null){		
				msg.setDressStyle(playerPuppet.getDressStyle());	
				msg.setWeaponStyle(playerPuppet.getWeaponStyle());
				msg.setWingStyle(playerPuppet.getWingStyle());
			}
		}
		
		//System.out.println("玩家 【" + player.getPlayerName() + "】 排位【" + model.getFamilySortId() + "】");
		return msg;
	}
	
	@Override
	public MarketItemMsg.Builder buildMarketItemMsg(PlayerMarket model) {
		MarketItemMsg.Builder msg = MarketItemMsg.newBuilder();
		msg.setMarketId(model.getMarketId());
		msg.setCurBuyNum(model.getCurBuyNum());		
		return msg;
	}

	@Override
	public SignMsg.Builder buildSignMsg(PlayerSign model) {
		SignMsg.Builder msg = SignMsg.newBuilder();
		msg.setDay(DateService.getMonthDay(new Date()));
		if(model != null){		
			msg.setSignNum(model.getSignNum());
			msg.setConSignDay(model.getConSignDay());
			msg.setReSignNum(model.getReSignNum());
			msg.setState(model.getState());
			if(model.getConSignRewardList() != null){
				msg.addAllRewardList(model.getConSignRewardList());
			}		
		}
		
		return msg;
	}

	@Override
	public PlayerTradeBagMsg.Builder buildPlayerTradeBagMsg(PlayerTradeBag model) {
		PlayerTradeBagMsg.Builder msg = PlayerTradeBagMsg.newBuilder();
		msg.setGoodsType(model.getGoodsType());
		msg.setNum(model.getNum());
		msg.setIsBinding(model.getIsBinding());
		msg.setOverTime(model.getOverTime());
		msg.setPlayerBagId(model.getPlayerTradeBagId());
		msg.setPrice(model.getPrice());
		msg.setItemId(model.getItemId());
		msg.setState(model.getState());		
		return msg;
	}

	@Override
	public PlayerTradeEquipmentMsg.Builder buildPlayerTradeEquipmentMsg(PlayerEquipment model) {
		PlayerTradeEquipmentMsg.Builder msg = PlayerTradeEquipmentMsg.newBuilder();
		msg.setPlayerEquipmentId(model.getPlayerEquipmentId());
		msg.setEquipmentId(model.getEquipmentId());
		msg.setEquipType(model.getEquipType());
		msg.setHoleNum(model.getHoleNum());
		msg.setScore(model.getScore());
		msg.setIsBinding(model.getIsBinding());
		
		for(Map.Entry<Integer, Integer> entry : model.getAddAttrMap().entrySet()){
			SynPlayerPropertyMsg.Builder msginfo = SynPlayerPropertyMsg.newBuilder();
			msginfo.setPropertyId(entry.getKey());
			msginfo.setPropertyValue(entry.getValue());
			msg.addAddPropertyMsg(msginfo);
		}
		
		return msg;
	
	}

	@Override
	public RewardMsg.Builder buildRewardMsg(int rewardId, int state) {
		RewardMsg.Builder msg = RewardMsg.newBuilder();
		msg.setId(rewardId);
		msg.setState(state);
		return msg;
	}

	@Override
	public WingMsg.Builder buildWingMsg(PlayerWing playerWing) {
		WingMsg.Builder msg = WingMsg.newBuilder();
		msg.setWingId(playerWing.getWingId());
		msg.setStar(playerWing.getStar());
		msg.setDressFlag(playerWing.getDressFlag());
		msg.setWingValue(playerWing.getWingValue());
		return msg;
	}

	@Override
	public BattleValueRankMsg.Builder buildBattleValueRankMsg(BattleValueRank model) {
		BattleValueRankMsg.Builder msg = BattleValueRankMsg.newBuilder();
		msg.setCareer(model.getCareer());
		msg.setGuildName(model.getGuildName());
		msg.setLevel(model.getLevel());
		msg.setPlayerId(model.getPlayerId());
		msg.setPlayerName(model.getPlayerName());
		msg.setRank(model.getRank());
		msg.setValue(model.getValue());
		return msg;
	}

	@Override
	public EquipRankMsg.Builder buildEquipRankMsg(EquipRank model) {
		EquipRankMsg.Builder msg = EquipRankMsg.newBuilder();
		msg.setCareer(model.getCareer());
		msg.setGuildName(model.getGuildName());
		msg.setLevel(model.getLevel());
		msg.setPlayerId(model.getPlayerId());
		msg.setPlayerName(model.getPlayerName());
		msg.setRank(model.getRank());
		msg.setValue(model.getValue());
		return msg;
	}

	@Override
	public GoldRankMsg.Builder buildGoldRankMsg(GoldRank model) {
		GoldRankMsg.Builder msg = GoldRankMsg.newBuilder();
		msg.setCareer(model.getCareer());
		msg.setGuildName(model.getGuildName());
		msg.setLevel(model.getLevel());
		msg.setPlayerId(model.getPlayerId());
		msg.setPlayerName(model.getPlayerName());
		msg.setRank(model.getRank());
		msg.setValue(model.getValue());
		return msg;
	}

	@Override
	public EnemyMsg.Builder buildEnemyMsg(PlayerEnemy model) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IFamilyService familyService = serviceCollection.getFamilyService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		Player player = playerService.getPlayerByID(model.getEnemyPlayerId());
		
		if(player == null) return null;
		
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(model.getEnemyPlayerId());
		
		EnemyMsg.Builder msg = EnemyMsg.newBuilder();		
		msg.setCareer(player.getCareer());
		msg.setEnemyPlayerId(model.getEnemyPlayerId());
		msg.setEnemyPlayerName(player.getPlayerName());
		
		int CREATE_FAMILY_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
		if(playerProperty.getLevel() >= CREATE_FAMILY_LEVEL){
			PlayerFamily playerFamily = familyService.getPlayerFamily(model.getEnemyPlayerId());
			if(playerFamily != null && playerFamily.getPlayerFamilyId() > 0){
				Family family = familyService.getFamily(playerFamily.getPlayerFamilyId());
				msg.setFamilyName(family.getFamilyName());
			}	
		}
		
		msg.setIsOnline(serviceCollection.getGameSocketService().checkOnLine(model.getEnemyPlayerId()) ? 1 : 0);
		msg.setLevel(playerProperty.getLevel());
		msg.setCreateTime(model.getAddTime());
		
		return msg;
	}

	@Override
	public MonsterStateMsg.Builder buildMonsterStateMsg(int refreshId, int state) {
		
		MonsterStateMsg.Builder msg = MonsterStateMsg.newBuilder();
		msg.setRefreshId(refreshId);
		msg.setState(state);
		
		return msg;
	}

	@Override
	public LoadPkPlayerMsg.Builder buildLoadPkPlayerMsg(String guid, long playerId,
			String playerName, int level, int career, int stage, int star) {
		
		LoadPkPlayerMsg.Builder msg = LoadPkPlayerMsg.newBuilder();
		msg.setGuid(guid);
		msg.setPlayerId(playerId);
		msg.setPlayerName(playerName);
		msg.setCareer(career);
		msg.setLevel(level);
		msg.setStage(stage);
		msg.setStar(star);
		
		return msg;
	}


	@Override
	public EndPKPlayerMsg.Builder buildEndPKPlayerMsg(String guid, long playerId, int state, 
			int useTime, int score,	int WinNum, List<Reward> rewards, int destroyTime) {
		
		EndPKPlayerMsg.Builder msg = EndPKPlayerMsg.newBuilder();
		msg.setGuid(guid);
		msg.setPlayerId(playerId);
		msg.setScore(score);
		msg.setState(state);
		msg.setWinNum(WinNum);
		msg.setUseTime(useTime);		
		msg.setDestroyTime(destroyTime);
		
		if(rewards != null){
			for(Reward reward : rewards){
				msg.addRewards(this.buildPKRewardMsg(reward.getType(), reward.getId(), reward.getNum()));
			}	
		}		
		
		return msg;
	}

	@Override
	public PKRewardMsg.Builder buildPKRewardMsg(int goodType, int itemId, int num) {
		
		PKRewardMsg.Builder msg = PKRewardMsg.newBuilder();
		msg.setGoodsType(goodType);
		msg.setItemId(itemId);
	    msg.setNum(num);
		 
		return msg;
	}

	@Override
	public TurnRecMsg.Builder buildTurnRecMsg(long playerId, String playerName, int rewardId) {
		TurnRecMsg.Builder msg = TurnRecMsg.newBuilder();
		msg.setPlayerId(playerId);
		msg.setPlayerName(playerName);
		msg.setRewardId(rewardId);
		
		return msg;
	}

	@Override
	public GuildMsg.Builder buildGuildMsg(Guild guild) {
		GuildMsg.Builder msg = GuildMsg.newBuilder();
		msg.setGuildId(guild.getGuildId());
		msg.setGuildName(guild.getGuildName());
		msg.setNotice(guild.getNotice());
		msg.setLevel(guild.getLevel());
		msg.setMemberNum(guild.getPlayerIds().size());
		msg.setHeaderId(guild.getHeaderId());
		msg.setHeaderName(guild.getHeaderName());
		msg.setBattleValue(guild.getBattleValue());
		msg.setAutoJoin(guild.getAutoJoin());
		msg.setAutoMinLv(guild.getAutoMinLv());
		msg.setAutoMaxLv(guild.getAutoMaxLv());
		msg.setMoney(guild.getMoney());
		msg.setBuildNum(guild.getBuildNum());
		msg.setCreateTime(guild.getCreateTime().getTime());
		return msg;
	}

	@Override
	public GuildWarMsg.Builder buildGuildWarMsg(Guild guild) {
		GuildWarMsg.Builder msg = GuildWarMsg.newBuilder();
		msg.setGuildId(guild.getGuildId());
		msg.setGuildName(guild.getGuildName());
		msg.setLevel(guild.getLevel());
		msg.setBattleValue(guild.getBattleValue());
		return msg;
	}
	
	@Override
	public GuildPlayerMsg.Builder buildGuildPlayerMsg(PlayerGuild playerGuild) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		Player player = playerService.getPlayerByID(playerGuild.getPlayerId());
		if(player == null) return null;
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerGuild.getPlayerId());
		PlayerExt playerExt = playerService.getPlayerExtById(playerGuild.getPlayerId());
		
		GuildPlayerMsg.Builder msg = GuildPlayerMsg.newBuilder();
		msg.setPlayerId(playerGuild.getPlayerId());
		msg.setPlayerName(player.getPlayerName());
		msg.setLevel(playerProperty.getLevel());
		msg.setCareer(player.getCareer());
		msg.setBattleValue(playerProperty.getBattleValue());
		msg.setRoleId(playerGuild.getRoleId());
		msg.setJoinTime(playerGuild.getJoinTime().getTime());
		if(!gameSocketService.checkOnLine(playerGuild.getPlayerId())){
			msg.setExitTime(playerExt.getExitTime().getTime());
		}		
		msg.setTicket(playerGuild.getTicket());
		msg.setContribution(playerGuild.getContribution());
		msg.setWeekMoney(playerGuild.getWeekMoney());
		msg.setWeekBuildNum(playerGuild.getWeekBuildNum());
		return msg;
	}

	@Override
	public ApplyPlayerMsg.Builder buildApplyPlayerMsg(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		Player player = playerService.getPlayerByID(playerId);
		if(player == null) return null;
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		if(playerProperty == null) return null;
		
		ApplyPlayerMsg.Builder msg = ApplyPlayerMsg.newBuilder();
		msg.setPlayerId(playerId);
		msg.setPlayerName(player.getPlayerName());
		msg.setLevel(playerProperty.getLevel());
		msg.setCareer(player.getCareer());
		msg.setBattleValue(playerProperty.getBattleValue());
		
		if(!gameSocketService.checkOnLine(playerId)){
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			msg.setExitTime(playerExt.getExitTime().getTime());
		}	
		return msg;
	}

	@Override
	public PlayerFurnaceMsg.Builder buildPlayerFurnaceMsg(PlayerFurnace model) {
		PlayerFurnaceMsg.Builder msg = PlayerFurnaceMsg.newBuilder();
		msg.setFurnaceId(model.getFurnaceId());
		msg.setStage(model.getStage());
		msg.setStar(model.getStar());
		msg.setPiece(model.getPiece());
		return msg;
	}
	
	@Override
	public RewardDataMsg.Builder buildRewardDataMsg(int rewardId, int num) {
		RewardDataMsg.Builder msg = RewardDataMsg.newBuilder();
		msg.setId(rewardId);
		msg.setNum(num);
		return msg;
	}

}
