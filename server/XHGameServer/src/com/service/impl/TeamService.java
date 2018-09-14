package com.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.constant.TaskConstant;
import com.constant.TeamConstant;
import com.dao.team.BaseTeamDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.chat.Notice;
import com.domain.family.PlayerFamily;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.instance.PlayerInstance;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.team.BaseTeam;
import com.domain.team.Team;
import com.domain.team.TeamPlayer;
import com.domain.vip.PlayerVip;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TeamProto.InvitePlayerMsg;
import com.message.TeamProto.S_ApplyJoinTeam;
import com.message.TeamProto.S_ApplyJoinTeamDeal;
import com.message.TeamProto.S_AutoAgreeApply;
import com.message.TeamProto.S_GetCaptainPostion;
import com.message.TeamProto.S_GetInviteList;
import com.message.TeamProto.S_GetTeamApplyList;
import com.message.TeamProto.S_GetTeamList;
import com.message.TeamProto.S_HasNewInvite;
import com.message.TeamProto.S_Invite;
import com.message.TeamProto.S_KickTeamPlayer;
import com.message.TeamProto.S_QuitTeam;
import com.message.TeamProto.S_SynTeam;
import com.message.TeamProto.S_SynTeamPlayerHp;
import com.message.TeamProto.TeamMsg;
import com.message.TeamProto.TeamPlayerMsg;
import com.service.IBuffService;
import com.service.IChatService;
import com.service.IFamilyService;
import com.service.IGuildService;
import com.service.IInstanceService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ISceneService;
import com.service.ITeamService;
import com.service.IVipService;
import com.util.ComparatorUtil;
import com.util.SerialNumberUtil;

/**
 * 组队系统
 * @author ken
 * @date 2017-3-2
 */
public class TeamService implements ITeamService {

	private BaseTeamDAO baseTeamDAO = new BaseTeamDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseTeam> map = new HashMap<Integer, BaseTeam>();
		List<BaseTeam> lists = baseTeamDAO.listBaseTeams();
		for(BaseTeam model : lists){
			map.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TEAM, map);
	}

	/**
	 * 取组队活动限制等级
	 */
	@SuppressWarnings("unchecked")
	private BaseTeam getBaseTeam(int id){
		Map<Integer, BaseTeam> map = (Map<Integer, BaseTeam>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TEAM);
		return map.get(id);
	}
	
	@Override
	public void initCache() {
		CacheService.putToCache(CacheConstant.TEAM, new ConcurrentHashMap<Integer, Team>());
	}

	/**
	 * 取队伍数据 根据创建者
	 */
	public Team getTeam(int teamId){
		return this.getTeamMap().get(teamId);
	}
	
	/**
	 * 取队伍缓存列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Team> getTeamMap(){
		return (Map<Integer, Team>) CacheService.getFromCache(CacheConstant.TEAM);
	}
	
	@Override
	public void getTeamList(long playerId, int activityId) {
		if(playerId < 1) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		
		S_GetTeamList.Builder builder = S_GetTeamList.newBuilder();
		
		Map<Integer, Team> map = this.getTeamMap();
		for(Map.Entry<Integer, Team> entry : map.entrySet()){
			Team team = entry.getValue();
			
			if(team.getTeamPlayerMap().values().size() >= 4) continue;
			
			if(playerProperty.getLevel() < team.getMinLevel()) continue;
			
			if(activityId > 0){
				if(team.getActivityId() != activityId) continue;
			}			
			
			TeamPlayer tp = team.getTeamPlayerMap().get(playerId);
			if(tp != null) continue;
			
			TeamMsg.Builder msg = this.buildTeamMsg(team);
			if(msg == null) continue;
			
			builder.addTeamList(msg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetTeamList_VALUE,builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
		
	}

	/**
	 * 队伍信息
	 */
	private TeamMsg.Builder buildTeamMsg(Team team){
		Collection<TeamPlayer> tpLists = team.getTeamPlayerMap().values();
		if(tpLists == null || tpLists.isEmpty()) return null;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		TeamMsg.Builder msg = TeamMsg.newBuilder();
		msg.setTeamId(team.getTeamId());
		msg.setActivityId(team.getActivityId());
		msg.setMinLevel(team.getMinLevel());
		msg.setPlayerNum(tpLists.size());
		msg.setCreateTime(team.getCreateTime());
		
		for(TeamPlayer tp : tpLists){
			if(tp.isCaptain()){
				Player player = playerService.getPlayerByID(tp.getPlayerId());
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(tp.getPlayerId());
				
				msg.setPlayerId(tp.getPlayerId());
				msg.setPlayerName(player.getPlayerName());
				msg.setCareer(player.getCareer());
				msg.setLevel(playerProperty.getLevel());
				break;
			}
		}
		
		return msg;
	}
	
	@Override
	public void createTeam(long playerId)throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);			
			if(playerExt.getTeamId() > 0) throw new GameException(ExceptionConstant.TEAM_2300);
			
			IInstanceService instanceService = serviceCollection.getInstanceService();			
			Map<Integer, PlayerInstance> playerInstanceMap = instanceService.listPlayerInstances(playerId);
			PlayerInstance pInstance = playerInstanceMap.get(playerExt.getMapId());
			
			// 副本中无法创建队伍
			if(pInstance != null) throw new GameException(ExceptionConstant.TEAM_2311);
			
			Team team = new Team();
			team.setTeamId(SerialNumberUtil.getTeamId());
			team.setMinLevel(1);
			team.setAutoAgreeApply(1);
			team.setAutoMatch(1);
			team.setAutoMatchTime(System.currentTimeMillis());
			team.setCreateTime(System.currentTimeMillis());
			TeamPlayer teamPlayer = this.createTeamPlayer(playerId, 1, true);
			team.getTeamPlayerMap().put(playerId, teamPlayer);
			
			this.getTeamMap().put(team.getTeamId(), team);

			playerExt.setTeamId(team.getTeamId());
			
			List<Long> playerIds = new ArrayList<Long>();
			playerIds.add(playerId);
			this.synTeam(playerIds, team);			
			
			for(Long id : playerIds){
				GameContext.getInstance().getServiceCollection().getTaskService().executeTask(id, TaskConstant.TYPE_17, null);
			}
			
			PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
			if(playerPuppet != null){
				// 同步队伍ID					
				playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.TEAM_ID, team.getTeamId());
			}
			
			// 判断是否触发家族buff
			this.checkFamilyBuff(playerId, team.getTeamId());			
		} 
	}
	
	/**
	 * 同步队伍
	 */
	public void synTeam(List<Long> playerIds, Team team){
		if(playerIds.isEmpty()) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		S_SynTeam.Builder builder = S_SynTeam.newBuilder();
		builder.setTeamId(team.getTeamId());
		builder.setActivityId(team.getActivityId());
		builder.setMinLevel(team.getMinLevel());
		builder.setState(team.getTeamPlayerMap().values().size() >= 4 ? 1 : 0);
		
		for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
			TeamPlayerMsg.Builder msg = this.buildTeamPlayerMsg(entry.getValue());
			if(msg == null) continue;
			builder.addListTeamPlayers(msg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SynTeam_VALUE,builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);
	}
	
	@Override
	public void synTeam(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);			
		if(playerExt.getTeamId() > 0){
			Team team = this.getTeam(playerExt.getTeamId());
			if(team == null) return;
			List<Long> tplayerIds = this.getOnlineTeamPlayerIds(team);
			this.synTeam(tplayerIds, team);
		}			
	}
	
	/**
	 * 队员信息
	 */
	private TeamPlayerMsg.Builder buildTeamPlayerMsg(TeamPlayer model) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();
		
		Player player = playerService.getPlayerByID(model.getPlayerId());
		if(player == null) return null;
		
		PlayerExt playerExt = playerService.getPlayerExtById(model.getPlayerId());
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(model.getPlayerId());
		
		
		TeamPlayerMsg.Builder msg = TeamPlayerMsg.newBuilder();
		msg.setPlayerId(model.getPlayerId());
		msg.setPlayerName(player.getPlayerName());
		msg.setTeamIndex(model.getTeamIndex());
		msg.setCaptain(model.isCaptain());
		msg.setLevel(playerProperty.getLevel());
		msg.setCareer(player.getCareer());

 		msg.setMapId(playerExt.getMapId());
		msg.setWeaponStyle(playerExt.getWeaponStyle());
		msg.setDressStyle(playerExt.getDressStyle());
		msg.setHp(playerExt.getHp());
		msg.setMaxHp(playerProperty.getHpMax());
		
		int online = serviceCollection.getGameSocketService().checkOnLine(model.getPlayerId()) ? 1 : 0;
		msg.setOnline(online);
		
		if(online == 1){
			PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(model.getPlayerId());
			if(playerPuppet != null){
				msg.setMapId(playerPuppet.getMapId());
				msg.setWeaponStyle(playerPuppet.getWeaponStyle());
				msg.setDressStyle(playerPuppet.getDressStyle());
				msg.setHp(playerPuppet.getHp());
				msg.setMaxHp(playerPuppet.getHpMax());
			}
		}
		
		msg.setBattleValue(playerProperty.getBattleValue());
		
		PlayerVip playerVip = vipService.getPlayerVip(model.getPlayerId());
		if (playerVip != null){
			msg.setVipLevel(playerVip.getLevel());
		}
		
		return msg;
	}
	
	/**
	 * 创建队员
	 */
	private TeamPlayer createTeamPlayer(long playerId, int teamIndex, boolean captain){
		TeamPlayer tp = new TeamPlayer();
		tp.setPlayerId(playerId);
		tp.setTeamIndex(teamIndex);
		tp.setCaptain(captain);
		return tp;
	}

	@Override
	public void changeTarget(long playerId, int activityId, int minLevel) throws Exception {
		if(playerId < 1 || minLevel < 0 || minLevel > 100) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			Team team = this.getTeam(playerExt.getTeamId());
			if(team == null) throw new GameException(ExceptionConstant.TEAM_2301);
			
			TeamPlayer tp = team.getTeamPlayerMap().get(playerId);
			if(tp == null) throw new GameException(ExceptionConstant.TEAM_2301);
			
			if(!tp.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			BaseTeam baseTeam = this.getBaseTeam(activityId);
			if(baseTeam == null) throw new GameException(ExceptionConstant.TEAM_2301);
			int lv = baseTeam.getMinimumLevel();
			if(minLevel < lv){
				minLevel = lv;
			}
				
			team.setActivityId(activityId);
			team.setMinLevel(minLevel);
			
			List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
			this.synTeam(playerIds, team);
			
			// 更换目标通知
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PARAM, 0, 0, baseTeam.getTargetName());
			paramList.add(notice1);			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_10, paramList, playerIds);
			
			// 更换最低等级通知
			List<Notice> paramList1 = new ArrayList<Notice>();			
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, team.getMinLevel()+"");
			paramList1.add(notice2);			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_11, paramList1, playerIds);
		}		
	}

	@Override
	public void getInviteList(long playerId, int type, int start, int offset) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		PlayerExt pext = playerService.getPlayerExtById(playerId);
		int minLevel = 0;
		Team team = this.getTeam(pext.getTeamId());
		if(team != null){
			minLevel = team.getMinLevel();
		}
		List<Long> allPlayerIds = new ArrayList<Long>();
		if(type == 1){
			//好友
			allPlayerIds = serviceCollection.getFriendService().getFriendList(playerId, 1);

		}else if(type == 2){
			//帮派 
			IGuildService guildService = serviceCollection.getGuildService();
			PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
			if(playerGuild != null && playerGuild.getGuildId() > 0){
				Guild guild = guildService.getGuildById(playerGuild.getGuildId());
				allPlayerIds.addAll(guild.getPlayerIds());
			}
		}else if(type == 3){
			//家族
			IFamilyService familyService = serviceCollection.getFamilyService();
			PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);	
			if (playerFamily.getPlayerFamilyId() > 0){
				allPlayerIds = familyService.getFamilyPlayerIds(playerFamily.getPlayerFamilyId());
			}
		}
		
		List<Long> lists = new ArrayList<Long>();
		for(Long pid : allPlayerIds){
			if(pid.equals(playerId)) continue;
			
			PlayerExt playerExt = playerService.getPlayerExtById(pid);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(pid);
			
			if(playerProperty.getLevel() < minLevel) continue;
			
			if(playerExt.getTeamId() > 0) continue;
			
			if(!gameSocketService.checkOnLine(pid)) continue;
			
			lists.add(pid);
		}
		
		int playerNum = lists.size();
		
		if(start <= lists.size()) {
			int fromIndex = start - 1;
			if(fromIndex < 0) fromIndex=0;
			int toIndex = fromIndex + offset;
			if(toIndex > lists.size()) toIndex = lists.size();
			lists = lists.subList(fromIndex, toIndex);
		}else{
			lists = new ArrayList<Long>();
		}
		
		
		S_GetInviteList.Builder builder = S_GetInviteList.newBuilder();
		builder.setType(type);
		builder.setPlayerNum(playerNum);
		for(Long id : lists){

			InvitePlayerMsg.Builder msg = this.buildInvitePlayerMsg(id);
			if(msg == null) continue;
			
			builder.addInvitePageList(msg);
		}
		MessageObj msg = new MessageObj(MessageID.S_GetInviteList_VALUE,builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void invite(long playerId, long inviterId) throws Exception {
		if(playerId < 1 || inviterId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM_INVITE)) {
			
			PlayerExt targetPlayerExt = playerService.getPlayerExtById(inviterId);
			if(targetPlayerExt == null) {
				throw new GameException(ExceptionConstant.PLAYER_1115);			
			}
			
			if(targetPlayerExt.getTeamId() > 0){
				throw new GameException(ExceptionConstant.TEAM_2303);
			}
			
			if(!gameSocketService.checkOnLine(inviterId)){
				throw new GameException(ExceptionConstant.PLAYER_1111);
			}
				
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			if(playerExt.getTeamId() > 0){
				//有队伍
				
				Team team = this.getTeam(playerExt.getTeamId());
				
				TeamPlayer tp = team.getTeamPlayerMap().get(playerId);
				
				if(tp == null || !tp.isCaptain()){
					throw new GameException(ExceptionConstant.TEAM_2302);
				}
				
			}else{
			    //没队伍				
				this.createTeam(playerId);				
			}			

			IInstanceService instanceService = serviceCollection.getInstanceService();			
			Map<Integer, PlayerInstance> playerInstanceMap = instanceService.listPlayerInstances(inviterId);
			PlayerInstance pInstance = playerInstanceMap.get(targetPlayerExt.getMapId());
			// 副本中无法收到邀请
			if(pInstance != null) throw new GameException(ExceptionConstant.TEAM_2310);
			
			S_Invite.Builder Inbuilder = S_Invite.newBuilder();
			MessageObj msgIn = new MessageObj(MessageID.S_Invite_VALUE, Inbuilder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msgIn);
			
			Player player = playerService.getPlayerByID(playerId);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			Team team = this.getTeam(playerExt.getTeamId());
			
			//推送  有新的邀请
			S_HasNewInvite.Builder builder = S_HasNewInvite.newBuilder();
			builder.setTeamId(team.getTeamId());
			builder.setPlayerLevel(playerProperty.getLevel());
			builder.setMinLevel(team.getMinLevel());
			builder.setActivityId(team.getActivityId());
			builder.setPlayerName(player.getPlayerName());
			MessageObj msg = new MessageObj(MessageID.S_HasNewInvite_VALUE,builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(inviterId, msg);
		}
	}

	/**
	 * 邀请列表玩家信息
	 */
	private InvitePlayerMsg.Builder buildInvitePlayerMsg(long playerId){
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Player player = playerService.getPlayerByID(playerId);
		if(player == null) return null;
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		
		InvitePlayerMsg.Builder msg = InvitePlayerMsg.newBuilder();
		msg.setPlayerId(playerId);
		msg.setPlayerName(player.getPlayerName());
		msg.setLevel(playerProperty.getLevel());
		msg.setCareer(player.getCareer());
		
		int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
		if(playerProperty.getLevel() >= GUILD_NEED_LEVEL){
			IGuildService guildService = serviceCollection.getGuildService();
			PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
			if(playerGuild != null && playerGuild.getGuildId() > 0){
				Guild guild = guildService.getGuildById(playerGuild.getGuildId());
				if(guild != null){
					msg.setGuildName(guild.getGuildName());
				}
			}
		}
		
		return msg;
	}

	@Override
	public void agreeInvite(long playerId, int teamId) throws Exception {	
		if(playerId < 1 || teamId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			Team team = this.getTeam(teamId);
			if(team == null){
				throw new GameException(ExceptionConstant.TEAM_2304);
			}		
			synchronized (team.getLock()) {
				// 玩家加入队伍
				this.joinTeam(playerId, team);
				// 任务触发
				GameContext.getInstance().getServiceCollection().getTaskService().executeTask(playerId, TaskConstant.TYPE_17, null);	
			}

		}

	}

	@Override
	public void quitTeam(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());
			
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2301);
			
			synchronized (team.getLock()) {
				//判断副本情况
				BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
				if(baseMap.isInstance()) throw new GameException(ExceptionConstant.TEAM_2308);
				
				TeamPlayer tp = team.getTeamPlayerMap().get(playerId);
				if(tp == null) throw new GameException(ExceptionConstant.TEAM_2301);
				
				if(tp.isCaptain()){
					//队长退出队伍
					tp.setCaptain(false);
					this.captainQuit(playerId, team);				
				}	
				
				team.getTeamPlayerMap().remove(playerId);
				playerExt.setTeamId(0);
				
				List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
				if(playerIds.isEmpty()){					
					this.getTeamMap().remove(team.getTeamId());
					team = null;
				}else{
					this.synTeam(playerIds, team);
				}
				
				PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
				if(playerPuppet != null){
					// 同步队伍ID					
					playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.TEAM_ID, playerExt.getTeamId());
				}
				
				S_QuitTeam.Builder builder = S_QuitTeam.newBuilder();
				MessageObj msg = new MessageObj(MessageID.S_QuitTeam_VALUE,builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);

				// 检测家族buff
				int teamId = 0;
				if(team != null){
					teamId = team.getTeamId();
				}
				this.checkFamilyBuff(playerId, teamId);
				
				// 退出队长通知
				IChatService chatService = serviceCollection.getChatService();
				Player player = playerService.getPlayerByID(playerId);
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
				paramList.add(notice1);			
				
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_14, paramList, playerIds);
			}
	
		}
	}
	
	/**
	 * 队长退队
	 */
	private void captainQuit(long playerId, Team team){		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService =  serviceCollection.getGameSocketService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
	
		TeamPlayer nextCaptain = null;
		for(int index = 1; index<= 4; index++){
			for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
				TeamPlayer tPlayer = entry.getValue();
				if(index == tPlayer.getTeamIndex()){
					if(tPlayer.getPlayerId() != playerId && gameSocketService.checkOnLine(tPlayer.getPlayerId())){
						nextCaptain = tPlayer;
					}
					break;
				}
			}
			if(nextCaptain != null){
				break;
			}
		}
		
		if(nextCaptain != null){
			nextCaptain.setCaptain(true);
			nextCaptain.setFollow(0);
		}else{
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			playerExt.setTeamId(0);
			this.getTeamMap().remove(team.getTeamId());					
			team = null;	
		}
	}
	
	/**
	 * 所有在线队员编号
	 */
	public List<Long> getOnlineTeamPlayerIds(Team team){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		List<Long> playerIds = new ArrayList<Long>();
		
		for(Long tpId : team.getTeamPlayerMap().keySet()){
			if(gameSocketService.checkOnLine(tpId)){
				playerIds.add(tpId);
			}
		}
		return playerIds;
	}

	@Override
	public void kickTeamPlayer(long playerId, long teamPlayerId) throws Exception {
		if(playerId < 1 || teamPlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());
			
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2301);
			
			synchronized (team.getLock()) {
				TeamPlayer tp = team.getTeamPlayerMap().get(playerId);
				
				if(!tp.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
				
				if(tp.getPlayerId() == teamPlayerId)  throw new GameException(ExceptionConstant.TEAM_2304);
				
				BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
				if(baseMap.isInstance()) throw new GameException(ExceptionConstant.TEAM_2309);
				
				PlayerExt teamPlayerExt = playerService.getPlayerExtById(teamPlayerId);
				
				//队员退出队伍
				team.getTeamPlayerMap().remove(teamPlayerId);
			
				teamPlayerExt.setTeamId(0);
				
				List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
				this.synTeam(playerIds, team);
				
				PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(teamPlayerId);
				if(playerPuppet != null){
					// 同步队伍ID					
					playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.TEAM_ID, teamPlayerExt.getTeamId());
				}
				
				S_KickTeamPlayer.Builder builder = S_KickTeamPlayer.newBuilder();
				MessageObj msg = new MessageObj(MessageID.S_KickTeamPlayer_VALUE,builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(teamPlayerId, msg);
			
				// 处理家族buff
				this.checkFamilyBuff(teamPlayerId, team.getTeamId());
				
				// 踢人
				Player player = playerService.getPlayerByID(teamPlayerId);	
				IChatService chatService = serviceCollection.getChatService();
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PLAYER, teamPlayerId, 0, player.getPlayerName());
				paramList.add(notice1);			
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_22, paramList, playerIds);
			}

			
		}
	}

	@Override
	public void changeCaptain(long playerId, long teamPlayerId) throws Exception {
		if(playerId < 1 || teamPlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IChatService chatService = serviceCollection.getChatService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			
			PlayerExt playerExt = playerService.getPlayerExtById(teamPlayerId);
			if(playerExt == null || !serviceCollection.getGameSocketService().checkOnLine(teamPlayerId)) throw new GameException(ExceptionConstant.PLAYER_1111); 
			
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());
			
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2301);
			
			TeamPlayer tp = team.getTeamPlayerMap().get(playerId);
			
			if(!tp.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			if(tp.getPlayerId() == teamPlayerId)  throw new GameException(ExceptionConstant.TEAM_2307);
			
			tp.setCaptain(false);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(teamPlayerId);
			teamPlayer.setCaptain(true);
			teamPlayer.setFollow(0);
			
			List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
			this.synTeam(playerIds, team);
			
			// 更换队长通知
			Player player = playerService.getPlayerByID(teamPlayerId);
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, teamPlayerId, 0, player.getPlayerName());
			paramList.add(notice1);			
			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_12, paramList, playerIds);
		}
	}

	@Override
	public void synUpLevel(PlayerExt playerExt) {
		if(playerExt.getTeamId() > 0){
			Team team = this.getTeam(playerExt.getTeamId());
			if(team != null){
				List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
				this.synTeam(playerIds, team);
			}
		}
	}
	
	@Override
	public void synHp(PlayerPuppet playerPuppet) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerPuppet.getEid());
		if(playerExt.getTeamId() > 0){
			Team team = this.getTeam(playerExt.getTeamId());
			if(team != null){
				List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
				
				S_SynTeamPlayerHp.Builder builder =  S_SynTeamPlayerHp.newBuilder();
				builder.setPlayerId(playerPuppet.getEid());
				builder.setHp(playerPuppet.getHp());
				builder.setMaxHp(playerPuppet.getHpMax());
				
				MessageObj msg = new MessageObj(MessageID.S_SynTeamPlayerHp_VALUE,builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);
			}
		}
		
	}	

	@Override
	public void dealLogin(PlayerExt playerExt) {
		if(playerExt.getTeamId() > 0){			
			Team team = this.getTeam(playerExt.getTeamId());
			if(team == null){
				playerExt.setTeamId(0);
				return;
			}
			
			TeamPlayer tp = team.getTeamPlayerMap().get(playerExt.getPlayerId());
			if(tp == null){
				playerExt.setTeamId(0);
				return;	
			}
			
			List<Long> playerIds = this.getOnlineTeamPlayerIds(team);
			
			this.synTeam(playerIds, team);			
		}
	}
	
	@Override
	public void dealExit(PlayerExt playerExt) {
		
		if(playerExt.getTeamId() > 0){
			Team team = this.getTeam(playerExt.getTeamId());
			
			if(team != null){
				TeamPlayer tp = team.getTeamPlayerMap().get(playerExt.getPlayerId());
				if(tp == null) return;				
				
				if(tp.isCaptain()){
					tp.setCaptain(false);
					this.captainQuit(playerExt.getPlayerId(), team);
				}				
			
				List<Long> playerIds = this.getOnlineTeamPlayerIds(team);				
				if(playerIds.isEmpty()){					
					playerExt.setTeamId(0);
					this.getTeamMap().remove(team.getTeamId());					
					team = null;					
				}else{
					// 下线清除跟随状态
					tp.setFollow(0);
					this.synTeam(playerIds, team);
				}				
			}
			
		}		
	}
	
	@Override
	public void applyJoinTeam(Long playerId, int teamId) throws Exception {
		if(playerId <= 0 || teamId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
				
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() > 0) throw new GameException(ExceptionConstant.TEAM_2305);
			
			Team team = this.getTeam(teamId);				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2301);
			
			// 如果自动同意申请
			if(team.getAutoAgreeApply() == TeamConstant.AUTO_AGREE_APPLY){
				this.joinTeam(playerId, team);
				return;
			}
			
			ConcurrentLinkedDeque<Long> teamApplyIdList = team.getTeamApplyIdList();			
			if (teamApplyIdList.contains(playerId)){
				teamApplyIdList.remove(playerId);
			}
			
			// 如果列表长度超出上限删除首元素
			int APPLY_JOIN_TEAM_lIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.APPLY_JOIN_TEAM_lIMIT);
			if(teamApplyIdList.size() >= APPLY_JOIN_TEAM_lIMIT){
				teamApplyIdList.removeLast();
			}
			
			// 加入申请列表
			teamApplyIdList.addFirst(playerId);
			
			// 同步给队长
			if(team != null &&team.getTeamPlayerMap() != null && !teamApplyIdList.isEmpty()){					
				for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
					TeamPlayer teamPlayer = entry.getValue();
					if(!teamPlayer.isCaptain()) continue;
					
					S_ApplyJoinTeam.Builder builder = S_ApplyJoinTeam.newBuilder();							
					MessageObj msg = new MessageObj(MessageID.S_ApplyJoinTeam_VALUE, builder.build().toByteArray());
					serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(teamPlayer.getPlayerId(), msg);
				}												
			}			

			// 同步给申请的玩家
			S_ApplyJoinTeam.Builder builder = S_ApplyJoinTeam.newBuilder();		
			MessageObj msg = new MessageObj(MessageID.S_ApplyJoinTeam_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
		}			
	}

	@Override
	public void getTeamApplyList(long playerId) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
				
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2301);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(!teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			S_GetTeamApplyList.Builder builder = S_GetTeamApplyList.newBuilder();
			
			if(!team.getTeamApplyIdList().isEmpty()){				
				for(Long id : team.getTeamApplyIdList()){
					InvitePlayerMsg.Builder msg = this.buildInvitePlayerMsg(id);
					if(msg == null) continue;
					
					builder.addApplyList(msg);
				}				
			}
			
			MessageObj msg = new MessageObj(MessageID.S_GetTeamApplyList_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void applyJoinTeamDeal(long playerId, Long applyPlayerId, int state) throws Exception {
		if(playerId <= 0 ||applyPlayerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2304);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(!teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);			
					
			// 删除申请信息
			ConcurrentLinkedDeque<Long> lists = team.getTeamApplyIdList();
			if(lists != null && !lists.isEmpty()){
				lists.remove(applyPlayerId);
			}
			
			Player player =  playerService.getPlayerByID(playerId);	
			// 拒绝
			S_ApplyJoinTeamDeal.Builder builder = S_ApplyJoinTeamDeal.newBuilder();
			if(state == 0){
				// 发送给的队长
				builder.setState(state);
				builder.setApplyPlayerId(applyPlayerId);
				builder.setPlayerName(player.getPlayerName());
				MessageObj msg = new MessageObj(MessageID.S_ApplyJoinTeamDeal_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
				
				// 发送给申请的玩家
				MessageObj msg1 = new MessageObj(MessageID.S_ApplyJoinTeamDeal_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(applyPlayerId, msg1);
				return;
			}			
	
			synchronized (team.getLock()) {
				this.joinTeam(applyPlayerId, team);
				
				// 发送给的队长
				builder.setState(state);
				builder.setApplyPlayerId(applyPlayerId);
				builder.setPlayerName(player.getPlayerName());
				MessageObj msg = new MessageObj(MessageID.S_ApplyJoinTeamDeal_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);	
			}

		}
	}

	/** 玩家加入队伍 */
	private void joinTeam(long playerId, Team team) throws Exception{
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		if(playerExt.getTeamId() > 0) throw new GameException(ExceptionConstant.TEAM_2303);
		
		IInstanceService instanceService = serviceCollection.getInstanceService();			
		Map<Integer, PlayerInstance> playerInstanceMap = instanceService.listPlayerInstances(playerId);
		PlayerInstance pInstance = playerInstanceMap.get(playerExt.getMapId());
		
		// 副本中无法加入队伍
		if(pInstance != null) throw new GameException(ExceptionConstant.TEAM_2311);
		
		if(team.getTeamPlayerMap().values().size() >= 4){
			throw new GameException(ExceptionConstant.TEAM_2306);
		}

		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		if(playerProperty.getLevel() < team.getMinLevel()){
			throw new GameException(ExceptionConstant.PLAYER_1110);
		}
		
		playerExt.setTeamId(team.getTeamId());
		
		for(int i= 1; i<= 4; i++){
			boolean bFind = false;
			for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
				TeamPlayer tp = entry.getValue();					
				if(tp.getTeamIndex() == i){
					
					bFind = true;
					break;
				}
			}
			if(!bFind){
				TeamPlayer teamPlayer = this.createTeamPlayer(playerId, i, false);
				team.getTeamPlayerMap().put(playerId, teamPlayer);
				if(team.getTeamPlayerMap().values().size() == 4){
					team.setAutoMatch(TeamConstant.NOT_AUTO_MATCH);
					team.setAutoMatchTime(0);
				}
				break;
			}
		}
		
		List<Long> playerIds = this.getOnlineTeamPlayerIds(team);		
		this.synTeam(playerIds, team);
		
		PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
		if(playerPuppet != null){
			// 同步队伍ID					
			playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.TEAM_ID, team.getTeamId());
		}	
		
		// 触发组队任务
		GameContext.getInstance().getServiceCollection().getTaskService().executeTask(playerId, TaskConstant.TYPE_17, null);
			
		// 判断是否触发家族buff
		this.checkFamilyBuff(playerId, team.getTeamId());
		
		// 进入队伍通知
		IChatService chatService = serviceCollection.getChatService();
		Player player = playerService.getPlayerByID(playerId);
		List<Notice> paramList = new ArrayList<Notice>();			
		Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
		paramList.add(notice1);			
		
		chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_13, paramList, playerIds);
	}

	@Override
	public void playerAutoMatch(long playerId, int activityId) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() > 0) throw new GameException(ExceptionConstant.TEAM_2305);
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			List<Team> teamList = new ArrayList<Team>();
			
			Map<Integer, Team> map = this.getTeamMap();
			for(Map.Entry<Integer, Team> entry : map.entrySet()){
				Team team = entry.getValue();
				if (team.getAutoMatch() == TeamConstant.NOT_AUTO_MATCH) continue;
				if(playerProperty.getLevel() < team.getMinLevel()) continue;
				if(team.getActivityId() != activityId) continue;
				
				teamList.add(team);
			}
			
			if(teamList.isEmpty()) return;			
			Collections.sort(teamList, new ComparatorUtil(ComparatorUtil.DOWN));	
			
			Team team = teamList.get(0);
					
			synchronized (team.getLock()) {
				this.joinTeam(playerId, team);
			}
			
		}
	}

	@Override
	public void teamAutoMatch(long playerId, int autoMatch) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2304);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(!teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			team.setAutoMatch(autoMatch);		
			team.setAutoMatchTime(System.currentTimeMillis());
			
		}		
	}

	@Override
	public void follow(long playerId, int state) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2304);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2307);
		
			teamPlayer.setFollow(state);			
		}
	}
	
	/** 判断家族BUFF添加移除 
	 * @param 变动的玩家
	 * @param 添加 1, 移除 -1
	 * 
	 */
	@Override
	public void checkFamilyBuff(long playerId, int teamId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IFamilyService familyService = serviceCollection.getFamilyService();
		IBuffService buffService = serviceCollection.getBuffService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);		
		
		PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);
		if(playerFamily == null) return;
	
		int FAMILY_BUFF = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FAMILY_BUFF);
		if(playerExt.getTeamId() < 1 || playerFamily.getPlayerFamilyId() < 1) {
			BasePuppet playerPuppet = sceneService.getPlayerPuppet(playerId);	
			if(playerPuppet != null){
				buffService.removeBuffById(playerPuppet, FAMILY_BUFF);
			}			
		}		
		
		if(teamId < 1) return;
		Team team = this.getTeam(teamId);	
		if(team == null) return;
		
		// 队伍玩家 < 3
		if(team.getTeamPlayerMap().size() < 3){
			for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
				TeamPlayer tp = entry.getValue();
				PlayerFamily tplayerFamily = familyService.getPlayerFamily(tp.getPlayerId());
				if(tplayerFamily == null) continue;	
				BasePuppet tPuppet =sceneService.getPlayerPuppet(tp.getPlayerId());
				if(tPuppet == null) continue;	
				if(tplayerFamily.getPlayerFamilyId() > 0) {
					buffService.removeBuffById(tPuppet, FAMILY_BUFF);
				}	
			}
			
			return;
		}
		
		// 队伍内家族成员数据
		Map<Long, List<Long>> familyNumMap = new HashMap<Long, List<Long>>();
		
		for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
			TeamPlayer tp = entry.getValue();
			
			PlayerFamily tplayerFamily = familyService.getPlayerFamily(tp.getPlayerId());
			if(tplayerFamily == null || tplayerFamily.getPlayerFamilyId() < 1) continue;
			
			List<Long> playerIds = familyNumMap.get(tplayerFamily.getPlayerFamilyId());
			if(playerIds == null){
				playerIds = new ArrayList<>();
				familyNumMap.put(tplayerFamily.getPlayerFamilyId(), playerIds);
			}
			
			playerIds.add(tp.getPlayerId());
		}
		
		for(Map.Entry<Long, List<Long>> entry : familyNumMap.entrySet()){
			List<Long> playerIds = entry.getValue();
			if (playerIds.size() > 2){
				// 如果家族内玩家大于2人，添加家族内所有玩家的buff 
				for(Long id : playerIds){
					PlayerExt fplayerExt = playerService.getPlayerExtById(id);
					if(fplayerExt == null || fplayerExt.getTeamId() != teamId) continue;
					
					buffService.addBuffById(id, FAMILY_BUFF);
				}					
			}else{
				// 如果家族内玩家小于三人，则清除家族内所有玩家的buff
				for(Long id : playerIds){
					PlayerPuppet ttPuppet = sceneService.getPlayerPuppet(id);	
					if(ttPuppet == null) continue;						
					buffService.removeBuffById(ttPuppet, FAMILY_BUFF);
				}				
			}
		}			
	}
	
	@Override
	public void clearTeamApplyList(long playerId) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2304);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(!teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			if(!team.getTeamApplyIdList().isEmpty()){
				team.getTeamApplyIdList().clear();
			}				
		}
		
	}

	@Override
	public void autoAgreeApply(long playerId) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2304);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(!teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			if(team.getAutoAgreeApply() == TeamConstant.NOT_AUTO_AGREE_APPLY){
				team.setAutoAgreeApply(TeamConstant.AUTO_AGREE_APPLY);		
		    }else{
		    	team.setAutoAgreeApply(TeamConstant.NOT_AUTO_AGREE_APPLY);	
		    }
			
			S_AutoAgreeApply.Builder builder = S_AutoAgreeApply.newBuilder();
			builder.setState(team.getAutoAgreeApply());
			MessageObj msg = new MessageObj(MessageID.S_AutoAgreeApply_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);	
		}		
	}

	@Override
	public void getCaptainPostion(long playerId) throws Exception {
		if(playerId <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.TEAM)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTeamId() <= 0) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Team team = this.getTeam(playerExt.getTeamId());				
			if(team == null)  throw new GameException(ExceptionConstant.TEAM_2304);
			
			TeamPlayer teamPlayer = team.getTeamPlayerMap().get(playerId);
			if(teamPlayer == null) throw new GameException(ExceptionConstant.TEAM_2304);
			if(teamPlayer.isCaptain()) throw new GameException(ExceptionConstant.TEAM_2307);
			
			for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
				TeamPlayer tPlayer = entry.getValue();
				if(!tPlayer.isCaptain()) continue;
				
				PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(tPlayer.getPlayerId());
				if(playerPuppet == null) return;				
				PlayerExt tPlayerExt = playerService.getPlayerExtById(tPlayer.getPlayerId());
				
				S_GetCaptainPostion.Builder builder = S_GetCaptainPostion.newBuilder();
				builder.setMapId(tPlayerExt.getMapId());
				builder.setPosition(protoBuilderService.buildVector3Msg(playerPuppet.getX(), playerPuppet.getY(),  playerPuppet.getZ()));
			
				MessageObj msg = new MessageObj(MessageID.S_GetCaptainPostion_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);	
			}		
			
		}
	}

}
