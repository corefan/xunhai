package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.TeamProto.C_AgreeInvite;
import com.message.TeamProto.C_ApplyJoinTeam;
import com.message.TeamProto.C_ApplyJoinTeamDeal;
import com.message.TeamProto.C_ChangeCaptain;
import com.message.TeamProto.C_ChangeTarget;
import com.message.TeamProto.C_Follow;
import com.message.TeamProto.C_GetInviteList;
import com.message.TeamProto.C_GetTeamList;
import com.message.TeamProto.C_Invite;
import com.message.TeamProto.C_KickTeamPlayer;
import com.message.TeamProto.C_PlayerAutoMatch;
import com.message.TeamProto.C_TeamAutoMatch;
import com.service.ITeamService;

/**
 * 组队接口
 * @author ken
 * @date 2017-3-2
 */
public class TeamAction {
	
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 组队大厅
	 */
	
	public void getTeamList(GameMessage gameMessage) throws Exception{
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetTeamList param = C_GetTeamList.parseFrom(gameMessage.getData());
		int activityId = param.getActivityId();
		teamService.getTeamList(playerId, activityId);
	}

	/**
	 * 创建队伍
	 */
	
	public void createTeam(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		teamService.createTeam(playerId);
	}

	/**
	 * 选择活动目标
	 */
	
	public void changeTarget(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		C_ChangeTarget param = C_ChangeTarget.parseFrom(gameMessage.getData());
		int activityId = param.getActivityId();
		int minLevel = param.getMinLevel();
		
		teamService.changeTarget(playerId, activityId, minLevel);
	}

	/**
	 * 获取社交邀请列表
	 */
	
	public void getInviteList(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_GetInviteList param = C_GetInviteList.parseFrom(gameMessage.getData());
		int type = param.getType();
		int start = param.getStart();
		int offset = param.getOffset();
		
		teamService.getInviteList(playerId, type, start, offset);
	}

	/**
	 * 邀请
	 */
	
	public void invite(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Invite param = C_Invite.parseFrom(gameMessage.getData());
		long inviterId = param.getInviterId();
		
		teamService.invite(playerId, inviterId);
	}

	/**
	 * 同意邀请 
	 */
	
	public void agreeInvite(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AgreeInvite param = C_AgreeInvite.parseFrom(gameMessage.getData());
		int teamId = param.getTeamId();
		
		teamService.agreeInvite(playerId, teamId);
	}

	/**
	 * 退出队伍
	 */
	
	public void quitTeam(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		teamService.quitTeam(playerId);
	}

	/**
	 * 踢队员
	 */
	
	public void kickTeamPlayer(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_KickTeamPlayer param = C_KickTeamPlayer.parseFrom(gameMessage.getData());
		
		teamService.kickTeamPlayer(playerId, param.getPlayerId());
	}

	/**
	 * 
	 * 转让队长
	 */
	
	public void changeCaptain(GameMessage gameMessage) throws Exception {

		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ChangeCaptain param = C_ChangeCaptain.parseFrom(gameMessage.getData());
		
		teamService.changeCaptain(playerId, param.getPlayerId());
	}

	/**
	 * 申请加入队伍
	 */
	
	public void applyJoinTeam(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ApplyJoinTeam param = C_ApplyJoinTeam.parseFrom(gameMessage.getData());
		int teamId = param.getTeamId();
		
		teamService.applyJoinTeam(playerId, teamId);		
	}

	/**
	 * 获取申请加入队伍消息
	 */
	
	public void getTeamApplyList(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		teamService.getTeamApplyList(playerId);
	}

	/**
	 * 加入队伍信息处理
	 */
	
	public void applyJoinTeamDeal(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ApplyJoinTeamDeal param = C_ApplyJoinTeamDeal.parseFrom(gameMessage.getData());
		long applyPlayerId = param.getApplyPlayerId();
		int state = param.getState();
		
		teamService.applyJoinTeamDeal(playerId, applyPlayerId, state);
	}

	/**
	 * 玩家自动匹配队伍
	 */
	
	public void playerAutoMatch(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();		
				
		C_PlayerAutoMatch param = C_PlayerAutoMatch.parseFrom(gameMessage.getData());
		int activityId = param.getActivityId();
		teamService.playerAutoMatch(playerId, activityId);
	}

	/**
	 * 队伍自动匹配玩家
	 */
	
	public void teamAutoMatch(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		C_TeamAutoMatch param = C_TeamAutoMatch.parseFrom(gameMessage.getData());
		
		teamService.teamAutoMatch(playerId, param.getState());
	}

	/**
	 * 跟随
	 */
	
	public void follow(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		C_Follow param = C_Follow.parseFrom(gameMessage.getData());
		teamService.follow(playerId, param.getState());
	}

	/**
	 * 清空队伍申请列表
	 */
	
	public void clearTeamApplyList(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		teamService.clearTeamApplyList(playerId);
	}

	/**
	 * 自动同意加入申请
	 */
	
	public void autoAgreeApply(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		teamService.autoAgreeApply(playerId);
	}

	/**
	 * 获取队长位置信息
	 */
	
	public void getCaptainPostion(GameMessage gameMessage) throws Exception {
		ITeamService teamService = serviceCollection.getTeamService();
		long playerId = gameMessage.getConnection().getPlayerId();		
		
		teamService.getCaptainPostion(playerId);		
	}

}
