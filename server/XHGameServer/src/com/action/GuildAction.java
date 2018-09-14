package com.action;

import java.util.List;
import java.util.Map;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ExceptionConstant;
import com.core.GameMessage;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.guild.Guild;
import com.domain.guild.GuildBuy;
import com.domain.guild.PlayerGuild;
import com.message.GuildProto.ApplyPlayerMsg;
import com.message.GuildProto.C_AgreeApply;
import com.message.GuildProto.C_AgreeInviteJoin;
import com.message.GuildProto.C_AgreeJoinUnion;
import com.message.GuildProto.C_ApplyGuild;
import com.message.GuildProto.C_ApplyUnion;
import com.message.GuildProto.C_AutoApply;
import com.message.GuildProto.C_ChangeGuildRole;
import com.message.GuildProto.C_ChangeGuilder;
import com.message.GuildProto.C_CreateGuild;
import com.message.GuildProto.C_CreateUnion;
import com.message.GuildProto.C_Donate;
import com.message.GuildProto.C_FeedManorBoss;
import com.message.GuildProto.C_GuildBuy;
import com.message.GuildProto.C_GuildFB;
import com.message.GuildProto.C_GuildWar;
import com.message.GuildProto.C_InviteJoin;
import com.message.GuildProto.C_KickGuild;
import com.message.GuildProto.C_ModifyNotice;
import com.message.GuildProto.C_RefuseApply;
import com.message.GuildProto.C_StudyGuildSkill;
import com.message.GuildProto.C_SubmitItem;
import com.message.GuildProto.C_UpgradeGuildSkill;
import com.message.GuildProto.GuildBuyMsg;
import com.message.GuildProto.S_AgreeApply;
import com.message.GuildProto.S_AgreeInviteJoin;
import com.message.GuildProto.S_AgreeJoinUnion;
import com.message.GuildProto.S_ApplyGuild;
import com.message.GuildProto.S_ApplyGuildFight;
import com.message.GuildProto.S_ApplyUnion;
import com.message.GuildProto.S_AutoApply;
import com.message.GuildProto.S_CallManorBoss;
import com.message.GuildProto.S_ChangeGuildRole;
import com.message.GuildProto.S_ChangeGuilder;
import com.message.GuildProto.S_ClearApplys;
import com.message.GuildProto.S_CreateGuild;
import com.message.GuildProto.S_Donate;
import com.message.GuildProto.S_FeedManorBoss;
import com.message.GuildProto.S_GetApplyList;
import com.message.GuildProto.S_GetGuild;
import com.message.GuildProto.S_GetGuildBuyData;
import com.message.GuildProto.S_GuildBuy;
import com.message.GuildProto.S_GuildFB;
import com.message.GuildProto.S_GuildWar;
import com.message.GuildProto.S_InviteJoin;
import com.message.GuildProto.S_KickGuild;
import com.message.GuildProto.S_ModifyNotice;
import com.message.GuildProto.S_QuickApply;
import com.message.GuildProto.S_QuitGuild;
import com.message.GuildProto.S_ReceiveGift;
import com.message.GuildProto.S_ReceiveRevenue;
import com.message.GuildProto.S_ReceiveSalary;
import com.message.GuildProto.S_RefuseApply;
import com.message.GuildProto.S_StudyGuildSkill;
import com.message.GuildProto.S_SubmitItem;
import com.message.GuildProto.S_UpgradeGuild;
import com.message.GuildProto.S_UpgradeGuildSkill;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IGuildService;
import com.service.IProtoBuilderService;

/**
 * 帮派接口
 * @author ken
 * @date 2018年4月10日
 */
public class GuildAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	private IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
	
	/**
	 * 帮派列表
	 */
	
	public void getGuildList(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getGuildList(playerId);
	}

	/**
	 * 帮派信息
	 */
	
	public void getGuild(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
		if(playerGuild == null || playerGuild.getGuildId() < 1){
			guildService.getGuildList(playerId);
			return;
		}
		
		Guild guild = guildService.getGuildById(playerGuild.getGuildId());
		if(guild == null) throw new GameException(ExceptionConstant.GUILD_3706);
		
		S_GetGuild.Builder builder = S_GetGuild.newBuilder();
		builder.setGuild(protoBuilderService.buildGuildMsg(guild));
		builder.setRoleId(playerGuild.getRoleId());
		builder.setContribution(playerGuild.getContribution());
		MessageObj msg = new MessageObj(MessageID.S_GetGuild_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 创建帮派
	 */
	
	public void createGuild(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_CreateGuild param = C_CreateGuild.parseFrom(gameMessage.getData());
		guildService.createGuild(playerId, param.getGuildName(), param.getNotice());
		
		S_CreateGuild.Builder builder = S_CreateGuild.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_CreateGuild_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 修改公告
	 */
	
	public void modifyNotice(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ModifyNotice param = C_ModifyNotice.parseFrom(gameMessage.getData());
		String notice = param.getNotice();
		
		guildService.modifyNotice(playerId, notice);
		
		S_ModifyNotice.Builder builder = S_ModifyNotice.newBuilder();
		builder.setNotice(notice);
		MessageObj msg = new MessageObj(MessageID.S_ModifyNotice_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 成员列表
	 */
	
	public void getGuildPlayerList(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getGuildPlayerList(playerId);
	}

	/**
	 * 申请帮派
	 */
	
	public void applyGuild(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ApplyGuild param = C_ApplyGuild.parseFrom(gameMessage.getData());
		
		long guildId = param.getGuildId();
		guildService.applyGuild(playerId, guildId);
		
		S_ApplyGuild.Builder builder = S_ApplyGuild.newBuilder();
		builder.setGuildId(guildId);
		MessageObj msg = new MessageObj(MessageID.S_ApplyGuild_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 一键申请
	 */
	
	public void quickApply(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		List<Long> guildIds = guildService.quickApply(playerId);
		
		S_QuickApply.Builder builder = S_QuickApply.newBuilder();
		builder.addAllGuildIds(guildIds);
		MessageObj msg = new MessageObj(MessageID.S_QuickApply_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 申请列表
	 */
	
	public void getApplyList(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		List<Long> playerIds = guildService.getApplyList(playerId);
		
		S_GetApplyList.Builder builder = S_GetApplyList.newBuilder();
		for(Long pid : playerIds){
			ApplyPlayerMsg.Builder pmsg = protoBuilderService.buildApplyPlayerMsg(pid);
			if(pmsg != null){
				builder.addApplyers(pmsg);	
			}
		}
		MessageObj msg = new MessageObj(MessageID.S_GetApplyList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 同意申请
	 */
	
	public void agreeApply(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AgreeApply param = C_AgreeApply.parseFrom(gameMessage.getData());
		long applyId = param.getApplyId();
		guildService.agreeApply(playerId, applyId);
		
		S_AgreeApply.Builder builder = S_AgreeApply.newBuilder();
		builder.setApplyId(applyId);
		MessageObj msg = new MessageObj(MessageID.S_AgreeApply_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 拒绝申请
	 */
	
	public void refuseApply(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_RefuseApply param = C_RefuseApply.parseFrom(gameMessage.getData());
		long applyId = param.getApplyId();
		guildService.refuseApply(playerId, applyId);
		
		S_RefuseApply.Builder builder = S_RefuseApply.newBuilder();
		builder.setApplyId(applyId);
		MessageObj msg = new MessageObj(MessageID.S_RefuseApply_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 清空申请列表
	 */
	
	public void clearApplys(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.clearApplys(playerId);
		
		S_ClearApplys.Builder builder = S_ClearApplys.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_ClearApplys_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);	
	}

	/**
	 * 自动接受申请
	 */
	
	public void autoAgreeApply(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AutoApply param = C_AutoApply.parseFrom(gameMessage.getData());
		int selected = param.getSelected();
		int autoMinLv = param.getAutoMinLv();
		int autoMaxLv = param.getAutoMaxLv();
		
		guildService.autoAgreeApply(playerId, selected, autoMinLv, autoMaxLv);
		
		S_AutoApply.Builder builder = S_AutoApply.newBuilder();
		builder.setSelected(selected);
		builder.setAutoMinLv(autoMinLv);
		builder.setAutoMaxLv(autoMaxLv);
		MessageObj msg = new MessageObj(MessageID.S_AutoApply_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 邀请进入
	 */
	
	public void inviteJoin(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_InviteJoin param = C_InviteJoin.parseFrom(gameMessage.getData());
		long invitedId = param.getInvitedId();
		guildService.inviteJoin(playerId, invitedId);
		
		S_InviteJoin.Builder builder = S_InviteJoin.newBuilder();
		builder.setInvitedId(invitedId);
		MessageObj msg = new MessageObj(MessageID.S_InviteJoin_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);	
		
	}

	/**
	 * 同意邀请
	 */
	
	public void agreeInvite(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AgreeInviteJoin param = C_AgreeInviteJoin.parseFrom(gameMessage.getData());
		long guildId = param.getGuildId();
		
		guildService.agreeInvite(playerId, guildId);
		
		S_AgreeInviteJoin.Builder builder = S_AgreeInviteJoin.newBuilder();
		builder.setGuildId(guildId);
		MessageObj msg = new MessageObj(MessageID.S_AgreeInviteJoin_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 退出帮派
	 */
	
	public void quitGuild(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.quitGuild(playerId);
		
		S_QuitGuild.Builder builder = S_QuitGuild.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_QuitGuild_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 转让帮主
	 */
	
	public void changeGuilder(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeGuilder param = C_ChangeGuilder.parseFrom(gameMessage.getData());
		long targetId = param.getTargetId();
		guildService.changeGuilder(playerId, targetId);
		
		S_ChangeGuilder.Builder builder = S_ChangeGuilder.newBuilder();
		builder.setTargetId(targetId);
		MessageObj msg = new MessageObj(MessageID.S_ChangeGuilder_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
		
	}

	/**
	 * 踢出帮派
	 */
	
	public void kickGuild(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_KickGuild param = C_KickGuild.parseFrom(gameMessage.getData());
		long targetId = param.getTargetId();
		guildService.kickGuild(playerId, targetId);
		
		S_KickGuild.Builder builder = S_KickGuild.newBuilder();
		builder.setTargetId(targetId);
		MessageObj msg = new MessageObj(MessageID.S_KickGuild_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 任免职务
	 */
	
	public void changeGuildRole(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeGuildRole param = C_ChangeGuildRole.parseFrom(gameMessage.getData());
		long targetId = param.getTargetId();
		int newRoleId = param.getNewRoleId();
		guildService.changeGuildRole(playerId, targetId, newRoleId);
		
		S_ChangeGuildRole.Builder builder = S_ChangeGuildRole.newBuilder();
		builder.setTargetId(targetId);
		builder.setNewRoleId(newRoleId);
		MessageObj msg = new MessageObj(MessageID.S_ChangeGuildRole_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 升级帮派
	 */
	
	public void upgradeGuild(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.upgradeGuild(playerId);
		
		S_UpgradeGuild.Builder builder = S_UpgradeGuild.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_UpgradeGuild_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 获取捐献各次数
	 */
	
	public void getDonateTimes(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getDonateTimes(playerId);
	}

	/**
	 * 捐献
	 */
	
	public void donate(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Donate param = C_Donate.parseFrom(gameMessage.getData());
		int id = param.getId();
		
		guildService.donate(playerId, id);
		
		S_Donate.Builder builder = S_Donate.newBuilder();
		builder.setId(id);
		MessageObj msg = new MessageObj(MessageID.S_Donate_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 获取宣战信息
	 */
	
	public void getGuildWarList(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getGuildWarList(playerId);
	}

	/**
	 * 发起宣战
	 */
	
	public void guildWar(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GuildWar param = C_GuildWar.parseFrom(gameMessage.getData());
		long guildId = param.getGuildId();
		
		long endWarTime = guildService.guildWar(playerId, guildId);
		
		S_GuildWar.Builder builder = S_GuildWar.newBuilder();
		builder.setGuildId(guildId);
		builder.setEndWarTime(endWarTime);
		MessageObj msg = new MessageObj(MessageID.S_GuildWar_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 研发帮派技能
	 */
	
	public void upgradeGuildSkill(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_UpgradeGuildSkill param = C_UpgradeGuildSkill.parseFrom(gameMessage.getData());
		int type = param.getType();
		
		int level = guildService.upgradeGuildSkill(playerId, type);
		if(level < 1) return;
		
		S_UpgradeGuildSkill.Builder builder = S_UpgradeGuildSkill.newBuilder();
		builder.setType(type);
		builder.setLevel(level);
		MessageObj msg = new MessageObj(MessageID.S_UpgradeGuildSkill_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 学习帮派技能
	 */
	
	public void studyGuildSkill(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_StudyGuildSkill param = C_StudyGuildSkill.parseFrom(gameMessage.getData());
		int type = param.getType();
		
		int level = guildService.studyGuildSkill(playerId, type);
		if(level < 1) return;
		
		S_StudyGuildSkill.Builder builder = S_StudyGuildSkill.newBuilder();
		builder.setType(type);
		builder.setLevel(level);
		MessageObj msg = new MessageObj(MessageID.S_StudyGuildSkill_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 获取已学习和研发的技能列表
	 */
	
	public void getGuildSkills(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getGuildSkills(playerId);
	}
	
	/**
	 * 城战面板
	 */
	public void getGuildFightData(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getGuildFightData(playerId);
	}
	
	/**
	 * 报名攻城
	 */
	public void applyGuildFight(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.applyGuildFight(playerId);
		
		S_ApplyGuildFight.Builder builder = S_ApplyGuildFight.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_ApplyGuildFight_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 报名攻城列表
	 */
	public void getGuildFights(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getGuildFights(playerId);
	}
	
	/**
	 * 创建联盟
	 */
	public void createUnion(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_CreateUnion param = C_CreateUnion.parseFrom(gameMessage.getData());
		
		guildService.createUnion(playerId, param.getUnionName());
	}
	
	/**
	 * 联盟列表
	 */
	public void getUnions(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getUnions(playerId);
	}
	
	/**
	 * 申请联盟
	 */
	public void applyUnion(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ApplyUnion param = C_ApplyUnion.parseFrom(gameMessage.getData());
		
		long unionId = param.getUnionId();
		guildService.applyUnion(playerId, unionId);
		
		S_ApplyUnion.Builder builder = S_ApplyUnion.newBuilder();
		builder.setUnionId(unionId);
		MessageObj msg = new MessageObj(MessageID.S_ApplyUnion_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 同意加入联盟
	 */
	public void agreeJoinUnion(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AgreeJoinUnion param = C_AgreeJoinUnion.parseFrom(gameMessage.getData());
		long guildId = param.getGuildId();
		guildService.agreeJoinUnion(playerId, guildId);
		
		S_AgreeJoinUnion.Builder builder = S_AgreeJoinUnion.newBuilder();
		builder.setGuildId(guildId);
		MessageObj msg = new MessageObj(MessageID.S_AgreeJoinUnion_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 提交攻城令
	 */
	public void submitItem(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_SubmitItem param = C_SubmitItem.parseFrom(gameMessage.getData());
		int itemNum = param.getItemNum();
		
		guildService.submitItem(playerId, itemNum);
		
		S_SubmitItem.Builder builder = S_SubmitItem.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_SubmitItem_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 进入城战
	 */
	public void enterGuildFight(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.enterGuildFight(playerId);
	}
	
	/**
	 * 获取税收数据
	 */
	public void getRevenueData(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getRevenueData(playerId);
		
	}
	
	/**
	 * 领取税收
	 */
	public void receiveRevenue(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.receiveRevenue(playerId);
		
		S_ReceiveRevenue.Builder builder = S_ReceiveRevenue.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_ReceiveRevenue_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 领取俸禄
	 */
	public void receiveSalary(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		int salaryNum = guildService.receiveSalary(playerId);
		
		S_ReceiveSalary.Builder builder = S_ReceiveSalary.newBuilder();
		builder.setSalaryNum(salaryNum);
		MessageObj msg = new MessageObj(MessageID.S_ReceiveSalary_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 领取礼包
	 */
	public void receiveGift(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();

		guildService.receiveGift(playerId);

		S_ReceiveGift.Builder builder = S_ReceiveGift.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_ReceiveGift_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 优惠购买记录
	 */
	public void getGuildBuyData(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		
		S_GetGuildBuyData.Builder builder = S_GetGuildBuyData.newBuilder();
		
		Map<Integer, GuildBuy> map = guildService.getGuidBuyMap();
		for(Map.Entry<Integer, GuildBuy> entry : map.entrySet()){
			GuildBuy model = entry.getValue();
			
			GuildBuyMsg.Builder msg = GuildBuyMsg.newBuilder();
			msg.setItemId(model.getItemId());
			msg.setBuyNum(model.getBuyNum());
			
			builder.addGuildBuys(msg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetGuildBuyData_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 优惠购买
	 */
	public void guildBuy(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GuildBuy param = C_GuildBuy.parseFrom(gameMessage.getData());
		int itemId = param.getItemId();
		int itemNum = param.getItemNum();
		
		int curBuyNum = guildService.guildBuy(playerId, itemId, itemNum);
		
		S_GuildBuy.Builder builder = S_GuildBuy.newBuilder();
		GuildBuyMsg.Builder info = GuildBuyMsg.newBuilder();
		info.setItemId(itemId);
		info.setBuyNum(curBuyNum);
		
		builder.setGuildBuy(info);
		
		MessageObj msg = new MessageObj(MessageID.S_GuildBuy_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 凌烟阁  1：开始 2：进入
	 */
	public void guildFB(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GuildFB param = C_GuildFB.parseFrom(gameMessage.getData());
		int type = param.getType();
		
		guildService.guildFB(playerId, type);
		
		S_GuildFB.Builder builder = S_GuildFB.newBuilder();
		builder.setType(type);
		MessageObj msg = new MessageObj(MessageID.S_GuildFB_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 获取领地面板数据
	 */
	public void getManorData(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.getManorData(playerId);
	}
	
	/**
	 * 进入帮派领地
	 */
	public void guildManor(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.guildManor(playerId);
	}
	
	/**
	 * 召唤领地boss
	 */
	public void callManorBoss(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		guildService.callManorBoss(playerId);
		
		S_CallManorBoss.Builder builder = S_CallManorBoss.newBuilder();
		MessageObj msg = new MessageObj(MessageID.S_CallManorBoss_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/**
	 * 喂养领地boss
	 */
	public void feedManorBoss(GameMessage gameMessage) throws Exception {
		IGuildService guildService = serviceCollection.getGuildService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_FeedManorBoss param = C_FeedManorBoss.parseFrom(gameMessage.getData());
		int itemNum= param.getItemNum();
		
		int feedNum = guildService.feedManorBoss(playerId, itemNum);
		
		S_FeedManorBoss.Builder builder = S_FeedManorBoss.newBuilder();
		builder.setFeedNum(feedNum);
		MessageObj msg = new MessageObj(MessageID.S_FeedManorBoss_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
}
