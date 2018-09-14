package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.FamilyConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.dao.family.PlayerFamilyDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.chat.Notice;
import com.domain.family.Family;
import com.domain.family.PlayerFamily;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.team.Team;
import com.domain.team.TeamPlayer;
import com.message.ChatProto.ParamType;
import com.message.FamilyProto.FamilyPlayerMsg;
import com.message.FamilyProto.S_CreateFamily;
import com.message.FamilyProto.S_DisbandFamily;
import com.message.FamilyProto.S_ExitFamily;
import com.message.FamilyProto.S_KickFamilyPlayer;
import com.message.FamilyProto.S_SynFamilyInfo;
import com.message.FamilyProto.S_SynInviteJoinFamily;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IChatService;
import com.service.IFamilyService;
import com.service.IFriendService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ITeamService;
import com.util.ComparatorUtil;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.PlayerUtil;
import com.util.ResourceUtil;


/**
 * 家族系统
 * @author jiangqin
 * @date 2017-4-6
 */
public class FamilyService implements IFamilyService{
	
	private PlayerFamilyDAO playerFamilyDAO = new PlayerFamilyDAO();
	
	@Override
	public void initCache() {
		Map<Long, Family> map = new ConcurrentHashMap<Long, Family>();
		List<Family> lists= playerFamilyDAO.listFamilys();
		for(Family model : lists){
			List<PlayerFamily> lists1 = playerFamilyDAO.listPlayerFamily(model.getPlayerFamilyId());
			
			for(PlayerFamily pf : lists1){
				model.getPlayerMap().put(pf.getPlayerId(), pf);
			}
			
			map.put(model.getPlayerFamilyId(), model);
		}
		CacheService.putToCache(CacheConstant.FAMILY, map);
	}
	
	/**
	 * 家族列表
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, Family> getFamilyMap(){
		return (Map<Long, Family>)CacheService.getFromCache(CacheConstant.FAMILY);
	}
	
	
	/**
	 * 家族信息
	 */
	public Family getFamily(long playerFamilyId){		
		return  this.getFamilyMap().get(playerFamilyId);
	}
	
	/** 根据玩家角色ID获取角色家族信息 */
	public PlayerFamily getPlayerFamily(long playerId){		
		PlayerFamily model = (PlayerFamily)CacheService.getFromCache(CacheConstant.PLAYER_FAMILY+playerId);		
		if(model == null){
			model = playerFamilyDAO.getPlayerFamily(playerId);
			if(model != null){
				CacheService.putToCache(CacheConstant.PLAYER_FAMILY+playerId, model);
			}
		}
					
		return model;
	}
	
	
	@Override
	public void getFamilyData(long playerId) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);
			if(playerFamily.getPlayerFamilyId() < 1) {
				throw new GameException(ExceptionConstant.FAMILY_2601);
			}		
			
			this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), playerId);
		}
	}	

	@Override
	public void createFamily(long playerId, String familyName) throws Exception {
		if(playerId < 1 || familyName == null || familyName.equals("")) throw new GameException(ExceptionConstant.ERROR_10000);
		
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ITeamService teamService = serviceCollection.getTeamService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			int CREATE_FAMILY_EXPEND_GOLD = serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_EXPEND_GOLD);
			
			// 创建条件限制
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if(playerWealth.getGold() < CREATE_FAMILY_EXPEND_GOLD) throw new GameException(ExceptionConstant.PLAYER_1112);
			if(this.isHaveFamilyName(familyName)) throw new GameException(ExceptionConstant.FAMILY_2604);
			if(familyName.length() > 14) throw new GameException(ExceptionConstant.FAMILY_2609);
			
			// 获取玩家队伍
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);		
			Team team = teamService.getTeam(playerExt.getTeamId());
			if (team == null) throw new GameException(ExceptionConstant.TEAM_2301);
			
			Map<Long, TeamPlayer> teamPlayerMap = team.getTeamPlayerMap();
			if(!teamPlayerMap.get(playerId).isCaptain()) throw new GameException(ExceptionConstant.TEAM_2302);
			
			int CREATE_FAMILY_PEOPLE = serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_PEOPLE);
	        if(teamPlayerMap.size() < CREATE_FAMILY_PEOPLE) throw new GameException(ExceptionConstant.FAMILY_2610);
			
	        List<Long> playerIds = new ArrayList<>();	        
			for(Long id : teamPlayerMap.keySet()){	
				if(!gameSocketService.checkOnLine(id)) throw new GameException(ExceptionConstant.PLAYER_1111);
				
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(id);
				int CREATE_FAMILY_LEVEL = serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
				
				if(playerProperty.getLevel() < CREATE_FAMILY_LEVEL) throw new GameException(ExceptionConstant.PLAYER_1110);
				PlayerFamily pf = this.getPlayerFamily(id);
				if (pf.getPlayerFamilyId() > 0) throw new GameException(ExceptionConstant.FAMILY_2603);			
				
				//if(!id.equals(playerId) && !friendService.isFriend(playerId, id))throw new GameException(ExceptionConstant.FAMILY_2611);	
				
				playerIds.add(id);
			}
			
			Family family = new Family();
			family.setPlayerFamilyId(IDUtil.geneteId(Family.class));
			family.setFamilyName(familyName);
			family.setFamilyCreateTime(System.currentTimeMillis());		
			playerFamilyDAO.createFamily(family);	
			
			// 消耗金币
			playerService.addGold_syn(playerId, -CREATE_FAMILY_EXPEND_GOLD);	
			
	        List<PlayerFamily> playerFamilyList = new ArrayList<PlayerFamily>();
			for(Long id : playerIds){			
				PlayerFamily pf = this.getPlayerFamily(id);
				
				if(id.equals(playerId)){
					pf.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_2);
					pf.setPosition(101);
				}else{	
					PlayerProperty playerProperty = playerService.getPlayerPropertyById(id);
					pf.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_1);	
					pf.setPosition(playerProperty.getLevel());
				}			
			
				pf.setFamilyTitle(familyName);			
				playerFamilyList.add(pf);	
				
				//触发任务
				GameContext.getInstance().getServiceCollection().getTaskService().executeTask(id, TaskConstant.TYPE_18, null);
			}
			
			// 排序
			Collections.sort(playerFamilyList, new ComparatorUtil());		
			int sortId = 1;
			for(PlayerFamily model:playerFamilyList){
				model.setFamilySortId(sortId);
				model.setPlayerFamilyId(family.getPlayerFamilyId());
				this.updatePlayerFamily(model);	
				
				family.getPlayerMap().put(model.getPlayerId(), model);
				
				sortId++;
			}
			
			this.getFamilyMap().put(family.getPlayerFamilyId(), family);	
			
			S_CreateFamily.Builder builder = S_CreateFamily.newBuilder();
			MessageObj msg = new MessageObj(MessageID.S_CreateFamily_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);

			// 同步所有玩家家族信息
			this.getSynFamilyInfo(family.getPlayerFamilyId(), 0);
			
			// 创建家族广播
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PARAM, 0, 0, family.getFamilyName());
			paramList.add(notice1);			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_6, paramList, playerIds);
						
			// 判断是否触发家族buff
			teamService.checkFamilyBuff(playerId, team.getTeamId());
		}
	}
	
	@Override
	public void disbandFamily(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ITeamService teamService = serviceCollection.getTeamService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IMailService mailService = serviceCollection.getMailService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);
			if (playerFamily.getPlayerFamilyId() < 0) throw new GameException(ExceptionConstant.FAMILY_2601);		
			if (playerFamily.getFamilyPosId() != FamilyConstant.PLAYER_FAMILY_2) throw new GameException(ExceptionConstant.FAMILY_2602);
			
			// 获取家族内所有玩家信息		
			List<Long> playerIds = new ArrayList<Long>();		
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());		
			for(Map.Entry<Long, PlayerFamily> entry : family.getPlayerMap().entrySet()){	
				PlayerFamily model = entry.getValue();
				model.reset();
				this.updatePlayerFamily(model);				
				playerIds.add(model.getPlayerId());
			}	
			
			family.getPlayerMap().clear();
			family.setDeleteFlag(1);
			this.updateFamily(family);		
			this.getFamilyMap().remove(family.getPlayerFamilyId());
			
			// 同步家族内所有玩家解散信息
			for(Long id : playerIds){				
				// 同步家族内所有玩家解散信息
				if(gameSocketService.checkOnLine(id)){
					S_DisbandFamily.Builder builder = S_DisbandFamily.newBuilder();		
					MessageObj msg = new MessageObj(MessageID.S_DisbandFamily_VALUE, builder.build().toByteArray());		
					gameSocketService.sendDataToPlayerByPlayerId(id, msg);
				}
				
				mailService.systemSendMail(id, ResourceUtil.getValue("family_disband_1"), ResourceUtil.getValue("family_disband", DateService.dateFormatYMD(new Date())), "", 0);
				
				// 同步家族称谓信息				
				playerService.synPlayerTitle(id, PlayerConstant.PLAYER_TITLE_TYPE_1, 0, "");
				
				// 判断是否触发家族buff		
				PlayerExt playerExt = playerService.getPlayerExtById(playerId);
				teamService.checkFamilyBuff(id, playerExt.getTeamId());
			}	
		}				
	}
	
	@Override
	public void inviteJoinFamily(long playerId, long friendId) throws Exception {	
		if(playerId < 1 || friendId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);
			if (playerFamily.getPlayerFamilyId() < 0) throw new GameException(ExceptionConstant.FAMILY_2601);		
			if (playerFamily.getFamilyPosId() != FamilyConstant.PLAYER_FAMILY_2) throw new GameException(ExceptionConstant.FAMILY_2602);
	
			PlayerFamily friendFamily = this.getPlayerFamily(friendId);
			if (friendFamily.getPlayerFamilyId() > 0) throw new GameException(ExceptionConstant.FAMILY_2603);	
			
			// 判断是否为好友
			IFriendService friendService = serviceCollection.getFriendService();
			if(!friendService.isFriend(playerId, friendId)) throw new GameException(ExceptionConstant.FAMILY_2613);
			
			// 判断好友是否在线
			if(!gameSocketService.checkOnLine(friendId)) throw new GameException(ExceptionConstant.PLAYER_1111);
					
			Player player = playerService.getPlayerByID(playerId);
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());		
			
			S_SynInviteJoinFamily.Builder builder = S_SynInviteJoinFamily.newBuilder();
			builder.setFamilyName(family.getFamilyName());
			builder.setPlayerFamilyId(playerFamily.getPlayerFamilyId());
			builder.setPlayerId(playerId);
			builder.setPlayerName(player.getPlayerName());
			
			MessageObj msgSyn = new MessageObj(MessageID.S_SynInviteJoinFamily_VALUE, builder.build().toByteArray());	
			gameSocketService.sendDataToPlayerByPlayerId(friendId, msgSyn);	
			
			// 邀请公告
			IChatService chatService = serviceCollection.getChatService();
			Notice noticeFamily = new Notice(ParamType.FAMILY, family.getPlayerFamilyId(), 0, family.getFamilyName());							
			Notice noticeFamily1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());							
			List<Notice> paramList = new ArrayList<Notice>();
			paramList.add(noticeFamily1);
			paramList.add(noticeFamily);
			
			List<Long> playerIds = new ArrayList<>();
			playerIds.add(friendId);
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_24, paramList, playerIds);
		}
	}
	
	@Override
	public void exitFamily(Long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ITeamService teamService = serviceCollection.getTeamService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);
			if (playerFamily.getFamilyPosId() == FamilyConstant.PLAYER_FAMILY_2) throw new GameException(ExceptionConstant.FAMILY_2600);
			
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());
			if(family == null) return;
			
			synchronized (family.getLock()) {
				family.getPlayerMap().remove(playerId);			
				
				// 改变排序  
				for(Map.Entry<Long, PlayerFamily> entry : family.getPlayerMap().entrySet()){
					PlayerFamily pf = entry.getValue();
					if(pf.getFamilySortId() <= playerFamily.getFamilySortId()) continue;
					pf.setFamilySortId(pf.getFamilySortId() - 1);
					
					this.updatePlayerFamily(pf);
				}
				
				playerFamily.reset();			
				this.updatePlayerFamily(playerFamily);	
				
				S_ExitFamily.Builder builder = S_ExitFamily.newBuilder();		
				MessageObj msg = new MessageObj(MessageID.S_ExitFamily_VALUE, builder.build().toByteArray());		
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);	
				
				// 同步家族称谓信息
				playerService.synPlayerTitle(playerId, PlayerConstant.PLAYER_TITLE_TYPE_1, 0, "");
				
				// 同步其他成员		
				List<Long> playerIds = this.getSynFamilyInfo(family.getPlayerFamilyId(), 0);
				
				// 公告离开家族
				IChatService chatService = serviceCollection.getChatService();
				Player player = playerService.getPlayerByID(playerId);	
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
				paramList.add(notice1);			
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_9, paramList, playerIds);
			
				// 判断是否触发家族buff
				int teamId = 0;
				PlayerExt playerExt = playerService.getPlayerExtById(playerId);
				if(playerExt != null) teamId = playerExt.getTeamId();
				teamService.checkFamilyBuff(playerId, teamId);
			}

		}
	}
	
	@Override
	public void changeFamilyLeader(long playerId, long targetPlayerId) throws Exception {
		if(playerId < 1 || targetPlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);		
			if (playerFamily.getFamilyPosId() != FamilyConstant.PLAYER_FAMILY_2){
				throw new GameException(ExceptionConstant.FAMILY_2602);
			}
			
			PlayerFamily targetPlayerFamily = this.getPlayerFamily(targetPlayerId);		
			if(targetPlayerFamily != null && playerFamily.getPlayerFamilyId() != targetPlayerFamily.getPlayerFamilyId()){
				throw new GameException(ExceptionConstant.FAMILY_2606);	
			}
			
			// 判断目标玩家是否在线
			if(!serviceCollection.getGameSocketService().checkOnLine(targetPlayerId)) throw new GameException(ExceptionConstant.PLAYER_1111);
				
			playerFamily.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_1);
			playerFamily.setFamilySortId(targetPlayerFamily.getFamilySortId());
			playerFamily.setFamilyTitle(targetPlayerFamily.getFamilyTitle());
			this.updatePlayerFamily(playerFamily);		
			
			targetPlayerFamily.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_2);
			targetPlayerFamily.setFamilySortId(1);		
			targetPlayerFamily.setFamilyTitle(ResourceUtil.getValue("family_leader_title"));
			this.updatePlayerFamily(targetPlayerFamily);
			
			// 更新家族玩家信息
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());
			family.getPlayerMap().put(playerId, playerFamily);
			family.getPlayerMap().put(targetPlayerId, targetPlayerFamily);		
			
			// 同步所有玩家家族信息
			List<Long> playerIds = this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);
	
			// 飘字		
			Player player = playerService.getPlayerByID(targetPlayerId);		
			for(Long id : playerIds){			
				serviceCollection.getCommonService().sendNoticeMsg(id, ResourceUtil.getValue("family_leader", player.getPlayerName()));		
			}	
			
			// 成为族长
			IChatService chatService = serviceCollection.getChatService();
			Player targetPlayer = playerService.getPlayerByID(targetPlayerId);	
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, targetPlayerId, 0, targetPlayer.getPlayerName());
			paramList.add(notice1);			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_7, paramList, playerIds);
		}
	}
	
	@Override
	public void changeFamilySortId(long playerId, List<Integer> sortList) throws Exception {
		if(playerId < 1 || sortList == null || sortList.isEmpty()) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {			
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);	
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);			
			if(playerFamily.getFamilyPosId()  != FamilyConstant.PLAYER_FAMILY_2)throw new GameException(ExceptionConstant.FAMILY_2602);
			
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());			
			List<PlayerFamily> pflist = new ArrayList<>();			
			for(int index = 1; index <= sortList.size(); index++){
				int sortId = sortList.get(index-1);					
				for(Map.Entry<Long, PlayerFamily> entry : family.getPlayerMap().entrySet()){
					PlayerFamily pf = entry.getValue();
					if(pf.getFamilySortId() == sortId){
						pflist.add(pf);
						break;
					}					
				}				
			}			
			
			
			if(pflist == null || pflist.isEmpty()) return;	
			
			for(int i = 0; i < pflist.size(); i++){
				PlayerFamily pfmodel = pflist.get(i);
				pfmodel.setFamilySortId(i+1);
				
				this.updatePlayerFamily(pfmodel);
			}			
		
			// 同步家族信息
			this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);
		}
	}
	
	@Override
	public void changeFamilyNotice(long playerId, String msg) throws Exception {
		if(playerId < 1 || msg == null || msg.equals("")) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);	
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);		
				
			if(playerFamily.getFamilyPosId()  != FamilyConstant.PLAYER_FAMILY_2){
				throw new GameException(ExceptionConstant.FAMILY_2602);
			}
			
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());
			family.setFamilyNotice(msg);		
			this.updateFamily(family);		
			
			// 同步家族信息
			this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);
		}
	}
	
	@Override
	public void kickFamilyPlayer(long playerId, Long targetPlayerId) throws Exception {
		if(playerId < 1 || targetPlayerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ITeamService teamService = serviceCollection.getTeamService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);	
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);
			if (playerFamily.getFamilyPosId()  != FamilyConstant.PLAYER_FAMILY_2) throw new GameException(ExceptionConstant.FAMILY_2602);
			
			PlayerFamily targetplayerFamily = this.getPlayerFamily(targetPlayerId);
			if (playerFamily.getPlayerFamilyId() != targetplayerFamily.getPlayerFamilyId()) throw new GameException(ExceptionConstant.FAMILY_2606);	
			if (targetplayerFamily.getFamilyPosId() == FamilyConstant.PLAYER_FAMILY_2) throw new GameException(ExceptionConstant.FAMILY_2600);
		
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());
			
			synchronized (family.getLock()) {
				family.getPlayerMap().remove(targetPlayerId);
				
				// 改变排序  
				for(Map.Entry<Long, PlayerFamily> entry : family.getPlayerMap().entrySet()){
					PlayerFamily pf = entry.getValue();
					if(pf.getFamilySortId() <= targetplayerFamily.getFamilySortId()) continue;
					pf.setFamilySortId(pf.getFamilySortId() - 1);
					
					this.updatePlayerFamily(pf);
				}
				
				targetplayerFamily.reset();
				this.updatePlayerFamily(targetplayerFamily);	
				
				
				S_KickFamilyPlayer.Builder builder = S_KickFamilyPlayer.newBuilder();		
				MessageObj msg = new MessageObj(MessageID.S_KickFamilyPlayer_VALUE, builder.build().toByteArray());		
				gameSocketService.sendDataToPlayerByPlayerId(targetPlayerId, msg);		
				
				// 同步
				List<Long>  playerIds = this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);		
				
				// 同步家族称谓信息				
				playerService.synPlayerTitle(targetPlayerId, PlayerConstant.PLAYER_TITLE_TYPE_1, 0, "");
				
				// 判断是否触发家族buff
				PlayerExt playerExt = playerService.getPlayerExtById(targetPlayerId);	
				teamService.checkFamilyBuff(targetPlayerId, playerExt.getTeamId());
				
				//公告
				Player targetPlayer = playerService.getPlayerByID(targetPlayerId);			
				IChatService chatService = serviceCollection.getChatService();
				Notice noticePlayer = new Notice(ParamType.PLAYER, targetPlayerId, 0, targetPlayer.getPlayerName());
				List<Notice> paramList = new ArrayList<Notice>();
				paramList.add(noticePlayer);
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_26, paramList, playerIds);
			}

		}
	}
	
	@Override
	public void inviteMsgDeal(long playerId, long playerFamilyId, int state) throws Exception {
		if(playerId < 1 || playerFamilyId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		// 拒绝邀请
		if (state == 0) return;	
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IFriendService friendService = serviceCollection.getFriendService();
		IPlayerService playerService = serviceCollection.getPlayerService();	
		ITeamService teamService = serviceCollection.getTeamService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {

		
			Family family = this.getFamily(playerFamilyId);
			if (family == null) throw new GameException(ExceptionConstant.FAMILY_2608);
			
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);
			if (playerFamily.getPlayerFamilyId() > 0) throw new GameException(ExceptionConstant.FAMILY_2603);
			
			int FAMILY_MAX_PEOPLE = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FAMILY_MAX_PEOPLE);
			
			int playerSize = family.getPlayerMap().size();	
			if (playerSize >= FAMILY_MAX_PEOPLE) throw new GameException(ExceptionConstant.FAMILY_2605);			
			
			// 等级判断
			int CREATE_FAMILY_LEVEL = serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if(playerProperty.getLevel() < CREATE_FAMILY_LEVEL) throw new GameException(ExceptionConstant.PLAYER_1110);		
				
			playerFamily.setPlayerId(playerId);
			playerFamily.setPlayerFamilyId(playerFamilyId);
			playerFamily.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_1);
			playerFamily.setFamilySortId(family.getPlayerMap().size() + 1);
			playerFamily.setFamilyTitle(family.getFamilyName());
			this.updatePlayerFamily(playerFamily);
			
			family.getPlayerMap().put(playerId, playerFamily);
			
			// 同步
			List<Long> playerIds = this.getSynFamilyInfo(playerFamilyId, 0);
			
			// 自动添加家族成员为好友
			friendService.autoAddFriend(playerId, playerIds);	
			
			// 飘字		
			Player player = playerService.getPlayerByID(playerId);		
			for(Long id : playerIds){			
				serviceCollection.getCommonService().sendNoticeMsg(id, ResourceUtil.getValue("family_enter", player.getPlayerName()));		
			}	
			
			// 任务触发
			serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_18, null);
			
		
			// 进入家族
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
			paramList.add(notice1);			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_8, paramList, playerIds);
			
			// 是否触发家族buff
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);	
			teamService.checkFamilyBuff(playerId, playerExt.getTeamId());
		}
	}	

	@Override
	public void changeFamilyPlayerTitle(long playerId, long targetPlayerId, String title) throws Exception {
		if(playerId < 1 || targetPlayerId < 1 || title == null || title.equals("")) throw new GameException(ExceptionConstant.ERROR_10000);
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);	
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);
			if(playerFamily.getFamilyPosId()  != FamilyConstant.PLAYER_FAMILY_2) throw new GameException(ExceptionConstant.FAMILY_2602);
			
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());	
			
			PlayerFamily targetplayerFamily = family.getPlayerMap().get(targetPlayerId);
			if(playerFamily.getPlayerFamilyId() != targetplayerFamily.getPlayerFamilyId()) throw new GameException(ExceptionConstant.FAMILY_2606);	
			
			targetplayerFamily.setFamilyTitle(title);		
			this.updatePlayerFamily(targetplayerFamily);	
			
			// 同步	
			this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);
		}
	}	
	
	/**
	 * 同步缓存更新
	 */	
	private void updatePlayerFamily(PlayerFamily playerFamily){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_FAMILY);
		if (!lists.contains(playerFamily)) {
			lists.add(playerFamily);
		}
		
		CacheService.putToCache(CacheConstant.PLAYER_FAMILY+playerFamily.getPlayerId(), playerFamily);
	}	
	
	/**
	 * 同步缓存更新
	 */	
	private void updateFamily(Family family){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.FAMILY);
		if (!lists.contains(family)) {
			lists.add(family);
		}
	}		
	
	
	/** 根据家族唯一ID获取家族成员*/
	public List<Long> getFamilyPlayerIds(long playerFamilyId){	
		Family family = this.getFamilyMap().get(playerFamilyId);
		if (family == null) return null;
		
		Map<Long, PlayerFamily> playerFamilyMap = family.getPlayerMap();		
		List<Long> playerIds = new ArrayList<Long>();
		for(Map.Entry<Long, PlayerFamily> entry : playerFamilyMap.entrySet()){		
			playerIds.add(entry.getKey());
		}
		return playerIds;
	}		
	
	
	/** 家族名字是否重复 */
	private boolean isHaveFamilyName(String familyName){		
		Map<Long, Family> familyMap = this.getFamilyMap();	
		for(Map.Entry<Long, Family> entry : familyMap.entrySet()){
			Family model = entry.getValue();			
			if(model.getFamilyName().equals(familyName)) {
				return true;
			}
		}		
		return false;		
	}	
	
	
	/** 组装家族同步信息 */
	private List<Long> getSynFamilyInfo(long playerFamilyId, long playerId){		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Family family = this.getFamily(playerFamilyId);	
		
		if(family == null){
//			System.out.println("family is null id is " + playerFamilyId);	
			return null;
		}
		
		S_SynFamilyInfo.Builder builder = S_SynFamilyInfo.newBuilder();
		builder.setPlayerFamilyId(playerFamilyId);
		builder.setFamilyName(family.getFamilyName());
		if(family.getFamilyNotice() == null){
			builder.setFamilyNotice("");
		}else{
			builder.setFamilyNotice(family.getFamilyNotice());
		}
		
	   
		if(playerId > 0){
			MessageObj msg = new MessageObj(MessageID.S_SynFamilyInfo_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
			return null;
		}else{
			List<Long> playerIds = new ArrayList<Long>();
			for(Map.Entry<Long, PlayerFamily> entry : family.getPlayerMap().entrySet()){
				FamilyPlayerMsg.Builder msg = protoBuilderService.buildFamilyPlayerMsg(entry.getValue());
				
				if (msg == null) continue;
				builder.addListFamilyPlayer(msg);
				
				playerIds.add(entry.getKey());
				
				// 同步家族称谓信息
				playerService.synPlayerTitle(entry.getKey(), PlayerConstant.PLAYER_TITLE_TYPE_1, entry.getValue().getFamilySortId(), family.getFamilyName());
			}
			
			MessageObj msg = new MessageObj(MessageID.S_SynFamilyInfo_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);
			return playerIds;
		}
	
	}
	
	@Override
	public void dealExit(long playerId) {	
		PlayerFamily playerFamily = this.getPlayerFamily(playerId);
		if(playerFamily == null || playerFamily.getPlayerFamilyId() < 1) return;
		
		this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);	
	}	

	@Override
	public void dealLogin(long playerId){		
		PlayerFamily playerFamily = this.getPlayerFamily(playerId);
		if(playerFamily == null || playerFamily.getPlayerFamilyId() < 1) return;		
		
		this.getSynFamilyInfo(playerFamily.getPlayerFamilyId(), 0);
	}

	@Override
	public void quartzDaily() {
		playerFamilyDAO.quartzDelete();
		Map<Long, Family> familyMap = this.getFamilyMap();
		for(Map.Entry<Long, Family> entry : familyMap.entrySet()){
			Family family = entry.getValue();
			family.setOpenFB(0);
		}
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_FAMILY+playerId);		
	}

	
	@Override
	public void quartzDealFamily() {
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IMailService mailService = serviceCollection.getMailService();
		
		try {
			//先同步一下缓存到数据库
			Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_FAMILY);
			if(lists != null && !lists.isEmpty()){
				serviceCollection.getBatchExcuteService().batchUpdate(lists);
			}
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		List<Family> removeList = new ArrayList<Family>();
		Map<Long, Family> familyMap = this.getFamilyMap();
		
		long curTime = System.currentTimeMillis();
		for(Map.Entry<Long, Family> entry : familyMap.entrySet()){
			Family family = entry.getValue();
			
			if(family == null || family.getDeleteFlag() == 1) return;
			
			// 判断所有玩家是否离线超过三天
			boolean bDisband = true;
			Map<Long, PlayerFamily> playerMap = family.getPlayerMap();
			for(Long pfId : playerMap.keySet()){
				PlayerExt playerExt = playerService.getPlayerExtById(pfId);
				if(playerExt == null) continue;
				
				if(gameSocketService.checkOnLine(pfId)) {
					bDisband = false;
					break;
				}
				
				int FAMILY_ALL_EXIT_TIME = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FAMILY_ALL_EXIT_TIME);
				if (DateService.difTime(playerExt.getExitTime()) < FAMILY_ALL_EXIT_TIME) { 
					bDisband = false;
					break;
				}	
			}
			
			if(bDisband){
				removeList.add(family);
			}else{
				//若族长一段时间不上线，则自动解除族长之职，由第二个成员成为族长，原族长则排到最后位置并得到现族长之前的称号
				boolean flag = this.autoChangeFamilyLeader(family, playerMap);
				if(!flag){
					removeList.add(family);
				}else{
					//若成员少于3人，则家族进入濒临解散状态，若在一段时间内人数无法补充到3人或以上，则家族自动解散
					int FAMILY_NUMBER_LESS_THAN = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FAMILY_NUMBER_LESS_THAN);
					if(playerMap.size() >= FAMILY_NUMBER_LESS_THAN){
						if(family.getFamilyDisbandTime() > 0){
							family.setFamilyDisbandTime(0);
							this.updateFamily(family);
						}
					}else{
						if(family.getFamilyDisbandTime() > 0){
							int FAMILY_CLOSE_TO_DESIVE = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FAMILY_CLOSE_TO_DESIVE);
							if(DateService.difTime(new Date(family.getFamilyDisbandTime())) >= FAMILY_CLOSE_TO_DESIVE){ 
								//解散
								removeList.add(family);
							}
						}else{
							family.setFamilyDisbandTime(curTime);
							this.updateFamily(family);
							
							// 濒临解散状态发邮件
							for(Map.Entry<Long, PlayerFamily> entry1 : playerMap.entrySet()){
								mailService.systemSendMail(entry1.getKey(), ResourceUtil.getValue("family_disband_1"), ResourceUtil.getValue("family_disband_2"), "", 0);
							}	
						}
					}
				}

			}
		}
		
		if(!removeList.isEmpty()){
			StringBuilder sb = new StringBuilder("(");
			
			for(Family model : removeList){
				Map<Long, PlayerFamily> playerMap = model.getPlayerMap();
				for(Map.Entry<Long, PlayerFamily> entry : playerMap.entrySet()){
					PlayerFamily pf = (PlayerFamily)CacheService.getFromCache(CacheConstant.PLAYER_FAMILY+entry.getKey());
					if(pf != null){
						pf.reset();
						
						// 同步家族称谓信息				
						playerService.synPlayerTitle(pf.getPlayerId(), PlayerConstant.PLAYER_TITLE_TYPE_1, 0, "");
						
						// 同步家族内所有玩家解散信息
						if(gameSocketService.checkOnLine(entry.getKey())){
							S_DisbandFamily.Builder builder = S_DisbandFamily.newBuilder();		
							MessageObj msg = new MessageObj(MessageID.S_DisbandFamily_VALUE, builder.build().toByteArray());		
							gameSocketService.sendDataToPlayerByPlayerId(entry.getKey(), msg);
						}
						
						mailService.systemSendMail(entry.getKey(), ResourceUtil.getValue("family_disband_1"), ResourceUtil.getValue("family_disband", DateService.dateFormatYMD(new Date())), "", 0);
					}
				}		
					
			    model.setPlayerMap(null);	
				model.setDeleteFlag(1);	
				familyMap.remove(model.getPlayerFamilyId());
				
				sb.append(model.getPlayerFamilyId());
				sb.append(",");
			}
			
			String sql = sb.substring(0, sb.length() - 1) + ")";
			try {
				playerFamilyDAO.quartzDisbandFamily(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	/** 切换家族族长*/
	private boolean autoChangeFamilyLeader(Family family, Map<Long, PlayerFamily> playerMap){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		// 如果删角色处理正确，则不会出现家族成员为空的情况		
		PlayerFamily leaderPlayerFamily = null;
		for(Map.Entry<Long, PlayerFamily> entry : playerMap.entrySet()){	
			PlayerFamily playerFamily = entry.getValue();
			if(playerFamily.getFamilyPosId() == FamilyConstant.PLAYER_FAMILY_2) {
				leaderPlayerFamily = playerFamily;
				break;
			}
		}
		
		if(leaderPlayerFamily == null){
			return false;
		}
		PlayerExt leaderPlayerExt = playerService.getPlayerExtById(leaderPlayerFamily.getPlayerId());	
		if(leaderPlayerExt == null){
			return false;
		}
		if(gameSocketService.checkOnLine(leaderPlayerFamily.getPlayerId())) {
			return true;
		}
		
		int FAMILY_LEADER_EXIT_TIME = serviceCollection.getCommonService().getConfigValue(ConfigConstant.FAMILY_LEADER_EXIT_TIME);
		// 族长是否离线超过3天	
		if (DateService.difTime(leaderPlayerExt.getExitTime()) < FAMILY_LEADER_EXIT_TIME) return true;	
		
		PlayerFamily newLeader = null;
		for(Map.Entry<Long, PlayerFamily> entry : playerMap.entrySet()){
			PlayerFamily playerFamily = entry.getValue();
			if(playerFamily.getFamilySortId() == 1){
				continue;
			}
			PlayerExt playerExt = playerService.getPlayerExtById(playerFamily.getPlayerId());
			if(playerExt == null){
				LogUtil.error("autoChangeFamilyLeader playerExt is null with is is "+playerFamily.getPlayerId());
				continue;
			}
			
			if(!gameSocketService.checkOnLine(playerFamily.getPlayerId())){			
				if (DateService.difTime(playerExt.getExitTime()) > FAMILY_LEADER_EXIT_TIME) continue;
			}
					
			if(newLeader == null || playerFamily.getFamilySortId() < newLeader.getFamilySortId()){
				newLeader = playerFamily;
			}			
		}
		
		if(newLeader == null) return false;
		
		// 改变排序  
		this.autoChangeLeaderSort(family, playerMap, leaderPlayerFamily, newLeader, false);		
		
		return true;
		
	}

	/**
	 * 自动排序位
	 */
	private void autoChangeLeaderSort(Family family, Map<Long, PlayerFamily> playerMap, PlayerFamily leaderPlayerFamily,
			PlayerFamily newLeader, boolean isDeleteFalg) {
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IChatService chatService = serviceCollection.getChatService();
		
		for(Map.Entry<Long, PlayerFamily> entry : playerMap.entrySet()){
			PlayerFamily pf = entry.getValue();
			if(pf.getFamilySortId() <= newLeader.getFamilySortId()) continue; 
			pf.setFamilySortId(pf.getFamilySortId() - 1);
			
			this.updatePlayerFamily(pf);
		}
		
		String title = newLeader.getFamilyTitle();
		String leaderTitle = leaderPlayerFamily.getFamilyTitle();
		
		if(isDeleteFalg){
			playerMap.remove(leaderPlayerFamily.getPlayerId());
		}else{
			leaderPlayerFamily.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_1);
			leaderPlayerFamily.setFamilySortId(family.getPlayerMap().size());
			leaderPlayerFamily.setFamilyTitle(title);
			this.updatePlayerFamily(leaderPlayerFamily);
		}		
		
		newLeader.setFamilyPosId(FamilyConstant.PLAYER_FAMILY_2);
		newLeader.setFamilySortId(1);
		newLeader.setFamilyTitle(leaderTitle);	
		this.updatePlayerFamily(newLeader);
		
		List<Long> onlineIds = this.getSynFamilyInfo(family.getPlayerFamilyId(), 0);
		
		//公告 成为族长 
		if(!onlineIds.isEmpty()){
			Player targetPlayer = playerService.getPlayerByID(newLeader.getPlayerId());	
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, newLeader.getPlayerId(), 0, targetPlayer.getPlayerName());
			paramList.add(notice1);			
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_7, paramList, onlineIds);
		}
	}

	@Override
	public void familyFB(long playerId, int type) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FAMILY)) {
			PlayerFamily playerFamily = this.getPlayerFamily(playerId);	
			if (playerFamily.getPlayerFamilyId() <= 0) throw new GameException(ExceptionConstant.FAMILY_2601);		
				
			Family family = this.getFamily(playerFamily.getPlayerFamilyId());
			if (family == null) throw new GameException(ExceptionConstant.FAMILY_2608);
			
			if(type == 1){
				if(playerFamily.getFamilyPosId()  != FamilyConstant.PLAYER_FAMILY_2){
					throw new GameException(ExceptionConstant.FAMILY_2602);
				}
				
				if(family.getOpenFB() > 0){
					throw new GameException(ExceptionConstant.FAMILY_2615);
				}
				
				family.setOpenFB(1);
				this.updateFamily(family);
				
				//【{0}】家族开启了【瓦岗寨】副本，请该家族成员前往狩猎
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PARAM, 0, 0, family.getFamilyName());
				paramList.add(notice1);
				serviceCollection.getChatService().synNotice(ChatConstant.CHAT_NOTICE_MAG_56, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
				
			}else{
				if(family.getOpenFB() == 0){
					throw new GameException(ExceptionConstant.FAMILY_2616);
				}
				
				if(family.getOpenFB() == 2){
					throw new GameException(ExceptionConstant.FAMILY_2617);
				}
				
				String sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_FAMILY, family.getPlayerFamilyId());
				serviceCollection.getSceneService().enterScene(playerId, FamilyConstant.MAP_FAMILY_8001, 0, false, sceneGuid, 0);
			}
		}
	}

	@Override
	public void endFamilyFB(long familyId) {
		Family family = this.getFamily(familyId);
		if (family == null) return;
		
		family.setOpenFB(2);
		this.updateFamily(family);
	}	
	
}
